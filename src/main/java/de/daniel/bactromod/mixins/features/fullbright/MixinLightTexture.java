package de.daniel.bactromod.mixins.features.fullbright;

import de.daniel.bactromod.config.Config;
import net.minecraft.client.renderer.Lightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Lightmap.class)
public class MixinLightTexture {

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/buffers/Std140Builder;putFloat(F)Lcom/mojang/blaze3d/buffers/Std140Builder;", ordinal = 5))
    public float modifyGammaMultiplier(float original) {
        return original * Config.load().gammaMultiplier;
    }

}