package de.daniel.bactromod

import de.daniel.bactromod.config.Config
import de.daniel.bactromod.impl.SystemInfo

fun init() {
    println("Initializing BactroMod...")
    SystemInfo
    Config.init()
}