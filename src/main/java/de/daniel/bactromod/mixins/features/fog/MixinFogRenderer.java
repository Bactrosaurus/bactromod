package de.daniel.bactromod.mixins.features.fog;

import com.llamalad7.mixinextras.sugar.Local;
import de.daniel.bactromod.config.Config;
import de.daniel.bactromod.config.ConfigData;
import org.joml.Vector4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;

@Mixin(value = FogRenderer.class, priority = 1500)
public abstract class MixinFogRenderer {

    @Shadow
    @Final
    private static List<FogEnvironment> FOG_ENVIRONMENTS;

    @Inject(method = "setupFog", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/fog/FogData;renderDistanceEnd:F", ordinal = 0, shift = At.Shift.AFTER, opcode = Opcodes.PUTFIELD))
    public void postFogSetup(Camera camera, int renderDistance, DeltaTracker renderTickCounter, float viewDistance, ClientLevel clientWorld, CallbackInfoReturnable<Vector4f> cir, @Local(name = "fog") FogData fogData, @Local(name = "fogType") FogType fogType, @Local(name = "entity") Entity entity, @Local(name = "renderDistanceInBlocks") float renderDistanceInBlocks) {
        for (int i = 0; i < FOG_ENVIRONMENTS.size(); ++i) {
            if (FOG_ENVIRONMENTS.get(i).isApplicable(fogType, entity)) {
                ConfigData config = Config.load();

                boolean fogEnabled = switch (i) {
                    case 0 -> config.lavaFog;
                    case 1 -> config.powderSnowFog;
                    case 2 -> config.blindnessFog;
                    case 3 -> config.darknessFog;
                    case 4 -> config.waterFog;
                    case 5 -> config.atmosphericFog;
                    default -> true;
                };

                if (!fogEnabled) {
                    fogData.environmentalStart = Float.MAX_VALUE;
                    fogData.environmentalEnd = Float.MAX_VALUE;
                    fogData.renderDistanceStart = Float.MAX_VALUE;
                    fogData.renderDistanceEnd = Float.MAX_VALUE;

                    // limit sky end to render distance of 32 chunks
                    fogData.skyEnd = i == 5 ? Mth.clamp(renderDistanceInBlocks, 2 * 16, 32 * 16) : Float.MAX_VALUE;
                    fogData.cloudEnd = i == 5 ? Minecraft.getInstance().options.cloudRange().get() * 16 : Float.MAX_VALUE;
                }

                break;
            }
        }
    }

}