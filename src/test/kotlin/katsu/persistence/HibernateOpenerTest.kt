package katsu.persistence

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.extracting
import org.testng.annotations.Test
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Test
class HibernateOpenerTest {
    fun `When connect and persist and fetch Then return that entity`() {
        val em = HibernateOpener.open(HibernateConfig(
            connection = HibernateConnection.InMemoryConnection("testDb"),
            managedClasses = listOf(TestEntity::class)
        ))
        em.transaction.begin()
        em.persist(TestEntity("testName"))
        em.transaction.commit()

        val entities = em.createQuery("from ${TestEntity.ENTITY_NAME}", TestEntity::class.java).resultList
        assertThat(entities).extracting { it.name }.containsExactly("testName")
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
