package de.daniel.bactromod.mixins.hooks.settingsbutton;

import de.daniel.bactromod.settingsbutton.CustomAccessibilityOptionsScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.AccessibilityOnboardingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AccessibilityOnboardingScreen.class)
public abstract class MixinAccessibilityOnboardingScreen extends Screen {

    @Shadow protected abstract void closeAndSetScreen(Screen screen);

    protected MixinAccessibilityOnboardingScreen(Component component) {
        super(component);
    }

    @ModifyArg(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/CommonButtons;accessibilityTextAndImage(Lnet/minecraft/client/gui/components/Button$OnPress;)Lnet/minecraft/client/gui/components/TextAndImageButton;"), index = 0)
    private Button.OnPress injected(Button.OnPress onPress) {
        return button -> {
            assert minecraft != null;
            closeAndSetScreen(new CustomAccessibilityOptionsScreen(this, minecraft.options));
        };
    }

}
