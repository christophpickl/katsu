package katsu.ui

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import javafx.scene.control.ListView
import katsu.model.Client
import katsu.ui.view.ViewIds
import org.testfx.api.FxRobot
import org.testng.annotations.Test
import tornadofx.selectedItem

// https://github.com/TestFX/TestFX
// https://github.com/edvin/tornadofx/tree/master/src/test/kotlin/tornadofx/tests
@Test(groups = ["ui"])
class UiTest {

    private val firstName = "f"
    private val notes = "n"

    fun `When add client Then client is added and list size increased`() = withKatsuFx {
        clickById(ViewIds.BUTTON_ADD_CLIENT)

        val list = clientList()
        assertThat(list.selectionModel.selectedIndex).isEqualTo(0)
        assertThat(list.selectedItem?.toClient()).isEqualTo(Client(
            id = 1,
            firstName = "",
            notes = ""
        ))
        assertThat(list.items).hasSize(1)
    }

    fun `Given added client When change data and save Then fields in list are updated`() = withKatsuFx {
        val list = clientList()
        clickById(ViewIds.BUTTON_ADD_CLIENT)

        clickById(ViewIds.TEXT_FIRSTNAME)
        write(firstName)
        clickById(ViewIds.TEXT_NOTES)
        write(notes)
        clickById(ViewIds.BUTTON_SAVE_CLIENT)

        assertThat(list.items[0].toClient()).isEqualTo(Client(
            id = 1,
            firstName = firstName,
            notes = notes
        ))
    }

    private fun FxRobot.clientList() = lookup(byId(ViewIds.LIST_CLIENTS)).query<ListView<ClientUi>>()

    private fun FxRobot.clickById(id: String) {
        clickOn(byId(id))
    }
}
