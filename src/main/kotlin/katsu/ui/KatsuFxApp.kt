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
import javax.persistence.EntityManager

fun appKodein() = Kodein {
    import(KoinModules.uiModule)
    import(KoinModules.persistenceModule)
    installTornadoSource()
}

class KatsuFxApp : App(
    primaryView = MainView::class
), KodeinAware {

    private val log = logger {}

    override var kodein = appKodein()

    override fun start(stage: Stage) {
        val em by kodein.instance<EntityManager>()
        DatabaseMigrator(em).migrate()
        super.start(stage)
        val controller by kodein.instance<MainController>()
        controller.start()
    }

    override fun stop() {
        log.debug { "Stop invoked, closing DB." }
        val em by kodein.instance<EntityManager>()
        em.close()
        val controller by kodein.instance<MainController>()
        controller.unsubscribeAll()
        val view by kodein.instance<MainView>()
        view.unsubscribeAll()
        super.stop()
    }
}
