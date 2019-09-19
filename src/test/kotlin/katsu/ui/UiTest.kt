package katsu.ui

import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.input.MouseButton
import javafx.scene.layout.VBox
import org.testfx.api.FxAssert
import org.testfx.api.FxRobot
import org.testfx.api.FxToolkit
import org.testfx.matcher.control.LabeledMatchers
import org.testfx.robot.Motion
import org.testng.annotations.Test
import tornadofx.action
import tornadofx.add
import tornadofx.button
import tornadofx.label
import java.util.function.Predicate

// https://github.com/TestFX/TestFX
// https://github.com/edvin/tornadofx/tree/master/src/test/kotlin/tornadofx/tests
@Test(groups = ["uiTest"])
class UiTest {
    fun `just a sample`() {
        val stage = FxToolkit.registerPrimaryStage()

        val root = FooPane()
        FxToolkit.setupFixture {
            stage.scene = Scene(root)
            stage.show()
        }

        FxAssert.verifyThat(root.label1, LabeledMatchers.hasText("hello"))
        val robot = FxRobot()
        robot.clickOn(Predicate<Node> { it.id == "foo" }, Motion.DIRECT, MouseButton.PRIMARY)
        try {
            FxAssert.verifyThat(root.label1, LabeledMatchers.hasText("griassi"))
        } catch (e: AssertionError) {
            robot.saveScreenshot(stage)
            throw e
        }
    }

    fun `Start app and click button`() {
        FxToolkit.registerPrimaryStage()
        FxToolkit.setupApplication(KatsuFxApp::class.java)
        val robot = FxRobot()
        robot.clickOn(Predicate<Node> { it.id == "btnClickMe" }, Motion.DIRECT, MouseButton.PRIMARY)
    }
}

class FooPane : VBox() {
    val label1 = label(text = "hello")
    init {
        add(label1)
        add(label(text = "katsu"))
        add(button(text = "click me").apply {
            id = "foo"
            action {
                label1.text = "griassi"
                println("click me clicked")
            }
        })
    }
}
