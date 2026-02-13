# Včelárska Aplikácia (Beekeeper App)

Modern Android application for beekeeping management, replacing Excel-based hive journals with advanced features including AI-powered audio/video inspection recording.

## 🎯 Project Overview

This application is designed to digitize and modernize beekeeping record-keeping, providing:
- **Digital hive management** replacing 5-year Excel spreadsheets
- **AI-powered inspection recording** using OpenAI Whisper & GPT-4
- **Comprehensive data tracking** for inspections, feeding, and detailed hive taxation
- **Phenological calendar** and task planning
- **Varroa calculator** and queen rearing timeline calculator
- **Analytics and visualization** of hive data over time
- **Excel import** for historical data (2020-2025)

## 🏗️ Architecture

### Technology Stack
- **Language:** Java (100% - no Kotlin)
- **Architecture:** MVVM + Repository Pattern
- **Database:** Room + SQLite (UUID-based IDs for future cloud sync)
- **Async:** RxJava2
- **Network:** Retrofit + OkHttp
- **AI Integration:** OpenAI API (Whisper + GPT-4)
- **Excel Processing:** Apache POI
- **Charts:** MPAndroidChart
- **UI:** Traditional Android (XML layouts + ViewBinding)

### Package Structure
```
com.beekeeper.app/
├── data/
│   ├── local/           # Room database, DAOs, entities
│   ├── remote/          # OpenAI API integration
│   └── repository/      # Repository layer
├── domain/
│   ├── model/           # Domain models
│   └── usecase/         # Business logic
├── presentation/        # UI layer (Activities, Fragments, ViewModels)
├── service/             # Background services
└── util/                # Utilities and constants
```

## 📊 Database Schema

### Core Entities
1. **Apiary** - Včelnica (location with multiple hives)
2. **Hive** - Úľ (individual beehive)
3. **Inspection** - Prehliadka (hive inspection records)
4. **InspectionRecording** - Audio/video recordings with AI transcription
5. **Feeding** - Krmenie (feeding records with weights)
6. **Taxation** - Taxácia (detailed frame-by-frame assessment)
7. **TaxationFrame** - Individual frame details (1-25 frames per hive)
8. **CalendarEvent** - Úkony a pripomienky (tasks and reminders)
9. **Settings** - Application settings (API keys, preferences)

## ✨ Key Features

### Phase 1: Foundation (IMPLEMENTED ✅)
- ✅ Complete project structure
- ✅ Room database with all entities and DAOs
- ✅ Repository layer
- ✅ Base classes (Activity, Fragment, ViewModel)
- ✅ Navigation framework with drawer
- ✅ Material Design theming
- ✅ Placeholder fragments for all sections

### Phase 2: Core CRUD (IN PROGRESS 🔄)
- [ ] Apiary management (create, read, update, delete)
- [ ] Hive management with full CRUD operations
- [ ] Manual inspection entry forms
- [ ] Feeding records with weight tracking
- [ ] Detailed taxation with frame-by-frame input

### Phase 3: AI Integration (PLANNED 📋)
- [ ] Audio recording service
- [ ] OpenAI Whisper integration (speech-to-text)
- [ ] GPT-4 data extraction from transcriptions
- [ ] Review & edit extracted data UI
- [ ] Recording file management

### Phase 4: Calendar & Calculators (PLANNED 📋)
- [ ] Phenological calendar
- [ ] Task planning and reminders
- [ ] Varroa growth calculator
- [ ] Queen rearing timeline calculator

### Phase 5: Excel Import (PLANNED 📋)
- [ ] Apache POI integration
- [ ] Historical data parser (2020-2025)
- [ ] Mapping Excel → Database entities
- [ ] Import progress UI

### Phase 6: Analytics (PLANNED 📋)
- [ ] MPAndroidChart graphs
- [ ] Hive strength trends
- [ ] Weight tracking over time
- [ ] Comparison between hives
- [ ] CSV/Excel export

### Phase 7: Cloud Sync (FUTURE 🚀)
- [ ] Backend API setup
- [ ] Push/pull synchronization
- [ ] Conflict resolution
- [ ] Multi-device support

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or newer
- JDK 8 or higher
- Android SDK 26 (Android 8.0) or higher
- OpenAI API key (for AI features)

### Build Instructions
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle dependencies
4. Run on emulator or physical device

### Configuration
1. Navigate to Settings in the app
2. Enter your OpenAI API key
3. Create your first apiary
4. Start recording hive data

## 📱 Supported Features by Data Source

### From Excel Import
The app can import historical data from `Úľový denník 2025.xlsx`:
- **Kalendár sheets** → Calendar events and phenological data
- **Krmenie sheets** → Feeding records with weights
- **Notes sheets** → Inspection records
- **Taxácia sheets** → Detailed frame assessments
- Historical years: 2020, 2021, 2022, 2023, 2024, 2025

### Hive Types Supported
- **Vertikálne úle** (U1-U8) - Vertical hives
- **Ležaté úle** (L1-L4, Lezan 1-4) - Horizontal hives
- **Oddielky** (O1-O8) - Nucleus boxes
- **Custom names** (Johanka, Risko, etc.)

## 🔐 Security & Privacy
- All data stored locally by default
- OpenAI API key stored securely in SharedPreferences
- No cloud sync without explicit user consent
- Recording files can be manually deleted
- Full data export capability

## 📋 Current Implementation Status

### ✅ Completed
- Project initialization with Gradle configuration
- Complete database schema (9 entities, 9 DAOs)
- Repository pattern implementation
- Base MVVM architecture classes
- Navigation component with drawer menu
- Material Design theme (bee-themed: yellow/amber colors)
- All placeholder UI fragments
- PreferencesManager for settings
- Constants and utility classes
- AndroidManifest with all permissions

### 🔄 In Development
- CRUD operations for apiaries and hives
- Manual inspection forms
- Feeding and taxation entry screens

### 📋 Pending
- OpenAI API integration
- Audio/video recording service
- Excel import functionality
- Calculator implementations
- Analytics and charts
- Cloud synchronization

## 🧪 Testing Strategy
- Unit tests for repositories and use cases
- Integration tests for database operations
- UI tests with Espresso for critical flows
- Manual testing with real beekeeping data

## 📖 Documentation
For detailed implementation plan and architecture decisions, see the original plan document included in the project.

## 🤝 Contributing
This is a personal beekeeping management project. Feel free to fork and adapt for your own needs.

## 📄 License
Private project - All rights reserved

## 🐝 About
Created to modernize beekeeping record-keeping, bringing the power of mobile technology and AI to traditional apiary management.

---
**Version:** 1.0.0 (Phase 1 Complete)
**Last Updated:** February 2025
**Built with:** ❤️ for beekeepers by beekeepers
