package de.daniel.bactromod.mixins.features.noopgmswitcher;

import net.minecraft.client.gui.screen.GameModeSwitcherScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GameModeSwitcherScreen.class)
public class MixinGameModeSwitcherScreen {

    @ModifyConstant(method = "apply(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/GameModeSwitcherScreen$GameModeSelection;)V", constant = @Constant(intValue = 2))
    private static int injectedPermissionLevel(int injectedPermissionLevel) {
        return 0;
    }

}