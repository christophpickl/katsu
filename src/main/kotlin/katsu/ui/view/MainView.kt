package katsu.ui.view

import com.sun.javafx.collections.ObservableListWrapper
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.scene.web.HTMLEditor
import katsu.formatKatsuDate
import katsu.model.Client
import katsu.ui.AddClientEvent
import katsu.ui.AddTreatmentEvent
import katsu.ui.ClientAddedEvent
import katsu.ui.ClientDeletedEvent
import katsu.ui.ClientSavedEvent
import katsu.ui.ClientUi
import katsu.ui.ClientsReloadedEvent
import katsu.ui.DeleteClientEvent
import katsu.ui.SaveClientEvent
import katsu.ui.TreatmentAddedEvent
import katsu.ui.TreatmentUi
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
import tornadofx.contextmenu
import tornadofx.enableWhen
import tornadofx.hbox
import tornadofx.hgrow
import tornadofx.htmleditor
import tornadofx.item
import tornadofx.label
import tornadofx.listview
import tornadofx.multiSelect
import tornadofx.selectWhere
import tornadofx.singleAssign
import tornadofx.stringBinding
import tornadofx.textfield
import tornadofx.top
import tornadofx.vbox
import tornadofx.vgrow

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
    private val treatmentView = TreatmentView()

    private fun storeTreatmentUiData() {
        selectedTreatment.value?.let { currentTreatment ->
            val inMemoryTreatment = treatments.first { it.id == currentTreatment.id }
            inMemoryTreatment.dateProperty().set(treatmentView.date)
            inMemoryTreatment.notesProperty().set(treatmentView.notes)
            logg.trace { "stored inMemoryTreatment: $inMemoryTreatment" }
        }
    }

    init {
        title = "Katsu"
        selectedClient.addListener { _: ObservableValue<out ClientUi?>, _: ClientUi?, newValue: ClientUi? ->
            logg.trace { "selected client changed: $newValue" }
            updateClientFields(newValue)
        }
        selectedTreatment.addListener { _: ObservableValue<out TreatmentUi?>, old: TreatmentUi?, new: TreatmentUi? ->
            logg.trace { "selected treatment changed: $new" }
            if (old != null) {
                treatments.firstOrNull { it.id == old.id }?.let { storedTreatment ->
                    storedTreatment.dateProperty().set(treatmentView.date)
                    storedTreatment.notesProperty().set(treatmentView.notes)
                }
            }
            treatmentView.updateTreatmentFields(new)
        }
        registrations += subscribe<ClientAddedEvent> { event ->
            logg.trace { "onClientAddedEvent: $event" }
            clients += event.client.toClientUi()
            clientsList.selectWhere { it.id == event.client.id }
            treatments.setAll(event.client.treatments.map { it.toTreatmentUi() })
        }
        registrations += subscribe<ClientSavedEvent> { event ->
            logg.trace { "onClientUpdatedEvent: $event" }
            clients.setById(event.client)
            val previousSelected = selectedTreatment.get()?.id
            treatments.setAll(event.client.treatments.map { it.toTreatmentUi() })
            if (previousSelected != null) {
                treatmentsList.selectWhere { it.id == previousSelected }
            }
        }
        registrations += subscribe<ClientDeletedEvent> { event ->
            logg.trace { "onClientDeletedEvent: $event" }
            clients.removeAll { it.id == event.clientId }
            treatments.clear()
        }
        registrations += subscribe<ClientsReloadedEvent> { event ->
            logg.trace { "onClientsReloadedEvent: $event" }
            clients.setAll(event.clients.map { it.toClientUi() })
        }
        registrations += subscribe<TreatmentAddedEvent> { event ->
            logg.trace { "onTreatmentAddedEvent: $event" }
            val treatmentUi = event.treatment.toTreatmentUi()
            val index = treatments.indexOfFirst { it.date > treatmentUi.date }.let { if (it == -1) 0 else it }
            treatments.add(index, treatmentUi)
            treatmentsList.selectWhere { it.id == treatmentUi.id }
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
        treatmentView.updateTreatmentFields(preselectedTreatment)
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
                        button("Add").apply {
                            id = ViewIds.BUTTON_ADD_CLIENT
                            action {
                                logg.debug { "Add client button clicked" }
                                fire(AddClientEvent)
                                firstNameField.requestFocus()
                            }
                        }
                        button("Save").apply {
                            id = ViewIds.BUTTON_SAVE_CLIENT
                            enableWhenClientSelected()
                            action {
                                storeTreatmentUiData()
                                val currentClient = selectedClient.get().toClient().updateByView()
                                fire(SaveClientEvent(currentClient))
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
                        contextmenu {
                            item(name = "Delete") {
                                setOnAction {
                                    fire(DeleteClientEvent(selectedClient.get().id))
                                }
                            }
                        }
                    }
                }
                vbox {
                    hgrow = Priority.ALWAYS
                    hbox {
                        vgrow = Priority.ALWAYS
                        vbox {
                            hgrow = Priority.NEVER
                            button("Add") {
                                maxWidth = Double.MAX_VALUE
                                enableWhenClientSelected()
                                action {
                                    fire(AddTreatmentEvent(selectedClient.get().toClient()))
                                }
                            }
                            treatmentsList = listview(treatments) {
                                vgrow = Priority.ALWAYS
                                multiSelect(false)
                                bindSelected(selectedTreatment)
                                cellFormat {
                                    textProperty().bind(it.dateProperty().stringBinding() { it?.formatKatsuDate() })
                                }
                                contextmenu {
                                    item(name = "Delete") {
                                        setOnAction {
                                            // not properly beneath click point :) but good enough
                                            val treatmentToDelete = selectedTreatment.get()
                                            treatments.removeAll { it.id == treatmentToDelete.id }
                                        }
                                    }
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
                        }
                    }

                    notesField = htmleditor().apply {
                        id = ViewIds.TEXT_NOTES
                        hgrow = Priority.ALWAYS
                        vgrow = Priority.ALWAYS
                    }

                    add(treatmentView)
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

private fun ObservableListWrapper<ClientUi>.setById(client: Client) {
    val clientIndex = indexOfFirst { it.id == client.id }
    require(clientIndex != -1) {
        "Given client's ID ($client) is not contained in stored IDs: ${map { it.id }.joinToString()}"
    }
    this[clientIndex] = client.toClientUi()
}
