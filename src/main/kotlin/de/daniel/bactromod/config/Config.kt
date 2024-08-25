package de.daniel.bactromod.config

import de.daniel.bactromod.configPath
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Files

@Serializable
data class ConfigData(
    val gammaMultiplier: Int = 15,
    val pumpkinBlur: Boolean = false,
    val fireOffset: Int = -30,
    val shieldOffset: Int = -20,
    val blindnessFog: Boolean = false,
    val darknessFog: Boolean = false,
    val lavaFog: Boolean = false,
    val powderSnowFog: Boolean = false,
    val waterFog: Boolean = false,
    val skyFog: Boolean = true,
    val terrainFog: Boolean = false,
    val thickFog: Boolean = false,
    val showMapWhileInBoat: Boolean = true,
    val fixShieldRiptideTrident: Boolean = true,
    val darkWindowBorders: Boolean = true
)

@OptIn(ExperimentalSerializationApi::class)
object Config {
    private lateinit var configData: ConfigData
    private val configScope = CoroutineScope(Dispatchers.IO)

    fun init() {
        configScope.launch {
            if (!Files.exists(configPath)) {
                Files.createFile(configPath)
                save(ConfigData())
            }
            configData = Json.decodeFromStream(Files.newInputStream(configPath))
        }
    }

    fun save(configObj: ConfigData) {
        configScope.launch {
            Json.encodeToStream(configObj, Files.newOutputStream(configPath))
            configData = configObj
        }
    }

    fun load(): ConfigData = configData
}