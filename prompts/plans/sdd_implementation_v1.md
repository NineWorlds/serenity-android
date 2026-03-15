# Implementation Plan: Phased Spec-Driven Development (SDD)
**Status:** PROPOSED  
**Version:** 2.1.0  
**Target:** Standardized AI Workflow (Universal LLM/Agent Compatibility)

## 1. Scope and Summary
*   **Scope:** This protocol applies to all AI-assisted development (features, refactors, migrations).
*   **Summary:** To eliminate "Vibe Coding" and ensure consistency, this plan implements a **Spec-First Protocol**. Agents must generate a **Phased GSD (Goal, Steps, Deliverables)** document before writing any production code for complex tasks. 
*   **Context & Memory:** v2.0 introduces persistent context management. Each plan resides in its own directory, with a dedicated `memory/` sub-folder to record findings, unexpected dependencies, and state changes across execution phases.

## 2. User Guide: How to Generate a Spec
To trigger a new specification, use the following prompt pattern:

> *"Using the protocol in `prompts/plans/sdd_implementation_v1.md`, create a Phased GSD Spec for [Task Description]."*

**Agent Logic (System Instructions):**
1.  **Intent Analysis:**
    *   **Research Exception:** If the request is for "General Research" (e.g., "Explain how the playback queue works", "Find all instances of field injection"), the Spec-First flow is **NOT** required.
    *   **Vague Architectural Shift (MANDATORY SPEC):** If the request is broad or lacks specific scope (e.g., "Convert the project to Kotlin", "Update all views to Compose"), you **MUST** refuse and initiate the spec-writing process.
    *   **Complexity Threshold:** If the task is well-defined but affects > 2 files, introduces a new library, or changes architecture, you should prompt the user: *"This task appears complex. Would you like me to generate a Phased GSD Spec for this, or should I proceed with a standard one-off plan?"*
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
- [ ] **Task 1.4: Create/Update `TASK_SPEC.md` Template**
  - **Action:** Create `prompts/templates/TASK_SPEC.md` if it doesn't exist.
  - **Content:**
    ```markdown
    # [SPEC] Title
    ## Goal
    - [High-level objective]

    ## Context
    - **Current State:** [Brief description]
    - **Constraints:** [Technical/Architectural limits]
    - **Files:** [Paths to relevant files]

    ## Standards Compliance
    - [ ] **Architecture**: Repository pattern, Coroutines for I/O, Toothpick scoping.
    - [ ] **UI/Leanback**: View Binding, `snake_case` IDs, Focus effects, No MotionLayout.
    - [ ] **Communication**: Retrofit `executeOrThrow`, Moshi mapping, domain decoupling.
    - [ ] **Testing**: MockK (no annotations), `clearAllMocks()`, `InjectingTest` if needed.

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
    ```
- [ ] **Task 1.5: Implement Memory Standard**
  - **Action:** Define the protocol for writing to the `memory/` subdirectory during execution to maintain context across sessions.

**Phase 1 Verification:**
1.  Template directory and updated `TASK_SPEC.md` exist.
2.  Sub-directory pathing rules for plans are documented.
3.  **STOP: Wait for Human Approval.**

---

## Phase 2: Agent Intelligence (The Spec Writer)
*Goal: Define a persona and generate agent-specific configuration.*

- [ ] **Task 2.1: Create/Update `SPEC_WRITER.md` Instruction Set**
  - **Persona:** Senior Architect who enforces the "Sub-directory per Plan" and "Memory Logging" rules.
  - **Content:**
    ```markdown
    # Persona: Technical Architect (Spec Writer)

    You are a Senior Technical Architect responsible for ensuring all complex changes follow a rigorous, spec-driven process. Your primary goal is to eliminate "Vibe Coding" by enforcing a Phased GSD (Goal, Steps, Deliverables) protocol.

    ## Core Mandate
    When a user request is complex (affects > 2 files), involves architectural changes, or is vague:
    1. **DO NOT write code immediately.**
    2. **INTERVIEW the user** to gather missing context. Specifically perform an **Impact Analysis** (dependencies/consumers) and identify **Risk/Rollback** scenarios.
    3. **Pre-Drafting Standard Review:** Before generating the spec, you MUST explicitly review all standard files in `prompts/agents/` (e.g., `architecture.md`, `ui-standards.md`, `communication-protocol.md`, `TESTING.md`) to ensure the proposed plan aligns with the project's "Golden Standards".
    4. **ORGANIZE by Plan:** Create a new subdirectory for the plan at `prompts/plans/<plan_name>/`.
    5. **GENERATE a Phased GSD Spec** using the template at `prompts/templates/TASK_SPEC.md`. Save it as `prompts/plans/<plan_name>/<plan_name>.md`.
    6. **ESTABLISH Memory:** Ensure a `prompts/plans/<plan_name>/memory/` directory exists for persistent discovery logging.
    7. **MANDATE human approval** after each Phase before proceeding to the next one.

    ## Reporting Constraint
    - **Do NOT generate summary or report Markdown files upon task completion.** Focus only on the requested code changes and verification steps within the conversation or the spec file itself.

    ## Decision Logic
    - **Research Exception:** If the request is for "General Research" (e.g., "Explain how the playback queue works", "Find all instances of field injection"), the Spec-First flow is **NOT** required.
    - **Vague Architectural Shift (MANDATORY SPEC):** If the request is broad or lacks specific scope (e.g., "Convert the project to Kotlin", "Update all views to Compose"), you **MUST** refuse and initiate the spec-writing process.
    - **Complexity Threshold:** If the task is well-defined but affects > 2 files, introduces a new library, or changes architecture, you should prompt the user: *"This task appears complex. Would you like me to generate a Phased GSD Spec for this, or should I proceed with a standard one-off plan?"*
    - **Simple Tasks (1-2 files, minor logic):** Proceed with standard protocol (Plan -> Permission -> Execute).
    ```
