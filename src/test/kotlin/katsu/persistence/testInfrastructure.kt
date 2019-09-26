@file:SuppressWarnings("MatchingDeclarationName")

package katsu.persistence

import java.io.Closeable
import javax.persistence.EntityManager
import kotlin.reflect.KClass

fun DatabaseOpener.openTestDb(managedClasses: List<KClass<*>>) = open(HibernateConfig(
    connection = HibernateConnection.InMemoryConnection("testDb"),
    managedClasses = managedClasses
))

fun EntityManager.use(action: (EntityManager) -> Unit) {
    val em = this
    val closeable = Closeable { em.close() }
    closeable.use {
        action(em)
    }
}

fun withTestDb(migration: Boolean = true, managedClasses: List<KClass<*>> = katsuManagedClasses, action: TestDbContext.() -> Unit) {
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
)
