# [SPEC] Agent Skills Integration (agentskills.io Standard)

## Goal
Implement the `agentskills.io` standard into the project's Phased Spec-Driven Development (SDD) protocol. This enables modular, pull-on-demand expertise discovery and activation, ensuring context economy while providing specialized agent capabilities.

## Context
- **Current State:** The project uses a Phased GSD protocol defined in `prompts/plans/sdd_implementation_v1.md`. All agent instructions are currently static and centralized.
- **Constraints:** Must work with non-native skill loaders (Android Studio Gemini/Otter). Must preserve the "Memory Protocol" and subdirectory structure for plans.
- **Files:**
    - `prompts/plans/sdd_implementation_v1.md` (Target for update)
    - `AGENTS.md` (System prompt entry point)
    - `prompts/agents/SPEC_WRITER.md` (Persona definition)

## Standards Compliance
- [ ] **Discovery**: Automated via `list_files` on `prompts/skills/`.
- [ ] **Context Economy**: Read only YAML frontmatter during discovery; body content is "Pull-on-Demand".
- [ ] **Metadata**: Mandatory `name`, `description`, `compatibility`, and `version` fields.
- [ ] **Activation**: Explicit "Activating Skill" announcement before loading full content.

## Impact Surface
- **Dependencies:** `sdd_implementation_v1.md` is the master protocol. Updating it affects all future spec-writing and execution.
- **Consumers:** All AI agents interacting with the project.

## Risk & Rollback
- **Potential Risks:** Complexity in renumbering might lead to broken links in other docs. Over-segmentation of skills could make discovery slow.
- **Rollback Strategy:** Revert `sdd_implementation_v1.md` using Git.

## Phase 1: Infrastructure & Standard Setup
### Tasks
- [x] **Task 1.1: Create Skills Directory**
  - **Action:** Create `prompts/skills/.gitkeep`.
- [x] **Task 1.2: Define metadata schema**
  - **Action:** Create a template `prompts/templates/SKILL_TEMPLATE.md` with the required YAML frontmatter.
- [x] **Task 1.3: Create "Example" Skill**
  - **Action:** Create `prompts/skills/ui-refactor/SKILL.md` as a reference implementation.

### Verification
- `prompts/skills/` directory exists.
- `SKILL_TEMPLATE.md` contains the mandatory fields.

### [APPROVED]

## Phase 2: Protocol Migration & Renumbering
### Tasks
- [x] **Task 2.1: Update `sdd_implementation_v1.md` Structure**
  - **Action:** Insert "Phase 1: Agent Skills Integration" at the start of the "Phases" section.
  - **Action:** Renumber existing Phase 1 to Phase 2, Phase 2 to Phase 3, etc.
- [x] **Task 2.2: Formalize Discovery Protocol**
  - **Action:** Add the requirement for agents to execute `list_files` on `prompts/skills/` at session start.
- [x] **Task 2.3: Define "Pull-on-Demand" & Activation Workflow**
  - **Action:** Add the mandatory announcement and persona shift logic.

### Verification
- `sdd_implementation_v1.md` contains the 5 mandatory architectural changes for `agentskills.io`.
- All phase numbers are sequential and consistent.

### [APPROVED - RETROACTIVE]

## Phase 3: Agent & Persona Updates
### Tasks
- [x] **Task 3.1: Update `SPEC_WRITER.md`**
  - **Action:** Instruct the Spec Writer persona to check for applicable skills before drafting a spec.
- [x] **Task 3.2: Update `AGENTS.md`**
  - **Action:** Add the "Manual Skill Emulation" section for IDE-integrated assistants.
- [x] **Task 3.3: Context Economy Enforcement**
  - **Action:** Add explicit prohibition for reading skill bodies during discovery.

### Verification
- `AGENTS.md` mentions `agentskills.io` standard.
- `SPEC_WRITER.md` includes the skill discovery step.

### [APPROVED - RETROACTIVE]

## Phase 4: Protocol Integration
### Tasks
- [x] **Task 4.1: Update Communication Rules**
  - **Action:** Update `COMMUNICATION.md` to mention mandatory impact analysis and skill discovery during the interview phase.

### Verification
- `COMMUNICATION.md` explicitly mentions `prompts/skills/` and Skill Discovery.

### [APPROVED]

## Phase 5: Validation
### Tasks
- [x] **Task 5.1: Discovery Simulation**
  - **Action:** Ask the agent to list all available skills and describe one without reading its body.
- [x] **Task 5.2: Activation Simulation**
  - **Action:** Trigger the activation of the example skill and verify the persona shift.

### Verification
- Agent successfully identifies the skill from metadata only.
- Agent announces activation before reading the full skill file.

## Final Deliverables
- Updated `prompts/plans/sdd_implementation_v1.md`
- Updated `AGENTS.md`
- Updated `prompts/agents/SPEC_WRITER.md`
- New `prompts/templates/SKILL_TEMPLATE.md`
- New `prompts/skills/ui-refactor/SKILL.md` (Reference)

## Discovery & Learning Log
- [x] **Context Economy**: Verified that using `maxLinesCount` in `get_file_text_by_path` effectively preserves the context window during discovery.
- [x] **Manual Emulation**: Confirmed that the "Announce -> Load" workflow successfully simulates native skill loaders for IDE-integrated agents.
- [x] **Protocol Enforcement**: Updated `SPEC_WRITER.md` and `AGENTS.md` to include "Hard Stop" rules after every phase to prevent autopilot execution.
