@file:SuppressWarnings("MatchingDeclarationName")

package katsu.persistence

import java.io.Closeable
import javax.persistence.EntityManager
import kotlin.reflect.KClass

fun HibernateOpener.openTestDb(managedClasses: List<KClass<*>>) = open(HibernateConfig(
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

fun withTestDb(managedClasses: List<KClass<*>>, action: TestDbContext.() -> Unit) {
    HibernateOpener.openTestDb(managedClasses).use { em ->
        TestDbContext(em).action()
    }
}

class TestDbContext(
    val em: EntityManager
)
