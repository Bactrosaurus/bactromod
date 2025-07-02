package de.daniel.bactromod.mixins.features.lowshield;

import de.daniel.bactromod.config.Config;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class MixinHeldItemRenderer {

    @Shadow
    @Final
    private ItemRenderer itemRenderer;

    @Inject(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "HEAD"), cancellable = true)
    public void renderItem(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext displayContext, MatrixStack matrixStack, VertexConsumerProvider multiBufferSource, int i, CallbackInfo ci) {
        if (itemStack.isOf(Items.SHIELD) && displayContext.isFirstPerson()) {
            if (!itemStack.isEmpty()) {
                matrixStack.translate(0.0D, Config.INSTANCE.load().getShieldOffset() / 100F, 0.0D);
                this.itemRenderer.renderItem(livingEntity, itemStack, displayContext, matrixStack, multiBufferSource, livingEntity.getWorld(), i, OverlayTexture.DEFAULT_UV, livingEntity.getId() + displayContext.ordinal());
                ci.cancel();
            }
        }
    }

}
