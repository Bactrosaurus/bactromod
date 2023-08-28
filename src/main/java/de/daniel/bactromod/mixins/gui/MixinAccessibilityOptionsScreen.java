package de.daniel.bactromod.mixins.gui;

import de.daniel.bactromod.config.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SimpleOptionsSubScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AccessibilityOptionsScreen.class)
public class MixinAccessibilityOptionsScreen extends SimpleOptionsSubScreen {

    public MixinAccessibilityOptionsScreen(Screen screen, Options options, Component component, OptionInstance<?>[] optionInstances) {
        super(screen, options, component, optionInstances);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        AccessibilityOptionsScreen instance = ((AccessibilityOptionsScreen) (Object) this);

        int y0 = 32;
        int itemHeight = 25;

        int rowTop = y0 + 4 + 10 * itemHeight;

        addRenderableWidget(Button.builder(
                Component.literal("BactroMod Settings"),
                (button) ->
                        Minecraft.getInstance().setScreen(new ConfigScreen(instance)))
                .bounds(width / 2 + 5, rowTop, 150, 20).build());
    }

}
