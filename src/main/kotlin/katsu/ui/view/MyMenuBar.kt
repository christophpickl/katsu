package katsu.ui.view

import de.codecentric.centerdevice.MenuToolkit
import javafx.scene.control.MenuBar
import katsu.persistence.ClientRepository
import katsu.service.DevelopmentFeatureService
import katsu.ui.ClientsReloadedEvent
import mu.KotlinLogging.logger
import org.kodein.di.generic.instance
import org.kodein.di.tornadofx.kodein
import tornadofx.Controller
import tornadofx.action
import tornadofx.item
import tornadofx.menu
import java.util.Locale

class MenuBarController : Controller() {

    private val logg = logger {}
    private val service: DevelopmentFeatureService by kodein().instance()
    private val repository: ClientRepository by kodein().instance()

    fun resetDummyData() {
        logg.debug { "reset dummy data" }
        service.resetDummyData()
        fire(ClientsReloadedEvent(
            clients = repository.fetchAll().map { it.toClient() }
        ))
    }
}

class MyMenuBar(
    private val controller: MenuBarController
) : MenuBar() {

    init {
        menu("Develop") {
            item("Reset Dummy Data").action {
                controller.resetDummyData()
            }
        }
    }
}


fun setGlobalMacMenuBar(mybar: MyMenuBar) {
    MenuToolkit.toolkit(Locale.getDefault()).also { tk ->
        val aboutStage = de.codecentric.centerdevice.dialogs.about.AboutStageBuilder
            .start("About")
            .withAppName("Katsu")
            .withCloseOnFocusLoss()
            .withHtml("""https://github.com/christophpickl/katsu""")
            .withVersionString("X.Y")
            .build()

        tk.setApplicationMenu(tk.createDefaultApplicationMenu("Katsu", aboutStage))
        tk.setGlobalMenuBar(mybar)
    }
}
