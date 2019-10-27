# Katsu

A shiatsu management desktop application, using Kotlin and TornadoFX (Kotlin-ish JavaFX).

It's a complete rewrite and slimmed version of the old [Gadsu](https://github.com/christophpickl/gadsu).

# Roadmap

# Phase 0
* rich text textfields (HTML? somehow make easy to highlight like with CMD+B)
* FINISH: rechts liste mit behandlungen (nur 4 entries zeigen)
    * persistence done; needs UI and integration

# Phase 1
* treatment list (single textfield, date)
* client picture: jpg+png, auto resize, auto crop
* moooar fields (client ABC type, donation type, birthday, names, gender, text sections)
* moooar client fields
	- client ABC type
	- donation type
	- birthday
	- email
	- names {first name, last name, nick name, internal name}
	- gender
	- accept DSGVO, accept mails
* mooar treatment fields:
	- text sections
	- donation money amount (can be used to calculate "client importance") 
* client treatment cooldown, treatment counter indicator
* treatment list (single textfield, date)

# Phase 2
* client treatment cooldown, treatment counter indicator
* autosave (on client change, on exit)
* extended client search/filter/ordering
* birthday reminder
* mail integration (templates; rich format html? own freeshiatsu address?) 

# Phase 3
* calendar integration
* treatment goal indicator
* doodle integration
* manage invitations (store mail + mail type), manage response
* treatment goal indicator


# IT Driven

* !! TODO: @OrderBy("${TreatmentDbo.COL_DATE} DESC") ... doesnt work :-/
* try TeamCity CI: https://www.jetbrains.com/teamcity/promo/free-ci/?
* make UI tests headless: https://vocabhunter.github.io/2016/07/27/TestFX.html
* introduce `buildSrc` gradle folder
* ? `Thread.setDefaultUncaughtExceptionHandler`?
* use more plugins? (kodein, javafx)
    * https://github.com/Kodein-Framework/Kodein-DI/blob/master/framework/tornadofx/kodein-di-framework-tornadofx-jvm/build.gradle.kts


# Notes Vision

* verwaltung von klienten
** suche + filter
** foto in liste!
* V2: cooldown feature (A/B/C klient, nix/geschenk/geld, last treatment)
** verwaltung von behandlungen
** zukuenftige behandlungen == termine ;)
** NO: meridiane, hara => mehr freitext
* V3:
** mails senden
** verwaltung von einladungen (mitzaehlen)
** gcal integration
** doodle integration
