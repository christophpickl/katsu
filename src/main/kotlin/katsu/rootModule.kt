package katsu

import katsu.persistence.persistenceModule
import katsu.service.serviceModule
import katsu.ui.uiModule
import org.kodein.di.Kodein
import org.kodein.di.tornadofx.installTornadoSource

fun appKodein() = Kodein {
    import(KoinModules.persistenceModule)
    import(KoinModules.serviceModule)
    import(KoinModules.uiModule)
    installTornadoSource()
}
