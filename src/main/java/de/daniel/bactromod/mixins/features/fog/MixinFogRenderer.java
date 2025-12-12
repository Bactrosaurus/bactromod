package de.daniel.bactromod.mixins.features.fog;

import de.daniel.bactromod.config.Config;
import de.daniel.bactromod.config.ConfigData;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.FogModifier;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(value = FogRenderer.class, priority = 1500)
public abstract class MixinFogRenderer {

    @Shadow
    @Final
    private static List<FogModifier> FOG_MODIFIERS;

    @Inject(method = "applyFog(Lnet/minecraft/client/render/Camera;ILnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/fog/FogData;renderDistanceEnd:F", ordinal = 0, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void postFogSetup(Camera camera, int renderDistance, RenderTickCounter renderTickCounter, float viewDistance, ClientWorld clientWorld, CallbackInfoReturnable<Vector4f> cir, float g, Vector4f vector4f, float h, CameraSubmersionType cameraSubmersionType, Entity entity, FogData fogData) {
        for (int i = 0; i < FOG_MODIFIERS.size(); ++i) {
            if (FOG_MODIFIERS.get(i).shouldApply(cameraSubmersionType, entity)) {
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
                    fogData.skyEnd = i == 5 ? MathHelper.clamp(h, 2 * 16, 32 * 16) : Float.MAX_VALUE;
                    fogData.cloudEnd = i == 5 ? MinecraftClient.getInstance().options.getCloudRenderDistance().getValue() * 16 : Float.MAX_VALUE;
                }

                break;
            }
        }
    }

}