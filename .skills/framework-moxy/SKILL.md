---
name: framework-moxy
description: "Trigger: Presenter Logic. Managing Moxy MVP state, Presenter lifecycle, and view delegation without @InjectPresenter."
---

# MVP with Moxy

## Presenter Delegation
Serenity uses Kotlin delegation for Moxy presenters to maintain a clean lifecycle and support Dependency Injection.

### Mandatory Pattern
DO NOT use `@InjectPresenter`. Instead, inject a `Provider` and use the `moxyPresenter` delegate.

```kotlin
@Inject
lateinit var presenterProvider: Provider<MainPresenter>

internal val presenter by moxyPresenter { presenterProvider.get() }
```

## View Interface Definition
Define a view interface that extends `MvpView`. Use Moxy strategy annotations to control how commands are queued.

```kotlin
@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {
    fun displayData(items: List<VideoContent>)
    fun showLoading()
    fun hideLoading()
}
```

## Presenter Implementation
Presenters should extend `MvpPresenter<ViewInterface>`.

```kotlin
class MainPresenter @InjectConstructor(
    private val repository: VideoRepository
) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadData()
    }

    private fun loadData() {
        // logic using repository and updating viewState
        viewState.showLoading()
        // ...
    }
}
```

## Integration with Toothpick
Ensure the presenter is available in the Toothpick scope, usually via `@InjectConstructor`.
For Activities, the injection usually happens in `super.onCreate()` when extending `InjectingMvpActivity`.
