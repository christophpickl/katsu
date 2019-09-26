@file:SuppressWarnings("MatchingDeclarationName")

package katsu.model

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

    @Column(name = "firstName", nullable = false, length = COL_LENGTH_LIL)
    var firstName: String,

    @Column(name = "note", nullable = false)
    var note: String
) {
    companion object {
        const val ENTITY_NAME = "Client"
        const val TABLE_NAME = "client"
    }
}

data class Client(
    val id: Long,
    val firstName: String,
    val note: String
)
