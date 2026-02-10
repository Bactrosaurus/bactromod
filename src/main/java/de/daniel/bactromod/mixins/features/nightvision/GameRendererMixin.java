package de.daniel.bactromod.mixins.features.nightvision;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.daniel.bactromod.config.Config;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = GameRenderer.class)
public class GameRendererMixin {
    @ModifyReturnValue(method = "getNightVisionStrength", at = @At("RETURN"))
    private static float cleanerNightVision(float original) {
        if (!Config.load().nightVision) return 0F;
        return original;
    }
}