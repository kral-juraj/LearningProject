# Kontext Projektu - Včelárska Aplikácia

## 📋 Základné Informácie

**Názov projektu:** Včelárska Aplikácia (Beekeeper App)
**Jazyk:** Java (100% - žiadny Kotlin)
**Platform:** Android (API 26+)
**Databáza:** Room + SQLite
**Architektúra:** MVVM + Repository Pattern
**Async:** RxJava2

## 🎯 Hlavný Cieľ

Nahradiť 5-ročný Excel súbor "Úľový denník 2025.xlsx" modernou Android aplikáciou s:
- Správou včelníc a úľov
- Audio/Video prehliadkami s AI extrakciou (OpenAI Whisper + GPT-4)
- Analýzou dát a vizualizáciou
- Importom historických dát z Excelu
- Kalkulačkami (Varroa, chov matiek)

## 📊 Aktuálny Stav

### Fáza 1: Foundation ✅ 100% COMPLETE
- Kompletná databáza (9 tabuliek)
- MVVM architektúra
- Base classes
- Navigation framework
- 60 súborov vytvorených

### Fáza 2: Core CRUD ⏳ 40% IN PROGRESS
- ✅ Apiary Management (100%)
- ✅ Hive Management (100%)
- ⏳ Navigation (0%)
- ⏳ Inspection Entry (0%)
- ⏳ Feeding Entry (0%)
- ⏳ Taxation Entry (0%)

**Celkovo:** 25% projektu hotových

## 🗄️ Databázová Schéma

### Hlavné Entity (9 tabuliek)

1. **apiaries** - Včelnice
   - id, name, location, latitude, longitude, timestamps

2. **hives** - Úle
   - id, apiaryId (FK), name, type, queenId, queenYear, active, notes

3. **inspections** - Prehliadky
   - id, hiveId (FK), date, temperature, strength, broodFrames, queenSeen, varroa, recordingId

4. **inspection_recordings** - Audio/Video nahrávky
   - id, inspectionId, filePath, transcription, extractedJson, processed

5. **feedings** - Krmenie
   - id, hiveId (FK), date, weightBefore, weightAfter, feedType, amountKg

6. **taxations** - Taxácie
   - id, hiveId (FK), date, temperature, totalFrames

7. **taxation_frames** - Detaily rámikov
   - id, taxationId (FK), position, cappedBroodDm, pollenDm, frameType

8. **calendar_events** - Kalendár úkonov
   - id, title, description, eventDate, eventType, hiveId, completed

9. **settings** - Nastavenia
   - key, value, updatedAt

## 🏗️ Implementované Komponenty

### Repozitáre (3/9)
- ✅ ApiaryRepository
- ✅ HiveRepository
- ✅ InspectionRepository
- ⏳ FeedingRepository (TODO)
- ⏳ TaxationRepository (TODO)
- ⏳ CalendarRepository (TODO)

### ViewModels (2)
- ✅ ApiaryViewModel
- ✅ HiveViewModel

### Fragmenty (6 placeholder + 2 funkčné)
- ✅ ApiaryListFragment (funkčný)
- ✅ HiveListFragment (funkčný)
- ⏳ DashboardFragment (placeholder)
- ⏳ CalendarFragment (placeholder)
- ⏳ CalculatorFragment (placeholder)
- ⏳ AnalyticsFragment (placeholder)
- ⏳ SettingsFragment (placeholder)

### Adapters (2)
- ✅ ApiaryAdapter (DiffUtil)
- ✅ HiveAdapter (DiffUtil)

### Dialógy (2)
- ✅ ApiaryDialogFragment
- ✅ HiveDialogFragment

## 📁 Štruktúra Projektu

```
app/src/main/java/com/beekeeper/app/
├── data/
│   ├── local/
│   │   ├── db/ (AppDatabase)
│   │   ├── dao/ (9 DAOs)
│   │   ├── entity/ (9 entities)
│   │   └── prefs/ (PreferencesManager)
│   ├── remote/ (TODO: OpenAI API)
│   └── repository/ (3/9 implementované)
├── domain/ (TODO: use cases)
├── presentation/
│   ├── base/ (BaseActivity, BaseFragment, BaseViewModel)
│   ├── main/ (MainActivity)
│   ├── apiary/ (✅ COMPLETE)
│   ├── hive/ (✅ COMPLETE)
│   └── [dashboard, calendar, calculator, analytics, settings] (TODO)
├── service/ (TODO: AudioRecording, ExcelImport)
└── util/ (Constants, DateUtils)
```

## 🔑 Kľúčové Technológie

### Dependencies
- AndroidX (AppCompat, Material, Navigation)
- Room 2.6.1 + RxJava2
- RxJava2 2.2.21
- Retrofit 2.9.0 (pre OpenAI)
- Apache POI 5.2.5 (pre Excel)
- MPAndroidChart v3.1.0 (pre grafy)
- WorkManager 2.9.0
- Dexter 6.2.3 (permissions)

