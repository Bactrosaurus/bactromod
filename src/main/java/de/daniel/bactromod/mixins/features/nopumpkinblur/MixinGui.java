package de.daniel.bactromod.mixins.features.nopumpkinblur;

import de.daniel.bactromod.config.Config;
import de.daniel.bactromod.config.ConfigData;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Gui.class)
public class MixinGui {

    @Redirect(method = "renderCameraOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack getItemBySlot(LocalPlayer instance, EquipmentSlot equipmentSlot) {
        ConfigData config = Config.INSTANCE.load();
        if (!equipmentSlot.isArmor() || config.getPumpkinBlur()) return instance.getItemBySlot(equipmentSlot);
        ItemStack realItem = instance.getItemBySlot(equipmentSlot);
        if (realItem.is(Items.CARVED_PUMPKIN)) {
            return ItemStack.EMPTY;
        } else {
            return realItem;
        }
    }

}