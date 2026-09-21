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
./gradlew runClientGameTest # tests settings access and fullbright in a disposable world
```

Use Java 25 or newer. Unit tests use temporary directories; client tests use
`build/run/clientGameTest/`, which is recreated on each run. Tests are not
included in the released mod jar.

### Automated checks

The [Build workflow](.github/workflows/build.yml) runs on pushes, pull requests,
and manual dispatch. It builds the mod, runs config/translation/metadata tests,
then launches a Minecraft client with strict Mixin injection counting and
software rendering. The client regression checks paused fullbright changes,
intermediate values, clamping, and restoration of vanilla lighting. It does not
replace visual checks on OpenGL and Vulkan before releases.

To enable it, push the workflow and test files to GitHub. If Actions is disabled,
enable it under **Settings → Actions → General**. View results under
**Actions → Build**; successful runs provide downloadable jars for seven days,
and failed runs retain diagnostic reports/logs for three days. No custom secrets,
deployment service, or paid runner is required.

Standard hosted runners are free for public repositories; private repositories
use the account's included allowance. Keep artifact/cache storage within the
free limits. If a payment method is configured, use an Actions budget with
**Stop usage when budget limit is reached** to prevent paid overages; workflow
YAML cannot enforce account billing settings. See
[GitHub Actions billing](https://docs.github.com/en/billing/concepts/product-billing/github-actions).

## 📸 Screenshots

![](https://i.imgur.com/CIdyeb7.png)

![](https://i.imgur.com/07NFlrz.png)
