@file:SuppressWarnings("MatchingDeclarationName")

package katsu.ui

import katsu.model.Client
import tornadofx.getProperty
import tornadofx.property

class ClientData(client: Client) {
    var id by property<Long>(client.id)
    fun idProperty() = getProperty(ClientData::id)

    var firstName by property<String>(client.firstName)
    fun firstNameProperty() = getProperty(ClientData::firstName)

    override fun toString() = "ClientData[id=$id, firstName=$firstName]"
}

fun Client.toClientData() = ClientData(this)
