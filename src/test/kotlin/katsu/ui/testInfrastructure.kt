@file:SuppressWarnings("MatchingDeclarationName")

package katsu.ui

import javafx.scene.Node
import javafx.stage.Stage
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
    System.setProperty("katsu.env", "ui_test")

    FxToolkit.registerPrimaryStage()
    val application = FxToolkit.setupApplication(KatsuFxApp::class.java)
    val robot = FxRobot()

    robot.action()

    FxToolkit.cleanupStages()
    FxToolkit.cleanupApplication(application)
}

fun byId(id: String) = Predicate<Node> { it.id == id }
