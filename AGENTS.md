# Serenity for Android - AI Assistant Style Guide

## Purpose & Scope
This document outlines mandatory rules and best practices for AI agents interacting with the Serenity for Android repository. It includes hard constraints (absolute no-go areas), required collaboration rules, and a clear framework for agents to operate safely and effectively.

**Intended audience:**
- Automated agents (bots) assisting with code generation, testing, or repository interactions.
- Engineers reviewing AI suggestions to ensure compliance.
- Repository reviewers verifying that automation aligns with project policies.

---

## Quick Start (for Agents)
AI agents must comply with these mandatory rules:
1. **Permission First**: Always halt and request explicit approval before taking any action.
2. **Action Plan Required**: Before proceeding, present a well-defined action plan that includes:
    - A short summary of the proposed change.
    - Files affected (explicit paths).
    - Reasoning, risks, and benefits.
    - Tests added, updated, or validated.
3. **Hard Constraints**: Strictly adhere to the constraints outlined in the "Hard Constraints" section below.
4. **Manual Verification Readiness**: Ensure that the proposal includes clear manual verification steps.

---

## Hard Constraints (Mandatory - No Exceptions)
Agents must not deviate from the following rules under any circumstances:
- **DO NOT act without explicit permission**: Always await written user approval for any operation.
- **DO NOT edit critical configuration or environment files**, such as `.env`.
- **DO NOT perform destructive Git operations** (e.g., `git reset --hard`, `rm`, `git checkout`, `git restore`) unless explicitly directed and approved.
- **DO NOT delete files** to resolve a failure without prior approval.
- **DO NOT amend commits** unless explicitly instructed in writing.
- **Coordinate changes**: Before modifying, restoring, or reverting work not authored by the agent, coordinate with the responsible party.
- **Stay scoped**: Operate exclusively within the defined task scope. Do not address other issues or errors unless explicitly requested.

---

## Git Interaction Rules
Agents must follow these explicit Git workflow rules:
- **Path-Scoped Commits**: Commit only the files you modified.
    - Commit tracked files with:
        ```bash
        git commit -m "<scoped message>" -- path/to/file1 path/to/file2
        ```
    - Add and commit new files explicitly:
        ```bash
        git add path/to/new/file && git commit -m "<message>"
        ```
- **Quoted Paths**: Files with special characters (e.g., brackets/parentheses) must be enclosed in quotes.
- **Rebases without Editors**: Use non-interactive modes for rebasing:
    ```bash
    GIT_EDITOR=: GIT_SEQUENCE_EDITOR=: git rebase --no-edit
    ```
- **Follow Coordination Protocols**: Coordinate with peers before renaming, moving, or restoring files not authored by the agent.

---

## Collaboration & Decision Protocols
Agents must adhere to a strict permission-based workflow:
1. **Raise Issues Proactively**: If a problem is identified, halt execution and notify the user with a concise description, proposed solution, and risks.
2. **Define How Before Proceeding**: Provide a clear breakdown of how the agent plans to execute any task.
3. **Seek Explicit Approval**: Proceed only after receiving written user consent for actions beyond small, well-scoped edits.

---

## Project Overview
Serenity for Android is a media server client for Android, integrating Plex and Emby. It uses modern Android development practices, including:
- MVP architecture with Moxy.
- Dependency injection with Toothpick.
- Reactive programming with Kotlin Coroutines and Retrofit/OkHttp.
- Robolectric for framework-dependent testing.
- MockK for all mocking needs.
- Glide for image loading.

Agents must respect these conventions and ensure compliance with repository standards.

---

## Code Style and Best Practices

### Language & Formatting
- Use **Kotlin** as the primary language. Convert Java code to Kotlin where feasible.
- Enforce proper formatting using:
    ```bash
    ./gradlew spotlessApply
    ```
- Follow Kotlin naming conventions and style guidelines.

### Dependency Injection (DI)
- Use Toothpick for DI.
- Prefer provider-based injection for testing; see usage examples below.

### Testing Guidelines (Enforced Rules)
Agents must adhere to these unit testing requirements:
1. **Mandatory Usage of MockK**: All mocking operations must use MockK. No other mocking libraries are permitted.
2. **DO NOT USE Mockk Annotations**: Prefer field level creation of the mocks directly.
2. **Unit Test Scopes**: Write unit tests for all new features, bug fixes, or critical code changes.
3. **Proper Mock Setup**:
    - Define mocks as properties, initialized with `mockk(relaxed = true)`.
    - Example:
        ```kotlin
        private val mockSharedPreferences: SharedPreferences = mockk(relaxed = true)
        private val mockPresenter: MainPresenter = mockk(relaxed = true)
        ```
    - Use `every` for expectations and `verify` for assertions.
4. **Set Up & Tear Down**: Define setup and cleanup routines explicitly:
    - Ensure all mocks are cleared in `@After` blocks:
        ```kotlin
        @After
        fun tearDown() {
            clearAllMocks()
            Toothpick.reset()
        }
        ```
    - Use `@BeforeClass` and `@AfterClass` for static mocks.

### Toothpick Rules for Tests
- Extend `us.nineworlds.serenity.test.InjectingTest`.
- Override `setUp` and call `super.setUp()` as the first action.
- Bind mocks to instances in `installTestModules`:
    ```kotlin
    inner class TestModule : Module() {
        init {
            bind(MainPresenter::class.java).toInstance(mockPresenter)
        }
    }
    ```

---

## Converting Moxy MVP Activities (Java → Kotlin)
To make Activities testable and decoupled:
1. **DO NOT use `@InjectPresenter` on fields.**
2. **DO inject Providers** and use `moxyPresenter` delegate.

Example:
```kotlin
// Inject Provider and use moxyPresenter
@Inject
lateinit var presenterProvider: Provider<MainPresenter>

internal val presenter by moxyPresenter { presenterProvider.get() }
```

---

## View Binding Standards
Always replace `findViewById` with View Binding. Example:
```kotlin
private lateinit var binding: ActivityMainBinding
private lateinit var progressBinding: IncludeLoadingProgressBinding

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    progressBinding = IncludeLoadingProgressBinding.bind(binding.root)
    setContentView(binding.root)
}
```

---

## Agent Decision Checklist
Agents must follow this checklist before acting:
1. **Task Scope**: Is the change small and well-defined? If not, request clarification.
2. **Permission First**: Have you received explicit user approval? If not, halt.
3. **Environment Awareness**: Are critical files or destructive operations involved? If yes, halt and request further instructions.
4. **Tests & Verification**: Have you ensured formatting and tests are valid? Document known issues clearly if they cannot be resolved.
5. **Coordination**: Have you ensured no conflicts with other agents? Coordinate before proceeding.

---

## Communication Templates
Use these templates to communicate effectively:

1. **Raising an Issue**:
    - "I detected [problem]. Proposed fix: [summary]. Files: [paths]. Risks: [brief]. Do you approve?"
2. **Proposing Changes**:
    - Include the following in PR summaries:
        1. Title (concise and scoped).
        2. Description (purpose and reasoning).
        3. Explicit files changed.
        4. Tests added or updated.
        5. Manual verification steps.

---

## Appendix: Compact References
- **Formatting**: `./gradlew spotlessApply`
- **Injection**: Toothpick rules.
- **MVP**: Moxy + Kotlin delegation.
- **Testing**:
    - Mocking: MockK only.
    - Test DI: `InjectingTest` and `injectTestModules`.

---
