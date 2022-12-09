plugins {
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.serialization") version "1.7.22"
    id("fabric-loom") version "1.0-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower") version "1.8.0"
}

group = "de.daniel"
version = "1.4"

repositories {
    mavenCentral()
    maven("https://maven.quiltmc.org/repository/release/")
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://maven.shedaniel.me/")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    minecraft("com.mojang:minecraft:1.19.3")
    mappings(loom.layered {
        mappings("org.quiltmc:quilt-mappings:1.19.3+build.2:intermediary-v2")
        officialMojangMappings()
    })

    modImplementation("net.fabricmc:fabric-loader:0.14.11")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.68.1+1.19.3")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.8.7+kotlin.1.7.22")

    modApi("com.terraformersmc:modmenu:5.0.1")
    modApi("me.shedaniel.cloth:cloth-config-fabric:9.0.94") {
        exclude(group = "net.fabricmc.fabric-api")
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(17)
    }
}