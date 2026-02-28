# Implementation Plan: Phased Spec-Driven Development (SDD)
**Status:** PROPOSED  
**Version:** 2.0.0  
**Target:** Standardized AI Workflow (Universal LLM/Agent Compatibility)

## 1. Scope and Summary
*   **Scope:** This protocol applies to all AI-assisted development (features, refactors, migrations).
*   **Summary:** To eliminate "Vibe Coding" and ensure consistency, this plan implements a **Spec-First Protocol**. Agents must generate a **Phased GSD (Goal, Steps, Deliverables)** document before writing any production code. 
*   **Context & Memory:** v2.0 introduces persistent context management. Each plan resides in its own directory, with a dedicated `memory/` sub-folder to record findings, unexpected dependencies, and state changes across execution phases.

## 2. User Guide: How to Generate a Spec
To trigger a new specification, use the following prompt pattern:

> *"Using the protocol in `prompts/plans/sdd_implementation_v1.md`, create a Phased GSD Spec for [Task Description]."*

**Agent Logic (System Instructions):**
1.  **Complexity Check (MANDATORY REFUSAL):** If the task affects > 2 files, introduces a new library, or changes architecture, you are **PROHIBITED** from writing code immediately. You must refuse and initiate the spec-writing process.
2.  **Organization:** Create a directory at `prompts/plans/<plan_name>/`. Place the spec file inside: `prompts/plans/<plan_name>/<plan_name>.md`.
3.  **Collaborative Interview:** Interview the user for missing context. Specifically perform an **Impact Analysis** (dependencies/consumers) and identify **Risk/Rollback** scenarios.
4.  **Template Adherence:** Use the `prompts/templates/TASK_SPEC.md` structure.

## 3. User Guide: How to Execute a Generated Spec
Once a spec is approved, follow this atomic execution workflow:

1.  **Initialization:** Read the spec and the project context.
2.  **Memory Management:** Before starting a phase, check `prompts/plans/<plan_name>/memory/` for previous findings.
3.  **Atomic Execution:** Perform only the tasks listed in the current Phase.
4.  **Discovery Logging:** If an unexpected behavior or dependency is found, record it immediately in a new file under `prompts/plans/<plan_name>/memory/`.
5.  **Human Gate:** Require explicit approval before moving to the next phase.

## 4. Example: Phased GSD Spec (Sub-directory Structure)
*Below is the folder structure for a generated spec.*

```text
prompts/plans/moshi_migration/
├── moshi_migration.md (The [SPEC] file)
└── memory/
    └── 01_dependency_discovery.md (Logs from Phase 1)
```

---

## Phase 1: Discovery & Templates
*Goal: Establish the standardized structure and organizational rules.*

- [ ] **Task 1.1: Identify Target Agent(s)**
  - **Action:** Ask the user which AI tool(s) are being targeted.
- [ ] **Task 1.2: Define Reporting Requirements**
  - **Action:** Confirm preference for suppressing summary reports.
- [ ] **Task 1.3: Create Standardized Directory Structure**
  - Ensure `prompts/agents/`, `prompts/templates/`, and `prompts/plans/` exist.
- [ ] **Task 1.4: Update `TASK_SPEC.md` Template**
  - Include sections for: `Impact Surface`, `Risk & Rollback`, and `Discovery & Learning Log`.
- [ ] **Task 1.5: Implement Memory Standard**
  - **Action:** Define the protocol for writing to the `memory/` subdirectory during execution to maintain context across sessions.

**Phase 1 Verification:**
1.  Template directory and updated `TASK_SPEC.md` exist.
2.  Sub-directory pathing rules for plans are documented.
3.  **STOP: Wait for Human Approval.**

---

## Phase 2: Agent Intelligence (The Spec Writer)
*Goal: Define a persona and generate agent-specific configuration.*

- [ ] **Task 2.1: Update `SPEC_WRITER.md` Instruction Set**
  - **Persona:** Senior Architect who enforces the "Sub-directory per Plan" and "Memory Logging" rules.
- [ ] **Task 2.2: Generate Agent-Specific Configuration Files**
  - Update `AGENTS.md` and any IDE-specific rules (e.g., `.cursorrules`).

**Phase 2 Verification:**
1.  `SPEC_WRITER.md` enforces subdirectory creation.
2.  **STOP: Wait for Human Approval.**

---

## Phase 3: Protocol Integration
*Goal: Formalize the rules in core documentation.*

- [ ] **Task 3.1: Update Communication Rules**
  - Update `COMMUNICATION.md` to mention mandatory impact analysis during the interview phase.

**Phase 3 Verification:**
1.  Review `COMMUNICATION.md` diff.
2.  **STOP: Wait for Human Approval.**

---

## Phase 4: Workflow Validation & Cleanup
*Goal: Prove the system prevents context loss.*

- [ ] **Task 4.1: Simulation Test**
  - Trigger a complex migration and verify the agent creates the subdirectory and the `memory/` folder.
- [ ] **Task 4.2: Final Project Cleanup**
  - Identify and list any temporary files for manual removal.

**Phase 4 Verification:**
1.  Success if Agent follows subdirectory and memory protocol.
2.  **FINAL APPROVAL: Implementation Complete.**

---

## License
MIT License
Copyright (c) 2026 David Carver and NineWorlds
