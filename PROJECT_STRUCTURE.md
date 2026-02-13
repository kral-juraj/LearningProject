# Project Structure Overview

## 📦 BeekeeperApp Project

```
LearningProject/
│
├── 📄 build.gradle                 # Root build configuration
├── 📄 settings.gradle              # Project settings
├── 📄 gradle.properties            # Gradle properties
├── 📄 README.md                    # Project documentation
├── 📄 IMPLEMENTATION_LOG.md        # Implementation details
├── 📄 PROJECT_STRUCTURE.md         # This file
├── 📄 Úľový denník 2025.xlsx      # Original Excel file for import
│
└── app/
    ├── 📄 build.gradle             # App module configuration
    ├── 📄 proguard-rules.pro       # ProGuard rules
    │
    └── src/
        ├── main/
        │   ├── 📄 AndroidManifest.xml
        │   │
        │   ├── java/com/beekeeper/app/
        │   │   │
        │   │   ├── 📄 BeekeeperApplication.java
        │   │   │
        │   │   ├── data/
        │   │   │   ├── local/
        │   │   │   │   ├── db/
        │   │   │   │   │   └── 📄 AppDatabase.java
        │   │   │   │   ├── dao/
        │   │   │   │   │   ├── 📄 ApiaryDao.java
        │   │   │   │   │   ├── 📄 HiveDao.java
        │   │   │   │   │   ├── 📄 InspectionDao.java
        │   │   │   │   │   ├── 📄 InspectionRecordingDao.java
        │   │   │   │   │   ├── 📄 FeedingDao.java
        │   │   │   │   │   ├── 📄 TaxationDao.java
        │   │   │   │   │   ├── 📄 TaxationFrameDao.java
        │   │   │   │   │   ├── 📄 CalendarEventDao.java
        │   │   │   │   │   └── 📄 SettingsDao.java
        │   │   │   │   ├── entity/
        │   │   │   │   │   ├── 📄 Apiary.java
        │   │   │   │   │   ├── 📄 Hive.java
        │   │   │   │   │   ├── 📄 Inspection.java
        │   │   │   │   │   ├── 📄 InspectionRecording.java
        │   │   │   │   │   ├── 📄 Feeding.java
        │   │   │   │   │   ├── 📄 Taxation.java
        │   │   │   │   │   ├── 📄 TaxationFrame.java
        │   │   │   │   │   ├── 📄 CalendarEvent.java
        │   │   │   │   │   └── 📄 Settings.java
        │   │   │   │   └── prefs/
        │   │   │   │       └── 📄 PreferencesManager.java
        │   │   │   ├── remote/ (To be implemented)
        │   │   │   │   ├── api/
        │   │   │   │   │   └── 📄 OpenAiApi.java (TODO)
        │   │   │   │   └── 📄 OpenAiManager.java (TODO)
        │   │   │   └── repository/
        │   │   │       ├── 📄 ApiaryRepository.java
        │   │   │       ├── 📄 HiveRepository.java
        │   │   │       └── 📄 InspectionRepository.java
        │   │   │
        │   │   ├── domain/ (To be expanded)
        │   │   │   ├── model/
        │   │   │   │   └── 📄 ExtractedInspectionData.java (TODO)
        │   │   │   └── usecase/
        │   │   │       └── 📄 ProcessAudioInspectionUseCase.java (TODO)
        │   │   │
        │   │   ├── presentation/
        │   │   │   ├── base/
        │   │   │   │   ├── 📄 BaseActivity.java
        │   │   │   │   ├── 📄 BaseFragment.java
        │   │   │   │   └── 📄 BaseViewModel.java
        │   │   │   ├── main/
        │   │   │   │   └── 📄 MainActivity.java
        │   │   │   ├── dashboard/
        │   │   │   │   └── 📄 DashboardFragment.java
        │   │   │   ├── apiary/
        │   │   │   │   └── 📄 ApiaryListFragment.java
        │   │   │   ├── calendar/
        │   │   │   │   └── 📄 CalendarFragment.java
        │   │   │   ├── calculator/
        │   │   │   │   └── 📄 CalculatorFragment.java
        │   │   │   ├── analytics/
        │   │   │   │   └── 📄 AnalyticsFragment.java
        │   │   │   └── settings/
        │   │   │       └── 📄 SettingsFragment.java
        │   │   │
        │   │   ├── service/ (To be implemented)
        │   │   │   ├── 📄 AudioRecordingService.java (TODO)
        │   │   │   └── 📄 ExcelImportService.java (TODO)
        │   │   │
        │   │   └── util/
        │   │       ├── 📄 Constants.java
        │   │       ├── 📄 DateUtils.java
        │   │       ├── 📄 FileUtils.java (TODO)
        │   │       └── 📄 PermissionUtils.java (TODO)
        │   │
        │   └── res/
        │       ├── layout/
        │       │   ├── 📄 activity_main.xml
        │       │   ├── 📄 nav_header.xml
        │       │   ├── 📄 fragment_dashboard.xml
        │       │   ├── 📄 fragment_apiary_list.xml
        │       │   ├── 📄 fragment_calendar.xml
        │       │   ├── 📄 fragment_calculator.xml
        │       │   ├── 📄 fragment_analytics.xml
        │       │   └── 📄 fragment_settings.xml
        │       ├── menu/
        │       │   └── 📄 drawer_menu.xml
        │       ├── navigation/
        │       │   └── 📄 nav_graph.xml
        │       ├── values/
        │       │   ├── 📄 strings.xml
        │       │   ├── 📄 colors.xml
        │       │   └── 📄 themes.xml
        │       ├── xml/
        │       │   ├── 📄 file_paths.xml
        │       │   ├── 📄 backup_rules.xml
        │       │   └── 📄 data_extraction_rules.xml
        │       └── mipmap-*/
        │           └── ic_launcher.png
        │
        └── test/
            └── (Unit tests - TODO)
```

