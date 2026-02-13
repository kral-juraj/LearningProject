# TODO List - Včelárska Aplikácia

## 🔥 HIGH PRIORITY (Fáza 2 - dokončiť)

### Navigation Setup ⚡ NEXT
- [ ] Pridať Safe Args plugin do `app/build.gradle`
- [ ] Definovať navigation actions v `nav_graph.xml`
- [ ] Implementovať navigáciu v `ApiaryListFragment` → `HiveListFragment`
- [ ] Testovať prechod s argumentami (apiaryId, apiaryName)

### Manual Inspection Entry 📝
- [ ] Vytvoriť `InspectionViewModel.java`
- [ ] Vytvoriť `InspectionEntryFragment.java`
- [ ] Layout `fragment_inspection_entry.xml` s formulárom
- [ ] Implementovať DatePickerDialog
- [ ] Implementovať TimePickerDialog
- [ ] SeekBar pre silu včelstva (1-10)
- [ ] CheckBox pre matku videnú
- [ ] CheckBox pre klieštika
- [ ] SeekBar pre agresivitu (1-5)
- [ ] Validácia vstupov
- [ ] Uloženie do databázy cez repository
- [ ] Toast notifikácia po uložení

### Inspection History Display 📊
- [ ] Vytvoriť `InspectionAdapter.java`
- [ ] Layout `item_inspection.xml`
- [ ] Fragment pre históriu prehliadok
- [ ] Načítanie prehliadok pre úľ
- [ ] Detail view pre prehliadku
- [ ] Edit/Delete akcie

### Hive Detail Screen 🏠
- [ ] Vytvoriť `HiveDetailFragment.java`
- [ ] TabLayout s 4 tabmi
- [ ] Tab 1: Prehľad (stats, posledná prehliadka)
- [ ] Tab 2: História prehliadok (RecyclerView)
- [ ] Tab 3: História krmenia (RecyclerView)
- [ ] Tab 4: História taxácií (RecyclerView)
- [ ] FAB menu pre nové akcie (inspection/feeding/taxation)

---

## 📋 MEDIUM PRIORITY (Fáza 2 pokračovanie)

### Feeding Management 🍯
- [ ] Vytvoriť `FeedingViewModel.java`
- [ ] Vytvoriť `FeedingRepository.java` (už existuje DAO)
- [ ] `FeedingEntryFragment.java`
- [ ] Layout pre krmenie form
- [ ] Spinner pre typ krmiva (Sirup 1:1, 3:2, Fondant, Pel)
- [ ] Weight before/after
- [ ] Uloženie do DB
- [ ] `FeedingAdapter.java` pre históriu
- [ ] Display v Hive Detail

### Taxation Management 📐
- [ ] Vytvoriť `TaxationViewModel.java`
- [ ] Vytvoriť `TaxationRepository.java`
- [ ] `TaxationEntryFragment.java`
- [ ] Layout pre taxáciu
- [ ] RecyclerView pre rámiky (1-25)
- [ ] Frame detail form (plod, pel, typ)
- [ ] `TaxationFrameAdapter.java`
- [ ] Uloženie kompletnej taxácie
- [ ] Detail view taxácie

### Dashboard 📈
- [ ] Aktualizovať `DashboardFragment.java`
- [ ] CardView s počtami (včelnice, úle, prehliadky)
- [ ] Posledné prehliadky (3-5)
- [ ] Upcoming calendar events
- [ ] Quick stats (celkový počet úľov, aktívnych)

---

## ⏳ LOW PRIORITY (Budúce fázy)

### Calendar & Events 📅 (Fáza 4)
- [ ] `CalendarViewModel.java`
- [ ] `CalendarRepository.java`
- [ ] Calendar view (MonthView)
- [ ] Event CRUD
- [ ] Fenologický kalendár (predvyplnený)
- [ ] Notifikácie (WorkManager)
- [ ] AlarmManager pre pripomienky

### Calculators 🔢 (Fáza 4)
- [ ] Varroa Calculator
  - [ ] Input: aktuálny počet
  - [ ] Output: projekcia rastu
  - [ ] Graf (MPAndroidChart)
  - [ ] Export výsledkov
- [ ] Queen Rearing Calculator
  - [ ] Input: dátum D
  - [ ] Output: tabuľka míľnikov (D+5, D+7, atď.)
  - [ ] Export do kalendára

