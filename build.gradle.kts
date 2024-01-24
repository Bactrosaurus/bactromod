plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    id("fabric-loom") version "1.5-SNAPSHOT"
}

group = "de.daniel"
version = "2.0"

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    minecraft("com.mojang:minecraft:1.20.4")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:0.15.6")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.10.17+kotlin.1.9.22")

    modApi("com.terraformersmc:modmenu:9.0.0")
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