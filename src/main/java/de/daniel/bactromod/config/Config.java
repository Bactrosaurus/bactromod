package de.daniel.bactromod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import de.daniel.bactromod.BactroMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

public class Config {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("bactromod.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static volatile ConfigData configData = loadOrCreate();

    private static ConfigData loadOrCreate() {
        try {
            if (!Files.exists(CONFIG_PATH)) {
                ConfigData defaults = new ConfigData();
                save(defaults);
                return defaults;
            }
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                JsonElement saved = GSON.fromJson(reader, JsonElement.class);
                if (saved == null || !saved.isJsonObject()) throw new JsonSyntaxException("Config root is not an object");
                JsonObject defaults = GSON.toJsonTree(new ConfigData()).getAsJsonObject();
                mergeDefaults(defaults, saved.getAsJsonObject());
                return normalize(GSON.fromJson(defaults, ConfigData.class));
            } catch (JsonParseException e) {
                Path backup = CONFIG_PATH.resolveSibling("bactromod_old_" + Instant.now().toEpochMilli() + ".json");
                while (Files.exists(backup)) backup = CONFIG_PATH.resolveSibling(backup.getFileName() + "_old");
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
        config = normalize(config);
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Path temporary = Files.createTempFile(CONFIG_PATH.getParent(), "bactromod", ".tmp");
            try (Writer writer = Files.newBufferedWriter(temporary)) {
                GSON.toJson(config, writer);
            }
            try {
                Files.move(temporary, CONFIG_PATH, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (java.nio.file.AtomicMoveNotSupportedException e) {
                Files.move(temporary, CONFIG_PATH, StandardCopyOption.REPLACE_EXISTING);
            }
            configData = config;
        } catch (IOException e) {
            BactroMod.LOGGER.error("Could not save config file.", e);
        }
    }

    public static ConfigData load() {
        return configData;
    }

    public static ConfigData copy(ConfigData config) {
        return normalize(GSON.fromJson(GSON.toJson(config), ConfigData.class));
    }

    private static ConfigData normalize(ConfigData config) {
        if (config == null) return new ConfigData();
        config.gammaMultiplier = Math.clamp(config.gammaMultiplier, 1, 15);
        config.fireOffset = Math.clamp(config.fireOffset, -100, 100);
        config.shieldOffset = Math.clamp(config.shieldOffset, -100, 100);
        Map<String, Integer> factors = new TreeMap<>();
        if (config.itemScalingFactors != null) {
            config.itemScalingFactors.forEach((item, value) -> {
                if (item != null) factors.put(item, value == null ? 100 : Math.clamp(value, 1, 100));
            });
        }
        if (factors.isEmpty()) factors.putAll(new ConfigData().itemScalingFactors);
        config.itemScalingFactors = factors;
        return config;
    }

    private static void mergeDefaults(JsonObject defaults, JsonObject saved) {
        saved.entrySet().forEach(entry -> {
            JsonElement defaultValue = defaults.get(entry.getKey());
            if (defaultValue != null && defaultValue.isJsonObject() && entry.getValue().isJsonObject()) {
                mergeDefaults(defaultValue.getAsJsonObject(), entry.getValue().getAsJsonObject());
            } else if (!entry.getValue().isJsonNull()) {
                defaults.add(entry.getKey(), entry.getValue());
            }
        });
    }
}
