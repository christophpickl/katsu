@file:SuppressWarnings("MatchingDeclarationName")

package katsu.model

import katsu.formatKatsuDate
import katsu.persistence.NO_ID
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
    var notes: HtmlString

) {
    companion object {
        const val ENTITY_NAME = "Treatment"
        const val TABLE_NAME = "treatment"
        const val COL_DATE = "date"

        val PROTOTYPE = TreatmentDbo(
            id = NO_ID,
            date = LocalDateTime.now(),
            notes = ""
        )
    }

    fun updateBy(newTreatment: Treatment) {
        date = newTreatment.date
        notes = newTreatment.notes
    }

    fun toTreatment() = Treatment(
        id = id,
        date = date,
        notes = notes
    )

    override fun toString() = "TreatmentDbo($id, ${date.formatKatsuDate()}, notes=${notes.katsuSubstring()})"
}

data class Treatment(
    val id: Long,
    val date: LocalDateTime,
    val notes: HtmlString
) {
    companion object {
        fun dummy() = Treatment(
            id = 1L,
            date = LocalDateTime.now(),
            notes = "dummy treat"
        )
    }

    fun toTreatmentDbo() = TreatmentDbo(
        id = id,
        date = date,
        notes = notes
    )

    override fun toString() = "Treatment($id, ${date.formatKatsuDate()}, notes=${notes.katsuSubstring()})"

}
