package de.daniel.bactromod.mixins.features.boatmap;

import de.daniel.bactromod.config.Config;
import de.daniel.bactromod.config.ConfigObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer {

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F", ordinal = 0))
    public float floatValue(float f, float g, float h) {
        Minecraft mc = Minecraft.getInstance();
        ConfigObject config = Config.INSTANCE.load();
        if (!config.getShowMapWhileInBoat()) return Mth.clamp(f, g, h);
        if (mc.player == null) return Mth.clamp(f, g, h);
        if (mc.player.getItemInHand(InteractionHand.MAIN_HAND).is(Items.FILLED_MAP)) {
            return 1.0F;
        } else return Mth.clamp(f, g, h);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F", ordinal = 1))
    public float floatValue1(float f, float g, float h) {
        Minecraft mc = Minecraft.getInstance();
        ConfigObject config = Config.INSTANCE.load();
        if (!config.getShowMapWhileInBoat()) return Mth.clamp(f, g, h);
        if (mc.player == null) return Mth.clamp(f, g, h);
        if (mc.player.getItemInHand(InteractionHand.OFF_HAND).is(Items.FILLED_MAP)) {
            return 1.0F;
        } else return Mth.clamp(f, g, h);
    }

}