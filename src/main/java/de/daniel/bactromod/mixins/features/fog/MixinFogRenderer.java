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
    private static void setupFog(Camera camera, FogRenderer.FogMode fogMode, float f, boolean thickFog, float g, CallbackInfo ci) {
        FogType fogType = camera.getFluidInCamera();
        Entity entity = camera.getEntity();

        boolean lavaFog = fogType == FogType.LAVA;
        boolean powderSnowFog = fogType == FogType.POWDER_SNOW;
        boolean blindnessFog = entity instanceof LivingEntity && ((LivingEntity) entity).hasEffect(MobEffects.BLINDNESS);
        boolean darknessFog = entity instanceof LivingEntity && ((LivingEntity) entity).hasEffect(MobEffects.DARKNESS);
        boolean waterFog = fogType == FogType.WATER;
        boolean skyFog = fogMode == FogRenderer.FogMode.FOG_SKY;
        boolean terrainFog = !(lavaFog || powderSnowFog || blindnessFog || darknessFog || waterFog || thickFog || skyFog);

        boolean disableLavaFog = lavaFog && Config.INSTANCE.load().getDisableLavaFog();
        boolean disablePowderSnowFog = powderSnowFog && Config.INSTANCE.load().getDisablePowderSnowFog();
        boolean disableDarknessFog = darknessFog && Config.INSTANCE.load().getDisableDarknessFog();
        boolean disableWaterFog = waterFog && Config.INSTANCE.load().getDisableWaterFog();
        boolean disableThickFog = thickFog && Config.INSTANCE.load().getDisableThickFog();
        boolean disableSkyFog = skyFog && Config.INSTANCE.load().getDisableSkyFog();
        boolean disableTerrainFog = terrainFog && Config.INSTANCE.load().getDisableTerrainFog();

        if (
                disableLavaFog ||
                disablePowderSnowFog ||
                disableDarknessFog ||
                disableWaterFog ||
                disableThickFog ||
                disableSkyFog ||
                disableTerrainFog
        ) {
            RenderSystem.setShaderFogStart(-8F);
            RenderSystem.setShaderFogEnd(1e6F);
            RenderSystem.setShaderFogShape(FogShape.CYLINDER);
        }
    }
}
