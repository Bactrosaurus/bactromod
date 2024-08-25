package de.daniel.bactromod

import de.daniel.bactromod.config.Config
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Path

val systemIsWindows11: Boolean = System.getProperty("os.name") == "Windows 11"
val configPath: Path = FabricLoader.getInstance().configDir.resolve("bactromod.json")

fun init() {
    println("Initializing BactroMod...")
    Config.init()
}