package katsu.ui

import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.kodein.di.tornadofx.installTornadoSource
import tornadofx.App
import tornadofx.Controller
import tornadofx.View
import tornadofx.action
import tornadofx.button
import tornadofx.label
import tornadofx.vbox

val uiModule = Kodein.Module("UI Module") {
    bind<DemoRepository>() with singleton { DemoRepositoryImpl() }
    bind() from singleton { MainController(instance()) }
}

val appKodein = Kodein {
    import(uiModule)
    installTornadoSource()
}

class KatsuFxApp : App(
    primaryView = MainView::class
), KodeinAware {
    // https://kodein.org/Kodein-DI/?6.3/tornadofx
    override val kodein = appKodein
}

interface DemoRepository {
    fun greeting(): String
}

class DemoRepositoryImpl : DemoRepository {
    override fun greeting(): String {
        return "hello"
    }
}

class MainController(private val repository: DemoRepository) : Controller() {
    //private val repository: DemoRepository by kodein().instance()
    fun greet(name: String) = "${repository.greeting()} $name!"
}

class MainView : View() {
    init {
        title = "Katsu"
    }

    private val controller: MainController by appKodein.instance()
    override val root = vbox {
        label("katsu here :)")
        button("click me").action {
            println(controller.greet("katsu"))
        }
    }
}
