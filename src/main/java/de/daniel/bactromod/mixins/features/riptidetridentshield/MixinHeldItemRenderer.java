package de.daniel.bactromod.mixins.features.riptidetridentshield;

import de.daniel.bactromod.config.Config;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class MixinHeldItemRenderer {

    @Shadow
    public void renderItem(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext, MatrixStack poseStack, VertexConsumerProvider multiBufferSource, int i) {}

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 11), cancellable = true)
    public void shieldTransformAutoSpinAttack(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, Hand hand, float h, ItemStack itemStack, float i, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int j, CallbackInfo ci) {
        if (itemStack.isOf(Items.SHIELD) && Config.load().fixShieldRiptideTrident) {
            boolean bl = hand == Hand.MAIN_HAND;
            Arm humanoidArm = bl ? abstractClientPlayerEntity.getMainArm() : abstractClientPlayerEntity.getMainArm().getOpposite();
            boolean bl2 = humanoidArm == Arm.RIGHT;
            matrixStack.translate(0.0F, 0.0F, 0.0F);
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(0.0F));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(0.0F));
            renderItem(abstractClientPlayerEntity, itemStack, bl2 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, matrixStack, vertexConsumerProvider, j);
            matrixStack.pop();
            ci.cancel();
        }
    }

}
