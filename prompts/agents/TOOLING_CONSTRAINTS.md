# AGENT CAPABILITY PROTOCOL

## 1. Filesystem Fallbacks
- **No Directory Creation Tool:** Use your `Write` tool to create a placeholder `.gitkeep` file in the target path.
- **No Delete/Move Tools:**
  1. Write the new file to the destination.
  2. Add the old file path to the "Cleanup & Manual Actions" section of the task spec for human action.

## 2. Environment Restrictions
- **No Direct Shell:** Do not assume shell access (`bash`/`zsh`).
- **Tool Discovery:** Use high-level APIs instead of terminal commands:
  - Discovery: Use `List`, `Find`, or `Search` APIs.
  - Execution: Use `Build` or `Test` APIs.
  - Device: Use `Log` or `Input` APIs.

## 3. Terminal & Human Fallback
- **Terminal as Last Resort:** If high-level APIs cannot perform a necessary action, fall back to using a terminal tool (if available).
- **Human-in-the-Loop:** If no terminal tool is available or if the operation is outside of agent capabilities:
  1. Describe the exact terminal commands required.
  2. Explain why these commands are necessary.
  3. Explicitly ask the user to run the commands.
  4. **Wait** for the user to confirm completion before proceeding.

## 4. Operational Standards
- **Read/Write Only:** Unless a delete tool is provided, assume a Read/Write-Only environment.
- **Explicit Paths:** All file operations MUST use explicit, absolute, or repository-relative paths.

## 5. Tool Selection Hierarchy
To ensure consistency and safety, always prioritize tools in the following order:

1. **Domain-Specific APIs**: Use tools designed for a specific task or lifecycle (e.g., `gradle_build`, `deploy`, `test` tools).
2. **Context-Aware IDE APIs (MCP Server)**: Use tools that leverage IDE indexing and semantic understanding (e.g., `find_usages`, `replace_text_in_file`, `get_file_problems`).
3. **General Filesystem APIs**: Use direct file read/write/list tools for basic manipulation.
4. **Terminal/Shell**: Use ONLY as a last resort for actions not supported by any of the above (e.g., complex Git operations not covered by standard tools).
