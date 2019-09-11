package katsu.ui

import tornadofx.*

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