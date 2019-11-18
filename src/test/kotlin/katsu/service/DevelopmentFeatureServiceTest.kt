package katsu.service

import assertk.assertThat
import assertk.assertions.isNotEmpty
import katsu.persistence.withTestDb
import org.testng.annotations.Test

@Test
class DevelopmentFeatureServiceTest {
    fun `When reset data Then there are some clients`() = withTestDb {
        DevelopmentFeatureService(em).resetDummyData()

        assertThat(em.findAllClients()).isNotEmpty()
    }
}
