package de.daniel.bactromod.config;

import de.daniel.bactromod.config.optiontypes.BooleanOption;
import de.daniel.bactromod.config.optiontypes.IntegerOption;

import java.util.Map;
import java.util.TreeMap;

public final class ConfigData {

    @IntegerOption(min = 1, max = 15)
    public int gammaMultiplier = 15;

    @BooleanOption
    public boolean pumpkinBlur;

    @IntegerOption(min = -100, max = 100)
    public int fireOffset = -30;

    @IntegerOption(min = -100, max = 100)
    public int shieldOffset = -20;

    @BooleanOption
    public boolean blindnessFog;

    @BooleanOption
    public boolean darknessFog;

    @BooleanOption
    public boolean lavaFog;

    @BooleanOption
    public boolean powderSnowFog;

    @BooleanOption
    public boolean waterFog;

    @BooleanOption
    public boolean atmosphericFog;

    @BooleanOption
    public boolean showMapWhileInBoat = true;

    @BooleanOption
    public boolean fixShieldRiptideTrident = true;

    @BooleanOption
    public boolean nightVision = true;

    @BooleanOption
    public boolean ignoreOpGamemodeSwitcher = true;

    @IntegerOption(max = 100, min = 1)
    public int totemOverlayPercentSize = 100;

    public Map<String, Integer> itemScalingFactors = new TreeMap<>();
}
