package de.daniel.bactromod.config.optiontypes;

import java.util.Locale;

public enum ConfigCategory {
    VISIBILITY,
    FOG,
    SCALING,
    OTHER;

    public String key() {
        return "category." + name().toLowerCase(Locale.ROOT);
    }
}
