@file:SuppressWarnings("MatchingDeclarationName")

package katsu.ui

import katsu.model.Client
import tornadofx.getProperty
import tornadofx.property

class ClientUi(client: Client) {
    var id by property<Long>(client.id)
    fun idProperty() = getProperty(ClientUi::id)

    var firstName by property<String>(client.firstName)
    fun firstNameProperty() = getProperty(ClientUi::firstName)

    var notes by property<String>(client.notes)
    fun notesProperty() = getProperty(ClientUi::notes)

    fun toClient() = Client(id = id, firstName = firstName, notes = notes)

    override fun toString() = "ClientData[id=$id, firstName=$firstName]"
}

fun Client.toClientUi() = ClientUi(this)
