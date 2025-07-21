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
    public record ConfigData(
            int gammaMultiplier,
            boolean pumpkinBlur,
            int fireOffset,
            int shieldOffset,
            boolean blindnessFog,
            boolean darknessFog,
            boolean lavaFog,
            boolean powderSnowFog,
            boolean waterFog,
            boolean atmosphericFog,
            boolean dimensionBossFog,
            boolean renderDistanceFog,
            boolean showMapWhileInBoat,
            boolean fixShieldRiptideTrident,
            boolean darkWindowBorders
    ) {
        public static ConfigData defaultConfig() {
            return new ConfigData(15, false, -30, -20, false, false, false, false, false,
                    false, false, false, true, true, true);
        }

        // This feels illegal
        public ConfigData withGammaMultiplier(int value) {
            return new ConfigData(value, pumpkinBlur, fireOffset, shieldOffset, blindnessFog, darknessFog, lavaFog,
                    powderSnowFog, waterFog, atmosphericFog, dimensionBossFog, renderDistanceFog,
                    showMapWhileInBoat, fixShieldRiptideTrident, darkWindowBorders);
        }

        public ConfigData withPumpkinBlur(boolean value) {
            return new ConfigData(gammaMultiplier, value, fireOffset, shieldOffset, blindnessFog, darknessFog, lavaFog,
                    powderSnowFog, waterFog, atmosphericFog, dimensionBossFog, renderDistanceFog,
                    showMapWhileInBoat, fixShieldRiptideTrident, darkWindowBorders);
        }

        public ConfigData withFireOffset(int value) {
            return new ConfigData(gammaMultiplier, pumpkinBlur, value, shieldOffset, blindnessFog, darknessFog, lavaFog,
                    powderSnowFog, waterFog, atmosphericFog, dimensionBossFog, renderDistanceFog,
                    showMapWhileInBoat, fixShieldRiptideTrident, darkWindowBorders);
        }

        public ConfigData withShieldOffset(int value) {
            return new ConfigData(gammaMultiplier, pumpkinBlur, fireOffset, value, blindnessFog, darknessFog, lavaFog,
                    powderSnowFog, waterFog, atmosphericFog, dimensionBossFog, renderDistanceFog,
                    showMapWhileInBoat, fixShieldRiptideTrident, darkWindowBorders);
        }

        public ConfigData withBlindnessFog(boolean value) {
            return new ConfigData(gammaMultiplier, pumpkinBlur, fireOffset, shieldOffset, value, darknessFog, lavaFog,
                    powderSnowFog, waterFog, atmosphericFog, dimensionBossFog, renderDistanceFog,
                    showMapWhileInBoat, fixShieldRiptideTrident, darkWindowBorders);
        }

        public ConfigData withDarknessFog(boolean value) {
            return new ConfigData(gammaMultiplier, pumpkinBlur, fireOffset, shieldOffset, blindnessFog, value, lavaFog,
                    powderSnowFog, waterFog, atmosphericFog, dimensionBossFog, renderDistanceFog,
                    showMapWhileInBoat, fixShieldRiptideTrident, darkWindowBorders);
        }

        public ConfigData withLavaFog(boolean value) {
            return new ConfigData(gammaMultiplier, pumpkinBlur, fireOffset, shieldOffset, blindnessFog, darknessFog, value,
                    powderSnowFog, waterFog, atmosphericFog, dimensionBossFog, renderDistanceFog,
                    showMapWhileInBoat, fixShieldRiptideTrident, darkWindowBorders);
        }

        public ConfigData withPowderSnowFog(boolean value) {
            return new ConfigData(gammaMultiplier, pumpkinBlur, fireOffset, shieldOffset, blindnessFog, darknessFog, lavaFog,
                    value, waterFog, atmosphericFog, dimensionBossFog, renderDistanceFog,
                    showMapWhileInBoat, fixShieldRiptideTrident, darkWindowBorders);
        }

        public ConfigData withWaterFog(boolean value) {
            return new ConfigData(gammaMultiplier, pumpkinBlur, fireOffset, shieldOffset, blindnessFog, darknessFog, lavaFog,
                    powderSnowFog, value, atmosphericFog, dimensionBossFog, renderDistanceFog,
                    showMapWhileInBoat, fixShieldRiptideTrident, darkWindowBorders);
        }

        public ConfigData withAtmosphericFog(boolean value) {
            return new ConfigData(gammaMultiplier, pumpkinBlur, fireOffset, shieldOffset, blindnessFog, darknessFog, lavaFog,
                    powderSnowFog, waterFog, value, dimensionBossFog, renderDistanceFog,
                    showMapWhileInBoat, fixShieldRiptideTrident, darkWindowBorders);
        }

        public ConfigData withDimensionBossFog(boolean value) {
            return new ConfigData(gammaMultiplier, pumpkinBlur, fireOffset, shieldOffset, blindnessFog, darknessFog, lavaFog,
                    powderSnowFog, waterFog, atmosphericFog, value, renderDistanceFog,
                    showMapWhileInBoat, fixShieldRiptideTrident, darkWindowBorders);
        }

        public ConfigData withRenderDistanceFog(boolean value) {
            return new ConfigData(gammaMultiplier, pumpkinBlur, fireOffset, shieldOffset, blindnessFog, darknessFog, lavaFog,
                    powderSnowFog, waterFog, atmosphericFog, dimensionBossFog, value,
                    showMapWhileInBoat, fixShieldRiptideTrident, darkWindowBorders);
        }

        public ConfigData withShowMapWhileInBoat(boolean value) {
            return new ConfigData(gammaMultiplier, pumpkinBlur, fireOffset, shieldOffset, blindnessFog, darknessFog, lavaFog,
                    powderSnowFog, waterFog, atmosphericFog, dimensionBossFog, renderDistanceFog,
                    value, fixShieldRiptideTrident, darkWindowBorders);
        }

        public ConfigData withFixShieldRiptideTrident(boolean value) {
            return new ConfigData(gammaMultiplier, pumpkinBlur, fireOffset, shieldOffset, blindnessFog, darknessFog, lavaFog,
                    powderSnowFog, waterFog, atmosphericFog, dimensionBossFog, renderDistanceFog,
                    showMapWhileInBoat, value, darkWindowBorders);
        }

        public ConfigData withDarkWindowBorders(boolean value) {
            return new ConfigData(gammaMultiplier, pumpkinBlur, fireOffset, shieldOffset, blindnessFog, darknessFog, lavaFog,
                    powderSnowFog, waterFog, atmosphericFog, dimensionBossFog, renderDistanceFog,
                    showMapWhileInBoat, fixShieldRiptideTrident, value);
        }
    }

    private static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("bactromod.json");
    private static volatile ConfigData configData;

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

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
