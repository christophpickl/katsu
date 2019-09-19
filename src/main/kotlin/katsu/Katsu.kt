package katsu

import katsu.ui.KatsuFxApp
import mu.KotlinLogging.logger
import tornadofx.launch
import java.io.File

object Katsu {
    object Configuration {
        object Directories {
            val applicationHome = File(
                System.getProperty("user.home"),
                if (Environment.isProd) ".katsu" else ".katsu_dev"
            )
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        Logging.configure(Environment.current)

        val log = logger {}
        log.info { "Starting up application..." }
        launch<KatsuFxApp>(args)
        log.info { "Main thread dying." }
    }
}

object KoinModules // register modules via extension properties
