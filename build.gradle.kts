import com.autoscout24.gradle.TodoPluginExtension
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

version = 1.0

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
    }
}

plugins {
    kotlin("jvm") version "1.3.50"
    application
    id("org.jetbrains.kotlin.plugin.jpa") version "1.3.50"
    id("io.gitlab.arturbosch.detekt").version("1.0.1")
    id("com.github.ben-manes.versions") version "0.25.0"
}
apply(plugin = "com.autoscout24.gradle.todo")

application {
    mainClassName = "katsu.Katsu"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("no.tornado:tornadofx:1.7.19")
    implementation("com.github.christophpickl.kpotpourri:logback4k:2.4")
    implementation("io.github.microutils:kotlin-logging:1.7.6")
    implementation("org.kodein.di:kodein-di-generic-jvm:6.3.3")
    implementation("org.kodein.di:kodein-di-framework-tornadofx-jvm:6.3.3")

    // PERSISTENCE
    implementation("com.h2database:h2:1.4.199")
    implementation("org.hibernate:hibernate-core:5.4.4.Final")
    implementation("org.hibernate:hibernate-hikaricp:5.4.4.Final")

    // TEST
    testImplementation("org.testng:testng:7.0.0")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.19")
    testImplementation("io.mockk:mockk:1.9.3")
    //testImplementation("com.github.tomakehurst:wiremock:2.24.1") {
    //    exclude(group = "junit", module = "junit")
    //}
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
}

configure<TodoPluginExtension> {
    todoPattern = "//[\\t\\s]*(TODO|FIXME) (.*)"
    failIfFound = true
    sourceFolder = "src" // to pick up main and test
    fileExtensions = listOf("kt")
}