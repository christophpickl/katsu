@file:SuppressWarnings("MatchingDeclarationName")

package katsu.service

import katsu.KoinModules
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

@Suppress("unused")
val KoinModules.serviceModule
    get() = Kodein.Module("Service Module") {
        bind() from singleton { DevelopmentFeatureService(instance(), instance()) }
    }
