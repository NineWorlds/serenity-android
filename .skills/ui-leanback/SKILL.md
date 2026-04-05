---
name: ui-leanback
description: Implementation expertise for Android TV (Leanback) UI, View Binding, Glide, and animations.
---

# UI Leanback & Media Client Standards

## View Binding Implementation
Mandatory for all new UI and refactors. Replaces `findViewById`.

### In Activities
```kotlin
private lateinit var binding: ActivityExampleBinding

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityExampleBinding.inflate(layoutInflater)
    setContentView(binding.root)
}
```

### In Fragments
```kotlin
private var _binding: FragmentExampleBinding? = null
private val binding get() = _binding!!

override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    _binding = FragmentExampleBinding.inflate(inflater, container, false)
    return binding.root
}

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}
```

### Handling Includes
```kotlin
val progressBinding = IncludeLoadingProgressBinding.bind(binding.root)
```

## Image Loading with Glide
Standard for all image loading and caching.

```kotlin
Glide.with(context)
    .load(url)
    .placeholder(R.drawable.default_video_still)
    .error(R.drawable.default_error)
    .into(binding.imageView)
```

## Animations (XML Only)
MotionLayout is prohibited. Use XML animations in `res/anim`.

### Example Fade In (`res/anim/fade_in.xml`)
```xml
<alpha xmlns:android="http://schemas.android.com/apk/res/android"
    android:interpolator="@android:anim/accelerate_interpolator"
    android:fromAlpha="0.0" android:toAlpha="1.0"
    android:duration="300" />
```

## Focus Effects (D-pad Navigation)
Non-negotiable for TV UI. Provide clear visual feedback.

### Scale-up on Focus
```kotlin
view.setOnFocusChangeListener { v, hasFocus ->
    if (hasFocus) {
        v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(200).start()
    } else {
        v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
    }
}
```
Alternatively, use a drawable selector with a border or background change.
