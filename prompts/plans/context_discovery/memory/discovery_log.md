# Discovery Log - Project Standards Context Discovery

## Phase 1: Deep Scanning & Pattern Identification - COMPLETE

### Task 1: Scan `serenity-app` for consistent Activity/Fragment/Presenter lifecycle patterns.
- [x] Initial scan of Activities and Fragments.
    - Patterns: Activities often extend `InjectingMvpActivity`. Fragments like `DetailsFragment` manually manage `MvpDelegate`.
    - View Binding: `binding = ActivitySelectionBinding.inflate(layoutInflater)` pattern is consistent.
- [x] Presenter injection and lifecycle management.
    - Pattern: `internal val presenter by moxyPresenter { presenterProvider.get() }` with injected `Provider<Presenter>`.
    - DI: Toothpick is used, typically scoped to `APPLICATION_SCOPE`.

### Task 2: Analyze `emby-lib` and `jellyfin-lib` for data repository and API error handling patterns.
- [x] Repository structure and interface patterns.
    - Pattern: Repositories (e.g., `VideoRepository`) use `suspend` functions and `withContext(Dispatchers.IO)`. They wrap the `SerenityClient` interface.
- [x] Error handling strategy in API clients.
    - Pattern: `EmbyAPIClient` uses a `Call<T>.executeOrThrow()` extension to handle Retrofit responses, throwing `IOException` on failure.

### Task 3: Identify UI naming conventions and design tokens (colors, dimensions) across modules.
- [x] Resource naming (XML layouts, IDs).
    - Layouts: `activity_`, `include_`, `item_`, `dialog_`, `button_`.
    - IDs: snake_case (e.g., `server_container`, `retry_button`).
- [x] Color and dimension usage (standardization).
    - Colors: `res/values/color.xml` and `legacy_colors.xml`.
    - Dimensions: `res/values/dimens.xml` and `app_dimens.xml`.

### Task 4: Compare `framer-motion` vs `css-transitions` equivalents in Android.
- [x] Animation patterns (XML, MotionLayout, or code-based).
    - Found XML animations in `res/anim` (e.g., `fade_in.xml`, `main_menu_grow.xml`).
    - No direct `MotionLayout` usage found in layouts yet, but `motion.xml` exists in `res/values`.

### Task 5: Check for a `constitution.md` or high-level philosophy document.
- [x] Search for philosophy/constitution docs.
    - Found `README.md`. No `constitution.md` exists. Philosophy: Open source (MIT), community-driven, focuses on Android TV/Fire TV, Emby/Jellyfin support (Plex deprecated).

## Phase 2: Extraction & Documentation - COMPLETE

### Task 1: Create or update `prompts/agents/architecture.md`
- [x] Documented Directory structure, module boundaries, and Data Flow (Golden Standard).

### Task 2: Create or update `prompts/agents/ui-standards.md`
- [x] Documented Component patterns, naming conventions, and Design Tokens.

### Task 3: Create or update `prompts/agents/communication-protocol.md`
- [x] Documented API design, error handling (`executeOrThrow`), and logging standards.

### Task 4: Update `prompts/agents/FRAMEWORKS.md`
- [x] Refreshed with latest "Source of Truth" findings (DI, MVP, View Binding, Testing).

### Task 5: Create/Update `prompts/agents/constitution.md`
- [x] Formalized project philosophy and "Non-Negotiables" for contributors.

## Phase 3: Conflict Resolution & Refinement - COMPLETE

### Task 1: Present any conflicting patterns discovered.
- [x] Conflict 1: **DI Scoping in Tests**. (Resolved: `InjectingTest` only for field injection, else JUnit 4).
- [x] Conflict 2: **View Binding vs. Legacy**. (Resolved: Mandatory for new/modified UI).
- [x] Conflict 3: **Animation Logic**. (Resolved: Stick to XML, **NO MotionLayout**).
- [x] Conflict 4: **Repository Access**. (Resolved: Presenters MUST use Repositories).

### Task 2: Ask user for the "Golden Standard" on identified conflicts.
- [x] User provided feedback and confirmed Golden Standards.

### Task 3: Finalize documentation based on user feedback.
- [x] Documentation updated in `architecture.md`, `ui-standards.md`, and `TESTING.md`.
