package de.daniel.bactromod.config

import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Files
import java.nio.file.Path

@OptIn(ExperimentalSerializationApi::class)
object Config {

    private val configPath: Path = FabricLoader.getInstance().configDir.resolve("bactromod.json")
    private lateinit var configObject: ConfigObject

        fun init() = runBlocking {
        withContext(Dispatchers.IO) {
            if (!Files.exists(configPath)) {
                Files.createFile(configPath)
                save(ConfigObject())
            }
            launch {
                withContext(Dispatchers.IO) {
                    configObject = Json.decodeFromStream(Files.newInputStream(configPath))
                }
            }
        }
    }

    fun save(configObj: ConfigObject) = runBlocking {
        withContext(Dispatchers.IO) {
            launch {
                withContext(Dispatchers.IO) {
                    Json.encodeToStream(configObj, Files.newOutputStream(configPath))
                }
            }
            configObject = configObj
        }
    }

    fun load(): ConfigObject = configObject

}