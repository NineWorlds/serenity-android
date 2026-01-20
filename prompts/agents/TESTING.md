## Testing Guidelines (MockK Only)
- **MockK Mandatory**: Use MockK for all mocking. No other libraries.
- **NO Annotations**: Do not use `@MockK`. Initialize via `mockk(relaxed = true)`.
- **Tear Down**: Always call `clearAllMocks()` and `Toothpick.reset()` in `@After`.
- **DI in Tests**: Extend `InjectingTest` and bind mocks in `installTestModules`.

### Proper Mock Setup
- Define mocks as properties, initialized with `mockk(relaxed = true)`.
- Example:
    ```kotlin
    private val mockSharedPreferences: SharedPreferences = mockk(relaxed = true)
    private val mockPresenter: MainPresenter = mockk(relaxed = true)
    ```
- Use `every` for expectations and `verify` for assertions.

### Set Up & Tear Down
Define setup and cleanup routines explicitly:
- Ensure all mocks are cleared in `@After` blocks:
    ```kotlin
    @After
    fun tearDown() {
        clearAllMocks()
        Toothpick.reset()
    }
    ```

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