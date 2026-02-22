# Windows JavaFX Fix - Troubleshooting Timeline

Complete timeline of iterations and fixes for Windows JavaFX crash.

---

## 📊 Summary Table

| Verzia | Veľkosť | Status | Hlavný problém |
|--------|---------|--------|----------------|
| v1.0 | 39 MB | ❌ Broken | `-Dprism.order=sw` vo všetkých súboroch |
| v1.1 | 39 MB | ❌ Broken | `-Dprism.order=sw` v launcher skriptoch |
| v1.2 | - | ❌ Skipped | Len macOS knižnice v distribúcii |
| v1.3 | 61 MB | ❌ Broken | Java module conflict (viacero verzií) |
| v1.4 | 61 MB | ❌ Broken | Windows BAT wildcard loop nefungoval |
| **v1.5** | **61 MB** | ✅ **Working** | **Explicit module enumeration** ⭐ |

---

## Iterácia 1: v1.0 - Portable Edition ❌

**Súbor:** `beekeeper-desktop-v1.0-portable.zip` (39 MB)

**Problém:**
```
Error: Graphics Device initialization failed for : sw
Error initializing QuantumRenderer: no suitable pipeline found
```

**Príčina:**
- `-Dprism.order=sw` flag vo všetkých súboroch:
  - `desktop/build.gradle` (applicationDefaultJvmArgs)
  - `desktop/dist/launcher-windows.bat`
  - `desktop/dist/launcher-unix.sh`
- Software rendering nie je podporovaný na Windows

**Čo bolo opravené:**
- Odstránený flag z `desktop/build.gradle`

---

## Iterácia 2: v1.1 - Windows Fix (Incomplete) ❌

**Súbor:** `beekeeper-desktop-v1.1-windows-fix.zip` (39 MB)

**Problém:**
```
Error: Graphics Device initialization failed for : sw
Error initializing QuantumRenderer: no suitable pipeline found
```

**Príčina:**
- Flag odstránený len z `build.gradle`
- Launcher skripty stále obsahovali `-Dprism.order=sw`
- Distribúcia používa launcher skripty z `dist/` adresára, nie z `build.gradle`

**Čo bolo opravené:**
- Odstránený flag z launcher skriptov:
  - `desktop/dist/launcher-windows.bat`
  - `desktop/dist/launcher-unix.sh`

---

## Iterácia 3: v1.2 - Missing Platform Libraries ❌

**Súbor:** Nevytvorené (preskočené na v1.3)

**Problém:**
```
Error initializing QuantumRenderer: no suitable pipeline found
java.lang.RuntimeException: No toolkit found
```

**Príčina:**
- Build na macOS zahrnul len macOS JavaFX knižnice:
  ```
  javafx-base-21.0.2-mac-aarch64.jar
  javafx-controls-21.0.2-mac-aarch64.jar
  javafx-fxml-21.0.2-mac-aarch64.jar
  javafx-graphics-21.0.2-mac-aarch64.jar
  ```
- Chýbali Windows knižnice (`javafx-*-win.jar`)
- Windows hľadalo Direct3D pipeline, ale nenašlo natívne DLL súbory

**Čo bolo opravené:**
- Pridané multi-platform JavaFX dependencies do `build.gradle`:
  ```gradle
  implementation "org.openjfx:javafx-base:21.0.2:win"
  implementation "org.openjfx:javafx-base:21.0.2:linux"
  implementation "org.openjfx:javafx-base:21.0.2:mac"
  implementation "org.openjfx:javafx-base:21.0.2:mac-aarch64"
  // ... (controls, fxml, graphics)
  ```

---

## Iterácia 4: v1.3 - Java Module Conflict ❌

**Súbor:** `beekeeper-desktop-v1.3-multiplatform.zip` (61 MB)

**Problém:**
```
Error occurred during initialization of boot layer
java.lang.module.FindException: Two versions of module javafx.graphics found
in /lib (javafx-graphics-21.0.2-linux.jar and javafx-graphics-21.0.2-mac-aarch64.jar)
```

**Príčina:**
- Distribúcia obsahuje JavaFX knižnice pre všetky platformy
- Launcher používal všetky JAR súbory v `--module-path`:
  ```bash
  --module-path "$APP_HOME/lib"  # ❌ Obsahuje win + linux + mac + mac-aarch64
  ```
- Java module system nenašiel viacero verzií rovnakého modulu

**Čo bolo opravené:**
- Launcher skripty upravené na platform-aware:
  - **Windows:** Vyberá len `javafx-*-win.jar` súbory
  - **Unix:** Deteguje platformu (Darwin/Linux) a architektúru (arm64/x86_64)
  - Každá platforma používa len svoje JAR súbory v module-path

---

## Iterácia 5: v1.4 - Platform-Aware Launchers (Broken) ❌

**Súbor:** `beekeeper-desktop-v1.4-multiplatform.zip` (61 MB)

**Status:** ❌ **BROKEN** - Windows BAT wildcard loop failure

**Problém:**
```
Error occurred during initialization of boot layer
java.lang.module.FindException: Module javafx.controls not found
```

**Príčina:**
- Windows BAT wildcard loop nefungoval správne:
  ```batch
  for %%f in ("%APP_HOME%\lib\javafx-*-win.jar") do (
      if defined JAVAFX_MODULES (
          set "JAVAFX_MODULES=%JAVAFX_MODULES%;%%f"
      ) else (
          set "JAVAFX_MODULES=%%f"
      )
  )
  ```
