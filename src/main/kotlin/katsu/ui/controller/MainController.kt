package katsu.ui.controller

import katsu.model.Client
import katsu.model.ClientDbo
import katsu.persistence.ClientRepository
import katsu.persistence.NO_ID
import katsu.ui.AddNewClientEvent
import katsu.ui.ClientAdded
import katsu.ui.ClientDeleted
import katsu.ui.ClientUpdated
import katsu.ui.ClientsReloaded
import katsu.ui.DeleteClient
import katsu.ui.UpdateClient
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
            insertNewClient()
        }
        registrations += subscribe<UpdateClient> {
            updateExistingClient(it.client)
        }
        registrations += subscribe<DeleteClient> {
            deleteClient(it.clientId)
        }
    }

    fun start() {
        fire(ClientsReloaded(fetchAllClients()))
    }

    private fun fetchAllClients() = repository.fetchAll().map { it.toClient() }

    fun fetch(id: Long): Client = repository.fetch(id).toClient()

    fun unsubscribeAll() {
        registrations.forEach { it.unsubscribe() }
    }

    private fun insertNewClient() {
        val client = Client(NO_ID, "dummy", "some note")
        insertOrUpdateClient(client)
        fire(ClientAdded(client))
    }

    private fun updateExistingClient(client: Client) {
        require(client.id != NO_ID) { "Not able to update a not yet persisted client!" }
        insertOrUpdateClient(client)
        fire(ClientUpdated(client))
    }

    private fun insertOrUpdateClient(client: Client) {
        logg.info { "insertOrUpdateClient(client=$client)" }
        if (client.id == NO_ID) {
            repository.save(client.toClientDbo())
        } else {
            val dbClient = repository.fetch(client.id)
            dbClient.firstName = client.firstName
            repository.save(dbClient)
        }
    }

    private fun deleteClient(id: Long) {
        logg.info { "deleteClient(id=$id)" }

        repository.delete(id)
        fire(ClientDeleted(id))
    }
}

private fun Client.toClientDbo() = ClientDbo(
    id = NO_ID,
    firstName = firstName,
    note = note
)

private fun ClientDbo.toClient() = Client(
    id = id,
    firstName = firstName,
    note = note
)
