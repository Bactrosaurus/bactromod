package de.daniel.bactromod.config

import de.daniel.bactromod.LOGGER
import kotlinx.datetime.Clock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import net.fabricmc.loader.api.FabricLoader
import java.io.IOException
import java.nio.file.Files
import kotlin.io.path.pathString

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
    val atmosphericFog: Boolean = false,
    val dimensionBossFog: Boolean = false,
    val renderDistanceFog: Boolean = false,
    val showMapWhileInBoat: Boolean = true,
    val fixShieldRiptideTrident: Boolean = true,
    val darkWindowBorders: Boolean = true
)

@OptIn(ExperimentalSerializationApi::class)
object Config {
    private val configPath = FabricLoader.getInstance().configDir.resolve("bactromod.json")

    private lateinit var configData: ConfigData

    private val format = Json {
        encodeDefaults = true
        prettyPrint = true
    }

    init {
        if (!Files.exists(configPath)) {
            try {
                Files.createDirectories(configPath.parent)
                Files.createFile(configPath)
                save(ConfigData())
            } catch (e: IOException) {
                // If creation of config path fails use default config
                configData = ConfigData()

                LOGGER.error(
                    "Could not create default config file or directory." +
                            " Make sure ${configPath.parent.pathString} is a directory and writable.", e
                )
            }
        } else {
            try {
                configData = format.decodeFromStream<ConfigData>(Files.newInputStream(configPath))
            } catch (e: IllegalArgumentException) {
                val backupConfig = configPath.resolveSibling("bactromod_old_${Clock.System.now().epochSeconds}.json")
                Files.move(configPath, backupConfig)
                Files.createFile(configPath)

                LOGGER.warn(
                    "Existing config file in ${configPath.pathString} is invalid" +
                            " and has been replaced with default config. Invalid config file" +
                            " has been backed up at ${backupConfig.pathString}.", e
                )
                save(ConfigData())
            }
        }
    }

    fun save(configObj: ConfigData) {
        format.encodeToStream<ConfigData>(configObj, Files.newOutputStream(configPath))
        configData = configObj
    }

    fun load(): ConfigData = configData
}