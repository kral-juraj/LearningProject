#!/bin/bash
# ========================================
# Beekeeper Desktop Application Launcher
# Linux/Unix Shell Script
# ========================================

echo "===================================="
echo "Beekeeper Desktop - Starting..."
echo "===================================="
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java not found!"
    echo "Please install Java 17 or higher from: https://adoptium.net/"
    echo ""
    exit 1
fi

# Show Java version
echo "Java version:"
java -version
echo ""

# Set database path to user home directory
DB_PATH="$HOME/beekeeper-desktop.db"
echo "Database location: $DB_PATH"
echo ""

# Check if database exists (first run)
if [ ! -f "$DB_PATH" ]; then
    echo "First run detected - database will be initialized with test data"
    echo "Test apiary 'Testovacia vcelnica' will be created"
    echo ""
fi

# Get the directory where this script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd "$SCRIPT_DIR"

# Build classpath from all JARs in lib directory
CLASSPATH="lib/*"

# Run the application
echo "Starting Beekeeper Desktop..."
echo ""

java \
  -Dprism.order=sw \
  -Djavafx.animation.fullspeed=false \
  -cp "$CLASSPATH" \
  com.beekeeper.desktop.Main

# Check exit code
if [ $? -ne 0 ]; then
    echo ""
    echo "===================================="
    echo "Application exited with error!"
    echo "===================================="
    exit 1
fi

echo ""
echo "===================================="
echo "Application closed successfully"
echo "===================================="
