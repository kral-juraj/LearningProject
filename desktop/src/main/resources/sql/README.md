# SQL Scripts for Database Initialization

This directory contains SQL scripts for initializing the Beekeeper Desktop database from scratch.

## 📁 File Structure

### Automation Scripts

**init_database.sh** (5.3 KB) ⭐ **INITIALIZATION SCRIPT**
- Automated database initialization from scratch
- Creates schema + loads all translations
- Includes verification and colored output
- **Usage:** `./init_database.sh ~/beekeeper-desktop-new.db`

**export_translations.sh** (3.5 KB) ⭐ **EXPORT SCRIPT**
- Exports current translations from database to SQL file
- Updates 07_translations_all.sql with latest data
- **IMPORTANT:** Run this after adding/modifying any translations
- **Usage:** `./export_translations.sh` (uses default database)
- Must be run before committing UI changes!

### Schema & Structure

**01_schema.sql** (3.6 KB)
- Complete database schema exported from production database
- Creates all 10 tables: apiaries, hives, inspections, feedings, taxations, taxation_frames, calendar_events, settings, inspection_recordings, translations
- Includes indexes and constraints
- **Usage:** Run first to create empty database structure

### Translation Data (i18n)

**02_translations_queen_standard.sql** (4.2 KB)
- Queen Rearing Calculator - Standard method (7 milestones)
- Keys: `queen.std.*`
- 14 translations (7 SK + 7 EN)

**03_translations_queen_caging.sql** (8.4 KB)
- Queen Rearing Calculator - Caging method (16 milestones)
- Keys: `queen.caging.*`
- 32 translations (16 SK + 16 EN)

**04_translations_queen_standard_fix.sql** (1.9 KB)
- Fixes for standard method translation keys
- Added missing keys that code references

**05_translations_queen_split_fix.sql** (6.5 KB)
- Queen Rearing Calculator - Split method translations
- Keys: `queen.split.*`
- Fixes for split method translation keys

**06_translations_varroa_tooltips.sql** (6.6 KB)
- Varroa Calculator Settings Dialog - help icon tooltips
- Keys: `varroa.tooltip.*`
- 12 tooltips across 4 sections: Reproduction, Life cycle, Behavior, Mortality
- 24 translations (12 SK + 12 EN)

**07_translations_all.sql** (142 KB) ⭐ **COMPLETE EXPORT**
- **ALL 531 translation keys (1,062 SK+EN translations)**
- Includes everything: menu, buttons, labels, dialogs, calculators, etc.
- **This is the master file with complete translations**
- Categories: app, button, label, table, dialog, calculator, varroa, queen, treatment, feed_type, event_type, validation, error, success, status

## 🚀 Quick Start: Initialize Fresh Database

### Option 1: Complete Initialization (Recommended)

```bash
# Create empty database with schema
sqlite3 ~/beekeeper-desktop-new.db < desktop/src/main/resources/sql/01_schema.sql

# Load ALL translations (531 keys)
sqlite3 ~/beekeeper-desktop-new.db < desktop/src/main/resources/sql/07_translations_all.sql

# Verify
sqlite3 ~/beekeeper-desktop-new.db "SELECT COUNT(*) FROM translations;"
# Expected: 1062 (531 SK + 531 EN)
```

### Option 2: Incremental Build (for development)

If you want to understand how translations were built incrementally:

```bash
# 1. Create schema
sqlite3 ~/beekeeper-desktop-new.db < desktop/src/main/resources/sql/01_schema.sql

# 2. Add translations incrementally (optional, for learning)
sqlite3 ~/beekeeper-desktop-new.db < desktop/src/main/resources/sql/02_translations_queen_standard.sql
sqlite3 ~/beekeeper-desktop-new.db < desktop/src/main/resources/sql/03_translations_queen_caging.sql
sqlite3 ~/beekeeper-desktop-new.db < desktop/src/main/resources/sql/04_translations_queen_standard_fix.sql
sqlite3 ~/beekeeper-desktop-new.db < desktop/src/main/resources/sql/05_translations_queen_split_fix.sql
sqlite3 ~/beekeeper-desktop-new.db < desktop/src/main/resources/sql/06_translations_varroa_tooltips.sql

# 3. Add remaining translations
sqlite3 ~/beekeeper-desktop-new.db < desktop/src/main/resources/sql/07_translations_all.sql
```

## 🔍 Verify Database Initialization

