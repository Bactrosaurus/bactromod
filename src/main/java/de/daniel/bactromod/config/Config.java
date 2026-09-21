package de.daniel.bactromod.config;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public final class Config {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("bactromod.json");
    private static final ConfigData DATA = ConfigFile.load(CONFIG_PATH);

    private Config() {}

    public static ConfigData get() { return DATA; }

    public static void save() { ConfigFile.write(CONFIG_PATH, DATA); }
}
