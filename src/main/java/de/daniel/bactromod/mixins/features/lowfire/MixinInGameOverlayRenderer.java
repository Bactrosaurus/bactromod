package de.daniel.bactromod.mixins.features.lowfire;

import de.daniel.bactromod.config.Config;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class MixinInGameOverlayRenderer {
    
    @Inject(method = "renderFireOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.AFTER))
    private static void renderFire(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, CallbackInfo ci) {
        matrixStack.translate(0, Config.load().fireOffset / 100F, 0);
    }

}
