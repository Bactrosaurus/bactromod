package de.daniel.bactromod.config;

import de.daniel.bactromod.config.optiontypes.IntegerOption;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.lang.reflect.Field;

public class ConfigScreenUtils {

    private static final String OPTION_KEY_PREFIX = "bactromod.options.";

    public static MutableComponent getOptionName(String fieldName) {
        return Component.translatable(OPTION_KEY_PREFIX + fieldName);
    }

    public static MutableComponent getOptionDescKey(String fieldName) {
        return Component.translatable(OPTION_KEY_PREFIX + fieldName + ".desc");
    }

    private static String optionKey(String fieldName) {
        return OPTION_KEY_PREFIX + fieldName;
    }

    public static OptionInstance<Boolean> createBooleanOption(Field field, ConfigData data) {
        try {
            boolean value = field.getBoolean(data);
            return OptionInstance.createBoolean(
                    optionKey(field.getName()),
                    OptionInstance.cachedConstantTooltip(getOptionDescKey(field.getName())),
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

    public static OptionInstance<Integer> createIntegerOption(Field field, ConfigData data) {
        try {
            int value = field.getInt(data);
            IntegerOption integerOption = field.getAnnotation(IntegerOption.class);
            return new OptionInstance<>(
                    optionKey(field.getName()),
                    OptionInstance.cachedConstantTooltip(getOptionDescKey(field.getName())),
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

    public static OptionInstance<Integer> createItemScalingOption(String item, Integer currentScaling, ConfigData data) {
        return new OptionInstance<>(
                item,
                OptionInstance.cachedConstantTooltip(
                        Component.literal(String.format(getOptionName("itemScalingFactors.itemDesc").getString(), Component.translatable(item).getString()))
                ),
                Options::genericValueLabel,
                new OptionInstance.IntRange(1, 100),
                currentScaling,
                newScaling -> {
                    data.itemScalingFactors.put(item, newScaling);
                    Config.save(data);
                }
        );
    }

}