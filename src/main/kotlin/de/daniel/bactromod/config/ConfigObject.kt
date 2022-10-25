package de.daniel.bactromod.config

import kotlinx.serialization.Serializable

@Serializable
data class ConfigObject(
    var gammaMultiplier: Int = 15,
    var disableDarkness: Boolean = true,
    var disablePumpkinBlur: Boolean = true,
    var fireOffset: Int = -30,
    var shieldOffset: Int = -20,
    var showHypixelFairySouls: Boolean = false
)