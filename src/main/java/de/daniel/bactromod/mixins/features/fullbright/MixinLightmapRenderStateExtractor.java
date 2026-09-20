package de.daniel.bactromod.mixins.features.fullbright;

import de.daniel.bactromod.config.Config;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.client.renderer.state.LightmapRenderState;
import net.minecraft.util.Mth;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightmapRenderStateExtractor.class)
public class MixinLightmapRenderStateExtractor {
    @Inject(method = "extract", at = @At("TAIL"), require = 1)
    private void applyBrightnessBoost(LightmapRenderState renderState, float partialTicks, CallbackInfo ci) {
        if (!renderState.needsUpdate) return;

        float strength = (Mth.clamp(Config.get().gammaMultiplier, 1, 15) - 1) / 14.0F;
        strength *= strength * strength;
        if (strength == 0.0F) return;

        renderState.ambientColor = new Vector3f(renderState.ambientColor).lerp(LightmapRenderStateExtractor.WHITE, strength);
    }
}
