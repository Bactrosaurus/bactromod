plugins {
    kotlin("jvm") version "1.8.20"
    kotlin("plugin.serialization") version "1.8.20"
    id("fabric-loom") version "1.1-SNAPSHOT"
}

group = "de.daniel"
version = "1.5"

repositories {
    mavenCentral()
    maven("https://maven.quiltmc.org/repository/release/")
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.shedaniel.me/")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    minecraft("com.mojang:minecraft:1.19.4")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:0.14.19")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.78.0+1.19.4")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.9.3+kotlin.1.8.20")

    modApi("com.terraformersmc:modmenu:6.2.0")
    modApi("me.shedaniel.cloth:cloth-config-fabric:10.0.96") {
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