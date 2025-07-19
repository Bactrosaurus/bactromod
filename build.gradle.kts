plugins {
    kotlin("jvm") version "2.2.0"
    id("fabric-loom") version "1.11-SNAPSHOT"
    kotlin("plugin.serialization") version "2.2.0"
}

group = "de.daniel"
version = "3.1"

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.8")
    mappings("net.fabricmc:yarn:1.21.8+build.1")

    modImplementation("net.fabricmc:fabric-loader:0.16.14")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.13.4+kotlin.2.2.0")

    modApi("com.terraformersmc:modmenu:15.0.0-beta.3")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
}

loom {
    accessWidenerPath = file("src/main/resources/bactromod.accesswidener")
}

kotlin {
    jvmToolchain(21)
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
}