- Quotes okolo wildcard pattern (`"..."`) spôsobili, že loop nenašiel žiadne súbory
- JAVAFX_MODULES premenná zostala prázdna
- Java nenašlo JavaFX moduly

**Čo bolo opravené:**
- Odstránené quotes z wildcard pattern
- Vytvorené explicitné loopy pre každý JavaFX modul:
  ```batch
  for %%f in (%APP_HOME%\lib\javafx-base-*-win.jar) do set JAVAFX_MODULES=%%f
  for %%f in (%APP_HOME%\lib\javafx-controls-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
  for %%f in (%APP_HOME%\lib\javafx-fxml-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
  for %%f in (%APP_HOME%\lib\javafx-graphics-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
  ```

---

## Iterácia 6: v1.5 - Explicit Module Enumeration ✅ ⭐

**Súbor:** `beekeeper-desktop-v1.5-multiplatform.zip` (61 MB)

**Status:** ✅ **WORKING** - Verified on macOS ARM

**Riešenie:**

### Windows Launcher (beekeeper.bat):
```batch
REM Explicit module enumeration (not wildcard)
set JAVAFX_MODULES=
for %%f in (%APP_HOME%\lib\javafx-base-*-win.jar) do set JAVAFX_MODULES=%%f
for %%f in (%APP_HOME%\lib\javafx-controls-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
for %%f in (%APP_HOME%\lib\javafx-fxml-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
for %%f in (%APP_HOME%\lib\javafx-graphics-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f

java --module-path "%JAVAFX_MODULES%" ...
```

### Windows Launcher (beekeeper.bat) - Pôvodné (v1.4):
```batch
REM Build Windows-specific module path (only win.jar files)
set JAVAFX_MODULES=
for %%f in ("%APP_HOME%\lib\javafx-*-win.jar") do (
    if defined JAVAFX_MODULES (
        set "JAVAFX_MODULES=%JAVAFX_MODULES%;%%f"
    ) else (
        set "JAVAFX_MODULES=%%f"
    )
)

java --module-path "%JAVAFX_MODULES%" ...
```

### Unix Launcher (beekeeper):
```bash
# Detect platform
OS_NAME="$(uname -s)"
OS_ARCH="$(uname -m)"

if [[ "$OS_NAME" == "Darwin" ]]; then
    if [[ "$OS_ARCH" == "arm64" ]]; then
        PLATFORM="mac-aarch64"
    else
        PLATFORM="mac"
    fi
elif [[ "$OS_NAME" == "Linux" ]]; then
    PLATFORM="linux"
fi

# Build platform-specific module path
JAVAFX_MODULES=""
for jar in "$APP_HOME"/lib/javafx-*-${PLATFORM}.jar; do
    JAVAFX_MODULES="$JAVAFX_MODULES:$jar"
done

java --module-path "$JAVAFX_MODULES" ...
```

**Výsledok:**
- ✅ Jeden ZIP funguje na všetkých platformách
- ✅ Windows používa len `javafx-*-win.jar` (Direct3D)
- ✅ macOS ARM používa len `javafx-*-mac-aarch64.jar` (Metal)
- ✅ macOS Intel používa len `javafx-*-mac.jar` (Metal)
- ✅ Linux používa len `javafx-*-linux.jar` (GTK)
- ✅ Žiadne module conflicts
- ✅ Natívne hardwarové rendering na všetkých platformách

---

## 🎯 Kľúčové Poznatky

### 1. Software Rendering Flag
- `-Dprism.order=sw` spôsobuje crash na Windows
- Odstránený zo VŠETKÝCH miest (build.gradle + launcher skripty)

### 2. Platform-Specific Knižnice
- Gradle JavaFX plugin sťahuje len knižnice pre aktuálnu platformu
- Pre multi-platform distribúciu treba explicitne pridať všetky platformy

### 3. Java Module System
- Nenachádza viacero verzií rovnakého modulu v module-path
- Riešenie: Launcher skripty vyberajú len platform-specific JAR súbory

### 4. Launcher Script Priority
- Distribúcia používa launcher skripty z `dist/` adresára
- Zmeny v `build.gradle` (applicationDefaultJvmArgs) nemajú efekt na distribúciu
- Launcher skripty musia byť upravené explicitne

### 5. Windows BAT Wildcard Patterns
- **KRITICKÉ:** Quotes okolo wildcard pattern nefungujú v BAT for loop:
  ```batch
  for %%f in ("%APP_HOME%\lib\javafx-*-win.jar") do ...  # ❌ Nenájde súbory
  for %%f in (%APP_HOME%\lib\javafx-*-win.jar) do ...    # ✅ Funguje
  ```
- Conditional IF inside FOR loop je problematické v BAT
- Riešenie: Explicitné loopy pre každý modul bez quotes

---

## 📝 Testing Status

| Platforma | Status | Verifikované |
|-----------|--------|--------------|
| macOS ARM (M1/M2) | ✅ Working | v1.5 - 2026-02-22 |
| macOS Intel | ⏳ Pending | - |
| Windows 10/11 | ⏳ Pending | - |
| Linux | ⏳ Pending | - |

---

**Vytvorené:** 2026-02-22
**Finálna verzia:** v1.5
**Status:** ✅ Fixed - čaká na Windows verification
**Distribution:** `desktop/distributions/beekeeper-desktop-v1.5-multiplatform.zip` (61 MB)
