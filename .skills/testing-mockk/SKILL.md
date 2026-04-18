---
name: testing-mockk
description: "Trigger: Unit Testing. Writing unit tests using MockK (no annotations), constructor injection, and InjectingTest patterns."
---

# Testing with MockK & Toothpick

## Mock Setup (No Annotations)
Standardized mocking requires explicit initialization to avoid reflection overhead and maintain consistency.

```kotlin
class MyPresenterTest {
    private val mockView: MyView = mockk(relaxed = true)
    private val mockRepo: MyRepository = mockk(relaxed = true)
    
    // ...
}
```

## Toothpick DI in Tests
When testing components that rely on field injection (Activities, Fragments), use the `TestModule` pattern.

### InjectingTest Base Class
Extend `us.nineworlds.serenity.test.InjectingTest` and install your mocks.

```kotlin
class MyActivityTest : InjectingTest() {
    private val mockPresenter: MyPresenter = mockk(relaxed = true)

    override fun installTestModules() {
        scope.installModules(TestModule())
    }

    inner class TestModule : Module() {
        init {
            bind(MyPresenter::class.java).toInstance(mockPresenter)
        }
    }

    @Before
    override fun setUp() {
        super.setUp()
        // Activity/Fragment initialization logic
    }
}
```

## Tear Down & Cleanup
Crucial for preventing state leakage between tests.

```kotlin
@After
fun tearDown() {
    clearAllMocks()
    Toothpick.reset()
}
```

## Standard JUnit 4 Setup
For pure logic classes (Repositories, Use Cases), use constructor injection and standard JUnit 4.

```kotlin
class MyRepositoryTest {
    private val mockApi: MyApi = mockk(relaxed = true)
    private lateinit var repository: MyRepository

    @Before
    fun setUp() {
        repository = MyRepository(mockApi)
    }
}
```
