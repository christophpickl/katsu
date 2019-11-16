@file:SuppressWarnings("MatchingDeclarationName")

package katsu.ui

import katsu.datePattern
import katsu.model.Client
import katsu.model.HtmlString
import katsu.model.Treatment
import tornadofx.getProperty
import tornadofx.property
import java.time.LocalDateTime

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

class TreatmentUi(treatment: Treatment) {
    var id by property<Long>(treatment.id)
    fun idProperty() = getProperty(TreatmentUi::id)

    var date by property<LocalDateTime>(treatment.date)
    fun dateProperty() = getProperty(TreatmentUi::date)
    val dateFormatted: String = date.format(datePattern)

    var notes by property<HtmlString>(treatment.notes)
    fun notesProperty() = getProperty(TreatmentUi::notes)

    fun toTreatment() = Treatment(
        id = id,
        date = date,
        notes = notes
    )

    override fun toString() = "TreatmentUi[id=$id, date=$date]"

}

fun Treatment.toTreatmentUi() = TreatmentUi(this)

