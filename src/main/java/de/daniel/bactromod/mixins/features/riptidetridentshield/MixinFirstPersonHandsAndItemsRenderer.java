package de.daniel.bactromod.mixins.features.riptidetridentshield;

import de.daniel.bactromod.config.Config;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.item.Items;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class MixinFirstPersonHandsAndItemsRenderer {
    @Redirect(method = "submitArmWithItem", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;isAutoSpinAttack:Z", opcode = Opcodes.GETFIELD))
    private boolean injected(AvatarRenderState instance) {
        return instance.isAutoSpinAttack && !(instance.leftHandItemStack.is(Items.SHIELD) && Config.get().fixShieldRiptideTrident);
    }
}
