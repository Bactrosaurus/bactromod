package de.daniel.bactromod.mixins.features.fog;

import de.daniel.bactromod.config.Config;
import de.daniel.bactromod.config.ConfigData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.FogModifier;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
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
    private static List<FogModifier> FOG_MODIFIERS;

    @Redirect(method = "applyFog(Lnet/minecraft/client/render/Camera;IZLnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/fog/FogModifier;applyStartEndModifier(Lnet/minecraft/client/render/fog/FogData;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/world/ClientWorld;FLnet/minecraft/client/render/RenderTickCounter;)V"))
    public void applyFog_applyStartEndModifier(FogModifier fogModifier, FogData fogData, Entity entity, BlockPos blockPos, ClientWorld clientWorld, float viewDistance, RenderTickCounter renderTickCounter) {
        ConfigData config = Config.INSTANCE.load();

        if (fogModifier.equals(FOG_MODIFIERS.get(0)) && !config.getLavaFog() ||
                fogModifier.equals(FOG_MODIFIERS.get(1)) && !config.getPowderSnowFog() ||
                fogModifier.equals(FOG_MODIFIERS.get(2)) && !config.getWaterFog() ||
                fogModifier.equals(FOG_MODIFIERS.get(5)) && !config.getThickFog()
        ) {
            fogData.environmentalStart = -8.0F;
            fogData.environmentalEnd = viewDistance;
            fogData.skyEnd = viewDistance;
            fogData.cloudEnd = viewDistance;
        } else if (fogModifier.equals(FOG_MODIFIERS.get(3)) && !config.getBlindnessFog() ||
                fogModifier.equals(FOG_MODIFIERS.get(4)) && !config.getDarknessFog()
        ) {
            fogData.environmentalStart = -8.0F;
            fogData.environmentalEnd = 1_000_000.0F;
            fogData.skyEnd = viewDistance;
            fogData.cloudEnd = viewDistance;
        } else if (fogModifier.equals(FOG_MODIFIERS.get(6)) && !config.getTerrainFog()) {
            fogData.environmentalStart = -8.0F;
            fogData.environmentalEnd = 1_000_000.0F;
            fogData.skyEnd = viewDistance;
            fogData.cloudEnd = MinecraftClient.getInstance().options.getCloudRenderDistance().getValue() * 16;
        } else {
            fogModifier.applyStartEndModifier(fogData, entity, blockPos, clientWorld, viewDistance, renderTickCounter);
        }
    }

    // Set render distance start and end to disable terrain fog
    @ModifyVariable(at = @At("HEAD"), method = "applyFog(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V", ordinal = 2, argsOnly = true)
    private float getRenderDistanceStart(float renderDistanceStart) {
        if (Config.INSTANCE.load().getTerrainFog()) return renderDistanceStart;
        return -8.0F;
    }

    @ModifyVariable(at = @At("HEAD"), method = "applyFog(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V", ordinal = 3, argsOnly = true)
    private float getRenderDistanceEnd(float renderDistanceEnd) {
        if (Config.INSTANCE.load().getTerrainFog()) return renderDistanceEnd;
        return 1_000_000.0F;
    }

}