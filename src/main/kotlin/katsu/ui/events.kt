package katsu.ui

import katsu.model.Client
import katsu.model.Treatment
import tornadofx.FXEvent

object AddNewClientEvent : FXEvent()
data class ClientAddedEvent(val client: Client) : FXEvent()

data class SaveClientEvent(val client: Client) : FXEvent()
data class ClientUpdatedEvent(val client: Client) : FXEvent()

data class DeleteClientEvent(val clientId: Long) : FXEvent()
data class ClientDeletedEvent(val clientId: Long) : FXEvent()

data class ClientsReloadedEvent(val clients: List<Client>) : FXEvent()

data class AddTreatmentEvent(val client: Client) : FXEvent()
data class TreatmentAddedEvent(val client: Client, val treatment: Treatment) : FXEvent()
