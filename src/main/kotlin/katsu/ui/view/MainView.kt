package katsu.ui.view

import com.sun.javafx.collections.ObservableListWrapper
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.geometry.Orientation
import javafx.scene.control.TextField
import katsu.model.Client
import katsu.persistence.NO_ID
import katsu.ui.ClientData
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

//class ClientModel(client: ClientData?  = null) : ItemViewModel<ClientData>(client) {
//    val firstName = bind(ClientData::firstNameProperty)
//}

class MainView : View() {

    private val logg = logger {}
    private val controller: MainController by appKodein.instance()

    private val clients = ObservableListWrapper(controller.fetchAllClients().map { it.toClientData() }.toMutableList())
    private val selectedClient = SimpleObjectProperty<ClientData>()
    private var firstNameField: TextField by singleAssign()

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
            firstNameField = textfield().apply {
            }
            separator(Orientation.HORIZONTAL)
            button("add dummy").apply {
                id = "btnClickMe"
                action {
                    logg.debug { "button clicked" }
                    val client = Client(NO_ID, "dummy", "some note")
                    controller.insertOrUpdateClient(client)
                    clients += client.toClientData()
                }
            }
            button("save").apply {
                enableWhen { selectedClient.isNotNull }
                action {
                    val currentClient = selectedClient.get().toClient().copy(firstName = firstNameField.text)
                    controller.insertOrUpdateClient(currentClient)
                }
            }
        }
    }

    init {
        title = "Katsu"
        selectedClient.addListener { observable: ObservableValue<out ClientData?>, oldValue: ClientData?, newValue: ClientData? ->
            firstNameField.text = newValue?.firstName ?: ""
        }
    }
}

fun ClientData.toClient() = Client(id = id, firstName = firstName, note = "")
