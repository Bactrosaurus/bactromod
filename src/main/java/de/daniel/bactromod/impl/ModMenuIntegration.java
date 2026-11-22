package de.daniel.bactromod.impl;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import de.daniel.bactromod.config.ConfigScreen;
import org.jspecify.annotations.NonNull;

public final class ModMenuIntegration implements ModMenuApi {
    @Override
    public @NonNull ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigScreen::new;
    }
}
