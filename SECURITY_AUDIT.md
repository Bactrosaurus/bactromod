# Security Audit Report — BactroMod

**Project:** BactroMod — client-side Fabric mod for Minecraft (Java 25)
**Audit date:** 2026-07-30
**Scope:** Full repository reconnaissance, source code, build configuration, dependencies, secrets, and deployment model.
**Mode:** Read-only audit. No project files were modified during this audit.

---

## Executive Summary

BactroMod is a **client-side-only** Fabric mod that injects visual/quality-of-life
tweaks into the Minecraft client via Mixins. It has **no server-side component, no
HTTP/API surface, no database, no authentication, no cryptography, no network I/O,
and no CI/CD or container configuration. The only persistent state is a single
local JSON config file written to the user's own game directory.

The application's attack surface is consequently very small. The audit found
**no Critical, High, or Medium vulnerabilities**. A small number of **Low** and
**Informational** issues are documented below, together with hardening
recommendations. The most notable real-world risk is unrelated to the mod's
runtime behavior: a **plaintext session token stored in `opencode.sh`** which is
not currently gitignored.

The existing security posture is sound for the project's intended purpose:
untrusted input never reaches code-execution paths, file operations are confined
to a fixed path, and Gson deserialization targets a concrete POJO with no
polymorphism (no gadget-chain risk).

---

## Risk Summary

| Severity      | Count |
| ------------- | ----: |
| Critical      |     0 |
| High          |     0 |
| Medium        |     0 |
| Low           |     3 |
| Informational |     3 |

---

## Critical & High Findings

None.

---

## Complete Findings

### L-1  Plaintext session token in `opencode.sh` (not gitignored)

- **Severity:** Low
- **CWE:** CWE-798 (Use of Hard-coded Credentials) / CWE-312 (Cleartext Storage of Sensitive Information)
- **Affected file:** `opencode.sh:1`
- **Evidence:** The file contains an opencode CLI session identifier in cleartext:
  `opencode -s ses_...`
- **Status:** Confirmed (file present on disk).
- **Exploitability:** The token is **not** currently tracked by git
  (`git ls-files opencode.sh` returns nothing) and was never committed
  (`git log -- opencode.sh` is empty). However, `git check-ignore opencode.sh`
  returns nothing — i.e. the file is **not ignored**. It is therefore at risk of
  being accidentally `git add`ed and pushed, which would leak the session token
  to anyone with access to the repository (or, for a public repo, to the world).
  The token's lifetime and exact capabilities depend on the opencode CLI, but
  any session token should be treated as a credential.
- **Preconditions:** A developer accidentally stages `opencode.sh`.
- **Potential impact:** Session hijacking / unauthorized use of the opencode
  agent session associated with the token.
- **Recommended remediation:** Add `opencode.sh` to `.gitignore` (and/or remove
  the file from the working tree). Alternatively, source the session id from an
  environment variable or a file outside the repo. The token in the existing
  file should be rotated/revoked once the file is no longer needed.
  ```
  # .gitignore addition
  /opencode.sh
  ```
- **Note:** Per audit instruction, this is a **recommendation only** — no
  `.gitignore` or `opencode.sh` changes were applied.

---

### L-2  No dependency lockfile; build uses snapshot/beta tooling versions

- **Severity:** Low
- **CWE:** CWE-1357 (Reliance on Uncontrolled Component) / CWE-1104 (Use of Unmaintained Third Party Components — adjacent)
- **Affected files:** `gradle.properties`, `settings.gradle.kts`, `build.gradle.kts`
- **Evidence:**
  - `loom_version=1.17-SNAPSHOT` (`settings.gradle.kts:11`) — Fabric Loom is
    pulled from a `-SNAPSHOT` maven coordinate, which resolves to whatever the
    snapshot repository currently serves and is not reproducible over time.
  - `modmenu_version=20.0.0-beta.2` (`gradle.properties:21`) — a beta
    dependency, by definition not a stable release.
  - No Gradle dependency lockfile (`gradle.lockfile`) or version catalog is
    used; transitive versions are resolved at build time.
  - The Gradle wrapper itself is pinned to a stable release
    (`gradle-wrapper.properties`: `gradle-9.5.1-bin.zip`,
    `validateDistributionUrl=true`) — this part is fine.
- **Status:** Probable (build reproducibility / minor supply-chain hardening).
- **Exploitability:** Not directly exploitable by an end user of the mod. The
  risk is to the build: a compromised or changed SNAPSHOT/beta artifact could
  alter the build output. This is a standard supply-chain-hygiene concern
  rather than an actively exploitable flaw.
