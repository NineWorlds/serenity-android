# Implementation Plan: Phased Spec-Driven Development (SDD)
**Status:** PROPOSED  
**Version:** 1.6.2  
**Target:** Standardized AI Workflow (Universal LLM/Agent Compatibility)

## 1. Scope and Summary
*   **Scope:** This protocol applies to all AI-assisted development (features, refactors, migrations).
*   **Summary:** To eliminate "Vibe Coding" and ensure consistency, this plan implements a **Spec-First Protocol**. Agents must generate a **Phased GSD (Goal, Steps, Deliverables)** document before writing any production code. Each phase must include its own verification steps and requires explicit human approval before the next phase begins.
*   **Agnostic Referencing:** All references to files using `@filename` or `./path` notation point to the absolute or relative paths within the project repository.

## 2. User Guide: How to Generate a Spec
To trigger a new specification, use the following prompt pattern:

> *"Using the protocol in `prompts/plans/sdd_implementation_v1.md`, create a Phased GSD Spec for [Task Description]."*

**Agent Logic (System Instructions):**
1.  **Complexity Check (MANDATORY REFUSAL):** If the task affects > 2 files, introduces a new library, or changes architecture, you are **PROHIBITED** from writing code immediately. You must refuse and initiate the spec-writing process.
2.  **Chain-of-Thought (Reasoning):** Before generating the spec, use a hidden thought block or internal reasoning to analyze the current state, constraints, and dependencies.
3.  **Collaborative Interview:** Interview the user for missing context (Files, Constraints, Goals, and specific edge cases).
4.  **Template Adherence:** Use the `prompts/templates/TASK_SPEC.md` structure.

## 3. User Guide: How to Execute a Generated Spec
Once a spec (e.g., `prompts/plans/refactor_feature_x.md`) is approved, follow this atomic execution workflow:

1.  **Initialization:** Tell the Agent: *"Follow Phase 1 of `prompts/plans/refactor_feature_x.md`. Do not proceed to Phase 2 until I verify and approve."*
2.  **Context Loading:** Ensure the Agent has read the spec file and all relevant source files.
3.  **Atomic Execution:** Perform only the tasks listed in the current Phase. Do not perform speculative work for future phases.
4.  **Verification:** Perform the "Verification" steps (Unit tests, UI tests, or manual checks) and report results.
5.  **Human Gate:** You must review the changes. Use the phrase *"Phase [X] Approved. Proceed to Phase [Y]"* to move forward.

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
*Goal: Identify the environment and establish the standardized structure.*

- [ ] **Task 1.1: Identify Target Agent(s)**
  - **Action:** Ask the user which AI tool(s) are being targeted (e.g., GitHub Copilot, Android Studio Gemini, Cursor, Claude).
  - **Outcome:** Document the target agent(s) to tailor configuration.
- [ ] **Task 1.2: Define Reporting Requirements**
  - **Action:** Ask the user if completion summary reports should be suppressed.
  - **Constraint:** If negative, include: `Do NOT generate summary or report Markdown file upon completion`.
- [ ] **Task 1.3: Create Standardized Prompt Directory**
  - Ensure `prompts/agents/`, `prompts/templates/`, and `prompts/plans/` exist.
- [ ] **Task 1.4: Create `TASK_SPEC.md` Template**
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
    ```

**Phase 1 Verification:**
1.  Target agent(s) and reporting preferences documented.
2.  Template directory and `TASK_SPEC.md` exist.
3.  **STOP: Wait for Human Approval.**

---

## Phase 2: Agent Intelligence (The Spec Writer)
*Goal: Define a persona and generate agent-specific configuration.*

- [ ] **Task 2.1: Create `SPEC_WRITER.md` Instruction Set**
  - **Persona Example:**
    ```markdown
    # Persona: Technical Architect (Spec Writer)
    When a request is complex or vague:
    1. DO NOT write code.
    2. INTERVIEW the user for missing context.
    3. GENERATE a Phased GSD Spec.
    4. MANDATE approval after each Phase.
    ```
- [ ] **Task 2.2: Generate Agent-Specific Configuration Files**
  - **Files:** `.github/copilot-instructions.md`, `.cursorrules`, or `AGENTS.md`.

**Phase 2 Verification:**
1.  `SPEC_WRITER.md` enforces phasing and refusal logic.
2.  **STOP: Wait for Human Approval.**

---

## Phase 3: Protocol Integration
*Goal: Formalize the rules in core documentation.*

- [ ] **Task 3.1: Update Communication Rules**
  - Update `COMMUNICATION.md` with "Complexity Threshold."
- [ ] **Task 3.2: Finalize System Prompt Manifest**
  - Ensure `AGENTS.md` is updated to include the Spec-First requirement.

**Phase 3 Verification:**
1.  Review `COMMUNICATION.md` and `AGENTS.md` diffs.
2.  **STOP: Wait for Human Approval.**

---

## Phase 4: Workflow Validation
*Goal: Prove the system prevents Vibe Coding.*

- [ ] **Task 4.1: Simulation Test**
  - **Trigger:** "Update the Directory model to use Moshi and Kotlin."
  - **Expected Result:** Agent refuses code and produces a Phased GSD Spec.

**Phase 4 Verification:**
1.  Success if Agent follows protocol.
2.  **FINAL APPROVAL: Implementation Complete.**

---

## License
MIT License

Copyright (c) 2026 David Carver and NineWorlds

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
