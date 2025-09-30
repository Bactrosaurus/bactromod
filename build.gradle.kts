plugins {
    id("fabric-loom") version "1.11-SNAPSHOT"
}

group = "de.daniel"
version = "3.2"

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.9")
    mappings("net.fabricmc:yarn:1.21.9+build.1")
    modImplementation("net.fabricmc:fabric-loader:0.17.2")
    modApi("com.terraformersmc:modmenu:15.0.0")
}

loom {
    accessWidenerPath = file("src/main/resources/bactromod.accesswidener")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
}