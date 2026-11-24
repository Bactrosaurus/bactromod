package de.daniel.bactromod.mixins.settingsbutton;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.daniel.bactromod.config.ConfigScreen;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;

@Mixin(AccessibilityOptionsScreen.class)
public abstract class MixinAccessibilityOptionsScreen extends OptionsSubScreen {
    protected MixinAccessibilityOptionsScreen(Screen lastScreen, Options options, Component title) {
        super(lastScreen, options, title);
    }

    @WrapOperation(method = "addOptions", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/OptionsList;addSmall([Lnet/minecraft/client/OptionInstance;)V"))
    private void addSettingsButton(OptionsList list, OptionInstance<?>[] options, Operation<Void> original) {
        Button settingsButton = Button.builder(
                Component.translatable("bactromod.options.title"),
                _ -> this.minecraft.gui.setScreen(new ConfigScreen(this))
        ).build();

        if (options.length % 2 == 1) {
            OptionInstance<?>[] paired = Arrays.copyOf(options, options.length - 1);
            original.call(list, paired);
            OptionInstance<?> last = options[options.length - 1];
            list.addSmall(last.createButton(this.options), last, settingsButton);
        } else {
            original.call(list, options);
            list.addSmall(settingsButton, null);
        }
    }
}
