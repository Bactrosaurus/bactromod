package de.daniel.bactromod.mixins.features.fog;

import de.daniel.bactromod.config.Config;
import de.daniel.bactromod.config.ConfigData;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(FogRenderer.class)
public abstract class MixinFogRenderer {

    @Shadow
    @Final
    private static List<FogEnvironment> FOG_ENVIRONMENTS;

    @Redirect(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/fog/environment/FogEnvironment;setupFog(Lnet/minecraft/client/renderer/fog/FogData;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/client/multiplayer/ClientLevel;FLnet/minecraft/client/DeltaTracker;)V"))
    private void setupFog(FogEnvironment fogEnvironment, FogData fogData, Entity cameraEntity, BlockPos cameraPos, ClientLevel world, float viewDistance, DeltaTracker tickCounter) {
        ConfigData config = Config.INSTANCE.load();

        if (fogEnvironment.equals(FOG_ENVIRONMENTS.get(0)) && !config.getLavaFog() ||
                fogEnvironment.equals(FOG_ENVIRONMENTS.get(1)) && !config.getPowderSnowFog() ||
                fogEnvironment.equals(FOG_ENVIRONMENTS.get(2)) && !config.getWaterFog() ||
                fogEnvironment.equals(FOG_ENVIRONMENTS.get(5)) && !config.getThickFog()
        ) {
            fogData.environmentalStart = -8.0F;
            fogData.environmentalEnd = viewDistance;
            fogData.skyEnd = viewDistance;
            fogData.cloudEnd = viewDistance;
        } else if (fogEnvironment.equals(FOG_ENVIRONMENTS.get(3)) && !config.getBlindnessFog() ||
                fogEnvironment.equals(FOG_ENVIRONMENTS.get(4)) && !config.getDarknessFog()
        ) {
            fogData.environmentalStart = -8.0F;
            fogData.environmentalEnd = 1_000_000.0F;
            fogData.skyEnd = viewDistance;
            fogData.cloudEnd = viewDistance;
        } else if (fogEnvironment.equals(FOG_ENVIRONMENTS.get(6)) && !config.getTerrainFog()) {
            fogData.environmentalStart = -8.0F;
            fogData.environmentalEnd = 1_000_000.0F;
            fogData.skyEnd = viewDistance;
            fogData.cloudEnd = Minecraft.getInstance().options.cloudRange().get() * 16;
        } else {
            fogEnvironment.setupFog(fogData, cameraEntity, cameraPos, world, viewDistance, tickCounter);
        }
    }

    // Set render distance start and end to disable terrain fog
    @ModifyVariable(at = @At("HEAD"), method = "updateBuffer(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V", ordinal = 2, argsOnly = true)
    private float getRenderDistanceStart(float renderDistanceStart) {
        if (Config.INSTANCE.load().getTerrainFog()) return renderDistanceStart;
        return -8.0F;
    }

    @ModifyVariable(at = @At("HEAD"), method = "updateBuffer(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V", ordinal = 3, argsOnly = true)
    private float getRenderDistanceEnd(float renderDistanceEnd) {
        if (Config.INSTANCE.load().getTerrainFog()) return renderDistanceEnd;
        return 1_000_000.0F;
    }

}