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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class ConfigScreen extends OptionsSubScreen {
    private final OptionInstance<?>[] configOptions;

    public ConfigScreen(Screen parentScreen) {
        super(parentScreen, Minecraft.getInstance().options, text("title"));
        ConfigData data = Config.get();
        this.configOptions = Arrays.stream(ConfigData.class.getDeclaredFields())
                .map(field -> configOption(field, data))
                .filter(Objects::nonNull)
                .toArray(OptionInstance<?>[]::new);
    }

    @Override
    protected void addOptions() {
        if (this.list == null) return;
        Button itemScalingButton = Button.builder(
                text("itemScalingFactors"),
                _ -> this.minecraft.gui.setScreen(new ItemScalingScreen(
                        this,
                        this.options,
                        text("title").append(" - ").append(text("itemScalingFactors"))
                ))
        ).tooltip(Tooltip.create(text("itemScalingFactors.desc"))).build();

        int count = this.configOptions.length;
        if (count % 2 == 1) {
            this.list.addSmall(Arrays.copyOf(this.configOptions, count - 1));
            OptionInstance<?> lastOption = this.configOptions[count - 1];
            this.list.addSmall(lastOption.createButton(this.options), lastOption, itemScalingButton);
        } else {
            this.list.addSmall(this.configOptions);
            this.list.addSmall(itemScalingButton, null);
        }
    }

    private static OptionInstance<?> configOption(Field field, ConfigData data) {
        if (field.isAnnotationPresent(BooleanOption.class)) return booleanOption(field, data);
        IntegerOption option = field.getAnnotation(IntegerOption.class);
        return option == null ? null : integerOption(field, data, option);
    }

    private static OptionInstance<Boolean> booleanOption(Field field, ConfigData data) {
        String name = field.getName();
        return OptionInstance.createBoolean(
                key(name),
                OptionInstance.cachedConstantTooltip(text(name + ".desc")),
                (Boolean) readField(field, data),
                value -> writeField(field, data, value)
        );
    }

    private static OptionInstance<Integer> integerOption(Field field, ConfigData data, IntegerOption option) {
        String name = field.getName();
        int initialValue = Math.clamp((Integer) readField(field, data), option.min(), option.max());
        return new OptionInstance<>(
                key(name),
                OptionInstance.cachedConstantTooltip(text(name + ".desc")),
                Options::genericValueLabel,
                new OptionInstance.IntRange(option.min(), option.max()),
                initialValue,
                value -> writeField(field, data, value)
        );
    }

    private static OptionInstance<Integer> itemScalingOption(String displayDescId, List<String> descriptionIds, int value, ConfigData data) {
        return new OptionInstance<>(
                displayDescId,
                OptionInstance.cachedConstantTooltip(Component.translatable(
                        "bactromod.options.itemScalingFactors.itemDesc", Component.translatable(displayDescId))),
                Options::genericValueLabel,
                new OptionInstance.IntRange(1, 100),
                Math.clamp(value, 1, 100),
                newValue -> {
                    for (String id : descriptionIds) {
                        if (newValue == 100) data.itemScalingFactors.remove(id);
                        else data.itemScalingFactors.put(id, newValue);
                    }
                    Config.save();
                }
        );
    }

    private static Object readField(Field field, ConfigData data) {
        try {
            return field.get(data);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot access config field " + field.getName(), e);
        }
    }

    private static void writeField(Field field, ConfigData data, Object value) {
        try {
            field.set(data, value);
            Config.save();
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot access config field " + field.getName(), e);
        }
    }

    private static MutableComponent text(String name) {
        return Component.translatable(key(name));
    }

    private static String key(String name) {
        return "bactromod.options." + name;
    }

    private static final class ItemScalingScreen extends OptionsSubScreen {
        private final List<ScalableItem> allItems = new ArrayList<>();
        private String searchFilter = "";

        ItemScalingScreen(Screen parentScreen, Options options, Component title) {
            super(parentScreen, options, title);
            loadItems();
        }

        private void loadItems() {
            ConfigData data = Config.get();
            Set<String> seenIds = new HashSet<>();
            Map<String, ItemGroup> groups = new LinkedHashMap<>();

            for (Item item : BuiltInRegistries.ITEM) {
                if (item == Items.AIR) continue;
                String descId = item.getDescriptionId();
                if (!seenIds.add(descId)) continue;
                String path = BuiltInRegistries.ITEM.getKey(item).getPath();
                addToGroup(groups, descId, path);
            }
            data.itemScalingFactors.keySet().forEach(descId -> {
                if (seenIds.add(descId)) addToGroup(groups, descId, descId);
            });

            groups.values().stream()
                    .map(group -> group.toScalableItem(data))
                    .sorted(Comparator.comparing(ScalableItem::localizedName, String.CASE_INSENSITIVE_ORDER))
                    .forEach(allItems::add);
        }

        private static void addToGroup(Map<String, ItemGroup> groups, String descId, String path) {
            String localizedName = Component.translatable(descId).getString();
            groups.computeIfAbsent(localizedName, name -> new ItemGroup(descId, name)).add(descId, path);
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

    private static final class ItemGroup {
        private final String displayDescId;
        private final String localizedName;
        private final List<String> descriptionIds = new ArrayList<>();
        private final StringBuilder searchText = new StringBuilder();

        ItemGroup(String displayDescId, String localizedName) {
            this.displayDescId = displayDescId;
            this.localizedName = localizedName;
            this.searchText.append(localizedName.toLowerCase(Locale.ROOT));
        }

        void add(String descId, String path) {
            descriptionIds.add(descId);
            searchText.append('\0').append(path.toLowerCase(Locale.ROOT))
                    .append('\0').append(descId.toLowerCase(Locale.ROOT));
        }

        ScalableItem toScalableItem(ConfigData data) {
            int initialScaling = descriptionIds.stream()
                    .map(data.itemScalingFactors::get)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(100);
            OptionInstance<Integer> option = itemScalingOption(displayDescId, List.copyOf(descriptionIds), initialScaling, data);
            return new ScalableItem(localizedName, searchText.toString(), option);
        }
    }

    private static final class ItemOptionsList extends OptionsList {
        ItemOptionsList(Minecraft minecraft, int width, OptionsSubScreen screen) {
            super(minecraft, width, screen);
        }

        void clear() {
            this.clearEntries();
        }
    }

    private record ScalableItem(String localizedName, String searchText, OptionInstance<Integer> option) {
        boolean matches(String query) {
            return searchText.contains(query);
        }
    }
}
