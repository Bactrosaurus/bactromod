# BactroMod

BactroMod is a lightweight **client-side Fabric mod** focused on visual clarity and quality-of-life tweaks.
It adds configurable rendering/gameplay behavior changes through Mixins only (no server-side component).

Download on Modrinth: https://modrinth.com/mod/bactromod

## Compatibility

- Environment: client only
- Mod loader: Fabric
- Fabric API: required
- ModMenu: optional (recommended for easier settings access)
- Java: uses the version defined by the project/toolchain (see `gradle.properties` + `build.gradle.kts`)
- Minecraft + loader target: defined in `gradle.properties` and runtime constraints in `src/main/resources/fabric.mod.json`

## Features

- **Brightness boost (fullbright-style):** scales in-game brightness via a configurable multiplier.
- **Night vision cleanup:** can suppress the night vision effect scaling.
- **Pumpkin blur toggle:** disables carved-pumpkin overlay blur.
- **Low fire:** configurable first-person fire overlay offset.
- **Low shield:** configurable first-person shield render offset.
- **Boat map visibility:** keep filled maps visible while moving in boats.
- **Fog controls:** toggle lava, powder snow, blindness, darkness, water, and atmospheric fog behavior.
- **Riptide + shield fix:** corrects shield rendering position during riptide use.
- **No-OP gamemode switcher shortcut:** enables the debug gamemode-switch flow without OP-level checks.
- **Settings button fallback:** injects a BactroMod settings button into the Credits & Attribution screen.

## Configuration

BactroMod settings are generated from config annotations and can be changed in-game.

- **Preferred:** via ModMenu config entry (if ModMenu is installed).
- **Fallback:** via the injected button in `CreditsAndAttributionScreen`.
- **File-based:** stored in `config/bactromod.json`.

Main configurable fields currently include:
`gammaMultiplier`, `nightVision`, `pumpkinBlur`, `fireOffset`, `shieldOffset`,
`blindnessFog`, `darknessFog`, `lavaFog`, `powderSnowFog`, `waterFog`,
`atmosphericFog`, `showMapWhileInBoat`, and `fixShieldRiptideTrident`.

## Installation

1. Install Fabric Loader for the supported Minecraft version.
2. Place the BactroMod `.jar` into your Minecraft `mods/` folder.
3. Ensure Fabric API is installed.
4. Launch Minecraft with the Fabric profile.

## Development

```bash
./gradlew build
./gradlew runClient
```

Build output jar:
- `build/libs/bactromod-<version>.jar`

## Screenshots

![](https://i.imgur.com/CIdyeb7.png)

![](https://i.imgur.com/07NFlrz.png)
