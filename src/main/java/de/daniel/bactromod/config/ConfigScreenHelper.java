package de.daniel.bactromod.config;

import de.daniel.bactromod.config.optiontypes.BooleanOption;
import de.daniel.bactromod.config.optiontypes.IntegerOption;
import de.daniel.bactromod.utils.SystemInfo;
import de.daniel.bactromod.windowborder.DwmApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConfigScreenHelper {
    private static final String OPTION_KEY_PREFIX = "bactromod.options.";
    private static final String OPTION_DESC_SUFFIX = ".desc";

    public static GameOptionsScreen getConfigScreen(Screen parentScreen) {
        ConfigData data = Config.load();
        GameOptions gameOptions = MinecraftClient.getInstance() != null ? MinecraftClient.getInstance().options : null;
        if (gameOptions == null) throw new IllegalStateException("MinecraftClient is not initialized");

        List<SimpleOption<?>> options = Arrays.stream(ConfigData.class.getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .map(field -> {
                    if (field.isAnnotationPresent(BooleanOption.class)) {
                        return createBooleanOption(field, data);
                    } else if (field.isAnnotationPresent(IntegerOption.class)) {
                        return createIntegerOption(field, data);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());

        return new GameOptionsScreen(parentScreen, gameOptions, Text.translatable(OPTION_KEY_PREFIX + "title")) {
            @Override
            protected void addOptions() {
                if (this.body == null) return;
                this.body.addAll(options.toArray(new SimpleOption<?>[0]));
            }
        };
    }

    private static SimpleOption<Boolean> createBooleanOption(Field field, ConfigData data) {
        try {
            boolean value = field.getBoolean(data);
            return SimpleOption.ofBoolean(
                    OPTION_KEY_PREFIX + field.getName(),
                    SimpleOption.constantTooltip(Text.translatable(OPTION_KEY_PREFIX + field.getName() + OPTION_DESC_SUFFIX)),
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

    private static SimpleOption<Integer> createIntegerOption(Field field, ConfigData data) {
        try {
            int value = field.getInt(data);
            IntegerOption integerOption = field.getAnnotation(IntegerOption.class);
            return new SimpleOption<>(
                    OPTION_KEY_PREFIX + field.getName(),
                    SimpleOption.constantTooltip(Text.translatable(OPTION_KEY_PREFIX + field.getName() + OPTION_DESC_SUFFIX)),
                    GameOptions::getGenericValueText,
                    new SimpleOption.ValidatingIntSliderCallbacks(integerOption.intMin(), integerOption.intMax()),
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

}
