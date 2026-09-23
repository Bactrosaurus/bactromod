# Repository Guidelines

## Project Structure & Module Organization

BactroMod is a single Gradle/Loom project: a client-only Fabric mod for Minecraft 26.3. Behavior runs through Mixins; there is no Fabric `client` entrypoint or server component. Fabric API is required; ModMenu is optional and its adapter is the only declared Java entrypoint.

Java sources live in `src/main/java/de/daniel/bactromod/`. Resources live in `src/main/resources/`: `fabric.mod.json` declares dependencies/entrypoints, `bactromod.mixins.json` registers injections, and `assets/bactromod/` contains the icon and translations.

## Configuration & UI Flow

- `config/ConfigData`: settings, defaults, and UI annotations. `config/optiontypes/` provides runtime field annotations for Boolean and Integer controls.
- `config/Config`: loads once on class initialization; `Config.get()` returns the same mutable instance. Its package-private `ConfigFile` helper owns Gson persistence for `<gameDir>/config/bactromod.json` and accepts a path so tests can use temporary directories. UI callbacks mutate settings and call `Config.save()`; Mixins read them at use time. External file edits are not hot-reloaded.
- `config/ConfigScreen`: discovers annotated fields through reflection, builds vanilla `OptionInstance` controls, and derives translation keys from field names. Its nested item-scaling screen lists registered items except air, includes saved entries, sorts by localized name, and searches names, registry paths, or description IDs. Each change saves immediately.
- `impl/ModMenuIntegration` and `mixins/settingsbutton/MixinCreditsAndAttributionScreen`: two routes to the same config screen. Preserve settings access without ModMenu.

## Feature Map

Each package below lives under `mixins/features/`. Targets name vanilla classes; Mixin classes use the same name prefixed with `Mixin`.

| Package | Vanilla target | Settings and behavior |
| --- | --- | --- |
| `fullbright` | `LightmapRenderStateExtractor` | `gammaMultiplier`: cubic ambient-color blend; 1 is vanilla, 15 fullbright. |
| `nightvision` | `GameRenderer` | `nightVision=false` suppresses night-vision intensity. |
| `nopumpkinblur` | `Hud` | `pumpkinBlur=false` hides the carved-pumpkin overlay. |
| `lowfire` | `ScreenEffectRenderer` | `fireOffset`: first-person fire translation. |
| `lowshield` | `FirstPersonHandsAndItemsRenderer` | `shieldOffset`: first-person shield translation. |
| `itemscaling` | `FirstPersonHandsAndItemsRenderer` | `itemScalingFactors`: per-item scale percentages, 1–100. |
| `riptidetridentshield` | `FirstPersonHandsAndItemsRenderer` | `fixShieldRiptideTrident`: adjusts the spin-attack branch for shields. |
| `boatmap` | `FirstPersonHandsAndItems` | `showMapWhileInBoat`: changes both hands' filled-map height interpolation. |
| `fog` | `FogRenderer` | Six `*Fog` toggles: lava, powder snow, blindness, darkness, water, atmospheric. |
| `noopgmswitcher` | `KeyboardHandler`, `GameModeSwitcherScreen` | `ignoreOpGamemodeSwitcher`: bypasses client permission checks; grants no server permissions. |

## Build, Test, and Development Commands

Use Java 25+ and the checked-in Gradle wrapper:

- `./gradlew build`: compile, process resources, and produce jars in `build/libs/`.
- `./gradlew test`: run JUnit config and packaged-resource tests; reports go to `build/reports/tests/test/`.
- `./gradlew runClientGameTest`: run the automated fullbright/settings-screen regression in a disposable `build/run/clientGameTest/` directory, with injection counting enabled.
- `./gradlew runClient`: launch the development client using `run/`.
- `./gradlew genSources`: generate Minecraft sources for inspecting injection targets.

Change versions in `gradle.properties`; retain expansion placeholders in `fabric.mod.json`, dependency minimums, and the Minecraft release-line constraint.

## Coding Style & Naming Conventions

Use four-space indentation, PascalCase classes, and camelCase methods/fields. Follow neighboring code; no formatter or linter is configured. Keep feature Mixins separate, named `Mixin<ExactVanillaClassName>`, annotated with `@Mixin`, and registered in `bactromod.mixins.json`. Prefer composable `@WrapOperation` hooks; call the wrapped operation unless intentionally replacing its behavior.

## Configuration & Rendering Safeguards

Read live settings through `Config.get()`; persist changes with `Config.save()`. Preserve temporary-file saves, malformed-JSON backups, and consumption-time clamping.

Add settings to `ConfigData` using `@BooleanOption` or `@IntegerOption(min, max)`; existing types need no manual UI registration. Keep separate annotations/builders for new data types rather than inspecting field types. Add `bactromod.options.<field>` and `bactromod.options.<field>.desc` translations in every language, matching official Minecraft terminology. Item scaling remains a separate map/UI path keyed by item description IDs.

Preserve fullbright's change-triggered lightmap refresh before extraction and cubic blend afterward. Keep fire-overlay pose stacks balanced with `try/finally`. Shield translation (injector order 900) must precede item scaling (1000) at both item submissions. Recheck fog indices (0–5 in the table's listed order), ordinals, local captures, and exact selectors after Minecraft upgrades.

## Testing Guidelines

JUnit tests under `src/test/java/` cover config persistence/recovery, translations, and packaged metadata. Use `*Test` names and temporary directories. Client tests under `src/gametest/` exercise actual transformed Minecraft classes; their test mod is excluded from release jars. Fullbright checks paused extraction after config changes without vanilla ticks or gamma changes; fog checks extracted distances after toggling atmospheric fog. These test render state, not final GPU pixels.

`.github/workflows/build.yml` runs the build and client tests on pushes to `dev`/`main`, PRs targeting `main`, and manual dispatch, using Java 25 and software Vulkan rendering on a standard Ubuntu runner. Commit directly to `dev`; protect the default `main` branch in GitHub settings by requiring a PR with a passing `build` check, and leave `dev` unprotected. Sync `dev` after merging to `main`. Discover Lavapipe through `VK_LOADER_DRIVERS_SELECT`; do not hardcode its version-dependent manifest filename. A Vulkan preflight and six-minute client timeout keep graphics failures diagnosable. Jars expire after seven days; failure diagnostics after three. No coverage target is enforced.

Build success alone does not verify Mixin application; the Mixin configuration uses `required: false` and `defaultRequire: 0`. Run `./gradlew runClientGameTest` after Mixin changes. After selector or Minecraft-version changes, also inspect vanilla sources and manually exercise affected features:

```bash
JAVA_TOOL_OPTIONS='-Dmixin.debug.countInjections=true -Dmixin.debug.export=true' ./gradlew runClient
```

Declare expected injection counts and exercise affected features in-game. For brightness changes, test the mod slider independently while paused; check rendering changes on OpenGL and Vulkan where available.

## Commit & Pull Request Guidelines

Recent commits commonly use `fix:`, `docs:`, and `chore:` prefixes, sometimes referencing issues such as `(#25)`. Use concise, descriptive subjects. PRs should explain behavior changes, link relevant issues, report build/runtime checks, and include screenshots for visible changes.
