package de.daniel.bactromod.mixins.settingsbutton;

import de.daniel.bactromod.config.ConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.CreditsAndAttributionScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.awt.*;

@Mixin(CreditsAndAttributionScreen.class)
public class MixinCreditsAndAttributionScreen {

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;add(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;", ordinal = 2))
    public Widget addChildAlt(DirectionalLayoutWidget instance, Widget layoutElement) {
        CreditsAndAttributionScreen inst = ((CreditsAndAttributionScreen) (Object) this);
        instance.add(layoutElement, instance.copyPositioner());
        ButtonWidget settingsButton = ButtonWidget.builder(
            Text.translatable("bactromod.options.title"),
            button -> MinecraftClient.getInstance().setScreen(new ConfigScreen(inst))).width(210).build();
        return instance.add(settingsButton);
    }
}
