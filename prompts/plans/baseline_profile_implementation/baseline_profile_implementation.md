# [SPEC] Implement Android Baseline Profile Support

## Goal
Implement and integrate Android Baseline Profile support into the `serenity-app` module to improve startup performance and runtime execution.

## Context
- **Current State:** The project does not currently have Baseline Profiles configured.
- **Constraints:** Must follow Android Baseline Profile best practices.
- **Files:** `:serenity-app` module.

## Standards Compliance
- [ ] **Architecture**: Repository pattern, Coroutines for I/O, Toothpick scoping.
- [ ] **UI/Leanback**: View Binding, `snake_case` IDs, Focus effects, No MotionLayout.
- [ ] **Communication**: Retrofit `executeOrThrow`, Moshi mapping, domain decoupling.
- [ ] **Testing**: MockK (no annotations), `clearAllMocks()`, `InjectingTest` if needed.

## Impact Surface
- **Dependencies:** Might require updates to Gradle plugins for profile generation.
- **Consumers:** `:serenity-app` build process.

## Risk & Rollback
- **Potential Risks:** Build failures if misconfigured. Increased build times.
- **Rollback Strategy:** Revert changes to `build.gradle.kts` and remove generated files.

## Phase 1: Setup and Configuration
### Tasks
- [ ] Task 1: Add `androidx.profileinstaller` dependency to `serenity-app`.
- [ ] Task 2: Create a Baseline Profile generator module or integrate into build.
- [ ] Task 3: Configure `build.gradle.kts` to package the generated profile.
### Verification
- [ ] Build the app and verify the profile is included in the APK.
- [ ] [WAIT FOR APPROVAL]

## Phase 2: Implementation and Generation
### Tasks
- [ ] Task 1: Implement Macrobenchmark/Profile Generator code.
- [ ] Task 2: Run generator to create `baseline-prof.txt`.
- [ ] Task 3: Check `baseline-prof.txt` into `serenity-app/src/main/baseline-prof.txt`.
### Verification
- [ ] Confirm baseline profile is being applied during installation.
- [ ] [WAIT FOR APPROVAL]

## Final Deliverables
- Baseline Profile configuration in `build.gradle.kts`.
- `baseline-prof.txt` checked into repository.

## Discovery & Learning Log
- [ ] Record any unexpected behavior, hidden dependencies, or architectural discoveries here during execution.

## Cleanup & Manual Actions (Human Required)
- [ ] Task: [Any other non-agent task]
