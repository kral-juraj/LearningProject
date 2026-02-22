# Git Commit - Quick Reference

## ⚡ Quick Commit

```bash
# 1. Check changes
git status
git diff --stat

# 2. Stage files (specific, not all)
git add desktop/build.gradle
git add desktop/src/main/java/com/beekeeper/desktop/Main.java

# 3. Commit with message
git commit -m "$(cat <<'EOF'
feat: add multi-platform JavaFX support

- Add Windows, Linux, macOS dependencies
- Implement platform-aware launchers
- Fix Windows BAT wildcard quotes

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
EOF
)"

# 4. Verify
git log -1 --stat
```

---

## 📝 Message Format

```
<type>: <summary>

<optional details>

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

**Types:** feat, fix, refactor, docs, test, chore, perf, style

---

## ✅ Good Messages

```
feat: add explicit JavaFX module enumeration for Windows
```

```
fix: remove quotes from wildcard pattern in Windows BAT

Quotes around wildcard prevented file matching in cmd.exe.
Changed to explicit module enumeration without quotes.
```

```
docs: update build-distribution skill with multi-platform setup
```

---

## ❌ Bad Messages

```
fix: update files                    # Too vague
```

```
feat: change build.gradle            # What, not why
```

```
update stuff                         # No type, no details
```

---

## 🚨 Never Do

- ❌ `git add -A` or `git add .` (stage specific files)
- ❌ `git commit --no-verify` (don't skip hooks)
- ❌ Stage sensitive files (.env, *.key, credentials)
- ❌ Commit without user asking
- ❌ Vague messages ("update stuff", "fix bug")

---

## 🔍 Before Committing

```bash
# See what changed
git status
git diff

# See what will be committed
git diff --staged

# Check recent commit style
git log --oneline -5
```

---

## 📋 Checklist

- [ ] `git status` reviewed
- [ ] Only related files staged
- [ ] No secrets (.env, keys)
- [ ] No build artifacts (*.class, build/)
- [ ] Message has type: summary format
- [ ] Message explains WHY
- [ ] Includes Co-Authored-By line
- [ ] Verified with `git log -1 --stat`

---

## 🛠️ Common Commands

### Stage Specific Files
```bash
git add desktop/build.gradle
git add desktop/dist/launcher-windows.bat
git add .claude/skills/build-distribution/SKILL.md
```

### Stage Directory
```bash
git add desktop/dist/
```

### Unstage File
```bash
git reset HEAD <file>
```

### Undo Last Commit (keep changes)
```bash
git reset --soft HEAD~1
```

### Amend Last Commit
```bash
git add forgotten-file.txt
git commit --amend --no-edit
```

---

## 📦 Real Examples

### Example 1: Multi-Platform Feature
```bash
git add desktop/build.gradle
git add desktop/dist/launcher-windows.bat
git add desktop/dist/launcher-unix.sh
git add desktop/src/main/java/com/beekeeper/desktop/Main.java

git commit -m "$(cat <<'EOF'
feat: add multi-platform JavaFX distribution support

- Add explicit platform dependencies (Windows, Linux, macOS Intel/ARM)
- Implement platform-aware launcher scripts with automatic detection
- Fix Windows BAT wildcard quotes issue preventing module loading
- Add portable database support (creates in distribution/data/)
- Remove -Dprism.order=sw flag causing Windows crashes

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
EOF
)"
```

### Example 2: Bug Fix
```bash
git add desktop/dist/launcher-windows.bat

git commit -m "$(cat <<'EOF'
fix: resolve Windows JavaFX module discovery failure

Windows BAT launcher used quotes around wildcard pattern which
cmd.exe treats as literal string. Changed to explicit module
enumeration without quotes.

Fixes: "Module javafx.controls not found" error on Windows

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
EOF
)"
```

### Example 3: Documentation
```bash
git add .claude/skills/build-distribution/SKILL.md
git add .claude/skills/build-distribution/CHANGELOG.md
git add WINDOWS_FIX.md

git commit -m "$(cat <<'EOF'
docs: update build-distribution skill with multi-platform setup

Add critical Windows/macOS setup instructions, platform-aware
launcher documentation, and troubleshooting for common errors.

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
EOF
)"
```

### Example 4: Cleanup
```bash
git add desktop/src/main/resources/sql/

git commit -m "$(cat <<'EOF'
chore: remove obsolete SQL export files

Removed old export files (00_init_data.sql, database_complete_export.sql,
database_schema_and_translations.sql) superseded by database_inserts_only.sql.

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
EOF
)"
```

---

## 🎯 Message Templates

### New Feature
```
feat: <what>

<why and key details>

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

### Bug Fix
```
fix: <what was fixed>

<what was broken and how fixed>

Fixes: <error description>

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

### Documentation
```
docs: <what documentation>

<why and what changed>

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

### Refactoring
```
refactor: <what was refactored>

<why and what improved>

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

---

**Last Updated:** 2026-02-22
**Version:** 1.0
