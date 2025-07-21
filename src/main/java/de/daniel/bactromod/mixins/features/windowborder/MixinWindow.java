package de.daniel.bactromod.mixins.features.windowborder;

import de.daniel.bactromod.config.Config;
import de.daniel.bactromod.utils.SystemInfo;
import de.daniel.bactromod.windowborder.DwmApi;
import de.daniel.bactromod.windowborder.NtDll;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.WindowSettings;
import net.minecraft.client.util.MonitorTracker;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class MixinWindow {

    @Shadow
    @Final
    private long handle;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void init(WindowEventHandler windowEventHandler, MonitorTracker monitorTracker, WindowSettings windowSettings, String string, String string2, CallbackInfo ci) {
        Config.ConfigData config = Config.load();
        if (!config.darkWindowBorders() || !SystemInfo.isWindows11) return;
        NtDll.getBuildNumber();
        DwmApi.updateDwm(this.handle);
    }

}