package de.daniel.bactromod.config;

import de.daniel.bactromod.config.optiontypes.BooleanOption;
import de.daniel.bactromod.config.optiontypes.IntegerOption;

public class ConfigData {

    /*
    Options here must be named like {variableName} in language files with pattern:
        bactromod.options.{variableName}
        bactromod.options.{variableName}.desc
     */

    @IntegerOption(intMin = 0, intMax = 15)
    public int gammaMultiplier = 15;

    @BooleanOption()
    public boolean pumpkinBlur = false;

    @IntegerOption(intMin = -100, intMax = 100)
    public int fireOffset = -30;

    @IntegerOption(intMin = -100, intMax = 100)
    public int shieldOffset = -20;

    @BooleanOption()
    public boolean blindnessFog = false;

    @BooleanOption()
    public boolean darknessFog = false;

    @BooleanOption()
    public boolean lavaFog = false;

    @BooleanOption()
    public boolean powderSnowFog = false;

    @BooleanOption()
    public boolean waterFog = false;

    @BooleanOption()
    public boolean atmosphericFog = false;

    @BooleanOption()
    public boolean showMapWhileInBoat = true;

    @BooleanOption()
    public boolean fixShieldRiptideTrident = true;

    @BooleanOption()
    public boolean darkWindowBorders = true;

}
