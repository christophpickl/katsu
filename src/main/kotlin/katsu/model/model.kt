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

@Entity(name = Client.ENTITY_NAME)
@Table(name = Client.TABLE_NAME)
data class Client(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long,

    @Column(name = "firstName", nullable = false, length = COL_LENGTH_LIL)
    val firstName: String,

    @Column(name = "note", nullable = false)
    val note: String
) {
    companion object {
        const val ENTITY_NAME = "Client"
        const val TABLE_NAME = "client"
    }
}
