# Serenity for Android: Leanback Paging Support Implementation Plan

This document outlines the multi-stage plan to implement paging support in the `serenity-app` module using `androidx.leanback:leanback-paging` and `androidx.paging:paging-runtime-ktx`.

## Status: Phase 5 Completed - Moving to Phase 6

## Overview
The goal is to implement paging for Movies, TV Shows, and Seasons.
- **Initial Load**: 30 items.
- **Subsequent Loads**: 15 items.
- **Backend**: Emby-lib / Jellyfin-lib (via `SerenityClient`).

---

## Phase 1: Dependency Acquisition
**Goal**: Add the necessary libraries to the project.

- [x] **Task 1.1**: Update `serenity-app/build.gradle.kts` with Paging 3 and Leanback Paging dependencies.
- [x] **Task 1.2**: Perform a Gradle Sync.

---

## Phase 2: Core Plumbing (Interface & Repository Updates)
**Goal**: Update shared interfaces to support pagination parameters.

- [x] **Task 2.1**: Update `SerenityClient` interface (`serenity-common`).
- [x] **Task 2.2**: Update `CategoryRepository` (`serenity-app`).
- [x] **Task 2.3**: Update `VideoRepository` (`serenity-app`).
- [x] **Task 2.4**: Run existing unit tests to ensure no regressions in basic data fetching.

---

## Phase 3: Paging Logic (Sources)
**Goal**: Create `PagingSource` implementations to handle the 30/15 logic.

- [x] **Task 3.1**: Create `VideoCategoryPagingSource`.
- [x] **Task 3.2**: Create `EpisodePagingSource`.
- [x] **Task 3.3**: Create `SimilarItemsPagingSource`.

---

## Phase 4: Presenter & View Interface Updates
**Goal**: Refactor MVP components from static Lists to Paging Flows.

- [x] **Task 4.1**: Update `MainMenuView` and `DetailsView` interface definitions to support `PagingData`.
- [x] **Task 4.2**: Refactor `MainMenuPresenter`.
    - Change `processesCategories` to initialize a `Flow<PagingData<VideoCategory>>` for each row.
- [x] **Task 4.3**: Refactor `DetailsMVPPresenter`.
    - Update `updateSeries` and `loadSimilarItems` to return Paging flows.

---

## Phase 5: UI Integration (Leanback)
**Goal**: Connect the Paging Flows to the Leanback UI.

- [x] **Task 5.1**: Implement a generic `SerenityPagingDataAdapter` extending `androidx.leanback.paging.PagingDataAdapter`.
- [x] **Task 5.2**: Fix compilation errors and update `MainMenuFragment` to handle `PagingData`.
- [x] **Task 5.3**: Update `MainMenuVideoContentVerticalGridFragment` and its adapters to support Paging.
- [x] **Task 5.4**: Fix compilation errors and update `DetailsFragment` to handle `PagingData` for episodes and similar items.
- [x] **Task 5.5**: Fix any remaining compilation errors in `DetailsMVPPresenter`.

---

## Phase 6: Date/Time API Migration
**Goal**: Migrate from Joda-Time to the built-in `java.time` (Java 8+) API.

- [ ] **Task 6.1**: Audit Joda-Time usage across the project (e.g., `LocalDateTime`, `LocalDateJsonAdapter`).
- [ ] **Task 6.2**: Replace `org.joda.time` imports with `java.time` equivalents.
- [ ] **Task 6.3**: Update Moshi adapters for `java.time`.
- [ ] **Task 6.4**: Remove `net.danlew:android.joda` dependency and `JodaTimeAndroid.init()` calls.

---

## Verification & Testing Steps
1. **Unit Tests**:
    - [x] Test `PagingSource` implementations with mocked `SerenityClient`.
    - [x] Verify `startIndex` and `limit` calculations.
2. **Manual Verification**:
    - Scroll through a large category (e.g., "All Movies") and observe network calls for 30 then 15 items.
    - Verify "watched" status updates correctly across paged items.
