package katsu.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.testng.annotations.Test

@Test
class ClientDboTest {
    fun `When transform to client Then it should be transformed`() {
        val dbo = ClientDbo.testInstance()

        val client = dbo.toClient()

        assertThat(client).isEqualTo(Client(
            id = dbo.id,
            firstName = dbo.firstName,
            notes = dbo.notes
        ))
    }
}