## 🎯 Key Components

### Data Layer
- **AppDatabase:** Room database singleton
- **9 Entities:** Apiary, Hive, Inspection, InspectionRecording, Feeding, Taxation, TaxationFrame, CalendarEvent, Settings
- **9 DAOs:** One for each entity with RxJava2 queries
- **3 Repositories:** Apiary, Hive, Inspection (more to be added)
- **PreferencesManager:** SharedPreferences wrapper

### Presentation Layer
- **MainActivity:** Navigation drawer host
- **6 Fragments:** Dashboard, Apiaries, Calendar, Calculators, Analytics, Settings
- **Base Classes:** BaseActivity, BaseFragment, BaseViewModel

### Utilities
- **Constants:** Application-wide constants
- **DateUtils:** Date formatting and manipulation

## 📊 Statistics

| Category | Count |
|----------|-------|
| Java Files | 37 |
| XML Files | 16 |
| Packages | 15 |
| Database Tables | 9 |
| DAO Methods | ~90 |
| Fragments | 6 |
| Activities | 1 |

## 🔄 Implementation Status

### ✅ Completed (Phase 1)
- Project structure and build configuration
- Complete database layer with entities and DAOs
- Repository pattern foundation
- Base MVVM architecture classes
- Navigation framework
- All placeholder UI screens

### 🔄 In Progress (Phase 2)
- CRUD operations for Apiary and Hive
- ViewModels with LiveData
- RecyclerView adapters
- Form validation

### 📋 Planned (Phase 3+)
- OpenAI API integration
- Audio recording service
- Excel import functionality
- Calculators (Varroa, Queen)
- Analytics with charts
- Cloud synchronization

## 🚀 Quick Navigation

| Feature | Location |
|---------|----------|
| Database Schema | `app/src/main/java/com/beekeeper/app/data/local/entity/` |
| DAOs | `app/src/main/java/com/beekeeper/app/data/local/dao/` |
| Repositories | `app/src/main/java/com/beekeeper/app/data/repository/` |
| UI Screens | `app/src/main/java/com/beekeeper/app/presentation/` |
| Layouts | `app/src/main/res/layout/` |
| Navigation | `app/src/main/res/navigation/nav_graph.xml` |
| String Resources | `app/src/main/res/values/strings.xml` |
| App Configuration | `app/build.gradle` |

## 🎨 UI Theme

- **Primary Color:** #FFC107 (Amber - Bee themed)
- **Primary Dark:** #FFA000
- **Accent:** #FF6F00
- **Background:** #FAFAFA
- **Text Primary:** #212121

## 📱 Permissions Required

- `INTERNET` - OpenAI API calls
- `RECORD_AUDIO` - Audio inspections
- `CAMERA` - Video inspections
- `READ_MEDIA_AUDIO/VIDEO` - Access recordings
- `READ_EXTERNAL_STORAGE` - Import Excel (API ≤32)
- `ACCESS_FINE_LOCATION` - Apiary GPS coordinates
- `POST_NOTIFICATIONS` - Calendar reminders

## 🔗 External Dependencies

- **AndroidX:** AppCompat, Material, ConstraintLayout, Navigation
- **Room:** Database ORM with RxJava2
- **RxJava2:** Reactive programming
- **Retrofit:** HTTP client for OpenAI API
- **Apache POI:** Excel file parsing
- **MPAndroidChart:** Graph visualization
- **Dexter:** Permission handling

## 📝 Notes

- All user-facing strings are in Slovak (`strings.xml`)
- Database uses UUID for primary keys (future cloud sync)
- Offline-first architecture
- Material Design with bee-themed colors
- ViewBinding enabled (no findViewById)
- ProGuard rules configured for POI and Room

---

**Version:** 1.0.0 - Phase 1 Complete
**Last Updated:** February 13, 2025
