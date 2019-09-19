@file:SuppressWarnings("MatchingDeclarationName")

package katsu.ui

import javafx.stage.Stage
import org.testfx.api.FxRobot
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val fileSafeDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy_MM_dd-HH_mm_ss")

fun FxRobot.saveScreenshot(stage: Stage) {
    robotContext().captureSupport.saveImage(
        capture(stage.scene.root).image,
        Paths.get("${fileSafeDateTimeFormatter.format(LocalDateTime.now())}.png")
    )
}
