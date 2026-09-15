package de.daniel.bactromod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.TreeMap;

public final class Config {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("bactromod.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LoggerFactory.getLogger("BactroMod");
    private static final ConfigData DATA = load();

    private Config() {}

    private static ConfigData load() {
        try {
            if (Files.notExists(CONFIG_PATH)) return createDefaults();

            try {
                ConfigData data = GSON.fromJson(Files.readString(CONFIG_PATH), ConfigData.class);
                if (data == null) return createDefaults();
                if (data.itemScalingFactors == null) data.itemScalingFactors = new TreeMap<>();
                return data;
            } catch (JsonParseException e) {
                Path backup = CONFIG_PATH.resolveSibling("bactromod_old_" + Instant.now().toEpochMilli() + ".json");
                while (Files.exists(backup)) {
                    backup = CONFIG_PATH.resolveSibling(backup.getFileName() + "_old");
                }
                Files.move(CONFIG_PATH, backup);
                LOGGER.warn("Invalid config replaced with defaults. Backup: {}", backup);
                return createDefaults();
            }
        } catch (IOException e) {
            LOGGER.error("Could not load or create config file.", e);
            return new ConfigData();
        }
    }

    private static ConfigData createDefaults() {
        ConfigData defaults = new ConfigData();
        write(defaults);
        return defaults;
    }

    private static void write(ConfigData data) {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Path temporary = Files.createTempFile(CONFIG_PATH.getParent(), "bactromod", ".tmp");
            Files.writeString(temporary, GSON.toJson(data));
            try {
                Files.move(temporary, CONFIG_PATH, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (java.nio.file.AtomicMoveNotSupportedException e) {
                Files.move(temporary, CONFIG_PATH, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            LOGGER.error("Could not save config file.", e);
        }
    }

    public static ConfigData get() { return DATA; }

    public static void save() { write(DATA); }
}
