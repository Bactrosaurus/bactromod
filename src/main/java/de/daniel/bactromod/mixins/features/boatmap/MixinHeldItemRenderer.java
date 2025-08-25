package de.daniel.bactromod.mixins.features.boatmap;

import de.daniel.bactromod.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HeldItemRenderer.class)
public class MixinHeldItemRenderer {
    @Final
    @Shadow
    private MinecraftClient client;

    @Shadow
    private ItemStack mainHand;

    @Shadow
    private ItemStack offHand;

    @Shadow
    private float equipProgressMainHand;

    @Shadow
    private float equipProgressOffHand;

    @Unique
    private boolean isOptionDisabled() {
        return !Config.load().showMapWhileInBoat;
    }

    @Unique
    private ClientPlayerEntity getLocalPlayer() {
        return this.client.player;
    }

    @Redirect(method = "updateHeldItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F", ordinal = 0))
    private float clampMainHandHeight(float f, float g, float h) {
        assert getLocalPlayer() != null;
        ItemStack itemStack = getLocalPlayer().getMainHandStack();
        boolean mapInMainHand = this.mainHand.isOf(Items.FILLED_MAP);
        if (isOptionDisabled() || !mapInMainHand) return MathHelper.clamp(f, g, h);
        float i = getLocalPlayer().getAttackCooldownProgress(1.0F);
        return this.equipProgressMainHand +
                MathHelper.clamp((this.mainHand == itemStack ? i * i * i : 0.0F) - this.equipProgressMainHand, -0.4F, 0.4F);
    }

    @Redirect(method = "updateHeldItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F", ordinal = 1))
    private float clampOffHandHeight(float f, float g, float h) {
        assert getLocalPlayer() != null;
        ItemStack itemStack = getLocalPlayer().getMainHandStack();
        boolean mapInOffhand = this.mainHand.isOf(Items.FILLED_MAP);
        if (isOptionDisabled() || !mapInOffhand) return MathHelper.clamp(f, g, h);
        return this.equipProgressOffHand +
                MathHelper.clamp((float) (this.offHand == itemStack ? 1 : 0) - this.equipProgressOffHand, -0.4F, 0.4F);
    }

}
