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
