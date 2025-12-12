package de.daniel.bactromod.config;

public class ConfigData {

    /*
    Options here must be named like {variableName} in language files with pattern:
        bactromod.options.{variableName}
        bactromod.options.{variableName}.desc
     */

    @ConfigOption(intMin = 0, intMax = 15)
    public int gammaMultiplier = 15;

    @ConfigOption()
    public boolean pumpkinBlur = false;

    @ConfigOption(intMin = -100, intMax = 100)
    public int fireOffset = -30;

    @ConfigOption(intMin = -100, intMax = 100)
    public int shieldOffset = -20;

    @ConfigOption()
    public boolean blindnessFog = false;

    @ConfigOption()
    public boolean darknessFog = false;

    @ConfigOption()
    public boolean lavaFog = false;

    @ConfigOption()
    public boolean powderSnowFog = false;

    @ConfigOption()
    public boolean waterFog = false;

    @ConfigOption()
    public boolean atmosphericFog = false;

    @ConfigOption()
    public boolean showMapWhileInBoat = true;

    @ConfigOption()
    public boolean fixShieldRiptideTrident = true;

    @ConfigOption()
    public boolean darkWindowBorders = true;

}
