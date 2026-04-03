# AGENTS.md - BactroMod Codebase Guide

## Project Overview
BactroMod is a **client-side Fabric mod** for Minecraft **26.1.1** (Java **25**).
All behavior changes are injected via **Mixins**; there is no server-side code.

Current Gradle properties:
- `mod_version=3.7`
- `archives_base_name=bactromod`
- `minecraft_version=26.1.1`
- `loader_version=0.18.6`
- `fabric_api_version=0.145.3+26.1.1`
- `modmenu_version=18.0.0-alpha.8`

## Build & Run
```bash
./gradlew build      # produces build/libs/bactromod-3.7.jar (+ sources jar)
./gradlew runClient  # launches a dev Minecraft instance under run/
```

The project uses the Gradle wrapper. Current Gradle heap cap is `-Xmx1G` (`gradle.properties`).

## Architecture

```
BactroMod (entry point: BactroMod::init)
|- config/  - JSON config via Gson + annotation-driven settings UI
|  |- ConfigData - annotated settings fields
|  |- Config - load/save/cached config at <gameDir>/config/bactromod.json
|  `- ConfigScreenHelper - reflects ConfigData fields into OptionInstance widgets
|- mixins/
|  |- features/ - feature mixins (listed below)
|  `- settingsbutton/ - injects a BactroMod settings button in Credits screen
`- impl/
   `- ModMenuIntegration - optional ModMenu config entrypoint
```

## Implemented Feature Mixins (Registered)

From `src/main/resources/bactromod.mixins.json`:

- `features.boatmap.MixinItemInHandRenderer`
- `features.fog.MixinFogRenderer`
- `features.fullbright.MixinLightTexture`
- `features.lowfire.MixinScreenEffectRenderer`
- `features.lowshield.MixinItemInHandRenderer`
- `features.nightvision.MixinGameRenderer`
- `features.noopgmswitcher.MixinGameModeSwitcherScreen`
- `features.noopgmswitcher.MixinKeyboardHandler`
- `features.nopumpkinblur.MixinGui`
- `features.riptidetridentshield.MixinItemInHandRenderer`
- `settingsbutton.MixinCreditsAndAttributionScreen`

## Config System - How It Works

`ConfigScreenHelper` iterates `ConfigData.class.getDeclaredFields()` at runtime and builds options from `@BooleanOption` and `@IntegerOption`.

`Config.load()` returns a cached `volatile` `ConfigData` instance.

`Config.save()` writes to `config/bactromod.json` and updates the in-memory cache.

If JSON parsing fails, `Config` backs up the broken file to `bactromod_old_<epoch>.json`, logs a warning, and recreates defaults.

### Current ConfigData fields

- `gammaMultiplier`
- `nightVision`
- `pumpkinBlur`
- `fireOffset`
- `shieldOffset`
- `blindnessFog`
- `darknessFog`
- `lavaFog`
- `powderSnowFog`
- `waterFog`
- `atmosphericFog`
- `showMapWhileInBoat`
- `fixShieldRiptideTrident`

## Adding a New Feature - Checklist

1. Add a field in `src/main/java/de/daniel/bactromod/config/ConfigData.java` with `@BooleanOption` or `@IntegerOption(intMin=..., intMax=...)`.
2. Add lang keys in all three files under `src/main/resources/assets/bactromod/lang/`:
   ```json
   "bactromod.options.myFeature": "Display Name",
   "bactromod.options.myFeature.desc": "Tooltip text."
   ```
3. Add a mixin class under `src/main/java/de/daniel/bactromod/mixins/features/<featurename>/Mixin<TargetClass>.java`.
4. Register it in `src/main/resources/bactromod.mixins.json` under `client`.
5. If required, add access widener entries in `src/main/resources/bactromod.accesswidener`.

## Key Files at a Glance

### Core Java Classes

| File | Purpose | Lines |
|---|---|---|
| `src/main/java/de/daniel/bactromod/BactroMod.java` | Main entry point; initializes the mod via `BactroMod::init` entrypoint. Contains a static logger for mod events. | 15 |
| `src/main/java/de/daniel/bactromod/config/Config.java` | Config I/O manager; uses Gson for JSON serialization. Handles load/save operations at `<gameDir>/config/bactromod.json`. Backs up broken config files to `bactromod_old_<epoch>.json`. | 62 |
| `src/main/java/de/daniel/bactromod/config/ConfigData.java` | POJO holding all user-facing settings (13 boolean + integer fields). Fields use `@BooleanOption` or `@IntegerOption(intMin=..., intMax=...)` annotations. | 54 |
| `src/main/java/de/daniel/bactromod/config/ConfigScreenHelper.java` | Reflection-based settings UI builder. Iterates `ConfigData.getDeclaredFields()` to dynamically generate `OptionInstance` widgets for the settings screen. | 96 |
| `src/main/java/de/daniel/bactromod/config/optiontypes/BooleanOption.java` | Runtime annotation marker for boolean config fields. | 9 |
| `src/main/java/de/daniel/bactromod/config/optiontypes/IntegerOption.java` | Runtime annotation marker for integer config fields with `intMin()` and `intMax()` bounds. | 11 |
| `src/main/java/de/daniel/bactromod/impl/ModMenuIntegration.java` | Implements Mod Menu API `ModMenuApi` interface. Returns `ConfigScreenHelper::getConfigScreen` as the mod config factory. | 15 |

### Resource Files

