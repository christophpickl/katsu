package katsu.persistence

import mu.KotlinLogging.logger
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.callback.BaseCallback
import org.flywaydb.core.api.callback.Context
import org.flywaydb.core.api.callback.Event
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.flywaydb.core.internal.info.MigrationInfoImpl
import javax.persistence.EntityManager
import javax.sql.DataSource

class DatabaseMigrator(
    private val em: EntityManager
) {
    private val log = logger {}
    private val migrationLocation = "/katsu/migration"

    fun migrate() {
        log.info { "Migrating database ..." }
        val flyway = buildFlyway(em.dataSource)
        val migrations = flyway.migrate()
        log.info { "Migration done ($migrations steps)." }
    }

    private fun buildFlyway(dataSource: DataSource) = Flyway(FluentConfiguration()
        .dataSource(dataSource)
        .locations(migrationLocation)
        .callbacks(LoggingCallback)
    )

    private val EntityManager.dataSource
        get() = entityManagerFactory.properties["hibernate.connection.datasource"] as DataSource
}

private object LoggingCallback : BaseCallback() {
    private val log = logger {}
    override fun handle(event: Event, context: Context) {
        if (event == Event.BEFORE_EACH_MIGRATE) {
            val migration = (context.migrationInfo as MigrationInfoImpl).resolvedMigration
            log.debug {
                "Version: ${migration.version}, Description: ${migration.description} " +
                    "(Script: ${migration.script})"
            }
        }
    }
}
