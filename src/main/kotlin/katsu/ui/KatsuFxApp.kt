package katsu.ui

import tornadofx.App
import tornadofx.View
import tornadofx.action
import tornadofx.button
import tornadofx.label
import tornadofx.vbox

class KatsuFxApp : App(
    primaryView = MainView::class
)

class MainView : View() {
    init {
        title = "Katsu"
    }

    override val root = vbox {
        label("hello")
        button("world").action {
            println("clicked")
        }
    }

}
