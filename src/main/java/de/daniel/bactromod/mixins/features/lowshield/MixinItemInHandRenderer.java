package de.daniel.bactromod.mixins.features.lowshield;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.daniel.bactromod.config.Config;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
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

    @Shadow
    public void renderItem(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext, boolean bl, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {}

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

    @Inject(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 11), cancellable = true)
    public void shieldTransformAutoSpinAttack(AbstractClientPlayer abstractClientPlayer, float f, float g, InteractionHand interactionHand, float h, ItemStack itemStack, float i, PoseStack poseStack, MultiBufferSource multiBufferSource, int j, CallbackInfo ci) {
        if (itemStack.is(Items.SHIELD) && Config.INSTANCE.load().getFixShieldRiptideTrident()) {
            boolean bl = interactionHand == InteractionHand.MAIN_HAND;
            HumanoidArm humanoidArm = bl ? abstractClientPlayer.getMainArm() : abstractClientPlayer.getMainArm().getOpposite();
            boolean bl2 = humanoidArm == HumanoidArm.RIGHT;
            poseStack.translate(0.0F, 0.0F, 0.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(0.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(0.0F));
            this.renderItem(abstractClientPlayer, itemStack, bl2 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !bl2, poseStack, multiBufferSource, j);
            poseStack.popPose();
            ci.cancel();
        }
    }

}