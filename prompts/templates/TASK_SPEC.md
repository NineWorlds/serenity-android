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

## TDD Execution Mode: Autonomous Phase Loop
- **Requirement**: For all programming tasks, the `@tdd` skill is **MANDATORY** for every phase. The Agent must trigger the skill and announce activation at the start of each programming phase.
- **Permissions granted**: Agent is authorized to trigger `execute_run_configuration` for TDD cycles ONLY AFTER user approval for each cycle within the approved phase.
- **Strict RED → GREEN Constraint**: The Agent is prohibited from proceeding to any subsequent TDD loop or Phase task until the current task's RED state has been successfully transitioned to a verified GREEN state. 
- **Verification Gate**: Each TDD loop task MUST be marked with the result of the test execution. If the test remains RED, the agent must diagnose and resolve, not move on.
- **Human Gates**: Verification steps at the end of each Phase require manual review and explicit "Proceed" command.

## Phase 1: [Name]
### Tasks
- [ ] **TDD Loop 1: [Behavior Name]**
    - [ ] RED: Write failing unit test.
    - [ ] GREEN: Implement minimal code to pass test.
    - [ ] **VERIFICATION**: [ ] Test Passed (GREEN)
- [ ] **TDD Loop 2: [Behavior Name]**
    - [ ] RED: Write failing unit test.
    - [ ] GREEN: Implement minimal code to pass test.
    - [ ] **VERIFICATION**: [ ] Test Passed (GREEN)
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
