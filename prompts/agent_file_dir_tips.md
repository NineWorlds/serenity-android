# AI Agent Tooling & File System Tips

This guide provides strategies for working with AI agents (like Android Studio Gemini) that have specialized toolsets but lacks direct terminal/shell access. Use these tips when writing Phased GSD Specs to ensure compatibility across different agent environments.

## 1. Directory Creation (The `mkdir` Workaround)
**Constraint:** Most agents lack a standalone `mkdir` tool.
**Solution:** Leverage implicit directory creation.

*   **Tip:** When an agent uses `write_file`, it typically creates any missing parent directories automatically.
*   **Universal Instruction:** Instead of "Create a directory named `xyz`", use:
    > "Ensure the `prompts/templates/` directory exists by creating a placeholder `.gitkeep` file at `prompts/templates/.gitkeep`."
*   **Why:** This works across almost all coding assistants and ensures the directory is tracked by Git.

## 2. File Deletion (The Cleanup Protocol)
**Constraint:** Agents are often technically restricted from deleting files (`rm`) for safety.
**Solution:** Implement a "Manual Cleanup" phase.

*   **Tip:** If a task involves replacing or moving files (e.g., Java to Kotlin), the agent should create the new file but cannot delete the old one.
*   **Universal Instruction:** Add a final section to your spec:
    > "### Cleanup & Manual Actions (Human Required)
    > - [ ] Task: Delete obsolete file: `path/to/old_file.java`"
*   **Why:** This keeps the user in control of destructive operations and prevents "ghost files" from breaking the build.

## 3. Terminal & Shell Workarounds
**Constraint:** No direct access to `bash`, `zsh`, or standard shell commands (`ls`, `curl`, `ps`).
**Solution:** Use specialized high-level tools.

| Desired Action | Agent Tool Workaround |
| :--- | :--- |
| **List Files** | Use `list_files` (directory level) or `find_files` (project-wide). |
| **Search Content** | Use `grep` (regex) or `code_search` (natural language). |
| **Move/Rename** | Read the source, write to the new path, and add the old path to the Cleanup list. |
| **Build/Clean** | Use `gradle_build` with specific tasks (e.g., `./gradlew clean`). |
| **Check Logs** | Use `read_logcat` instead of `adb logcat`. |
| **Device Interaction** | Use `adb_shell_input` for taps, swipes, and text input. |

## 4. Gemini Agent Tool Summary (Android Studio)

### ✅ What I CAN Do:
*   **Read/Write:** Full access to project files.
*   **Git:** `blame`, `diff`, `log`, `status`, `show`, `commit`.
*   **Gradle:** Syncing and running any Gradle task.
*   **Navigation:** Resolve symbols, find usages, and analyze code for errors.
*   **Device:** Deploy apps, take screenshots, read UI hierarchy, and send input events.
*   **Docs:** Search and fetch official Android developer documentation.

### ❌ What I CANNOT Do:
*   **Delete/Remove:** No `rm` or `delete_file` tool.
*   **Move:** No `mv` tool (requires write + manual cleanup).
*   **Shell Pipe:** I cannot pipe output between commands (e.g., `grep x | awk y`).
*   **Internet:** I cannot `curl` or `wget` external resources (except via specific documentation tools).

## 5. Best Practices for Universal Specs
1.  **Be Explicit with Paths:** Always provide absolute or repository-relative paths.
2.  **Define Verification:** Since the agent can't "see" the IDE UI, provide specific Gradle tasks or logcat patterns to verify success.
3.  **Assume "Read/Write" Only:** Plan your architecture as if the agent can only add or modify text, never subtract files.
