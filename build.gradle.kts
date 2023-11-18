plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.20"
    id("fabric-loom") version "1.4-SNAPSHOT"
}

group = "de.daniel"
version = "1.9"

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    minecraft("com.mojang:minecraft:1.20.2")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:0.14.24")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.10.14+kotlin.1.9.20")

    modApi("com.terraformersmc:modmenu:8.0.0")
}

loom {
    accessWidenerPath = file("src/main/resources/bactromod.accesswidener")
}

kotlin {
    jvmToolchain(17)
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }
}