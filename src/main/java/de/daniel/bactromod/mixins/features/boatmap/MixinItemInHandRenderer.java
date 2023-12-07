package de.daniel.bactromod.mixins.features.boatmap;

import de.daniel.bactromod.config.Config;
import de.daniel.bactromod.config.ConfigObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collections;

@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private float mainHandHeight;

    @Shadow
    private float offHandHeight;

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isHandsBusy()Z", ordinal = 0))
    public boolean isHandsBusy(LocalPlayer instance) {
        ConfigObject config = Config.INSTANCE.load();
        if (!config.getShowMapWhileInBoat()) return instance.isHandsBusy();
        ItemStack mainHandItem = instance.getMainHandItem();
        ItemStack offHandItem = instance.getOffhandItem();
        if (mainHandItem.is(Items.FILLED_MAP) || offHandItem.is(Items.FILLED_MAP)) {
            return false;
        }
        return instance.isHandsBusy();
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F", ordinal = 2))
    public float clamp1(float f, float g, float h) {
        assert minecraft.player != null;
        return getItemHeight(EquipmentSlot.MAINHAND, minecraft.player, f, g, h);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F", ordinal = 3))
    public float clamp2(float f, float g, float h) {
        assert minecraft.player != null;
        return getItemHeight(EquipmentSlot.OFFHAND, minecraft.player, f, g, h);
    }

    @Unique
    public float getItemHeight(EquipmentSlot hand, LocalPlayer player, float f, float g, float h) {
        if (player.isHandsBusy()) {
            ItemStack mainHandItem = player.getMainHandItem();
            ItemStack offHandItem = player.getOffhandItem();
            ItemStack handItem = (hand == EquipmentSlot.MAINHAND) ? mainHandItem : offHandItem;
            float handHeight = (hand == EquipmentSlot.MAINHAND) ? mainHandHeight : offHandHeight;
            if (mainHandItem.is(Items.FILLED_MAP) && offHandItem.is(Items.FILLED_MAP)) {
                return Mth.clamp(f, g, h);
            }
            if (handItem.is(Items.FILLED_MAP)) {
                return Mth.clamp(handHeight - 0.4f, 0.0f, 1.0f) - handHeight;
            }
        }
        return Mth.clamp(f, g, h);
    }

}