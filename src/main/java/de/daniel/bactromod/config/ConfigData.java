package de.daniel.bactromod.config;

import de.daniel.bactromod.config.optiontypes.BooleanOption;
import de.daniel.bactromod.config.optiontypes.ConfigCategory;
import de.daniel.bactromod.config.optiontypes.IntegerOption;

import java.util.Map;
import java.util.TreeMap;

public final class ConfigData {

    @IntegerOption(min = 1, max = 15, category = ConfigCategory.LIGHTING)
    public int gammaMultiplier = 15;

    @BooleanOption(category = ConfigCategory.LIGHTING)
    public boolean nightVision = true;

    @BooleanOption(category = ConfigCategory.FOG)
    public boolean atmosphericFog;

    @BooleanOption(category = ConfigCategory.FOG)
    public boolean waterFog;

    @BooleanOption(category = ConfigCategory.FOG)
    public boolean lavaFog;

    @BooleanOption(category = ConfigCategory.FOG)
    public boolean powderSnowFog;

    @BooleanOption(category = ConfigCategory.FOG)
    public boolean blindnessFog;

    @BooleanOption(category = ConfigCategory.FOG)
    public boolean darknessFog;

    @IntegerOption(min = -100, max = 100, category = ConfigCategory.OVERLAYS_AND_ITEMS)
    public int fireOffset = -30;

    @IntegerOption(min = -100, max = 100, category = ConfigCategory.OVERLAYS_AND_ITEMS)
    public int shieldOffset = -20;

    @BooleanOption(category = ConfigCategory.OVERLAYS_AND_ITEMS)
    public boolean pumpkinBlur;

    @IntegerOption(min = 1, max = 100, category = ConfigCategory.OVERLAYS_AND_ITEMS)
    public int totemOverlayPercentSize = 100;

    @BooleanOption
    public boolean showMapWhileInBoat = true;

    @BooleanOption
    public boolean fixShieldRiptideTrident = true;

    @BooleanOption
    public boolean ignoreOpGamemodeSwitcher = true;

    public Map<String, Integer> itemScalingFactors = new TreeMap<>();
}
