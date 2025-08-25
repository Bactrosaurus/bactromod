package de.daniel.bactromod.config;

import java.lang.reflect.Field;

public class ConfigDefaults {

    public static ConfigData createDefaults() {
        ConfigData config = new ConfigData();
        for (Field f : ConfigData.class.getDeclaredFields()) {
            ConfigOption opt = f.getAnnotation(ConfigOption.class);
            if (opt != null) {
                try {
                    f.setAccessible(true);
                    if (f.getType() == int.class) {
                        f.setInt(config, opt.intDefault());
                    } else if (f.getType() == boolean.class) {
                        f.setBoolean(config, opt.boolDefault());
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Could not set default for " + f.getName(), e);
                }
            }
        }
        return config;
    }

}
