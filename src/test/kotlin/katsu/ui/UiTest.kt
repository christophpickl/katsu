package katsu.ui

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import javafx.scene.control.ListView
import katsu.ui.view.ViewIds
import org.testfx.api.FxRobot
import org.testng.annotations.Test

// https://github.com/TestFX/TestFX
// https://github.com/edvin/tornadofx/tree/master/src/test/kotlin/tornadofx/tests
@Test(groups = ["uiTest"])
class UiTest {

    fun `When add client Then client list size increased`() = withKatsuFx {
        val list = findClientList()
        assertThat(list.items).isEmpty()

        clickAddClient()

        assertThat(list.items).hasSize(1)
    }

    fun `Given two clients When change data and save and go back and forth Then fields are updated`() = withKatsuFx {
        val list = findClientList()

        clickAddClient()
        clickAddClient()

        sleep(500)
        clickOn(byId(ViewIds.TEXT_FIRSTNAME))
        sleep(2_000)

        assertThat(list.items).hasSize(1)
    }

    private fun FxRobot.findClientList() = lookup(byId(ViewIds.LIST_CLIENTS)).query<ListView<*>>()

    private fun FxRobot.clickAddClient() {
        clickOn(byId(ViewIds.BUTTON_ADD_CLIENT))
    }
}
