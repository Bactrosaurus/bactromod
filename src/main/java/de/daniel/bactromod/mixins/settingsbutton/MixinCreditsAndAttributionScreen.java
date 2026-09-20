package de.daniel.bactromod.mixins.settingsbutton;

import com.llamalad7.mixinextras.sugar.Local;
import de.daniel.bactromod.config.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.CreditsAndAttributionScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreditsAndAttributionScreen.class)
public class MixinCreditsAndAttributionScreen {
    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/LinearLayout;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;", ordinal = 2, shift = At.Shift.AFTER), expect = 1)
    private void addSettingsButton(CallbackInfo ci, @Local(name = "content") LinearLayout content) {
        CreditsAndAttributionScreen screen = (CreditsAndAttributionScreen) (Object) this;
        content.addChild(Button.builder(Component.translatable("bactromod.options.title"), _ -> Minecraft.getInstance().gui.setScreen(ConfigScreen.getConfigScreen(screen))).width(210).build());
    }
}
