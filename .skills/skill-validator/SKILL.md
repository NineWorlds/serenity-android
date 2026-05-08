---
name: skill-validator
description: "Validating agent skills against the agentskills.io specification and generating compliance reports."
metadata:
  triggers: "skill-audit, validate-skill, check-compliance"
---

# Skill Validator Standards

## Purpose
This skill provides the logic required to audit and enforce compliance for ANY agent skill within the `serenity-android` project according to `agentskills.io` standards.

## The "Strict" Protocol (LLM-Driven Audit)
As a strict validator, I assume every skill is in violation until proven otherwise. I perform an exhaustive scan via the following protocols:

### 1. Mandatory/Optional Metadata Audit
- **Mandatory**:
  - `name`: 1-64 chars, lowercase alphanumeric + hyphens only. No consecutive hyphens, no start/end hyphens. MUST match parent folder name.
  - `description`: 1-1024 chars, non-empty.
- **Optional**:
  - `license`: Short description or reference to file (e.g., `LICENSE.txt`).
  - `compatibility`: 1-500 chars (if present).
  - `metadata`: Arbitrary key-value map (string:string).
  - `allowed-tools`: Space-delimited list of pre-approved tools (experimental).
- **Token Limit**: Metadata MUST be < 100 tokens.

### 2. Structural & Reference Integrity
- **Mandatory**: `tmp_SKILL.md` present.
- **Assets/References**: 
  - `assets/` MUST ONLY contain operational files (templates, resources).
  - `references/` MUST contain structured markdown or readable text.
- **Reference Management**:
  - All file references inside `tmp_SKILL.md` MUST use relative paths (e.g., `./references/doc.md`).
  - **Constraint**: No parent traversal (`../`). Chain depth MUST be 1 level.
  - **Audit**: Identify absolute paths or invalid relative links as violations.

### 3. Progressive Disclosure & Token Economy
- **Size**: `tmp_SKILL.md` MUST be < 500 lines.
- **Heuristic for References**:
  - Flag long code blocks (> 50 lines), large tables, or deep technical manuals.
  - **Suggestion**: Propose moving flagged content to `references/` to preserve context economy.
- **Token Limit**: Instructions (body) MUST be < 5000 tokens.

## Reporting & Output
Upon completion, I generate an HTML report with a table of results that includes both compliance findings (violations) and structural strengths (best practices/adherence). The report uses the template at `assets/audit_report_template.html`.

## Operational Workflow
1. **Exhaustive Scan**: The agent reads the target `tmp_SKILL.md`, `references/`, and `assets/`.
2. **Strict Reporting**: The agent generates the HTML report using the template and writes it to `${PROJECT_ROOT}/skill_audit.html`.
3. **Compliance Level**: Strict (Pass/Fail).

## Compliance
- For details, see `https://agentskills.io/specification`.
- **Variable Resolution Policy**: `${PROJECT_ROOT}` refers to the absolute path of the project root. Defaults to current working directory if not set.
