package katsu.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.testng.annotations.Test

@Test
class TreatmentDboTest {

    fun `When transform to treatment Then it should be transformed`() {
        val dbo = TreatmentDbo.testInstance()

        val treatment = dbo.toTreatment()

        assertThat(treatment).isEqualTo(Treatment(
            id = dbo.id,
            date = dbo.date,
            notes = dbo.notes
        ))
    }

    fun `When update Then fields are updated`() {
        val dbo = TreatmentDbo.testInstance()
        val treatment = Treatment.testInstance()

        dbo.updateBy(treatment)

        assertThat(dbo.date).isEqualTo(treatment.date)
        assertThat(dbo.notes).isEqualTo(treatment.notes)
    }
}
