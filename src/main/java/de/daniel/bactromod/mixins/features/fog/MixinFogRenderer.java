package de.daniel.bactromod.mixins.features.fog;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.daniel.bactromod.config.Config;
import de.daniel.bactromod.config.ConfigData;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import net.minecraft.client.renderer.fog.environment.BlindnessFogEnvironment;
import net.minecraft.client.renderer.fog.environment.DarknessFogEnvironment;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.client.renderer.fog.environment.LavaFogEnvironment;
import net.minecraft.client.renderer.fog.environment.PowderedSnowFogEnvironment;
import net.minecraft.client.renderer.fog.environment.WaterFogEnvironment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(value = FogRenderer.class, priority = 1500)
public abstract class MixinFogRenderer {
    @Shadow
    @Final
    private static List<FogEnvironment> FOG_ENVIRONMENTS;

    @ModifyReturnValue(method = "setupFog", at = @At("RETURN"))
    private FogData applyFogToggles(FogData fog, @Local(argsOnly = true, name = "camera") Camera camera, @Local(argsOnly = true, name = "renderDistanceInChunks") int renderDistanceInChunks, @Local(name = "fogType") FogType fogType) {
        Entity entity = camera.entity();
        if (entity == null) return fog;
        for (FogEnvironment environment : FOG_ENVIRONMENTS) {
            if (!environment.isApplicable(fogType, entity)) continue;
            if (bactromod$isFogEnabled(environment)) return fog;

            fog.environmentalStart = Float.MAX_VALUE;
            fog.environmentalEnd = Float.MAX_VALUE;
            fog.renderDistanceStart = Float.MAX_VALUE;
            fog.renderDistanceEnd = Float.MAX_VALUE;

            boolean atmospheric = environment instanceof AtmosphericFogEnvironment;
            fog.skyEnd = atmospheric ? Math.clamp(renderDistanceInChunks * 16F, 2 * 16, 32 * 16) : Float.MAX_VALUE;
            fog.cloudEnd = atmospheric ? Minecraft.getInstance().options.cloudRange().get() * 16 : Float.MAX_VALUE;
            return fog;
        }
        return fog;
    }

    @Unique
    private static boolean bactromod$isFogEnabled(FogEnvironment environment) {
        ConfigData config = Config.get();
        return switch (environment) {
            case LavaFogEnvironment _ -> config.lavaFog;
            case PowderedSnowFogEnvironment _ -> config.powderSnowFog;
            case BlindnessFogEnvironment _ -> config.blindnessFog;
            case DarknessFogEnvironment _ -> config.darknessFog;
            case WaterFogEnvironment _ -> config.waterFog;
            case AtmosphericFogEnvironment _ -> config.atmosphericFog;
            default -> true;
        };
    }
}
