package de.daniel.bactromod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import de.daniel.bactromod.BactroMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public class Config {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("bactromod.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static volatile ConfigData configData = loadOrCreate();

    private static ConfigData loadOrCreate() {
        try {
            if (!Files.exists(CONFIG_PATH)) {
                Files.createDirectories(CONFIG_PATH.getParent());
                ConfigData defaults = new ConfigData();
                save(defaults);
                return defaults;
            }
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                ConfigData data = GSON.fromJson(reader, ConfigData.class);
                return data != null ? data : new ConfigData();
            } catch (JsonSyntaxException | JsonIOException e) {
                Path backup = CONFIG_PATH.resolveSibling("bactromod_old_" + Instant.now().getEpochSecond() + ".json");
                Files.move(CONFIG_PATH, backup);
                BactroMod.LOGGER.warn(
                        "Config file in {} is invalid; replaced with defaults. Backup at {}.", CONFIG_PATH, backup);
                ConfigData defaults = new ConfigData();
                save(defaults);
                return defaults;
            }
        } catch (IOException e) {
            BactroMod.LOGGER.error("Could not load or create config file.", e);
            return new ConfigData();
        }
    }

    public static void save(ConfigData config) {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(config, writer);
            configData = config;
        } catch (IOException e) {
            BactroMod.LOGGER.error("Could not save config file.", e);
        }
    }

    public static ConfigData load() {
        return configData != null ? configData : new ConfigData();
    }

}
