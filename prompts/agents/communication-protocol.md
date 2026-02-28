# Communication Protocol Standards

## Mission
Ensure reliable and observable communication between the application and media servers.

## API Design & Implementation
- **Retrofit**: All network calls must use Retrofit 2 interfaces.
- **Service Interfaces**: Group related endpoints into specific service interfaces (e.g., `UsersService`, `FilterService`).
- **Synchronous Execution**: Use `.execute()` within Coroutine-wrapped repository methods rather than Retrofit's `.enqueue()`.

## Error Handling & Reliability
- **Execute or Throw**: Use the `executeOrThrow()` extension pattern for all `Call<T>` objects to ensure consistent error propagation.
- **Exceptions**: Throw `IOException` for network failures. Let the Presenter layer handle high-level user messaging.
- **Interceptors**: Use OkHttp Interceptors for cross-cutting concerns like logging and authorization headers.

## Data Schema & Mapping
- **Moshi**: Use Moshi for JSON serialization/deserialization.
- **Adapters**: Create custom Moshi `JsonAdapter`s for non-standard types (e.g., `LocalDateTime`).
- **Domain Mapping**: API models should be mapped to domain models (in `:serenity-common`) as soon as possible to decouple the app from specific API schemas.

## Logging & Observability
- **Timber**: Use Timber for all logging. Avoid `Log.*` or `println`.
- **HttpLogging**: Enable `HttpLoggingInterceptor` (Level.BASIC or BODY) in debug builds only.

## Code Example: executeOrThrow Extension
```kotlin
private fun <T> Call<T>.executeOrThrow(): T {
    val response = execute()
    if (response.isSuccessful) {
        return response.body() ?: throw IOException("Response was null")
    }
    throw IOException("Request failed with code ${response.code()}")
}
```
