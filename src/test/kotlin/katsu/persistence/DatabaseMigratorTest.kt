package katsu.persistence

import org.testng.annotations.Test

@Test
class DatabaseMigratorTest {
    fun `migrate flyway`() = withTestDb {
        DatabaseMigrator(em).migrate()
    }
}
