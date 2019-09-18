# katsu
shiatsu management desktop application

# TODO

* DB migration
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

## Phase 2

* introduce `buildSrc` gradle folder

# Tech Notes

* get sure you are running with Java 8 (JRE; gradle as well)
