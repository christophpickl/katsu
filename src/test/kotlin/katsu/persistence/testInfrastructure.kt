@file:SuppressWarnings("MatchingDeclarationName")

package katsu.persistence

import katsu.model.ClientDbo
import katsu.model.TreatmentDbo
import java.io.Closeable
import java.util.concurrent.atomic.AtomicInteger
import javax.persistence.EntityManager
import kotlin.reflect.KClass

private val testDbCounter = AtomicInteger()
fun DatabaseOpener.openTestDb(managedClasses: List<KClass<*>>) = open(HibernateConfig(
    connection = HibernateConnection.InMemoryConnection("testDb${testDbCounter.getAndIncrement()}"),
    managedClasses = managedClasses
))

fun EntityManager.use(action: (EntityManager) -> Unit) {
    val em = this
    val closeable = Closeable {
        em.close()
    }
    closeable.use {
        action(em)
    }
}

fun withTestDb(
    migration: Boolean = true,
    managedClasses: List<KClass<*>> = katsuManagedClasses,
    action: TestDbContext.() -> Unit
) {
    DatabaseOpener.openTestDb(managedClasses).use { em ->
        val testDbContext = TestDbContext(em)
        if (migration) {
            testDbContext.migrate()
        }
        testDbContext.action()
    }
}

class TestDbContext(
    val em: EntityManager
) {
    fun migrate() {
        DatabaseMigrator(em).migrate()
    }

    fun persist(vararg entities: Any) {
        em.transactional {
            entities.forEach {
                persist(it)
            }
        }
    }

    fun EntityManager.findAllClients() =
        createQuery("from ${ClientDbo.ENTITY_NAME}", ClientDbo::class.java).resultList

    fun EntityManager.findAllTreatments() =
        createQuery("from ${TreatmentDbo.ENTITY_NAME}", TreatmentDbo::class.java).resultList
}
