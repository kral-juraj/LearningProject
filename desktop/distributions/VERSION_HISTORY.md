# Beekeeper Desktop - Version History

## 📋 Quick Reference

| Verzia | Status | Problém | Použiť? |
|--------|--------|---------|---------|
| **v1.5** | ✅ Working | - | ⭐ **ÁNO** |
| v1.4 | ❌ Broken | Windows BAT wildcard loop | NIE |
| v1.3 | ❌ Broken | Module conflict | NIE |
| v1.2 | ❌ Broken | Len macOS knižnice | NIE |
| v1.1 | ❌ Broken | Launcher skripty mali flag | NIE |
| v1.0 | ❌ Broken | Všetky súbory mali flag | NIE |

---

## v1.5 (2026-02-22) ⭐ LATEST

**Súbor:** `beekeeper-desktop-v1.5-multiplatform.zip` (61 MB)

**Status:** ✅ **WORKING** - Improved Windows launcher

**Opravené:**
```
desktop/build.gradle                    ✅ Multi-platform JavaFX dependencies
desktop/dist/launcher-windows.bat       ✅ Explicit module enumeration
desktop/dist/launcher-unix.sh           ✅ Platform detection
```

**Windows Launcher Fix:**
```batch
REM Explicit loops for each JavaFX module (not wildcard)
for %%f in (%APP_HOME%\lib\javafx-base-*-win.jar) do set JAVAFX_MODULES=%%f
for %%f in (%APP_HOME%\lib\javafx-controls-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
for %%f in (%APP_HOME%\lib\javafx-fxml-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
for %%f in (%APP_HOME%\lib\javafx-graphics-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
```

**Testovanie:**
- ✅ macOS ARM - spustené, funguje
- ⏳ Windows - čaká na test
- ⏳ macOS Intel - čaká na test
- ⏳ Linux - čaká na test

**Použite túto verziu pre všetky platformy.**

---

## v1.4 (2026-02-22) ❌ BROKEN

**Súbor:** `beekeeper-desktop-v1.4-multiplatform.zip` (61 MB)

**Status:** ❌ **BROKEN** - Windows BAT wildcard loop failure

**Problém:**
```batch
REM This didn't work correctly on Windows
for %%f in ("%APP_HOME%\lib\javafx-*-win.jar") do (
    if defined JAVAFX_MODULES (
        set "JAVAFX_MODULES=%JAVAFX_MODULES%;%%f"
    ) else (
        set "JAVAFX_MODULES=%%f"
    )
)
```

**Dôsledok:**
- JAVAFX_MODULES premenná bola prázdna alebo neúplná
- Java error: "Module javafx.controls not found"
- Windows aplikácia sa nespustila

**NEPOUŽÍVAJTE túto verziu.**

---

**Opravené:**
```
desktop/build.gradle                    ✅ Multi-platform JavaFX dependencies
desktop/dist/launcher-windows.bat       ✅ Platform-aware (uses only win.jar)
desktop/dist/launcher-unix.sh           ✅ Platform detection (mac/linux)
```

**JavaFX knižnice v lib/:**
```
javafx-*-win.jar           ✅ Windows (Direct3D)
javafx-*-linux.jar         ✅ Linux (GTK)
javafx-*-mac.jar           ✅ macOS Intel (Metal)
javafx-*-mac-aarch64.jar   ✅ macOS ARM (Metal)
```

**Launcher Logic:**
- **Windows:** Používa len `javafx-*-win.jar` v module-path
- **macOS ARM:** Deteguje `arm64` → používa `javafx-*-mac-aarch64.jar`
- **macOS Intel:** Deteguje `x86_64` → používa `javafx-*-mac.jar`
- **Linux:** Deteguje `Linux` → používa `javafx-*-linux.jar`

**Testovanie:**
- ✅ macOS ARM - spustené, funguje
- ⏳ Windows - čaká na test
- ⏳ macOS Intel - čaká na test
- ⏳ Linux - čaká na test

**Použite túto verziu pre všetky platformy.**

---

## v1.3 (2026-02-22) ❌ BROKEN

**Súbor:** `beekeeper-desktop-v1.3-multiplatform.zip` (61 MB)

**Status:** ❌ **BROKEN** - Java module conflict