```bash
# Check tables exist
sqlite3 ~/beekeeper-desktop-new.db ".tables"
# Expected: apiaries, calendar_events, feedings, hives, inspection_recordings, inspections, settings, taxation_frames, taxations, translations

# Check translation count
sqlite3 ~/beekeeper-desktop-new.db "SELECT COUNT(DISTINCT key) as unique_keys, COUNT(*) as total_translations FROM translations;"
# Expected: 531|1062

# Check Slovak translations
sqlite3 ~/beekeeper-desktop-new.db "SELECT COUNT(*) FROM translations WHERE language='sk';"
# Expected: 531

# Check English translations
sqlite3 ~/beekeeper-desktop-new.db "SELECT COUNT(*) FROM translations WHERE language='en';"
# Expected: 531

# Sample query - check button translations
sqlite3 ~/beekeeper-desktop-new.db "SELECT key, language, value FROM translations WHERE category='button' AND key LIKE 'button.add%' ORDER BY key, language;"
```

## 📊 Translation Categories

| Category | Description | Example Keys |
|----------|-------------|--------------|
| `app` | Application title, version | app.title, app.version |
| `menu` | Menu bar items | menu.file, menu.edit, menu.help |
| `button` | Button labels | button.add, button.save, button.delete |
| `label` | Form field labels | label.name, label.location, label.date |
| `table` | Table column headers | table.column.name, table.column.status |
| `dialog` | Dialog titles/content | dialog.title.add_apiary, dialog.header.confirm |
| `tooltip` | Tooltip text | tooltip.feed_amount, tooltip.worker_cycle |
| `error` | Error messages | error.loading, error.validation.required |
| `success` | Success messages | success.created, success.updated |
| `status` | Status bar messages | status.loading, status.saved |
| `validation` | Validation errors | validation.required, validation.invalid_number |
| `calculator` | Calculator UI | calculator.varroa.title, calculator.feed.result |
| `varroa` | Varroa calculator | varroa.label.mortality, varroa.tooltip.drone_offspring |
| `queen` | Queen rearing | queen.std.start_cells, queen.split.broodless_period |
| `treatment` | Treatment types | treatment.thymol, treatment.oxalic_acid |
| `feed_type` | Feed types | feed_type.sugar_syrup, feed_type.fondant |
| `event_type` | Calendar events | event_type.treatment, event_type.inspection |

## 🔄 Updating Translations (CRITICAL WORKFLOW)

### ⚠️ MANDATORY: When you add/modify translations, ALWAYS update SQL init script!

**Complete Workflow:**

1. **Add new translations to database:**
   ```sql
   -- /tmp/my_new_translations.sql
   INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES
   (lower(hex(randomblob(16))), 'button.my_feature', 'sk', 'Moja funkcia', 'button', datetime('now')),
   (lower(hex(randomblob(16))), 'button.my_feature', 'en', 'My Feature', 'button', datetime('now'));
   ```

2. **Apply to your development database:**
   ```bash
   sqlite3 ~/beekeeper-desktop.db < /tmp/my_new_translations.sql
   ```

3. **Export updated translations (CRITICAL!):**
   ```bash
   cd desktop/src/main/resources/sql/
   ./export_translations.sh
   ```

   This updates `07_translations_all.sql` with your new translations.

4. **Verify export:**
   ```bash
   # Check that 07_translations_all.sql was updated
   git diff desktop/src/main/resources/sql/07_translations_all.sql

   # Should show your new translation keys added
   ```

5. **Test fresh database initialization:**
   ```bash
   # Create test database from SQL scripts
   ./init_database.sh ~/test-db.db

   # Verify your new keys are present
   sqlite3 ~/test-db.db "SELECT * FROM translations WHERE key='button.my_feature';"
   ```

6. **Commit both code and SQL file:**
   ```bash
   git add desktop/src/main/resources/sql/07_translations_all.sql
   git add [your code files with new UI]
   git commit -m "Add my_feature with translations"
   ```

### ❌ Common Mistakes to Avoid

1. **DON'T forget to run export_translations.sh** - other developers won't have your translations!
2. **DON'T commit UI changes without updating SQL file** - CI/CD will fail!
3. **DON'T manually edit 07_translations_all.sql** - use export script!

### ✅ Best Practice: Pre-Commit Hook

Add this to `.git/hooks/pre-commit` to automatically check:

