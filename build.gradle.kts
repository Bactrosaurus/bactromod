plugins {
    id("net.fabricmc.fabric-loom")
}

group = providers.gradleProperty("maven_group").get()
version = providers.gradleProperty("mod_version").get()

base {
    archivesName = providers.gradleProperty("archives_base_name")
}

repositories {
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:${providers.gradleProperty("minecraft_version").get()}")
    implementation("net.fabricmc:fabric-loader:${providers.gradleProperty("loader_version").get()}")
    implementation("net.fabricmc.fabric-api:fabric-api:${providers.gradleProperty("fabric_api_version").get()}")
    api("com.terraformersmc:modmenu:${providers.gradleProperty("modmenu_version").get()}")
}

tasks.processResources {
    inputs.property("version", version)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            mapOf(
                "version" to version,
                "loader_version" to providers.gradleProperty("loader_version").get(),
                "minecraft_version" to providers.gradleProperty("minecraft_version").get()
            )
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

java {
    withSourcesJar()
}

tasks.jar {
    inputs.property("archivesName", base.archivesName)

    from("LICENSE") {
        rename { "${it}_${base.archivesName.get()}" }
    }
}