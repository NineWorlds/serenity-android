# Project Constitution & Philosophy

## Mission
To provide a free, open-source, and high-performance media client for Android TV and Fire TV, empowering users to access their personal media collections (via Emby and Jellyfin) with a premium "leanback" experience.

## Core Philosophy
- **Community First**: Serenity is an open-source project (MIT License). Contributions from the community are welcomed and encouraged.
- **Leanback Optimization**: The UI is purpose-built for TV interfaces, prioritizing D-pad navigation, focus clarity, and large-screen readability.
- **Provider Neutrality**: While Plex support is deprecated, the architecture aims to be modular enough to support multiple media server backends (currently Emby and Jellyfin).
- **Performance & Stability**: Prioritize smooth animations and reliable playback. Business logic must be unit-tested to ensure stability across server versions.

## Non-Negotiables for Contributors
1. **Respect the User**: No tracking beyond essential analytics (e.g., Firebase for crashes/usage if enabled), no ads, and no paywalls.
2. **Code Quality**: Adhere to the project's MVP (Moxy), DI (Toothpick), and Testing (MockK) standards.
3. **Documentation**: Major architectural changes or new features must be documented in the `prompts/agents/` directory to guide future development.
4. **Transparency**: All development happens in the open via pull requests and issue tracking.

## Technical Vision
- **Modernization**: Transition from Java to Kotlin.
- **Decoupling**: Maintain clear boundaries between the UI (`serenity-app`), server-specific logic (`*-lib`), and core domain models (`serenity-common`).
- **Reactive Patterns**: Leverage Coroutines for asynchronous work and Flow where appropriate for reactive state management.
