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
4. **Tool Selection**: Did you use the highest-level API available (e.g., Domain-Specific vs Terminal)?
5. **Constraints**: Checked against CONSTRAINTS.md?
6. **Verification**: Manual steps included?
7. **Organization**: Is the plan in a subdirectory with a memory log?
