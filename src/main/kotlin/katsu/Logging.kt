package katsu

import ch.qos.logback.classic.Level
import com.github.christophpickl.kpotpourri.logback4k.Logback4k
import java.io.File

object Logging {
    private val logsDirectory = File(Katsu.Configuration.Directories.applicationHome, "logs")

    fun configure(environment: Environment) {
        Logback4k.reconfigure {
            println("Reconfiguring log ...")
            rootLevel = Level.WARN
            packageLevel(Level.ALL, "katsu")
            when (environment) {
                Environment.PROD -> {
                    val filePrefix = "${logsDirectory.canonicalPath}/katsu"
                    addFileAppender(file = "$filePrefix.log", filePattern = "$filePrefix.%d{yyyy-MM-dd}.log") {
                        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] [%-28thread] %logger{60} - %msg%n"
                    }
                }
                Environment.DEV, Environment.UI_TEST -> {
                    addConsoleAppender {
                        pattern = "%d{HH:mm:ss.SSS} [%-5level] [%-28thread] %logger{60} - %msg%n"
                    }
                }
            }.hashCode() // enforce branch
        }
    }
}
