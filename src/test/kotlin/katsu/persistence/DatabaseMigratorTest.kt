package katsu.persistence

import org.testng.annotations.Test

@Test
class DatabaseMigratorTest {
    fun `migrate flyway`() = withTestDb(migration = false) {
        DatabaseMigrator(em).migrate()
    }
}
