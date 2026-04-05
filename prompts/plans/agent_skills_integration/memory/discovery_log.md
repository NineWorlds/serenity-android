# Skill Migration Log

## Migration Summary
- Legacy skills moved from `prompts/skills/` to `.skills/`.
- Discovery logic updated in all core documentation (`AGENTS.md`, `sdd_implementation_v1.md`, `SPEC_WRITER.md`, `COMMUNICATION.md`) to point to `.skills/`.
- All agents now instructed to prefer native skill loading (if available) or fall back to manual discovery via `.skills/`.
