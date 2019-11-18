package katsu.model

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isEqualToIgnoringGivenProperties
import org.testng.annotations.Test

@Test
class ClientDboTest {

    private val anyTreatments = emptyList<Treatment>()

    fun `When transform to client Then it should be transformed`() {
        val dbo = ClientDbo.testInstance()

        val client = dbo.toClient()

        assertThat(client).isEqualToIgnoringGivenProperties(Client(
            id = dbo.id,
            firstName = dbo.firstName,
            notes = dbo.notes,
            treatments = anyTreatments
        ), Client::treatments)
    }

    fun `When update Then fields are updated`() {
        val dbo = ClientDbo.testInstance()
        val client = Client.testInstance()

        dbo.updateBy(client)

        assertThat(dbo.firstName).isEqualTo(client.firstName)
        assertThat(dbo.notes).isEqualTo(client.notes)
        assertThat(dbo.treatments).hasSize(client.treatments.size)
    }
}