### Analytics 📊 (Fáza 6)
- [ ] `AnalyticsViewModel.java`
- [ ] Line chart - vývoj sily včelstiev
- [ ] Line chart - váhy úľov v čase
- [ ] Bar chart - počet úkonov
- [ ] Porovnanie úľov
- [ ] Filter podľa dátumu
- [ ] Export do CSV
- [ ] Export do PDF

### Settings ⚙️
- [ ] Aktualizovať `SettingsFragment.java`
- [ ] Input pre OpenAI API key
- [ ] Predvolená včelnica
- [ ] Auto-delete recordings (dni)
- [ ] Notifikácie ON/OFF
- [ ] Dark mode (TODO budúcnosť)
- [ ] Export all data
- [ ] Import Excel trigger
- [ ] O aplikácii (verzia, autor)

---

## 🚀 FUTURE FEATURES (Fázy 3, 5, 7)

### Audio/Video Recording 🎙️ (Fáza 3)
- [ ] `AudioRecordingService.java`
- [ ] MediaRecorder setup
- [ ] Record/Pause/Stop UI
- [ ] Save to file
- [ ] OpenAI Whisper API integration
- [ ] Transcription display
- [ ] GPT-4 data extraction
- [ ] Review & edit extracted data
- [ ] Save to inspection
- [ ] Auto-delete old recordings

### Excel Import 📊 (Fáza 5)
- [ ] `ExcelImportService.java`
- [ ] Apache POI setup
- [ ] File picker (SAF)
- [ ] Parse Calendar sheets
- [ ] Parse Krmenie sheets
- [ ] Parse Notes sheets
- [ ] Parse Taxacia sheets
- [ ] Mapping logic
- [ ] Progress dialog
- [ ] Error handling
- [ ] Import všetkých rokov (2020-2025)
- [ ] Verification screen

### Cloud Sync 🌐 (Fáza 7)
- [ ] Backend API design
- [ ] Auth systém
- [ ] Push/pull sync logic
- [ ] Conflict resolution
- [ ] Sync status indicator
- [ ] Manual sync trigger
- [ ] Auto sync (WorkManager)
- [ ] Multi-device support

---

## 🐛 BUGS & FIXES

- [ ] TODO: Navigácia medzi obrazovkami nefunguje (Safe Args)
- [ ] TODO: Žiadne indikátory počtu úľov v Apiary card
- [ ] TODO: Žiadne unit testy

---

## 🎨 UI/UX IMPROVEMENTS

- [ ] Splash screen
- [ ] Dark mode support
- [ ] Lepšie ikony (custom)
- [ ] Animácie pri prechode medzi obrazovkami
- [ ] SwipeRefreshLayout pre zoznamy
- [ ] Skeleton loading screens
- [ ] Better empty states s ilustráciami
- [ ] Snackbar namiesto Toast
- [ ] Floating labels
- [ ] Material You colors (Android 12+)

---

## 📖 DOCUMENTATION

- [ ] Javadoc komentáre pre všetky public metódy
- [ ] README.md - usage instructions
- [ ] API documentation
- [ ] User manual (slovensky)
- [ ] Video tutorial
- [ ] Screenshots pre README

---

## 🧪 TESTING

- [ ] Unit testy - ViewModels
- [ ] Unit testy - Repositories
- [ ] Unit testy - Use Cases
- [ ] Integration testy - Database
- [ ] UI testy - Espresso
- [ ] End-to-end testy
- [ ] Performance testing
- [ ] Memory leak testing

---

## 🔐 SECURITY & OPTIMIZATION

- [ ] ProGuard konfigurácia
- [ ] R8 optimization
- [ ] Database indexing
- [ ] Lazy loading pre veľké zoznamy
- [ ] Pagination
- [ ] Image compression
- [ ] Cache stratégia
- [ ] Secure storage pre API keys
- [ ] Backup & restore

---

## 📦 RELEASE PREPARATION

- [ ] Version bump system
- [ ] Changelog
- [ ] Beta testing
- [ ] Bug fixing
- [ ] Performance optimization
- [ ] Play Store listing
- [ ] Screenshots & promo
- [ ] Privacy policy
- [ ] Terms of service

---

**Priorita:** Navigation → Inspection Entry → Hive Detail
**Target:** Dokončiť Fázu 2 do konca februára
**Status:** 🟢 On Track
