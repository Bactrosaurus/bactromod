plugins {
    kotlin("jvm") version "2.0.0"
    id("fabric-loom") version "1.7-SNAPSHOT"
    kotlin("plugin.serialization") version "2.0.0"
}

group = "de.daniel"
version = "2.3"

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    minecraft("com.mojang:minecraft:1.21")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:0.15.11")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.100.7+1.21")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.11.0+kotlin.2.0.0")

    modApi("com.terraformersmc:modmenu:11.0.0")
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