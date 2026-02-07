## Code Style & Frameworks
- **Language**: Use Kotlin. Convert Java to Kotlin where feasible.
- **Formatting**: Always run `./gradlew spotlessApply`.
- **DI (Toothpick)**: Use Toothpick; prefer provider-based injection.
- **MVP (Moxy)**: Use Kotlin delegation. NEVER use `@InjectPresenter`.
- **View Binding**: Replace all `findViewById` with View Binding.

### MVP (Moxy) Conversion Example
To make Activities testable and decoupled:
1. **DO NOT use `@InjectPresenter` on fields.**
2. **DO inject Providers** and use `moxyPresenter` delegate.

```kotlin
// Inject Provider and use moxyPresenter
@Inject
lateinit var presenterProvider: Provider<MainPresenter>

internal val presenter by moxyPresenter { presenterProvider.get() }
```

### View Binding Standards
Always replace `findViewById` with View Binding. Example:
```kotlin
private lateinit var binding: ActivityMainBinding
private lateinit var progressBinding: IncludeLoadingProgressBinding

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    progressBinding = IncludeLoadingProgressBinding.bind(binding.root)
    setContentView(binding.root)
}
```