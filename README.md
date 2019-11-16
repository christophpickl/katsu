# Katsu

A shiatsu management desktop application, using Kotlin and TornadoFX (Kotlin-ish JavaFX).

It's a complete rewrite and slimmed version of the old [Gadsu](https://github.com/christophpickl/gadsu).

# Roadmap

# Phase 0
* confirm dialog on delete
* TODO: @OrderBy("${TreatmentDbo.COL_DATE} DESC") ... doesnt work :-/
* FINISH: rechts liste mit behandlungen (nur 4 entries zeigen)
* ... wo drunter noch ein einziges grosses textfeld angezeigt wird (selber screen!)
// prefs hardcoded in kotlin

# Phase 1
* client picture: jpg+png, auto resize, auto crop
* moooar fields (client ABC type, donation type, birthday, names, gender, text sections) 
* treatment list (single textfield, date)

# Phase 2
* client treatment cooldown, treatment counter indicator
* autosave (on client change, on exit)
* mail integration (templates; rich format html? own freeshiatsu address?) 

# Phase 3
* calendar integration
* doodle integration
* manage invitations (store mail + mail type), manage response
* treatment goal indicator


## IT Driven

* try TeamCity CI: https://www.jetbrains.com/teamcity/promo/free-ci/?
* make UI tests headless: https://vocabhunter.github.io/2016/07/27/TestFX.html
* introduce `buildSrc` gradle folder
* ? `Thread.setDefaultUncaughtExceptionHandler`?
* use more plugins? (kodein, javafx)
    * https://github.com/Kodein-Framework/Kodein-DI/blob/master/framework/tornadofx/kodein-di-framework-tornadofx-jvm/build.gradle.kts)

# Tech Notes

* get sure you are running with Java 8 (JRE; gradle as well)
