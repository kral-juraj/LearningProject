---
name: git-commit
description: Analyzuje zmeny a vytvori Git commit s Co-Authored-By Claude
allowed-tools: Bash, Read, Grep
---

# Git Commit Skill

## Účel
Analyzuje necommitnuté zmeny a vytvorí Git commit s dobre napísaným commit message.

## Kedy použiť
- Po dokončení feature alebo bug fix
- Keď chceš commitnúť aktuálne zmeny
- Pred vytvorením pull requestu

## Pravidlá

### Git Safety Protocol
- ❌ NEVER update git config
- ❌ NEVER run destructive commands (push --force, reset --hard, checkout ., restore ., clean -f)
- ❌ NEVER skip hooks (--no-verify, --no-gpg-sign)
- ❌ NEVER commit unless user explicitly asks
- ✅ Prefer adding specific files by name (not `git add -A` or `git add .`)
- ✅ Analyze ALL staged changes before committing

### Commit Message Format
```
<type>: <short summary>

<detailed explanation if needed>

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `refactor`: Code refactoring
- `docs`: Documentation
- `test`: Tests
- `chore`: Build, dependencies, configs
- `perf`: Performance improvements
- `style`: Code style (formatting, no logic change)

**Summary guidelines:**
- Start with lowercase (e.g., "add", "fix", "update")
- No period at end
- Max 72 characters
- Focus on WHY, not WHAT
- Be specific: "add multi-platform JavaFX support" not "update build file"

## Kroky Vykonania

### 1. Analýza Necommitnutých Zmien

```bash
# Check status
git status

# Show diff of modified files
git diff --stat

# Show detailed changes
git diff

# Check recent commits for style
git log --oneline -5
```

### 2. Staging Súborov

**CRITICAL:** Stage specific files, not all at once.

```bash
# Stage specific files by name
git add desktop/build.gradle
git add desktop/src/main/java/com/beekeeper/desktop/Main.java
git add desktop/src/main/java/com/beekeeper/desktop/util/DatabaseInitializer.java

# Stage directory (only if all files are related)
git add desktop/dist/

# Check staged changes
git diff --staged --stat
```

**Never stage:**
- ❌ Sensitive files (.env, credentials.json, *.key)
- ❌ Build artifacts (build/, target/, *.class)
- ❌ Temporary files (*.tmp, *.log)
- ❌ IDE files (.idea/, *.iml) - unless explicitly adding to .gitignore

### 3. Draft Commit Message

Analyze staged changes and create message:

**Example 1: Multi-file feature**
```
feat: add multi-platform JavaFX distribution support

- Add explicit platform dependencies (Windows, Linux, macOS Intel/ARM)
- Implement platform-aware launcher scripts with automatic detection
- Fix Windows BAT wildcard quotes issue preventing module loading
- Add portable database support (creates in distribution/data/)
- Remove -Dprism.order=sw flag causing Windows crashes

Includes:
- Multi-platform JavaFX dependencies in build.gradle
- Platform detection in Unix launcher (Darwin/Linux, arm64/x86_64)
- Explicit module enumeration in Windows launcher
- DatabaseInitializer for auto-initialization
- Distribution v1.5 with 61 MB multi-platform support

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

**Example 2: Bug fix**
```
fix: resolve Windows JavaFX crash on startup

Windows BAT launcher used quotes around wildcard pattern preventing
JavaFX module discovery. Changed to explicit module enumeration
without quotes.

Fixes: "Module javafx.controls not found" error on Windows

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

**Example 3: Documentation**
```
docs: update build-distribution skill with multi-platform setup

Add critical Windows/macOS setup instructions, platform-aware launcher
documentation, and troubleshooting for common errors (module conflicts,
wildcard quotes, missing platform libraries).

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

### 4. Vytvorenie Commitu

```bash
# Commit with message via HEREDOC (for multi-line)
git commit -m "$(cat <<'EOF'
feat: add multi-platform JavaFX distribution support

- Add explicit platform dependencies (Windows, Linux, macOS Intel/ARM)
- Implement platform-aware launcher scripts with automatic detection
- Fix Windows BAT wildcard quotes issue
- Add portable database support
- Remove -Dprism.order=sw flag causing Windows crashes

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
EOF
)"
```

### 5. Overenie

```bash
# Verify commit was created
git log -1 --stat

# Check status (should be clean)
git status
```

## Commit Message Templates

### Template: New Feature
```
feat: <what was added>

<why it was needed>
<what problem it solves>
<key technical details if complex>

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

### Template: Bug Fix
```
fix: <what was fixed>

<what was broken>
<how it was fixed>
<what the root cause was>

