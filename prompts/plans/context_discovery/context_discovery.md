# [SPEC] Standardized Context Discovery

## Goal
Formalize "Tribal Knowledge" into "Golden Standards" through a heuristic-based discovery process. This plan identifies architectural "Gravity Wells," documents anti-patterns, and maps cross-module protocols to ensure consistency and formalize project intelligence in a language/framework-agnostic manner.

## Context
- **Current State:** The project has implementation-defined standards that may be implicitly understood but lack formal documentation in AI system prompts.
- **Constraints:** This plan is language and framework agnostic. The executing agent must determine the specific tech stack through probing and adapt its discovery methodology accordingly.
- **SDD Memory:** All findings, surprising dependencies, and architectural debt must be logged in `prompts/plans/context_discovery/memory/`.

## Standards Compliance
- [ ] **Architecture**: Spec-First approach.
- [ ] **Organization**: Dedicated plan directory with memory log.

## Impact Surface
- **Dependencies:** All project-level standards (e.g., `prompts/agents/*`).
- **Consumers:** Future AI agent sessions, developer onboarding, and code review processes.

## Risk & Rollback
- **Potential Risks:** Misidentifying legacy code as a current standard; failing to capture subtle architectural boundaries or "Tribal Knowledge."
- **Rollback Strategy:** Revert documentation changes via Git.

## Phase 1: Project Profiling & Heuristic Probing
*Goal: Identify the project's technical stack, architectural "Gravity Wells," and anti-patterns to inform the creation of Golden Standards.*

### Tasks
- [ ] **Task 1.1: Stack & Ecosystem Identification**
- [ ] **Task 1.2: 'Gravity Well' & Architecture Mapping**
- [ ] **Task 1.3: Anti-Pattern & Legacy Pattern Identification**
- [ ] **Task 1.4: Infrastructure, Asset & Multi-Module Mapping**
- [ ] **Task 1.5: Tribal Knowledge Synthesis & User Interview**

### Verification
- `memory/discovery_log.md` contains a confirmed "Project Profile" and categorized list of patterns and rules.
- User has confirmed the "Golden Standards" vs "Anti-Patterns" identified during the probe.
### [WAIT FOR APPROVAL]

## Phase 2: Extraction & Golden Standard Formalization
*Goal: Translate the discovery findings and user feedback into formal documentation and AI system prompts.*

### Tasks
- [ ] **Task 2.1: Formalize Architectural & Communication Standards**
- [ ] **Task 2.2: Formalize Resource, UI & Asset Standards**
- [ ] **Task 2.3: Document Forbidden Patterns**
- [ ] **Task 2.4: Formalize Testing & Infrastructure Standards**

### Verification
- Project standard files (e.g., in `prompts/agents/`) accurately reflect the "Golden Standards" derived from implementation.
### [WAIT FOR APPROVAL]

## Phase 3: Validation Dry-Run
*Goal: Validate the new documentation by 'mapping' it against real-world complexity to identify remaining gaps.*

### Tasks
- [ ] **Task 3.1: Selection of Validation Targets**
- [ ] **Task 3.2: Implementation Mapping & Gap Analysis**
- [ ] **Task 3.3: Final Refinement**

### Verification
- `memory/validation_report.md` documents the mapping process and confirms standard coverage for the complex target files.
### [WAIT FOR APPROVAL]

## Final Deliverables
- Comprehensive project standards suite (e.g., in `prompts/agents/`).
- `memory/discovery_log.md` (Evidence of discovery process).
- `memory/validation_report.md` (Proof of accuracy and coverage).
