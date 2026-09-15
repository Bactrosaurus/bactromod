package de.daniel.bactromod.config;

import de.daniel.bactromod.config.optiontypes.BooleanOption;
import de.daniel.bactromod.config.optiontypes.IntegerOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class ConfigScreen {
    private ConfigScreen() {}

    public static OptionsSubScreen getConfigScreen(Screen parentScreen) {
        ConfigData data = Config.get();
        OptionInstance<?>[] configOptions = Arrays.stream(ConfigData.class.getDeclaredFields())
                .map(field -> configOption(field, data))
                .filter(java.util.Objects::nonNull)
                .toArray(OptionInstance<?>[]::new);

        return new OptionsSubScreen(
                parentScreen,
                Minecraft.getInstance().options,
                text("title")
        ) {
            @Override
            protected void addOptions() {
                if (this.list == null) return;
                this.list.addSmall(configOptions);

                this.list.addSmall(List.of(
                        Button.builder(
                                text("itemScalingFactors"),
                                _ -> Minecraft.getInstance().gui.setScreen(new ItemScalingScreen(this,
                                        Minecraft.getInstance().options, text("title").append(" - ")
                                        .append(text("itemScalingFactors"))))
                        ).tooltip(Tooltip.create(text("itemScalingFactors.desc"))).build()
                ));
            }
        };
    }

    private static OptionInstance<?> configOption(Field field, ConfigData data) {
        if (field.isAnnotationPresent(BooleanOption.class)) return booleanOption(field, data);
        IntegerOption option = field.getAnnotation(IntegerOption.class);
        return option == null ? null : integerOption(field, data, option);
    }

    private static OptionInstance<Boolean> booleanOption(Field field, ConfigData data) {
        String name = field.getName();
        try {
            return OptionInstance.createBoolean(key(name),
                    OptionInstance.cachedConstantTooltip(text(name + ".desc")), field.getBoolean(data),
                    value -> updateBoolean(field, data, value));
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot access config field " + name, e);
        }
    }

    private static OptionInstance<Integer> integerOption(Field field, ConfigData data, IntegerOption option) {
        String name = field.getName();
        try {
            return new OptionInstance<>(key(name),
                    OptionInstance.cachedConstantTooltip(text(name + ".desc")), Options::genericValueLabel,
                    new OptionInstance.IntRange(option.min(), option.max()), field.getInt(data),
                    value -> updateInteger(field, data, value));
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot access config field " + name, e);
        }
    }

    private static OptionInstance<Integer> itemScalingOption(String descriptionId, int value, ConfigData data) {
        return new OptionInstance<>(descriptionId, OptionInstance.cachedConstantTooltip(Component.translatable(
                "bactromod.options.itemScalingFactors.itemDesc", Component.translatable(descriptionId))),
                Options::genericValueLabel, new OptionInstance.IntRange(1, 100), value, newValue -> {
            data.itemScalingFactors.put(descriptionId, newValue);
            Config.save();
        });
    }

    private static MutableComponent text(String name) {
        return Component.translatable(key(name));
    }

    private static String key(String name) { return "bactromod.options." + name; }

    private static void updateBoolean(Field field, ConfigData data, boolean value) {
        try {
            field.setBoolean(data, value);
            Config.save();
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot access config field " + field.getName(), e);
        }
    }

    private static void updateInteger(Field field, ConfigData data, int value) {
        try {
            field.setInt(data, value);
            Config.save();
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot access config field " + field.getName(), e);
        }
    }

    private static class ItemScalingScreen extends OptionsSubScreen {
        private final List<ScalableItem> allItems = new ArrayList<>();
        private String searchFilter = "";

        ItemScalingScreen(Screen parentScreen, Options options, Component title) {
            super(parentScreen, options, title);
            loadItems();
        }

        private void loadItems() {
            ConfigData data = Config.get();
            Set<String> seen = new HashSet<>();
            for (Item item : BuiltInRegistries.ITEM) {
                if (item == Items.AIR) continue;
                String descId = item.getDescriptionId();
                String path = BuiltInRegistries.ITEM.getKey(item).getPath();
                addItem(data, seen, descId, path, data.itemScalingFactors.getOrDefault(descId, 100));
            }
            data.itemScalingFactors.forEach((descId, scaling) -> addItem(data, seen, descId, descId, scaling));
            allItems.sort(Comparator.comparing(ScalableItem::localizedName, String.CASE_INSENSITIVE_ORDER));
        }

        private void addItem(ConfigData data, Set<String> seen, String descId, String path, int scaling) {
            if (!seen.add(descId)) return;
            OptionInstance<Integer> option = itemScalingOption(descId, scaling, data);
            String localizedName = Component.translatable(descId).getString();
            allItems.add(new ScalableItem(descId, localizedName, path, option));
        }

        @Override
        protected void addTitle() {
            this.layout.setHeaderHeight(48);
            LinearLayout header = LinearLayout.vertical().spacing(4);
            header.defaultCellSetting().alignHorizontallyCenter();
            header.addChild(new StringWidget(this.title, this.font));

            EditBox searchBox = new EditBox(this.font, 200, 20, text("search"));
            searchBox.setHint(text("search").setStyle(EditBox.SEARCH_HINT_STYLE));
            searchBox.setValue(this.searchFilter);
            searchBox.setResponder(query -> {
                this.searchFilter = query;
                filterOptions(query);
            });
            header.addChild(searchBox);

            this.layout.addToHeader(header);
        }

        @Override
        protected void addContents() {
            this.list = this.layout.addToContents(new ItemOptionsList(this.minecraft, this.width, this));
            this.addOptions();
        }

        @Override
        protected void addOptions() {
            filterOptions(this.searchFilter);
        }

        private void filterOptions(String query) {
            if (!(this.list instanceof ItemOptionsList itemList)) return;

            itemList.clear();
            String filter = query.trim().toLowerCase(Locale.ROOT);
            itemList.addSmall(this.allItems.stream()
                    .filter(item -> filter.isEmpty() || item.matches(filter))
                    .map(ScalableItem::option)
                    .toArray(OptionInstance<?>[]::new));
            itemList.setScrollAmount(0);
        }
    }

    private static class ItemOptionsList extends OptionsList {
        ItemOptionsList(Minecraft minecraft, int width, OptionsSubScreen screen) {
            super(minecraft, width, screen);
        }

        void clear() {
            this.clearEntries();
        }
    }

    private record ScalableItem(String descId, String localizedName, String path, OptionInstance<Integer> option) {
        boolean matches(String query) {
            return localizedName.toLowerCase(Locale.ROOT).contains(query)
                    || path.toLowerCase(Locale.ROOT).contains(query)
                    || descId.toLowerCase(Locale.ROOT).contains(query);
        }
    }

}
