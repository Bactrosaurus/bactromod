package de.daniel.bactromod.config;

import de.daniel.bactromod.config.optiontypes.BooleanOption;
import de.daniel.bactromod.config.optiontypes.IntegerOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ConfigScreen {

    public static OptionsSubScreen getConfigScreen(Screen parentScreen) {
        ConfigData data = Config.load();
        Options gameOptions = Minecraft.getInstance().options;

        List<OptionInstance<?>> configOptions = new ArrayList<>();

        for (Field field : ConfigData.class.getDeclaredFields()) {
            if (!field.isAnnotationPresent(BooleanOption.class) && !field.isAnnotationPresent(IntegerOption.class))
                continue;
            if (field.isAnnotationPresent(BooleanOption.class)) {
                configOptions.add(ConfigScreenUtils.createBooleanOption(field, data));
            } else if (field.isAnnotationPresent(IntegerOption.class)) {
                configOptions.add(ConfigScreenUtils.createIntegerOption(field, data));
            }
        }

        return new OptionsSubScreen(parentScreen, gameOptions, ConfigScreenUtils.getOptionName("title")) {
            @Override
            protected void addOptions() {
                if (this.list == null) return;
                this.list.addSmall(configOptions.toArray(new OptionInstance<?>[0]));

                // Add item scaling options
                this.list.addSmall(List.of(
                        Button.builder(
                                ConfigScreenUtils.getOptionName("itemScalingFactors"),
                                _ -> Minecraft.getInstance().gui.setScreen(getItemScalingScreen(this))
                        ).tooltip(Tooltip.create(ConfigScreenUtils.getOptionDescKey("itemScalingFactors"))).build()
                ));
            }
        };
    }

    private static OptionsSubScreen getItemScalingScreen(Screen parentScreen) {
        ConfigData data = Config.load();
        Options gameOptions = Minecraft.getInstance().options;
        List<OptionInstance<Integer>> configOptions = new ArrayList<>();
        data.itemScalingFactors.forEach((item, scaling) -> configOptions.add(ConfigScreenUtils.createItemScalingOption(item, scaling, data)));

        return new OptionsSubScreen(parentScreen, gameOptions, ConfigScreenUtils.getOptionName("title").append(" - ").append(ConfigScreenUtils.getOptionName("itemScalingFactors"))) {
            @Override
            protected void addOptions() {
                if (this.list == null) return;
                this.list.addSmall(configOptions.toArray(new OptionInstance<?>[0]));
            }
        };
    }

}
