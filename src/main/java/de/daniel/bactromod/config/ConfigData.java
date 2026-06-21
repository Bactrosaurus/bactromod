package de.daniel.bactromod.config;

import de.daniel.bactromod.config.optiontypes.BooleanOption;
import de.daniel.bactromod.config.optiontypes.IntegerOption;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigData {

    /*
        Options here must be named like {variableName} in language files with pattern:
        bactromod.options.{variableName}
        bactromod.options.{variableName}.desc
     */

    @IntegerOption(intMin = 1, intMax = 15)
    public int gammaMultiplier = 15;

    @BooleanOption
    public boolean pumpkinBlur = false;

    @IntegerOption(intMin = -100, intMax = 100)
    public int fireOffset = -30;

    @IntegerOption(intMin = -100, intMax = 100)
    public int shieldOffset = -20;

    @BooleanOption
    public boolean blindnessFog = false;

    @BooleanOption
    public boolean darknessFog = false;

    @BooleanOption
    public boolean lavaFog = false;

    @BooleanOption
    public boolean powderSnowFog = false;

    @BooleanOption
    public boolean waterFog = false;

    @BooleanOption
    public boolean atmosphericFog = false;

    @BooleanOption
    public boolean showMapWhileInBoat = true;

    @BooleanOption
    public boolean fixShieldRiptideTrident = true;

    @BooleanOption
    public boolean nightVision = true;

    @BooleanOption
    public boolean ignoreOpGamemodeSwitcher = true;

    /*
        Map containing all item scaling options.
        Please note that keys must be named like their item ids!
     */
    public Map<String, Integer> itemScalingFactors =
            Stream.of(
                    "totem_of_undying",
                    "golden_apple",
                    "enchanted_golden_apple",
                    "potion",
                    "splash_potion",
                    "firework_rocket",
                    "water_bucket",
                    "lava_bucket",
                    "ender_pearl",
                    "end_crystal",
                    "golden_carrot",
                    "bread",
                    "cooked_beef",
                    "cooked_porkchop",
                    "cooked_mutton",
                    "cooked_chicken",
                    "cooked_rabbit",
                    "cooked_cod",
                    "cooked_salmon",
                    "pumpkin_pie",
                    "shield"
            )
            .map(s -> "item.minecraft." + s)
            .collect(Collectors.toMap(Function.identity(), _ -> 100, (a, _) -> a, TreeMap::new));

}
