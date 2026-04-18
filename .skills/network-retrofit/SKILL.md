---
name: network-retrofit
description: "Trigger: API & Networking. Implementing Retrofit services, Moshi adapters, DTO mapping, and synchronous execution patterns."
---

# Networking with Retrofit & Moshi

## Retrofit Service Definition
Use Retrofit 2 interfaces for all network calls. Group related endpoints.

```kotlin
interface UsersService {
    @GET("Users/{userId}")
    suspend fun getUser(@Path("userId") userId: String): UserDTO
}
```

## Synchronous Execution Pattern
While we use `suspend` functions, the underlying implementation often uses a custom extension to handle Retrofit `Call` objects synchronously within a coroutine context for better error handling.

### `executeOrThrow` Extension
```kotlin
private fun <T> Call<T>.executeOrThrow(): T {
    val response = execute()
    if (response.isSuccessful) {
        return response.body() ?: throw IOException("Response was null")
    }
    throw IOException("Request failed with code ${response.code()} and message ${response.message()}")
}
```

## Moshi JSON Mapping
Standard for JSON parsing. Use `@Json` for explicit mapping.

```kotlin
@JsonClass(generateAdapter = true)
data class UserDTO(
    @Json(name = "Name") val name: String,
    @Json(name = "Id") val id: String
)
```

## Custom Moshi Adapters
For non-standard types like `LocalDateTime`.

```kotlin
class LocalDateTimeAdapter {
    @FromJson
    fun fromJson(json: String): LocalDateTime = LocalDateTime.parse(json)

    @ToJson
    fun toJson(value: LocalDateTime): String = value.toString()
}
```

## Domain Mapping
Decouple the application from API schemas by mapping DTOs to domain models immediately.

```kotlin
class UserRepository(private val service: UsersService) {
    suspend fun getUser(id: String): User = withContext(Dispatchers.IO) {
        val dto = service.getUser(id)
        dto.toDomain()
    }
}
```
