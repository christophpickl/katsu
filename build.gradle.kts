import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

version = 1.0

repositories {
    jcenter()
    mavenCentral()
    maven { setUrl("https://dl.bintray.com/kodein-framework/Kodein-DI") }
}

plugins {
    kotlin("jvm") version "1.3.50"
    application
    id("org.jetbrains.kotlin.plugin.jpa") version "1.3.21"
    id("io.gitlab.arturbosch.detekt").version("1.0.1")
}

application {
    mainClassName = "katsu.Katsu"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("no.tornado:tornadofx:1.7.19")
    // implementation("io.github.microutils:kotlin-logging:1.7.6")
    // implementation("ch.qos.logback:logback-classic:1.2.3")
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

    // https://github.com/Kodein-Framework/Kodein-DI/blob/master/framework/tornadofx/kodein-di-framework-tornadofx-jvm/src/test/kotln/org/kodein/di/tornadofx/testapp.kt
//    testImplementation("org.testfx:testfx-core:4.0.4-alpha")
//    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
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
}
