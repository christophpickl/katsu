# Katsu

A shiatsu management desktop application, using Kotlin and TornadoFX (Kotlin-ish JavaFX).

It's a complete rewrite and slimmed version of the old [Gadsu](https://github.com/christophpickl/gadsu).

# TODO

* !!! MVP:
    * ein einziges grosses textfeld mit notiz (plus namensfeld)
    * danach: rechts liste mit behandlungen (nur 4 entries zeigen)
    * ... wo drunter noch ein einziges grosses textfeld angezeigt wird (selber screen!)
* release: create GIT tag, backup old DB prod-data, and automatically replace *.app file

# Roadmap

# Phase 0
* UI tests
* auto release
* autosave (on client change, on exit)
// prefs hardcoded in kotlin

# Phase 1
* treatment list (single textfield, date)
* client picture: jpg+png, auto resize, auto crop
* rich text textfields
* moooar fields (client ABC type, donation type, birthday, names, gender, text sections) 
* client treatment cooldown, treatment counter indicator

# Phase 2
* treatment goal indicator
* mail integration (templates; rich format html? own freeshiatsu address?) 
* calendar integration
* doodle integration
* manage invitations (store mail + mail type), manage response

## IT Driven

* introduce `buildSrc` gradle folder
* ? `Thread.setDefaultUncaughtExceptionHandler`?
* use more plugins? (kodein, javafx)
    * https://github.com/Kodein-Framework/Kodein-DI/blob/master/framework/tornadofx/kodein-di-framework-tornadofx-jvm/build.gradle.kts)

# Tech Notes

* get sure you are running with Java 8 (JRE; gradle as well)
