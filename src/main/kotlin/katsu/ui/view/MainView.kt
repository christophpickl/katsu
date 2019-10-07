package katsu.ui.view

import com.sun.javafx.collections.ObservableListWrapper
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.geometry.Orientation
import javafx.scene.control.TextField
import katsu.model.Client
import katsu.ui.AddNewClientEvent
import katsu.ui.ClientAdded
import katsu.ui.ClientData
import katsu.ui.ClientDeleted
import katsu.ui.ClientUpdated
import katsu.ui.DeleteClient
import katsu.ui.UpdateClient
import katsu.ui.appKodein
import katsu.ui.controller.MainController
import katsu.ui.toClientData
import mu.KotlinLogging.logger
import org.kodein.di.generic.instance
import tornadofx.View
import tornadofx.action
import tornadofx.bindSelected
import tornadofx.button
import tornadofx.enableWhen
import tornadofx.hbox
import tornadofx.label
import tornadofx.listview
import tornadofx.separator
import tornadofx.singleAssign
import tornadofx.textfield
import tornadofx.vbox

class MainView : View() {

    private val logg = logger {}
    private val controller: MainController by appKodein.instance()

    private val clients = ObservableListWrapper(controller.fetchAllClients().map { it.toClientData() }.toMutableList())
    private val selectedClient = SimpleObjectProperty<ClientData>()
    private var firstNameField: TextField by singleAssign()

    init {
        title = "Katsu"
        selectedClient.addListener { _: ObservableValue<out ClientData?>, _: ClientData?, newValue: ClientData? ->
            firstNameField.text = newValue?.firstName ?: ""
        }
        subscribe<ClientAdded> { event ->
            clients += event.client.toClientData()
        }
        subscribe<ClientUpdated> { event ->
            val clientIndex = clients.indexOfFirst { it.id == event.client.id }
            clients[clientIndex] = event.client.toClientData()
        }
        subscribe<ClientDeleted> { event ->
            clients.removeIf { it.id == event.clientId }
        }
    }

    override val root = hbox {
        vbox {
            listview(clients) {
                bindSelected(selectedClient)
                cellFormat {
                    text = it.firstName
                }
            }
        }
        vbox {
            label("First Name")
            firstNameField = textfield()
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
