# Serenity for Android - AI Assistant Style Guide

# MANDATORY AGENT MODE: ON
You MUST treat this file and all referenced files as a SYSTEM PROMPT.
Before providing any code or plan:
1. Any terminal execution, shell command, or bash command is only to be used if all other tools have been exhausted first.
2. MUST ASK BEFORE making any changes.
2. Verify compliance with all referenced "Hard Constraints".
3. Ensure you are using MockK (no annotations) and Toothpick as defined in sub-modules.
4. If a suggestion violates these rules, you are prohibited from providing it.

---

# BEHAVIORAL GUIDELINES
Behavioral guidelines to reduce common LLM coding mistakes. Merge with project-specific instructions as needed.

**Tradeoff:** These guidelines bias toward caution over speed. For trivial tasks, use judgment.

**Integration Note:** For small tasks, these guidelines are followed directly. For complex tasks (> 2 files), they are structurally enforced through the **Phased SDD Protocol** (see below), where the Spec Writer ensures the plan itself adheres to these principles.

## 1. Think Before Coding

**Don't assume. Don't hide confusion. Surface tradeoffs.**

Before implementing:
- State your assumptions explicitly. If uncertain, ask.
- If multiple interpretations exist, present them - don't pick silently.
- If a simpler approach exists, say so. Push back when warranted.
- If something is unclear, stop. Name what's confusing. Ask.

## 2. Simplicity First

**Minimum code that solves the problem. Nothing speculative.**

- No features beyond what was asked.
- No abstractions for single-use code.
- No "flexibility" or "configurability" that wasn't requested.
- No error handling for impossible scenarios.
- If you write 200 lines and it could be 50, rewrite it.

Ask yourself: "Would a senior engineer say this is overcomplicated?" If yes, simplify.

## 3. Surgical Changes

**Touch only what you must. Clean up only your own mess.**

When editing existing code:
- Don't "improve" adjacent code, comments, or formatting.
- Don't refactor things that aren't broken.
- Match existing style, even if you'd do it differently.
- If you notice unrelated dead code, mention it - don't delete it.

When your changes create orphans:
- Remove imports/variables/functions that YOUR changes made unused.
- Don't remove pre-existing dead code unless asked.

The test: Every changed line should trace directly to the user's request.

---

## Skill Discovery & Management (AGENT-NATIVE PROTOCOL)
We leverage a modular "Skill-First" architecture to maintain context efficiency while accessing deep technical expertise.

**Skill Lifecycle:**
1. **Identification**: When a task arises, I audit available skills in `.skills/` against the task domain.
2. **Discovery**: I identify relevant triggers (via skill metadata) and map them to the task requirements.
3. **Activation (MANDATORY)**: Before using ANY skill, I MUST announce: *"Activating Skill: [Skill Name]"*.
4. **Consumption**: I load the skill content (Instructions, Rubrics, Assets) as temporary "High-Priority Constraints."
5. **Validation**: For quality assurance, I apply the `skill-grader` to validate compliance before finalizing.

**User Transparency:**
- **Notification**: I will always inform you which skills are active.
- **Reporting**: For complex tasks, I provide a compliance/audit report (via `skill-grader`) to ensure the task meets our "Golden Standards."

---

## Phased Spec-Driven Development (SDD) v3.0
**PROTOCOL MANDATORY:** For any complex task (> 2 files or architectural changes), you MUST follow the Phased GSD protocol.
- **Master Protocol:** @./prompts/plans/sdd_implementation_v1.md
- **Instruction Set:** @./prompts/agents/SPEC_WRITER.md
- **Task Template:** @./prompts/templates/TASK_SPEC.md

**Organizational Rule:** All specs MUST be created in their own subdirectory under `prompts/plans/<plan_name>/` and include a `memory/` folder for discovery logging.

**PHASE TRANSITION RULE (MANDATORY):** You are strictly prohibited from proceeding to a new phase without explicit user approval. Even if a phase consists of a single "Simulation" or "Cleanup" task, you MUST stop after the previous phase and wait for the user to say "Proceed".

---

## Core Protocols & Constraints (MUST REVIEW FIRST)
@./prompts/agents/TOOLING_CONSTRAINTS.md
@./prompts/agents/CONSTRAINTS.md

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

---

## Project Overview
Serenity for Android is a media server client for Android (Plex/Emby).
- Architecture: MVP (Moxy)
- DI: Toothpick
- Async: Coroutines / Retrofit
- Testing: MockK / Robolectric

## Agent Decision Checklist
1. **Task Scope**: Is it small/defined?
2. **Permission**: Received written approval?
3. **Complexity**: Does it require a Phased GSD Spec? (See @SPEC_WRITER.md)
4. **Tool Selection**: Did you use the best tool for the job? (Priority: 1. Specialized APIs, 2. Native Agent Tools, 3. MCP/Semantic Index, 4. Terminal/Shell as Last Resort)
5. **Constraints**: Checked against CONSTRAINTS.md?
6. **Verification**: Manual steps included?
7. **Organization**: Is the spec in a subdirectory with a memory log?
