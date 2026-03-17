package de.daniel.bactromod.config;

import de.daniel.bactromod.config.optiontypes.BooleanOption;
import de.daniel.bactromod.config.optiontypes.IntegerOption;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

public class ConfigScreenHelper {
    private static final String OPTION_KEY_PREFIX = "bactromod.options.";
    private static final String OPTION_DESC_SUFFIX = ".desc";

    public static OptionsSubScreen getConfigScreen(Screen parentScreen) {
        ConfigData data = Config.load();
        Options gameOptions = Minecraft.getInstance().options;

        List<OptionInstance<?>> configOptions = new ArrayList<>();
        for (Field field : ConfigData.class.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(BooleanOption.class)) {
                configOptions.add(createBooleanOption(field, data));
            } else if (field.isAnnotationPresent(IntegerOption.class)) {
                configOptions.add(createIntegerOption(field, data));
            }
        }

        return new OptionsSubScreen(parentScreen, gameOptions, Component.translatable(OPTION_KEY_PREFIX + "title")) {
            @Override
            protected void addOptions() {
                if (this.list == null) return;
                this.list.addSmall(configOptions.toArray(new OptionInstance<?>[0]));
            }
        };
    }

    private static OptionInstance<Boolean> createBooleanOption(Field field, ConfigData data) {
        try {
            boolean value = field.getBoolean(data);
            return OptionInstance.createBoolean(
                    optionKey(field.getName()),
                    OptionInstance.cachedConstantTooltip(Component.translatable(optionDescKey(field.getName()))),
                    value,
                    val -> {
                        try {
                            field.setBoolean(data, val);
                            Config.save(data);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static OptionInstance<Integer> createIntegerOption(Field field, ConfigData data) {
        try {
            int value = field.getInt(data);
            IntegerOption integerOption = field.getAnnotation(IntegerOption.class);
            return new OptionInstance<>(
                    optionKey(field.getName()),
                    OptionInstance.cachedConstantTooltip(Component.translatable(optionDescKey(field.getName()))),
                    Options::genericValueLabel,
                    new OptionInstance.IntRange(integerOption.intMin(), integerOption.intMax()),
                    value,
                    val -> {
                        try {
                            field.setInt(data, val);
                            Config.save(data);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static String optionKey(String fieldName) {
        return OPTION_KEY_PREFIX + fieldName;
    }

    private static String optionDescKey(String fieldName) {
        return optionKey(fieldName) + OPTION_DESC_SUFFIX;
    }

}
