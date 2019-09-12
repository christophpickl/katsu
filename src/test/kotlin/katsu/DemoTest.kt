package katsu

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.testng.annotations.Test

@Test
class DemoTest {
    fun foo() {
        assertThat(2 + 2).isEqualTo(4)
    }
}
