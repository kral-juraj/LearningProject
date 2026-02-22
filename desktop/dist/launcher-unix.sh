#!/bin/bash
# Beekeeper Desktop Launcher Script

# Get application home directory
APP_HOME="$(cd "$(dirname "$0")/.." && pwd)"

# Detect platform and build platform-specific module path
OS_NAME="$(uname -s)"
OS_ARCH="$(uname -m)"

if [[ "$OS_NAME" == "Darwin" ]]; then
    # macOS - use mac or mac-aarch64 depending on architecture
    if [[ "$OS_ARCH" == "arm64" ]]; then
        PLATFORM="mac-aarch64"
    else
        PLATFORM="mac"
    fi
elif [[ "$OS_NAME" == "Linux" ]]; then
    PLATFORM="linux"
else
    echo "Unsupported platform: $OS_NAME"
    exit 1
fi

# Build platform-specific module path (only platform-specific jar files)
JAVAFX_MODULES=""
for jar in "$APP_HOME"/lib/javafx-*-${PLATFORM}.jar; do
    if [ -f "$jar" ]; then
        if [ -z "$JAVAFX_MODULES" ]; then
            JAVAFX_MODULES="$jar"
        else
            JAVAFX_MODULES="$JAVAFX_MODULES:$jar"
        fi
    fi
done

# Execute application with platform-specific JavaFX module-path
# Note: Uses only javafx-*-${PLATFORM}.jar to avoid module conflicts
exec java \
  -Djavafx.animation.fullspeed=false \
  -Dapple.awt.application.appearance=system \
  --enable-native-access=ALL-UNNAMED \
  --module-path "$JAVAFX_MODULES" \
  --add-modules javafx.controls,javafx.fxml \
  -cp "$APP_HOME/lib/*" \
  com.beekeeper.desktop.Main
