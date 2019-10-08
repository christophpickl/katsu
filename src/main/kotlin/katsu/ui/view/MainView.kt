package katsu.ui.view

import com.sun.javafx.collections.ObservableListWrapper
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.geometry.Orientation
import javafx.scene.control.ListView
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import katsu.model.Client
import katsu.ui.AddNewClientEvent
import katsu.ui.ClientAddedEvent
import katsu.ui.ClientData
import katsu.ui.ClientDeletedEvent
import katsu.ui.ClientUpdatedEvent
import katsu.ui.ClientsReloadedEvent
import katsu.ui.DeleteClientEvent
import katsu.ui.UpdateClientEvent
import katsu.ui.toClientData
import mu.KotlinLogging.logger
import tornadofx.FXEventRegistration
import tornadofx.View
import tornadofx.action
import tornadofx.bindSelected
import tornadofx.button
import tornadofx.enableWhen
import tornadofx.hbox
import tornadofx.hgrow
import tornadofx.label
import tornadofx.listview
import tornadofx.multiSelect
import tornadofx.selectWhere
import tornadofx.separator
import tornadofx.singleAssign
import tornadofx.textarea
import tornadofx.textfield
import tornadofx.vbox
import tornadofx.vgrow

class MainView : View() {

    private val logg = logger {}

    private val clients = ObservableListWrapper<ClientData>(mutableListOf())
    private val selectedClient = SimpleObjectProperty<ClientData>()
    private val registrations = mutableListOf<FXEventRegistration>()

    private var clientsList: ListView<ClientData> by singleAssign()
    private var firstNameField: TextField by singleAssign()
    private var notesField: TextArea by singleAssign()

    init {
        title = "Katsu"
        selectedClient.addListener { _: ObservableValue<out ClientData?>, _: ClientData?, newValue: ClientData? ->
            updateFields(newValue)
        }
        registrations += subscribe<ClientAddedEvent> { event ->
            clients += event.client.toClientData()
            clientsList.selectWhere { it.id == event.client.id }
        }
        registrations += subscribe<ClientUpdatedEvent> { event ->
            val clientIndex = clients.indexOfFirst { it.id == event.client.id }
            clients[clientIndex] = event.client.toClientData()
        }
        registrations += subscribe<ClientDeletedEvent> { event ->
            clients.removeIf { it.id == event.clientId }
        }
        registrations += subscribe<ClientsReloadedEvent> { event ->
            clients.setAll(event.clients.map { it.toClientData() })
        }
    }

    private fun updateFields(client: ClientData?) {
        firstNameField.text = client?.firstName ?: ""
        notesField.text = client?.notes ?: ""
    }

    fun unsubscribeAll() {
        registrations.forEach { it.unsubscribe() }
    }

    override val root = hbox {
        vbox {
            clientsList = listview(clients) {
                vgrow = Priority.ALWAYS
                id = ViewIds.LIST_CLIENTS
                multiSelect(false)
                bindSelected(selectedClient)
                cellFormat {
                    text = it.firstName
                }
            }
        }
        vbox {
            hgrow = Priority.ALWAYS
            label("First Name")
            firstNameField = textfield().apply {
                hgrow = Priority.ALWAYS
                id = ViewIds.TEXT_FIRSTNAME
            }
            label("Notes")
            notesField = textarea().apply {
                id = ViewIds.TEXT_NOTES
                hgrow = Priority.ALWAYS
                vgrow = Priority.ALWAYS
            }

            separator(Orientation.HORIZONTAL)

            hbox {
                button("New").apply {
                    id = ViewIds.BUTTON_NEW_CLIENT
                    action {
                        logg.debug { "Add new client button clicked" }
                        fire(AddNewClientEvent)
                        firstNameField.requestFocus()
                    }
                }
                button("Save").apply {
                    id = ViewIds.BUTTON_SAVE_CLIENT
                    enableWhen { selectedClient.isNotNull }
                    action {
                        val currentClient = selectedClient.get().toClient().updateByView()
                        fire(UpdateClientEvent(currentClient))
                    }
                }
                button("Delete").apply {
                    id = ViewIds.BUTTON_DELETE_CLIENT
                    enableWhen { selectedClient.isNotNull }
                    action {
                        fire(DeleteClientEvent(selectedClient.get().id))
                    }
                }
            }
        }
    }

    private fun Client.updateByView() = copy(
        firstName = firstNameField.text,
        notes = notesField.text
    )
}
