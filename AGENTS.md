# BactroMod Agent Guide

## Build

- This is a single-project, client-only Fabric Loom mod. The supported versions and dependency pins are in `gradle.properties`; do not duplicate or edit the expanded values in `src/main/resources/fabric.mod.json`.
- Use the Gradle wrapper. `./gradlew build` is the repository's verification command and produces the mod and sources jars in `build/libs/`. There is no test suite, lint task, or CI workflow in this repository.
- A Java 25+ JDK is required (`build.gradle.kts` sets `options.release = 25`); the wrapper is Gradle `9.5.1`. Gradle uses a 1 GiB heap, parallel execution, and deliberately disables configuration cache in `gradle.properties`.
- `./gradlew runClient` launches the client development instance and uses the project-local `run/` directory.

## Wiring

- The Fabric client entrypoint is `de.daniel.bactromod.BactroMod::init`; gameplay changes are implemented by client Mixins registered in `src/main/resources/bactromod.mixins.json`.
- Mixin classes live under `src/main/java/de/daniel/bactromod/mixins/features/<feature>/` and should be named `Mixin<ExactVanillaClassName>`. The mixin config has `requireAnnotations: true`, so every registered mixin must have an `@Mixin` annotation.
- `fabric.mod.json` is processed by Gradle: `${version}`, `${loader_version}`, and `${minecraft_version}` are expanded by `processResources`. Change their sources in `gradle.properties` instead.
- ModMenu is an optional runtime integration (`suggests`), even though its API is a compile-time dependency. Keep the fallback Credits-screen settings entrypoint working when changing config UI.

## Configuration

- `Config.load()` returns the cached `ConfigData`; `Config.save(data)` persists it to `<gameDir>/config/bactromod.json` and updates the cache. Mixins should read the current config rather than caching values at class initialization, and should call `Config.load()` once per method when multiple fields are needed.
- Boolean and integer settings in `ConfigData` are discovered reflectively through `@BooleanOption` and `@IntegerOption`. For a new setting, add the annotated field and matching translation keys `bactromod.options.<field>` and `bactromod.options.<field>.desc` to all three language files: `en_us.json`, `de_de.json`, and `ru_ru.json`.
- `itemScalingFactors` is not annotation-driven. Its item description IDs and defaults are declared in `ConfigData`, and the screen iterates that map; change `ConfigScreen`/`ConfigScreenUtils` only when changing the item-scaling UI behavior.
- Invalid JSON is moved beside the config as `bactromod_old_<epoch>.json`, then defaults are recreated. Preserve this recovery behavior when changing `Config`.

## Adding Features

1. Add the config field and translations if the feature is configurable.
2. Add a client Mixin under the feature package and register its exact class in `bactromod.mixins.json`.
3. Run `./gradlew build` before considering the change verified.
