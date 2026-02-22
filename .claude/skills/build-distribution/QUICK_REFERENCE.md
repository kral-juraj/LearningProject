# Build Distribution - Quick Reference

## ⚡ Quick Build

```bash
# 1. Verify setup
grep "javafx-base.*:win" desktop/build.gradle
grep 'for %%f in (%APP_HOME%' desktop/dist/launcher-windows.bat

# 2. Build
gradle desktop:clean desktop:distZip

# 3. Verify (should be ~61 MB)
ls -lh desktop/build/distributions/beekeeper-desktop.zip

# 4. Check multi-platform
unzip -l desktop/build/distributions/beekeeper-desktop.zip | grep javafx | wc -l
# Should show: 16 (4 modules × 4 platforms)

# 5. Version and save
NEW_VERSION="v1.6"  # Increment from last
cp desktop/build/distributions/beekeeper-desktop.zip \
   desktop/distributions/beekeeper-desktop-${NEW_VERSION}-multiplatform.zip
```

---

## 🚨 Critical Checks BEFORE Building

### 1. build.gradle Multi-Platform Dependencies

```gradle
// MUST HAVE all 4 platforms for each module:
implementation "org.openjfx:javafx-base:21.0.2:win"        // ✅
implementation "org.openjfx:javafx-base:21.0.2:linux"      // ✅
implementation "org.openjfx:javafx-base:21.0.2:mac"        // ✅
implementation "org.openjfx:javafx-base:21.0.2:mac-aarch64" // ✅
```

### 2. Windows Launcher - NO Quotes

```batch
# ❌ WRONG (quotes around wildcard):
for %%f in ("%APP_HOME%\lib\javafx-*-win.jar") do ...

# ✅ CORRECT (no quotes, explicit modules):
for %%f in (%APP_HOME%\lib\javafx-base-*-win.jar) do set JAVAFX_MODULES=%%f
for %%f in (%APP_HOME%\lib\javafx-controls-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
for %%f in (%APP_HOME%\lib\javafx-fxml-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
for %%f in (%APP_HOME%\lib\javafx-graphics-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
```

### 3. NO Software Rendering Flag

```gradle
applicationDefaultJvmArgs = [
    // ❌ NEVER include: '-Dprism.order=sw'  (crashes Windows)
    '-Djavafx.animation.fullspeed=false',   // ✅
    // ...
]
```

---

## 🔍 Verification Steps

### After Build

```bash
cd /tmp
unzip -q desktop/build/distributions/beekeeper-desktop.zip

# 1. Check JavaFX libraries (should show all platforms)
ls beekeeper-desktop/lib/ | grep javafx | sort

# Expected output (16 files):
# javafx-base-21.0.2-linux.jar
# javafx-base-21.0.2-mac-aarch64.jar
# javafx-base-21.0.2-mac.jar
# javafx-base-21.0.2-win.jar
# javafx-controls-21.0.2-linux.jar
# ... (12 more)

# 2. Check Windows launcher
cat beekeeper-desktop/bin/beekeeper.bat | grep "javafx-base-\*-win.jar"
# Should find: for %%f in (%APP_HOME%\lib\javafx-base-*-win.jar)

# 3. Check Unix launcher
cat beekeeper-desktop/bin/beekeeper | grep "uname -s"
# Should find platform detection

# 4. Test launch (macOS/Linux)
cd beekeeper-desktop/bin
chmod +x beekeeper
./beekeeper > /tmp/test.log 2>&1 &
sleep 5
pkill -f "beekeeper.desktop.Main"
grep "Database initialized" /tmp/test.log
```

---

## ❌ Common Errors & Quick Fixes

### Error: "no suitable pipeline found" (Windows)

**Cause:** Missing Windows JavaFX libraries

**Fix:**
```bash
# Add to build.gradle dependencies:
implementation "org.openjfx:javafx-graphics:21.0.2:win"
# (and base, controls, fxml)

gradle desktop:clean desktop:distZip
```

### Error: "Module javafx.controls not found" (Windows)

**Cause:** Quotes around wildcard in BAT file

**Fix in desktop/dist/launcher-windows.bat:**
```batch
# Change from:
for %%f in ("%APP_HOME%\lib\javafx-*-win.jar") do ...

# To:
for %%f in (%APP_HOME%\lib\javafx-base-*-win.jar) do set JAVAFX_MODULES=%%f
```

### Error: "Two versions of module javafx.graphics found"

**Cause:** Launcher using all JAR files instead of platform-specific

**Fix:** Ensure launcher selects only platform JARs:
- Windows: only `*-win.jar`
- macOS ARM: only `*-mac-aarch64.jar`
- macOS Intel: only `*-mac.jar`
- Linux: only `*-linux.jar`

### Distribution is only 39 MB (should be 61 MB)

**Cause:** Missing multi-platform dependencies

**Fix:** Add all platform variants to build.gradle dependencies

---

## 📋 Quick Checklist

Before release:
- [ ] build.gradle has win, linux, mac, mac-aarch64 JavaFX deps
- [ ] Windows BAT has explicit module enumeration (no quotes)
- [ ] Unix SH has platform detection (uname -s, uname -m)
- [ ] NO -Dprism.order=sw in applicationDefaultJvmArgs
- [ ] Distribution is ~61 MB
- [ ] Version number incremented
- [ ] Tested on macOS (or will test on Windows/Linux)

---

## 📦 Distribution Contents

**Expected structure:**
```
beekeeper-desktop/
├── bin/
│   ├── beekeeper           (platform-aware Unix launcher)
│   └── beekeeper.bat       (explicit Windows launcher)
├── lib/                    (~61 MB)
│   ├── javafx-base-21.0.2-win.jar
│   ├── javafx-base-21.0.2-linux.jar
│   ├── javafx-base-21.0.2-mac.jar
│   ├── javafx-base-21.0.2-mac-aarch64.jar
│   └── ... (12 more JavaFX JARs + app JARs)
├── sql/
│   └── database_inserts_only.sql  (1,599 INSERT statements)
├── data/                   (created on first run)
│   └── beekeeper.db
└── README.txt
```

**JavaFX modules in each platform:**
- javafx-base
- javafx-controls
- javafx-fxml
- javafx-graphics

**Total:** 4 modules × 4 platforms = 16 JavaFX JAR files

---

## 🎯 Success Criteria

After building and testing:
- ✅ Distribution is ~61 MB
- ✅ Contains 16 JavaFX JAR files (4 platforms)
- ✅ Launcher scripts are platform-aware
- ✅ Application launches on macOS without errors
- ✅ Database initializes with 785 keys, 1 apiary, 2 hives
- ✅ Version number documented in README.md

---

**Last Updated:** 2026-02-22
**Current Version:** v1.5
**Next Version:** v1.6
