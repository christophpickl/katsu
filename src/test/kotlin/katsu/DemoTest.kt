package katsu

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.testng.annotations.Test

class DemoTest {
    @Test
    fun foo() {
        assertThat(2 + 2).isEqualTo(4)
    }
}