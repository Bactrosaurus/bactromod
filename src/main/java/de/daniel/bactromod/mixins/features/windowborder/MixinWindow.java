package de.daniel.bactromod.mixins.features.windowborder;

import com.mojang.blaze3d.platform.DisplayData;
import com.mojang.blaze3d.platform.ScreenManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.platform.WindowEventHandler;
import de.daniel.bactromod.config.Config;
import de.daniel.bactromod.config.ConfigData;
import de.daniel.bactromod.utils.SystemInfo;
import de.daniel.bactromod.windowborder.DwmApi;
import de.daniel.bactromod.windowborder.NtDll;
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
    private long window;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void init(WindowEventHandler handler, ScreenManager manager, DisplayData display, String videoMode, String title, CallbackInfo ci) {
        ConfigData config = Config.INSTANCE.load();
        if (!config.getDarkWindowBorders() || !SystemInfo.INSTANCE.isWindows11()) return;
        NtDll.getBuildNumber();
        DwmApi.updateDwm(this.window);
    }

}