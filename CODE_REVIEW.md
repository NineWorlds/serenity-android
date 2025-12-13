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

**Findings:**
* `{Observations on MVP and DI patterns. Note any deviations from the AGENTS.md guide.}`

### 1.2. Component & Module Design
- **Single Responsibility:** Does each new/modified class have a single, clear purpose?
- **Immutability:** Are `val` and immutable collections (`List`, `Set`) preferred over `var` and mutable collections? [1]
- **Modularity:** Are new components reasonably decoupled to facilitate future maintenance and testing?

**Findings:**
* `{Observations on component design and code organization.}`

---

## 2. Security Analysis

### 2.1. Data Handling & Storage
- **Sensitive Data:** Is any sensitive information (API keys, user data, tokens) being logged, hardcoded, or stored insecurely? Hardcoded secrets are a critical violation.
- **Input Validation:** Is all external input (e.g., from `Intent` extras, network responses, user entry) properly validated to prevent crashes or exploits?
- **Permissions:** If new Android permissions are requested, are they justified and handled with the appropriate runtime checks?

**Findings:**
* `{Security-related observations. Mention any missing input validation or handling of sensitive data.}`

### 2.2. Network & Communication
- **Secure Transport:** Are all network calls made over HTTPS via Retrofit?
- **API Key Management:** Are API keys and secrets managed securely and not hardcoded?

**Findings:**
* `{Observations on network security. Confirm HTTPS usage and secure secret management.}`

---

## 3. Performance & Resource Management

### 3.1. Main Thread & Concurrency
- **Main Thread Blocking:** Are there any long-running operations (I/O, heavy computation, network calls) on the main thread? All such work must be delegated to a background thread.
- **Coroutines Usage:** Is asynchronous work handled with Kotlin Coroutines? Are `CoroutineScope`s (e.g., `viewModelScope`, `lifecycleScope`) managed correctly to prevent leaks? [1]

**Findings:**
* `{Performance observations, especially regarding main thread usage and coroutine implementation.}`

### 3.2. Memory & Resource Leaks
- **Context Leaks:** Is any `Activity` or `Fragment` `Context` passed to a long-lived object (e.g., a singleton or Presenter)?
- **Resource Cleanup:** Are resources like `BroadcastReceiver`s, `Cursor`s, or streams properly closed or unregistered in the appropriate lifecycle methods (`onStop`, `onDestroy`, `onCleared`)?
- **View Binding:** If `findViewById` is being converted, is View Binding being used correctly, especially for included layouts? [1]

**Findings:**
* `{Memory-related observations. Note potential leaks or incorrect resource handling.}`

### 3.3. UI & Rendering
- **Layout Efficiency:** Are XML layouts overly nested? Could `ConstraintLayout` be used more effectively?
- **Image Loading:** Is Glide used for all image loading to leverage its caching and memory management capabilities? [1]

**Findings:**
* `{UI performance observations, focusing on layout complexity and image loading strategy.}`

---

## 4. Unit Testing & Testability

### 4.1. Test Coverage & Quality
- **Test Presence:** Does new or modified logic have corresponding unit tests? All new features and fixes require them. [1]
- **Assertion Quality:** Are assertions specific and meaningful? (e.g., asserting specific state or behavior, not just `isNotNull`).
- **Mocking Strategy (MockK):**
    - Are mocks created as class properties using `mockk(relaxed = true)`?
    - Are `clearAllMocks()` and `Toothpick.reset()` called in a teardown method (`@After`)? [1]

**Findings:**
* `{Testing observations. Note missing tests, weak assertions, or incorrect mocking patterns.}`

### 4.2. Test Structure
- **Base Class:** Do tests requiring DI correctly extend `InjectingTest`? [1]
- **DI in Tests:** Is a local `TestModule` used to `bind(...).toInstance(mockObject)` for dependencies, and is it installed via `installTestModules()`? `scope.inject()` should not be called directly. [1]
- **Setup/Teardown:** Is `@Before` used for setup and `@After` for teardown? Does `setUp()` call `super.setUp()`? [1]

**Findings:**
* `{Observations on test structure and DI. Confirm adherence to the AGENTS.md testing guidelines.}`

---

## 5. Compliance & Style

- **Kotlin Conventions:** Does the code follow standard Kotlin idioms and naming conventions?
- **Formatting:** Has `./gradlew spotlessApply` been run? The code should be properly formatted. [1]
- **`AGENTS.md` Adherence:** Does the change comply with all rules and decision points in the agent style guide, especially regarding Git usage and asking for permission? [1]

**Findings:**
* `{General compliance and style notes. Confirm formatting and adherence to project-wide rules.}`

---

## Actionable Recommendations

*   **MUST FIX:** `{List of critical issues, such as security flaws, crashes, or direct violations of AGENTS.md hard constraints.}`
*   **SHOULD FIX:** `{List of important issues, such as performance problems, architectural deviations, or missing tests.}`
*   **CONSIDER:** `{List of suggestions for improvement, like code clarity, minor refactors, or stronger test assertions.}`
