package katsu.ui.view

import com.sun.javafx.collections.ObservableListWrapper
import javafx.beans.property.ReadOnlyBooleanWrapper
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.geometry.Orientation
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.scene.web.HTMLEditor
import katsu.model.Client
import katsu.model.Treatment
import katsu.ui.AddNewClientEvent
import katsu.ui.AddTreatmentEvent
import katsu.ui.ClientAddedEvent
import katsu.ui.ClientDeletedEvent
import katsu.ui.ClientUi
import katsu.ui.ClientUpdatedEvent
import katsu.ui.ClientsReloadedEvent
import katsu.ui.DeleteClientEvent
import katsu.ui.TreatmentAddedEvent
import katsu.ui.UpdateClientEvent
import katsu.ui.toClientUi
import mu.KotlinLogging.logger
import tornadofx.FXEventRegistration
import tornadofx.View
import tornadofx.action
import tornadofx.bindSelected
import tornadofx.button
import tornadofx.enableWhen
import tornadofx.hbox
import tornadofx.hgrow
import tornadofx.htmleditor
import tornadofx.label
import tornadofx.listview
import tornadofx.multiSelect
import tornadofx.selectWhere
import tornadofx.separator
import tornadofx.singleAssign
import tornadofx.textfield
import tornadofx.vbox
import tornadofx.vgrow

class MainView : View() {

    private val logg = logger {}

    private val clients = ObservableListWrapper<ClientUi>(mutableListOf())
    private val treatments = ObservableListWrapper<Treatment>(mutableListOf())
    private val selectedClient = SimpleObjectProperty<ClientUi>()
    private val registrations = mutableListOf<FXEventRegistration>()

    private var clientsList: ListView<ClientUi> by singleAssign()
    private var firstNameField: TextField by singleAssign()
    private var notesField: HTMLEditor by singleAssign()

    init {
        title = "Katsu"
        selectedClient.addListener { _: ObservableValue<out ClientUi?>, _: ClientUi?, newValue: ClientUi? ->
            updateFields(newValue)
        }
        registrations += subscribe<ClientAddedEvent> { event ->
            clients += event.client.toClientUi()
            clientsList.selectWhere { it.id == event.client.id }
            treatments.setAll(event.client.treatments)
        }
        registrations += subscribe<ClientUpdatedEvent> { event ->
            val clientIndex = clients.indexOfFirst { it.id == event.client.id }
            clients[clientIndex] = event.client.toClientUi()
            treatments.setAll(event.client.treatments)
        }
        registrations += subscribe<ClientDeletedEvent> { event ->
            clients.removeIf { it.id == event.clientId }
            treatments.clear()
        }
        registrations += subscribe<ClientsReloadedEvent> { event ->
            clients.setAll(event.clients.map { it.toClientUi() })
        }
        registrations += subscribe<TreatmentAddedEvent> { event ->
            val clientIndex = clients.indexOfFirst { it.id == event.client.id }
            if (clientIndex == -1) {
                println("not found: ${event.client} in clients: $clients")
            }
            clients[clientIndex] = event.client.toClientUi()
            treatments.setAll(event.client.treatments)
            event.treatment // FIXME use it to change treatment UI
        }
    }

    private fun updateFields(client: ClientUi?) {
        firstNameField.text = client?.firstName ?: ""
        notesField.htmlText = client?.notes ?: ""
        treatments.setAll(client?.treatments ?: emptyList())
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
            hbox {
                vgrow = Priority.ALWAYS
                vbox {
                    hgrow = Priority.ALWAYS
                    label("First Name")
                    firstNameField = textfield().apply {
                        hgrow = Priority.ALWAYS
                        id = ViewIds.TEXT_FIRSTNAME
                    }
                }
                vbox {
                    hgrow = Priority.NEVER
                    listview(treatments) {
                        cellFormat {
                            text = it.dateFormatted
                        }
                    }
                    button("Add") {
                        action {
                            fire(AddTreatmentEvent(selectedClient.get().toClient()))
                        }
                    }
                    button("Delete") {
                        enableWhen(ReadOnlyBooleanWrapper(false))
                    }
                }
            }


            label("Notes")
            notesField = htmleditor().apply {
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
        notes = notesField.htmlText
    )
}
