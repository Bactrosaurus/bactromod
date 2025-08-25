package de.daniel.bactromod.mixins.features.fullbright;

import de.daniel.bactromod.config.Config;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightmapTextureManager.class)
public class MixinLightmapTextureManager {

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
    public float floatValue(Double d) {
        return d.floatValue() * Config.load().gammaMultiplier;
    }

}