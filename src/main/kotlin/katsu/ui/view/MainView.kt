package katsu.ui.view

import com.sun.javafx.collections.ObservableListWrapper
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.geometry.Orientation
import javafx.scene.control.ListView
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import katsu.model.Client
import katsu.ui.AddNewClientEvent
import katsu.ui.ClientAdded
import katsu.ui.ClientData
import katsu.ui.ClientDeleted
import katsu.ui.ClientUpdated
import katsu.ui.ClientsReloaded
import katsu.ui.DeleteClient
import katsu.ui.UpdateClient
import katsu.ui.toClientData
import mu.KotlinLogging.logger
import tornadofx.FXEventRegistration
import tornadofx.View
import tornadofx.action
import tornadofx.bindSelected
import tornadofx.button
import tornadofx.enableWhen
import tornadofx.hbox
import tornadofx.label
import tornadofx.listview
import tornadofx.multiSelect
import tornadofx.selectWhere
import tornadofx.separator
import tornadofx.singleAssign
import tornadofx.textarea
import tornadofx.textfield
import tornadofx.vbox

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
            firstNameField.text = newValue?.firstName ?: ""
        }
        registrations += subscribe<ClientAdded> { event ->
            clients += event.client.toClientData()
            clientsList.selectWhere { it.id == event.client.id }
        }
        registrations += subscribe<ClientUpdated> { event ->
            val clientIndex = clients.indexOfFirst { it.id == event.client.id }
            clients[clientIndex] = event.client.toClientData()
        }
        registrations += subscribe<ClientDeleted> { event ->
            clients.removeIf { it.id == event.clientId }
        }
        registrations += subscribe<ClientsReloaded> {
            clients.setAll(it.clients.map { it.toClientData() })
        }
    }

    fun unsubscribeAll() {
        registrations.forEach { it.unsubscribe() }
    }

    override val root = hbox {
        vbox {
            clientsList = listview(clients) {
                id = ViewIds.LIST_CLIENTS
                multiSelect(false)
                bindSelected(selectedClient)
                cellFormat {
                    text = it.firstName
                }
            }
        }
        vbox {
            label("First Name")
            firstNameField = textfield().apply {
                id = ViewIds.TEXT_FIRSTNAME
            }
            label("Notes")
            notesField = textarea().apply {
                id = ViewIds.TEXT_NOTES
            }
            separator(Orientation.HORIZONTAL)

            button("Add new").apply {
                id = ViewIds.BUTTON_ADD_CLIENT
                action {
                    logg.debug { "Add new client button clicked" }
                    fire(AddNewClientEvent)
                }
            }
            button("save").apply {
                enableWhen { selectedClient.isNotNull }
                action {
                    val currentClient = selectedClient.get().toClient().updateByView()
                    fire(UpdateClient(currentClient))
                }
            }
            button("delete").apply {
                enableWhen { selectedClient.isNotNull }
                action {
                    fire(DeleteClient(selectedClient.get().id))
                }
            }
        }
    }

    private fun Client.updateByView() = copy(
        firstName = firstNameField.text
    )
}
