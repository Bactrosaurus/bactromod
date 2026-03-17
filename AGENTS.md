# AGENTS.md - BactroMod Codebase Guide

## Project Overview
BactroMod is a **client-side Fabric mod** for Minecraft **26.1-pre-3** (Java **25**).
All behavior changes are injected via **Mixins**; there is no server-side code.

Current Gradle properties:
- `mod_version=3.6`
- `archives_base_name=bactromod`
- `minecraft_version=26.1-pre-3`
- `loader_version=0.18.4`
- `fabric_api_version=0.143.14+26.1`
- `modmenu_version=18.0.0-alpha.6`

## Build & Run
```bash
./gradlew build      # produces build/libs/bactromod-3.6.jar (+ sources jar)
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

| File | Purpose |
|---|---|
| `src/main/java/de/daniel/bactromod/config/ConfigData.java` | All user-facing settings |
| `src/main/java/de/daniel/bactromod/config/ConfigScreenHelper.java` | Reflection-based settings UI builder |
| `src/main/resources/bactromod.mixins.json` | Client mixin registration list |
| `src/main/resources/bactromod.accesswidener` | Access grants for Minecraft internals |
| `src/main/resources/assets/bactromod/lang/en_us.json` | Canonical English translations |
| `src/main/resources/fabric.mod.json` | Entrypoints, dependencies, metadata |

## Conventions
- Mixin class naming is `Mixin<ExactVanillaClassName>`.
- Read config inline in mixins via `Config.load().<field>`.
- Keep `ConfigData` field names aligned with lang keys `bactromod.options.<field>` and `.desc`.
- ModMenu support is optional (`suggests` in `fabric.mod.json`), and a settings button is injected into `CreditsAndAttributionScreen` as fallback.

