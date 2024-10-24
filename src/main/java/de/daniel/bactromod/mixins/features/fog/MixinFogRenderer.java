package de.daniel.bactromod.mixins.features.fog;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import de.daniel.bactromod.config.Config;
import de.daniel.bactromod.config.ConfigData;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class MixinFogRenderer {

    @Inject(method = "setupFog", at = @At(value = "RETURN"), cancellable = true)
    private static void setupFog(Camera camera, FogRenderer.FogMode fogMode, Vector4f vector4f, float f, boolean thickFog, float g, CallbackInfoReturnable<FogParameters> cir) {
        FogType fogType = camera.getFluidInCamera();
        Entity entity = camera.getEntity();
        LivingEntity livingEntity = entity instanceof LivingEntity ? (LivingEntity) entity : null;
        if (livingEntity == null) return;

        boolean lavaFog = fogType == FogType.LAVA;
        boolean powderSnowFog = fogType == FogType.POWDER_SNOW;
        boolean blindnessFog = livingEntity.hasEffect(MobEffects.BLINDNESS);
        boolean darknessFog = livingEntity.hasEffect(MobEffects.DARKNESS);
        boolean waterFog = fogType == FogType.WATER;
        boolean skyFog = fogMode == FogRenderer.FogMode.FOG_SKY;
        // boolean terrainFog = !(lavaFog || powderSnowFog || blindnessFog || darknessFog || waterFog || thickFog || skyFog);
        boolean terrainFog = fogMode == FogRenderer.FogMode.FOG_TERRAIN;

        ConfigData config = Config.INSTANCE.load();
        
        boolean disableLavaFog = lavaFog && !config.getLavaFog();
        boolean disablePowderSnowFog = powderSnowFog && !config.getPowderSnowFog();
        boolean disableBlindnessFog = blindnessFog && !config.getBlindnessFog();
        boolean disableDarknessFog = darknessFog && !config.getDarknessFog();
        boolean disableWaterFog = waterFog && !config.getWaterFog();
        boolean disableThickFog = thickFog && !config.getThickFog();
        boolean disableSkyFog = skyFog && !config.getSkyFog();
        boolean disableTerrainFog = terrainFog && !config.getTerrainFog();

        if (
            disableLavaFog ||
            disablePowderSnowFog ||
            disableBlindnessFog ||
            disableDarknessFog ||
            disableWaterFog ||
            disableThickFog ||
            disableSkyFog ||
            disableTerrainFog
        ) {
            cir.setReturnValue(FogParameters.NO_FOG);
        }
    }
}