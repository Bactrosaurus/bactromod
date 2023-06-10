plugins {
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.serialization") version "1.8.22"
    id("fabric-loom") version "1.2-SNAPSHOT"
    id("com.modrinth.minotaur") version "2.+"
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

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN")) // This is the default. Remember to have the MODRINTH_TOKEN environment variable set or else this will fail, or set it to whatever you want - just make sure it stays private!
    projectId.set("bactromod") // This can be the project ID or the slug. Either will work!
    // versionNumber.set("1.6") // You don't need to set this manually. Will fail if Modrinth has this version already
    versionType.set("release") // This is the default -- can also be `beta` or `alpha`
    uploadFile.set(tasks.remapJar) // With Loom, this MUST be set to `remapJar` instead of `jar`!
    gameVersions.addAll("1.20") // Must be an array, even with only one version
    // loaders.add("fabric") // Must also be an array - no need to specify this if you're using Loom or ForgeGradle
    dependencies { // A special DSL for creating dependencies
        // scope.type
        // The scope can be `required`, `optional`, `incompatible`, or `embedded`
        // The type can either be `project` or `version`
        required.project("fabric-language-kotlin")
        required.project("cloth-config")
        optional.version("modmenu")
    }
}