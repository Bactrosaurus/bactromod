package de.daniel.bactromod.impl

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import de.daniel.bactromod.config.ConfigScreen

class ModMenuIntegration : ModMenuApi {

    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> = ConfigScreenFactory { ConfigScreen(it) }

}