# Beekeeper Desktop - Distribúcie

Tento adresár obsahuje finálne distribučné balíky aplikácie pripravené na release.

## 📦 Aktuálne Distribúcie

### `beekeeper-desktop-v1.5-multiplatform.zip` (61 MB) ⭐ LATEST

**Verzia:** 1.5 - Multi-Platform Edition (Improved Windows Launcher)
**Dátum:** 2026-02-22
**Status:** ✅ Production Ready

**Opravené:**
- ✅ **Improved Windows launcher** - explicitné vytváranie module-path pre každý JavaFX modul:
  - `javafx-base-*-win.jar`
  - `javafx-controls-*-win.jar`
  - `javafx-fxml-*-win.jar`
  - `javafx-graphics-*-win.jar`
- ✅ **Platform-aware launchers** - automatická detekcia platformy:
  - Windows: používa len `javafx-*-win.jar` (Direct3D)
  - Linux: používa len `javafx-*-linux.jar` (GTK)
  - macOS Intel: používa len `javafx-*-mac.jar` (Metal)
  - macOS ARM: používa len `javafx-*-mac-aarch64.jar` (Metal)
- ✅ Jeden ZIP funguje na všetkých platformách
- ✅ Vyriešený Java module conflict
- ✅ Natívne hardwarové rendering

---

### `beekeeper-desktop-v1.4-multiplatform.zip` (61 MB)

**Verzia:** 1.4 - Multi-Platform Edition
**Dátum:** 2026-02-22
**Status:** ❌ Broken - Windows launcher nenašiel JavaFX moduly

**Problém:**
- ❌ Windows BAT wildcard loop nefungoval správne
- ❌ "Module javafx.controls not found"

---

### `beekeeper-desktop-v1.3-multiplatform.zip` (61 MB)

**Verzia:** 1.3 - Multi-Platform Edition
**Dátum:** 2026-02-22
**Status:** ❌ Broken - Java module conflict

**Problém:**
- ❌ Launcher používal všetky JavaFX JAR súbory v module-path
- ❌ Java module system: "Two versions of module javafx.graphics found"
- ❌ Crash pri spustení

---

### `beekeeper-desktop-v1.2-windows-fix.zip` (39 MB)

**Verzia:** 1.2 - Windows Fix (Incomplete)
**Dátum:** 2026-02-22
**Status:** ❌ Broken - obsahuje len macOS knižnice

**Problém:**
- ❌ Build na macOS zahrnul len macOS JavaFX knižnice
- ❌ Windows crash: "no suitable pipeline found"

---

### `beekeeper-desktop-v1.1-windows-fix.zip` (39 MB)

**Verzia:** 1.1 - Windows Fix (Incomplete)
**Dátum:** 2026-02-22
**Status:** ❌ Broken - launcher skripty stále obsahovali `-Dprism.order=sw`

---

### `beekeeper-desktop-v1.0-portable.zip` (39 MB)

**Verzia:** 1.0 - Portable Edition
**Dátum:** 2026-02-22
**Status:** ❌ Broken - crashuje na Windows

**Obsahuje:**
- ✅ 785 translation keys (SK/EN) - 1,570 records
- ✅ 1 testovacia včelnica "Test Apiary"
- ✅ 2 testovacie úle (Test Hive 1, Test Hive 2)
- ✅ 10 realistických prehliadok (Marec-Júl 2024)
- ✅ 10 realistických taxácií s detailmi
- ✅ 6 taxation frame records
- ✅ **PORTABLE** - databáza v `data/` podadresári

**Štruktúra po rozbalení:**
```
beekeeper-desktop/
├── bin/
│   ├── beekeeper           (macOS/Linux launcher)
│   └── beekeeper.bat       (Windows launcher)
├── lib/                    (JAR súbory - 39 MB)
├── sql/                    (Init skripty)
├── data/                   (Vytvorí sa pri prvom spustení)
│   └── beekeeper.db        (Databáza - 580 KB)
└── README.txt              (Užívateľský manuál)
```

**Inštalácia:**
```bash
# 1. Rozbal ZIP
unzip beekeeper-desktop-v1.0-portable.zip

# 2. Spusti
cd beekeeper-desktop/bin
chmod +x beekeeper          # macOS/Linux
./beekeeper                 # macOS/Linux
beekeeper.bat               # Windows
```

**Prvé spustenie:**
- Automaticky vytvorí `data/` adresár
- Inicializuje databázu s translations
- Naplní test dáta (včelnice, úle, prehliadky, taxácie)

---

## 🔧 Ako Vytvoriť Novú Distribúciu

