# Architecture Standards

## Mission
Maintain a clean, testable, and modular MVP architecture that scales across media server providers (Emby, Jellyfin, Plex).

## Module Boundaries
- **`:serenity-app`**: UI (Activities, Fragments, Adapters), Presenters, and Glue logic.
- **`:emby-lib` / `:jellyfin-lib`**: Provider-specific API communication and mapping.
- **`:serenity-common`**: Shared models and interfaces (Pure Kotlin).
- **`:serenity-android-common`**: Shared Android-specific utilities.

## Core Rules & Non-Negotiables
- **Presenter Separation**: Presenters MUST interact with Repositories. Presenters are prohibited from talking to API clients or `SerenityClient` directly.
- **Dependency Injection**: Use Toothpick. Most services should be in `APPLICATION_SCOPE`.
- **Concurrency**: Use Coroutines. Never block the Main thread. Use `Dispatchers.IO` for I/O.
- **Networking**: NEVER use Retrofit's `.enqueue()`. Use `suspend` functions.
- **Base Classes**: Activities should extend `InjectingActivity` or `InjectingMvpActivity`.

## Data Flow (Golden Standard)
1. **View** -> **Presenter**
2. **Presenter** -> **Repository** (suspend)
3. **Repository** (`Dispatchers.IO`) -> **API Client**
4. **API Client** -> Returns Data/Throws Exception
5. **Presenter** -> Handles result and updates **View**.

## Implementation Guide
See `architecture-core` skill for repository patterns, module boundaries, and DI entry point details.
