@file:SuppressWarnings("MatchingDeclarationName")

package katsu.persistence

import katsu.Environment
import katsu.Katsu
import katsu.KoinModules
import mu.KotlinLogging.logger
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.io.File
import java.util.concurrent.atomic.AtomicInteger
import javax.persistence.EntityManager

private val log = logger {}

val Katsu.Configuration.Directories.database
    get() = File(applicationHome, "database/db")

private val dbCounter = AtomicInteger(1)

@Suppress("unused")
val KoinModules.persistenceModule
    get() = Kodein.Module("Persistence Module") {
        log.info { "Opening database at: ${Katsu.Configuration.Directories.database.canonicalPath}" }
        bind<EntityManager>() with singleton {
            DatabaseOpener.open(
                HibernateConfig(
                    connection = when (Environment.current) {
                        Environment.UI_TEST -> HibernateConnection.InMemoryConnection(
                            "uiTest${dbCounter.getAndIncrement()}")
                        else -> HibernateConnection.FileConnection(Katsu.Configuration.Directories.database)
                    },
                    managedClasses = katsuManagedClasses
                )
            )
        }
        bind<ClientRepository>() with singleton { ClientRepositoryImpl(instance()) }
    }
