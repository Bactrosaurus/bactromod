package de.daniel.bactromod.config;

import lombok.With;

@With
public record ConfigData(
        int gammaMultiplier,
        boolean pumpkinBlur,
        int fireOffset,
        int shieldOffset,
        boolean blindnessFog,
        boolean darknessFog,
        boolean lavaFog,
        boolean powderSnowFog,
        boolean waterFog,
        boolean atmosphericFog,
        boolean dimensionBossFog,
        boolean renderDistanceFog,
        boolean showMapWhileInBoat,
        boolean fixShieldRiptideTrident,
        boolean darkWindowBorders
) {

    public static ConfigData defaultConfig() {
        return new ConfigData(
                15,
                false,
                -30,
                -20,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                true,
                true,
                true
        );
    }

}

