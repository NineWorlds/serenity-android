## Testing Guidelines (MockK Only)

## Mission
Ensure high reliability and regression safety through standardized, decoupled unit tests using MockK and JUnit 4.

## Core Rules
- **MockK Mandatory**: Use MockK for all mocking. No other libraries.
- **NO Annotations**: Do not use `@MockK`. Initialize via `mockk(relaxed = true)`.
- **Tear Down**: Always call `clearAllMocks()` and `Toothpick.reset()` in `@After`.
- **Standard JUnit 4**: Use standard JUnit 4 for tests that do not require dependency injection (i.e., the class under test uses constructor injection).

## DI in Tests (InjectingTest)
- **Requirement**: Extend `us.nineworlds.serenity.test.InjectingTest` ONLY if the class under test uses field-level injection (common in Activities/Fragments/some Presenters).
- **Setup**: Override `setUp` and call `super.setUp()` as the first action.
- **Binding**: Bind mocks to instances in `installTestModules` using a local `TestModule`.

### Proper Mock Setup
- Define mocks as properties, initialized with `mockk(relaxed = true)`.
```kotlin
private val mockSharedPreferences: SharedPreferences = mockk(relaxed = true)
private val mockPresenter: MainPresenter = mockk(relaxed = true)
```

### Toothpick Rules for Tests
```kotlin
inner class TestModule : Module() {
    init {
        bind(MainPresenter::class.java).toInstance(mockPresenter)
    }
}
```

### Cleanup
Ensure all mocks and DI scopes are cleared:
```kotlin
@After
fun tearDown() {
    clearAllMocks()
    Toothpick.reset()
}
```
