plugins {
    kotlin("jvm")
    `maven-publish`
}

dependencies {
    implementation(project(":library-core"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    implementation("ch.qos.logback:logback-classic:1.5.6")
}

kotlin {
    sourceSets.main {
        kotlin.srcDirs("src/")
    }
}