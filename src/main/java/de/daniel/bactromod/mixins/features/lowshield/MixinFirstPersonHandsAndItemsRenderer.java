package de.daniel.bactromod.mixins.features.lowshield;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import de.daniel.bactromod.config.Config;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class MixinFirstPersonHandsAndItemsRenderer {
    @Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"), expect = 2, order = 900)
    private void offsetShield(CallbackInfo ci, @Local(argsOnly = true, name = "itemStack") ItemStack itemStack, @Local(argsOnly = true, name = "poseStack") PoseStack poseStack) {
        if (!(itemStack.getItem() instanceof ShieldItem)) return;
        int offset = Math.clamp(Config.get().shieldOffset, -100, 100);
        if (offset != 0) poseStack.translate(0, offset / 100F, 0);
    }
}
