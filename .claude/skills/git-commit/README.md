# Git Commit Skill

Skill pre vytvorenie Git commitu s dobre napísaným commit message a Co-Authored-By.

## 📋 Obsah

- **SKILL.md** - Kompletný guide s pravidlami, príkladmi, checklist
- **QUICK_REFERENCE.md** - Rýchle príkazy, templates, real examples
- **README.md** - Tento súbor

## 🚀 Použitie

### Základné použitie

```bash
# Vyvolaj skill v Claude Code
/git-commit
```

Alebo:

```bash
# 1. Analyzuj zmeny
git status
git diff --stat

# 2. Stage súbory
git add <file1> <file2>

# 3. Commit
git commit -m "$(cat <<'EOF'
<type>: <summary>

<details>

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
EOF
)"
```

## 📝 Message Format

```
<type>: <short summary (max 72 chars)>

<optional detailed explanation>
<why the change was needed>
<what problem it solves>

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

### Types

| Type | Použitie |
|------|----------|
| `feat` | Nová funkcia |
| `fix` | Oprava bugu |
| `refactor` | Refactoring kódu |
| `docs` | Dokumentácia |
| `test` | Testy |
| `chore` | Build, dependencies, configs |
| `perf` | Performance improvements |
| `style` | Code style (formatting) |

## ✅ Good Examples

```
feat: add multi-platform JavaFX support

- Windows, Linux, macOS dependencies
- Platform-aware launchers
- Fix Windows BAT wildcard quotes

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

```
fix: resolve Windows JavaFX crash on startup

Windows BAT used quotes around wildcard preventing module discovery.
Changed to explicit enumeration without quotes.

Fixes: "Module javafx.controls not found"

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

```
docs: update build-distribution skill with multi-platform setup

Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
```

## ❌ Bad Examples

```
fix: update files              # Too vague
```

```
feat: change build.gradle      # What, not why
```

```
update stuff                   # No type, no details
```

## 🚨 Safety Rules

- ❌ NEVER `git add -A` or `git add .` (stage specific files)
- ❌ NEVER `git commit --no-verify` (don't skip hooks)
- ❌ NEVER stage sensitive files (.env, keys, credentials)
- ❌ NEVER commit without user explicitly asking
- ✅ ALWAYS stage specific files by name
- ✅ ALWAYS include Co-Authored-By line
- ✅ ALWAYS explain WHY, not just WHAT

## 📚 Full Documentation

See **SKILL.md** for:
- Complete workflow
- Staging strategies
- Message templates
- Best practices
- Recovery commands
- Common mistakes

See **QUICK_REFERENCE.md** for:
- Quick commands
- Real examples
- Templates
- Checklist

## 🎯 Checklist

Before committing:
- [ ] Reviewed `git status` and `git diff`
- [ ] Staged only related files
- [ ] No secrets/build artifacts staged
- [ ] Message has type: summary format
- [ ] Message explains WHY
- [ ] Includes Co-Authored-By line
- [ ] Verified with `git log -1 --stat`

---

**Created:** 2026-02-22
**Version:** 1.0
**Status:** Production Ready
