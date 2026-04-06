---
name: ui-refactor
description: Specialized skill for refactoring legacy Android views to View Binding and enforcing Leanback UI standards.
compatibility: gemini-1.5-pro, claude-3.5-sonnet
metadata:
  version: 1.0.0
---

# Skill: UI Refactor (Leanback & View Binding)

## Purpose
This skill provides deep expertise in migrating legacy Android layouts to View Binding and ensuring compliance with the project's Leanback/TV UI standards.

## Guidelines & Constraints
- **View Binding**: All `findViewById` calls must be removed.
- **Null Safety**: Use `_binding` and `binding` pattern in Fragments to avoid leaks.
- **Leanback**: Ensure focus effects are present for all interactive elements.
- **Naming**: IDs must be `snake_case`.

## Trigger Logic
- Activated when the user requests a UI refactor, View Binding migration, or new TV screen implementation.

## Instructions
1.  **Analyze Layout**: Identify all IDs and nested includes.
2.  **Generate Binding**: Use the generated binding class for the layout.
3.  **Migration**: Replace manual view lookups with binding references.
4.  **Verification**: Confirm focus behavior in D-pad navigation.

## References
- `prompts/agents/ui-standards.md`
- `prompts/agents/architecture.md`
