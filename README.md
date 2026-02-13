# Včelárska Aplikácia (Beekeeper App)

Modern **multi-platform** beekeeping management application available on **Android** and **Desktop (JavaFX)**. Replaces Excel-based hive journals with advanced features including AI-powered audio/video inspection recording.

## 🎯 Project Overview

This application digitizes and modernizes beekeeping record-keeping, providing:
- **Multi-platform support** - Android mobile app & Desktop (Windows/Mac/Linux)
- **Digital hive management** replacing 5-year Excel spreadsheets
- **AI-powered inspection recording** using OpenAI Whisper & GPT-4
- **Comprehensive data tracking** for inspections, feeding, and detailed hive taxation
- **Phenological calendar** and task planning
- **Varroa calculator** and queen rearing timeline calculator
- **Analytics and visualization** of hive data over time
- **Excel import/export** for historical data (2020-2025)

## 🏗️ Multi-Module Architecture

```
BeekeeperApp/
├── shared/              # Platform-agnostic business logic (55% code reuse)
│   ├── entity/          # 9 POJOs (no platform dependencies)
│   ├── dao/             # DAO interfaces (RxJava2)
│   ├── repository/      # Business logic repositories
│   ├── viewmodel/       # Shared ViewModels (BehaviorRelay)
│   ├── scheduler/       # Platform-agnostic scheduler abstraction
│   └── util/            # Shared utilities
│
├── desktop/             # Desktop application (JavaFX + JDBC)
│   ├── controller/      # JavaFX controllers
│   ├── view/            # FXML layouts
│   ├── dao/jdbc/        # JDBC/SQLite implementations
│   ├── db/              # Database manager
│   └── Main.java        # Desktop entry point
│
└── app/                 # Android application
    ├── data/            # Room database (Android-specific)
    ├── presentation/    # Android UI (Activities, Fragments)
    └── service/         # Background services
```

### Technology Stack

#### Shared
- **Language:** Java 8
- **Architecture:** MVVM + Repository Pattern
- **Async:** RxJava2 + RxRelay
- **Database Interface:** DAO pattern with reactive streams
- **Code Reuse:** ~55% shared between platforms

#### Android
- **Database:** Room + SQLite
- **UI:** XML layouts + ViewBinding
- **Schedulers:** AndroidSchedulers.mainThread()
- **Network:** Retrofit + OkHttp
- **AI Integration:** OpenAI API (Whisper + GPT-4)
- **Charts:** MPAndroidChart

#### Desktop
- **Database:** JDBC + SQLite
- **UI:** JavaFX + FXML
- **Schedulers:** JavaFxScheduler.platform()
- **Excel:** Apache POI
- **Packaging:** Gradle distributions

## 📊 Database Schema

### Core Entities (9 total - 100% shared)
1. **Apiary** - Včelnica (location with multiple hives)
2. **Hive** - Úľ (individual beehive)
3. **Inspection** - Prehliadka (hive inspection records)
4. **InspectionRecording** - Audio/video recordings with AI transcription
5. **Feeding** - Krmenie (feeding records with weights)
6. **Taxation** - Taxácia (detailed frame-by-frame assessment)
7. **TaxationFrame** - Individual frame details (1-25 frames per hive)
8. **CalendarEvent** - Úkony a pripomienky (tasks and reminders)
9. **Settings** - Application settings (API keys, preferences)

### Database Implementation
- **Android:** Room with @Entity annotations
- **Desktop:** JDBC with SQL DDL
- **Schema:** Identical on both platforms
- **Foreign Keys:** CASCADE DELETE for referential integrity
- **Indexes:** Optimized for common queries

## 🚀 Getting Started

### Desktop Application

#### Prerequisites
- JDK 8 or higher
- Gradle 6.8+

#### Build & Run
```bash
# Using the provided script
./run-desktop.sh

# Or manually
gradle desktop:run

# Or build distributable
gradle desktop:build
cd desktop/build/distributions
unzip desktop.zip
./desktop/bin/desktop
```

#### First Launch
1. Application starts automatically
2. Database created at: `~/beekeeper-desktop.db`
3. Create your first apiary in the "Včelnice" tab
4. Add hives to the apiary
5. Start recording inspections

**See [DESKTOP_SETUP.md](DESKTOP_SETUP.md) for detailed instructions.**

### Android Application

#### Prerequisites
- Android Studio Arctic Fox or newer
- JDK 8 or higher
- Android SDK 26 (Android 8.0) or higher
- OpenAI API key (for AI features)

#### Build Instructions
1. Open project in Android Studio
2. Sync Gradle dependencies
3. Run on emulator or physical device

## ✨ Features

### Desktop Application (IMPLEMENTED ✅)
- ✅ **Apiary Management** - Full CRUD operations
- ✅ **Hive Management** - Create, edit, delete, activate/deactivate
- ✅ **Inspection List** - View all inspections by hive
- ✅ **SQLite Database** - Local persistent storage
- ✅ **Reactive UI** - RxJava2 + JavaFX bindings
- ✅ **Slovak Localization** - Complete UI in Slovak
- ✅ **Tabbed Interface** - Easy navigation between features

