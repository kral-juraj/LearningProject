# Build Distribution Skill - Changelog

## v2.0 (2026-02-22) - Multi-Platform Support

### Pridané

#### 1. Multi-Platform JavaFX Dependencies
- **KRITICKÉ:** Explicitné dependencies pre všetky platformy v build.gradle
- Windows, Linux, macOS (Intel + ARM) support
- 4 moduly (base, controls, fxml, graphics) × 4 platformy = 16 JAR súborov

```gradle
implementation "org.openjfx:javafx-base:21.0.2:win"
implementation "org.openjfx:javafx-base:21.0.2:linux"
implementation "org.openjfx:javafx-base:21.0.2:mac"
implementation "org.openjfx:javafx-base:21.0.2:mac-aarch64"
// ... (controls, fxml, graphics)
```

#### 2. Platform-Aware Launcher Scripts

**Windows (launcher-windows.bat):**
- Explicitná module enumeration pre každý JavaFX modul
- **KRITICKÉ:** Bez quotes okolo wildcard pattern
```batch
for %%f in (%APP_HOME%\lib\javafx-base-*-win.jar) do set JAVAFX_MODULES=%%f
for %%f in (%APP_HOME%\lib\javafx-controls-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
```

**Unix (launcher-unix.sh):**
- Platform detection: Darwin vs Linux
- Architecture detection: arm64 vs x86_64
- Dynamic module selection based on platform

#### 3. Overenie Multi-Platform Setup
- Nový krok 1: Overenie build.gradle pred buildovaním
- Overenie launcher skriptov
- Overenie prítomnosti všetkých platform-specific JAR súborov v distribúcii

#### 4. Rozšírený Checklist
- Multi-platform setup checks
- JavaFX knižnice verification
- Launcher scripts validation
- Version tracking

#### 5. Nové Časté Problémy

**Windows Specific:**
- "no suitable pipeline found" → chýbajú Windows knižnice
- "Module javafx.controls not found" → quotes okolo wildcard
- Java module conflict → launcher používa všetky JAR súbory

**Riešenia:**
- Explicitná module enumeration v BAT
- Multi-platform dependencies v build.gradle
- Platform detection v Unix launcher

#### 6. Version Tracking
- Minor version incrementing (v1.5 → v1.6)
- Dokumentácia VERSION_HISTORY.md
- README.md updates

#### 7. Kritické Poznatky
- Software rendering flag crashuje Windows
- Windows BAT wildcard quotes problem
- Java module system limitations
- Multi-platform dependency requirements

### Zmenené

#### Distribúcia
- **Veľkosť:** 39 MB → 61 MB (multi-platform knižnice)
- **Názov:** `beekeeper-desktop-vX.Y-multiplatform.zip`
- **Support:** Len macOS → Windows + macOS + Linux

#### Test Data
- 1 včelnica, 2 úle → pridané 10 inspections + 10 taxations

#### Testovanie
- Rozšírené overenie multi-platform knižníc
- Platform-aware launcher verification
- Portable database testing

### Odstránené

- Single-platform build approach
- `-Dprism.order=sw` flag (crashoval Windows)
- Quotes okolo wildcard pattern v Windows BAT

---

## v1.0 (2026-02-22) - Initial Version

### Pridané
- Database export workflow
- DatabaseInitializer.java integration
- build.gradle distribution configuration
- Basic testing steps
- Initial checklist

---

## Migračný Návod: v1.0 → v2.0

Ak používaš starú verziu skillu (v1.0):

### 1. Aktualizuj build.gradle
```gradle
// Pridaj multi-platform dependencies
def javaFxVersion = '21.0.2'
dependencies {
    implementation "org.openjfx:javafx-base:${javaFxVersion}:win"
    implementation "org.openjfx:javafx-base:${javaFxVersion}:linux"
    implementation "org.openjfx:javafx-base:${javaFxVersion}:mac"
    implementation "org.openjfx:javafx-base:${javaFxVersion}:mac-aarch64"
    // ... (controls, fxml, graphics)
}
```

### 2. Aktualizuj Windows Launcher
```batch
# Odstráň quotes, použi explicit enumeration
for %%f in (%APP_HOME%\lib\javafx-base-*-win.jar) do set JAVAFX_MODULES=%%f
for %%f in (%APP_HOME%\lib\javafx-controls-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
for %%f in (%APP_HOME%\lib\javafx-fxml-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
for %%f in (%APP_HOME%\lib\javafx-graphics-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
```

### 3. Aktualizuj Unix Launcher
```bash
# Pridaj platform detection
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
for jar in "$APP_HOME"/lib/javafx-*-${PLATFORM}.jar; do
    JAVAFX_MODULES="$JAVAFX_MODULES:$jar"
done
```

### 4. Odstráň -Dprism.order=sw
```gradle
// applicationDefaultJvmArgs - NESMIE obsahovať:
'-Dprism.order=sw'  // ❌ Odstráň
```

### 5. Rebuild Distribúciu
```bash
gradle desktop:clean desktop:distZip
```

### 6. Overte Multi-Platform Support
```bash
unzip -l build/distributions/*.zip | grep javafx | wc -l
# Mal by ukázať 16 (4 moduly × 4 platformy)
```

---

**Vytvorené:** 2026-02-22
**Aktuálna verzia:** v2.0
**Kompatibilita:** Windows 10+, macOS 10.14+, Linux (Ubuntu 20.04+)