```bash
#!/bin/bash
# Check if UI files changed but SQL not updated

UI_CHANGED=$(git diff --cached --name-only | grep -E '\.(fxml|java)$' || true)
SQL_CHANGED=$(git diff --cached --name-only | grep '07_translations_all.sql' || true)

if [ -n "$UI_CHANGED" ] && [ -z "$SQL_CHANGED" ]; then
    echo "⚠️  WARNING: UI files changed but 07_translations_all.sql not updated"
    echo "Did you forget to run: ./export_translations.sh ?"
    echo ""
    echo "Continue anyway? (yes/no)"
    read -r answer
    if [ "$answer" != "yes" ]; then
        exit 1
    fi
fi
```

## 📝 Export Current Database

To export current database state:

```bash
# Export schema
sqlite3 ~/beekeeper-desktop.db ".schema" > desktop/src/main/resources/sql/01_schema.sql

# Export all translations
sqlite3 ~/beekeeper-desktop.db "SELECT ... FROM translations" > desktop/src/main/resources/sql/07_translations_all.sql
```

## ⚠️ Important Notes

1. **Order matters:** Always run `01_schema.sql` first to create tables
2. **Idempotent:** Scripts use `INSERT OR REPLACE` so they can be run multiple times safely
3. **Master file:** `07_translations_all.sql` contains ALL translations - use this for fresh installations
4. **Incremental files (02-06):** Historical files showing how translations were built, useful for understanding but not required
5. **Random IDs:** Translation IDs are generated with `lower(hex(randomblob(16)))` - they'll be different each time but that's OK (key+language is the unique constraint)

## 🔒 Security: What's Safe in Git?

### ✅ SAFE to commit (what we have):

- **Schema files (01_schema.sql)** - Only `CREATE TABLE` statements, no data
- **Translation files (02-07)** - Only UI text translations (public strings)
- **Init/export scripts** - Automation scripts without credentials
- **README.md** - Documentation

These files contain:
- Database structure (table definitions)
- UI translations ("Add Apiary", "Pridať včelnicu")
- No personal data, no credentials, no API keys

### ❌ NEVER commit to git:

- **User data dumps:** Real apiary names, locations, GPS coordinates
- **Personal information:** Email addresses, phone numbers, notes
- **Credentials:** Passwords, API keys, tokens, connection strings
- **Production database backups:** Any file with real user data
- **Settings with secrets:** API keys, authentication tokens

### 🚨 Example of what NOT to commit:

```sql
-- ❌ DANGER - Contains user data
INSERT INTO apiaries VALUES ('Jan Novak''s Apiary', 48.123456, 17.654321);
INSERT INTO hives VALUES (..., 'Queen from Peter, tel: +421...', ...);
INSERT INTO settings VALUES ('google_maps_api_key', 'AIza...');

-- ✅ SAFE - Only structure
CREATE TABLE apiaries (id TEXT, name TEXT, ...);

-- ✅ SAFE - Only UI translations
INSERT INTO translations VALUES ('button.add', 'sk', 'Pridať', ...);
```

### 📝 .gitignore Configuration

Make sure your `.gitignore` includes:

```gitignore
# User databases (contain real data)
*.db
*.sqlite
*.sqlite3

# Database backups
*.db.backup
*.sql.backup

# Production data dumps
*_dump.sql
*_backup.sql
production_*.sql

# Configuration with secrets
.env
config.properties
secrets.yml
```

**Our SQL scripts are safe because they only contain:**
1. Empty table structures (schema)
2. Public UI translations visible in the app
3. No user data, no secrets, no credentials

## 🛠️ Troubleshooting

**Problem:** Translation count is wrong after import
```bash
# Delete all translations and reimport
sqlite3 ~/beekeeper-desktop.db "DELETE FROM translations;"
sqlite3 ~/beekeeper-desktop.db < desktop/src/main/resources/sql/07_translations_all.sql
```

**Problem:** Application shows `[button.my_key]` placeholders
- Translation key is missing from database
- Check spelling of key in code vs database
- Run query: `SELECT * FROM translations WHERE key='button.my_key';`

**Problem:** Database locked
```bash
# Close all connections and retry
sqlite3 ~/beekeeper-desktop.db ".exit"
```

## 📚 Related Documentation

- **CLAUDE.md** - Development guide with i18n rules (MANDATORY)
- **I18N_IMPLEMENTATION_PLAN.md** - Original implementation plan
- **I18N_IMPLEMENTATION_STATUS.md** - Complete status report
- **TranslationManager.java** - Java code for loading translations
- **TranslationMigration.java** - Java-based migration (alternative to SQL scripts)

---

**Last Updated:** 2025-02-16
**Total Translation Keys:** 531
**Total Translations:** 1,062 (SK + EN)
**Schema Version:** 1.0 (10 tables)
