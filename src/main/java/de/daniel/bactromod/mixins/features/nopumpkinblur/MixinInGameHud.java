package de.daniel.bactromod.mixins.features.nopumpkinblur;

import de.daniel.bactromod.config.Config;
import de.daniel.bactromod.config.ConfigData;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public class MixinInGameHud {

    @Redirect(method = "renderMiscOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack getEquippedStack(ClientPlayerEntity instance, EquipmentSlot equipmentSlot) {
        ConfigData config = Config.load();
        if (!equipmentSlot.isArmorSlot() || config.pumpkinBlur()) return instance.getEquippedStack(equipmentSlot);
        ItemStack realItem = instance.getEquippedStack(equipmentSlot);
        if (realItem.isOf(Items.CARVED_PUMPKIN)) {
            return ItemStack.EMPTY;
        } else {
            return realItem;
        }
    }

}