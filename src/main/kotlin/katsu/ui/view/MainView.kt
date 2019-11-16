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
import katsu.ui.AddNewClientEvent
import katsu.ui.AddTreatmentEvent
import katsu.ui.ClientAddedEvent
import katsu.ui.ClientDeletedEvent
import katsu.ui.ClientUi
import katsu.ui.ClientUpdatedEvent
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
        selectedTreatment.addListener { _: ObservableValue<out TreatmentUi?>, old: TreatmentUi?, newValue: TreatmentUi? ->
            logg.trace { "selected treatment changed: $newValue" }
            if (old != null) {
                treatments.firstOrNull { it.id == old.id }?.let { storedTreatment ->
                    storedTreatment.dateProperty().set(treatmentView.date)
                    storedTreatment.notesProperty().set(treatmentView.notes)
                }
            }
            treatmentView.updateTreatmentFields(newValue)
        }
        registrations += subscribe<ClientAddedEvent> { event ->
            logg.trace { "onClientAddedEvent: $event" }
            clients += event.client.toClientUi()
            clientsList.selectWhere { it.id == event.client.id }
            treatments.setAll(event.client.treatments.map { it.toTreatmentUi() })
        }
        registrations += subscribe<ClientUpdatedEvent> { event ->
            logg.trace { "onClientUpdatedEvent: $event" }
            clients.setById(event.client)
            treatments.setAll(event.client.treatments.map { it.toTreatmentUi() })
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
                                storeTreatmentUiData()
                                val currentClient = selectedClient.get().toClient().updateByView()
                                fire(SaveClientEvent(currentClient))
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
                                        enableWhen(selectedTreatment.isNotNull)
                                        setOnAction {
                                            // not firing an event ;)
                                            val treatmentToDelete = selectedTreatment.get() // not properly beneath click point :)
                                            treatments.removeAll { it.id == treatmentToDelete.id }
                                        }
                                    }
                                }
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
    require(clientIndex != -1) { "given client's ID ($client) is not contained in stored IDs: ${map { it.id }.joinToString()}" }
    this[clientIndex] = client.toClientUi()
}

/*

                                setCellFactory {
                                    val cell = ListCell<TreatmentUi>()

                                    val contextMenu = ContextMenu().apply {
                                        val deleteItem = MenuItem().apply {
                                            text = "Delete"
                                            action {
                                                val clickedTreatument: TreatmentUi? = cell.item
                                                println("context menuuu delete: ${clickedTreatument}")
                                            }
                                        }
                                        items.addAll(deleteItem)
                                    }

                                    cell.textProperty().bind(cell.itemProperty().stringBinding(op = { it?.date?.formatKatsuDate() }))
//                                    cell.textProperty().bindBidirectional(cell.itemProperty().get().dateProperty(), object : StringConverter<LocalDateTime>() {
//                                        override fun toString(date: LocalDateTime?): String =  date?.formatKatsuDate() ?: ""
//                                        override fun fromString(string: String?): LocalDateTime = datePatternParse(string!!)
//                                    })

                                    cell.emptyProperty().addListener { _, _, isNowEmpty ->
                                        cell.contextMenu =
                                            if (isNowEmpty) null
                                            else contextMenu
                                    }

//                                    cell.itemProperty().
//                                    cell.textProperty().bindBidirectional(cell.item.dateProperty(), LocalDateTimeStringConverter())//.bind(cell.item.dateFormatted)
                                    /*

            MenuItem editItem = new MenuItem();
            editItem.textProperty().bind(Bindings.format("Edit \"%s\"", cell.itemProperty()));
            editItem.setOnAction(event -> {
                String item = cell.getItem();
                // code to edit item...
            });
            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Delete \"%s\"", cell.itemProperty()));
            deleteItem.setOnAction(event -> listView.getItems().remove(cell.getItem()));
            contextMenu.getItems().addAll(editItem, deleteItem);

            cell.textProperty().bind(cell.itemProperty());

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
                                     */
                                    cell
                                }
 */