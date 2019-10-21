@file:SuppressWarnings("MatchingDeclarationName")

package katsu.model

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = TreatmentDbo.ENTITY_NAME)
@Table(name = TreatmentDbo.TABLE_NAME)
data class TreatmentDbo(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long,

    @Column(name = COL_DATE, nullable = false)
    var date: LocalDateTime,

    @Column(name = "notes", nullable = false)
    var notes: String

) {
    companion object {
        const val ENTITY_NAME = "Treatment"
        const val TABLE_NAME = "treatment"
        const val COL_DATE = "date"
    }
}
