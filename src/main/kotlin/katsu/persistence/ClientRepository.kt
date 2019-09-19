package katsu.persistence

import katsu.model.Client
import javax.persistence.EntityManager

interface ClientRepository {
    fun fetchAll(): List<Client>
}

class ClientRepositoryImpl(
    private val em: EntityManager
) : ClientRepository {
    override fun fetchAll(): List<Client> = em.createQuery("from ${Client.ENTITY_NAME}", Client::class.java).resultList
}
