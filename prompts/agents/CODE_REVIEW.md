# Serenity for Android: General Code Review

## Report Summary

**Change ID:** `{git_commit_hash_or_pr_number}`
**Author:** `{author_name}`
**Agent:** Senior Android Reviewer Agent
**Overall Assessment:** `{Choose one: "Approved", "Approved with Suggestions", "Requires Discussion", "Changes Requested"}`

---

## 1. High-Level Architectural Review

### 1.1. Adherence to Project Architecture (MVP & DI)
- **MVP Pattern (Moxy):** Does the change correctly separate concerns?
    - Are Views (`Activity`/`Fragment`) kept "dumb" and passive?
    - Is all business logic, state management, and data fetching located within a `Presenter`?
- **Dependency Injection (Toothpick):**
    - Are new dependencies correctly provided via a `Module`?
    - For `Activity`/`Fragment` components, does the change use the `Provider<Presenter>` and `moxyPresenter` delegate pattern instead of `@InjectPresenter`? [1]
    - Are new bindings correctly scoped (e.g., `APPLICATION_SCOPE`)?
    - Is `@InjectConstructor` used where possible for automatic injection?

**Findings:**
* `{Observations on MVP and DI patterns. Note any deviations from the AGENTS.md guide.}`

### 1.2. Component & Module Design
- **Single Responsibility:** Does each new/modified class have a single, clear purpose?
- **Immutability:** Are `val` and immutable collections (`List`, `Set`) preferred over `var` and mutable collections? [1]
- **Modularity:** Are new components reasonably decoupled? Does it follow the Repository pattern?

**Findings:**
* `{Observations on component design and code organization.}`

---

## 2. Security Analysis

### 2.1. Data Handling & Storage
- **Sensitive Data:** Is any sensitive information being logged or hardcoded? Hardcoded secrets are a critical violation.
- **Input Validation:** Is all external input properly validated?

**Findings:**
* `{Security-related observations.}`

### 2.2. Network & Communication
- **Secure Transport:** Are all network calls made over HTTPS via Retrofit?
- **Prohibited Patterns:** Does it use Retrofit's `.enqueue()`? (Forbidden - must use `suspend` + Coroutines). [1]
- **Reliability:** Does it use `executeOrThrow()` for consistent error handling? [1]

**Findings:**
* `{Observations on network security and communication standards.}`

---

## 3. Performance & Resource Management

### 3.1. Main Thread & Concurrency
- **Main Thread Blocking:** Are heavy operations delegated to `Dispatchers.IO` or `Dispatchers.Default`?
- **Coroutines Usage:** Are `CoroutineScope`s managed correctly to prevent leaks? [1]

**Findings:**
* `{Performance observations.}`

### 3.2. Memory & Resource Leaks
- **Context Leaks:** Is any `Activity`/`Fragment` `Context` leaking into long-lived objects?
- **Resource Cleanup:** Are listeners/receivers properly unregistered?
- **View Binding:** Is View Binding used correctly? Is `findViewById` avoided? [1]

**Findings:**
* `{Memory-related observations.}`

### 3.3. UI & Rendering
- **Layout Efficiency:** Are layouts optimized?
- **Image Loading:** Is Glide used for all image loading? [1]
- **Animations:** Does it use `MotionLayout`? (Forbidden - must use XML animations). [1]
- **TV Optimization:** Does the UI provide clear focus effects for D-pad navigation? [1]

**Findings:**
* `{UI performance and Leanback standard observations.}`

---

## 4. Unit Testing & Testability

### 4.1. Test Coverage & Quality
- **Test Presence:** Does new/modified logic have corresponding unit tests?
- **Mocking Strategy (MockK):**
    - Are mocks created as class properties using `mockk(relaxed = true)`?
    - **Prohibited:** Are annotations like `@MockK` or `@RelaxedMockK` used? (Forbidden). [1]
    - Are `clearAllMocks()` and `Toothpick.reset()` called in `@After`? [1]

**Findings:**
* `{Testing observations.}`

### 4.2. Test Structure
- **Base Class:** Do tests requiring DI correctly extend `InjectingTest`? [1]
- **Mock Data:** Is mock JSON data placed in the correct `resources/mock-data/` directory using the `provider_endpoint_params.json` convention? [1]

**Findings:**
* `{Observations on test structure and DI.}`

---

## 5. Compliance & Style

- **Kotlin Conventions:** Does the code follow standard Kotlin idioms?
- **Formatting:** Has `./gradlew spotlessApply` been run? [1]
- **`AGENTS.md` Adherence:** Does the change comply with all "Hard Constraints"? [1]

**Findings:**
* `{General compliance and style notes.}`

---

## Actionable Recommendations

*   **MUST FIX:** `{List of critical issues or direct violations of AGENTS.md hard constraints.}`
*   **SHOULD FIX:** `{Important issues like architectural deviations or missing tests.}`
*   **CONSIDER:** `{Suggestions for improvement.}`
