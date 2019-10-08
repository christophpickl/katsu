@file:SuppressWarnings("MatchingDeclarationName")

package katsu.model

import katsu.persistence.NO_ID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

const val COL_LENGTH_LIL = 128
// const val COL_LENGTH_MED = 512

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
    var notes: String
) {
    companion object {
        const val ENTITY_NAME = "Client"
        const val TABLE_NAME = "client"
        const val FIRST_NAME_LENGTH = COL_LENGTH_LIL
    }

    fun toClient() = Client(
        id = id,
        firstName = firstName,
        notes = notes
    )

    fun updateBy(client: Client) {
        firstName = client.firstName
        notes = client.notes
    }
}

data class Client(
    val id: Long,
    val firstName: String,
    val notes: String
) {
    companion object {
        val PROTOTYPE = Client(
            id = NO_ID,
            firstName = "",
            notes = ""
        )
    }

    fun toClientDbo() = ClientDbo(
        id = NO_ID,
        firstName = firstName,
        notes = notes
    )
}
