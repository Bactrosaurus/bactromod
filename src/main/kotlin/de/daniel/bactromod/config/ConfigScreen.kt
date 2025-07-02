package de.daniel.bactromod.config

import de.daniel.bactromod.utils.SystemInfo
import de.daniel.bactromod.windowborder.DwmApi
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.option.GameOptionsScreen
import net.minecraft.client.option.GameOptions
import net.minecraft.client.option.SimpleOption
import net.minecraft.text.Text

class ConfigScreen(screen: Screen) : GameOptionsScreen(
    screen, MinecraftClient.getInstance().options, Text.translatable("bactromod.options.title")
) {

    override fun addOptions() {
        this.body?.addAll(
            SimpleOption(
                "bactromod.options.gamma",
                SimpleOption.constantTooltip(Text.translatable("bactromod.options.gamma.description")),
                GameOptions::getGenericValueText,
                SimpleOption.ValidatingIntSliderCallbacks(0, 15),
                Config.load().gammaMultiplier
            ) { Config.save(Config.load().copy(gammaMultiplier = it)) },

            SimpleOption.ofBoolean(
                "bactromod.options.pumpkinblur",
                SimpleOption.constantTooltip(Text.translatable("bactromod.options.pumpkinblur.description")),
                Config.load().pumpkinBlur
            ) { Config.save(Config.load().copy(pumpkinBlur = it)) },

            SimpleOption(
                "bactromod.options.fireoffset",
                SimpleOption.constantTooltip(Text.translatable("bactromod.options.fireoffset.description")),
                GameOptions::getGenericValueText,
                SimpleOption.ValidatingIntSliderCallbacks(-100, 100),
                Config.load().fireOffset
            ) { Config.save(Config.load().copy(fireOffset = it)) },

            SimpleOption(
                "bactromod.options.shieldoffset",
                SimpleOption.constantTooltip(Text.translatable("bactromod.options.shieldoffset.description")),
                GameOptions::getGenericValueText,
                SimpleOption.ValidatingIntSliderCallbacks(-100, 100),
                Config.load().shieldOffset
            ) { Config.save(Config.load().copy(shieldOffset = it)) },

            SimpleOption.ofBoolean(
                "bactromod.options.boatmap",
                SimpleOption.constantTooltip(Text.translatable("bactromod.options.boatmap.description")),
                Config.load().showMapWhileInBoat
            ) { Config.save(Config.load().copy(showMapWhileInBoat = it)) },

            SimpleOption.ofBoolean(
                "bactromod.options.lavafog",
                SimpleOption.constantTooltip(Text.translatable("bactromod.options.lavafog.description")),
                Config.load().lavaFog
            ) { Config.save(Config.load().copy(lavaFog = it)) },

            SimpleOption.ofBoolean(
                "bactromod.options.snowfog",
                SimpleOption.constantTooltip(Text.translatable("bactromod.options.snowfog.description")),
                Config.load().powderSnowFog
            ) { Config.save(Config.load().copy(powderSnowFog = it)) },

            SimpleOption.ofBoolean(
                "bactromod.options.blindnessfog",
                SimpleOption.constantTooltip(Text.translatable("bactromod.options.blindnessfog.description")),
                Config.load().blindnessFog
            ) { Config.save(Config.load().copy(blindnessFog = it)) },

            SimpleOption.ofBoolean(
                "bactromod.options.darknessfog",
                SimpleOption.constantTooltip(Text.translatable("bactromod.options.darknessfog.description")),
                Config.load().darknessFog
            ) { Config.save(Config.load().copy(darknessFog = it)) },

            SimpleOption.ofBoolean(
                "bactromod.options.waterfog",
                SimpleOption.constantTooltip(Text.translatable("bactromod.options.waterfog.description")),
                Config.load().waterFog
            ) { Config.save(Config.load().copy(waterFog = it)) },

            SimpleOption.ofBoolean(
                "bactromod.options.thickfog",
                SimpleOption.constantTooltip(Text.translatable("bactromod.options.thickfog.description")),
                Config.load().thickFog
            ) { Config.save(Config.load().copy(thickFog = it)) },

            SimpleOption.ofBoolean(
                "bactromod.options.terrainfog",
                SimpleOption.constantTooltip(Text.translatable("bactromod.options.terrainfog.description")),
                Config.load().terrainFog
            ) { Config.save(Config.load().copy(terrainFog = it)) },

            SimpleOption.ofBoolean(
                "bactromod.options.fixShieldRiptideTrident",
                SimpleOption.constantTooltip(Text.translatable("bactromod.options.fixShieldRiptideTrident.description")),
                Config.load().fixShieldRiptideTrident
            ) { Config.save(Config.load().copy(fixShieldRiptideTrident = it)) },

            SimpleOption.ofBoolean(
                "bactromod.options.windowborders",
                SimpleOption.constantTooltip(Text.translatable("bactromod.options.windowborders.description")),
                Config.load().darkWindowBorders
            ) {
                Config.save(Config.load().copy(darkWindowBorders = it))
                if (SystemInfo.isWindows11()) DwmApi.updateDwm(MinecraftClient.getInstance().window.handle)
            }
        )
    }

}