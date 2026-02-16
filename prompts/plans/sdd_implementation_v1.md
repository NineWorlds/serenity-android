# Implementation Plan: Phased Spec-Driven Development (SDD)
**Status:** PROPOSED  
**Version:** 1.6.0  
**Target:** Standardized AI Workflow (Project Agnostic)

## 1. Scope and Summary
*   **Scope:** This protocol applies to all AI-assisted development within any software project. It governs how architectural changes, refactors, and new features are planned and executed by AI Agents.
*   **Summary:** To eliminate "Vibe Coding" and ensure consistency, this plan implements a **Spec-First Protocol**. Agents must generate a **Phased GSD (Goal, Steps, Deliverables)** document before writing code. Each phase must include its own verification steps and require explicit human approval before the next phase begins.

## 2. User Guide: How to Generate a Spec
To generate a new specification using this protocol, follow these steps:

1.  **Reference this Plan:** In your prompt to the AI, reference this file: *"Using the protocol in @sdd_implementation_v1.md, create a Phased GSD Spec for [Task Description]."*
2.  **Complexity Check:** If the task affects > 2 files, introduces a new library, or changes architecture, the Agent must refuse to code until the spec is finalized.
3.  **Collaborative Refinement:** The Agent will interview you for context (Files, Constraints, Goals).
4.  **Template Adherence:** Ensure the Agent uses the `TASK_SPEC.md` structure (Goal, Context, Phases with Verification/Approval Gates).

## 3. User Guide: How to Execute a Generated Spec
Once a spec (e.g., `refactor_feature_x.md`) is generated and approved, follow this execution workflow:

1.  **Initialize the Phase:** Tell the Agent: *"Follow Phase 1 of @refactor_feature_x.md. Do not proceed to Phase 2 until I verify and approve."*
2.  **Context Loading:** Ensure the Agent has read the spec file and all relevant source files listed in the "Context" section.
3.  **Atomic Execution:** The Agent performs the tasks listed in the current Phase.
4.  **Verification:** The Agent must perform the "Verification" steps listed for that Phase and report results.
5.  **Human Gate:** You must review the changes and verification results. Use the phrase *"Phase 1 Approved. Proceed to Phase 2"* to move forward.

## 4. Example: Phased GSD Spec (Moshi Conversion)
*Below is an example of what a generated spec looks like.*

```markdown
# [SPEC] Convert Directory.java to Moshi/Kotlin
## Goal
Migrate legacy Java model to Kotlin with Moshi JSON parsing.

## Context
- **Files:** `Directory.java`, `SerenityClient.kt`
- **Constraint:** Must maintain field naming compatibility with existing API.

## Phase 1: Model Conversion
### Tasks
- [ ] Convert `Directory.java` to `Directory.kt` (Data Class).
- [ ] Add `@Json` annotations for Moshi.
### Verification
- Unit test: `DirectoryTest` passes with sample JSON.
### [WAIT FOR APPROVAL]

## Phase 2: Integration
### Tasks
- [ ] Update `SerenityClient` to use `MoshiConverterFactory`.
- [ ] Remove `Gson` references if unused.
### Verification
- Please verify the project builds and tests run.
### [WAIT FOR APPROVAL]
```

---

## Phase 1: Discovery & Templates
*Goal: Identify the environment and establish the standardized structure for specifications.*

- [ ] **Task 1.1: Identify Target Agent(s)**
  - **Action:** Ask the user which AI tool(s) are being targeted (e.g., GitHub Copilot, Android Studio Gemini, Cursor, Claude).
  - **Outcome:** Document the target agent(s) to tailor the configuration files in Phase 2.
- [ ] **Task 1.2: Define Reporting Requirements**
  - **Action:** Ask the user if completion summary reports (Markdown files) should be generated.
  - **Constraint:** If negative, include the instruction: `Do NOT generate summary or report Markdown file upon completion`. (Note: Targets Claude specifically).
- [ ] **Task 1.3: Create Standardized Prompt Directory**
  - Create a designated directory for AI prompts and templates (e.g., `.ai/templates/` or `prompts/templates/`).
- [ ] **Task 1.4: Create `TASK_SPEC.md` Template**
  - Define a project-agnostic Phased GSD structure inspired by [GitHub Spec-Kit](https://github.com/github/spec-kit) and [GSD](https://github.com/gsd-build/get-shit-done).
  - **Template Structure:**
    ```markdown
    # [TITLE]
    ## Goal
    - [High-level objective]
    
    ## Context
    - **Current State:** [Brief description]
    - **Constraints:** [Technical/Architectural limits]
    - **Files:** [Paths to relevant files]
    
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
    ```

**Phase 1 Verification:**
1.  Target agent(s) and reporting preferences documented.
2.  Template directory exists.
3.  `TASK_SPEC.md` reviewed for clarity and phasing.
4.  **STOP: Wait for Human Approval.**

---

## Phase 2: Agent Intelligence (The Spec Writer)
*Goal: Define a persona and generate agent-specific configuration.*

- [ ] **Task 2.1: Create `SPEC_WRITER.md` Instruction Set**
  - **Persona Example:**
    ```markdown
    # Persona: Technical Architect (Spec Writer)
    When a request is complex or vague:
    1. DO NOT write code.
    2. INTERVIEW the user for missing context (Files, Constraints, Goal).
    3. GENERATE a Phased GSD Spec using the `TASK_SPEC.md` template.
    4. MANDATE approval after each Phase.
    
    [ADD CONDITIONAL CONSTRAINT FROM TASK 1.2 HERE IF NEGATIVE]
    ```
  - Add logic to identify "Vibe" requests.
- [ ] **Task 2.2: Generate Agent-Specific Configuration Files**
  - Based on Target Agent (Task 1.1):
    - **GitHub Copilot:** `.github/copilot-instructions.md`
    - **Cursor:** `.cursorrules`
    - **Claude/Gemini/Custom:** `AGENTS.md` (Referencing the SDD protocol).

**Phase 2 Verification:**
1.  `SPEC_WRITER.md` enforces phasing and refusal logic.
2.  Agent-specific config created and correctly references SDD.
3.  **STOP: Wait for Human Approval.**

---

## Phase 3: Protocol Integration
*Goal: Formalize the "Phased Spec-First" rule in core developer documentation.*

- [ ] **Task 3.1: Update Communication Rules**
  - Update `COMMUNICATION.md` to include the "Complexity Threshold" (e.g., > 2 files or architectural changes require a Phased Spec).
- [ ] **Task 3.2: Finalize System Prompt Manifest**
  - Ensure the root AI manifest (e.g., `AGENTS.md`) is updated to include the Spec-First requirement.

**Phase 3 Verification:**
1.  Review `COMMUNICATION.md` and `AGENTS.md` diffs.
2.  **STOP: Wait for Human Approval.**

---

## Phase 4: Workflow Validation
*Goal: Prove the system prevents Vibe Coding in the targeted environment.*

- [ ] **Task 4.1: Simulation Test**
  - **Trigger:** "Update the Directory model to use Moshi and Kotlin."
  - **Expected Result:** Agent refuses code, interviews user, and produces a Phased GSD Spec.

**Phase 4 Verification:**
1.  Success if Agent follows protocol.
2.  **FINAL APPROVAL: Implementation Complete.**
