# Katsu

A shiatsu management desktop application, using Kotlin and TornadoFX (Kotlin-ish JavaFX).

It's a complete rewrite and slimmed version of the old [Gadsu](https://github.com/christophpickl/gadsu).

# TODO

* configure travis (test, check, verify, validate)
* release: create GIT tag, backup old DB prod-data, and automatically replace *.app file
* tornadofx testing:
    * see: https://github.com/Kodein-Framework/Kodein-DI/blob/master/framework/tornadofx/kodein-di-framework-tornadofx-jvm/src/test/kotln/org/kodein/di/tornadofx/testapp.kt
    * use: testImplementation("org.testfx:testfx-core:4.0.4-alpha")
    * use: testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
* !!! MVP:
    * links liste mit namen (only)
    * ein einziges grosses textfeld mit notiz (plus namensfeld)
    * danach: rechts liste mit behandlungen (nur 4 entries zeigen)
    * ... wo drunter noch ein einziges grosses textfeld angezeigt wird (selber screen!)

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

# Tech Notes

* get sure you are running with Java 8 (JRE; gradle as well)
