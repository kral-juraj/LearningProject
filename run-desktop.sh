#!/bin/bash

# Build and run Beekeeper Desktop Application

echo "Building Beekeeper Desktop..."
gradle desktop:build

if [ $? -eq 0 ]; then
    echo ""
    echo "Build successful! Starting application..."
    echo "Database will be created at: $HOME/beekeeper-desktop.db"
    echo ""
    gradle desktop:run
else
    echo ""
    echo "Build failed. Please check the errors above."
    exit 1
fi
