package katsu.persistence

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isEqualToIgnoringGivenProperties
import assertk.assertions.isNotEqualTo
import katsu.model.Client
import katsu.model.ClientDbo
import katsu.model.testInstance
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@Test
class ClientRepositoryImplTest {

    private lateinit var unsavedClient: ClientDbo

    @BeforeMethod
    fun `reset state`() {
        unsavedClient = ClientDbo.testInstance().copy(id = NO_ID)
    }

    fun `When save client Then then it is persisted`() = withTestDb {
        ClientRepositoryImpl(em).save(unsavedClient)

        val found = em.find(Client::class.java, unsavedClient.id)
        assertThat(found).isEqualTo(unsavedClient)
    }

    fun `Given single client When fetch all Then return that client`() = withTestDb {
        persist(unsavedClient)

        val actualClients = ClientRepositoryImpl(em).fetchAll()

        assertThat(actualClients).hasSize(1)
        assertThat(actualClients[0]).isEqualToIgnoringGivenProperties(unsavedClient, ClientDbo::id)
        assertThat(actualClients[0].id).isNotEqualTo(NO_ID)
    }
}

private fun TestDbContext.persist(client: ClientDbo) {
    em.transactional {
        persist(client)
    }
}

fun TestDbContext.migrate() {
    DatabaseMigrator(em).migrate()
}