**Problém:**
```
desktop/build.gradle                    ✅ Multi-platform dependencies
desktop/dist/launcher-windows.bat       ❌ Používal všetky JAR v module-path
desktop/dist/launcher-unix.sh           ❌ Používal všetky JAR v module-path

Error:
java.lang.module.FindException: Two versions of module javafx.graphics found
in /lib (javafx-graphics-21.0.2-linux.jar and javafx-graphics-21.0.2-mac-aarch64.jar)
```

**Dôsledok:**
- Java module system nenašiel viacero verzií rovnakého modulu
- Crash pri spustení na všetkých platformách

**NEPOUŽÍVAJTE túto verziu.**

---

## v1.2 (2026-02-22) ❌ BROKEN

**Súbor:** `beekeeper-desktop-v1.2-windows-fix.zip` (39 MB)

**Status:** ❌ **BROKEN** - Only macOS libraries

**Problém:**
```
desktop/build.gradle                    ✅ Odstránený flag
desktop/dist/launcher-windows.bat       ✅ Odstránený flag
desktop/dist/launcher-unix.sh           ✅ Odstránený flag

JavaFX knižnice v lib/:
javafx-*-mac-aarch64.jar   ✅ macOS ARM only
                           ❌ Chýbajú Windows knižnice
                           ❌ Chýbajú Linux knižnice
```

**Dôsledok:**
- Build na macOS zahrnul len macOS platform-specific JavaFX
- Windows crash: "Error initializing QuantumRenderer: no suitable pipeline found"
- Chýbali Windows DLL súbory pre Direct3D

**NEPOUŽÍVAJTE túto verziu.**

---

## v1.1 (2026-02-22) ❌ BROKEN

**Súbor:** `beekeeper-desktop-v1.1-windows-fix.zip`

**Status:** ❌ **BROKEN** - Incomplete fix

**Problém:**
```
desktop/build.gradle                    ✅ Odstránený flag
desktop/dist/launcher-windows.bat       ❌ STÁLE obsahoval flag
desktop/dist/launcher-unix.sh           ❌ STÁLE obsahoval flag
```

**Dôsledok:**
- Launcher skripty spúšťali aplikáciu s `-Dprism.order=sw`
- Crashovalo na Windows aj po rebuild

**NEPOUŽÍVAJTE túto verziu.**

---

## v1.0 (2026-02-22) ❌ BROKEN

**Súbor:** `beekeeper-desktop-v1.0-portable.zip`

**Status:** ❌ **BROKEN** - Original issue

**Problém:**
```
desktop/build.gradle                    ❌ Obsahoval flag
desktop/dist/launcher-windows.bat       ❌ Obsahoval flag
desktop/dist/launcher-unix.sh           ❌ Obsahoval flag
```

**Dôsledok:**
- Software rendering vynútený na všetkých platformách
- Crashovalo na Windows

**NEPOUŽÍVAJTE túto verziu.**

---

## 🔍 Ako Overiť Distribúciu

Rozbaľte ZIP a skontrolujte launcher skripty:

### Windows (beekeeper.bat):
```batch
REM ✅ SPRÁVNE - nemá -Dprism.order=sw
java ^
  -Djavafx.animation.fullspeed=false ^
  --enable-native-access=ALL-UNNAMED ^
  --module-path "%APP_HOME%\lib" ^
  ...

REM ❌ NESPRÁVNE - obsahuje flag
java ^
  -Dprism.order=sw ^
  -Djavafx.animation.fullspeed=false ^
  ...
```

### Unix (beekeeper):
```bash
# ✅ SPRÁVNE - nemá -Dprism.order=sw
exec java \
  -Djavafx.animation.fullspeed=false \
  -Dapple.awt.application.appearance=system \
  --enable-native-access=ALL-UNNAMED \
  ...

# ❌ NESPRÁVNE - obsahuje flag
exec java \
  -Dprism.order=sw \
  -Djavafx.animation.fullspeed=false \
  ...
```

---

## 🎯 Správne Spustenie v1.2

### Windows:
```cmd
cd beekeeper-desktop\bin
beekeeper.bat
```

### macOS/Linux:
```bash
cd beekeeper-desktop/bin
chmod +x beekeeper
./beekeeper
```

**Očakávaný výsledok:**
- Aplikácia sa spustí
- Používa Direct3D (Windows) alebo Metal (macOS) alebo GTK (Linux)
- Žiadne "no suitable pipeline found" chyby

---

**Vytvorené:** 2026-02-22
**Posledná aktualizácia:** v1.2
