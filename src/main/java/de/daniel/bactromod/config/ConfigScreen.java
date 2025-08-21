package de.daniel.bactromod.config;

import de.daniel.bactromod.utils.SystemInfo;
import de.daniel.bactromod.windowborder.DwmApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

public class ConfigScreen extends GameOptionsScreen {

    public ConfigScreen(Screen screen) {
        super(screen, MinecraftClient.getInstance().options, Text.translatable("bactromod.options.title"));
    }

    @Override
    protected void addOptions() {
        if (this.body == null) return;

        this.body.addAll(
                new SimpleOption<>(
                        "bactromod.options.gamma",
                        SimpleOption.constantTooltip(Text.translatable("bactromod.options.gamma.description")),
                        GameOptions::getGenericValueText,
                        new SimpleOption.ValidatingIntSliderCallbacks(0, 15),
                        Config.load().gammaMultiplier(),
                        val -> Config.save(Config.load().withGammaMultiplier(val))
                ),

                SimpleOption.ofBoolean(
                        "bactromod.options.pumpkinblur",
                        SimpleOption.constantTooltip(Text.translatable("bactromod.options.pumpkinblur.description")),
                        Config.load().pumpkinBlur(),
                        val -> Config.save((Config.load().withPumpkinBlur(val)))
                ),

                new SimpleOption<>(
                        "bactromod.options.fireoffset",
                        SimpleOption.constantTooltip(Text.translatable("bactromod.options.fireoffset.description")),
                        GameOptions::getGenericValueText,
                        new SimpleOption.ValidatingIntSliderCallbacks(-100, 100),
                        Config.load().fireOffset(),
                        val -> Config.save(Config.load().withFireOffset(val))
                ),

                new SimpleOption<>(
                        "bactromod.options.shieldoffset",
                        SimpleOption.constantTooltip(Text.translatable("bactromod.options.shieldoffset.description")),
                        GameOptions::getGenericValueText,
                        new SimpleOption.ValidatingIntSliderCallbacks(-100, 100),
                        Config.load().shieldOffset(),
                        val -> Config.save(Config.load().withShieldOffset(val))
                ),

                SimpleOption.ofBoolean(
                        "bactromod.options.boatmap",
                        SimpleOption.constantTooltip(Text.translatable("bactromod.options.boatmap.description")),
                        Config.load().showMapWhileInBoat(),
                        val -> Config.save(Config.load().withShowMapWhileInBoat(val))
                ),

                SimpleOption.ofBoolean(
                        "bactromod.options.lavafog",
                        SimpleOption.constantTooltip(Text.translatable("bactromod.options.lavafog.description")),
                        Config.load().lavaFog(),
                        val -> Config.save(Config.load().withLavaFog(val))
                ),

                SimpleOption.ofBoolean(
                        "bactromod.options.snowfog",
                        SimpleOption.constantTooltip(Text.translatable("bactromod.options.snowfog.description")),
                        Config.load().powderSnowFog(),
                        val -> Config.save(Config.load().withPowderSnowFog(val))
                ),

                SimpleOption.ofBoolean(
                        "bactromod.options.blindnessfog",
                        SimpleOption.constantTooltip(Text.translatable("bactromod.options.blindnessfog.description")),
                        Config.load().blindnessFog(),
                        val -> Config.save(Config.load().withBlindnessFog(val))
                ),

                SimpleOption.ofBoolean(
                        "bactromod.options.darknessfog",
                        SimpleOption.constantTooltip(Text.translatable("bactromod.options.darknessfog.description")),
                        Config.load().darknessFog(),
                        val -> Config.save(Config.load().withDarknessFog(val))
                ),

                SimpleOption.ofBoolean(
                        "bactromod.options.waterfog",
                        SimpleOption.constantTooltip(Text.translatable("bactromod.options.waterfog.description")),
                        Config.load().waterFog(),
                        val -> Config.save(Config.load().withWaterFog(val))
                ),

                SimpleOption.ofBoolean(
                        "bactromod.options.dimensionBossFog",
                        SimpleOption.constantTooltip(Text.translatable("bactromod.options.dimensionBossFog.description")),
                        Config.load().dimensionBossFog(),
                        val -> Config.save(Config.load().withDimensionBossFog(val))
                ),

                SimpleOption.ofBoolean(
                        "bactromod.options.atmosphericFog",
                        SimpleOption.constantTooltip(Text.translatable("bactromod.options.atmosphericFog.description")),
                        Config.load().atmosphericFog(),
                        val -> Config.save(Config.load().withAtmosphericFog(val))
                ),

                SimpleOption.ofBoolean(
                        "bactromod.options.renderDistanceFog",
                        SimpleOption.constantTooltip(Text.translatable("bactromod.options.renderDistanceFog.description")),
                        Config.load().renderDistanceFog(),
                        val -> Config.save(Config.load().withRenderDistanceFog(val))
                ),

                SimpleOption.ofBoolean(
                        "bactromod.options.fixShieldRiptideTrident",
                        SimpleOption.constantTooltip(Text.translatable("bactromod.options.fixShieldRiptideTrident.description")),
                        Config.load().fixShieldRiptideTrident(),
                        val -> Config.save(Config.load().withFixShieldRiptideTrident(val))
                ),

                SimpleOption.ofBoolean(
                        "bactromod.options.windowborders",
                        SimpleOption.constantTooltip(Text.translatable("bactromod.options.windowborders.description")),
                        Config.load().darkWindowBorders(),
                        val -> {
                            Config.save(Config.load().withDarkWindowBorders(val));
                            if (SystemInfo.isWindows11) DwmApi.updateDwm(MinecraftClient.getInstance().getWindow().getHandle());
                        }
                )
        );
    }

}