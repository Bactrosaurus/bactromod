package de.daniel.bactromod.mixins.features.boatmap;

import de.daniel.bactromod.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer {
    @Final
    @Shadow
    private Minecraft minecraft;

    @Shadow
    private ItemStack mainHandItem;

    @Shadow
    private ItemStack offHandItem;

    @Shadow
    private float mainHandHeight;

    @Shadow
    private float offHandHeight;

    @Unique
    private boolean isOptionDisabled() {
        return !Config.INSTANCE.load().getShowMapWhileInBoat();
    }

    @Unique
    private LocalPlayer getLocalPlayer() {
        return this.minecraft.player;
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F", ordinal = 0))
    private float clampMainHandHeight(float f, float g, float h) {
        assert getLocalPlayer() != null;
        ItemStack itemStack = getLocalPlayer().getMainHandItem();
        boolean mapInMainHand = this.mainHandItem.is(Items.FILLED_MAP);
        if (isOptionDisabled() || !mapInMainHand) return Mth.clamp(f, g, h);
        float i = getLocalPlayer().getAttackStrengthScale(1.0F);
        return this.mainHandHeight +
                Mth.clamp((this.mainHandItem == itemStack ? i * i * i : 0.0F) - this.mainHandHeight, -0.4F, 0.4F);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F", ordinal = 1))
    private float clampOffHandHeight(float f, float g, float h) {
        assert getLocalPlayer() != null;
        ItemStack itemStack = getLocalPlayer().getOffhandItem();
        boolean mapInOffhand = this.offHandItem.is(Items.FILLED_MAP);
        if (isOptionDisabled() || !mapInOffhand) return Mth.clamp(f, g, h);
        return this.offHandHeight +
                Mth.clamp((float) (this.offHandItem == itemStack ? 1 : 0) - this.offHandHeight, -0.4F, 0.4F);
    }

}