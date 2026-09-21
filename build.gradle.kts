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
    testImplementation(platform("org.junit:junit-bom:${gradleProperty("junit_version")}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    dependsOn(tasks.jar)
    inputs.file(tasks.jar.flatMap { it.archiveFile })
    workingDir = layout.buildDirectory.dir("unit-test").get().asFile
    doFirst { workingDir.mkdirs() }
    systemProperty("bactromod.test.jar", tasks.jar.get().archiveFile.get().asFile.absolutePath)
    systemProperty("bactromod.test.version", project.version.toString())
}

fabricApi {
    configureTests {
        createSourceSet = true
        modId = "bactromod-test"
        enableGameTests = false
        enableClientGameTests = true
    }
}

loom {
    runConfigs.named("clientGameTest") {
        systemProperties.put("mixin.debug.countInjections", "true")
    }
}

tasks.processResources {
    val properties = mapOf(
        "version" to version,
        "loader_version" to gradleProperty("loader_version"),
        "minecraft_version" to gradleProperty("minecraft_version"),
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
