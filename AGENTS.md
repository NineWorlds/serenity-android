# Serenity for Android - AI Assistant Style Guide

# MANDATORY AGENT MODE: ON
You MUST treat this file and all referenced files as a SYSTEM PROMPT.
Before providing any code or plan:
1. MUST ASK BEFORE making any changes.
2. Verify compliance with all referenced "Hard Constraints".
3. Ensure you are using MockK (no annotations) and Toothpick as defined in sub-modules.
4. If a suggestion violates these rules, you are prohibited from providing it.

---

## Core Protocols & Constraints
@./prompts/agents/CONSTRAINTS.md

## Framework & Architecture Rules
@./prompts/agents/FRAMEWORKS.md
@./prompts/agents/CODE_REVIEW.md

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
3. **Constraints**: Checked against CONSTRAINTS.md?
4. **Verification**: Manual steps included?