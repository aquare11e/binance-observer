import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
}

group = "me.rkomarov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val ktorVersion: String by project
val krontabVersion: String by project
val influxdbVersion: String by project
val muLoggingVersion: String by project
val logbackVersion: String by project

dependencies {
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    implementation("dev.inmo:krontab:$krontabVersion")

    implementation("com.influxdb:influxdb-client-kotlin:$influxdbVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:$muLoggingVersion")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveFileName.set("binobs.jar")
        destinationDirectory.set(File("build/fatjar"))
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "me.rkomarov.binobs.MainKt"))
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}