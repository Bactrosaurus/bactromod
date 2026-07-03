# AGENTS.md - BactroMod Codebase Guide

## Project Overview
BactroMod is a **client-side Fabric mod** for Minecraft (Java **25**). All behavior changes are injected via **Mixins**; there is no server-side code. Versions live in `gradle.properties` (source of truth).

## Build & Run
```bash
./gradlew build      # produces build/libs/bactromod-<version>.jar (+ sources jar)
./gradlew runClient  # launches a dev Minecraft instance under run/
```
**No test suite exists.** `./gradlew build` is the only verification step.

**Java 25 JDK required** on the build PATH (`options.release = 25` in `build.gradle.kts`). System `java` may be older — set `JAVA_HOME` to a Java 25+ JDK if the build fails with "Releaseversion 25 nicht unterstützt".

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
- **Map-based item scaling**: `ConfigData.itemScalingFactors` is a `Map<String, Integer>`. Wired manually in `ConfigScreen` + `ConfigScreenUtils.createItemScalingOption()`. Entries are NOT annotation-discovered — you must add them to the `Stream.of(...)` list in ConfigData.
- **Item scaling button**: Added as a `Button` widget in the main screen's `addOptions()` via `this.list.addSmall(List.of(...))`.

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
Map: `itemScalingFactors` — keys are item description IDs (e.g. `item.minecraft.totem_of_undying`, matching `Item.getDescriptionId()`), values are percentages 1–100. Backed by a mutable `TreeMap` via `Collectors.toMap(..., TreeMap::new)`.

## Registered Mixins (from `bactromod.mixins.json`)
`features.boatmap`, `features.fog`, `features.fullbright`, `features.itemscaling`, `features.lowfire`, `features.lowshield`, `features.nightvision`, `features.noopgmswitcher` (2 mixins), `features.nopumpkinblur`, `features.riptidetridentshield`, `settingsbutton`.

Config file is at `<gameDir>/config/bactromod.json`. If JSON parsing fails, Config backs up the broken file to `bactromod_old_<epoch>.json` and recreates defaults.


<!-- headroom:rtk-instructions -->
# RTK (Rust Token Killer) - Token-Optimized Commands

When running shell commands, **always prefix with `rtk`**. This reduces context
usage by 60-90% with zero behavior change. If rtk has no filter for a command,
it passes through unchanged — so it is always safe to use.

## Key Commands
```bash
# Git (59-80% savings)
rtk git status          rtk git diff            rtk git log

# Files & Search (60-75% savings)
rtk ls <path>           rtk read <file>         rtk grep <pattern>
rtk find <pattern>      rtk diff <file>

# Test (90-99% savings) — shows failures only
rtk pytest tests/       rtk cargo test          rtk test <cmd>

# Build & Lint (80-90% savings) — shows errors only
rtk tsc                 rtk lint                rtk cargo build
rtk prettier --check    rtk mypy                rtk ruff check

# Analysis (70-90% savings)
rtk err <cmd>           rtk log <file>          rtk json <file>
rtk summary <cmd>       rtk deps                rtk env

# GitHub (26-87% savings)
rtk gh pr view <n>      rtk gh run list         rtk gh issue list

# Infrastructure (85% savings)
rtk docker ps           rtk kubectl get         rtk docker logs <c>

# Package managers (70-90% savings)
rtk pip list            rtk pnpm install        rtk npm run <script>
```

## Rules
- In command chains, prefix each segment: `rtk git add . && rtk git commit -m "msg"`
- For debugging, use raw command without rtk prefix
- `rtk proxy <cmd>` runs command without filtering but tracks usage
<!-- /headroom:rtk-instructions -->
