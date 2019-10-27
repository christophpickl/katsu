package katsu.ui

import katsu.model.Client
import tornadofx.FXEvent

object AddNewClientEvent : FXEvent()
class ClientAddedEvent(val client: Client) : FXEvent()

class UpdateClientEvent(val client: Client) : FXEvent()
class ClientUpdatedEvent(val client: Client) : FXEvent()

class DeleteClientEvent(val clientId: Long) : FXEvent()
class ClientDeletedEvent(val clientId: Long) : FXEvent()

class ClientsReloadedEvent(val clients: List<Client>) : FXEvent()
