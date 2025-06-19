plugins {
    kotlin("jvm") version "2.1.21"
    id("fabric-loom") version "1.10-SNAPSHOT"
    kotlin("plugin.serialization") version "2.1.21"
}

group = "de.daniel"
version = "2.9"

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")

    minecraft("com.mojang:minecraft:1.21.6")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:0.16.14")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.13.3+kotlin.2.1.21")

    modApi("com.terraformersmc:modmenu:15.0.0-beta.2")
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