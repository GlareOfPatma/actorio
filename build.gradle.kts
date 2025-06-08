plugins {
    kotlin("jvm") version "2.1.21"
}

allprojects {
    group = "ru.luna"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }
}


dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

tasks.test {
    useJUnitPlatform()
}