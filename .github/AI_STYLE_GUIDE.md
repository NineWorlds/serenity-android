# Serenity for Android - AI Assistant Style Guide

## Purpose & Scope
This document defines how an AI agent should safely and effectively interact with the Serenity for Android repository. It specifies hard constraints (what the agent must never do), collaboration rules, coding and testing conventions, and conversion patterns (Java → Kotlin) with examples. Keep all examples and context; reorganize for fast machine consumption and human readability.

Intended audience:
- Automated agents (bots) assisting with code changes
- Engineers reading AI-generated suggestions
- Reviewers verifying compliance with repository policies

---

## Quick Start (for agents)
- Read this guide before taking any action.
- If you detect an error or a risky operation, always pause and ask for explicit permission.
- When you propose a code change, provide:
    1. Short summary of the change.
    2. Files touched (explicit paths).
    3. Reasoning and risks.
    4. Tests added/updated.
- Always optimize token usage while preserving clarity.

---

## Hard Constraints (must follow)
- NEVER edit `.env` or other environment variable files.
- NEVER run destructive git operations (e.g., `git reset --hard`, `rm`, `git checkout`/`git restore` to an older commit) unless explicitly instructed in writing.
- BEFORE deleting any file to silence a local failure, stop and ask the user.
- NEVER amend commits unless you have explicit written approval in the task thread.
- Coordinate with other agents before reverting or deleting work you did not author.
- Always double-check `git status` before committing.

---

## Git Interaction Rules (explicit workflow)
- Commit only the files you touched; use path-scoped commits:
    - Tracked files: git commit -m "<scoped message>" -- path/to/file1 path/to/file2
    - New files: add explicitly and commit only those paths.
- Quote paths containing brackets/parentheses when staging/committing.
- When running `git rebase`, avoid interactive editors: set `GIT_EDITOR=:` and `GIT_SEQUENCE_EDITOR=:` or use `--no-edit`.
- Moving/renaming/restoring files is allowed with coordination.
- Do not use `git restore` on files you didn't author—coordinate instead.

---

## Collaboration & Decision Points
- If you find a problem, mention it and ask before attempting a fix.
- Describe *how* you would fix it before proceeding.
- Proceed only after permission is granted for changes beyond small, well-scoped edits.
- When multiple agents are active, coordinate to avoid overlapping edits.

---

## Project Overview (brief)
Serenity for Android is a media server client for Android (Plex & Emby). It uses modern Android patterns and tooling: MVP (Moxy), Toothpick, Kotlin Coroutines, Retrofit/OkHttp, Glide, Spotless, and a test stack (JUnit, Robolectric, MockK, AssertJ/Turbine).

---

## Style & Conventions

### Language & Formatting
- Primary language: Kotlin. Prefer converting Java to Kotlin where feasible.
- Run formatting before commit: ./gradlew spotlessApply
- Follow standard Kotlin naming conventions.

### Architecture
- Pattern: MVP (Model-View-Presenter) with Moxy.
- Presenters: business logic; Views: "dumb" UI.
- Activities should inherit from InjectingMvpActivity.
- Tests that require DI should inherit from InjectingTest.

### Dependency Injection
- Use Toothpick for DI.
- Inject with `@Inject` and prefer provider-based presenter injection for testability (see pattern below).

### Async & Networking
- Use Kotlin Coroutines for asynchronous work.
- Use Retrofit + OkHttp for network calls.
- Use Glide for image loading.

### Immutability
- Prefer immutable data structures where possible.

---

## Testing Guidelines (must follow)
- Unit tests for all new features and bug fixes.
- Use MockK for mocking; prefer mockk(relaxed = true) for simple stubbing.
- Define mocks as properties in test classes and instantiate with mockk().

Correct mock example:
```kotlin
private val mockSharedPreferences: SharedPreferences = mockk(relaxed = true)
private val mockPresenter: MainPresenter = mockk(relaxed = true)
```

- Use @Before for setup and @After for teardown. Always call clearAllMocks() in @After.
  Example:
```kotlin
@Before
fun setUp() {
  every { mockSharedPreferences.getBoolean("serenity_first_run", true) } returns true
}

@After
fun tearDown() {
  activity.finish()
  clearAllMocks()
  Toothpick.reset()
}
```

