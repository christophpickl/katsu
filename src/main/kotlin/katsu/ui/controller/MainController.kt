package katsu.ui.controller

import katsu.model.Client
import katsu.persistence.ClientRepository
import katsu.persistence.NO_ID
import katsu.ui.AddNewClientEvent
import katsu.ui.ClientAddedEvent
import katsu.ui.ClientDeletedEvent
import katsu.ui.ClientUpdatedEvent
import katsu.ui.ClientsReloadedEvent
import katsu.ui.DeleteClientEvent
import katsu.ui.UpdateClientEvent
import mu.KotlinLogging.logger
import org.kodein.di.generic.instance
import org.kodein.di.tornadofx.kodein
import tornadofx.Controller
import tornadofx.FXEventRegistration

class MainController : Controller() {

    private val registrations = mutableListOf<FXEventRegistration>()

    private val logg = logger {}
    private val repository: ClientRepository by kodein().instance()

    init {
        registrations += subscribe<AddNewClientEvent> {
            addNewClient()
        }
        registrations += subscribe<UpdateClientEvent> {
            updateClient(it.client)
        }
        registrations += subscribe<DeleteClientEvent> {
            deleteClient(it.clientId)
        }
    }

    fun start() {
        fire(ClientsReloadedEvent(fetchAllClients()))
    }

    private fun fetchAllClients() = repository.fetchAll().map { it.toClient() }

    fun fetch(id: Long): Client = repository.fetch(id).toClient()

    fun unsubscribeAll() {
        registrations.forEach { it.unsubscribe() }
    }

    private fun addNewClient() {
        fire(ClientAddedEvent(insertOrUpdateClient(Client.PROTOTYPE)))
    }

    private fun updateClient(client: Client) {
        require(client.id != NO_ID) { "Not able to update a not yet persisted client!" }
        fire(ClientUpdatedEvent(insertOrUpdateClient(client)))
    }

    private fun insertOrUpdateClient(client: Client): Client {
        logg.info { "insertOrUpdateClient(client=$client)" }
        return if (client.id == NO_ID) {
            repository.save(client.toClientDbo())
        } else {
            val dbClient = repository.fetch(client.id)
            dbClient.updateBy(client)
            repository.save(dbClient)
        }.toClient()
    }

    private fun deleteClient(id: Long) {
        logg.info { "deleteClient(id=$id)" }

        repository.delete(id)
        fire(ClientDeletedEvent(id))
    }
}
