package de.daniel.bactromod.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ConfigScreenHelper {

    public static GameOptionsScreen getConfigScreen(Screen parentScreen) {
        List<SimpleOption<?>> options = new ArrayList<>();
        ConfigData data = Config.load();

        for (Field f : ConfigData.class.getDeclaredFields()) {
            ConfigOption opt = f.getAnnotation(ConfigOption.class);
            if (opt == null) continue;
            f.setAccessible(true);
            if (f.getType() == int.class) {
                try {
                    int value = f.getInt(data);
                    options.add(new SimpleOption<>(
                            "bactromod.options." + f.getName(),
                            SimpleOption.constantTooltip(Text.translatable("bactromod.options." + f.getName() + ".desc")),
                            GameOptions::getGenericValueText,
                            new SimpleOption.ValidatingIntSliderCallbacks(opt.intMin(), opt.intMax()),
                            value,
                            val -> {
                                try {
                                    f.setInt(data, val);
                                    Config.save(data);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    ));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else if (f.getType() == boolean.class) {
                try {
                    boolean value = f.getBoolean(data);
                    options.add(SimpleOption.ofBoolean(
                            "bactromod.options." + f.getName(),
                            SimpleOption.constantTooltip(Text.translatable("bactromod.options." + f.getName() + ".desc")),
                            value,
                            val -> {
                                try {
                                    f.setBoolean(data, val);
                                    Config.save(data);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    ));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return new GameOptionsScreen(
                parentScreen,
                MinecraftClient.getInstance().options,
                Text.translatable("bactromod.options.title")
        ) {
            @Override
            protected void addOptions() {
                if (this.body == null) return;
                this.body.addAll(options.toArray(new SimpleOption<?>[0]));
            }
        };
    }

}
