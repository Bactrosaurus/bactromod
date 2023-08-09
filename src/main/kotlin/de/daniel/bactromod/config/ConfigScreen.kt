package de.daniel.bactromod.config

import net.minecraft.client.Minecraft
import net.minecraft.client.OptionInstance
import net.minecraft.client.Options
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.SimpleOptionsSubScreen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

class ConfigScreen(screen: Screen) : SimpleOptionsSubScreen(
    screen, Minecraft.getInstance().options, Component.literal("BactroMod Settings"), options()
) {

    companion object {

        private fun options(): Array<OptionInstance<*>> {
            return arrayOf(

                OptionInstance(
                "Gamma multiplier",
                OptionInstance.cachedConstantTooltip(Component.literal("Multiplies in-game gamma (brightness) by set number.")),
                Options::genericValueLabel,
                OptionInstance.IntRange(0, 15),
                Config.load().gammaMultiplier
                ) { Config.save(Config.load().copy(gammaMultiplier = it)) },

                OptionInstance.createBoolean(
                    "Disable pumpkin blur",
                    OptionInstance.cachedConstantTooltip(Component.literal("Disables black texture overlay that strongly limits view when player wears a carved pumpkin")),
                    Config.load().disablePumpkinBlur
                ) { Config.save(Config.load().copy(disablePumpkinBlur = it)) },

                OptionInstance(
                    "Fire offset",
                    OptionInstance.cachedConstantTooltip(Component.literal("Moves fire texture in height. Usually you would select a negative value to move fire texture down and free up your view.")),
                    Options::genericValueLabel,
                    OptionInstance.IntRange(-100, 100),
                    Config.load().fireOffset
                ) { Config.save(Config.load().copy(fireOffset = it)) },

                OptionInstance(
                    "Shield offset",
                    OptionInstance.cachedConstantTooltip(Component.literal("Moves shield in height when in first person perspective. Usually you would select a negative value to move shield down and free up your view.")),
                    Options::genericValueLabel,
                    OptionInstance.IntRange(-100, 100),
                    Config.load().shieldOffset
                ) { Config.save(Config.load().copy(shieldOffset = it)) },

                OptionInstance.createBoolean(
                    "Disable lava fog",
                    OptionInstance.cachedConstantTooltip(Component.literal("Disables fog caused by diving into lava.")),
                    Config.load().disableLavaFog
                ) { Config.save(Config.load().copy(disableLavaFog = it)) },

                OptionInstance.createBoolean(
                    "Disable powder snow fog",
                    OptionInstance.cachedConstantTooltip(Component.literal("Disables fog caused by diving into powder snow.")),
                    Config.load().disablePowderSnowFog
                ) { Config.save(Config.load().copy(disablePowderSnowFog = it)) },

                OptionInstance.createBoolean(
                    "Disable blindness fog",
                    OptionInstance.cachedConstantTooltip(Component.literal("Disables fog caused by blindness effect.")),
                    Config.load().disableBlindnessFog
                ) { Config.save(Config.load().copy(disableBlindnessFog = it)) },

                OptionInstance.createBoolean(
                    "Disable darkness fog",
                    OptionInstance.cachedConstantTooltip(Component.literal("Disables fog caused by darkness effect.")),
                    Config.load().disableDarknessFog
                ) { Config.save(Config.load().copy(disableDarknessFog = it)) },

                OptionInstance.createBoolean(
                    "Disable water fog",
                    OptionInstance.cachedConstantTooltip(Component.literal("Disables fog caused by diving into water.")),
                    Config.load().disableWaterFog
                ) { Config.save(Config.load().copy(disableWaterFog = it)) },

                OptionInstance.createBoolean(
                    "Disable sky fog",
                    OptionInstance.cachedConstantTooltip(Component.literal("Disables sky fog.")),
                    Config.load().disableSkyFog
                ) { Config.save(Config.load().copy(disableSkyFog = it)) },

                OptionInstance.createBoolean(
                    "Disable thick fog",
                    OptionInstance.cachedConstantTooltip(Component.literal("Disables thick fog.")),
                    Config.load().disableThickFog
                ) { Config.save(Config.load().copy(disableThickFog = it)) },

                OptionInstance.createBoolean(
                    "Disable terrain fog",
                    OptionInstance.cachedConstantTooltip(Component.literal("Disables terrain fog.")),
                    Config.load().disableTerrainFog
                ) { Config.save(Config.load().copy(disableTerrainFog = it)) },

                OptionInstance.createBoolean(
                    "Show Hypixel fairy souls",
                    OptionInstance.cachedConstantTooltip(Component.literal("Just a feature I needed and decided to add to this mod. Shows locations of Fairy Souls in Hypixel Skyblock.")),
                    Config.load().showHypixelFairySouls
                ) { Config.save(Config.load().copy(showHypixelFairySouls = it)) }

            )
        }

    }

    override fun createFooter() {
        addRenderableWidget(
            Button.builder(
                CommonComponents.GUI_DONE
            ) { minecraft!!.setScreen(lastScreen) }.bounds(width / 2 - 75, height - 27, 150, 20).build()
        )
    }

}