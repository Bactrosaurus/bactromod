package de.daniel.bactromod.mixins.features.itemscaling;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import de.daniel.bactromod.config.Config;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class MixinFirstPersonHandsAndItemsRenderer {
    @Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"), expect = 2)
    private void scaleItem(CallbackInfo ci, @Local(argsOnly = true, name = "itemStack") ItemStack itemStack, @Local(argsOnly = true, name = "poseStack") PoseStack poseStack) {
        Integer configuredScale = Config.get().itemScalingFactors.get(itemStack.getItem().getDescriptionId());
        float scale = Math.clamp(configuredScale == null ? 100 : configuredScale, 1, 100) / 100F;
        if (scale != 1) poseStack.scale(scale, scale, scale);
    }
}
