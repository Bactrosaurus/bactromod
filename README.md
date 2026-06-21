# BactroMod

BactroMod is a client-side Fabric mod for visual clarity and quality-of-life tweaks. It injects all behavior changes through **Mixins**, which means there is no server-side component.
It works out of the box on any compatible Fabric installation.

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

**Main options:**

`gammaMultiplier` · `nightVision` · `pumpkinBlur` · `fireOffset` · `shieldOffset` · `blindnessFog` · `darknessFog` · `lavaFog` · `powderSnowFog` · `waterFog` · `atmosphericFog` · `showMapWhileInBoat` · `fixShieldRiptideTrident` · `ignoreOpGamemodeSwitcher`

**Item scaling** factors (totems, potions, food, shields, etc.) are configured in a separate sub-screen accessible from the main settings screen.

## 📦 Installation

1. Install **Fabric Loader** for the supported Minecraft version.
2. Place the BactroMod `.jar` into your Minecraft `mods/` folder.
3. Ensure **Fabric API** is installed.
4. Launch Minecraft with the Fabric profile.

## 🛠️ Development

```bash
./gradlew build       # produces build/libs/bactromod-<version>.jar
./gradlew runClient   # launches a dev Minecraft instance under run/
```

> **Java 25+ JDK required** on the build PATH (`options.release = 25` in `build.gradle.kts`).

Build output: `build/libs/bactromod-<version>.jar`

## 📸 Screenshots

![](https://i.imgur.com/CIdyeb7.png)

![](https://i.imgur.com/07NFlrz.png)

---

<div align="center">

Licensed under **LGPL-3.0-only** · Source on [Codeberg](https://codeberg.org/Bactrosaurus/bactromod)

</div>
