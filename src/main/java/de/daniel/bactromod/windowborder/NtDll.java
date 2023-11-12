package de.daniel.bactromod.windowborder;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

public interface NtDll extends Library {
    NtDll INSTANCE = Native.load("ntdll", NtDll.class);

    void RtlGetNtVersionNumbers(
            IntByReference MajorVersion,
            IntByReference MinorVersion,
            IntByReference BuildNumber
    );

    static void getBuildNumber() {
        final IntByReference majorVersion = new IntByReference();
        final IntByReference buildNumber = new IntByReference();
        INSTANCE.RtlGetNtVersionNumbers(majorVersion, new IntByReference(), buildNumber);

        WindowUtil.majorVersion = majorVersion.getValue();
        WindowUtil.buildNumber = buildNumber.getValue() & ~0xF0000000;
    }
}