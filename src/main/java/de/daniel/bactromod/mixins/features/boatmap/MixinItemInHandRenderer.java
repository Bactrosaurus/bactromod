package de.daniel.bactromod.mixins.features.boatmap;

import de.daniel.bactromod.config.Config;
import de.daniel.bactromod.config.ConfigData;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer {

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isHandsBusy()Z"))
    private boolean isHandsBusy(LocalPlayer player) {
        ConfigData config = Config.INSTANCE.load();
        if (!config.getShowMapWhileInBoat()) player.isHandsBusy();
        ItemStack mainHandItem = player.getMainHandItem();
        ItemStack offHandItem = player.getOffhandItem();
        if (mainHandItem.is(Items.FILLED_MAP) || offHandItem.is(Items.FILLED_MAP)) {
            return false;
        }
        return player.isHandsBusy();
    }

}