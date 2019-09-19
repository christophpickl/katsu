package katsu.ui.view

import katsu.ui.appKodein
import katsu.ui.controller.MainController
import mu.KotlinLogging
import org.kodein.di.generic.instance
import tornadofx.View
import tornadofx.action
import tornadofx.button
import tornadofx.label
import tornadofx.vbox

class MainView : View() {
    init {
        title = "Katsu"
    }

    private val logg = KotlinLogging.logger {}
    private val controller: MainController by appKodein.instance()
    override val root = vbox {
        label("katsu here :)")
        button("click me").action {
            logg.debug { "button clicked" }
            println("Count clients: ${controller.fetchAllClients().size}")
        }
    }
}
