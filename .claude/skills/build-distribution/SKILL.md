---
name: build-distribution
description: Vytvori multi-platform distribucny balik aplikacie (Windows/macOS/Linux) s kompletnou databazou, translations a test datami
allowed-tools: Bash, Read, Write, Edit, Glob, Grep
---

# Build Distribution - Multi-Platform Distribucny Balik

## Ucel
Vytvori produkcne pripraveny multi-platform ZIP balik Beekeeper Desktop aplikacie s:
- **Multi-platform support** (Windows, macOS Intel/ARM, Linux)
- **Platform-aware launchers** - automaticka detekcia platformy
- Automatickou inicializaciou databazy
- Vsetkymi 785 translation keys (SK/EN)
- Test datami (1 vcelnica, 2 ule, 10 inspections, 10 taxations)
- Nativnym hardwarovym renderingom (Direct3D/Metal/GTK)

## Kedy pouzit
- Pred odoslanim aplikacie testerom
- Pri vytvarani release verzie
- Po pridani novych translations alebo schemy databazy
- Ked treba refreshnut distribucny balik
- **VZDY pri zmene launcher skriptov alebo JavaFX dependencies**

## KRITICKY PRE WINDOWS/MAC SUPPORT

### Multi-Platform JavaFX Dependencies

**DOLEZITE:** Desktop distribúcia MUSI obsahovať JavaFX knižnice pre VŠETKY platformy.

#### Overenie build.gradle

```gradle
// desktop/build.gradle

javafx {
    version = "21.0.2"
    modules = ['javafx.controls', 'javafx.fxml']
}

// CRITICAL: Multi-platform JavaFX dependencies
def javaFxVersion = '21.0.2'

dependencies {
    implementation project(':shared')

    // Multi-platform JavaFX support - REQUIRED for cross-platform distribution
    implementation "org.openjfx:javafx-base:${javaFxVersion}:win"
    implementation "org.openjfx:javafx-base:${javaFxVersion}:linux"
    implementation "org.openjfx:javafx-base:${javaFxVersion}:mac"
    implementation "org.openjfx:javafx-base:${javaFxVersion}:mac-aarch64"

    implementation "org.openjfx:javafx-controls:${javaFxVersion}:win"
    implementation "org.openjfx:javafx-controls:${javaFxVersion}:linux"
    implementation "org.openjfx:javafx-controls:${javaFxVersion}:mac"
    implementation "org.openjfx:javafx-controls:${javaFxVersion}:mac-aarch64"

    implementation "org.openjfx:javafx-fxml:${javaFxVersion}:win"
    implementation "org.openjfx:javafx-fxml:${javaFxVersion}:linux"
    implementation "org.openjfx:javafx-fxml:${javaFxVersion}:mac"
    implementation "org.openjfx:javafx-fxml:${javaFxVersion}:mac-aarch64"

    implementation "org.openjfx:javafx-graphics:${javaFxVersion}:win"
    implementation "org.openjfx:javafx-graphics:${javaFxVersion}:linux"
    implementation "org.openjfx:javafx-graphics:${javaFxVersion}:mac"
    implementation "org.openjfx:javafx-graphics:${javaFxVersion}:mac-aarch64"

    // Other dependencies...
}
```

### Platform-Aware Launcher Scripts

#### Windows Launcher (desktop/dist/launcher-windows.bat)

**CRITICAL:** Explicitna module enumeration (NIE wildcard s quotes)

```batch
@echo off
REM Beekeeper Desktop Launcher Script for Windows

REM Get application home directory
set APP_HOME=%~dp0..

REM Build Windows-specific classpath (all JARs for classpath)
set APP_CLASSPATH=%APP_HOME%\lib\*

REM Build Windows-specific module path (only win.jar files for modules)
REM CRITICAL: No quotes around wildcard pattern
set JAVAFX_MODULES=
for %%f in (%APP_HOME%\lib\javafx-base-*-win.jar) do set JAVAFX_MODULES=%%f
for %%f in (%APP_HOME%\lib\javafx-controls-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
for %%f in (%APP_HOME%\lib\javafx-fxml-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
for %%f in (%APP_HOME%\lib\javafx-graphics-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f

REM Execute application with Windows-specific JavaFX module-path
REM Note: Uses only javafx-*-win.jar to avoid module conflicts
java ^
  -Djavafx.animation.fullspeed=false ^
  --enable-native-access=ALL-UNNAMED ^
  --module-path "%JAVAFX_MODULES%" ^
  --add-modules javafx.controls,javafx.fxml ^
  -cp "%APP_CLASSPATH%" ^
  com.beekeeper.desktop.Main
```

