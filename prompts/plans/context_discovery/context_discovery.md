# [SPEC] Project Standards Context Discovery

## Goal
Perform a comprehensive scan of the repository to identify, extract, and document "Tribal Knowledge" and architectural patterns. Update or create standards in `prompts/agents/` to ensure AI and human developers align with the "Golden Standards" of the project.

## Context
- **Current State:** Several standard files exist (`FRAMEWORKS.md`, `TESTING.md`, etc.), but deeper architectural, UI, and communication patterns need formalization.
- **Constraints:** Must adhere to existing Toothpick, Moxy, and MockK mandates. No directory creation tool available (use `.gitkeep` workarounds).
- **Files:** `prompts/agents/*`, `serenity-app/`, `emby-lib/`, `jellyfin-lib/`.

## Impact Surface
- **Dependencies:** None (documentation only).
- **Consumers:** All future AI agent interactions and developer onboarding.

## Risk & Rollback
- **Potential Risks:** Documenting a legacy pattern as a "Golden Standard" by mistake.
- **Rollback Strategy:** `git checkout prompts/agents/` or `git revert`.

## Phase 1: Deep Scanning & Pattern Identification
### Tasks
- [ ] Task 1: Scan `serenity-app` for consistent Activity/Fragment/Presenter lifecycle patterns.
- [ ] Task 2: Analyze `emby-lib` and `jellyfin-lib` for data repository and API error handling patterns.
- [ ] Task 3: Identify UI naming conventions and design tokens (colors, dimensions) across modules.
- [ ] Task 4: Compare `framer-motion` vs `css-transitions` equivalents in Android (e.g., XML animations vs MotionLayout).
- [ ] Task 5: Check for a `constitution.md` or high-level philosophy document.

### Verification
- List of identified patterns stored in `prompts/plans/context_discovery/memory/discovery_log.md`.
### [WAIT FOR APPROVAL]

## Phase 2: Extraction & Documentation
### Tasks
- [ ] Task 1: Create or update `prompts/agents/architecture.md` (Directory structure, module boundaries, data flow).
- [ ] Task 2: Create or update `prompts/agents/ui-standards.md` (Component patterns, naming conventions, design tokens).
- [ ] Task 3: Create or update `prompts/agents/communication-protocol.md` (API design, error schemas, logging).
- [ ] Task 4: Update `prompts/agents/FRAMEWORKS.md` to reflect any new "Source of Truth" findings.
- [ ] Task 5: Create/Update `prompts/agents/constitution.md` with the project philosophy.

### Verification
- All files exist in `prompts/agents/` with "Mission," "Non-Negotiables," and "Code Examples" sections.
### [WAIT FOR APPROVAL]

## Phase 3: Conflict Resolution & Refinement
### Tasks
- [ ] Task 1: Present any conflicting patterns discovered (e.g., multiple ways of handling DI in tests).
- [ ] Task 2: Ask user for the "Golden Standard" on identified conflicts.
- [ ] Task 3: Finalize documentation based on user feedback.

### Verification
- User confirms standards are accurate.
### [WAIT FOR APPROVAL]

## Final Deliverables
- `prompts/agents/architecture.md`
- `prompts/agents/ui-standards.md`
- `prompts/agents/communication-protocol.md`
- `prompts/agents/constitution.md`
- Updated `prompts/agents/FRAMEWORKS.md`

## Discovery & Learning Log
- (To be populated during Phase 1)