- **Recommended remediation:**
  1. Prefer a stable (non-snapshot) Fabric Loom version when one is available.
  2. Pin/lock transitive dependencies via Gradle dependency locking
     (`./gradlew dependencies --write-locks`) or a version catalog.
  3. Keep `modmenu` on a non-beta release for published builds, if possible.
- **No code change required** — build configuration hardening only.

---

### L-3  Config values are not re-validated when loaded from disk

- **Severity:** Low
- **CWE:** CWE-20 (Improper Input Validation)
- **Affected file:** `Config.java:31` (load path); `ConfigData.java` (field defaults/ranges)
- **Evidence:** `Config.load()` returns the cached `ConfigData` deserialized by
  Gson directly from `bactromod.json`. The UI clamps integer options to their
  `@IntegerOption(intMin/intMax)` ranges and item-scaling factors to `1–100`
  (see `ConfigScreenUtils.java:56`, `ConfigScreenUtils.java:79`), but a user
  who hand-edits the JSON can store out-of-range values, e.g. an
  `itemScalingFactors` entry of `0` or `-500`, or a `gammaMultiplier` of `9999`.
  These are then consumed by the mixins (e.g.
  `MixinLightTexture.java:14`, `MixinItemInHandRenderer.java:23`).
- **Status:** Potential issue (low impact).
- **Exploitability:** The config file is on the user's own machine and is
  writable only by someone who already has local filesystem access. There is no
  remote/unchecked input path. Impact is limited to client-side visual glitches:
  - A scaling factor of `0` would render an item at zero size (invisible in hand).
  - A huge `gammaMultiplier` would over-brighten the lightmap.
  - Out-of-range `fireOffset`/`shieldOffset` just move the overlay further than
    the slider would allow.
  None of these cause crashes or code execution (verified: values are used
  only in arithmetic / `poseStack` transforms).
- **Preconditions:** Local attacker or an intentional hand-edit of the config.
- **Potential impact:** Cosmetic glitch / self-inflicted annoyance. No
  privilege escalation, no data loss.
- **Recommended remediation:** Optional hardening — clamp values to their
  annotated ranges inside `Config.loadOrCreate()` after deserialization, e.g. a
  small `validate()` method on `ConfigData` that re-applies `@IntegerOption`
  bounds and clamps each `itemScalingFactors` value to `1–100`. This makes the
  on-disk format as robust as the UI path.

---

## Informational Findings

### I-1  `noopgmswitcher` mixins bypass client-side OP permission checks

- **Severity:** Informational
- **CWE:** CWE-862 (Missing Authorization) — but this is an **intended feature**, not a defect.
- **Affected files:**
  - `MixinGameModeSwitcherScreen.java:17` — returns `true` from
    `PermissionCheck.check(...)` when `ignoreOpGamemodeSwitcher` is enabled.
  - `MixinKeyboardHandler.java:17` and `:26` — same pattern for F3+F4 debug keys.
- **Explanation:** When the `ignoreOpGamemodeSwitcher` config option is on
  (default `true`), the mod makes the client-side gamemode-switcher debug UI
  appear regardless of whether the local player has OP permissions. This is a
  documented, user-toggled feature (README "No-OP gamemode switcher"; lang key
  `bactromod.options.ignoreOpGamemodeSwitcher.desc`).
- **Why this is NOT a vulnerability:** This affects only the **client UI** that
  *proposes* a gamemode change. The actual gamemode change is still subject to
  **server-side** OP/permission enforcement in vanilla Minecraft. The mixin does
  not grant any server-side privilege; it only suppresses the client's
  pre-flight check that would otherwise hide the menu.
- **Status:** Informational — intended behavior, correctly scoped to client only.
- **No remediation required.**

---

### I-2  Gson deserialization of config is safe (no gadget-chain risk)

- **Severity:** Informational
- **Affected file:** `Config.java:31`
- **Explanation:** Gson is listed by the audit checklist as a potentially
  dangerous deserialization mechanism. Here, `GSON.fromJson(reader,
  ConfigData.class)` deserializes into a **concrete, non-polymorphic POJO**
  (`ConfigData`) with only primitive fields, a `Map<String,Integer>`, and no
  custom type adapters, no `@JsonTypeInfo`, and no `Object`/interface fields.
  Gson does **not** honor `@JsonTypeInfo`-style polymorphic typing the way
  Jackson does, and without a custom deserializer it cannot instantiate
  arbitrary classes. There is therefore no gadget-chain or arbitrary-type
  instantiation path.
