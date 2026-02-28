# [SPEC] Update SDD Protocol to v2.0 (Context & Memory)

## Goal
Update the SDD Implementation Plan (`sdd_implementation_v1.md`) and associated templates to incorporate advanced context management, impact analysis, and persistent memory logging, while adopting a subdirectory-based organizational structure.

## Context
- **Current State:** Plans are flat files in `prompts/plans/`. No formal "Impact Surface" or "Discovery Log" exists.
- **Constraints:** Must remain agent-agnostic. No tool-discovery automation (per user request).
- **Files:** 
    - `prompts/plans/sdd_implementation_v1.md`
    - `prompts/templates/TASK_SPEC.md`
    - `prompts/agents/SPEC_WRITER.md`

## Phase 1: Template & Protocol Redesign
### Tasks
- [ ] **Task 1.1: Update `TASK_SPEC.md` Template**
    - Add `## Impact Surface` section (Dependency mapping).
    - Add `## Risk & Rollback` section (Failure protocols).
    - Add `## Discovery & Learning Log` section (State persistence).
- [ ] **Task 1.2: Update `sdd_implementation_v1.md` Organizational Rules**
    - Define requirement for subdirectories: `./prompts/plans/<plan_name>/`.
    - Define requirement for memory directory: `./prompts/plans/<plan_name>/memory/`.
- [ ] **Task 1.3: Add Task 1.5 (Memory Standard) to `sdd_implementation_v1.md`**
    - Formally define how "Memory" is recorded during execution.

### Verification
- Verify `TASK_SPEC.md` contains the new sections.
- Verify `sdd_implementation_v1.md` reflects the subdirectory and memory pathing rules.
### [WAIT FOR APPROVAL]

## Phase 2: Agent Instruction Updates
### Tasks
- [ ] **Task 2.1: Update `SPEC_WRITER.md` Persona**
    - Instruct the agent to perform "Impact Analysis" during the interview.
    - Instruct the agent to use the subdirectory structure for all new plans.
- [ ] **Task 2.2: Update `AGENTS.md`**
    - Point to the updated protocol version and pathing rules.

### Verification
- Simulation test: Ask the agent to "Prepare a plan for X" and verify it proposes a subdirectory structure.
### [WAIT FOR APPROVAL]

## Final Deliverables
- Updated `prompts/plans/sdd_implementation_v1.md`
- Updated `prompts/templates/TASK_SPEC.md`
- Updated `prompts/agents/SPEC_WRITER.md`

## Cleanup & Manual Actions (Human Required)
- [ ] Task: Move any existing legacy plans into subdirectories if desired.
