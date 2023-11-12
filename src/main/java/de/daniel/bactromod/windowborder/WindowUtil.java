package de.daniel.bactromod.windowborder;

public final class WindowUtil {
    public static final int MINIMUM_BUILD_NUM = 22000;
    public static final int BACKDROP_BUILD_NUM = 22621;
    public static int majorVersion = Integer.MIN_VALUE;
    public static int buildNumber = Integer.MIN_VALUE;

    public static boolean checkCompatibility() {
        return (majorVersion >= 10 && buildNumber >= MINIMUM_BUILD_NUM);
    }

}
