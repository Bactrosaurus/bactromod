plugins {
    id("fabric-loom") version "1.15-SNAPSHOT"
}

group = "de.daniel"
version = "3.5"

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.11")
    mappings("net.fabricmc:yarn:1.21.11+build.4")
    modImplementation("net.fabricmc:fabric-loader:0.18.4")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.141.3+1.21.11")
    modApi("com.terraformersmc:modmenu:17.0.0-beta.2")
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