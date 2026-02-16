#!/bin/bash
#
# Export all translations from database to SQL init script
# Updates 07_translations_all.sql with current database state
#
# Usage:
#   ./export_translations.sh [database_path]
#
# Example:
#   ./export_translations.sh                         # Use default ~/beekeeper-desktop.db
#   ./export_translations.sh ~/custom-db.db          # Use custom database
#

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Get database path (default: ~/beekeeper-desktop.db)
DB_PATH="${1:-$HOME/beekeeper-desktop.db}"

# Get directory where this script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
OUTPUT_FILE="$SCRIPT_DIR/07_translations_all.sql"

echo -e "${BLUE}╔════════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║           Beekeeper Translations Exporter                 ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════════════════════════╝${NC}"
echo ""

# Check if sqlite3 is available
if ! command -v sqlite3 &> /dev/null; then
    echo -e "${RED}ERROR: sqlite3 is not installed${NC}"
    exit 1
fi

# Check if database exists
if [ ! -f "$DB_PATH" ]; then
    echo -e "${RED}ERROR: Database not found at: $DB_PATH${NC}"
    exit 1
fi

echo -e "${BLUE}Database:${NC} $DB_PATH"
echo -e "${BLUE}Output:${NC} $OUTPUT_FILE"
echo ""

# Get current translation count
BEFORE_COUNT=$(sqlite3 "$DB_PATH" "SELECT COUNT(*) FROM translations;")
BEFORE_KEYS=$(sqlite3 "$DB_PATH" "SELECT COUNT(DISTINCT key) FROM translations;")

echo -e "${BLUE}Exporting translations...${NC}"
echo -e "  ${GREEN}→ Total translations: $BEFORE_COUNT${NC}"
echo -e "  ${GREEN}→ Unique keys: $BEFORE_KEYS${NC}"
echo ""

# Export translations to SQL file
sqlite3 "$DB_PATH" << 'EOF' > "$OUTPUT_FILE"
.mode list
.separator ''
SELECT '-- Complete translations export (' || COUNT(*) || ' translations, ' || COUNT(DISTINCT key) || ' unique keys)' || char(10) ||
'-- Generated: ' || datetime('now') || char(10) ||
'-- Categories: app, button, label, table, dialog, calculator, varroa, queen, treatment, feed_type, event_type, validation, error, success, status' || char(10) || char(10) ||
'INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES' || char(10)
FROM translations;

SELECT
  CASE
    WHEN rownum > 1 THEN ',' || char(10)
    ELSE ''
  END ||
  '(lower(hex(randomblob(16))), ''' || key || ''', ''' || language || ''', ''' || replace(value, '''', '''''') || ''', ''' || COALESCE(category, '') || ''', datetime(''now''))'
FROM (
  SELECT
    key, language, value, category,
    ROW_NUMBER() OVER (ORDER BY category, key, language) as rownum
  FROM translations
  ORDER BY category, key, language
);

SELECT ';' || char(10);
EOF

# Check if export succeeded
if [ ! -f "$OUTPUT_FILE" ]; then
    echo -e "${RED}ERROR: Export failed - output file not created${NC}"
    exit 1
fi

# Verify file content
FILE_SIZE=$(stat -f%z "$OUTPUT_FILE" 2>/dev/null || stat -c%s "$OUTPUT_FILE" 2>/dev/null)
LINE_COUNT=$(wc -l < "$OUTPUT_FILE" | tr -d ' ')

if [ "$FILE_SIZE" -lt 1000 ]; then
    echo -e "${RED}ERROR: Export file is too small ($FILE_SIZE bytes)${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Export completed successfully${NC}"
echo -e "  ${GREEN}→ File size: $(numfmt --to=iec-i --suffix=B $FILE_SIZE 2>/dev/null || echo "$FILE_SIZE bytes")${NC}"
echo -e "  ${GREEN}→ Lines: $LINE_COUNT${NC}"
echo ""

# Verify by counting INSERT statements
INSERT_COUNT=$(grep -c "^(lower" "$OUTPUT_FILE" || echo "0")
echo -e "${BLUE}Verification:${NC}"
echo -e "  ${GREEN}→ INSERT statements in SQL: $INSERT_COUNT${NC}"
echo -e "  ${GREEN}→ Translations in database: $BEFORE_COUNT${NC}"

if [ "$INSERT_COUNT" -eq "$BEFORE_COUNT" ]; then
    echo -e "  ${GREEN}✓ Counts match - export is complete${NC}"
else
    echo -e "  ${YELLOW}⚠ Count mismatch - please review export${NC}"
    echo -e "  ${YELLOW}  This might be OK if translations were added during export${NC}"
fi

echo ""
echo -e "${GREEN}╔════════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║                Export completed successfully!              ║${NC}"
echo -e "${GREEN}╚════════════════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "${YELLOW}Next steps:${NC}"
echo "1. Review changes: git diff $OUTPUT_FILE"
echo "2. Test initialization: ./init_database.sh ~/test-db.db"
echo "3. Commit changes: git add $OUTPUT_FILE"
echo ""
echo -e "${BLUE}File updated:${NC} $OUTPUT_FILE"
echo -e "${GREEN}Ready to commit! 🎉${NC}"
