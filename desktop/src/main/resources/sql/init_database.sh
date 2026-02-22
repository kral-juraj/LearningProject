#!/bin/bash
set -e

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
DB_PATH="${HOME}/beekeeper-desktop.db"
EXPORT_FILE="${SCRIPT_DIR}/database_complete_export.sql"

echo -e "${YELLOW}Beekeeper Desktop - Database Initialization${NC}"
echo "=============================================="

if [ ! -f "$EXPORT_FILE" ]; then
    echo -e "${RED}ERROR: Export file not found: $EXPORT_FILE${NC}"
    exit 1
fi

if [ -f "$DB_PATH" ]; then
    echo -e "${YELLOW}WARNING: Database already exists at: $DB_PATH${NC}"
    read -p "Do you want to overwrite it? (y/N) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        echo "Aborted."
        exit 0
    fi
    echo -e "${YELLOW}Backing up existing database...${NC}"
    cp "$DB_PATH" "${DB_PATH}.backup.$(date +%Y%m%d_%H%M%S)"
    echo -e "${GREEN}✓ Backup created${NC}"
    rm -f "$DB_PATH"
fi

echo -e "${YELLOW}Creating database from export...${NC}"
sqlite3 "$DB_PATH" < "$EXPORT_FILE"

echo -e "${YELLOW}Verifying database...${NC}"

TABLES=$(sqlite3 "$DB_PATH" "SELECT COUNT(*) FROM sqlite_master WHERE type='table';")
TRANSLATIONS=$(sqlite3 "$DB_PATH" "SELECT COUNT(DISTINCT key) FROM translations;")
TRANSLATION_RECORDS=$(sqlite3 "$DB_PATH" "SELECT COUNT(*) FROM translations;")
APIARIES=$(sqlite3 "$DB_PATH" "SELECT COUNT(*) FROM apiaries;")
HIVES=$(sqlite3 "$DB_PATH" "SELECT COUNT(*) FROM hives;")

echo ""
echo "Database Statistics:"
echo "  Tables: $TABLES"
echo "  Translation keys: $TRANSLATIONS"
echo "  Translation records: $TRANSLATION_RECORDS"
echo "  Apiaries: $APIARIES"
echo "  Hives: $HIVES"
echo ""

if [ "$TABLES" -eq 10 ] && [ "$TRANSLATIONS" -eq 785 ] && [ "$TRANSLATION_RECORDS" -eq 1570 ]; then
    echo -e "${GREEN}✓ Database initialized successfully!${NC}"
    echo ""
    echo "Database location: $DB_PATH"
    echo "You can now run: gradle desktop:run"
else
    echo -e "${RED}⚠ WARNING: Database values don't match expected${NC}"
fi
