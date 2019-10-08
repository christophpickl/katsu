package katsu.persistence

import assertk.all
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isEqualToIgnoringGivenProperties
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEqualTo
import assertk.assertions.isSuccess
import assertk.assertions.messageContains
import com.github.christophpickl.kpotpourri.common.string.times
import katsu.model.ClientDbo
import katsu.model.testInstance
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import javax.persistence.PersistenceException

@Test
class ClientRepositoryImplTest {

    private lateinit var client: ClientDbo
    private val nonExistingClientId = 42L

    @BeforeMethod
    fun `reset state`() {
        client = ClientDbo.testInstance().copy(id = NO_ID)
    }

    fun `When save client Then it is persisted`() = withTestDb {
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

    fun `When delete non existing Then fail`() = withTestDb {
        assertThat {
            ClientRepositoryImpl(em).delete(nonExistingClientId)
        }.isFailure().all {
            isInstanceOf(ClientNotFoundException::class)
            messageContains(nonExistingClientId.toString())
        }
    }

    fun `Given client When delete that client Then no clients exist anymore`() = withTestDb {
        persist(client)

        ClientRepositoryImpl(em).delete(client.id)

        assertThat(em.createQuery("from ${ClientDbo.ENTITY_NAME}", ClientDbo::class.java).resultList).isEmpty()
    }

    fun `Given client with maximum length When persist Then succeed`() = withTestDb {
        assertThat {
            persist(client.copy(firstName = "x".times(ClientDbo.FIRST_NAME_LENGTH)))
        }.isSuccess()
    }

    fun `Given client with more than maximum length When persist Then fail`() = withTestDb {
        assertThat {
            persist(client.copy(firstName = "x".times(ClientDbo.FIRST_NAME_LENGTH + 1)))
        }.isFailure()
            .isInstanceOf(PersistenceException::class)
    }
}
