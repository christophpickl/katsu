@file:SuppressWarnings("MatchingDeclarationName")

package katsu.ui

import javafx.scene.Node
import javafx.stage.Stage
import katsu.Environment
import org.testfx.api.FxRobot
import org.testfx.api.FxToolkit
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.function.Predicate

private val fileSafeDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy_MM_dd-HH_mm_ss")

fun FxRobot.saveScreenshot(stage: Stage) {
    robotContext().captureSupport.saveImage(
        capture(stage.scene.root).image,
        Paths.get("${fileSafeDateTimeFormatter.format(LocalDateTime.now())}.png")
    )
}

fun withKatsuFx(action: FxRobot.() -> Unit) {
    val previousEnvironment = Environment.current
    Environment.current = Environment.UI_TEST

    FxToolkit.registerPrimaryStage() // maybe do it @BeforeClass?
    val application = FxToolkit.setupApplication(KatsuFxApp::class.java) // maybe do this @BeforeMethod?
    val robot = FxRobot()

    try {
        robot.action()
    } finally {
        Environment.current = previousEnvironment
        FxToolkit.cleanupStages()
        FxToolkit.cleanupApplication(application)
    }
}

fun byId(id: String) = Predicate<Node> { it.id == id }
