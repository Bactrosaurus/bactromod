package de.daniel.bactromod.mixins.features.lowshield;

import com.mojang.blaze3d.vertex.PoseStack;
import de.daniel.bactromod.config.Config;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer {

    @Shadow
    @Final
    private ItemRenderer itemRenderer;

    @Inject(method = "renderItem", at = @At(value = "HEAD"), cancellable = true)
    public void renderItem(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext displayContext, boolean bl, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (itemStack.is(Items.SHIELD) && displayContext.firstPerson()) {
            if (!itemStack.isEmpty()) {
                poseStack.translate(0.0D, Config.INSTANCE.load().getShieldOffset() / 100F, 0.0D);
                this.itemRenderer.renderStatic(livingEntity, itemStack, displayContext, bl, poseStack, multiBufferSource, livingEntity.level(), i, OverlayTexture.NO_OVERLAY, livingEntity.getId() + displayContext.ordinal());
                ci.cancel();
            }
        }
    }

}
