plugins {
    id("net.fabricmc.fabric-loom")
}

fun gradleProperty(name: String) = providers.gradleProperty(name).get()

group = gradleProperty("maven_group")
version = gradleProperty("mod_version")

base {
    archivesName = gradleProperty("archives_base_name")
}

repositories {
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:${gradleProperty("minecraft_version")}")
    implementation("net.fabricmc:fabric-loader:${gradleProperty("loader_version")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${gradleProperty("fabric_api_version")}")
    compileOnly("com.terraformersmc:modmenu:${gradleProperty("modmenu_version")}")
}

tasks.processResources {
    // Fabric Loader normalizes snapshots (26.4-snapshot-7 -> 26.4-alpha.7), so pre-release builds
    // depend on the whole release line ("~26.4-"); a release such as 26.4 keeps "~26.4".
    val minecraftVersion = gradleProperty("minecraft_version")
    val minecraftDependency = if ('-' in minecraftVersion) "~${minecraftVersion.substringBefore('-')}-" else "~$minecraftVersion"
    val properties = mapOf(
        "version" to version,
        "loader_version" to gradleProperty("loader_version"),
        "minecraft_dependency" to minecraftDependency,
        "fabric_api_version" to gradleProperty("fabric_api_version"),
        "modmenu_version" to gradleProperty("modmenu_version")
    )
    inputs.properties(properties)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(properties)
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
