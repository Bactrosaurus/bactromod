plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
    id("fabric-loom") version "1.3-SNAPSHOT"
}

group = "de.daniel"
version = "1.7"

repositories {
    mavenCentral()
    maven("https://maven.quiltmc.org/repository/release/")
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.shedaniel.me/")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")

    minecraft("com.mojang:minecraft:1.20.1")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:0.14.21")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.84.0+1.20.1")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.10.0+kotlin.1.9.0")

    modApi("com.terraformersmc:modmenu:7.1.0")
    modApi("me.shedaniel.cloth:cloth-config-fabric:11.0.99") {
        exclude(group = "net.fabricmc.fabric-api")
    }
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