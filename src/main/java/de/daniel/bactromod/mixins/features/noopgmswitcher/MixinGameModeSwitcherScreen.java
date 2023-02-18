package de.daniel.bactromod.mixins.features.noopgmswitcher;

import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GameModeSwitcherScreen.class)
public class MixinGameModeSwitcherScreen {

    @ModifyConstant(method = "switchToHoveredGameMode(Lnet/minecraft/client/Minecraft;Ljava/util/Optional;)V", constant = @Constant(intValue = 2))
    private static int injectedPermissionLevel(int injectedPermissionLevel) {
        return 0;
    }

}