package katsu.ui

import katsu.model.Client
import tornadofx.FXEvent

object AddNewClientEvent : FXEvent()
class ClientAdded(val client: Client) : FXEvent()

class UpdateClient(val client: Client) : FXEvent()
class ClientUpdated(val client: Client) : FXEvent()

class DeleteClient(val clientId: Long) : FXEvent()
class ClientDeleted(val clientId: Long) : FXEvent()

class ClientsReloaded(val clients: List<Client>) : FXEvent()
