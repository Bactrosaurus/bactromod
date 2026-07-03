package de.daniel.bactromod.mixins.features.lowfire;

import com.mojang.blaze3d.vertex.PoseStack;
import de.daniel.bactromod.config.Config;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class MixinScreenEffectRenderer {

    @Inject(method = "submitFire", at = @At("HEAD"))
    private static void submitFire(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, TextureAtlasSprite sprite, CallbackInfo ci) {
        poseStack.translate(0, Config.load().fireOffset / 100F, 0);
    }

}
