# [SPEC] Title
## Goal
- [High-level objective]

## Context
- **Current State:** [Brief description]
- **Constraints:** [Technical/Architectural limits]
- **Files:** [Paths to relevant files]

## Standards Compliance
- [ ] **Architecture**: Repository pattern, Coroutines for I/O, Toothpick scoping. (See `architecture-core`)
- [ ] **UI/Leanback**: View Binding, `snake_case` IDs, Focus effects, No MotionLayout. (See `ui-leanback`)
- [ ] **Communication**: Retrofit `executeOrThrow`, Moshi mapping, domain decoupling. (See `network-retrofit`)
- [ ] **Testing**: MockK (no annotations), `clearAllMocks()`, `InjectingTest` if needed. (See `testing-mockk`)

## Impact Surface
- **Dependencies:** [What might break?]
- **Consumers:** [Who uses the affected files?]

## Risk & Rollback
- **Potential Risks:** [What could go wrong?]
- **Rollback Strategy:** [How to undo if it fails? (e.g. git revert)]

## Phase 1: [Name]
### Tasks
- [ ] Task 1: [Details]
- [ ] Task 2: [Details]
### Verification
- [Test command or manual check]
### [WAIT FOR APPROVAL]

## Phase 2: [Name]
...

## Final Deliverables
- [List of files/artifacts]

## Discovery & Learning Log
- [ ] Record any unexpected behavior, hidden dependencies, or architectural discoveries here during execution.

## Cleanup & Manual Actions (Human Required)
- [ ] Task: Delete legacy/obsolete files: `[path/to/old_file]`
- [ ] Task: [Any other non-agent task]
