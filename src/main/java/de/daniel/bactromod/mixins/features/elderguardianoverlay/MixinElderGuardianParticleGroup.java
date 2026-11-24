package de.daniel.bactromod.mixins.features.elderguardianoverlay;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.daniel.bactromod.config.Config;
import net.minecraft.client.particle.ElderGuardianParticleGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.stream.Stream;

@Mixin(ElderGuardianParticleGroup.class)
public class MixinElderGuardianParticleGroup {
    @WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;toList()Ljava/util/List;"))
    private <T> List<T> hideElderGuardianOverlay(Stream<T> instance, Operation<List<T>> original) {
        return Config.get().elderGuardianOverlay ? original.call(instance) : List.of();
    }
}