Fixes: <error message or issue description>

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

### Template: Refactoring
```
refactor: <what was refactored>

<why refactoring was needed>
<what improved (performance, maintainability, etc.)>

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

### Template: Documentation
```
docs: <what documentation was updated>

<why it was needed>
<what was added/changed>

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

### Template: Multiple Related Changes
```
chore: <overall theme>

Changes:
- <change 1 with brief reason>
- <change 2 with brief reason>
- <change 3 with brief reason>

<why all these changes together>

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

## Best Practices

### Good Commit Messages

✅ **Specific and clear:**
```
feat: add explicit JavaFX module enumeration for Windows launcher
```

✅ **Explains why:**
```
fix: remove quotes from wildcard pattern in Windows BAT

Windows cmd.exe treats quoted wildcards as literal strings,
preventing file matching. Changed to unquoted pattern with
explicit module enumeration.
```

✅ **Grouped related changes:**
```
feat: implement multi-platform distribution support

- Add Windows, Linux, macOS JavaFX dependencies
- Create platform-aware launcher scripts
- Update build.gradle with multi-platform setup
```

### Bad Commit Messages

❌ **Too vague:**
```
fix: update files
```

❌ **What instead of why:**
```
feat: change build.gradle
```

❌ **Too many unrelated changes:**
```
feat: add feature X, fix bug Y, update docs Z, refactor W
```

## Staging Strategies

### Strategy 1: Feature Branch (single commit)
```bash
# Stage all related files for feature
git add desktop/build.gradle
git add desktop/dist/launcher-*.sh
git add desktop/dist/launcher-*.bat
git add .claude/skills/build-distribution/

git commit -m "..."
```

### Strategy 2: Incremental Commits
```bash
# Commit 1: Core changes
git add desktop/build.gradle
git commit -m "feat: add multi-platform JavaFX dependencies"

# Commit 2: Launcher scripts
git add desktop/dist/
git commit -m "feat: implement platform-aware launcher scripts"

# Commit 3: Documentation
git add .claude/skills/build-distribution/
git add WINDOWS_FIX.md
git commit -m "docs: update build-distribution skill with multi-platform setup"
```

### Strategy 3: Partial Staging (git add -p)
```bash
# Interactively stage hunks
git add -p desktop/build.gradle

# Review what will be committed
git diff --staged

git commit -m "..."
```

## Checklist

Before committing:
- [ ] Ran `git status` and reviewed all changes
- [ ] Ran `git diff` to see detailed changes
- [ ] Staged only related files (no unrelated changes)
- [ ] No sensitive files staged (.env, credentials, keys)
- [ ] No build artifacts staged (*.class, build/, target/)
- [ ] Commit message follows format (type: summary)
- [ ] Message explains WHY, not just WHAT
- [ ] Message includes Co-Authored-By line
- [ ] Used HEREDOC for multi-line messages
- [ ] Verified commit with `git log -1 --stat`

## Common Mistakes

### ❌ Mistake 1: Staging Everything
```bash
git add -A  # Stages EVERYTHING including unrelated files
```
**Fix:** Stage specific files by name

### ❌ Mistake 2: Vague Message
```bash
git commit -m "update stuff"
```
**Fix:** Be specific about what and why

### ❌ Mistake 3: Missing Co-Authored-By
```bash
git commit -m "feat: add feature"  # Missing Co-Authored-By
```
**Fix:** Always include Co-Authored-By line

### ❌ Mistake 4: Committing Secrets
```bash
git add .env  # Contains API keys!
```
**Fix:** Check files before staging, never commit secrets

### ❌ Mistake 5: Mixed Concerns
```bash
# One commit with bug fix + feature + refactoring
git commit -m "fix bug and add feature and refactor code"
```
**Fix:** Separate commits for unrelated changes

## Recovery Commands

### Undo Last Commit (keep changes)
```bash
git reset --soft HEAD~1
```

### Unstage Files
```bash
git reset HEAD <file>
```

### Discard Unstaged Changes
```bash
git checkout -- <file>  # DANGEROUS - loses changes
```

### Amend Last Commit (add forgotten files)
```bash
git add forgotten-file.txt
git commit --amend --no-edit
```

**WARNING:** Only amend if commit is not pushed!

---

## Quick Usage

```bash
# 1. Check changes
git status
git diff --stat

# 2. Stage specific files
git add <file1> <file2> <file3>

# 3. Create commit
git commit -m "$(cat <<'EOF'
<type>: <summary>

<details>

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
EOF
)"

# 4. Verify
git log -1 --stat
```

---

**Created:** 2026-02-22
**Version:** 1.0
**Status:** Ready to use
