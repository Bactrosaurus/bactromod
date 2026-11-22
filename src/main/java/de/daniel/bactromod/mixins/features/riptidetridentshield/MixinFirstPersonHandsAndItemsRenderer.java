package de.daniel.bactromod.mixins.features.riptidetridentshield;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import de.daniel.bactromod.config.Config;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class MixinFirstPersonHandsAndItemsRenderer {
    @WrapOperation(method = "submitArmWithItem", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;isAutoSpinAttack:Z", opcode = Opcodes.GETFIELD))
    private boolean fixShieldTransform(AvatarRenderState instance, Operation<Boolean> original, @Local(argsOnly = true) ItemStack itemStack) {
        return original.call(instance) && !(Config.get().fixShieldRiptideTrident && itemStack.getItem() instanceof ShieldItem);
    }
}
