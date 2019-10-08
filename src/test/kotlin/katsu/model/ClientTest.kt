package katsu.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import katsu.ui.toClientUi
import org.testng.annotations.Test

@Test
class ClientTest {
    fun `When transform to DBO Then it should be transformed`() {
        val client = Client.testInstance()

        val dbo = client.toClientDbo()

        assertThat(dbo).isEqualTo(ClientDbo(
            id = client.id,
            firstName = client.firstName,
            notes = client.notes
        ))
    }

    fun `When transform to UI Then it should be transformed`() {
        val client = Client.testInstance()

        val data = client.toClientUi()

        assertThat(data.firstName).isEqualTo(client.firstName)
        assertThat(data.notes).isEqualTo(client.notes)
    }
}
