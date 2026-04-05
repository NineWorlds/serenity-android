---
name: code-reviewer
description: Expertise in reviewing Serenity code for architectural alignment, security, performance, and testing standards.
---

# Code Review Criteria

## Architectural Review
- **MVP Pattern (Moxy)**:
    - Views (`Activity`/`Fragment`) must be "dumb" and passive.
    - Business logic, state, and data fetching must be in the `Presenter`.
- **DI (Toothpick)**:
    - dependencies provided via `Module`.
    - Use `Provider<Presenter>` and `moxyPresenter` delegate (NOT `@InjectPresenter`).
    - Correct scoping (e.g., `APPLICATION_SCOPE`).
    - `@InjectConstructor` usage.
- **Repository Pattern**: Presenters must use Repositories, not API clients directly.

## Security & Reliability
- **Data Handling**: No sensitive data in logs/hardcoded. Input validation.
- **Networking**: HTTPS via Retrofit.
- **Error Handling**: Use `executeOrThrow()` for consistent propagation.
- **Prohibited**: No Retrofit `.enqueue()`. Use Coroutines.

## Performance
- **Concurrency**: Heavy ops on `Dispatchers.IO`/`Default`. Correct `CoroutineScope` management.
- **Resource Management**: No Context leaks. Proper unregistration of listeners.
- **UI**: View Binding used (no `findViewById`). Glide for images. XML animations (no `MotionLayout`).
- **Leanback**: D-pad focus effects are mandatory.

## Testing Standards
- **MockK**: Class properties, `mockk(relaxed = true)`, NO annotations.
- **Cleanup**: `clearAllMocks()` and `Toothpick.reset()` in `@After`.
- **Structure**: `InjectingTest` for DI tests. Mock data in `resources/mock-data/` with `provider_endpoint_params.json` naming.

## Style & Compliance
- **Kotlin**: Idiomatic code, `val` and immutable collections preferred.
- **Formatting**: `./gradlew spotlessApply` compliance.
- **Constraints**: Adherence to `CONSTRAINTS.md`.
