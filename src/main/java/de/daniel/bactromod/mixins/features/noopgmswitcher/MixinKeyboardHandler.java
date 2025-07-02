package de.daniel.bactromod.mixins.features.noopgmswitcher;

import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Keyboard.class)
public class MixinKeyboardHandler {

    @ModifyConstant(method = "processF3", constant = @Constant(intValue = 2, ordinal = 2))
    private int injectedPermissionLevel2(int injectedPermissionLevel) {
        return 0;
    }

    @ModifyConstant(method = "processF3", constant = @Constant(intValue = 2, ordinal = 1))
    private int injectedPermissionLevel1(int injectedPermissionLevel) {
        return 0;
    }

}