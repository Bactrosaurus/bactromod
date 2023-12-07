package de.daniel.bactromod.windowborder;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.IntByReference;
import de.daniel.bactromod.config.Config;
import de.daniel.bactromod.config.ConfigObject;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.system.NativeType;

public interface DwmApi extends Library {
    DwmApi INSTANCE = Native.load("dwmapi", DwmApi.class);

    int INT_SIZE = 4;
    int DWMWA_USE_IMMERSIVE_DARK_MODE = 20;
    int DWMWA_WINDOW_CORNER_PREFERENCE = 33;
    int DWMWA_BORDER_COLOR = 34;
    int DWMWA_CAPTION_COLOR = 35;
    int DWMWA_TEXT_COLOR = 36;
    int DWMWA_SYSTEMBACKDROP_TYPE = 38;

    enum DWM_SYSTEMBACKDROP_TYPE {
        DWMSBT_AUTO("auto");

        public final String translate;

        DWM_SYSTEMBACKDROP_TYPE(final String translate) {
            this.translate = translate;
        }
    }

    enum DWM_WINDOW_CORNER_PREFERENCE {
        DWMWCP_DEFAULT("default");

        public final String translate;

        DWM_WINDOW_CORNER_PREFERENCE(final String translate) {
            this.translate = translate;
        }
    }

    int DWMWA_COLOR_DEFAULT = 0xFFFFFFFF;

    @NativeType("HRESULT")
    void DwmSetWindowAttribute(
            HWND hwnd,
            int dwAttribute,
            PointerType pvAttribute,
            int cbAttribute
    );

    static void updateDwm(final long window) {
        if (!WindowUtil.checkCompatibility()) {
            return;
        }

        final HWND hwnd = new HWND(Pointer.createConstant(GLFWNativeWin32.glfwGetWin32Window(window)));

        ConfigObject config = Config.INSTANCE.load();

        INSTANCE.DwmSetWindowAttribute(hwnd, DWMWA_USE_IMMERSIVE_DARK_MODE, new IntByReference(
                config.getShowNiceWindowBorders() ? 1 : 0
        ), INT_SIZE);

        if (WindowUtil.buildNumber >= WindowUtil.BACKDROP_BUILD_NUM) {
            INSTANCE.DwmSetWindowAttribute(hwnd, DWMWA_SYSTEMBACKDROP_TYPE, new IntByReference(DWM_SYSTEMBACKDROP_TYPE.DWMSBT_AUTO.ordinal()), INT_SIZE);
        }

        INSTANCE.DwmSetWindowAttribute(hwnd, DWMWA_WINDOW_CORNER_PREFERENCE, new IntByReference(DWM_WINDOW_CORNER_PREFERENCE.DWMWCP_DEFAULT.ordinal()), INT_SIZE);
        INSTANCE.DwmSetWindowAttribute(hwnd, DWMWA_BORDER_COLOR, new IntByReference(DWMWA_COLOR_DEFAULT), INT_SIZE);
        INSTANCE.DwmSetWindowAttribute(hwnd, DWMWA_CAPTION_COLOR, new IntByReference(DWMWA_COLOR_DEFAULT), INT_SIZE);
        INSTANCE.DwmSetWindowAttribute(hwnd, DWMWA_TEXT_COLOR, new IntByReference(DWMWA_COLOR_DEFAULT), INT_SIZE);
    }
}