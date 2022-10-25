package de.daniel.bactromod

import de.daniel.bactromod.config.Config
import kotlinx.coroutines.runBlocking

fun init() = runBlocking {
    println("Initializing BactroMod...")
    Config.init()
}