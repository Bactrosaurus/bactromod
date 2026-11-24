package de.daniel.bactromod.config.optiontypes;

import java.util.Locale;

public enum ConfigCategory {
    LIGHTING,
    FOG,
    OVERLAYS_AND_ITEMS,
    OTHER;

    public String key() {
        return "category." + name().toLowerCase(Locale.ROOT);
    }
}
