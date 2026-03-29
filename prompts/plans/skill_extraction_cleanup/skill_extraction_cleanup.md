# [SPEC] Agent Skills Extraction and Refactor (v7 - Non-Destructive)

## Goal
Decompose detailed implementation expertise into modular, discoverable skills while maintaining high-level "Do's and Don'ts" (Hard Constraints) in the original agent documentation. This ensures context economy by offloading "how-to" while keeping "what-to" as always-on context.

## Context
- **Status**: Awaiting Approval
- **Non-Destructive**: Original agent files will be refactored/simplified, NOT deleted.
- **Core Protocols**: `CONSTRAINTS.md`, `TOOLING_CONSTRAINTS.md`, `COMMUNICATION.md`, `constitution.md`, and `SPEC_WRITER.md` remain as primary anchor files.

## Standards Compliance
- [ ] **Skill Structure**: Mandatory YAML frontmatter.
- [ ] **Discovery**: Skills placed in `prompts/skills/<skill-name>/SKILL.md`.
- [ ] **Context Economy**: Agent files reduced to essential constraints; implementation details moved to skills.
- [ ] **Size Constraint**: **Each skill MUST be at most 500 lines long.**

## Content Mapping (Redistribution)

| Agent File | Keep in Agent File (Do's & Don'ts) | Move to Skill (Implementation/Expertise) | Target Skill |
| :--- | :--- | :--- | :--- |
| `TESTING.md` | Core Rules, DI requirements (when to extend `InjectingTest`). | Mock setup examples, Toothpick cleanup code, detailed JUnit 4 setup. | `testing-mockk` |
| `ui-standards.md` | Layout naming conventions, ID standards, Focus effect mandate. | View Binding implementation code, Glide configuration, XML animation details. | `ui-leanback` |
| `architecture.md` | Non-Negotiables, Repository pattern mandate, Data Flow high-level rules. | Module boundary details, DI entry point code, Repository code examples. | `architecture-core` |
| `FRAMEWORKS.md` | Code Style (Kotlin, Spotless). | DI mechanics, MVP delegate patterns, View Binding usage, Animation/Glide details. | `framework-toothpick`, `framework-moxy`, `ui-leanback` |
| `CODE_REVIEW.md` | Checklist summary/Assessment levels. | Detailed review criteria and finding templates. | `code-reviewer` |
| `communication-protocol.md` | Moshi/Retrofit mandates, Error handling rules. | Moshi adapter code, Retrofit interface examples, `executeOrThrow` implementation. | `network-retrofit` |

## Phase 1: Skill Extraction (Implementation)
### Tasks
- [ ] **Task 1.1: Extract `testing-mockk`**
- [ ] **Task 1.2: Extract `ui-leanback`**
- [ ] **Task 1.3: Extract `architecture-core`**
- [ ] **Task 1.4: Extract `framework-toothpick`**
- [ ] **Task 1.5: Extract `framework-moxy`**
- [ ] **Task 1.6: Extract `network-retrofit`**
- [ ] **Task 1.7: Extract `code-reviewer`**

### Verification
- Present the body of all 7 new skills for user review.
- Confirm each skill contains the "How-to" and "Implementation Examples".
- **[WAIT FOR APPROVAL]**

## Phase 2: Agent File Refactoring (The "Simplified Guide")
### Tasks
- [ ] **Task 2.1: Refactor `TESTING.md`** (Strip to rules).
- [ ] **Task 2.2: Refactor `architecture.md`** (Strip to non-negotiables).
- [ ] **Task 2.3: Refactor `ui-standards.md`** (Strip to conventions).
- [ ] **Task 2.4: Refactor `communication-protocol.md`** (Strip to protocol mandates).
- [ ] **Task 2.5: Refactor `CODE_REVIEW.md`** (Strip to checklist).
- [ ] **Task 2.6: Update `CONSTRAINTS.md`** (Incorporate framework-agnostic style rules from `FRAMEWORKS.md`).

### Verification
- Original files are now concise, focusing only on "What" (Rules/Constraints) rather than "How".
- **[WAIT FOR APPROVAL]**

## Phase 3: Cleanup & Integration
### Tasks
- [ ] **Task 3.1: Finalize `FRAMEWORKS.md`**
    - Since all implementation is in skills and style is in `CONSTRAINTS.md`, reduce this to a simple mapping of frameworks to skills.
- [ ] **Task 3.2: Update `AGENTS.md`**
    - Maintain links to simplified agent files.
    - Ensure the "Skill Discovery" section is prominent for implementation help.
- [ ] **Task 3.3: Update `TASK_SPEC.md`**

## Final State of `prompts/agents/`
- All files remain, but their content is strictly limited to **Hard Constraints** and **Do's/Don'ts**. 
- Complexity and implementation logic are offloaded to the `prompts/skills/` directory.
