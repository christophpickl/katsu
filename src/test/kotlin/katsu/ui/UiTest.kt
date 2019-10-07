package katsu.ui

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import javafx.scene.control.ListView
import javafx.scene.input.MouseButton
import katsu.ui.view.ViewIds
import org.testfx.api.FxRobot
import org.testfx.robot.Motion
import org.testng.annotations.Test

// https://github.com/TestFX/TestFX
// https://github.com/edvin/tornadofx/tree/master/src/test/kotlin/tornadofx/tests
@Test(groups = ["uiTest"])
class UiTest {

    fun `When add client Then client list size increased`() = withKatsuFx {
        val list = findClientList()
        assertThat(list.items).isEmpty()

        addClient()

        assertThat(list.items).hasSize(1)
    }

    fun `When add client Then client list size increased2`() = withKatsuFx {
        val list = findClientList()
        assertThat(list.items).isEmpty()

        addClient()

        assertThat(list.items).hasSize(1)
    }

    private fun FxRobot.findClientList() = lookup(byId(ViewIds.LIST_CLIENTS)).query<ListView<*>>()

    private fun FxRobot.addClient() {
        clickOn(byId(ViewIds.BUTTON_ADD_CLIENT), Motion.DIRECT, MouseButton.PRIMARY)
    }
}
