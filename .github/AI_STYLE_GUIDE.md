# Serenity for Android - AI Assistant Style Guide

## Project Overview

This project is a media server client for Android, supporting both Plex and Emby servers. It's built with a focus on a clean, maintainable architecture, leveraging modern Android development practices.

## Core Frameworks

*   **MVP (Model-View-Presenter):** The application follows the MVP architectural pattern, using the **Moxy** framework to facilitate this. Presenters are responsible for business logic, while views (Activities and Fragments) are responsible for displaying data and handling user input.
*   **Dependency Injection:** **Toothpick** is used for dependency injection. It's used to provide dependencies to activities, fragments, presenters, and other components.
*   **Asynchronous Programming:** **Kotlin Coroutines** are used for asynchronous operations.
*   **Networking:** **Retrofit** and **OkHttp** are used for making network requests to the media servers.
*   **Image Loading:** **Glide** is used for loading and displaying images.
*   **Testing:**
    *   **Unit Testing:** **JUnit**, **Robolectric**, **AssertJ**, and **Turbine** are used for unit testing.
    *   **Mocking:** **MockK** is the primary mocking framework.

## Style Guide

*   **Language:** The project is primarily written in **Kotlin**. New code should be written in Kotlin, and existing Java code should be migrated to Kotlin where feasible.
*   **Code Formatting:** The project uses **Spotless** to enforce code formatting. Before committing any code, run `./gradlew spotlessApply` to ensure that the code is properly formatted.
*   **Architecture:** Adhere to the MVP pattern. Business logic should reside in presenters, and views should be as "dumb" as possible.
*   **Dependency Injection:** Use Toothpick for dependency injection. Inject dependencies into activities, fragments, and presenters using `@Inject`.
*   **Asynchronous Operations:** Use Kotlin Coroutines for all asynchronous operations.
*   **Immutability:** Prefer immutable data structures where possible.
*   **Testing:**
    *   Write unit tests for all new features and bug fixes.
    *   Use MockK for mocking dependencies in unit tests.
    *   Use Robolectric to test Android framework-dependent code.
*   **Naming Conventions:**
    *   Follow the standard Kotlin naming conventions.
    *   Name presenters with the `Presenter` suffix (e.g., `MainPresenter`).
    *   Name views with the `View` suffix (e.g., `MainView`).

---

### AI Style Guide: Converting Moxy MVP Activities from Java to Kotlin

This guide outlines the standard patterns to follow when converting a Java `Activity` that uses Moxy for its presentation layer and Toothpick for dependency injection to Kotlin. The primary goal is to refactor the class to allow a mock `Presenter` to be injected during testing, improving testability. The conversion of `MainActivity` and the patterns established in `LoginUserActivity` serve as the blueprint.

#### 1. The Core Pattern: Refactor Presenter Injection

The most critical change is to move away from Moxy's field annotation in favor of a provider-based approach that gives us control over the presenter's instantiation.

**DON'T:** Use the `@InjectPresenter` annotation. This couples the Activity directly to a concrete presenter implementation, which is difficult to replace in a test environment.

*Old Java Way (`@InjectPresenter`):*
'''java
@InjectPresenter
MainPresenter presenter;
'''

**DO:** Inject a `Provider` for the presenter using Toothpick and use the `moxyPresenter` Kotlin delegate to manage the presenter's lifecycle.

*New Kotlin Way (`Provider` + `moxyPresenter` delegate):*
'''kotlin
// In your Activity class:

// 1. Inject a Provider for your presenter
@Inject
lateinit var presenterProvider: Provider<MainPresenter>

// 2. Use the moxyPresenter delegate to get the presenter from the provider.
//    This ensures Moxy correctly handles lifecycle events.
internal val presenter by moxyPresenter { presenterProvider.get() }
'''

#### 2. Configure Dependency Injection with Toothpick

The provider-based pattern works by having different modules for production and testing.

**Production Configuration (`SerenityApplication.kt`)**

In the main application class, a Toothpick module is installed that binds the presenter interface to its concrete implementation. This is how the real presenter is provided to the `Provider<MainPresenter>`.

*Example (`SerenityApplication.kt` and `MainPresenterModule.kt`):*
'''kotlin
// SerenityApplication.kt
class SerenityApplication : Application() {
    protected open fun inject() {
        val scope = Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE)
        // The real module is installed for the production app.
        scope.installModules(..., MainPresenterModule())
        ...
    }
}

// MainPresenterModule.kt
class MainPresenterModule : Module() {
    init {
        // This tells Toothpick how to create a MainPresenter.
        bind(MainPresenter::class.java).to(MainPresenter::class.java)
    }
}
'''

**Test Configuration (`MainActivityTest.kt`)**

In a test class, a **different** module (`TestModule`) is installed. This test-specific module overrides the binding from the production module, telling Toothpick to provide a mock instance instead of the real one.

*Example (`MainActivityTest.kt`):*
'''kotlin
class MainActivityTest : InjectingTest() {

  // 1. Create a mock instance of the presenter.
  private val mockPresenter: MainPresenter = mockk(relaxed = true)

  // ... setUp and other tests

  // 2. Override the module installation from the base test class.
  override fun installTestModules() {
    // Install a test-specific module that provides the mock.
    scope.installTestModules(TestingModule(), TestModule())
  }

  // 3. The TestModule binds the presenter class to the mock instance.
  inner class TestModule : Module() {
    init {
      // ... other test bindings
      bind(MainPresenter::class.java).toInstance(mockPresenter)
    }
  }
}
'''

#### 3. Modernize View Access with View Binding

As part of the conversion to Kotlin, all `findViewById` calls must be replaced with View Binding.

**DO:** Use `ActivityMainBinding.inflate()` to create a binding instance. For layouts that use the `<include>` tag, create a second binding instance for the included layout by using its static `bind()` method, as demonstrated in `LoginUserActivity` and `MainActivity`.

*Correct View Binding for Included Layouts:*
'''kotlin
// Declare binding properties for the main layout and the included one.
private lateinit var binding: ActivityMainBinding
private lateinit var progressBinding: IncludeLoadingProgressBinding

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Inflate the main binding and bind the included layout to its root.
    binding = ActivityMainBinding.inflate(layoutInflater)
    progressBinding = IncludeLoadingProgressBinding.bind(binding.root)
    setContentView(binding.root)

    // Access views from the correct binding object.
    mainMenuContainer = binding.mainGalleryMenu
    dataLoadingContainer = progressBinding.dataLoadingContainer
    setSupportActionBar(binding.actionToolbar)
}
'''

#### 4. Use Correct Base Classes

-   **Activities** must inherit from `InjectingMvpActivity`.
-   **Tests** must inherit from `InjectingTest`.

These base classes contain the necessary setup logic for Toothpick scope management.
