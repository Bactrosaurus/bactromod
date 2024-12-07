package de.daniel.bactromod.mixins.settingsbutton;

import de.daniel.bactromod.config.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.CreditsAndAttributionScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreditsAndAttributionScreen.class)
public class MixinCreditsAndAttributionScreen {

    @SuppressWarnings("unchecked")
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/LinearLayout;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;", ordinal = 2))
    public LayoutElement addChildAlt(LinearLayout instance, LayoutElement layoutElement) {
        CreditsAndAttributionScreen inst = ((CreditsAndAttributionScreen) (Object) this);
        instance.addChild(layoutElement, instance.newCellSettings());
        Button settingsButton = Button.builder(
            Component.translatable("bactromod.options.title"),
            button -> Minecraft.getInstance().setScreen(new ConfigScreen(inst))).width(210).build();
        return instance.addChild(settingsButton);
    }
}
