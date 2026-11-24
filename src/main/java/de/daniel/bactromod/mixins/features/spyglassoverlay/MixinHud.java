package de.daniel.bactromod.mixins.features.spyglassoverlay;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.daniel.bactromod.config.Config;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public class MixinHud {
    @WrapOperation(method = "extractCameraOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Hud;extractSpyglassOverlay(Lnet/minecraft/client/gui/GuiGraphicsExtractor;F)V"))
    private void hideSpyglassOverlay(Hud instance, GuiGraphicsExtractor graphics, float scopeScale, Operation<Void> original) {
        if (Config.get().spyglassOverlay) {
            original.call(instance, graphics, scopeScale);
        }
    }
}
