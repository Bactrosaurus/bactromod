package de.daniel.bactromod.config

import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

object ConfigScreen {

    fun getConfigScreen(parentScreen: Screen): Screen {
        val configBuilder = ConfigBuilder.create()
            .setParentScreen(parentScreen)
            .setTitle(Component.literal("BactroMod Settings"))

        val entryBuilder = configBuilder.entryBuilder()

        configBuilder.getOrCreateCategory(Component.literal("Visual"))
            .addEntry(
                entryBuilder
                    .startIntSlider(Component.literal("Gamma Multiplier"), Config.load().gammaMultiplier, 1, 15)
                    .setTooltip(Component.literal("Multiplies in-game gamma (brightness) by set number. Needed because brightness can't be adjusted in options.txt anymore."))
                    .setSaveConsumer { Config.save(Config.load().copy(gammaMultiplier = it)) }
                    .build()
            )
            .addEntry(
                entryBuilder
                    .startBooleanToggle(Component.literal("Disable Pumpkin Blur"), Config.load().disablePumpkinBlur)
                    .setTooltip(Component.literal("Disables black texture overlay that strongly limits view when player wears a carved pumpkin"))
                    .setSaveConsumer { Config.save(Config.load().copy(disablePumpkinBlur = it)) }
                    .build()
            )
            .addEntry(
                entryBuilder
                    .startIntSlider(Component.literal("Fire offset"), Config.load().fireOffset, -100, 100)
                    .setTooltip(Component.literal("Moves fire texture in height. Usually you would select a negative value to move fire texture down and free up your view."))
                    .setSaveConsumer { Config.save(Config.load().copy(fireOffset = it)) }
                    .build()
            )
            .addEntry(
                entryBuilder
                    .startIntSlider(Component.literal("Shield offset"), Config.load().shieldOffset, -100, 100)
                    .setTooltip(Component.literal("Moves shield in height when in first person perspective. Usually you would select a negative value to move shield down and free up your view."))
                    .setSaveConsumer { Config.save(Config.load().copy(shieldOffset = it)) }
                    .build()
            )

        configBuilder.getOrCreateCategory(Component.literal("Fog"))
            .addEntry(
                entryBuilder
                    .startBooleanToggle(Component.literal("Disable lava fog"), Config.load().disableLavaFog)
                    .setTooltip(Component.literal("Disables fog caused by diving into lava."))
                    .setSaveConsumer { Config.save(Config.load().copy(disableLavaFog = it)) }
                    .build()
            )
            .addEntry(
                entryBuilder
                    .startBooleanToggle(Component.literal("Disable powder snow fog"), Config.load().disablePowderSnowFog)
                    .setTooltip(Component.literal("Disables fog caused by diving into powder snow."))
                    .setSaveConsumer { Config.save(Config.load().copy(disablePowderSnowFog = it)) }
                    .build()
            )
            .addEntry(
                entryBuilder
                    .startBooleanToggle(Component.literal("Disable blindness fog"), Config.load().disableBlindnessFog)
                    .setTooltip(Component.literal("Disables fog caused by blindness effect."))
                    .setSaveConsumer { Config.save(Config.load().copy(disableBlindnessFog = it)) }
                    .build()
            )
            .addEntry(
                entryBuilder
                    .startBooleanToggle(Component.literal("Disable darkness fog"), Config.load().disableDarknessFog)
                    .setTooltip(Component.literal("Disables fog caused by darkness effect."))
                    .setSaveConsumer { Config.save(Config.load().copy(disableDarknessFog = it)) }
                    .build()
            )
            .addEntry(
                entryBuilder
                    .startBooleanToggle(Component.literal("Disable water fog"), Config.load().disableWaterFog)
                    .setTooltip(Component.literal("Disables fog caused by diving into water."))
                    .setSaveConsumer { Config.save(Config.load().copy(disableWaterFog = it)) }
                    .build()
            )
            .addEntry(
                entryBuilder
                    .startBooleanToggle(Component.literal("Disable thick fog"), Config.load().disableThickFog)
                    .setTooltip(Component.literal("Disables thick fog."))
                    .setSaveConsumer { Config.save(Config.load().copy(disableThickFog = it)) }
                    .build()
            )
            .addEntry(
                entryBuilder
                    .startBooleanToggle(Component.literal("Disable sky fog"), Config.load().disableSkyFog)
                    .setTooltip(Component.literal("Disables sky fog."))
                    .setSaveConsumer { Config.save(Config.load().copy(disableSkyFog = it)) }
                    .build()
            )
            .addEntry(
                entryBuilder
                    .startBooleanToggle(Component.literal("Disable terrain fog"), Config.load().disableTerrainFog)
                    .setTooltip(Component.literal("Disables terrain fog."))
                    .setSaveConsumer { Config.save(Config.load().copy(disableTerrainFog = it)) }
                    .build()
            )

        configBuilder.getOrCreateCategory(Component.literal("Misc"))
            .addEntry(
                entryBuilder
                    .startBooleanToggle(
                        Component.literal("Show Hypixel Fairy Souls"),
                        Config.load().showHypixelFairySouls
                    )
                    .setTooltip(Component.literal("Just a feature I needed and decided to add to this mod. Shows locations of Fairy Souls in Hypixel Skyblock."))
                    .setSaveConsumer { Config.save(Config.load().copy(showHypixelFairySouls = it)) }
                    .build()
            )

        return configBuilder.build()
    }
}