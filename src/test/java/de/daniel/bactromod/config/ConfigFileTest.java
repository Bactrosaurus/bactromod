package de.daniel.bactromod.config;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ConfigFileTest {
    @TempDir
    Path directory;

    @Test
    void createsDefaultsAndParentDirectory() {
        Path path = directory.resolve("config/bactromod.json");
        ConfigData loaded = ConfigFile.load(path);
        assertTrue(Files.isRegularFile(path));
        assertEquals(new Gson().toJson(new ConfigData()), new Gson().toJson(loaded));
    }

    @Test
    void savesAndReloadsChangesWithoutLeavingTemporaryFiles() throws IOException {
        Path path = directory.resolve("bactromod.json");
        ConfigData data = ConfigFile.load(path);
        data.gammaMultiplier = 4;
        data.nightVision = false;
        data.shieldOffset = -75;
        data.itemScalingFactors.put("item.minecraft.diamond_sword", 35);
        ConfigFile.write(path, data);

        assertEquals(new Gson().toJson(data), new Gson().toJson(ConfigFile.load(path)));
        try (var files = Files.list(directory)) {
            assertEquals(1, files.count());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"{}", "{\"itemScalingFactors\":null}", "null", ""})
    void recoversMissingAndNullValues(String json) throws IOException {
        Path path = directory.resolve("bactromod.json");
        Files.writeString(path, json);
        ConfigData data = ConfigFile.load(path);
        assertEquals(new Gson().toJson(new ConfigData()), new Gson().toJson(data));
        assertNotNull(data.itemScalingFactors);
    }

    @Test
    void preservesExistingSettingsWhenNewFieldsAreMissing() throws IOException {
        Path path = directory.resolve("bactromod.json");
        Files.writeString(path, "{\"gammaMultiplier\":7,\"nightVision\":false}");
        ConfigData data = ConfigFile.load(path);
        assertEquals(7, data.gammaMultiplier);
        assertFalse(data.nightVision);
        assertEquals(new ConfigData().shieldOffset, data.shieldOffset);
        assertNotNull(data.itemScalingFactors);
    }

    @ParameterizedTest
    @ValueSource(strings = {"{broken", "{\"gammaMultiplier\":\"invalid\"}", "[]"})
    void backsUpInvalidConfigBeforeWritingDefaults(String broken) throws IOException {
        Path path = directory.resolve("bactromod.json");
        Path previousBackup = directory.resolve("bactromod_old_0.json");
        Files.writeString(previousBackup, "previous backup");
        Files.writeString(path, broken);

        assertEquals(new Gson().toJson(new ConfigData()), new Gson().toJson(ConfigFile.load(path)));
        assertEquals("previous backup", Files.readString(previousBackup));
        try (var files = Files.list(directory)) {
            var backups = files.filter(file -> !file.equals(path) && !file.equals(previousBackup)).toList();
            assertEquals(1, backups.size());
            assertTrue(backups.getFirst().getFileName().toString().startsWith("bactromod_old_"));
            assertEquals(broken, Files.readString(backups.getFirst()));
        }
        assertEquals(new Gson().toJson(new ConfigData()), new Gson().toJson(ConfigFile.load(path)));
    }
}
