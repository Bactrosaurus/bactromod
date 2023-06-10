plugins {
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.serialization") version "1.8.22"
    id("fabric-loom") version "1.2-SNAPSHOT"
}

group = "de.daniel"
version = "1.6"

repositories {
    mavenCentral()
    maven("https://maven.quiltmc.org/repository/release/")
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.shedaniel.me/")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    minecraft("com.mojang:minecraft:1.20")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:0.14.21")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.83.0+1.20")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.9.5+kotlin.1.8.22")

    modApi("com.terraformersmc:modmenu:7.0.1")
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