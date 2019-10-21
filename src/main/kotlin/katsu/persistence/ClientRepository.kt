package katsu.persistence

import katsu.model.ClientDbo
import javax.persistence.EntityManager

interface ClientRepository {
    fun fetch(id: Long): ClientDbo
    fun fetchAll(): List<ClientDbo>
    fun save(client: ClientDbo): ClientDbo
    fun delete(id: Long): ClientDbo
}

class ClientRepositoryImpl(
    private val em: EntityManager
) : ClientRepository {

    override fun fetch(id: Long): ClientDbo =
        em.find(ClientDbo::class.java, id)?.also { client ->
            client.treatments.sortByDescending { it.date }
        } ?: throw ClientNotFoundException(id)

    override fun fetchAll(): List<ClientDbo> =
        em.createQuery("from ${ClientDbo.ENTITY_NAME}", ClientDbo::class.java).resultList

    override fun save(client: ClientDbo): ClientDbo {
        em.transactional {
            persist(client)
        }
        return client
    }

    override fun delete(id: Long): ClientDbo {
        val client = fetch(id)
        em.transactional {
            remove(client)
        }
        return client
    }
}

class ClientNotFoundException(id: Long) : RuntimeException("Client not found by ID: $id")
