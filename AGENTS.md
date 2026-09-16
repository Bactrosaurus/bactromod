# BactroMod Agent Guide

## Project shape

- BactroMod is a client-only Fabric mod. It has no server component and currently has no Fabric `client` entrypoint; behavior is implemented by client Mixins.
- The only declared Java entrypoint is the optional ModMenu adapter: `de.daniel.bactromod.impl.ModMenuIntegration`.
- The repository is a single Gradle/Loom project. Main source is under `src/main/java/de/daniel/bactromod/`; resources are under `src/main/resources/`.

## Build and run

- Use the checked-in Gradle wrapper, not a system Gradle installation.
- The current toolchain is Java 25+, Minecraft 26.3, Fabric Loader 0.19.5, Fabric API `0.160.5+26.3`, ModMenu `21.0.0-beta.1`, Loom `1.17-SNAPSHOT`, and Gradle 9.7.1. The authoritative dependency/version values are in `gradle.properties` and the wrapper version is in `gradle/wrapper/gradle-wrapper.properties`.
- `./gradlew build` is the repository verification command. It compiles the mod, processes resources, and creates the mod jar and sources jar in `build/libs/`.
- `./gradlew runClient` launches the Minecraft development client using the project-local `run/` directory.
- There is no test suite, lint task, or CI workflow in this repository. A successful build checks compilation and resource processing, but not whether every Mixin applies correctly at runtime.
- Gradle is configured with a 1 GiB heap, parallel execution, and configuration cache disabled. Preserve these settings unless the build setup itself is being changed.

## Metadata and Mixins

- `src/main/resources/fabric.mod.json` is expanded by `processResources`. Keep `${version}`, `${loader_version}`, `${minecraft_version}`, `${fabric_api_version}`, and `${modmenu_version}` there; change their sources in `gradle.properties` instead of hardcoding expanded values in the metadata.
- The mod depends on Fabric Loader, Minecraft, Java 25+, and Fabric API. Fabric Loader and Fabric API use their configured versions as inclusive minimums so newer compatible releases are accepted; Minecraft remains constrained to the configured release line because Mixins require version-specific verification. ModMenu is `compileOnly` at build time, a suggested optional runtime dependency, and also uses its configured version as an inclusive minimum.
- Register gameplay Mixins in `src/main/resources/bactromod.mixins.json`. It is a client mixin config with `compatibilityLevel` `JAVA_25`, `required: false`, and `injectors.defaultRequire: 0`.
- Every registered mixin must have an `@Mixin` annotation. `overwrites.requireAnnotations` also requires explicit `@Overwrite` annotations if overwrite methods are added. Keep the registered name synchronized with the package and class name.
- Feature Mixins live under `src/main/java/de/daniel/bactromod/mixins/features/<feature>/` and generally use the convention `Mixin<ExactVanillaClassName>`. Multiple classes with the same simple name are intentional because each feature remains isolated in its own package.
- The fallback settings access is a button injected into `CreditsAndAttributionScreen` by `MixinCreditsAndAttributionScreen`; it is not a Fabric entrypoint. Preserve it when changing the config UI.

## Current feature wiring

The registered feature packages and their configuration fields are:

- `fullbright`: multiplies the lightmap gamma by `gammaMultiplier` (default 15, range 1–15).
- `nightvision`: returns zero from `GameRenderer.nightVisionScale` when `nightVision` is disabled (default enabled).
- `nopumpkinblur`: hides a carved pumpkin from the camera overlay when `pumpkinBlur` is disabled (default disabled).
- `lowfire`: translates the first-person fire overlay by `fireOffset / 100` (default -30, range -100–100).
- `lowshield`: translates first-person shield rendering by `shieldOffset / 100` (default -20, range -100–100).
- `boatmap`: changes the first- and off-hand filled-map hand-height interpolation in `FirstPersonHandsAndItems.tick` when `showMapWhileInBoat` is enabled (default enabled).
- `fog`: independently controls lava, powder snow, blindness, darkness, water, and atmospheric fog through `lavaFog`, `powderSnowFog`, `blindnessFog`, `darknessFog`, `waterFog`, and `atmosphericFog` (all default disabled).
- `itemscaling`: applies per-item first-person scale values from `itemScalingFactors` (default 100 in the UI, range 1–100).
- `riptidetridentshield`: replaces the affected shield transform during a riptide trident spin when `fixShieldRiptideTrident` is enabled (default enabled).
- `noopgmswitcher`: bypasses the relevant permission checks for the F3+F4 game-mode switcher when `ignoreOpGamemodeSwitcher` is enabled (default enabled).

