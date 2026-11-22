package de.daniel.bactromod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Objects;
import java.util.TreeMap;

public final class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LoggerFactory.getLogger("BactroMod");
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("bactromod.json");
    private static final ConfigData DATA = load();

    private Config() {}

    public static ConfigData get() {
        return DATA;
    }

    public static void save() {
        write(DATA);
    }

    private static ConfigData load() {
        try {
            if (Files.notExists(Config.CONFIG_PATH)) return createDefaults();

            try {
                ConfigData data = GSON.fromJson(Files.readString(Config.CONFIG_PATH), ConfigData.class);
                if (data == null) return createDefaults();
                if (data.itemScalingFactors == null) {
                    data.itemScalingFactors = new TreeMap<>();
                } else {
                    data.itemScalingFactors.values().removeIf(Objects::isNull);
                }
                return data;
            } catch (JsonParseException e) {
                Path backup = nextBackupPath();
                Files.move(Config.CONFIG_PATH, backup);
                LOGGER.warn("Invalid config replaced with defaults. Backup: {}", backup);
                return createDefaults();
            }
        } catch (IOException e) {
            LOGGER.error("Could not load or create config file.", e);
            return new ConfigData();
        }
    }

    private static Path nextBackupPath() {
        String prefix = "bactromod_old_" + Instant.now().toEpochMilli();
        Path backup = Config.CONFIG_PATH.resolveSibling(prefix + ".json");
        for (int i = 1; Files.exists(backup); i++) {
            backup = Config.CONFIG_PATH.resolveSibling(prefix + "_" + i + ".json");
        }
        return backup;
    }

    private static ConfigData createDefaults() {
        ConfigData defaults = new ConfigData();
        write(defaults);
        return defaults;
    }

    private static void write(ConfigData data) {
        Path temporary = null;
        try {
            Path parent = Config.CONFIG_PATH.getParent();
            if (parent == null) throw new IOException("No parent directory exists");
            Files.createDirectories(parent);
            temporary = Files.createTempFile(parent, "bactromod", ".tmp");
            Files.writeString(temporary, GSON.toJson(data));
            try {
                Files.move(temporary, Config.CONFIG_PATH, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporary, Config.CONFIG_PATH, StandardCopyOption.REPLACE_EXISTING);
            }
            temporary = null;
        } catch (IOException e) {
            LOGGER.error("Could not save config file.", e);
        } finally {
            if (temporary != null) {
                try {
                    Files.deleteIfExists(temporary);
                } catch (IOException e) {
                    LOGGER.warn("Could not delete temporary config file {}.", temporary, e);
                }
            }
        }
    }
}
