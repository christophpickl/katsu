@file:SuppressWarnings("MatchingDeclarationName")

package katsu.persistence

import katsu.Katsu
import katsu.KoinModules
import katsu.model.ClientDbo
import mu.KotlinLogging.logger
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.io.File
import javax.persistence.EntityManager

private val log = logger {}
val katsuManagedClasses = listOf(ClientDbo::class)

val Katsu.Configuration.Directories.database
    get() = File(applicationHome, "database")

@Suppress("unused")
val KoinModules.persistenceModule
    get() = Kodein.Module("Persistence Module") {
        log.info { "Opening database at: ${Katsu.Configuration.Directories.database.canonicalPath}" }
        bind<EntityManager>() with singleton {
            DatabaseOpener.open(HibernateConfig(
                connection = HibernateConnection.FileConnection(Katsu.Configuration.Directories.database),
                managedClasses = katsuManagedClasses
            ))
        }
        bind<ClientRepository>() with singleton { ClientRepositoryImpl(instance()) }
    }
