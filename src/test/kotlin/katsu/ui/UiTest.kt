package katsu.ui

import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.input.MouseButton
import javafx.scene.layout.VBox
import org.testfx.api.FxRobot
import org.testfx.api.FxToolkit
import org.testfx.robot.Motion
import org.testng.annotations.Test
import tornadofx.action
import tornadofx.add
import tornadofx.button
import tornadofx.label
import java.util.function.Predicate

// https://github.com/edvin/tornadofx/tree/master/src/test/kotlin/tornadofx/tests
@Test(groups = ["uiTest"])
class UiTest {
    fun `just a sample`() {
        val stage = FxToolkit.registerPrimaryStage()

        FxToolkit.setupFixture {
            val root = FooPane()
            stage.scene = Scene(root)
            stage.show()
        }

        val robot = FxRobot()
//        robot.robotContext().captureSupport.saveImage(
//            robot.capture(stage.scene.root).image,
//            Paths.get("foo.png")
//        )
        robot.clickOn(Predicate<Node> { it.id == "foo" }, Motion.DIRECT, MouseButton.PRIMARY)
    }
}

class FooPane : VBox() {
    init {
        add(label(text = "hello"))
        add(label(text = "katsu"))
        add(button(text = "click me").apply {
            id = "foo"
            action {
                println("click me clicked")
            }
        })
    }
}
