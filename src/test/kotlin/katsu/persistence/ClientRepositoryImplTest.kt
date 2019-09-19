package katsu.persistence

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualToIgnoringGivenProperties
import assertk.assertions.isNotEqualTo
import katsu.model.Client
import katsu.model.testInstance
import org.testng.annotations.Test

@Test
class ClientRepositoryImplTest {

    private val unsavedClient = Client.testInstance().copy(id = NO_ID)

    fun `Given single client When fetch all Then return that client`() = withTestDb {
        migrate()
        persist(unsavedClient)

        val actualClients = ClientRepositoryImpl(em).fetchAll()

        assertThat(actualClients).hasSize(1)
        assertThat(actualClients[0]).isEqualToIgnoringGivenProperties(unsavedClient, Client::id)
        assertThat(actualClients[0].id).isNotEqualTo(NO_ID)
    }
}

private fun TestDbContext.persist(client: Client) {
    em.transactional {
        persist(client)
    }
}

fun TestDbContext.migrate() {
    DatabaseMigrator(em).migrate()
}
