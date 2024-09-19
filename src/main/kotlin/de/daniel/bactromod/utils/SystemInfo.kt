package de.daniel.bactromod.utils

object SystemInfo {

    fun isWindows11(): Boolean = System.getProperty("os.name") == "Windows 11"

}