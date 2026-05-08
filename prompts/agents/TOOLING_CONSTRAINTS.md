# AGENT CAPABILITY PROTOCOL

## 1. Tool Selection Strategy: "Best Tool for the Job"
You should not be dogmatic about any single protocol. Instead, choose the tool that provides the highest fidelity, safety, and efficiency for the specific task at hand.

### Hierarchy of Preference:
1. **Internal Native Agent Tools**: Use native, platform-provided capabilities first. These are "closest to the model" and highly optimized. Only bypass these if a specialized tool provides significantly higher precision, context, or safety for the specific task.
2. **Domain-Specific Specialized Tools**: Use tools built for the Android/Gradle lifecycle (e.g., `gradle_build`, `deploy`, `test`, `render_compose_preview`). These understand the project state better than generic tools.
3. **Standardized Interfaces (MCP)**: Use MCP-based tools (e.g., `find_usages`, `analyze_current_file`) when you need IDE-level semantic understanding that isn't provided natively.
4. **General Filesystem APIs**: Use basic `read_file`, `write_file`, and `list_files` for simple I/O when semantic understanding isn't required.
5. **Terminal/Shell (Mandatory Last Resort)**: Use the terminal **only** after verifying that no higher-level API (tools 1-4) can perform the action. Before initiating a `run_shell_command`, you **must** explicitly document in the task spec which tools from categories 1-4 you attempted (or why they are inapplicable) and confirm the failure of those approaches.

## 2. Filesystem & Environment Safety
- **No Directory Creation Tool:** Use your `Write` tool to create a placeholder `.gitkeep` file in the target path.
- **No Delete/Move Tools:**
  1. Write the new file to the destination.
  2. Add the old file path to the "Cleanup & Manual Actions" section of the task spec for human action.
- **Explicit Paths:** All file operations MUST use explicit, absolute, or repository-relative paths.

## 3. Terminal & Human Fallback
- **Terminal as Absolute Last Resort**: Terminal commands (`run_shell_command`) are forbidden until you have exhausted all specialized, native, and MCP tools. If a shell command is required, you must first state: "I have attempted [Tool A, Tool B] and they were unable to resolve this. I am now proceeding to shell execution as a last resort."
- **Human-in-the-Loop:** If no terminal tool is available or if the operation is outside of agent capabilities:
  1. Describe the exact terminal commands required.
  2. Explain why these commands are necessary.
  3. Explicitly ask the user to run the commands.
  4. **Wait** for the user to confirm completion before proceeding.

## 4. Context Economy
- **Avoid Context Bloat**: Do not request massive amounts of data via generic search tools if a specific tool (like `resolve_symbol`) can give you the exact information you need.
- **Lazy Discovery**: Prioritize the "Pull" model (reading metadata first) before loading full file contents.
