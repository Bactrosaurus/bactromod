plugins {
    kotlin("jvm") version "2.0.21"
    id("fabric-loom") version "1.8-SNAPSHOT"
    kotlin("plugin.serialization") version "2.0.21"
}

group = "de.daniel"
version = "2.6"

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    minecraft("com.mojang:minecraft:1.21.3")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:0.16.9")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.12.3+kotlin.2.0.21")

    modApi("com.terraformersmc:modmenu:12.0.0-beta.1")
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