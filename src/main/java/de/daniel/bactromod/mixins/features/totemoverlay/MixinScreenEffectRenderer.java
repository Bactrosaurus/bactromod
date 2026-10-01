package de.daniel.bactromod.mixins.features.totemoverlay;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import de.daniel.bactromod.config.Config;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ScreenEffectRenderer.class)
public class MixinScreenEffectRenderer {
    @WrapOperation(method = "renderItemActivationAnimation", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"), expect = 1)
    private void scaleTotemOverlay(PoseStack instance, float xScale, float yScale, float zScale, Operation<Void> original, @Local(argsOnly = true) PlayerRenderState playerState) {
        PlayerRenderState.ItemActivationRenderState activation = playerState.itemActivation;

        if (activation != null && activation.item.is(Items.TOTEM_OF_UNDYING)) {
            float scale = Mth.clamp(Config.get().totemOverlayPercentSize, 1, 100) / 100F;

            xScale *= scale;
            yScale *= scale;
            zScale *= scale;
        }

        original.call(instance, xScale, yScale, zScale);
    }
}