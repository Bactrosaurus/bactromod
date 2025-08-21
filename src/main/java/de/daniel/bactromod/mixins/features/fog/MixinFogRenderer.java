package de.daniel.bactromod.mixins.features.fog;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.List;

@Mixin(FogRenderer.class)
public abstract class MixinFogRenderer {

    @Shadow
    @Final
    private static List<FogModifier> FOG_MODIFIERS;

    @WrapOperation(method = "applyFog(Lnet/minecraft/client/render/Camera;IZLnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/fog/FogModifier;applyStartEndModifier(Lnet/minecraft/client/render/fog/FogData;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/world/ClientWorld;FLnet/minecraft/client/render/RenderTickCounter;)V"))
    public void applyFog_applyStartEndModifier(FogModifier fogModifier, FogData fogData, Entity entity, BlockPos blockPos, ClientWorld clientWorld, float viewDistance, RenderTickCounter renderTickCounter, Operation<Void> original) {
        ConfigData config = Config.load();
        viewDistance /= 2;

        if (fogModifier.equals(FOG_MODIFIERS.get(0)) && !config.lavaFog() ||
                fogModifier.equals(FOG_MODIFIERS.get(1)) && !config.powderSnowFog() ||
                fogModifier.equals(FOG_MODIFIERS.get(2)) && !config.blindnessFog() ||
                fogModifier.equals(FOG_MODIFIERS.get(3)) && !config.darknessFog() ||
                fogModifier.equals(FOG_MODIFIERS.get(4)) && !config.waterFog() ||
                fogModifier.equals(FOG_MODIFIERS.get(5)) && !config.dimensionBossFog()) {
            fogData.environmentalStart = Float.MAX_VALUE;
            fogData.environmentalEnd = Float.MAX_VALUE;
            fogData.skyEnd = Float.MAX_VALUE;
            fogData.cloudEnd = Float.MAX_VALUE;
        } else if (fogModifier.equals(FOG_MODIFIERS.get(6)) && !config.atmosphericFog()) {
            fogData.environmentalStart = Float.MAX_VALUE;
            fogData.environmentalEnd = Float.MAX_VALUE;
            fogData.skyEnd = viewDistance;
            fogData.cloudEnd = MinecraftClient.getInstance().options.getCloudRenderDistance().getValue() * 16;
        } else {
            original.call(fogModifier, fogData, entity, blockPos, clientWorld, viewDistance, renderTickCounter);
        }
    }

    @ModifyConstant(method = "applyFog(Lnet/minecraft/client/render/Camera;IZLnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;", constant = @Constant(intValue = 16))
    private int applyFog_modifyH(int value) {
        ConfigData config = Config.load();
        if (!config.renderDistanceFog()) {
            value *= 2;
        }

        return value;
    }

}