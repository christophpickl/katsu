package katsu

import katsu.ui.KatsuFxApp
import tornadofx.launch

object Katsu {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Starting up Katsu...")
        launch<KatsuFxApp>(args)
    }
}
