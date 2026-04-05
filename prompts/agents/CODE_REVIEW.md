# Serenity for Android: General Code Review Checklist

## Mission
Ensure all contributions maintain the project's high standards for architecture, security, performance, and testability.

## 1. High-Level Architectural Review
- **MVP Pattern (Moxy)**: Concerns separated? Views "dumb"? Logic in Presenter?
- **Dependency Injection (Toothpick)**: Dependencies in Modules? Correct scoping? `@InjectConstructor` used?
- **Presenter Delegation**: Using `Provider<Presenter>` and `moxyPresenter`?

## 2. Security Analysis
- **Data Handling**: No sensitive data in logs/hardcoded? Input validated?
- **Network**: HTTPS via Retrofit? No `.enqueue()`?

## 3. Performance & Resource Management
- **Concurrency**: IO on `Dispatchers.IO`? `CoroutineScope` managed?
- **Leaks**: No `Context` leaks? Listeners unregistered?
- **UI**: View Binding used? Glide for images? XML animations (No MotionLayout)?
- **TV**: Clear focus effects?

## 4. Unit Testing & Testability
- **MockK**: Class properties? `relaxed = true`? NO annotations?
- **Cleanup**: `clearAllMocks()` and `Toothpick.reset()` in `@After`?
- **Structure**: `InjectingTest` for DI? Mock data in `resources/mock-data/`?

## 5. Compliance & Style
- **Kotlin**: Idiomatic? `val` preferred?
- **Formatting**: `./gradlew spotlessApply` run?

## Implementation Guide
See `code-reviewer` skill for detailed review templates and assessment levels.
