package de.daniel.bactromod.mixins.features.noopgmswitcher;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Keyboard;
import net.minecraft.command.permission.PermissionCheck;
import net.minecraft.command.permission.PermissionPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Keyboard.class)
public class MixinKeyboard {

    @WrapOperation(method = "processF3", at = @At(value = "INVOKE", target = "Lnet/minecraft/command/permission/PermissionCheck;allows(Lnet/minecraft/command/permission/PermissionPredicate;)Z", ordinal = 0))
    private boolean injectedPermissionLevel0(PermissionCheck instance, PermissionPredicate permissionPredicate, Operation<Boolean> original) {
        return true;
    }

    @WrapOperation(method = "processF3", at = @At(value = "INVOKE", target = "Lnet/minecraft/command/permission/PermissionCheck;allows(Lnet/minecraft/command/permission/PermissionPredicate;)Z", ordinal = 1))
    private boolean injectedPermissionLevel1(PermissionCheck instance, PermissionPredicate permissionPredicate, Operation<Boolean> original) {
        return true;
    }

}