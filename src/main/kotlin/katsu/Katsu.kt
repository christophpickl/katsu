package katsu

import ch.qos.logback.classic.Level
import com.github.christophpickl.kpotpourri.logback4k.Logback4k
import katsu.ui.KatsuFxApp
import tornadofx.launch
import java.io.File

object Katsu {

    val HOME_DIRECTORY = File(System.getProperty("user.home"), if (Environment.isProd) ".katsu" else ".katsu_dev")
    val LOGS_DIRECTORY = File(HOME_DIRECTORY, "logs")

    @JvmStatic
    fun main(args: Array<String>) {
        initLogging()
        launch<KatsuFxApp>(args)
    }

    private fun initLogging() {
        Logback4k.reconfigure {
            rootLevel = Level.WARN
            packageLevel(Level.DEBUG, "katsu")
            when (Environment.current) {
                Environment.PROD -> {
                    val filePrefix = "${LOGS_DIRECTORY}/katsu"
                    addFileAppender(file = "$filePrefix.log", filePattern = "$filePrefix.%d{yyyy-MM-dd}.log") {
                        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] [%-28thread] %logger{60} - %msg%n"
                    }
                }
                Environment.DEV -> {
                    addConsoleAppender {
                        pattern = "%d{HH:mm:ss.SSS} [%-5level] [%-28thread] %logger{60} - %msg%n"
                    }
                }
            }.hashCode() // enforce branch
        }
    }
}
