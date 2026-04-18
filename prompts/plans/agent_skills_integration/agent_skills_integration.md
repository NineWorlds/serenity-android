# [SPEC] Agent Skills Integration (agentskills.io Standard)

## Goal
Implement the `agentskills.io` standard into the project's Phased Spec-Driven Development (SDD) protocol. This enables modular, pull-on-demand expertise discovery and activation, ensuring context economy while providing specialized agent capabilities using a universal root `.skills` directory.

## Context
- **Constraints:** Must work with non-native skill loaders (Android Studio Gemini/Otter), Claude Code, OpenHands, and Opencode.
- **Memory Path:** `prompts/plans/agent_skills_integration/memory/`
- **Files:**
    - `prompts/plans/sdd_implementation_v1.md` (Target for update)
    - `AGENTS.md` (System prompt entry point)
    - `prompts/agents/SPEC_WRITER.md` (Persona definition)

## Standards Compliance
- [ ] **Architecture**: SDD Protocol v3.0, persistent context management via `/memory`.
- [ ] **Discovery**: Automated via `available_skills` block in the system prompt or native agent tools.
- [ ] **Native Preference**: If the agent's system prompt includes the `### Operational Protocol` for skills or native skill tools, this MUST be used exclusively.
- [ ] **Emulated Fallback**: If no native support is detected, agents MUST fallback to manual discovery via the `.skills/` directory.
- [ ] **Context Economy**: Read only YAML frontmatter during discovery; body content is "Pull-on-Demand".
- [ ] **Metadata**: Mandatory `name`, `description`, `compatibility`, and `version` fields.
- [ ] **Activation**: Explicit "Activating Skill" announcement before loading full content.
- [ ] **Universal Access**: Symbolic links from `.claude/skills` to `.skills/`, and `.opencode/skills` to `.skills/`.

## Impact Surface
- **Dependencies:** `sdd_implementation_v1.md` is the master protocol. Updating it affects all future spec-writing and execution.
- **Consumers:** All AI agents (Claude, OpenHands, Opencode, Gemini) interacting with the project.

## Risk & Rollback
- **Potential Risks:** Complexity in renumbering might lead to broken links in other docs. Over-segmentation of skills could make discovery slow.
- **Rollback Strategy:** Revert changes via Git.

## Phase 1: Infrastructure & Compatibility Setup
### Tasks
- [ ] **Task 1.1: Initialize Persistent Memory**
- [ ] **Task 1.2: Multi-Agent Compatibility Check (Symbolic Links)**
- [ ] **Task 1.3: Create Universal Skills Directory**
- [ ] **Task 1.4: Update Artifact Log**

### Verification
- `memory/discovered_artifacts.md` exists and is populated.
- `.skills/` exists as the primary repository.
- User has been consulted regarding symbolic links.

## Phase 2: Migration & Global Path Updates
### Tasks
- [ ] **Task 2.1: Migrate Legacy Skills**
- [ ] **Task 2.2: Update Global Discovery Paths**
- [ ] **Task 2.3: Memory Sync**

### Verification
- `prompts/skills/` is empty or removed.
- `AGENTS.md` reflects the new `.skills/` path and Native Preference rule.

## Phase 3: Universal Protocol Integration
### Tasks
- [ ] **Task 3.1: Implement Branching Logic**
- [ ] **Task 3.2: Persona-Specific Alignment**
- [ ] **Task 3.3: Dual-Mode Validation**

### Verification
- Agent successfully identifies the skill from metadata only.
- Agent prefers native capabilities where available.
- All final deliverables are tracked in memory.

## Final Deliverables
- Updated `prompts/plans/sdd_implementation_v1.md`
- Updated `AGENTS.md`
- Updated `prompts/agents/SPEC_WRITER.md`
- Root `.skills/` repository with migrated content.
- Symbolic links for Claude and Opencode pointing to `.skills/`.
- `prompts/plans/agent_skills_integration/memory/discovered_artifacts.md`
- `prompts/plans/agent_skills_integration/memory/discovery_log.md`

## Discovery & Learning Log
- [ ] Migration successful; discovery logic now unified across all agents.