- **Trust boundary:** The config file is local, written by the mod itself or
  hand-edited by the user on their own machine. No remote/untrusted source
  feeds this deserializer.
- **Status:** Informational — verified safe. No remediation required.

---

### I-3  AGENTS.md references an access widener that no longer exists

- **Severity:** Informational
- **Affected files:** `AGENTS.md` (mentions `bactromod.accesswidener`);
  `bactromod.mixins.json` (no access-widener reference); repo root (no
  `*.accesswidener` file present).
- **Explanation:** Git history shows the access widener was intentionally
  removed in commit `a152e70 chore: removed unused access widener`. The
  `AGENTS.md` developer guide still contains the line:
  *"If the mixin needs access to private/protected members, add entries to
  `bactromod.accesswidener`."* and the `ConfigData Fields` / mixins list,
  implying one exists. This is stale documentation, not a security issue, but
  it could mislead a future contributor into creating an access widener when
  none is wired up.
- **Status:** Informational — documentation drift.
- **Recommended remediation:** Update `AGENTS.md` to remove/clarify the access
  widener reference. (Outside the strict scope of a security audit; included
  for completeness.)

---

## Dependency Findings

Dependencies are declared in `build.gradle.kts` and versioned in
`gradle.properties`. No dependency vulnerability scanner (OWASP
Dependency-Check, Trivy, etc.) is available in this environment, so the
following is a manual review of the declared coordinates.

| Dependency                  | Version (from `gradle.properties`)        | Notes |
|-----------------------------|-------------------------------------------|-------|
| Minecraft (`com.mojang`)    | `26.2`                                    | Game version; not a library vulnerability vector. |
| Fabric Loader               | `0.19.3`                                  | Stable. |
| Fabric API                  | `0.152.1+26.2`                            | Minecraft-pinned; stable for this MC version. |
| ModMenu (`com.terraformersmc`)| `20.0.0-beta.2`                         | **Beta** release (see L-2). |
| Fabric Loom (build plugin)  | `1.17-SNAPSHOT`                           | **Snapshot** build plugin (see L-2). |
| Gradle wrapper              | `9.5.1-bin.zip`, `validateDistributionUrl=true` | Stable, pinned, URL validated. Good. |

**Repositories:** `mavenCentral`, `gradlePluginPortal`, Fabric maven, and
TerraformersMC maven are all used over **HTTPS** (`build.gradle.kts:13`,
`settings.gradle.kts:5-8`). No insecure `http://` repository references were
found. No dependency-confusion indicator (no `flatDir`, no custom local repos).

**No confirmed vulnerable or unmaintained dependencies were identified.**
The only dependency-related hardening items are L-2 (snapshot/beta versions
and missing lockfile).

---

## Secrets & Credential Findings

A repository-wide scan was performed for passwords, API keys, tokens, private
keys, certificates, and hardcoded credentials across `.java`, `.json`, `.kts`,
`.properties`, `.yml/.yaml`, `.md`, and `.sh` files, plus a `git log -S`
history scan for "password".

| Finding | Severity | Details |
|---------|----------|---------|
| Session token in `opencode.sh` | Low | See **L-1**. Single opencode session id (`ses_...`) in cleartext. **Not** tracked by git, but **not** gitignored. **Redacted here** — see the file on disk. |
| No other secrets found | — | Source files, lang files, `fabric.mod.json`, `gradle.properties`, and full git history are clean of credentials. The only non-trivial string in the repo is the opencode session id above. |

No actual secret values are reproduced in this report (CWE-532 avoidance). The
`opencode.sh` token should be rotated/revoked regardless of whether it is later
gitignored.

---

## Attack Chains

No meaningful attack chains were identified. The codebase has no reachable
remote/untrusted input that flows into a dangerous sink:

- The only external input is the local config file, which is written by the
  mod itself (or hand-edited by the local user). It is deserialized into a
  concrete POJO (no polymorphism → no deserialization gadget chain), and its
  values flow only into arithmetic/graphics transforms (no `exec`, no
  `ProcessBuilder`, no `Class.forName`, no `Runtime.`, no reflection on
  user-supplied types, no network calls).
- There are no HTTP endpoints, no SQL, no LDAP, no XML processing, no SSRF
  sinks, no file-upload handlers, and no command-execution paths.
- The Mixins only read config flags and adjust rendering; they do not open
  sockets, files beyond the config, or process attacker-controlled streams.

The closest thing to a "chain" is trivial and self-contained:
> Hand-edit `bactromod.json` with an out-of-range scaling factor (L-3) → item
> renders incorrectly. Impact: cosmetic only; no escalation.