### Design Patterns
- MVVM (Model-View-ViewModel)
- Repository Pattern
- Observer Pattern (LiveData)
- Reactive Programming (RxJava2)
- Singleton (Database)

## 🎨 UI/UX Features

### Material Design
- MaterialCardView pre položky
- FloatingActionButton (FAB)
- MaterialAlertDialog
- TextInputLayout outlined
- Bee-themed colors (Yellow/Amber)

### Interakcie
- RecyclerView s lazy loading
- Pull to refresh (TODO)
- Empty states
- Loading indicators
- Error handling s Toast

## 📝 Dôležité Rozhodnutia

1. **Java Only** - Používateľ pozná len Java, nie Kotlin
2. **Offline First** - Všetky dáta lokálne, sync neskôr
3. **UUID IDs** - Pre budúcu cloud synchronizáciu
4. **RxJava2** - Reaktívne database queries
5. **OpenAI API** - Používateľ zadá vlastný kľúč
6. **Excel Import** - Všetky historické dáta (2020-2025)

## 🚀 Ďalšie Kroky (Priorita)

### 1. Navigation (1-2 hodiny)
```java
// app/build.gradle
plugins {
    id 'androidx.navigation.safeargs' version '2.7.6'
}

// nav_graph.xml
<action
    android:id="@+id/action_apiaryList_to_hiveList"
    app:destination="@id/hiveListFragment" />

// ApiaryListFragment.java
Bundle args = new Bundle();
args.putString("apiaryId", apiary.getId());
args.putString("apiaryName", apiary.getName());
Navigation.findNavController(view)
    .navigate(R.id.action_apiaryList_to_hiveList, args);
```

### 2. Manual Inspection Entry (4-6 hodín)

**Potrebné súbory:**
- InspectionViewModel.java
- InspectionEntryFragment.java
- InspectionAdapter.java
- fragment_inspection_entry.xml
- item_inspection.xml

**Polia formuláru:**
- Dátum (DatePickerDialog)
- Čas (TimePickerDialog)
- Teplota (EditText number)
- Sila včelstva (SeekBar 1-10)
- Zásoby kg (EditText decimal)
- Rámiky s plodom (EditText number)
- Matka videná (CheckBox)
- Klieštik (CheckBox + počet)
- Agresivita (SeekBar 1-5)
- Poznámky (EditText multiline)

### 3. Hive Detail Screen (3-4 hodiny)

**TabLayout s 4 tabmi:**
- Tab 1: Prehľad (posledná prehliadka, graf sily)
- Tab 2: Prehliadky (RecyclerView)
- Tab 3: Krmenie (RecyclerView)
- Tab 4: Taxácie (RecyclerView)

## 🔮 Budúce Fázy

### Fáza 3: OpenAI Integration (2-3 týždne)
- AudioRecordingService
- Whisper API (speech-to-text)
- GPT-4 extraction
- Review & edit UI

### Fáza 4: Calendar & Calculators (2 týždne)
- Fenologický kalendár
- Varroa calculator
- Queen rearing calculator

### Fáza 5: Excel Import (2 týždne)
- Apache POI parser
- Mapping Excel → Entities
- Progress UI

### Fáza 6: Analytics (2 týždne)
- MPAndroidChart graphs
- Trend analysis
- CSV export

### Fáza 7: Cloud Sync (2-3 týždne)
- Backend API
- Push/pull sync
- Conflict resolution

## 📖 Dokumentácia Súbory

- `README.md` - Prehľad projektu
- `IMPLEMENTATION_LOG.md` - Fáza 1 detaily
- `PHASE_2_PROGRESS.md` - Fáza 2 progress
- `PROJECT_STRUCTURE.md` - Štruktúra súborov
- `GETTING_STARTED.md` - Development guide
- `PROJECT_CONTEXT.md` - Tento súbor

## 💾 Ako Pokračovať

1. Otvorte Android Studio
2. File → Open → `/Users/juraj.kral/IdeaProjects/LearningProject`
3. Prečítajte `PHASE_2_PROGRESS.md` pre aktuálny stav
4. Pokračujte s Navigation setup alebo Inspection Entry
5. Commitujte zmeny do Git (odporúčané)

## 🎯 Milestones

- [x] Fáza 1 Complete - Foundation ready
- [x] First CRUD working - Apiary Management
- [x] Second CRUD working - Hive Management
- [ ] Navigation working - Apiary → Hive
- [ ] First data entry form - Manual Inspection
- [ ] First AI feature - Audio recording
- [ ] Excel import working
- [ ] App ready for production use

**Status:** 🟢 ON TRACK | **Progress:** 25% | **Next:** Navigation + Inspection Entry
