package de.daniel.bactromod.mixins.features.lowfire;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import de.daniel.bactromod.config.Config;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ScreenEffectRenderer.class)
public class MixinScreenEffectRenderer {
    @WrapOperation(method = "submitFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitCustomGeometry(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;Lnet/minecraft/client/renderer/SubmitNodeCollector$CustomGeometryRenderer;)V"), expect = 1)
    private static void offsetFire(SubmitNodeCollector instance, PoseStack poseStack, RenderType renderType, SubmitNodeCollector.CustomGeometryRenderer renderer, Operation<Void> original) {
        poseStack.pushPose();
        try {
            poseStack.translate(0, Mth.clamp(Config.get().fireOffset, -100, 100) / 100F, 0);
            original.call(instance, poseStack, renderType, renderer);
        } finally {
            poseStack.popPose();
        }
    }
}