### Android Application (Phase 2 Complete ✅)
- ✅ Complete project structure
- ✅ Room database with all entities and DAOs
- ✅ Repository layer
- ✅ MVVM architecture
- ✅ Navigation framework
- ✅ Material Design theming
- ✅ Base CRUD operations

### Planned Features (Future)
- 🔄 **AI Integration** - Whisper + GPT-4 for voice-to-data
- 🔄 **Excel Import** - Historical data from Excel files
- 🔄 **Analytics** - Charts and trends
- 🔄 **Calculators** - Varroa & queen rearing
- 🔄 **Calendar** - Phenological events & reminders

## 🔐 Security & Privacy
- All data stored locally by default
- No cloud sync without explicit user consent
- Desktop database: `~/beekeeper-desktop.db`
- Android database: Internal app storage
- OpenAI API key stored securely
- Recording files can be manually deleted
- Full data export capability

## 📁 Project Structure

```
LearningProject/
├── shared/                    # Shared business logic (33 files)
│   ├── src/main/java/
│   │   └── com.beekeeper.shared/
│   │       ├── entity/        # 9 POJOs
│   │       ├── dao/           # 9 DAO interfaces
│   │       ├── repository/    # 5 repositories
│   │       ├── viewmodel/     # 6 ViewModels
│   │       ├── scheduler/     # Scheduler abstraction
│   │       └── util/          # 2 utilities
│   └── build.gradle           # Pure Java library
│
├── desktop/                   # Desktop application (21 files)
│   ├── src/main/java/
│   │   └── com.beekeeper.desktop/
│   │       ├── Main.java      # Entry point
│   │       ├── controller/    # 4 JavaFX controllers
│   │       ├── dao/jdbc/      # 9 JDBC DAOs
│   │       ├── db/            # DatabaseManager
│   │       └── scheduler/     # Desktop scheduler impl
│   ├── src/main/resources/
│   │   └── view/              # 4 FXML files
│   └── build.gradle           # JavaFX + JDBC
│
├── app/                       # Android application
│   ├── src/main/java/
│   │   └── com.beekeeper.app/
│   │       ├── data/          # Room DAOs (Android-specific)
│   │       ├── presentation/  # Activities, Fragments
│   │       └── service/       # Background services
│   └── build.gradle           # Android library
│
├── run-desktop.sh             # Desktop launch script
├── README.md                  # This file
├── DESKTOP_SETUP.md           # Desktop setup guide
└── CONVERSION_GUIDE.md        # Platform conversion guide
```

## 🧪 Testing

### Desktop
- Unit tests: `gradle shared:test desktop:test`
- Integration tests: In-memory SQLite
- Manual testing with real data

### Android
- Unit tests: JUnit + Mockito
- Integration tests: Room database tests
- UI tests: Espresso

## 📖 Documentation

- **[DESKTOP_SETUP.md](DESKTOP_SETUP.md)** - Desktop installation and usage
- **[CONVERSION_GUIDE.md](CONVERSION_GUIDE.md)** - Cross-platform development guide
- **[QUICK_START.md](QUICK_START.md)** - Quick start guide
- **[PROJECT_CONTEXT.md](PROJECT_CONTEXT.md)** - Project context and decisions

## 🔄 Multi-Platform Development

### Code Reuse Strategy
- **Shared (55%):** Entities, DAOs, Repositories, ViewModels, Utils
- **Android (20%):** Room implementations, Android UI
- **Desktop (25%):** JDBC implementations, JavaFX UI

### Cross-Platform Workflow
1. Develop business logic in `shared/`
2. Implement platform-specific DAOs
3. Create UI with platform frameworks
4. Test on both platforms
5. Deploy separately

See [CONVERSION_GUIDE.md](CONVERSION_GUIDE.md) for detailed workflows.

## 📊 Current Implementation Status

### ✅ Completed (90%)
- Multi-module Gradle structure
- Shared business logic (entities, DAOs, repositories, ViewModels)
- Desktop application with JavaFX UI
- JDBC/SQLite database layer
- Basic CRUD operations for apiaries and hives
- Reactive UI with RxJava2
- Scheduler abstraction for cross-platform threading

### 🔄 In Development (10%)
- Android UI refactoring to use shared ViewModels
- Comprehensive test suite
- Advanced features (taxation, feeding, analytics)

### 📋 Planned
- AI integration (Whisper + GPT-4)
- Excel import/export
- Calendar and calculators
- Cloud synchronization

## 🤝 Contributing
This is a personal beekeeping management project. Feel free to fork and adapt for your own needs.

## 📄 License
Private project - All rights reserved

## 🐝 About
Created to modernize beekeeping record-keeping, bringing the power of multi-platform technology and AI to traditional apiary management.

---
**Version:** 2.0.0 (Multi-Platform Edition)
**Platforms:** Android + Desktop (Windows/Mac/Linux)
**Last Updated:** February 2025
**Built with:** ❤️ for beekeepers by beekeepers
