package de.daniel.bactromod.mixins.features.noopgmswitcher;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screen.GameModeSwitcherScreen;
import net.minecraft.command.permission.PermissionCheck;
import net.minecraft.command.permission.PermissionPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GameModeSwitcherScreen.class)
public class MixinGameModeSwitcherScreen {

    @WrapOperation(method = "apply(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/GameModeSwitcherScreen$GameModeSelection;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/command/permission/PermissionCheck;allows(Lnet/minecraft/command/permission/PermissionPredicate;)Z"))
    private static boolean allows(PermissionCheck instance, PermissionPredicate permissionPredicate, Operation<Boolean> original) {
        return true;
    }

}