| File | Purpose |
|---|---|
| `src/main/resources/fabric.mod.json` | Mod metadata: id, version, entrypoints (`BactroMod::init`, `ModMenuIntegration`), mixin registration, accessWidener file. Declares dependencies on Fabric Loader, Minecraft 26.1.1+, Java 25+. |
| `src/main/resources/bactromod.mixins.json` | Mixin package configuration: `de.daniel.bactromod.mixins`, Java 25 compatibility, client-side only. Registers 11 mixins (see below). |
| `src/main/resources/bactromod.accesswidener` | Fabric access widener file. Makes `OptionInstance.ValueSet` class and `OptionInstance` class accessible/extendable. |
| `src/main/resources/assets/bactromod/lang/en_us.json` | English translations for all config options (titles + descriptions) and Mod Menu summaries. |
| `src/main/resources/assets/bactromod/lang/de_de.json` | German translations. |
| `src/main/resources/assets/bactromod/lang/ru_ru.json` | Russian translations. |
| `src/main/resources/assets/bactromod/icon.png` | Mod icon (displayed in Mod Menu and mod list). |

### Feature Mixins (Feature-Specific)

All mixins are registered in `bactromod.mixins.json` under the `client` array.

#### Boat Map Feature
| File | Target | Purpose |
|---|---|---|
| `src/main/java/de/daniel/bactromod/mixins/features/boatmap/MixinItemInHandRenderer.java` | `ItemInHandRenderer` | Shows map in off-hand while riding a boat (checks `showMapWhileInBoat` config). |

#### Fog Features
| File | Target | Purpose |
|---|---|---|
| `src/main/java/de/daniel/bactromod/mixins/features/fog/MixinFogRenderer.java` | `FogRenderer` | Disables environment fogs (lava, powder snow, blindness, darkness, water, atmospheric) based on config flags. Highest priority (1500) to override other mixins. |

#### Fullbright Feature
| File | Target | Purpose |
|---|---|---|
| `src/main/java/de/daniel/bactromod/mixins/features/fullbright/MixinLightTexture.java` | `Lightmap` | Multiplies gamma by `gammaMultiplier` config field (1–15). |

#### Low Fire Feature
| File | Target | Purpose |
|---|---|---|
| `src/main/java/de/daniel/bactromod/mixins/features/lowfire/MixinScreenEffectRenderer.java` | `ScreenEffectRenderer` | Adjusts fire overlay vertical position by `fireOffset` pixels. |

#### Low Shield Feature
| File | Target | Purpose |
|---|---|---|
| `src/main/java/de/daniel/bactromod/mixins/features/lowshield/MixinItemInHandRenderer.java` | `ItemInHandRenderer` | Adjusts shield vertical position by `shieldOffset` pixels. |

#### Night Vision Feature
| File | Target | Purpose |
|---|---|---|
| `src/main/java/de/daniel/bactromod/mixins/features/nightvision/MixinGameRenderer.java` | `GameRenderer` | Disables night vision visual effect (`nightVision` config). |

#### No OP Gamemode Switcher Feature
| File | Target | Purpose |
|---|---|---|
| `src/main/java/de/daniel/bactromod/mixins/features/noopgmswitcher/MixinGameModeSwitcherScreen.java` | `GameModeSwitcherScreen` | Prevents gamemode switcher screen from opening on OP-only press. |
| `src/main/java/de/daniel/bactromod/mixins/features/noopgmswitcher/MixinKeyboardHandler.java` | `KeyboardHandler` | Cancels gamemode switcher keybind handling. |

#### No Pumpkin Blur Feature
| File | Target | Purpose |
|---|---|---|
| `src/main/java/de/daniel/bactromod/mixins/features/nopumpkinblur/MixinGui.java` | `Gui` | Disables black pumpkin head overlay (`pumpkinBlur` config). |

#### Riptide Trident Shield Feature
| File | Target | Purpose |
|---|---|---|
| `src/main/java/de/daniel/bactromod/mixins/features/riptidetridentshield/MixinItemInHandRenderer.java` | `ItemInHandRenderer` | Fixes off-hand shield rendering while using riptide trident (`fixShieldRiptideTrident` config). |

### Settings Button Feature
| File | Target | Purpose |
|---|---|---|
| `src/main/java/de/daniel/bactromod/mixins/settingsbutton/MixinCreditsAndAttributionScreen.java` | `CreditsAndAttributionScreen` | Injects "BactroMod Settings" button into the Credits screen (fallback UI for when Mod Menu is unavailable). |

### Build Configuration

| File | Purpose |
|---|---|
| `build.gradle.kts` | Gradle build config. Applies Fabric Loom plugin. Defines dependencies (Minecraft, Fabric Loader, Fabric API, ModMenu). Configures access widener path. Processes `fabric.mod.json` to replace version/loader/minecraft properties. Targets Java 25. |
| `gradle.properties` | Gradle properties and version constants. `mod_version=3.7`, `minecraft_version=26.1.1`, `loader_version=0.18.6`, heap cap `-Xmx1G`. |
| `settings.gradle.kts` | Project name configuration. |

### Project Root Files

| File | Purpose |
|---|---|
| `README.md` | User-facing project documentation. |
| `.gitignore` | Git ignore patterns. |
| `gradlew` / `gradlew.bat` | Gradle wrapper scripts (Unix / Windows). |

### Runtime Output Directories

- `build/` — Compiled classes, JAR artifacts, resources
- `run/` — Test Minecraft instance files (config, saves, mods, logs)

## Conventions
- Mixin class naming is `Mixin<ExactVanillaClassName>`.
- Read config inline in mixins via `Config.load().<field>`.
- Keep `ConfigData` field names aligned with lang keys `bactromod.options.<field>` and `.desc`.
- ModMenu support is optional (`suggests` in `fabric.mod.json`), and a settings button is injected into `CreditsAndAttributionScreen` as fallback.
- Lang files are JSON objects mapping keys to localized strings. Add keys for each new config option.
- All mixins are client-side only; there is no server-side code.
- All feature mixins read config at injection time via `Config.load()` to respect runtime changes.

