package katsu.ui.controller

import katsu.model.Client
import katsu.model.ClientDbo
import katsu.persistence.ClientRepository
import katsu.persistence.NO_ID
import mu.KotlinLogging
import org.kodein.di.generic.instance
import org.kodein.di.tornadofx.kodein
import tornadofx.Controller

class MainController : Controller() {

    private val logg = KotlinLogging.logger {}
    private val repository: ClientRepository by kodein().instance()

    fun fetchAllClients() = repository.fetchAll().map { it.toClient() }

    fun fetch(id: Long): Client =
        repository.fetch(id).toClient()

    fun insertOrUpdateClient(client: Client) {
        logg.info { "insertOrUpdateClient(client=$client)" }
        if (client.id == NO_ID) {
            repository.save(client.toClientDbo())
        } else {
            val dbClient = repository.fetch(client.id)
            dbClient.firstName = client.firstName
            repository.save(dbClient)
        }
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
