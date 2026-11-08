# Repository Guidelines

## Project Structure & Module Organization

BactroMod is a single Gradle/Loom project: a client-only Fabric mod for Minecraft 26.3. Behavior runs through Mixins; there is no Fabric `client` entrypoint or server component. Fabric API is required; ModMenu is optional and its adapter is the only declared Java entrypoint.

Java sources live in `src/main/java/de/daniel/bactromod/`. Resources live in `src/main/resources/`: `fabric.mod.json` declares dependencies/entrypoints, `bactromod.mixins.json` registers injections, and `assets/bactromod/` contains the icon and translations.

## Configuration & UI Flow

- `config/ConfigData`: settings, defaults, and UI annotations. `config/optiontypes/` provides runtime field annotations for Boolean and Integer controls.
- `config/Config`: loads once on class initialization; `Config.get()` returns the same mutable instance. Its package-private `ConfigFile` helper owns Gson persistence for `<gameDir>/config/bactromod.json`. UI callbacks mutate settings and call `Config.save()`; Mixins read them at use time. External file edits are not hot-reloaded.
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
| `totemoverlay` | `ScreenEffectRenderer` | `totemOverlayPercentSize`: scales the item activation animation, 1–100; only when the activated item is a Totem of Undying. |

## Build and Development Commands

Use Java 25+ and the checked-in Gradle wrapper:

- `./gradlew build`: compile, process resources, and produce jars in `build/libs/`.
- `./gradlew runClient`: launch the development client using `run/`.
- `./gradlew genSources`: generate Minecraft sources for inspecting injection targets.

Change versions in `gradle.properties`; retain expansion placeholders in `fabric.mod.json`, dependency minimums, and the Minecraft release-line constraint.

## Coding Style & Naming Conventions

Use four-space indentation, PascalCase classes, and camelCase methods/fields. Follow neighboring code; no formatter or linter is configured. Keep feature Mixins separate, named `Mixin<ExactVanillaClassName>`, annotated with `@Mixin`, and registered in `bactromod.mixins.json`. Prefer composable `@WrapOperation` hooks; call the wrapped operation unless intentionally replacing its behavior.

## Configuration & Rendering Safeguards

Read live settings through `Config.get()`; persist changes with `Config.save()`. Preserve temporary-file saves, malformed-JSON backups, and consumption-time clamping.

Add settings to `ConfigData` using `@BooleanOption` or `@IntegerOption(min, max)`; existing types need no manual UI registration. Keep separate annotations/builders for new data types rather than inspecting field types. Add `bactromod.options.<field>` and `bactromod.options.<field>.desc` translations in every language, matching official Minecraft terminology. Item scaling remains a separate map/UI path keyed by item description IDs.

Preserve fullbright's change-triggered lightmap refresh before extraction and cubic blend afterward. Keep fire-overlay pose stacks balanced with `try/finally`. Shield translation (injector order 900) must precede item scaling (1000) at both item submissions. Recheck fog indices (0–5 in the table's listed order), ordinals, local captures, and exact selectors after Minecraft upgrades.

## Verification

There are no automated tests or CI workflows. Build success alone does not verify Mixin application; the Mixin configuration uses `required: false` and `defaultRequire: 0`. After Mixin, selector, or Minecraft-version changes, inspect vanilla sources and manually exercise affected features:

```bash
JAVA_TOOL_OPTIONS='-Dmixin.debug.countInjections=true -Dmixin.debug.export=true' ./gradlew runClient
```

Declare expected injection counts and exercise affected features in-game. For brightness changes, test the mod slider independently while paused; check rendering changes on OpenGL and Vulkan where available.

## Commit & Pull Request Guidelines

Commit directly to `dev`; merge into `main` through PRs and sync `dev` afterward.

Recent commits commonly use `fix:`, `docs:`, and `chore:` prefixes, sometimes referencing issues such as `(#25)`. Use concise, descriptive subjects. PRs should explain behavior changes, link relevant issues, report build/runtime checks, and include screenshots for visible changes.
