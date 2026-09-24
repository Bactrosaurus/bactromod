# BactroMod

BactroMod is a client-side Fabric mod for visual clarity and quality-of-life tweaks. It injects all behavior changes through **Mixins**, which means there is no server-side component.

## ✨ Features

| Feature                         | Description                                                                                           |
|---------------------------------|-------------------------------------------------------------------------------------------------------|
| 🌟 **Fullbright**               | Adjustable brightness boost ranging from vanilla lighting to full brightness.                         |
| 👁️ **Night vision cleanup**    | Suppresses the night vision effect.                                                                   |
| 🎃 **Pumpkin blur toggle**      | Disables the carved-pumpkin overlay blur.                                                             |
| 🔥 **Low fire**                 | Configurable first-person fire overlay offset.                                                        |
| 🛡️ **Low shield**              | Configurable first-person shield render offset.                                                       |
| 🗺️ **Boat map visibility**     | Keep filled maps visible while moving in boats.                                                       |
| 🌫️ **Fog controls**            | Toggle lava, powder snow, blindness, darkness, water, and atmospheric fog.                            |
| 📐 **Item scaling**             | Scale individual items in first-person view via a dedicated sub-screen with search.                   |
| 🌊 **Riptide + shield fix**     | Corrects shield rendering position during riptide use.                                                |
| 🎮 **No-OP gamemode switcher**  | Enables the debug gamemode-switch screen without OP-level checks.                                     |

## ⚙️ Configuration

BactroMod settings can be changed in-game or edited directly in the config file.

| Method | Description |
| --- | --- |
| **ModMenu** *(preferred)* | Config entry inside ModMenu (if installed). |
| **Credits screen** *(fallback)* | Injected "BactroMod Settings" button in `CreditsAndAttributionScreen`. |
| **Config file** | `<gameDir>/config/bactromod.json` (JSON, editable by hand) |

## 📦 Installation

1. Install **Fabric Loader** and **Fabric API** for Minecraft.
2. Place the BactroMod `.jar` into your Minecraft `mods/` folder.
3. Launch Minecraft with the Fabric profile.

## 🛠️ Development

```bash
./gradlew build       # produces the mod jar in build/libs/
./gradlew runClient   # launches a dev Minecraft instance under run/
./gradlew test        # runs config and packaged-resource regression tests
./gradlew runClientGameTest # tests fullbright and fog in disposable client worlds
```

Use Java 25 or newer. Unit tests use temporary directories; client tests use
`build/run/clientGameTest/`, which is recreated on each run. Tests are not
included in the released mod jar.

## 📸 Screenshots

![](https://i.imgur.com/CIdyeb7.png)

![](https://i.imgur.com/07NFlrz.png)
