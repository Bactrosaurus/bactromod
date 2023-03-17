plugins {
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"
    id("fabric-loom") version "1.1-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower") version "1.8.0"
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
    mappings(loom.layered {
        mappings("org.quiltmc:quilt-mappings:1.19.4+build.3:intermediary-v2")
        officialMojangMappings()
    })

    modImplementation("net.fabricmc:fabric-loader:0.14.17")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.76.0+1.19.4")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.9.2+kotlin.1.8.10")

    modApi("com.terraformersmc:modmenu:6.1.0-rc.4")
    modApi("me.shedaniel.cloth:cloth-config-fabric:10.0.96") {
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