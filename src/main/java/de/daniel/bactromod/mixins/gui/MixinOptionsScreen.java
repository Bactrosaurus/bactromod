package de.daniel.bactromod.mixins.gui;

import de.daniel.bactromod.gui.CustomAccessibilityOptionsScreen;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin(OptionsScreen.class)
public abstract class MixinOptionsScreen {

    @Shadow
    @Final
    private Options options;

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/OptionsScreen;openScreenButton(Lnet/minecraft/network/chat/Component;Ljava/util/function/Supplier;)Lnet/minecraft/client/gui/components/Button;", ordinal = 7), index = 1)
    private Supplier<Screen> injected(Supplier<Screen> supplier) {
        OptionsScreen instance = ((OptionsScreen) (Object) this);
        return () -> new CustomAccessibilityOptionsScreen(instance, this.options);
    }

}