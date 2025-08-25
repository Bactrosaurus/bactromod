package de.daniel.bactromod.config;

public class ConfigData {

    /*
    Options here must be named like {variableName} in language files with pattern:
        bactromod.options.{variableName}
        bactromod.options.{variableName}.desc
     */

    @ConfigOption(intMin = 0, intMax = 15, intDefault = 15)
    public int gammaMultiplier;

    @ConfigOption()
    public boolean pumpkinBlur;

    @ConfigOption(intMin = -100, intMax = 100, intDefault = -30)
    public int fireOffset;

    @ConfigOption(intMin = -100, intMax = 100, intDefault = -20)
    public int shieldOffset;

    @ConfigOption()
    public boolean blindnessFog;

    @ConfigOption()
    public boolean darknessFog;

    @ConfigOption()
    public boolean lavaFog;

    @ConfigOption()
    public boolean powderSnowFog;

    @ConfigOption()
    public boolean waterFog;

    @ConfigOption()
    public boolean atmosphericFog;

    @ConfigOption()
    public boolean dimensionBossFog;

    @ConfigOption(boolDefault = true)
    public boolean renderDistanceFog;

    @ConfigOption(boolDefault = true)
    public boolean showMapWhileInBoat;

    @ConfigOption(boolDefault = true)
    public boolean fixShieldRiptideTrident;

    @ConfigOption(boolDefault = true)
    public boolean darkWindowBorders;

}
