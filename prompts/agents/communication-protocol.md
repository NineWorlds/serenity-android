# Communication Protocol Standards

## Mission
Ensure reliable and observable communication between the application and media servers.

## Protocol Mandates
- **Retrofit Only**: All network calls must use Retrofit 2 interfaces.
- **Service Segregation**: Group related endpoints into specific service interfaces.
- **Synchronous Execution Pattern**: Use `.execute()` within Coroutine-wrapped repository methods.
- **FORBIDDEN**: NEVER use Retrofit's `.enqueue()`. All async logic must be handled via Coroutines.
- **Error Propagation**: Use the `executeOrThrow()` extension pattern for all `Call<T>` objects.
- **Data Mapping**: API models (DTOs) MUST be mapped to domain models in `:serenity-common` to decouple the application from server-specific schemas.
- **Moshi**: Use Moshi for JSON serialization/deserialization.
- **Timber**: Use Timber for all logging. Avoid `Log.*` or `println`.

## Implementation Guide
See `network-retrofit` skill for Moshi adapter examples, `executeOrThrow` code, and service interface templates.
