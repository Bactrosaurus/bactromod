package de.daniel.bactromod

import de.daniel.bactromod.config.Config
import de.daniel.bactromod.bettersky.BetterSkyRenderer
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry
import net.minecraft.world.level.Level

fun init() {
    println("Initializing BactroMod...")
    Config.init()
    // DimensionRenderingRegistry.registerSkyRenderer(Level.OVERWORLD, BetterSkyRenderer())
}