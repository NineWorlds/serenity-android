# UI & Design Standards

## Mission
Deliver a consistent, high-performance "leanback" experience for Android TV and Fire TV users.

## Component Selection
- **Activities**: Must extend `InjectingActivity` or `InjectingMvpActivity`.
- **Fragments**: Use Leanback support fragments. Manually manage `MvpDelegate` if extending a non-Mvp base fragment.
- **Adapters**: Use `SerenityPagingDataAdapter` for list content.

## View Binding Mandate
- **New Code**: View Binding is mandatory. `findViewById` is strictly forbidden.
- **Legacy Migration**: Any modification to a legacy layout SHOULD trigger a migration to View Binding for that component.

## Naming Conventions (Hard Rules)
- **Layouts**:
    - `activity_*.xml`: Full-screen activities.
    - `fragment_*.xml`: Fragment layouts.
    - `include_*.xml`: Reusable layout components.
    - `item_*.xml`: List/grid items.
    - `dialog_*.xml`: Custom dialog layouts.
- **IDs**: Always use `snake_case`. (e.g., `server_container`, `video_title`).
- **Resources**: Use `app_dimens.xml` and `app_styles.xml` for project-specific overrides.

## Design & Accessibility
- **Colors**: Avoid hardcoded hex values. Reference `@color/` resources.
- **Animations**: **DO NOT use MotionLayout**. Use XML animations in `res/anim`.
- **Focus Effects**: Clear visual feedback on focus (scale/border) is non-negotiable for D-pad navigation.

## Implementation Guide
See `ui-leanback` skill for View Binding implementation, Glide configuration, and Focus effect code.
