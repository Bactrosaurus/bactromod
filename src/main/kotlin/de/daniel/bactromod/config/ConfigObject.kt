package de.daniel.bactromod.config

import kotlinx.serialization.Serializable

@Serializable
data class ConfigObject(
    var gammaMultiplier: Int = 15,
    var disablePumpkinBlur: Boolean = true,
    var fireOffset: Int = -30,
    var shieldOffset: Int = -20,
    var showHypixelFairySouls: Boolean = false,
    var disableBlindnessFog: Boolean = true,
    var disableDarknessFog: Boolean = true,
    var disableLavaFog: Boolean = true,
    var disablePowderSnowFog: Boolean = true,
    var disableSkyFog: Boolean = true,
    var disableTerrainFog: Boolean = true,
    var disableThickFog: Boolean = true,
    var disableWaterFog: Boolean = true
)