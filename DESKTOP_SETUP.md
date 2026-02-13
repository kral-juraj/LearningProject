# Beekeeper Desktop - Setup Guide

Complete setup and usage guide for the Beekeeper Desktop application (JavaFX version).

## 📋 Table of Contents
- [System Requirements](#system-requirements)
- [Installation](#installation)
- [First Launch](#first-launch)
- [Using the Application](#using-the-application)
- [Database Location](#database-location)
- [Troubleshooting](#troubleshooting)
- [Development Setup](#development-setup)

---

## 🖥️ System Requirements

### Minimum Requirements
- **Operating System:** Windows 7+, macOS 10.12+, or Linux (Ubuntu 18.04+)
- **Java:** JDK 8 or higher (JDK 11+ recommended)
- **RAM:** 512 MB minimum, 1 GB recommended
- **Disk Space:** 100 MB for application + space for database

### Recommended
- **Java:** JDK 17 LTS
- **RAM:** 2 GB
- **Display:** 1280x800 or higher resolution

---

## 📥 Installation

### Option 1: Run from Source (Development)

#### Prerequisites
1. Install JDK 8+:
   ```bash
   # macOS (using Homebrew)
   brew install openjdk@17

   # Ubuntu/Debian
   sudo apt install openjdk-17-jdk

   # Windows
   # Download from: https://adoptium.net/
   ```

2. Install Gradle (if not using wrapper):
   ```bash
   # macOS
   brew install gradle

   # Ubuntu/Debian
   sudo apt install gradle
   ```

#### Build and Run
```bash
# Clone the repository (if not done)
cd LearningProject

# Using the provided script (easiest)
./run-desktop.sh

# Or manually with Gradle
gradle desktop:run
```

### Option 2: Standalone Distribution

#### Build Distribution
```bash
# Build standalone distribution
gradle desktop:build

# Navigate to distribution
cd desktop/build/distributions

# Extract the archive
unzip desktop.zip
# or
tar -xvf desktop.tar
```

#### Run Standalone
```bash
# On macOS/Linux
cd desktop/bin
./desktop

# On Windows
cd desktop\bin
desktop.bat
```

### Option 3: Create Native Launcher (macOS/Linux)

Create a launcher script in `/usr/local/bin`:

```bash
#!/bin/bash
# Save as: /usr/local/bin/beekeeper

cd /path/to/LearningProject
gradle desktop:run
```

Make executable:
```bash
chmod +x /usr/local/bin/beekeeper
```

Now run from anywhere:
```bash
beekeeper
```

---

## 🚀 First Launch

### Initial Startup

1. **Launch the application** using one of the methods above

2. **Database Creation**
   - On first launch, the application automatically creates a SQLite database
   - Default location: `~/beekeeper-desktop.db`
   - You'll see a console message: `Initializing database at: /Users/yourname/beekeeper-desktop.db`

3. **Welcome Screen**
   - The main window appears with three tabs:
     - **Včelnice** (Apiaries)
     - **Úle** (Hives) - disabled until apiary selected
     - **Prehliadky** (Inspections) - disabled until hive selected

### Creating Your First Apiary

1. Click **"Pridať včelnicu"** (Add Apiary) button
2. Enter apiary name (e.g., "Domáca", "Záhrada")
3. Click OK
4. Your apiary appears in the table

### Creating Your First Hive

1. Select an apiary from the list
2. Switch to **"Úle"** (Hives) tab
3. Click **"Pridať úľ"** (Add Hive) button
4. Enter hive name (e.g., "U1", "Úľ 1")
5. Click OK
6. Your hive appears in the table

---

## 💻 Using the Application

### Main Window Layout

```
┌─────────────────────────────────────────┐
│ Súbor  Nápoveda                         │  Menu Bar
├─────────────────────────────────────────┤
│ [Včelnice] [Úle] [Prehliadky]           │  Tabs
├─────────────────────────────────────────┤
│                                          │
│  Current Tab Content                    │
│  (Table + Toolbar)                      │
│                                          │
├─────────────────────────────────────────┤
│ Status: 3 včelníc                       │  Status Bar
└─────────────────────────────────────────┘
```

### Včelnice (Apiaries) Tab

#### Toolbar Buttons
- **Pridať včelnicu** - Create new apiary
- **Upraviť** - Edit selected apiary
- **Zmazať** - Delete selected apiary (⚠️ deletes all hives!)
- **Obnoviť** - Refresh the list

#### Table Columns
- **Názov** - Apiary name
- **Lokalita** - Location (optional)

#### Operations
- **Create:** Click "Pridať včelnicu", enter name
- **Edit:** Select apiary, click "Upraviť", change name
- **Delete:** Select apiary, click "Zmazať", confirm
  - ⚠️ **Warning:** This CASCADE deletes all hives and their inspections!
- **Refresh:** Click "Obnoviť" to reload from database

### Úle (Hives) Tab

#### Toolbar Buttons
- **Pridať úľ** - Create new hive
- **Upraviť** - Edit selected hive
- **Zmazať** - Delete selected hive (⚠️ deletes all inspections!)
- **Zmeniť stav** - Toggle active/inactive status
- **Obnoviť** - Refresh the list

#### Table Columns
- **Názov** - Hive name
- **Typ** - Hive type (VERTICAL/HORIZONTAL/NUKE)
- **Aktívny** - Active status (true/false)

#### Operations
- **Create:** Click "Pridať úľ", enter name
- **Edit:** Select hive, click "Upraviť", change name
- **Toggle Active:** Select hive, click "Zmeniť stav"
- **Delete:** Select hive, click "Zmazať", confirm
  - ⚠️ **Warning:** This CASCADE deletes all inspections!
- **Refresh:** Click "Obnoviť" to reload from database

### Prehliadky (Inspections) Tab

#### Toolbar Buttons
- **Nová prehliadka** - Create new inspection (planned feature)
- **Zobraziť** - View inspection details
- **Zmazať** - Delete selected inspection
- **Obnoviť** - Refresh the list

#### Table Columns
- **Dátum** - Inspection date (formatted: dd.MM.yyyy)
- **Teplota (°C)** - Temperature
- **Sila (1-10)** - Hive strength estimate

#### Operations
- **View:** Select inspection, click "Zobraziť" to see details
- **Delete:** Select inspection, click "Zmazať", confirm
- **Refresh:** Click "Obnoviť" to reload from database

### Keyboard Shortcuts

Currently, all operations use mouse/buttons. Keyboard shortcuts will be added in future versions.

---

## 🗄️ Database Location

### Default Location
```
~/beekeeper-desktop.db
```

### Platform-Specific Paths
- **macOS:** `/Users/yourname/beekeeper-desktop.db`
- **Linux:** `/home/yourname/beekeeper-desktop.db`
- **Windows:** `C:\Users\yourname\beekeeper-desktop.db`

### Changing Database Location

To use a custom database location, edit `Main.java`:

```java
// Change this line in Main.java
String dbPath = userHome + "/beekeeper-desktop.db";

// To:
String dbPath = "/custom/path/to/database.db";
```

Then rebuild:
```bash
gradle desktop:build
```

### Database Backup

```bash
# Simple copy
cp ~/beekeeper-desktop.db ~/beekeeper-backup-$(date +%Y%m%d).db

# Or use SQLite backup
sqlite3 ~/beekeeper-desktop.db ".backup ~/beekeeper-backup.db"
```

### Database Schema

The database contains 9 tables:
1. `apiaries` - Včelnice
2. `hives` - Úle
3. `inspections` - Prehliadky
4. `feedings` - Krmenie
5. `taxations` - Taxácie
6. `taxation_frames` - Rámiky taxácie
7. `calendar_events` - Kalendár udalostí
8. `settings` - Nastavenia
9. `inspection_recordings` - Audio nahrávky

**Foreign Keys:** CASCADE DELETE enabled for referential integrity.

---

## 🔧 Troubleshooting

### Application Won't Start

#### Error: "Could not find or load main class"
```bash
# Rebuild the project
gradle clean desktop:build

# Try running again
gradle desktop:run
```

#### Error: "JavaFX runtime components are missing"
```bash
# Ensure JavaFX is in dependencies
gradle desktop:dependencies | grep javafx

# If missing, check desktop/build.gradle
```

#### Error: "SDK location not found"
This is normal if Android module fails. Desktop module should still work.

### Database Issues

#### Error: "Database not initialized"
```bash
# Check database file exists
ls -la ~/beekeeper-desktop.db

# Check permissions
chmod 644 ~/beekeeper-desktop.db
```

#### Error: "Table does not exist"
The database schema might be corrupted. Options:
1. Delete database file (lose all data):
   ```bash
   rm ~/beekeeper-desktop.db
   ```
2. Restore from backup:
   ```bash
   cp ~/beekeeper-backup.db ~/beekeeper-desktop.db
   ```

#### Data Not Appearing
1. Click "Obnoviť" (Refresh) button
2. Restart application
3. Check database manually:
   ```bash
   sqlite3 ~/beekeeper-desktop.db
   sqlite> SELECT * FROM apiaries;
   sqlite> .quit
   ```

### Performance Issues

#### Slow Database Queries
```bash
# Check database size
ls -lh ~/beekeeper-desktop.db

# Vacuum database to optimize
sqlite3 ~/beekeeper-desktop.db "VACUUM;"
```

#### High Memory Usage
- Close and restart application
- Reduce number of open tabs
- Check for RxJava subscription leaks (disposables not cleared)

### UI Issues

#### Window Too Small
- Manually resize window (minimum 800x600)
- Or edit Main.java to set larger default size

#### Text Not Displaying
- Check system locale settings
- Verify UTF-8 encoding support

---

## 🛠️ Development Setup

### IDE Setup (IntelliJ IDEA)

1. **Open Project**
   ```
   File → Open → Select LearningProject folder
   ```

2. **Import Gradle Project**
   - IntelliJ should auto-detect Gradle
   - Wait for Gradle sync to complete

3. **Configure Run Configuration**
   ```
   Run → Edit Configurations → + → Application

   Name: Desktop App
   Main class: com.beekeeper.desktop.Main
   Module: desktop.main
   Working directory: $PROJECT_DIR$/desktop
   ```

4. **Run from IDE**
   - Click green Run button
   - Or: Right-click Main.java → Run 'Main.main()'

### Debugging

#### Enable Debug Logging
Add to Main.java:
```java
System.setProperty("javafx.verbose", "true");
System.setProperty("prism.verbose", "true");
```

#### Database Debug
```java
// In DatabaseManager.java
System.out.println("Executing SQL: " + sql);
```

#### RxJava Debug
```java
// Add error handlers
.subscribe(
    data -> { /* success */ },
    error -> {
        error.printStackTrace();
        System.err.println("RxJava error: " + error.getMessage());
    }
);
```

### Building Release Version

```bash
# Clean build
gradle clean

# Build with optimizations
gradle desktop:build -Dorg.gradle.java.home=/path/to/jdk17

# Create distribution
cd desktop/build/distributions
unzip desktop.zip

# Test distribution
./desktop/bin/desktop
```

### Hot Reload (Development)

For faster iteration during development:
```bash
# Terminal 1: Auto-rebuild on changes
gradle desktop:build --continuous

# Terminal 2: Run application
gradle desktop:run
```

---

## 📚 Additional Resources

### Documentation
- [README.md](README.md) - Project overview
- [CONVERSION_GUIDE.md](CONVERSION_GUIDE.md) - Cross-platform development
- [JavaFX Documentation](https://openjfx.io/javadoc/17/)
- [RxJava Documentation](http://reactivex.io/documentation)

### Support
For issues or questions:
1. Check this guide first
2. Review project documentation
3. Check database integrity
4. Rebuild from clean state

---

**Last Updated:** February 2025
**Version:** 2.0.0 (Multi-Platform Edition)
