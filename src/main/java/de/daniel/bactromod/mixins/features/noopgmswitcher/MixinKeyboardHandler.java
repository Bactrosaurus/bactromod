package de.daniel.bactromod.mixins.features.noopgmswitcher;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.server.permissions.PermissionSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(KeyboardHandler.class)
public class MixinKeyboardHandler {

    @WrapOperation(method = "handleDebugKeys", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/permissions/PermissionCheck;check(Lnet/minecraft/server/permissions/PermissionSet;)Z", ordinal = 0))
    private boolean injectedPermissionLevel0(PermissionCheck instance, PermissionSet permissionPredicate, Operation<Boolean> original) {
        return true;
    }

    @WrapOperation(method = "handleDebugKeys", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/permissions/PermissionCheck;check(Lnet/minecraft/server/permissions/PermissionSet;)Z", ordinal = 1))
    private boolean injectedPermissionLevel1(PermissionCheck instance, PermissionSet permissionPredicate, Operation<Boolean> original) {
        return true;
    }

}