**POZOR:** NIKDY nepouzit quotes okolo wildcard: `for %%f in ("%APP_HOME%\lib\javafx-*-win.jar")`
- Quotes sposobia ze loop nenajde ziadne subory
- JAVAFX_MODULES zostane prazdny
- Error: "Module javafx.controls not found"

#### Unix Launcher (desktop/dist/launcher-unix.sh)

**Platform detection + dynamic module selection**

```bash
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
```

## Kroky Vykonania

### 1. Overenie Multi-Platform Setup

**PRED buildovanim distribucie VZDY overte:**

```bash
# 1. Check build.gradle ma multi-platform dependencies
grep "javafx-base.*:win" desktop/build.gradle
grep "javafx-base.*:linux" desktop/build.gradle
grep "javafx-base.*:mac-aarch64" desktop/build.gradle

# 2. Check Windows launcher NEMA quotes okolo wildcard
grep 'for %%f in (%APP_HOME%' desktop/dist/launcher-windows.bat

# 3. Check Unix launcher ma platform detection
grep 'uname -s' desktop/dist/launcher-unix.sh
```

**Ak nieco chyba, STOP a oprav najprv!**

### 2. Priprava Databazoveho Exportu

#### 1.1 Vytvorenie cistej databazy zo vsetkych SQL skriptov
```bash
cd desktop/src/main/resources/sql

# Vytvorit novu prazdnu databazu
rm -f /tmp/beekeeper-complete.db
sqlite3 /tmp/beekeeper-complete.db "SELECT 'Database created';"

# Nacitat vsetky SQL skripty v spravnom poradi
sqlite3 /tmp/beekeeper-complete.db < 01_schema.sql
sqlite3 /tmp/beekeeper-complete.db < 02_translations_queen_standard.sql
# ... vsetky ostatne v poradi ...
sqlite3 /tmp/beekeeper-complete.db < 20_translations_fix_supering.sql
```

**Poznamka:** Pouzit `ls -1 *.sql | sort` pre spravne poradie suborov.

#### 1.2 Overenie schemy databazy
```bash
# Skontrolovat aktualnu schemu z DatabaseManager
gradle desktop:run &
APP_PID=$!
sleep 5
kill $APP_PID

# Overit stlpce v tabulkach
sqlite3 ~/beekeeper-desktop.db "PRAGMA table_info(apiaries);"
sqlite3 ~/beekeeper-desktop.db "PRAGMA table_info(hives);"
```

#### 1.3 Pridanie test dat s aktualnou schemou
```bash
sqlite3 ~/beekeeper-desktop.db << 'EOF'
DELETE FROM apiaries;
DELETE FROM hives;

INSERT OR REPLACE INTO apiaries (
    id, name, location, latitude, longitude, 
    displayOrder, registrationNumber, address, description, 
    createdAt, updatedAt
) VALUES (
    'test-001', 'Test Apiary', NULL, NULL, NULL, 
    0, NULL, NULL, NULL, 
    1707854400000, 1707854400000
);

INSERT OR REPLACE INTO hives (
    id, apiaryId, name, type, queenId, queenYear, 
    queenColor, active, notes, createdAt, updatedAt
) VALUES (
    'hive-001', 'test-001', 'Test Hive 1', NULL, NULL, NULL,
    NULL, 1, NULL, 1707854400000, 1707854400000
);
EOF
```

**DOLEZITE:** Vzdy pouzit aktualnu schemu z DatabaseManager!

#### 1.4 Export INSERT prikazov
```bash
cd desktop/src/main/resources/sql

sqlite3 ~/beekeeper-desktop.db << 'EOF' > database_inserts_only.sql
.mode insert apiaries
SELECT * FROM apiaries;
.mode insert hives  
SELECT * FROM hives;
.mode insert translations
SELECT * FROM translations;
EOF

# Overit export
wc -l database_inserts_only.sql
```

**Vystup:** Subor `database_inserts_only.sql` s ~1,573 INSERT prikazmi.

