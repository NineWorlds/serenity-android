---
name: architecture-core
description: Implementation expertise for Serenity's MVP architecture, Repository pattern, and Coroutines.
---

# Architecture Core & Data Flow

## Repository Pattern
Repositories are the single source of truth for data. They abstract the data source (API, Cache) from the Presenter.

### Implementation Template
```kotlin
@InjectConstructor
class VideoRepository(private val client: SerenityClient) {

    suspend fun fetchItemById(itemId: String): IMediaContainer = withContext(Dispatchers.IO) {
        client.fetchItemById(itemId)
    }
}
```

## Presenter Logic (Moxy)
Presenters manage View state and coordinate data flow.

### Handling Async Work
Use `viewModelScope` (or equivalent in Moxy) to launch coroutines.

```kotlin
fun loadData() {
    launch {
        try {
            val data = repository.fetchData()
            viewState.displayData(data)
        } catch (e: Exception) {
            viewState.showError(e.message)
        }
    }
}
```

## Module Boundaries
- `:serenity-app`: UI and Presenters.
- `:emby-lib` / `:jellyfin-lib`: API Clients and Provider-specific logic.
- `:serenity-common`: Shared POJOs and Interfaces.

## DI Entry Points (Toothpick)
- `SerenityApplication`: Global scope (`APPLICATION_SCOPE`).
- `InjectingActivity` / `InjectingMvpActivity`: Base classes for DI support.
