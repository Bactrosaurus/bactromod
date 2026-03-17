package de.daniel.bactromod.mixins.features.riptidetridentshield;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.daniel.bactromod.config.Config;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer {

    @Shadow
    public void renderItem(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, SubmitNodeCollector orderedRenderCommandQueue, int i)  {}

    @Inject(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 12), cancellable = true)
    public void shieldTransformAutoSpinAttack(AbstractClientPlayer abstractClientPlayerEntity, float f, float g, InteractionHand hand, float h, ItemStack itemStack, float i, PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, int j, CallbackInfo ci) {
        if (itemStack.is(Items.SHIELD) && Config.load().fixShieldRiptideTrident) {
            boolean bl = hand == InteractionHand.MAIN_HAND;
            HumanoidArm humanoidArm = bl ? abstractClientPlayerEntity.getMainArm() : abstractClientPlayerEntity.getMainArm().getOpposite();
            boolean bl2 = humanoidArm == HumanoidArm.RIGHT;
            matrixStack.translate(0.0F, 0.0F, 0.0F);
            matrixStack.mulPose(Axis.XP.rotationDegrees(0.0F));
            matrixStack.mulPose(Axis.ZP.rotationDegrees(0.0F));
            renderItem(abstractClientPlayerEntity, itemStack, bl2 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, matrixStack, orderedRenderCommandQueue, j);
            matrixStack.popPose();
            ci.cancel();
        }
    }

}
