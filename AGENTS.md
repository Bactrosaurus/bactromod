# AGENTS.md - BactroMod Codebase Guide

## Project Overview
BactroMod is a **client-side Fabric mod** for Minecraft (Java **25**). All behavior changes are injected via **Mixins**; there is no server-side code. Versions live in `gradle.properties` (source of truth).

## Build & Run
```bash
./gradlew build      # produces build/libs/bactromod-<version>.jar (+ sources jar)
./gradlew runClient  # launches a dev Minecraft instance under run/
```
**No test suite exists.** `./gradlew build` is the only verification step.

Gradle quirks: `configuration-cache=false` (IntelliJ incompatibility), `parallel=true`, heap cap `-Xmx1G` — all in `gradle.properties`.

## Architecture
```
de.daniel.bactromod (entry point: BactroMod::init)
├── config/            — JSON config via Gson + annotation-driven settings UI
│   ├── ConfigData     — POJO (settings fields + itemScalingFactors Map)
│   ├── Config         — load/save/cached config at <gameDir>/config/bactromod.json
│   ├── ConfigScreen   — builds the main settings screen + item scaling sub-screen
│   ├── ConfigScreenUtils — OptionInstance factories for both annotation fields and Map-based entries
│   └── optiontypes/   — @BooleanOption, @IntegerOption runtime annotations
├── mixins/
│   ├── features/      — one subdirectory per feature, each with a Mixin<TargetClass>
│   └── settingsbutton/ — injects "BactroMod Settings" button into Credits screen
└── impl/
    └── ModMenuIntegration — optional ModMenu config entrypoint (implements ModMenuApi)
```

## Config Screen: Two Different Approaches

- **Annotation-driven fields** (booleans + integers): `ConfigScreen` uses `@BooleanOption`/`@IntegerOption` + reflection to auto-discover ConfigData fields and build widgets. Adding a field + lang keys is enough; no screen code change needed.
- **Map-based item scaling**: `ConfigData.itemScalingFactors` is a `Map<String, Integer>`. Wired manually in `ConfigScreen` + `ConfigScreenUtils.createItemScalingOption()`. Entries are NOT annotation-discovered — you must add them to the `Map.of(...)` default in ConfigData.
- **Item scaling button**: Added as a `Button` widget in the main screen's `init()` override via `addRenderableWidget()`. Not pushed through the options layout.

## Key Conventions

- **Mixin class naming**: `Mixin<ExactVanillaClassName>` in package `features/<featurename>/`.
- **Config access**: In mixins, always read config inline: `Config.load().<field>`. This respects runtime changes. Avoid calling `Config.load()` twice in the same method — cache locally.
- **Lang keys**: `bactromod.options.<fieldName>` (title) and `bactromod.options.<fieldName>.desc` (tooltip). The field name in `ConfigData` **must** match the lang key suffix. Update **all three** lang files: `en_us.json`, `de_de.json`, `ru_ru.json`.
- **All mixins are client-side only** (registered under `"client"` in `bactromod.mixins.json`).
- **`requireAnnotations: true`** in `bactromod.mixins.json` — every mixin class **must** carry an `@Mixin` annotation or the build fails.
- **ModMenu is an `api` dependency** (compile classpath), but optional at runtime (`suggests` in `fabric.mod.json`).
- **`fabric.mod.json` uses Gradle expansion**: `version`, `loader_version`, and `minecraft_version` are injected by `processResources` from `gradle.properties`. Don't edit them directly in the JSON.

## Adding a New Feature (annotation-driven)
1. Add a field in `ConfigData.java` with `@BooleanOption` or `@IntegerOption(intMin=..., intMax=...)`.
2. Add lang keys in all three lang files under `assets/bactromod/lang/`.
3. Create a mixin class at `mixins/features/<featurename>/Mixin<TargetClass>.java`.
4. Register it in `bactromod.mixins.json` under the `"client"` array.
5. If the mixin needs access to private/protected members, add entries to `bactromod.accesswidener`.

Adding a Map-based feature (like item scaling) requires manually wiring the screen in `ConfigScreen` + `ConfigScreenUtils`.

## ConfigData Fields (current)
Boolean: `pumpkinBlur`, `blindnessFog`, `darknessFog`, `lavaFog`, `powderSnowFog`, `waterFog`, `atmosphericFog`, `showMapWhileInBoat`, `fixShieldRiptideTrident`, `nightVision`, `ignoreOpGamemodeSwitcher`
Integer: `gammaMultiplier` (1–15), `fireOffset` (−100–100), `shieldOffset` (−100–100)
Map: `itemScalingFactors` — keys are `Items` registry names in CONSTANT_CASE, values are percentages 1–100.

`Map.of(...)` returns an **immutable map**. If the config screen needs to add/update entries at runtime, wrap in `new HashMap<>(Map.of(...))` in ConfigData.

## Registered Mixins (from `bactromod.mixins.json`)
`features.boatmap`, `features.fog`, `features.fullbright`, `features.itemscaling`, `features.lowfire`, `features.lowshield`, `features.nightvision`, `features.noopgmswitcher` (2 mixins), `features.nopumpkinblur`, `features.riptidetridentshield`, `settingsbutton`.

Config file is at `<gameDir>/config/bactromod.json`. If JSON parsing fails, Config backs up the broken file to `bactromod_old_<epoch>.json` and recreates defaults.
