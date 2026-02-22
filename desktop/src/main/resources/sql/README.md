# SQL Database Scripts

## 📦 Database Exports (Recommended for Distribution)

### `database_complete_export.sql` (274 KB, 1577 lines)
**Complete database dump with test data**

Contains:
- ✅ Complete schema (10 tables)
- ✅ 785 translation keys (1548 SK+EN records)
- ✅ Test data (1 apiary, 2 hives)
- ✅ All constraints, indexes, triggers

**Use for:** Distribution, testing, development setup

**How to use:**
```bash
# Create new database from complete export
sqlite3 ~/beekeeper-desktop.db < database_complete_export.sql
```

### `database_schema_and_translations.sql` (274 KB)
**Schema and translations without test data**

Contains:
- ✅ Complete schema (10 tables)
- ✅ 785 translation keys (1548 SK+EN records)
- ❌ NO test data

**Use for:** Production deployment, clean installation

**How to use:**
```bash
# Create production database
sqlite3 ~/beekeeper-desktop.db < database_schema_and_translations.sql
```

---

## 📝 Individual SQL Scripts (Historical)

These scripts were used to build the database incrementally during development.

### Schema
- `01_schema.sql` - Base database schema (10 tables)

### Translations (Sequential)
All translation migration files from 02-20

### Test Data
- `18_test_data.sql` - Test apiary and hives
- `19_test_taxations_realistic.sql` - Realistic taxation test data

---

## 🚀 Quick Start

### Option 1: Use Complete Export (Recommended)
```bash
cd desktop/src/main/resources/sql
rm -f ~/beekeeper-desktop.db
sqlite3 ~/beekeeper-desktop.db < database_complete_export.sql
```

### Option 2: Use init_database.sh Script
```bash
cd desktop/src/main/resources/sql
./init_database.sh
```

---

## 📊 Database Statistics

**Tables:** 10
- apiaries, hives, inspections, feedings, taxations, taxation_frames, calendar_events, settings, inspection_recordings, translations

**Translations:**
- 785 unique keys
- 1548 total records (SK + EN)

**Test Data:**
- 1 apiary: "Test Apiary"
- 2 hives: "Test Hive 1", "Test Hive 2"

---

**Generated:** 2026-02-22
**Database Version:** 1.0
**Total Translation Keys:** 785 (SK/EN)
