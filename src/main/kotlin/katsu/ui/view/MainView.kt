package katsu.ui.view

import com.sun.javafx.collections.ObservableListWrapper
import javafx.beans.property.ReadOnlyBooleanWrapper
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.scene.web.HTMLEditor
import katsu.datePattern
import katsu.model.Client
import katsu.ui.AddNewClientEvent
import katsu.ui.AddTreatmentEvent
import katsu.ui.ClientAddedEvent
import katsu.ui.ClientDeletedEvent
import katsu.ui.ClientUi
import katsu.ui.ClientUpdatedEvent
import katsu.ui.ClientsReloadedEvent
import katsu.ui.DeleteClientEvent
import katsu.ui.TreatmentAddedEvent
import katsu.ui.TreatmentUi
import katsu.ui.UpdateClientEvent
import katsu.ui.toClientUi
import katsu.ui.toTreatmentUi
import mu.KotlinLogging.logger
import tornadofx.FXEventRegistration
import tornadofx.View
import tornadofx.action
import tornadofx.bindSelected
import tornadofx.borderpane
import tornadofx.button
import tornadofx.center
import tornadofx.enableWhen
import tornadofx.hbox
import tornadofx.hgrow
import tornadofx.htmleditor
import tornadofx.label
import tornadofx.listview
import tornadofx.multiSelect
import tornadofx.selectWhere
import tornadofx.singleAssign
import tornadofx.textfield
import tornadofx.top
import tornadofx.vbox
import tornadofx.vgrow
import java.time.LocalDateTime

class MainView : View() {

    private val logg = logger {}
    private val menuBarController: MenuBarController by inject()

    private val clients = ObservableListWrapper<ClientUi>(mutableListOf())
    private val treatments = ObservableListWrapper<TreatmentUi>(mutableListOf())
    private val selectedTreatment = SimpleObjectProperty<TreatmentUi>()
    private val selectedClient = SimpleObjectProperty<ClientUi>()
    private val registrations = mutableListOf<FXEventRegistration>()

    private var clientsList: ListView<ClientUi> by singleAssign()
    private var firstNameField: TextField by singleAssign()
    private var notesField: HTMLEditor by singleAssign()
    private var treatmentsList: ListView<TreatmentUi> by singleAssign()

    private var treatmentNotesField: HTMLEditor by singleAssign()
    private var dateField: TextField by singleAssign()

    init {
        title = "Katsu"
        selectedClient.addListener { _: ObservableValue<out ClientUi?>, _: ClientUi?, newValue: ClientUi? ->
            updateClientFields(newValue)
        }
        selectedTreatment.addListener { _: ObservableValue<out TreatmentUi?>, _: TreatmentUi?, newValue: TreatmentUi? ->
            updateTreatmentFields(newValue)
        }
        registrations += subscribe<ClientAddedEvent> { event ->
            clients += event.client.toClientUi()
            clientsList.selectWhere { it.id == event.client.id }
            treatments.setAll(event.client.treatments.map { it.toTreatmentUi() })
        }
        registrations += subscribe<ClientUpdatedEvent> { event ->
            val clientIndex = clients.indexOfFirst { it.id == event.client.id }
            clients[clientIndex] = event.client.toClientUi()
            treatments.setAll(event.client.treatments.map { it.toTreatmentUi() })
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
            clients[clientIndex] = event.client.toClientUi()
            val treatmentUi = event.treatment.toTreatmentUi()
            treatments.add(treatmentUi)
//            treatments.setAll(event.client.treatments)
            updateTreatmentFields(treatmentUi)
        }
    }


    private fun updateClientFields(client: ClientUi?) {
        firstNameField.text = client?.firstName ?: ""
        notesField.htmlText = client?.notes ?: ""
        val treatmentsUi = client?.treatments?.map { it.toTreatmentUi() }
        treatments.setAll(treatmentsUi ?: emptyList())

        val preselectedTreatment = treatmentsUi?.firstOrNull()
        if (preselectedTreatment != null) {
            treatmentsList.selectWhere { it.id == preselectedTreatment.id }
        }
        updateTreatmentFields(preselectedTreatment)
    }

    private fun updateTreatmentFields(treatment: TreatmentUi?) {
        dateField.text = treatment?.dateFormatted ?: LocalDateTime.now().format(datePattern)
        treatmentNotesField.htmlText = treatment?.notes ?: ""
    }

    override val root = borderpane {
        val mybar = MyMenuBar(menuBarController).apply {
            isUseSystemMenuBar = true
        }
        if (System.getProperty("katsu.isMac") != null) {
            setGlobalMacMenuBar(mybar)
        } else {
            top {
                add(mybar)
            }
        }
        center {
            hbox {


                vbox {
                    hbox(spacing = 5) {
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
                            enableWhenClientSelected()
                            action {
                                val currentClient = selectedClient.get().toClient().updateByView()
                                fire(UpdateClientEvent(currentClient))
                            }
                        }
                        button("Delete").apply {
                            id = ViewIds.BUTTON_DELETE_CLIENT
                            enableWhenClientSelected()
                            action {
                                fire(DeleteClientEvent(selectedClient.get().id))
                            }
                        }
                    }
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
                            hbox(spacing = 5) {
                                button("Add") {
                                    enableWhenClientSelected()
                                    action {
                                        fire(AddTreatmentEvent(selectedClient.get().toClient()))
                                    }
                                }
                                button("Delete") {
                                    enableWhen(ReadOnlyBooleanWrapper(false))
                                }
                            }
                            treatmentsList = listview(treatments) {
                                vgrow = Priority.ALWAYS
                                multiSelect(false)

                                bindSelected(selectedTreatment)
                                cellFormat {
                                    text = it.dateFormatted
                                }
                            }
                        }
                    }

                    notesField = htmleditor().apply {
                        id = ViewIds.TEXT_NOTES
                        hgrow = Priority.ALWAYS
                        vgrow = Priority.ALWAYS
                    }

                    label("TREATMENT ===")
                    vbox {
                        hgrow = Priority.ALWAYS
                        label("Date")
                        dateField = textfield().apply {
                            hgrow = Priority.ALWAYS
                        }
                    }
                    treatmentNotesField = htmleditor().apply {
                        hgrow = Priority.ALWAYS
                        vgrow = Priority.ALWAYS
                    }

//            separator(Orientation.HORIZONTAL)
                }
            }
        }
    }

    private fun Client.updateByView() = copy(
        firstName = firstNameField.text,
        notes = notesField.htmlText,
        treatments = this@MainView.treatments.map { it.toTreatment() }
    )

    fun unsubscribeAll() {
        registrations.forEach { it.unsubscribe() }
    }

    private fun Button.enableWhenClientSelected() {
        enableWhen { selectedClient.isNotNull }
    }
}
