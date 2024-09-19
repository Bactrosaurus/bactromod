plugins {
    kotlin("jvm") version "2.0.20"
    id("fabric-loom") version "1.7-SNAPSHOT"
    kotlin("plugin.serialization") version "2.0.20"
}

group = "de.daniel"
version = "2.6"

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.2")

    minecraft("com.mojang:minecraft:1.21.1")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:0.16.5")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.12.2+kotlin.2.0.20")

    modApi("com.terraformersmc:modmenu:11.0.2")
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