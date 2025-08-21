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
    private static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("bactromod.json");
    private static volatile ConfigData configData;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static {
        try {
            if (!Files.exists(configPath)) {
                try {
                    Files.createDirectories(configPath.getParent());
                    save(ConfigData.defaultConfig());
                } catch (IOException e) {
                    // If creation of config path fails use default config
                    configData = ConfigData.defaultConfig();

                    BactroMod.LOGGER.error(
                            "Could not create default config file or directory." +
                                    " Make sure {} is a directory and writable.", configPath.getParent(), e
                    );
                }
            } else {
                try (Reader reader = Files.newBufferedReader(configPath)) {
                    configData = gson.fromJson(reader, ConfigData.class);
                    if (configData == null) configData = ConfigData.defaultConfig();
                } catch (JsonSyntaxException | JsonIOException | IOException e) {
                    Path backup = configPath.resolveSibling("bactromod_old_" + Instant.now().getEpochSecond() + ".json");
                    Files.move(configPath, backup);

                    BactroMod.LOGGER.warn(
                            "Existing config file in {} is invalid" +
                                    " and has been replaced with default config. Invalid config file" +
                                    " has been backed up at {}.", configPath, backup);
                    save(ConfigData.defaultConfig());
                }
            }
        } catch (IOException e) {
            configData = ConfigData.defaultConfig();
            BactroMod.LOGGER.error("Could not load default config file or directory.", e);
        }
    }

    public static void save(ConfigData config) {
        try (Writer writer = Files.newBufferedWriter(configPath)) {
            gson.toJson(config, writer);
            configData = config;
        } catch (IOException e) {
            BactroMod.LOGGER.error("Could not save config file or directory.", e);
        }
    }

    public static ConfigData load() {
        return configData != null ? configData : ConfigData.defaultConfig();
    }

}