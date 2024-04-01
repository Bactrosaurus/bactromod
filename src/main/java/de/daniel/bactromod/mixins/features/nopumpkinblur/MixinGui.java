package de.daniel.bactromod.mixins.features.nopumpkinblur;

import de.daniel.bactromod.config.Config;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Gui.class)
public class MixinGui {

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    public boolean is(ItemStack itemStack, Item item) {
        if (Config.INSTANCE.load().getPumpkinBlur()) {
            return false;
        } else {
            return itemStack.is(Blocks.CARVED_PUMPKIN.asItem());
        }
    }

}