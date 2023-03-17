package de.daniel.bactromod.gui

import de.daniel.bactromod.config.ConfigScreen
import net.minecraft.client.Options
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class CustomAccessibilityOptionsScreen(screen: Screen, options: Options) : AccessibilityOptionsScreen(screen, options) {

    @Override
    override fun init() {
        addRenderableWidget(
            Button.builder(
                Component.literal("BactroMod Settings")
            ) { minecraft!!.setScreen(ConfigScreen.getConfigScreen(this)) }
                .bounds(this.width / 2 + 5, height / 6 + 202, 150, 20).build()
        )
        super.init()
    }

}