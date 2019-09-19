@file:SuppressWarnings("MatchingDeclarationName")

package katsu.ui

import katsu.KoinModules
import katsu.ui.controller.MainController
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

@Suppress("unused")
val KoinModules.uiModule
    get() = Kodein.Module("UI Module") {
        bind() from singleton { MainController() }
    }
