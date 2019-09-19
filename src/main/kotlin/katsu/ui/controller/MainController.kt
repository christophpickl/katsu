package katsu.ui.controller

import katsu.persistence.ClientRepository
import org.kodein.di.generic.instance
import org.kodein.di.tornadofx.kodein
import tornadofx.Controller

class MainController : Controller() {

    private val repository: ClientRepository by kodein().instance()

    fun fetchAllClients() = repository.fetchAll()
}
