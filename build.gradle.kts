plugins {
    id("fabric-loom") version "1.11-SNAPSHOT"
}

group = "de.daniel"
version = "3.1"

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.8")
    mappings("net.fabricmc:yarn:1.21.8+build.1")
    modImplementation("net.fabricmc:fabric-loader:0.16.14")
    modApi("com.terraformersmc:modmenu:15.0.0-beta.3")
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