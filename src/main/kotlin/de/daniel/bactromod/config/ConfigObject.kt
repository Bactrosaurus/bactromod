package de.daniel.bactromod.config

import kotlinx.serialization.Serializable

@Serializable
data class ConfigObject(
    var gammaMultiplier: Int = 15,
    var pumpkinBlur: Boolean = false,
    var fireOffset: Int = -30,
    var shieldOffset: Int = -20,
    var blindnessFog: Boolean = false,
    var darknessFog: Boolean = false,
    var lavaFog: Boolean = false,
    var powderSnowFog: Boolean = false,
    var waterFog: Boolean = false,
    var skyFog: Boolean = true,
    var terrainFog: Boolean = false,
    var thickFog: Boolean = false,
    var showMapWhileInBoat: Boolean = true,
    var fixShieldRiptideTrident: Boolean = true,
    var darkWindowBorders: Boolean = true
)