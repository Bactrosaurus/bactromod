package de.daniel.bactromod.mixins.features.lowfire;

import com.mojang.blaze3d.vertex.PoseStack;
import de.daniel.bactromod.config.Config;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class MixinScreenEffectRenderer {
    
    @Inject(method = "renderFire", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = At.Shift.AFTER))
    private static void renderFire(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, TextureAtlasSprite sprite, CallbackInfo ci) {
        matrixStack.translate(0, Config.load().fireOffset / 100F, 0);
    }

}
