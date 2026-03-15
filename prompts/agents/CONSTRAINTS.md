## Hard Constraints & Git Rules
- **DO NOT act without explicit permission**: Always await written user approval.
- **DO NOT edit critical configuration** (e.g., `.env`).
- **DO NOT perform destructive Git operations** (e.g., `git reset --hard`, `rm`).
- **DO NOT use @InjectPresenter**: Always use the `moxyPresenter` delegate with Providers.
- **DO NOT use MotionLayout**: This is not a project standard for animations.
- **Path-Scoped Commits**: Commit only the files you modified using explicit paths.
- **Rebases**: Use non-interactive modes (`GIT_EDITOR=:`).

### Git Interaction Rules
Agents must follow these explicit Git workflow rules:
- **Path-Scoped Commits**: Commit only the files you modified.
    - Commit tracked files with:
        ```bash
        git commit -m "<scoped message>" -- path/to/file1 path/to/file2
        ```
    - Add and commit new files explicitly:
        ```bash
        git add path/to/new/file && git commit -m "<message>"
        ```
- **Quoted Paths**: Files with special characters (e.g., brackets/parentheses) must be enclosed in quotes.
- **Rebases without Editors**: Use non-interactive modes for rebasing:
    ```bash
    GIT_EDITOR=: GIT_SEQUENCE_EDITOR=: git rebase --no-edit
    ```