## Configuration and settings UI

- `Config.get()` returns the single cached `ConfigData` instance initialized from `<gameDir>/config/bactromod.json`. Mutate that instance and call `Config.save()`; saving writes the JSON through a temporary file and moves it into place.
- Mixins must read the current config at use time; do not copy settings into static fields during class initialization. If one method needs several settings, call `Config.get()` once and reuse the returned `ConfigData`.
- Main-screen settings use a type-specific annotation from `config.optiontypes`: `@BooleanOption` creates a toggle and `@IntegerOption(min, max)` creates an integer slider. Keep each data type in its own annotation and option builder; do not inspect field types to choose UI controls.
- A new setting requires matching `bactromod.options.<field>` and `bactromod.options.<field>.desc` translations in every file under `assets/bactromod/lang/`.
- `itemScalingFactors` is a `Map<String, Integer>` keyed by item description IDs and is deliberately not annotation-driven. The item-scaling sub-screen enumerates registered items, excludes air, sorts by localized name, supports searching by localized name, registry path, or description ID, and persists each change immediately.
- If the config file contains invalid JSON, `Config` moves it beside the config as `bactromod_old_<epoch>.json` (adding a suffix on collision), logs the backup location, and recreates defaults. Preserve this recovery behavior when changing config loading.
- Gson does not enforce the UI ranges when loading hand-edited JSON. Consumers apply gamma, offsets, and item scale values directly, so validate or clamp values at the consumption boundary if changing those paths.

## Runtime-sensitive Mixins

The following selectors are coupled to the current Minecraft 26.3 implementation and need runtime verification after a Minecraft or mapping upgrade:

- `MixinFogRenderer` maps the order of `FOG_ENVIRONMENTS` by index: lava 0, powder snow 1, blindness 2, darkness 3, water 4, atmospheric 5.
- `MixinKeyboardHandler` wraps both `PermissionCheck.check` calls in `handleDebugKeys`; `MixinGameModeSwitcherScreen` wraps the switcher's permission check.
- `MixinFirstPersonHandsAndItemsRenderer` for the riptide shield fix targets `PoseStack.translate` at ordinal 12 and manually balances the pose stack with `popPose()`.
- Several render Mixins depend on exact method descriptors and invocation targets in `FirstPersonHandsAndItems`, `FirstPersonHandsAndItemsRenderer`, `ScreenEffectRenderer`, `Lightmap`, `GameRenderer`, and `Hud`.

When changing Minecraft versions or any target method, inspect the decompiled target and run the development client to verify each affected feature. Do not treat `./gradlew build` alone as proof that injections still apply.

## Adding or changing a feature

1. Identify whether the behavior belongs in an existing feature package or a new client Mixin.
2. For a configurable option, add an annotated field in `ConfigData`, then add its name and description to every language file. The field name becomes the translation-key suffix automatically. For item scaling, update the map/UI path instead.
3. Add the exact Mixin class name to `bactromod.mixins.json` and ensure the class has `@Mixin`.
4. Preserve the live `Config.get()` access pattern and safe config recovery behavior.
5. Run `./gradlew build`; for injection-point or Minecraft-version changes, also run `./gradlew runClient` and exercise the affected feature.
