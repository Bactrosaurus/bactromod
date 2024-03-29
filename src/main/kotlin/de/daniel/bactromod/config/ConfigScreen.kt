package de.daniel.bactromod.config

import de.daniel.bactromod.impl.SystemInfo
import de.daniel.bactromod.windowborder.DwmApi
import net.minecraft.client.Minecraft
import net.minecraft.client.OptionInstance
import net.minecraft.client.Options
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.SimpleOptionsSubScreen
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component

class ConfigScreen(screen: Screen) : SimpleOptionsSubScreen(
    screen, Minecraft.getInstance().options, Component.translatable("bactromod.options.title"), options()
) {

    companion object {

        private fun options(): Array<OptionInstance<*>> {
            return arrayOf(
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
                    Config.load().disablePumpkinBlur
                ) { Config.save(Config.load().copy(disablePumpkinBlur = it)) },

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
                    Config.load().disableLavaFog
                ) { Config.save(Config.load().copy(disableLavaFog = it)) },

                OptionInstance.createBoolean(
                    "bactromod.options.snowfog",
                    OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.snowfog.description")),
                    Config.load().disablePowderSnowFog
                ) { Config.save(Config.load().copy(disablePowderSnowFog = it)) },

                OptionInstance.createBoolean(
                    "bactromod.options.blindnessfog",
                    OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.blindnessfog.description")),
                    Config.load().disableBlindnessFog
                ) { Config.save(Config.load().copy(disableBlindnessFog = it)) },

                OptionInstance.createBoolean(
                    "bactromod.options.darknessfog",
                    OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.darknessfog.description")),
                    Config.load().disableDarknessFog
                ) { Config.save(Config.load().copy(disableDarknessFog = it)) },

                OptionInstance.createBoolean(
                    "bactromod.options.waterfog",
                    OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.waterfog.description")),
                    Config.load().disableWaterFog
                ) { Config.save(Config.load().copy(disableWaterFog = it)) },

                OptionInstance.createBoolean(
                    "bactromod.options.skyfog",
                    OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.skyfog.description")),
                    Config.load().disableSkyFog
                ) { Config.save(Config.load().copy(disableSkyFog = it)) },

                OptionInstance.createBoolean(
                    "bactromod.options.thickfog",
                    OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.thickfog.description")),
                    Config.load().disableThickFog
                ) { Config.save(Config.load().copy(disableThickFog = it)) },

                OptionInstance.createBoolean(
                    "bactromod.options.terrainfog",
                    OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.terrainfog.description")),
                    Config.load().disableTerrainFog
                ) { Config.save(Config.load().copy(disableTerrainFog = it)) },

                OptionInstance.createBoolean(
                    "bactromod.options.windowborders",
                    OptionInstance.cachedConstantTooltip(Component.translatable("bactromod.options.windowborders.description")),
                    Config.load().showNiceWindowBorders
                ) {
                    Config.save(Config.load().copy(showNiceWindowBorders = it))
                    if (SystemInfo.systemIsWindows11) DwmApi.updateDwm(Minecraft.getInstance().window.window)
                }
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