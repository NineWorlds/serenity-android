# Discovery Log - Project Standards Context Discovery

## Phase 1: Deep Scanning & Pattern Identification - COMPLETE

### Task 1.1: Stack & Ecosystem Identification
- **Languages:** Kotlin (Primary), Java (Legacy).
- **Build System:** Gradle with Version Catalogs (`libs.versions.toml`).
- **Core Frameworks:** Leanback, Moxy, Toothpick, Coroutines, Retrofit, Moshi, Glide, Media3.
- **Testing:** JUnit 4, MockK (relaxed), Robolectric.

### Task 1.2: 'Gravity Well' & Architecture Mapping
- **Entry Point:** `SerenityApplication` (Toothpick `APPLICATION_SCOPE`).
- **Base Classes:** `InjectingActivity`, `InjectingMvpActivity`.
- **Logic Center:** Presenters manage UI state; Repositories handle data fetching via Coroutines.
- **Presenter Pattern:** `internal val presenter by moxyPresenter { presenterProvider.get() }`.

### Task 1.3: Anti-Pattern Identification
- **Forbidden:** `@InjectPresenter` (Detected violations in `ExoplayerVideoActivity` and `StatusOverlayFrameLayout`).
- **Forbidden:** `MotionLayout`.
- **Legacy:** `findViewById` (MUST use View Binding).
- **Legacy:** Retrofit `.enqueue()` (MUST use `suspend` + `executeOrThrow`).

### Task 1.4: UI, Asset & Multi-Module Mapping
- **Resource Naming:** `activity_`, `fragment_`, `include_`, `item_`, `dialog_`.
- **IDs:** `snake_case` (e.g., `retry_button`).
- **Colors/Dimens:** Centralized in `res/values/`.
- **Module Flow:** `serenity-app` -> `*-lib` -> `serenity-common`.

### Task 1.5: Tribal Knowledge Synthesis
- **Testing Strategy:** `InjectingTest` only for field injection. MockK must be used without annotations.
- **TV Optimization:** Focus effects are non-negotiable for D-pad navigation.
- **Provider Decoupling:** API models must map to `serenity-common` domain models.

---

## Phase 2: Extraction & Documentation - COMPLETE
- Architectural standards formalized in `architecture.md`.
- UI/Resource standards formalized in `ui-standards.md`.
- Communication protocols formalized in `communication-protocol.md`.
- Testing and MockK standards formalized in `TESTING.md`.
- Forbidden patterns explicitly listed in `CONSTRAINTS.md` and `FRAMEWORKS.md`.

---

## Phase 3: Validation Dry-Run - PENDING
*Awaiting user approval to proceed.*