### 2. Aktualizacia DatabaseInitializer.java

Upravit loadTranslations() metodu:
```java
private static void loadTranslations() throws SQLException {
    String sqlContent = loadResourceFile("/sql/database_inserts_only.sql");
    if (sqlContent == null) {
        System.err.println("WARNING: Could not load database inserts file");
        return;
    }
    executeSQL(sqlContent);
}
```

Upravit executeSQL() aby skipovala DDL prikazy:
```java
String sqlUpper = sql.toUpperCase();
if (sqlUpper.startsWith("CREATE TABLE") ||
    sqlUpper.startsWith("CREATE INDEX") ||
    sqlUpper.startsWith("PRAGMA")) {
    continue;
}
```

### 3. Aktualizacia build.gradle (Distribution Contents)

```gradle
// desktop/build.gradle

// CRITICAL: applicationDefaultJvmArgs NESMIE obsahovat -Dprism.order=sw
application {
    mainClass = 'com.beekeeper.desktop.Main'
    applicationDefaultJvmArgs = [
        // NO -Dprism.order=sw - crashuje na Windows!
        '-Djavafx.animation.fullspeed=false',
        '-Dapple.awt.application.appearance=system',
        '--enable-native-access=ALL-UNNAMED',
        '--module-path', 'LIB_PLACEHOLDER',
        '--add-modules', 'javafx.controls,javafx.fxml'
    ]
}

distributions {
    main {
        distributionBaseName = 'beekeeper-desktop'
        contents {
            // Database initialization SQL
            from('src/main/resources/sql') {
                include 'database_inserts_only.sql'
                into 'sql'
            }
            // README
            from('dist') {
                include 'README.txt'
            }
            // Custom launcher scripts (override Gradle generated ones)
            from('dist') {
                include 'launcher-unix.sh'
                include 'launcher-windows.bat'
                into 'bin'
                rename 'launcher-unix.sh', 'beekeeper'
                rename 'launcher-windows.bat', 'beekeeper.bat'
            }
        }
    }
}
```

### 4. Testovanie Lokalne

```bash
rm -f ~/beekeeper-desktop.db
gradle desktop:clean desktop:build
gradle desktop:run
```

**Overenia:**
```bash
sqlite3 ~/beekeeper-desktop.db << 'EOF'
SELECT COUNT(DISTINCT key) FROM translations;
SELECT COUNT(*) FROM apiaries;
SELECT COUNT(*) FROM hives;
EOF
```

**Ocakavane vysledky:** 785, 1, 2

### 5. Vytvorenie Distribucie

```bash
# Clean build pre istotu
gradle desktop:clean desktop:distZip

# Check velkost (mala by byt ~61 MB pre multi-platform)
ls -lh desktop/build/distributions/beekeeper-desktop.zip
```

**Ocakavana velkost:** ~61 MB (obsahuje Windows + Linux + macOS kniznice)

### 6. Overenie Multi-Platform Kniznic v Distribuci

```bash
cd /tmp
rm -rf test-dist
unzip -q desktop/build/distributions/beekeeper-desktop.zip -d test-dist

# CRITICAL: Overte ze distribucia obsahuje VSETKY platformy
ls test-dist/beekeeper-desktop/lib/ | grep javafx | sort
```

**Ocakavany vystup:**
```
javafx-base-21.0.2-linux.jar
javafx-base-21.0.2-mac-aarch64.jar
javafx-base-21.0.2-mac.jar
javafx-base-21.0.2-win.jar
javafx-controls-21.0.2-linux.jar
javafx-controls-21.0.2-mac-aarch64.jar
javafx-controls-21.0.2-mac.jar
javafx-controls-21.0.2-win.jar
javafx-fxml-21.0.2-linux.jar
javafx-fxml-21.0.2-mac-aarch64.jar
javafx-fxml-21.0.2-mac.jar
javafx-fxml-21.0.2-win.jar
javafx-graphics-21.0.2-linux.jar
javafx-graphics-21.0.2-mac-aarch64.jar
javafx-graphics-21.0.2-mac.jar
javafx-graphics-21.0.2-win.jar
```

**AK CHYBAJU WINDOWS ALEBO LINUX KNIZNICE:**
- STOP - distribucia nebude fungovat na tych platformach
- Skontroluj build.gradle multi-platform dependencies
- Rebuild: `gradle desktop:clean desktop:distZip`

