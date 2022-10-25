package de.daniel.bactromod.mixins.features.fullbright;

import de.daniel.bactromod.config.Config;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightTexture.class)
public class MixinLightTexture {

    @Redirect(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
    public float floatValue(Double d) {
        return d.floatValue() * Config.INSTANCE.load().getGammaMultiplier();
    }

    @Inject(method = "getDarknessGamma", at = @At(value = "RETURN"), cancellable = true)
    private void getDarknessGamma(float f, CallbackInfoReturnable<Float> cir) {
        if (Config.INSTANCE.load().getDisableDarkness()) {
            cir.setReturnValue(0.0F);
        }
    }

}