package katsu

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.testng.annotations.Test

@Test
class EnvironmentTest {
    fun `Given no system property When get current Then default to DEV`() {
        assertThat(Environment.current).isEqualTo(Environment.DEV)
    }
}
