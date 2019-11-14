@file:SuppressWarnings("MatchingDeclarationName")

package katsu.ui

import katsu.model.Client
import katsu.model.Treatment
import tornadofx.getProperty
import tornadofx.property

class ClientUi(client: Client) {
    var id by property<Long>(client.id)
    fun idProperty() = getProperty(ClientUi::id)

    var firstName by property<String>(client.firstName)
    fun firstNameProperty() = getProperty(ClientUi::firstName)

    var notes by property<String>(client.notes)
    fun notesProperty() = getProperty(ClientUi::notes)

    var treatments by property<List<Treatment>>(client.treatments)
    fun treatmentsProperty() = getProperty(ClientUi::treatments)

    fun toClient() = Client(id = id, firstName = firstName, notes = notes, treatments = treatments)

    override fun toString() = "ClientUi[id=$id, firstName=$firstName]"
}

fun Client.toClientUi() = ClientUi(this)