### 7. Overenie Launcher Skriptov

```bash
# Check Windows launcher
cat test-dist/beekeeper-desktop/bin/beekeeper.bat | grep "javafx-base-\*-win.jar"
cat test-dist/beekeeper-desktop/bin/beekeeper.bat | grep "javafx-controls-\*-win.jar"

# Check Unix launcher
cat test-dist/beekeeper-desktop/bin/beekeeper | grep "uname -s"
cat test-dist/beekeeper-desktop/bin/beekeeper | grep "PLATFORM="
```

**AK launcher skripty NIE SU platform-aware:**
- Skontroluj desktop/dist/launcher-*.sh a *.bat
- Rebuild distribucie

### 8. Testovanie Distribucie (macOS/Linux)

```bash
cd /tmp/test-dist/beekeeper-desktop/bin
chmod +x beekeeper
rm -rf ../data  # Clean test

# Launch and wait
./beekeeper > /tmp/dist-test.log 2>&1 &
APP_PID=$!
sleep 5
kill $APP_PID 2>/dev/null || true

# Verify
cat /tmp/dist-test.log | head -50
sqlite3 /tmp/test-dist/beekeeper-desktop/data/beekeeper.db "SELECT COUNT(*) FROM translations;"
sqlite3 /tmp/test-dist/beekeeper-desktop/data/beekeeper.db "SELECT COUNT(*) FROM apiaries;"
```

**Ocakavane vysledky:**
- `1570` translations
- `1` apiary
- Log obsahuje "Database initialized (785 translation keys)"

### 9. Vytvorenie Versionovanej Distribucie

```bash
# Get current version number (increment from last)
ls -1 desktop/distributions/*.zip | tail -1

# Create new version (e.g., v1.5 -> v1.6)
NEW_VERSION="v1.6"
cp desktop/build/distributions/beekeeper-desktop.zip \
   desktop/distributions/beekeeper-desktop-${NEW_VERSION}-multiplatform.zip

ls -lh desktop/distributions/beekeeper-desktop-${NEW_VERSION}-multiplatform.zip
```

### 10. Update Dokumentacie

```bash
# Update desktop/distributions/README.md s novou verziou
# Update desktop/distributions/VERSION_HISTORY.md
```

## Checklist

### Multi-Platform Setup
- [ ] build.gradle ma multi-platform JavaFX dependencies (win, linux, mac, mac-aarch64)
- [ ] desktop/dist/launcher-windows.bat pouziva explicit module enumeration
- [ ] desktop/dist/launcher-windows.bat NEMA quotes okolo wildcard pattern
- [ ] desktop/dist/launcher-unix.sh ma platform detection (uname -s, uname -m)
- [ ] applicationDefaultJvmArgs NEOBSAHUJE -Dprism.order=sw

### Database & Translations
- [ ] database_inserts_only.sql ma ~1,599 INSERT prikazov
- [ ] DatabaseInitializer.java pouziva database_inserts_only.sql
- [ ] build.gradle zahrnuje database_inserts_only.sql
- [ ] Databaza obsahuje 785 keys, 1 apiary, 2 hives, 10 inspections, 10 taxations

### Distribúcia
- [ ] Distribúcia je ~61 MB (nie 39 MB)
- [ ] lib/ obsahuje javafx-*-win.jar, javafx-*-linux.jar, javafx-*-mac*.jar
- [ ] Launcher skripty su platform-aware
- [ ] Lokalne testovanie uspesne (macOS/Linux)
- [ ] Aplikacia startuje bez chyb
- [ ] Version number inkrementovany

## Caste Problemy

### Windows: "no suitable pipeline found"
**Pricina:** Chybaju Windows JavaFX kniznice v distribuci
**Riesenie:**
1. Skontroluj build.gradle ma `implementation "org.openjfx:javafx-base:21.0.2:win"`
2. Rebuild: `gradle desktop:clean desktop:distZip`
3. Overte: `unzip -l build/distributions/*.zip | grep javafx-base.*win`

### Windows: "Module javafx.controls not found"
**Pricina:** Windows BAT launcher pouziva quotes okolo wildcard pattern
**Riesenie:**
```batch
# NESPRAVNE (nenajde subory):
for %%f in ("%APP_HOME%\lib\javafx-*-win.jar") do ...

# SPRAVNE:
for %%f in (%APP_HOME%\lib\javafx-base-*-win.jar) do set JAVAFX_MODULES=%%f
for %%f in (%APP_HOME%\lib\javafx-controls-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
```

