package de.daniel.bactromod.mixins.features.nopumpkinblur;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.daniel.bactromod.config.Config;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public class MixinHud {
    @WrapOperation(method = "extractCameraOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;"), expect = 1)
    private ItemStack getEquippedStack(LocalPlayer instance, EquipmentSlot equipmentSlot, Operation<ItemStack> original) {
        ItemStack realItem = original.call(instance, equipmentSlot);
        return equipmentSlot.isArmor() && !Config.get().pumpkinBlur && realItem.is(Items.CARVED_PUMPKIN) ? ItemStack.EMPTY : realItem;
    }
}
