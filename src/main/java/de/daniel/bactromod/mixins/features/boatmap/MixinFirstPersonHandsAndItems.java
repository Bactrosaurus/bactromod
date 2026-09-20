package de.daniel.bactromod.mixins.features.boatmap;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.daniel.bactromod.config.Config;
import net.minecraft.client.player.FirstPersonHandsAndItems;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FirstPersonHandsAndItems.class)
public class MixinFirstPersonHandsAndItems {
    @Shadow
    private ItemStack mainHandItem;

    @Shadow
    private ItemStack offHandItem;

    @Shadow
    private float mainHandHeight;

    @Shadow
    private float offHandHeight;

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F", ordinal = 0), expect = 1)
    private float clampMainHandHeight(float value, float min, float max, Operation<Float> original, LocalPlayer player) {
        if (!Config.get().showMapWhileInBoat || !mainHandItem.is(Items.FILLED_MAP)) {
            return original.call(value, min, max);
        }
        float strength = player.getItemSwapScale(1);
        float target = mainHandItem == player.getMainHandItem() ? strength * strength * strength : 0;
        return mainHandHeight + Mth.clamp(target - mainHandHeight, -.4F, .4F);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F", ordinal = 1), expect = 1)
    private float clampOffHandHeight(float value, float min, float max, Operation<Float> original, LocalPlayer player) {
        if (!Config.get().showMapWhileInBoat || !offHandItem.is(Items.FILLED_MAP)) {
            return original.call(value, min, max);
        }
        float target = offHandItem == player.getOffhandItem() ? 1 : 0;
        return offHandHeight + Mth.clamp(target - offHandHeight, -.4F, .4F);
    }
}
