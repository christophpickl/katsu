package katsu

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.testng.annotations.Test

@Test
class KatsuTest {
    fun `Given DEV environment When get application home Then combine user home directory with katsu DEV folder`() {
        System.setProperty("katsu.env", "dev")

        val applicationHome = Katsu.Configuration.Directories.applicationHome.canonicalPath

        assertThat(applicationHome).isEqualTo(System.getProperty("user.home") + "/.katsu_dev")
    }
}