### Java: "Two versions of module javafx.graphics found"
**Pricina:** Launcher pouziva vsetky JAR subory v module-path
**Riesenie:** Launcher musi vybrat LEN platform-specific JAR subory:
- Windows: len javafx-*-win.jar
- macOS ARM: len javafx-*-mac-aarch64.jar
- macOS Intel: len javafx-*-mac.jar
- Linux: len javafx-*-linux.jar

### Distribúcia je len 39 MB (nie 61 MB)
**Pricina:** Chybaju multi-platform JavaFX dependencies
**Riesenie:** Pridat do build.gradle dependencies pre vsetky platformy

### "table has X columns but Y values were supplied"
**Riesenie:** Regenerovat database_inserts_only.sql z novej DB po spusteni aplikacie

### "Resource 'xxx' not found"
**Riesenie:** Pridat chybajuce translation keys do SQL skriptov a regenerovat export

### Prazdna databaza
**Riesenie:** Skontrolovat ze executeSQL() skipuje CREATE TABLE prikazy

## Vysledok

Po uspesnom vykonani:

### Distribucia
- `desktop/distributions/beekeeper-desktop-vX.Y-multiplatform.zip` (61 MB)
- **Multi-platform support:** Windows, macOS (Intel + ARM), Linux
- **Platform-aware launchers** s automatickou detekciou

### JavaFX Kniznice
- 16 JAR suborov (4 moduly × 4 platformy):
  - javafx-base, javafx-controls, javafx-fxml, javafx-graphics
  - win, linux, mac, mac-aarch64
- **Native hardware rendering:**
  - Windows: Direct3D
  - macOS: Metal
  - Linux: GTK

### Database & Translations
- 785 translation keys (SK/EN) = 1,570 records
- 1 testovacia vcelnica
- 2 testovacie ule
- 10 realistickych inspections (Marec-Jul 2024)
- 10 realistickych taxations s frame details
- Automaticka inicializacia pri prvom spusteni
- **Portable** - databaza v data/ subdirectory

### Testovanie
- ✅ Ready for testing na Windows
- ✅ Ready for testing na macOS (Intel + ARM)
- ✅ Ready for testing na Linux

---

## Version Tracking

**DOLEZITE:** Vzdy inkrementuj minor version pri vytvarani novej distribucie.

Aktualna verzia: **v1.5**
Nasledujuca verzia: **v1.6**

```bash
# Pri vytvarani novej distribucie:
NEW_VERSION="v1.6"
cp desktop/build/distributions/beekeeper-desktop.zip \
   desktop/distributions/beekeeper-desktop-${NEW_VERSION}-multiplatform.zip

# Update dokumentacie:
# - desktop/distributions/README.md
# - desktop/distributions/VERSION_HISTORY.md
```

---

## Kriticke Poznatky z v1.0 → v1.5

### 1. Software Rendering Flag
- ❌ `-Dprism.order=sw` crashuje JavaFX na Windows
- ✅ Odstranit z build.gradle a launcher skriptov
- ✅ Pouzit native hardware rendering (Direct3D/Metal/GTK)

### 2. Multi-Platform JavaFX
- ❌ Gradle JavaFX plugin stiahne len kniznice pre aktualnu platformu
- ✅ Explicitne pridat vsetky platformy do dependencies

### 3. Java Module System
- ❌ Nenahradzuje viacero verzii rovnakeho modulu v module-path
- ✅ Launcher skripty musia vybrat LEN platform-specific JAR subory

### 4. Windows BAT Wildcards
- ❌ Quotes okolo wildcard pattern nefunguju: `for %%f in ("%APP_HOME%\lib\*.jar")`
- ✅ Pouzit bez quotes: `for %%f in (%APP_HOME%\lib\javafx-base-*-win.jar)`
- ✅ Explicitna module enumeration pre kazdy JavaFX modul

---

**Posledna aktualizacia:** 2026-02-22
**Aktualna verzia:** v1.5
**Status:** Production Ready - Windows & macOS Verified
**Distribucia:** 61 MB multi-platform ZIP
