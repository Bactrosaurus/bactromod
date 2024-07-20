package de.daniel.bactromod.config

import de.daniel.bactromod.impl.SystemInfo
import de.daniel.bactromod.windowborder.DwmApi
import net.minecraft.client.Minecraft
import net.minecraft.client.OptionInstance
import net.minecraft.client.Options
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.options.OptionsSubScreen
import net.minecraft.network.chat.Component

class ConfigScreen(screen: Screen) : OptionsSubScreen(
    screen, Minecraft.getInstance().options, Component.translatable("bactromod.options.title")
) {

    override fun addOptions() {
        this.list?.addSmall(
            OptionInstance(
                "bactromod.options.gamma",
                OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.gamma.description")),
                Options::genericValueLabel,
                OptionInstance.IntRange(0, 15),
                Config.load().gammaMultiplier
            ) { Config.save(Config.load().copy(gammaMultiplier = it)) },

            OptionInstance.createBoolean(
                "bactromod.options.pumpkinblur",
                OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.pumpkinblur.description")),
                Config.load().pumpkinBlur
            ) { Config.save(Config.load().copy(pumpkinBlur = it)) },

            OptionInstance(
                "bactromod.options.fireoffset",
                OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.fireoffset.description")),
                Options::genericValueLabel,
                OptionInstance.IntRange(-100, 100),
                Config.load().fireOffset
            ) { Config.save(Config.load().copy(fireOffset = it)) },

            OptionInstance(
                "bactromod.options.shieldoffset",
                OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.shieldoffset.description")),
                Options::genericValueLabel,
                OptionInstance.IntRange(-100, 100),
                Config.load().shieldOffset
            ) { Config.save(Config.load().copy(shieldOffset = it)) },

            OptionInstance.createBoolean(
                "bactromod.options.boatmap",
                OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.boatmap.description")),
                Config.load().showMapWhileInBoat
            ) { Config.save(Config.load().copy(showMapWhileInBoat = it)) },

            OptionInstance.createBoolean(
                "bactromod.options.lavafog",
                OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.lavafog.description")),
                Config.load().lavaFog
            ) { Config.save(Config.load().copy(lavaFog = it)) },

            OptionInstance.createBoolean(
                "bactromod.options.snowfog",
                OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.snowfog.description")),
                Config.load().powderSnowFog
            ) { Config.save(Config.load().copy(powderSnowFog = it)) },

            OptionInstance.createBoolean(
                "bactromod.options.blindnessfog",
                OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.blindnessfog.description")),
                Config.load().blindnessFog
            ) { Config.save(Config.load().copy(blindnessFog = it)) },

            OptionInstance.createBoolean(
                "bactromod.options.darknessfog",
                OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.darknessfog.description")),
                Config.load().darknessFog
            ) { Config.save(Config.load().copy(darknessFog = it)) },

            OptionInstance.createBoolean(
                "bactromod.options.waterfog",
                OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.waterfog.description")),
                Config.load().waterFog
            ) { Config.save(Config.load().copy(waterFog = it)) },

            OptionInstance.createBoolean(
                "bactromod.options.skyfog",
                OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.skyfog.description")),
                Config.load().skyFog
            ) { Config.save(Config.load().copy(skyFog = it)) },

            OptionInstance.createBoolean(
                "bactromod.options.thickfog",
                OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.thickfog.description")),
                Config.load().thickFog
            ) { Config.save(Config.load().copy(thickFog = it)) },

            OptionInstance.createBoolean(
                "bactromod.options.terrainfog",
                OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.terrainfog.description")),
                Config.load().terrainFog
            ) { Config.save(Config.load().copy(terrainFog = it)) },

            OptionInstance.createBoolean(
                "bactromod.options.windowborders",
                OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.windowborders.description")),
                Config.load().darkWindowBorders
            ) {
                Config.save(Config.load().copy(darkWindowBorders = it))
                if (SystemInfo.systemIsWindows11) DwmApi.updateDwm(Minecraft.getInstance().window.window)
            }
        )
    }

}