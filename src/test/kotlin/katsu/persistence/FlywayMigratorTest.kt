package katsu.persistence

import org.testng.annotations.Test

@Test
class FlywayMigratorTest {
    fun `migrate flyway`() = withTestDb(emptyList()) {
        FlywayMigrator(em).migrate()
    }
}
