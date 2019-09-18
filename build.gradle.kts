import com.autoscout24.gradle.TodoPluginExtension
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import edu.sc.seis.macAppBundle.MacAppBundlePluginExtension
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

version = 1.0


val myMainClassName = "katsu.Katsu"
val myAppName = "Katsu"

repositories {
    jcenter()
    mavenCentral()
    maven { setUrl("https://dl.bintray.com/kodein-framework/Kodein-DI") }
    maven { setUrl("https://dl.bintray.com/christophpickl/cpickl") }
}

buildscript {
    dependencies {
        classpath("com.autoscout24.gradle:gradle-todo-plugin:1.0")
        classpath("commons-io:commons-io:2.6") // needed by todoPlugin as of missing org/apache/commons/io/FilenameUtils
        classpath("gradle.plugin.edu.sc.seis:macAppBundle:2.2.3")
    }
}

plugins {
    kotlin("jvm") version "1.3.50"
    application
    jacoco
    id("org.jetbrains.kotlin.plugin.jpa") version "1.3.50"
    id("io.gitlab.arturbosch.detekt").version("1.0.1")
    id("com.github.ben-manes.versions") version "0.25.0"
}
apply(plugin = "com.autoscout24.gradle.todo")

application {
    mainClassName = myMainClassName
}

dependencies {
    // ATTENTION: for macApp to work, use _compile_ instead _implementation_!
    compile(kotlin("stdlib-jdk8"))

    compile("no.tornado:tornadofx:1.7.19") {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk7")
    }
    compile("com.github.christophpickl.kpotpourri:logback4k:2.4")
    compile("io.github.microutils:kotlin-logging:1.7.6")
    compile("org.kodein.di:kodein-di-generic-jvm:6.3.3")
    compile("org.kodein.di:kodein-di-framework-tornadofx-jvm:6.3.3")

    // PERSISTENCE
    compile("com.h2database:h2:1.4.199")
    compile("org.hibernate:hibernate-core:5.4.5.Final")
    compile("org.hibernate:hibernate-hikaricp:5.4.5.Final")
    compile("org.flywaydb:flyway-core:6.0.3")

    // TEST
    testImplementation("org.testng:testng:7.0.0")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.19")
    testImplementation("io.mockk:mockk:1.9.3")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    withType<Test> {
        useTestNG()
    }

    withType<Detekt> {
        config = files("src/main/build/detekt.yml")
        source("src/main/kotlin", "src/test/kotlin")
        reports {
            html.enabled = false
            xml.enabled = false
            txt.enabled = false
        }
    }

    named<DependencyUpdatesTask>("dependencyUpdates") {
        val rejectPatterns = listOf("alpha", "Alpha2", "beta", "eap").map { qualifier ->
            Regex("(?i).*[.-]$qualifier[.\\d-]*")
        }
        resolutionStrategy {
            componentSelection {
                all {
                    if (rejectPatterns.any { it.matches(candidate.version) }) {
                        reject("Release candidate")
                    }
                }
            }
        }
        checkForGradleUpdate = true
        outputFormatter = "json"
        outputDir = "build/reports"
        reportfileName = "dependencyUpdates"
    }

    withType<JacocoReport> {
        reports {
            html.isEnabled = false
            xml.isEnabled = false
            // executionData(withType<Test>())
        }
    }

    withType<JacocoCoverageVerification> {
        violationRules {
            rule {
                limit {
                    counter = "LINE"
                    value = "COVEREDRATIO"
                    minimum = BigDecimal(0.50)
                }
            }
        }
    }
}

configure<TodoPluginExtension> {
    todoPattern = "//[\\t\\s]*(TODO|FIXME) (.*)"
    failIfFound = true
    sourceFolder = "src" // to pick up main and test
    fileExtensions = listOf("kt")
}

// =====================================================================================================================
// PACKAGING MAC
// =====================================================================================================================

// travis lacks hdiutil binary, so only enabled by explicit sysproperty
if (System.getProperty("katsu.enableMacBundle") == "true") {
    println("[KATSU] enabling mac bundle")
    apply(plugin = "edu.sc.seis.macAppBundle")

    configure<MacAppBundlePluginExtension> {
        appName = myAppName
        mainClassName = myMainClassName
        jvmVersion = "1.8"
        icon = "src/main/build/logo.icns"
        // javaProperties.put("apple.laf.useScreenMenuBar", "true")
        // backgroundImage = "doc/macbackground.png"
//        javaProperties.put("katsu.isMacApp", "true")
//        if (System.getProperty("katsu.environment") == "prod") {
//            println "[KATSU] macApp is going to be in PROD mode!"
//            javaProperties.put("katsu.environment", "prod")
//        } else {
//            println "[KATSU] macApp is going to be in DEV mode"
//        }
    }
}
