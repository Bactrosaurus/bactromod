package de.daniel.bactromod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import de.daniel.bactromod.BactroMod;
import de.daniel.bactromod.utils.SystemInfo;
import de.daniel.bactromod.windowborder.DwmApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

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
                    save(new ConfigData());
                } catch (IOException e) {
                    // If creation of config path fails use default config
                    configData = new ConfigData();

                    BactroMod.LOGGER.error(
                            "Could not create default config file or directory." +
                                    " Make sure {} is a directory and writable.", configPath.getParent(), e
                    );
                }
            } else {
                try (Reader reader = Files.newBufferedReader(configPath)) {
                    configData = gson.fromJson(reader, ConfigData.class);
                    if (configData == null) configData = new ConfigData();
                } catch (JsonSyntaxException | JsonIOException | IOException e) {
                    Path backup = configPath.resolveSibling("bactromod_old_" + Instant.now().getEpochSecond() + ".json");
                    Files.move(configPath, backup);

                    BactroMod.LOGGER.warn(
                            "Existing config file in {} is invalid" +
                                    " and has been replaced with default config. Invalid config file" +
                                    " has been backed up at {}.", configPath, backup);
                    save(new ConfigData());
                }
            }
        } catch (IOException e) {
            configData = new ConfigData();
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

        // Update DWM whenever settings are saved (for dark window-borders in Windows 11)
        // Make sure window is already initialized
        if (SystemInfo.isWindows11 && MinecraftClient.getInstance().getWindow() != null)
            DwmApi.updateDwm(MinecraftClient.getInstance().getWindow().getHandle());
    }

    public static ConfigData load() {
        if (configData == null) {
            configData = new ConfigData();
        }
        return configData;
    }

}
