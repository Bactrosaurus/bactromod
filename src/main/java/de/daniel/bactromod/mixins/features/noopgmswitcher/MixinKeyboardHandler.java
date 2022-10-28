package de.daniel.bactromod.mixins.features.noopgmswitcher;

import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(KeyboardHandler.class)
public class MixinKeyboardHandler {

    @ModifyConstant(method = "handleDebugKeys", constant = @Constant(intValue = 2, ordinal = 2))
    private static int injectedPermissionLevel2(int injectedPermissionLevel) {
        return 0;
    }

    @ModifyConstant(method = "handleDebugKeys", constant = @Constant(intValue = 2, ordinal = 1))
    private static int injectedPermissionLevel1(int injectedPermissionLevel) {
        return 0;
    }

}