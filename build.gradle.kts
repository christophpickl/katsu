version = 1.0

repositories {
    jcenter()
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.3.50"
    application
}

application {
    mainClassName = "katsu.App"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // implementation("io.github.microutils:kotlin-logging:1.7.6")
    // implementation("ch.qos.logback:logback-classic:1.2.3")

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
}
