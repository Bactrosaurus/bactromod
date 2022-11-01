package de.daniel.bactromod.mixins.features.fog;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import de.daniel.bactromod.config.Config;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public class MixinFogRenderer {

    @Inject(method = "setupFog", at = @At(value = "RETURN"))
    private static void setupFog(Camera camera, FogRenderer.FogMode fogMode, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
        FogType fogType = camera.getFluidInCamera();
        Entity entity = camera.getEntity();

        boolean lavaFog = fogType == FogType.LAVA;
        boolean powderSnowFog = fogType == FogType.POWDER_SNOW;
        boolean blindnessFog = entity instanceof LivingEntity && ((LivingEntity) entity).hasEffect(MobEffects.BLINDNESS);
        boolean darknessFog = entity instanceof LivingEntity && ((LivingEntity) entity).hasEffect(MobEffects.DARKNESS);
        boolean waterFog = fogType == FogType.WATER;
        boolean skyFog = fogMode == FogRenderer.FogMode.FOG_SKY;
        boolean terrainFog = !(lavaFog || powderSnowFog || blindnessFog || darknessFog || waterFog || thickFog || skyFog);

        if (lavaFog && Config.INSTANCE.load().getDisableLavaFog()) renderSystemDisableFog();
        if (powderSnowFog && Config.INSTANCE.load().getDisablePowderSnowFog()) renderSystemDisableFog();
        if (blindnessFog && Config.INSTANCE.load().getDisableBlindnessFog()) renderSystemDisableFog();
        if (darknessFog && Config.INSTANCE.load().getDisableDarknessFog()) renderSystemDisableFog();
        if (waterFog && Config.INSTANCE.load().getDisableWaterFog()) renderSystemDisableFog();
        if (thickFog && Config.INSTANCE.load().getDisableThickFog()) renderSystemDisableFog();
        if (skyFog && Config.INSTANCE.load().getDisableSkyFog()) renderSystemDisableFog();
        if (terrainFog && Config.INSTANCE.load().getDisableTerrainFog()) renderSystemDisableFog();
    }

    private static void renderSystemDisableFog() {
        RenderSystem.setShaderFogStart(-8.0F);
        RenderSystem.setShaderFogEnd(1e6F);
        RenderSystem.setShaderFogShape(FogShape.CYLINDER);
    }

}
