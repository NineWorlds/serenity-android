# UI & Design Standards

## Mission
Deliver a consistent, high-performance "leanback" experience for Android TV and Fire TV users.

## Component Patterns
- **Activities**: Must extend `InjectingActivity` or `InjectingMvpActivity`. Use View Binding for layout inflation.
- **Fragments**: Use Leanback support fragments (e.g., `DetailsSupportFragment`) where applicable. Manually manage `MvpDelegate` if extending a non-Mvp base fragment.
- **Adapters**: Use `SerenityPagingDataAdapter` for list content to support efficient data loading and pagination.

## View Binding & Legacy Migration
- **Mandatory**: All new UI code **MUST** use View Binding.
- **Legacy Rule**: Any modification to a legacy layout **SHOULD** trigger a migration to View Binding for that component. Replace all `findViewById` calls with binding references.
- **Forbidden**: `findViewById` is strictly forbidden in new code.

## Naming Conventions
- **Layouts**:
    - `activity_*.xml`: Full-screen activities.
    - `fragment_*.xml`: Fragment layouts.
    - `include_*.xml`: Reusable layout components.
    - `item_*.xml`: Layouts for list/grid items.
    - `dialog_*.xml`: Custom dialog layouts.
- **IDs**: Always use `snake_case`. (e.g., `server_container`, `video_title`).
- **Resources**: Group related resources. Use `app_dimens.xml` and `app_styles.xml` for project-specific overrides.

## Design Tokens
- **Colors**: Reference `@color/` resources. Avoid hardcoded hex values in layouts. Use `legacy_colors.xml` only when maintaining older components.
- **Dimensions**: Use `dimens.xml` for standard margins, padding, and text sizes. Standard Leanback dimensions should be respected.

## Animations & Transitions
- **XML Animations**: Use `res/anim` for standard transitions (fades, scales).
- **Forbidden Pattern**: **DO NOT use MotionLayout**. It is not a project standard. Simple transitions should stay in XML.
- **Focus Effects**: TV UI must provide clear visual feedback on focus (e.g., scale up or border highlight). This is non-negotiable for D-pad navigation.

## Code Example: View Binding in Activity
```kotlin
private lateinit var binding: ActivityServerSelectionBinding
private lateinit var progressBinding: IncludeLoadingProgressBinding

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityServerSelectionBinding.inflate(layoutInflater)
    progressBinding = IncludeLoadingProgressBinding.bind(binding.root)
    setContentView(binding.root)
}
```
