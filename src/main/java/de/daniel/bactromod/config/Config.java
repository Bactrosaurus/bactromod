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
    private static final ConfigData DATA = load(CONFIG_PATH);

    private Config() {}

    public static ConfigData get() {
        return DATA;
    }

    public static void save() {
        write(CONFIG_PATH, DATA);
    }

    private static ConfigData load(Path path) {
        try {
            if (Files.notExists(path)) return createDefaults(path);

            try {
                ConfigData data = GSON.fromJson(Files.readString(path), ConfigData.class);
                if (data == null) return createDefaults(path);
                if (data.itemScalingFactors == null) {
                    data.itemScalingFactors = new TreeMap<>();
                } else {
                    data.itemScalingFactors.values().removeIf(Objects::isNull);
                }
                return data;
            } catch (JsonParseException e) {
                Path backup = nextBackupPath(path);
                Files.move(path, backup);
                LOGGER.warn("Invalid config replaced with defaults. Backup: {}", backup);
                return createDefaults(path);
            }
        } catch (IOException e) {
            LOGGER.error("Could not load or create config file.", e);
            return new ConfigData();
        }
    }

    private static Path nextBackupPath(Path path) {
        String prefix = "bactromod_old_" + Instant.now().toEpochMilli();
        Path backup = path.resolveSibling(prefix + ".json");
        for (int i = 1; Files.exists(backup); i++) {
            backup = path.resolveSibling(prefix + "_" + i + ".json");
        }
        return backup;
    }

    private static ConfigData createDefaults(Path path) {
        ConfigData defaults = new ConfigData();
        write(path, defaults);
        return defaults;
    }

    private static void write(Path path, ConfigData data) {
        Path temporary = null;
        try {
            Path parent = path.getParent();
            if (parent != null) Files.createDirectories(parent);
            temporary = Files.createTempFile(parent, "bactromod", ".tmp");
            Files.writeString(temporary, GSON.toJson(data));
            try {
                Files.move(temporary, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING);
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
