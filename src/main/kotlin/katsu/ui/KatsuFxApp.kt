package katsu.ui

import javafx.stage.Stage
import katsu.KoinModules
import katsu.persistence.DatabaseMigrator
import katsu.persistence.persistenceModule
import katsu.ui.controller.MainController
import katsu.ui.view.MainView
import mu.KotlinLogging.logger
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import org.kodein.di.tornadofx.installTornadoSource
import tornadofx.App
import tornadofx.find
import javax.persistence.EntityManager

val appKodein = Kodein {
    import(KoinModules.uiModule)
    import(KoinModules.persistenceModule)
    installTornadoSource()
}

class KatsuFxApp : App(
    primaryView = MainView::class
), KodeinAware {

    init {
        // eager load controller to make event subscription work
        find(MainController::class)
    }

    private val log = logger {}

    override val kodein = appKodein
    private val em by kodein.instance<EntityManager>()

    override fun start(stage: Stage) {
        super.start(stage)
        DatabaseMigrator(em).migrate()
    }

    override fun stop() {
        log.debug { "Stop invoked, closing DB." }
        em.close()
        super.stop()
    }
}
