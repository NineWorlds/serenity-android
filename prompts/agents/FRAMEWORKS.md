# Frameworks & Core Libraries Standards

## Mission
Maintain consistency across the project by adhering to established framework patterns and ensuring seamless integration of new code with the existing codebase.

## Code Style
- **Language**: Use Kotlin. Convert Java to Kotlin where feasible.
- **Formatting**: Always run `./gradlew spotlessApply`.
- **Immutability**: Prefer `val` and immutable collections (`List`, `Set`) over `var` and mutable collections.

## Dependency Injection (Toothpick)
- **Standard**: Use Toothpick; prefer provider-based injection.
- **Scoping**: Most services and repositories should be bound in `APPLICATION_SCOPE` (within `SerenityApplication`).
- **InjectConstructor**: Use `@InjectConstructor` on classes where applicable to simplify DI.
- **Manual Injection**: In Presenters or other components where constructor injection is not feasible, use manual injection in the `init` block: `Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE))`.

## MVP (Moxy)
- **Standard**: Use Kotlin delegation for presenters.
- **Hard Constraint**: NEVER use `@InjectPresenter`.
- **Pattern**: Inject a `Provider<Presenter>` and use the `moxyPresenter` delegate.

```kotlin
@Inject
lateinit var presenterProvider: Provider<MainPresenter>

internal val presenter by moxyPresenter { presenterProvider.get() }
```

## View Binding
- **Standard**: Always replace `findViewById` with View Binding. Mandatory for new UI and refactors.
- **Standard**: Handle included layouts using their generated binding classes (e.g., `IncludeLoadingProgressBinding.bind(binding.root)`).

## Networking & Persistence
- **Retrofit**: Use for all API communications. Use `executeOrThrow()` for consistent error handling.
- **Constraint**: DO NOT use `.enqueue()`. Use `suspend` functions in repositories.
- **Moshi**: Standard for JSON parsing. Use `@Json(name = "...")` for mapping API fields to domain properties.
- **Glide**: Use for all image loading and caching.

## Animations
- **Constraint**: **DO NOT use MotionLayout**. Stick to XML animations in `res/anim`.

## Testing & Mocking
- **MockK**: Mandatory for all mocking. No annotations allowed (`mockk(relaxed = true)`).
- **Tear Down**: Always call `clearAllMocks()` and `Toothpick.reset()` in `@After`.
- **InjectingTest**: Extend `us.nineworlds.serenity.test.InjectingTest` for tests requiring DI (field injection).
