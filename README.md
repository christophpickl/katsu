# Katsu

A shiatsu management desktop application, using Kotlin and TornadoFX (Kotlin-ish JavaFX).

It's a complete rewrite and slimmed version of the old [Gadsu](https://github.com/christophpickl/gadsu).


# Roadmap

* // do prefs hardcoded in kotlin (it's just for me)

## Phase 1 - MVP

* links liste mit namen (only)
* ein einziges grosses textfeld mit notiz (plus namensfeld)
* autosave (on client change, on exit)
* auto release: create GIT tag, backup old DB prod-data, and automatically replace *.app file

## Phase 2

* danach: rechts liste mit behandlungen (nur 4 entries zeigen)
	* ... wo drunter noch ein einziges grosses textfeld angezeigt wird (selber screen!)
* treatment list (single textfield, date)
* client picture: jpg+png, auto resize, auto crop
* rich text textfields (HTML? somehow make easy to highlight like with CMD+B)
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

## Phase 3

* extended client search/filter/ordering
* birthday reminder
* mail integration (templates; rich format html? own freeshiatsu address?) 
* calendar integration

## Phase X

* treatment goal indicator
* doodle integration
* manage invitations (store mail + mail type), manage response

# IT Driven

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
