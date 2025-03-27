plugins {
    kotlin("jvm") version "2.1.20"
    id("fabric-loom") version "1.10-SNAPSHOT"
    kotlin("plugin.serialization") version "2.1.20"
}

group = "de.daniel"
version = "2.8"

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    minecraft("com.mojang:minecraft:1.21.5")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:0.16.10")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.13.2+kotlin.2.1.20")

    modApi("com.terraformersmc:modmenu:14.0.0-rc.2")
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