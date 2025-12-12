plugins {
    id("fabric-loom") version "1.14-SNAPSHOT"
}

group = "de.daniel"
version = "3.4"

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.11")
    mappings("net.fabricmc:yarn:1.21.11+build.3")
    modImplementation("net.fabricmc:fabric-loader:0.18.2")
    modApi("com.terraformersmc:modmenu:17.0.0-alpha.1")
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