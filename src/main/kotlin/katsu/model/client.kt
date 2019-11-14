@file:SuppressWarnings("MatchingDeclarationName")

package katsu.model

import katsu.persistence.NO_ID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

typealias HtmlString = String

@Entity(name = ClientDbo.ENTITY_NAME)
@Table(name = ClientDbo.TABLE_NAME)
data class ClientDbo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long,

    @Column(name = "firstName", nullable = false, length = FIRST_NAME_LENGTH)
    var firstName: String,

    @Column(name = "notes", nullable = false)
    var notes: HtmlString,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var treatments: MutableList<TreatmentDbo> = mutableListOf()
) {
    companion object {
        const val ENTITY_NAME = "Client"
        const val TABLE_NAME = "client"
        const val FIRST_NAME_LENGTH = COL_LENGTH_LIL
    }

    fun toClient() = Client(
        id = id,
        firstName = firstName,
        notes = notes,
        treatments = treatments.map { it.toTreatment() }
    )

    fun updateBy(client: Client) {
        firstName = client.firstName
        notes = client.notes
    }
}

data class Client(
    val id: Long,
    val firstName: String,
    val notes: HtmlString,
    val treatments: List<Treatment>
) {
    companion object {
        val PROTOTYPE = Client(
            id = NO_ID,
            firstName = "",
            notes = """
                <html dir="ltr"><head></head><body contenteditable="true">
                <h1><font face="Arial">Medical</font></h1>
                <ul><li><span style="font-family: Arial;">TODO</span></li></ul>
                </body></html>
            """.trimIndent(),
            treatments = emptyList()
        )
    }

    fun toClientDbo() = ClientDbo(
        id = NO_ID,
        firstName = firstName,
        notes = notes,
        treatments = treatments.map { it.toTreatmentDbo() }.toMutableList()
    )
}
