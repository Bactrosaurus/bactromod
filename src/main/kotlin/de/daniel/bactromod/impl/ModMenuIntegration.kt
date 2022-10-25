package de.daniel.bactromod.impl

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import de.daniel.bactromod.config.ConfigScreen
import net.minecraft.client.gui.screens.Screen

class ModMenuIntegration : ModMenuApi {

    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent: Screen ->
            ConfigScreen.getConfigScreen(parent)
        }
    }

}