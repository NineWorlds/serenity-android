---
name: framework-toothpick
description: "Trigger: Dependency Injection. Managing Toothpick scopes, binding modules, and implementing constructor or provider-based injection."
---

# Dependency Injection with Toothpick

## Scoping Standards
Serenity uses hierarchical scopes to manage object lifecycles.

- **APPLICATION_SCOPE**: Global singletons (Repositories, API Clients, Preferences).
- **Activity/Fragment Scopes**: Lifecycle-bound dependencies.

## Injection Patterns

### Constructor Injection (Preferred)
Use `@InjectConstructor` to allow Toothpick to automatically instantiate and inject dependencies.

```kotlin
@InjectConstructor
class MyRepository(private val api: MyApi) { ... }
```

### Provider-Based Injection
Useful for lazy initialization or breaking circular dependencies.

```kotlin
@Inject
lateinit var presenterProvider: Provider<MainPresenter>
```

### Manual Injection
Used in components where constructor injection isn't possible (e.g., legacy classes, some base classes).

```kotlin
init {
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE))
}
```

## Module Configuration
Bindings are defined in `Module` classes.

```kotlin
class MyModule : Module() {
    init {
        bind(MyService::class.java).to(MyServiceImpl::class.java).singleton()
        bind(MyRepository::class.java).toInstance(myRepositoryInstance)
    }
}
```

## Integration in Activities/Fragments
Extend `InjectingActivity` or `InjectingMvpActivity` to have scopes managed automatically.

```kotlin
class MyActivity : InjectingActivity() {
    @Inject lateinit var myService: MyService
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Injection happens in super.onCreate()
    }
}
```
