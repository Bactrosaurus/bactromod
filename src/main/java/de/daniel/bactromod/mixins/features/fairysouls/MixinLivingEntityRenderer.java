package de.daniel.bactromod.mixins.features.fairysouls;

import de.daniel.bactromod.config.Config;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(ArmorStandRenderer.class)
public abstract class MixinLivingEntityRenderer {

    @Unique
    private static boolean isEntityFairySoulArmorStand(LivingEntity livingEntity) {
        if (livingEntity.getType() == EntityType.ARMOR_STAND) {
            if (livingEntity.getItemBySlot(EquipmentSlot.HEAD).is(Items.PLAYER_HEAD)) {
                ItemStack head = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
                CompoundTag tag = head.getTag();
                if (tag == null) return false;
                int[] skullOwner = tag.getCompound("SkullOwner").getIntArray("Id");
                int[] fairySoulSkullOwner = new int[]{1470417116, -1685177023, -2133154296, -1877319353};
                return Arrays.equals(skullOwner, fairySoulSkullOwner);
            }
        }
        return false;
    }

    @Shadow
    public abstract ResourceLocation getTextureLocation(ArmorStand armorStand);

    @Inject(method = "getRenderType(Lnet/minecraft/world/entity/decoration/ArmorStand;ZZZ)Lnet/minecraft/client/renderer/RenderType;", at = @At(value = "HEAD"), cancellable = true)
    protected void getRenderType(ArmorStand armorStand, boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<RenderType> cir) {
        if (isEntityFairySoulArmorStand(armorStand) && Config.INSTANCE.load().getShowHypixelFairySouls()) {
            ResourceLocation resourceLocation = this.getTextureLocation(armorStand);
            cir.setReturnValue(RenderType.outline(resourceLocation));
        }
    }

}