- Static mocks (mockkStatic/mockkObject) should be set up and torn down in @BeforeClass/@AfterClass as required.
- Use Robolectric for Android-framework dependent tests.
- When converting tests to Kotlin, replace AssertJ assertions with AssertK equivalents.

### Toothpick rules for tests
- Extend us.nineworlds.serenity.test.InjectingTest.
- Override setUp() and call super.setUp() as the first action.
- Implement installTestModules() to bind test modules.
- Define an inner Module class that binds mocks to instances using bind(...).toInstance(...).
- Use scope.installTestModules() for installation.
- Trust the base InjectingTest for cleanup; add @After for additional teardown if needed.

---

## Converting Moxy MVP Activities (Java → Kotlin) — AI-Focused Pattern

Goal: make Activities testable and decoupled from concrete presenters.

Pattern summary:
1. DON'T use `@InjectPresenter` on fields.
2. DO inject a Toothpick Provider and use moxyPresenter delegate.

Example (Kotlin):
```kotlin
// 1. Inject a Provider for your presenter
@Inject
lateinit var presenterProvider: Provider<MainPresenter>

// 2. Use the moxyPresenter delegate to get the presenter from the provider.
internal val presenter by moxyPresenter { presenterProvider.get() }
```

Production DI (SerenityApplication.kt + Module):
```kotlin
// SerenityApplication.kt
class SerenityApplication : Application() {
    protected open fun inject() {
        val scope = Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE)
        scope.installModules(..., MainPresenterModule())
    }
}

// MainPresenterModule.kt
class MainPresenterModule : Module() {
    init {
        bind(MainPresenter::class.java).to(MainPresenter::class.java)
    }
}
```

Test DI (override binding to provide mock):
```kotlin
class MainActivityTest : InjectingTest() {
  private val mockPresenter: MainPresenter = mockk(relaxed = true)

  override fun installTestModules() {
    scope.installTestModules(TestingModule(), TestModule())
  }

  inner class TestModule : Module() {
    init {
      bind(MainPresenter::class.java).toInstance(mockPresenter)
    }
  }
}
```

---

## View Binding (conversions)
Replace findViewById with View Binding in Activities:

Correct usage for included layouts:
```kotlin
private lateinit var binding: ActivityMainBinding
private lateinit var progressBinding: IncludeLoadingProgressBinding

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    progressBinding = IncludeLoadingProgressBinding.bind(binding.root)
    setContentView(binding.root)

    mainMenuContainer = binding.mainGalleryMenu
    dataLoadingContainer = progressBinding.dataLoadingContainer
    setSupportActionBar(binding.actionToolbar)
}
```

---

## Examples (preserved from original)
- DO NOT use `@InjectPresenter` (Java).
- DO use Provider + moxyPresenter in Kotlin.
- Production/Test module examples included above.
- Unit test setUp/tearDown examples included above.
- View Binding example included above.

---

## Decision Checklist (agent must run through before making changes)
1. Is this change within a single, well-scoped area (UI, presenter, module, tests)? If no, ask.
2. Will this change modify environment files or secrets? If yes, stop and ask.
3. Does the change involve deleting files? If yes, ask for confirmation.
4. Will it require destructive git commands? If yes, ask for explicit written permission.
5. Have you run ./gradlew spotlessApply and relevant tests locally (or described why you couldn't)? If no, run or explain.

---

## Communication Templates (for agent prompts)
- If you detect a problem but need permission:
    - "I detected [problem]. Proposed fix: [short description]. Files: [explicit paths]. Risks: [brief]. Do you approve?"
- When proposing a PR-summary (what to include):
    1. Title: concise, scoped.
    2. Summary: what and why.
    3. Files changed: explicit paths.
    4. Tests: added/updated.
    5. Migration notes (if any).
    6. Manual verification steps.

---

## Appendix: Compact Reference

- Formatting: ./gradlew spotlessApply
- DI: Toothpick, bind(...) rules
- MVP: Moxy + provider + moxyPresenter
- Tests: MockK, Robolectric, InjectingTest base class
- Always ask before destructive actions.

---