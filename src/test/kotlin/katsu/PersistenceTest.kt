package katsu

import assertk.assertThat
import assertk.assertions.hasSize
import org.testng.annotations.Test
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Test
class PersistenceTest {
    fun `When connect and persist and fetch Then return that entity`() {
        val em = HibernateManager.open(HibernateConfig(
            connection = HibernateConnection.InMemoryConnection("test"),
            managedClasses = listOf(TestEntity::class)
        ))
        em.transaction.begin()
        em.persist(TestEntity("bar"))
        em.transaction.commit()

        val foos = em.createQuery("from ${TestEntity.ENTITY_NAME}", TestEntity::class.java).resultList
        assertThat(foos).hasSize(1)
        em.close()
    }
}

@Entity(name = TestEntity.ENTITY_NAME)
@Table
private data class TestEntity(
    @Id
    val name: String
) {
    companion object {
        const val ENTITY_NAME = "TestEntity"
    }
}
