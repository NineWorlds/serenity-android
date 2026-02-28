# Architecture Standards

## Mission
Maintain a clean, testable, and modular MVP architecture that scales across media server providers (Emby, Jellyfin, Plex).

## Directory Structure & Module Boundaries
- **`:serenity-app`**: The main Android application. Contains UI (Activities, Fragments, Adapters), Presenters, and Glue logic.
    - `ui/`: View components following the MVP pattern.
    - `core/`: Business logic, domain models, and repositories.
- **`:emby-lib` / `:jellyfin-lib`**: Provider-specific implementations. These libraries handle API communication and data mapping for their respective servers.
- **`:serenity-common`**: Pure Kotlin module for shared models and interfaces.
- **`:serenity-android-common`**: Shared Android-specific utilities and base classes.

## Data Flow (Golden Standard)
1. **View** (`Activity`/`Fragment`) calls a method on the **Presenter**.
2. **Presenter** calls a `suspend` function in a **Repository**.
3. **Repository** switches to `Dispatchers.IO` and calls the **API Client** (e.g., `EmbyAPIClient`).
4. **API Client** executes the network request and returns data or throws a typed exception.
5. **Presenter** handles the result (via Coroutine scope) and updates the **View**.

## Non-Negotiables
- **Presenters MUST interact with Repositories**: Presenters are prohibited from talking to API clients or `SerenityClient` directly.
- **API Clients** must be injected into Repositories.
- **Concurrency**: Use Coroutines. Never block the Main thread. Always use `withContext(Dispatchers.IO)` in repositories for I/O operations.
- **Scoping**: Use Toothpick for DI. Most services should be in `APPLICATION_SCOPE`.

## Code Example: Repository Pattern
```kotlin
@InjectConstructor
class VideoRepository constructor(private val client: SerenityClient) {

    suspend fun fetchItemById(itemId: String): IMediaContainer = withContext(Dispatchers.IO) {
        client.fetchItemById(itemId)
    }
}
```
