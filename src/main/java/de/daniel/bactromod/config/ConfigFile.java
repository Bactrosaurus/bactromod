package de.daniel.bactromod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.TreeMap;

final class ConfigFile {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LoggerFactory.getLogger("BactroMod");

    private ConfigFile() {}

    static ConfigData load(Path path) {
        try {
            if (Files.notExists(path)) return createDefaults(path);

            try {
                ConfigData data = GSON.fromJson(Files.readString(path), ConfigData.class);
                if (data == null) return createDefaults(path);
                if (data.itemScalingFactors == null) data.itemScalingFactors = new TreeMap<>();
                return data;
            } catch (JsonParseException e) {
                Path backup = path.resolveSibling("bactromod_old_" + Instant.now().toEpochMilli() + ".json");
                while (Files.exists(backup)) {
                    backup = path.resolveSibling(backup.getFileName() + "_old");
                }
                Files.move(path, backup);
                LOGGER.warn("Invalid config replaced with defaults. Backup: {}", backup);
                return createDefaults(path);
            }
        } catch (IOException e) {
            LOGGER.error("Could not load or create config file.", e);
            return new ConfigData();
        }
    }

    private static ConfigData createDefaults(Path path) {
        ConfigData defaults = new ConfigData();
        write(path, defaults);
        return defaults;
    }

    static void write(Path path, ConfigData data) {
        try {
            Files.createDirectories(path.getParent());
            Path temporary = Files.createTempFile(path.getParent(), "bactromod", ".tmp");
            Files.writeString(temporary, GSON.toJson(data));
            try {
                Files.move(temporary, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (java.nio.file.AtomicMoveNotSupportedException e) {
                Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            LOGGER.error("Could not save config file.", e);
        }
    }
}
