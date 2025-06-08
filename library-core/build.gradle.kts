plugins {
    kotlin("jvm")
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    testImplementation(kotlin("test"))

}

kotlin {
    sourceSets.main {
        kotlin.srcDirs("src/")
    }
    sourceSets.test {
        kotlin.srcDirs("test/")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "actorio"
            groupId = "ru.luna"
            version = "1.0.0"
        }
    }
}