---

## Positive Security Controls

The following security-relevant practices are already implemented correctly and
are worth preserving:

1. **Minimal attack surface by design** — client-only mod (`environment:
   "client"` in `fabric.mod.json`), no server code, no network listeners, no
   IPC, no database.
2. **Concrete-POJO Gson deserialization** for config — no polymorphic typing,
   no custom deserializers, no gadget-chain risk (I-2).
3. **Fixed, non-user-controlled config path** — `CONFIG_PATH` is built from
   `FabricLoader.getInstance().getConfigDir().resolve("bactromod.json")`
   (`Config.java:18`). User input never influences the path. No path traversal.
4. **Safe backup filename** — invalid-config backup uses
   `Instant.now().getEpochSecond()` (`Config.java:34`), not user-supplied input.
5. **Annotation-driven value clamping in the UI** — `@IntegerOption(intMin/intMax)`
   is enforced in `ConfigScreenUtils` (`ConfigScreenUtils.java:56`,
   `ConfigScreenUtils.java:79`), preventing the GUI from writing out-of-range
   values.
6. **Volatile config cache** — `configData` is `volatile` (`Config.java:20`),
   ensuring consistent visibility of config updates across the render/input
   threads.
7. **HTTPS-only Maven repositories** — all build repositories use HTTPS.
8. **Pinned, validated Gradle wrapper** — `gradle-9.5.1-bin.zip` with
   `validateDistributionUrl=true`.
9. **`requireAnnotations: true`** in `bactromod.mixins.json` — the build fails
   unless every mixin class carries `@Mixin`, preventing accidentally wiring up
   a bare class as a mixin.
10. **Restricted reflection** — `ConfigScreenUtils` reflects only on the mod's
    own `ConfigData` class fields (`ConfigData.class.getDeclaredFields()` in
    `ConfigScreen.java:25`), never on user-supplied types.
11. **No sensitive data in logs** — `Config.java` logs the config *path* on
    error, not its *contents*; no tokens/passwords/P II are logged anywhere.
12. **No `System.exit`, no `Runtime.exec`, no `ProcessBuilder`, no
    `ObjectInputStream`, no `XMLDecoder`, no dynamic class loading** anywhere
    in the source (grep-verified).

---

## Recommended Remediation Plan

### Immediate
1. **L-1:** Add `/opencode.sh` to `.gitignore` and rotate/revise the opencode
   session token in `opencode.sh`. This is the only finding with real-world
   leakage potential (if accidentally committed).

### Short-term
2. **L-2:** Migrate Fabric Loom off the `-SNAPSHOT` coordinate to a stable
   release; consider pinning `modmenu` to a stable (non-beta) version for
   published builds; add Gradle dependency locking (`--write-locks`) for
   reproducible/transitive-version control.
3. **I-3:** Update `AGENTS.md` to remove the stale `bactromod.accesswidener`
   reference so future contributors aren't misled.

### Long-term hardening (optional)
4. **L-3:** Add a `validate()` step to `Config.loadOrCreate()` that re-clamps
   `@IntegerOption` fields and `itemScalingFactors` values to their allowed
   ranges after deserialization, making the on-disk format as robust as the UI.
5. Consider running an automated dependency scanner (OWASP
   Dependency-Check/Trivy) in a future CI pipeline once CI is introduced — none
   exists today, so this is forward-looking only.

---

## Residual Risks

- **No automated dependency scan was run.** No scanner (OWASP
  Dependency-Check, Trivy, Snyk, OSV) was available in the audit environment.
  The dependency review in this report is manual and covers only the
  *declared* coordinates in `gradle.properties`/`build.gradle.kts`, not the
  full transitive tree. Transitive vulnerabilities, if any, were not
  enumerable here. Running `./gradlew dependencies` (or a scanner) in a
  network-enabled environment is recommended to close this gap.
- **Static analysis tooling** (Semgrep, SpotBugs/FindSecBugs, PMD) was not
  available either. Findings are based on a full manual read of all 18 source
  files plus resource/config files. Given the small codebase this is
  comprehensive, but automated SAST would add confidence.
- **The `opencode.sh` token's exact scope/lifetime could not be determined**
  from the repository alone. It is treated as a credential out of caution;
  rotating it is recommended regardless of whether it is gitignored.
- **Snapshot/beta build tooling (L-2) reproducibility** could not be fully
  assessed without performing a build (which is out of scope for a read-only
  audit). The concern is build-time supply-chain hygiene, not a runtime flaw.