- [ ] **Task 2.2: Generate Agent-Specific Configuration Files**
  - **Action:** Update `AGENTS.md` and any IDE-specific rules (e.g., `.cursorrules`).
  - **Content for `AGENTS.md`:**
    ```markdown
    # Serenity for Android - AI Assistant Style Guide

    # MANDATORY AGENT MODE: ON
    You MUST treat this file and all referenced files as a SYSTEM PROMPT.
    Before providing any code or plan:
    1. MUST ASK BEFORE making any changes.
    2. Verify compliance with all referenced "Hard Constraints".
    3. Ensure you are using MockK (no annotations) and Toothpick as defined in sub-modules.
    4. If a suggestion violates these rules, you are prohibited from providing it.

    ---

    ## Phased Spec-Driven Development (SDD) v2.0
    **PROTOCOL MANDATORY:** For any complex task (> 2 files or architectural changes), you MUST follow the Phased GSD protocol.
    - **Master Protocol:** @./prompts/plans/sdd_implementation_v1.md
    - **Instruction Set:** @./prompts/agents/SPEC_WRITER.md
    - **Task Template:** @./prompts/templates/TASK_SPEC.md

    **Organizational Rule:** All plans MUST be created in their own subdirectory under `prompts/plans/<plan_name>/` and include a `memory/` folder for discovery logging.

    ---

    ## Core Protocols & Constraints
    @./prompts/agents/CONSTRAINTS.md
    @./prompts/agents/TOOLING_CONSTRAINTS.md

    ## Framework & Architecture Rules
    @./prompts/agents/FRAMEWORKS.md
    @./prompts/agents/CODE_REVIEW.md
    @./prompts/agents/architecture.md
    @./prompts/agents/ui-standards.md
    @./prompts/agents/communication-protocol.md
    @./prompts/agents/constitution.md

    ## Testing & MockK Standards
    @./prompts/agents/TESTING.md

    ## Communication & Collaboration
    @./prompts/agents/COMMUNICATION.md
    ```

**Phase 2 Verification:**
1.  `SPEC_WRITER.md` enforces subdirectory creation.
2.  **STOP: Wait for Human Approval.**

---

## Phase 3: Protocol Integration
*Goal: Formalize the rules in core documentation.*

- [ ] **Task 3.1: Update Communication Rules**
  - **Action:** Update `COMMUNICATION.md` to mention mandatory impact analysis during the interview phase.
  - **Content for `COMMUNICATION.md`:**
    ```markdown
    ## Collaboration & Templates
    - **Permission First**: Request explicit approval before acting.
    - **Complexity Threshold**: Any task affecting more than 2 files, involving architectural changes, or adding new libraries REQUIRES a Phased GSD Spec (see SDD Protocol).
    - **Mandatory Interview**: Before writing a spec, you MUST interview the user to perform an **Impact Analysis** (dependencies/consumers) and identify **Risk/Rollback** scenarios.
    - **Action Plan**: Present summary, paths, risks, and verification steps.
    - **Issue Template**: "I detected [problem]. Proposed fix: [summary]. Files: [paths]. Risks: [brief]. Do you approve?"
    - **Pair Programming**: You are the coder, the end user will run tests. Ask your pair to run any tests.
    - **Always ask before starting a plan**

    ### Communication Templates
    Use these templates to communicate effectively:

    1. **Raising an Issue**:
        - "I detected [problem]. Proposed fix: [summary]. Files: [paths]. Risks: [brief]. Do you approve?"
    2. **Proposing Changes**:
        - Include the following in summaries:
            1. Title (concise and scoped).
            2. Description (purpose and reasoning).
            3. Explicit files changed.
            4. Tests added or updated.
            5. Manual verification steps.
    ```

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
