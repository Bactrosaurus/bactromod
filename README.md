# BactroMod

BactroMod is a client-side Fabric mod for visual clarity and quality-of-life tweaks. It injects all behavior changes through **Mixins**, which means there is no server-side component.
It supports Minecraft 26.2 with Fabric Loader 0.19.3 and Fabric API 0.152.1+26.2.

## ✨ Features

| Feature                         | Description                                                                                           |
|---------------------------------|-------------------------------------------------------------------------------------------------------|
| 🌟 **Fullbright**               | Scales in-game brightness via a configurable multiplier.                                              |
| 👁️ **Night vision cleanup**    | Suppresses the night vision effect.                                                                   |
| 🎃 **Pumpkin blur toggle**      | Disables the carved-pumpkin overlay blur.                                                             |
| 🔥 **Low fire**                 | Configurable first-person fire overlay offset.                                                        |
| 🛡️ **Low shield**              | Configurable first-person shield render offset.                                                       |
| 🗺️ **Boat map visibility**     | Keep filled maps visible while moving in boats.                                                       |
| 🌫️ **Fog controls**            | Toggle lava, powder snow, blindness, darkness, water, and atmospheric fog.                            |
| 📐 **Item scaling**             | Scale individual items (totems, potions, food, etc.) in first-person view via a dedicated sub-screen. |
| 🌊 **Riptide + shield fix**     | Corrects shield rendering position during riptide use.                                                |
| 🎮 **No-OP gamemode switcher**  | Enables the debug gamemode-switch screen without OP-level checks.                                     |

## ⚙️ Configuration

BactroMod settings are generated from config annotations and can be changed in-game.

| Method | Description |
| --- | --- |
| **ModMenu** *(preferred)* | Config entry inside ModMenu (if installed). |
| **Credits screen** *(fallback)* | Injected "BactroMod Settings" button in `CreditsAndAttributionScreen`. |
| **Config file** | `<gameDir>/config/bactromod.json` — JSON, editable by hand. |

## 📦 Installation

1. Install **Fabric Loader 0.19.3** for **Minecraft 26.2**.
2. Place the BactroMod `.jar` into your Minecraft `mods/` folder.
3. Ensure **Fabric API 0.152.1+26.2** is installed.
4. Launch Minecraft with the Fabric profile.

## 🛠️ Development

```bash
./gradlew build       # produces the mod and sources jars in build/libs/
./gradlew runClient   # launches a dev Minecraft instance under run/
```

> **Java 25+ JDK required** on the build PATH (`options.release = 25` in `build.gradle.kts`).

Build output: `build/libs/bactromod-<version>.jar` and `build/libs/bactromod-<version>-sources.jar`

## 📸 Screenshots

![](https://i.imgur.com/CIdyeb7.png)

![](https://i.imgur.com/07NFlrz.png)
