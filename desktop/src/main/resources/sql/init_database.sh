#!/bin/bash
#
# Initialize Beekeeper Desktop database from scratch
# Creates schema and loads all 531 translation keys (1,062 SK+EN translations)
#
# Usage:
#   ./init_database.sh [database_path]
#
# Example:
#   ./init_database.sh ~/beekeeper-desktop.db        # Initialize at default location
#   ./init_database.sh ~/test-db.db                  # Initialize at custom location
#   ./init_database.sh :memory:                      # Initialize in-memory (for testing)
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

echo -e "${BLUE}╔════════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║         Beekeeper Desktop Database Initializer           ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════════════════════════╝${NC}"
echo ""

# Check if sqlite3 is available
if ! command -v sqlite3 &> /dev/null; then
    echo -e "${RED}ERROR: sqlite3 is not installed${NC}"
    echo "Install it with: brew install sqlite3 (macOS) or apt-get install sqlite3 (Linux)"
    exit 1
fi

# Check if database already exists
if [ -f "$DB_PATH" ] && [ "$DB_PATH" != ":memory:" ]; then
    echo -e "${YELLOW}WARNING: Database already exists at: $DB_PATH${NC}"
    echo -n "Do you want to DELETE and recreate it? (yes/no): "
    read -r confirmation
    if [ "$confirmation" != "yes" ]; then
        echo -e "${RED}Aborted.${NC}"
        exit 0
    fi
    rm "$DB_PATH"
    echo -e "${GREEN}✓ Old database deleted${NC}"
fi

echo ""
echo -e "${BLUE}Database path:${NC} $DB_PATH"
echo ""

# Step 1: Create schema
echo -e "${BLUE}[1/2] Creating database schema (10 tables)...${NC}"
if [ -f "$SCRIPT_DIR/01_schema.sql" ]; then
    sqlite3 "$DB_PATH" < "$SCRIPT_DIR/01_schema.sql"
    echo -e "${GREEN}✓ Schema created successfully${NC}"

    # Verify tables
    TABLE_COUNT=$(sqlite3 "$DB_PATH" "SELECT COUNT(*) FROM sqlite_master WHERE type='table';")
    echo -e "  ${GREEN}→ Tables created: $TABLE_COUNT${NC}"
else
    echo -e "${RED}ERROR: 01_schema.sql not found${NC}"
    exit 1
fi

# Step 2: Load translations
echo ""
echo -e "${BLUE}[2/2] Loading translations (531 keys, 1,062 SK+EN)...${NC}"
if [ -f "$SCRIPT_DIR/07_translations_all.sql" ]; then
    sqlite3 "$DB_PATH" < "$SCRIPT_DIR/07_translations_all.sql"
    echo -e "${GREEN}✓ Translations loaded successfully${NC}"

    # Verify translations
    TRANSLATION_COUNT=$(sqlite3 "$DB_PATH" "SELECT COUNT(*) FROM translations;")
    KEY_COUNT=$(sqlite3 "$DB_PATH" "SELECT COUNT(DISTINCT key) FROM translations;")
    SK_COUNT=$(sqlite3 "$DB_PATH" "SELECT COUNT(*) FROM translations WHERE language='sk';")
    EN_COUNT=$(sqlite3 "$DB_PATH" "SELECT COUNT(*) FROM translations WHERE language='en';")

    echo -e "  ${GREEN}→ Total translations: $TRANSLATION_COUNT${NC}"
    echo -e "  ${GREEN}→ Unique keys: $KEY_COUNT${NC}"
    echo -e "  ${GREEN}→ Slovak (SK): $SK_COUNT${NC}"
    echo -e "  ${GREEN}→ English (EN): $EN_COUNT${NC}"
else
    echo -e "${RED}ERROR: 07_translations_all.sql not found${NC}"
    exit 1
fi

# Final verification
echo ""
echo -e "${BLUE}[Verification] Running integrity checks...${NC}"

# Check foreign keys
sqlite3 "$DB_PATH" "PRAGMA foreign_keys = ON; PRAGMA foreign_key_check;" > /tmp/fk_check.txt 2>&1
if [ -s /tmp/fk_check.txt ]; then
    echo -e "${YELLOW}⚠ Foreign key issues detected${NC}"
    cat /tmp/fk_check.txt
else
    echo -e "${GREEN}✓ Foreign key constraints OK${NC}"
fi

# Check unique keys
DUPLICATE_KEYS=$(sqlite3 "$DB_PATH" "SELECT key, language, COUNT(*) as cnt FROM translations GROUP BY key, language HAVING cnt > 1;" | wc -l)
if [ "$DUPLICATE_KEYS" -gt 0 ]; then
    echo -e "${YELLOW}⚠ Found $DUPLICATE_KEYS duplicate translation keys${NC}"
else
    echo -e "${GREEN}✓ No duplicate translation keys${NC}"
fi

# Summary
echo ""
echo -e "${GREEN}╔════════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║                Database initialized successfully!         ║${NC}"
echo -e "${GREEN}╚════════════════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "${BLUE}Database location:${NC} $DB_PATH"
echo -e "${BLUE}Tables:${NC} $TABLE_COUNT"
echo -e "${BLUE}Translation keys:${NC} $KEY_COUNT"
echo -e "${BLUE}Total translations:${NC} $TRANSLATION_COUNT (SK: $SK_COUNT, EN: $EN_COUNT)"
echo ""
echo -e "${YELLOW}Next steps:${NC}"
echo "1. Run application: gradle desktop:run"
echo "2. Check translations: sqlite3 $DB_PATH 'SELECT * FROM translations LIMIT 5;'"
echo "3. Switch language: Menu → Language → English (EN)"
echo ""
echo -e "${GREEN}Happy beekeeping! 🐝${NC}"
