package de.daniel.bactromod.gui

import de.daniel.bactromod.config.ConfigScreen
import net.minecraft.client.Minecraft
import net.minecraft.client.Options
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class CustomAccessibilityOptionsScreen(screen: Screen, options: Options) : AccessibilityOptionsScreen(screen, options) {

    private lateinit var bactromodSettings: Button

    override fun init() {
        super.init()
        bactromodSettings = Button.builder(Component.literal("BactroMod Settings")) {
            Minecraft.getInstance().setScreen(ConfigScreen(this))
        }.size(150, 20).build()
    }

    override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        super.render(guiGraphics, i, j, f)
        guiGraphics.enableScissor(0, 32, width, height - 32)
        bactromodSettings.setPosition(width / 2 + 5, 32 + 4 - list.scrollAmount.toInt() + 10 * 25)
        bactromodSettings.render(guiGraphics, i, j, f)
        guiGraphics.disableScissor()
    }

    override fun mouseClicked(d: Double, e: Double, i: Int): Boolean {
        if (bactromodSettings.isMouseOver(d, e)) {
            bactromodSettings.mouseClicked(d, e, i)
        }
        return super.mouseClicked(d, e, i)
    }

}