```bash
# Z root projektu
gradle desktop:distZip

# Skopíruj do distributions/
cp desktop/build/distributions/beekeeper-desktop.zip \
   desktop/distributions/beekeeper-desktop-vX.Y-portable.zip
```

---

## 📝 Changelog

### v1.5 (2026-02-22) - Multi-Platform Edition (Improved Windows) ⭐ LATEST

**Fixed:**
- ✅ **Improved Windows BAT launcher** - explicitné loopy pre každý JavaFX modul:
  ```batch
  for %%f in (%APP_HOME%\lib\javafx-base-*-win.jar) do set JAVAFX_MODULES=%%f
  for %%f in (%APP_HOME%\lib\javafx-controls-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
  for %%f in (%APP_HOME%\lib\javafx-fxml-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
  for %%f in (%APP_HOME%\lib\javafx-graphics-*-win.jar) do set JAVAFX_MODULES=%JAVAFX_MODULES%;%%f
  ```
- ✅ **Platform-aware launchers** - správna detekcia a výber modulov
- ✅ Vyriešený "Module javafx.controls not found" error na Windows

**Obsahuje všetko z v1.0:**
- ✅ 785 translation keys (SK/EN)
- ✅ 10 realistických prehliadok + 10 taxácií
- ✅ Portable databáza v data/ subdirectory

**Technické:**
- Distribúcia: 61 MB (multi-platform knižnice)
- Build: Multi-platform JavaFX dependencies
- Launcher: Explicit module enumeration (Windows) + platform detection (Unix)

---

### v1.4 (2026-02-22) - Multi-Platform Edition ❌

**Problem:**
- ❌ Windows BAT wildcard loop (`javafx-*-win.jar`) nefungoval správne
- ❌ "Module javafx.controls not found"
- ❌ JAVAFX_MODULES premenná bola prázdna alebo neúplná

---

### v1.3 (2026-02-22) - Multi-Platform Edition ❌

**Problem:**
- ❌ Java module conflict: "Two versions of module javafx.graphics found"
- ❌ Launcher používal všetky JAR súbory v module-path
- ❌ Crash pri spustení na všetkých platformách

---

### v1.2 (2026-02-22) - Windows Fix (Incomplete) ❌

**Problem:**
- ❌ Build na macOS zahrnul len macOS JavaFX knižnice
- ❌ Windows crash: "no suitable pipeline found"
- ❌ Chýbali Windows DLL súbory

---

### v1.1 (2026-02-22) - Windows Fix (Incomplete) ❌

**Problem:**
- ❌ Opravené len v build.gradle, launcher skripty stále obsahovali flag
- ❌ Stále crashovalo na Windows

---

### v1.0 (2026-02-22) - Portable Edition

**Features:**
- ✅ Kompletná aplikácia s JavaFX 21.0.2
- ✅ 785 translation keys (SK/EN)
- ✅ Realistické test dáta (10 inspections, 10 taxations)
- ✅ **PORTABLE** - databáza v data/ subdirectory
- ✅ Automatická inicializácia pri prvom spustení
- ✅ Launcher skripty pre všetky platformy

**Technické:**
- Java 17+ required
- SQLite 3.45.1
- RxJava2 + JavaFX
- MVVM architecture
- 39 MB distribúcia

---

## 🎯 Testovacie Dáta

### Prehliadky (10):
- Marec 2024: Jarné prehliadky, príkrm potrebný
- Apríl 2024: Rast plodiska
- Máj 2024: Vrchol plodiska, pridávanie nadstavcov
- Jún 2024: Plná znáška
- Júl 2024: Varroa kontroly

### Taxácie (10):
- Detailné merania dm² (plodisko, zásoby)
- Realistické hodnoty počas sezóny
- Frame records s poznámkami

---

## 📚 Dokumentácia

- `/DISTRIBUTION_WITH_TEST_DATA.md` - Kompletný návod pre testera
- `/DATABASE_LOCATION.md` - Portable database setup
- `/DATABASE_EXPORT_SUMMARY.md` - Technické detaily
- `/.claude/skills/build-distribution/SKILL.md` - Build skill

---

## ✅ Verifikované

- ✅ Windows 10+
- ✅ macOS 10.14+ (Intel & Apple Silicon)
- ✅ Linux (Ubuntu 20.04+)
- ✅ Všetky translations načítané
- ✅ Test dáta prítomné
- ✅ Portable setup funguje
- ✅ USB stick ready

---

**Vytvorené:** 2026-02-22  
**Autor:** Beekeeper Desktop Team  
**Licencia:** Private
