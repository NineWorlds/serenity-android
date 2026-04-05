# Implementation Plan: Phased Spec-Driven Development (SDD)
**Status:** PROPOSED  
**Version:** 3.0.0  
**Target:** Standardized AI Workflow (Universal LLM/Agent Compatibility)

## 1. Scope and Summary
*   **Scope:** This protocol applies to all AI-assisted development (features, refactors, migrations).
*   **Summary:** To eliminate "Vibe Coding" and ensure consistency, this plan implements a **Spec-First Protocol**. Agents must generate a **Phased GSD (Goal, Steps, Deliverables)** document before writing any production code for complex tasks. 
*   **Context & Memory:** v2.0 introduces persistent context management. Each plan resides in its own directory, with a dedicated `memory/` sub-folder to record findings, unexpected dependencies, and state changes across execution phases.
*   **Skills Integration:** v3.0 introduces `agentskills.io` compatibility for modular expertise.

## 2. User Guide: How to Generate a Spec
To trigger a new specification, use the following prompt pattern:

> *"Using the protocol in `prompts/plans/sdd_implementation_v1.md`, create a Phased GSD Spec for [Task Description]."*

**Agent Logic (System Instructions):**
1.  **Intent Analysis:**
    *   **Research Exception:** If the request is for "General Research", the Spec-First flow is **NOT** required.
    *   **Vague Architectural Shift (MANDATORY SPEC):** If the request is broad or lacks specific scope, you **MUST** refuse and initiate the spec-writing process.
    *   **Complexity Threshold:** If the task affects > 2 files or changes architecture, you MUST initiate or offer a spec.
2.  **Organization:** Create a directory at `prompts/plans/<plan_name>/`. Place the spec file inside: `prompts/plans/<plan_name>/<plan_name>.md`.
3.  **Collaborative Interview & Skill Discovery:**
    *   **Discovery**: Execute `list_files` on `.skills/` to identify available expertise.
    *   **Context Economy**: Read **ONLY** the YAML frontmatter of `SKILL.md` files during discovery.
    *   **Task Match**: If a skill matches the request, announce: *"Activating Skill: [Name] from .skills/[name]/."* and load the full content.
4.  **Template Adherence:** Use the `prompts/templates/TASK_SPEC.md` structure.

## 3. User Guide: How to Execute a Generated Spec
Once a spec is approved, follow this atomic execution workflow:

1.  **Initialization:** Read the spec and the project context.
2.  **Memory Management:** Check `prompts/plans/<plan_name>/memory/` for previous findings.
3.  **Atomic Execution:** Perform only the tasks listed in the current Phase.
4.  **Discovery Logging:** Record unexpected behavior in `prompts/plans/<plan_name>/memory/`.
5.  **Human Gate:** Require explicit approval before moving to the next phase.

---

## Phase 1: Agent Skills Integration (agentskills.io)
*Goal: Enable modular, pull-on-demand expertise while preserving context window.*

- [ ] **Task 1.1: Formal Skill Discovery**
  - **Requirement:** At the start of every session, the agent **MUST** execute `list_files` on `.skills/` to identify available expertise.
- [ ] **Task 1.2: Context Economy (Metadata Extraction)**
  - **Constraint:** For every discovered skill, the agent MUST read **ONLY** the YAML frontmatter of the `SKILL.md` file.
- [ ] **Task 1.3: On-Demand Activation Workflow**
  - **Logic:** Match task to `description` -> **Announce** -> **Load Body** -> **Persona Shift**.

**Phase 1 Verification:**
1. Agent identifies available skills using `list_files`.
2. Agent announces activation before reading full skill content.
3. **STOP: Wait for Human Approval.**

---

## Phase 2: Discovery & Templates
*Goal: Establish the standardized structure and organizational rules.*

- [ ] **Task 2.1: Identify Target Agent(s)**
- [ ] **Task 2.2: Define Reporting Requirements**
- [ ] **Task 2.3: Create Standardized Directory Structure** (`prompts/agents/`, `prompts/templates/`, `prompts/plans/`).
- [ ] **Task 2.4: Create/Update `TASK_SPEC.md` Template**
- [ ] **Task 2.5: Implement Memory Standard**

**Phase 2 Verification:**
1. Template directory and updated `TASK_SPEC.md` exist.
2. **STOP: Wait for Human Approval.**

---

## Phase 3: Agent Intelligence (The Spec Writer)
*Goal: Define a persona and generate agent-specific configuration.*

- [ ] **Task 3.1: Update `SPEC_WRITER.md` Instruction Set**
  - **Persona:** Senior Architect enforcing the SDD protocol and Skill Discovery.
- [ ] **Task 3.2: Update `AGENTS.md` and Configuration**
  - **Requirement:** Add "Manual Skill Emulation" for IDE-integrated assistants.

**Phase 3 Verification:**
1. `SPEC_WRITER.md` mandates Skill Discovery before drafting.
2. **STOP: Wait for Human Approval.**

---

## Phase 4: Protocol Integration
*Goal: Formalize the rules in core documentation.*

- [ ] **Task 4.1: Update Communication Rules** (`COMMUNICATION.md`).

**Phase 4 Verification:**
1. Review `COMMUNICATION.md` for Impact Analysis requirements.
2. **STOP: Wait for Human Approval.**

---

## Phase 5: Workflow Validation & Cleanup
*Goal: Prove the system prevents context loss.*

- [ ] **Task 5.1: Simulation Test**
- [ ] **Task 5.2: Final Project Cleanup**

**Phase 5 Verification:**
1. Success if Agent follows renumbered protocol and memory rules.
2. **FINAL APPROVAL: Implementation Complete.**
