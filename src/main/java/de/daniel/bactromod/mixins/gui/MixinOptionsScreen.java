package de.daniel.bactromod.mixins.gui;

import de.daniel.bactromod.gui.CustomAccessibilityOptionsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(OptionsScreen.class)
public class MixinOptionsScreen {

    @Shadow
    @Final
    private Options options;

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/OptionsScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;", ordinal = 11))
    public GuiEventListener changeScreen(GuiEventListener par1) {
        Minecraft mc = Minecraft.getInstance();
        OptionsScreen instance = ((OptionsScreen) (Object) this);
        return new Button(instance.width / 2 + 5, instance.height / 6 + 120 - 6, 150, 20,
                Component.translatable("options.accessibility.title"),
                (button) -> mc.setScreen(new CustomAccessibilityOptionsScreen(instance, options)));
    }

}