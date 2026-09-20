package de.daniel.bactromod.mixins.features.fullbright;

import de.daniel.bactromod.config.Config;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.client.renderer.state.LightmapRenderState;
import net.minecraft.util.Mth;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightmapRenderStateExtractor.class)
public class MixinLightmapRenderStateExtractor {
    @Shadow
    private boolean needsUpdate;

    @Unique
    private int bactromod$gammaMultiplier = Integer.MIN_VALUE;

    @Inject(method = "extract", at = @At("HEAD"), expect = 1)
    private void refreshChangedBrightness(LightmapRenderState renderState, float partialTicks, CallbackInfo ci) {
        int gammaMultiplier = Mth.clamp(Config.get().gammaMultiplier, 1, 15);
        if (gammaMultiplier == bactromod$gammaMultiplier) return;

        bactromod$gammaMultiplier = gammaMultiplier;
        needsUpdate = true;
    }

    @Inject(method = "extract", at = @At("TAIL"), require = 1, expect = 1)
    private void applyBrightnessBoost(LightmapRenderState renderState, float partialTicks, CallbackInfo ci) {
        if (!renderState.needsUpdate) return;

        float strength = (bactromod$gammaMultiplier - 1) / 14.0F;
        strength *= strength * strength;
        if (strength == 0.0F) return;

        renderState.ambientColor = new Vector3f(renderState.ambientColor).lerp(LightmapRenderStateExtractor.WHITE, strength);
    }
}
