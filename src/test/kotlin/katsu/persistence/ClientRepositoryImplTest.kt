package katsu.persistence

import assertk.all
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isEqualToIgnoringGivenProperties
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEqualTo
import assertk.assertions.messageContains
import katsu.model.ClientDbo
import katsu.model.testInstance
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@Test
class ClientRepositoryImplTest {

    private lateinit var client: ClientDbo
    private val nonExistingClientId = 42L

    @BeforeMethod
    fun `reset state`() {
        client = ClientDbo.testInstance().copy(id = NO_ID)
    }

    fun `When save client Then then it is persisted`() = withTestDb {
        ClientRepositoryImpl(em).save(client)

        val found = em.find(ClientDbo::class.java, client.id)
        assertThat(found).isEqualTo(client)
    }

    fun `Given client existWhen fetch non existing client Then throw`() = withTestDb {
        persist(client)

        val fetched = ClientRepositoryImpl(em).fetch(client.id)

        assertThat(fetched).isEqualTo(client)
    }

    fun `When fetch non existing client Then throw`() = withTestDb {
        assertThat {
            ClientRepositoryImpl(em).fetch(nonExistingClientId)
        }.isFailure().all {
            isInstanceOf(ClientNotFoundException::class)
            messageContains(nonExistingClientId.toString())
        }
    }

    fun `Given single client When fetch all Then return that client`() = withTestDb {
        persist(client)

        val actualClients = ClientRepositoryImpl(em).fetchAll()

        assertThat(actualClients).hasSize(1)
        assertThat(actualClients[0]).isEqualToIgnoringGivenProperties(client, ClientDbo::id)
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
