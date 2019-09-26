package katsu.persistence

import katsu.model.ClientDbo
import javax.persistence.EntityManager

interface ClientRepository {
    fun fetchAll(): List<ClientDbo>
    fun save(client: ClientDbo)
    fun fetch(id: Long): ClientDbo
}

class ClientRepositoryImpl(
    private val em: EntityManager
) : ClientRepository {

    // TODO test me
    override fun fetch(id: Long): ClientDbo =
        em.find(ClientDbo::class.java, id) ?: throw Exception("Client not found by ID: $id")

    override fun fetchAll(): List<ClientDbo> =
        em.createQuery("from ${ClientDbo.ENTITY_NAME}", ClientDbo::class.java).resultList

    override fun save(client: ClientDbo) {
        em.transactional {
            persist(client)
        }
    }
}
