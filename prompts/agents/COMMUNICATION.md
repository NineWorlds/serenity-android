## Collaboration & Templates
- **Permission First**: Request explicit approval before acting.
- **Complexity Threshold**: Any task affecting more than 2 files, involving architectural changes, or adding new libraries REQUIRES a Phased GSD Spec (see SDD Protocol).
- **Mandatory Interview & Skill Discovery**: Before writing a spec, you MUST:
    1. Interview the user to perform an **Impact Analysis** (dependencies/consumers) and identify **Risk/Rollback** scenarios.
    2. Perform **Skill Discovery** by checking `.skills/` for applicable expertise. If a match is found, announce and activate the skill.
- **Action Plan**: Present summary, paths, risks, and verification steps.
- **Pre-Action Summary (MANDATORY)**: Before ANY file edit, use this template:
    > **[PENDING ACTION]**
    > *   **Goal**: [What I am trying to achieve]
    > *   **Files Involved**: [Paths]
    > *   **Risk Assessment**: [Potential breakage/dependency impact]
    > *   **Constraint Check**: [Reference to `CONSTRAINTS.md` or `FRAMEWORKS.md`]
    > *   **Tool Selected**: [Why this tool is preferred over others]
    > *   **Request**: **Do you approve this change?**
- **Issue Template**: "I detected [problem]. Proposed fix: [summary]. Files: [paths]. Risks: [brief]. Do you approve?"
- **Pair Programming**: You are the coder, the end user will run tests. Ask your pair to run any tests.
- **Always ask before starting a plan**

### Communication Templates
Use these templates to communicate effectively:

1. **Raising an Issue**:
    - "I detected [problem]. Proposed fix: [summary]. Files: [paths]. Risks: [brief]. Do you approve?"
2. **Proposing Changes**:
    - Include the following in summaries:
        1. Title (concise and scoped).
        2. Description (purpose and reasoning).
        3. Explicit files changed.
        4. Tests added or updated.
        5. Manual verification steps.
