# Development Plan - Beekeeper Desktop App

**Posledná aktualizácia:** 14. február 2025
**Aktuálny stav:** Fáza 3 Complete ✅

---

## 📊 Aktuálny Stav Aplikácie

### ✅ Implementované Funkcie (Fáza 3 Complete)

**Desktop aplikácia je plne funkčná s:**
- ✅ Včelnice (Apiaries) - CRUD operácie
- ✅ Úle (Hives) - CRUD, aktivácia/deaktivácia
- ✅ Prehliadky (Inspections) - kompletný formulár (23 polí)
- ✅ Krmenie (Feeding) - CRUD, typy krmiva, auto-výpočet množstva
- ✅ Taxácie (Taxation) - master-detail s rámikmi, agregované údaje
- ✅ Kalendár (Calendar) - globálne udalosti, prepojenie na úle/včelnice
- ✅ SQLite databáza - `~/beekeeper-desktop.db`
- ✅ Reaktívne UI - RxJava2 + JavaFX
- ✅ Slovenská lokalizácia - 100%

**Technológie:**
- JavaFX 17.0.2
- JDBC + SQLite
- RxJava2
- Multi-module Gradle (shared + desktop + app)

---

## 🚀 Plánované Fázy Vývoja

---

### **Fáza 4: Analytika & Grafy** 📈

**Priorita:** Vysoká
**Odhadovaný čas:** 2-3 týždne
**Účel:** Vizualizácia dát, trendy, porovnania úľov

#### Funkcie:

**4.1. Dashboard Tab**
- Celkový počet včelníc, úľov, prehliadok
- Počet aktívnych/neaktívnych úľov
- Posledné prehliadky (5 najnovších)
- Najbližšie kalendárové udalosti
- Celková suma krmenia za mesiac
- Quick stats cards

**4.2. Analytics Tab**
- Graf vývoja sily včelstiev v čase (line chart)
- Graf krmenia v čase (bar chart)
- Porovnanie úľov (multiple lines)
- Filter podľa dátumu (od-do)
- Výber včelnice/úľa
- Export grafov do PNG

**4.3. Reports**
- Mesačný report
- Ročný summary
- Report pre konkrétny úľ
- Export do TXT/CSV

#### Technológie:
- JavaFX Charts (LineChart, BarChart, PieChart)
- Alebo JFreeChart (pokročilejšie grafy)

#### Súbory na vytvorenie:
```
desktop/src/main/java/com/beekeeper/desktop/
├── controller/
│   ├── DashboardController.java
│   ├── AnalyticsController.java
│   └── ReportsController.java
├── util/
│   ├── ChartUtils.java
│   └── StatisticsCalculator.java
└── resources/view/
    ├── dashboard.fxml
    ├── analytics.fxml
    └── reports.fxml
```

#### Úlohy:
1. Vytvoriť dashboard.fxml s CardView layoutom
2. DashboardController - načítať stats z DB
3. analytics.fxml s ChartView
4. AnalyticsController - grafy s live dátami
5. ChartUtils - helper metódy pre grafy
6. StatisticsCalculator - agregácie a výpočty
7. Pridať do MainController nové taby
8. Testing

---

### **Fáza 5: Kalkulačky** 🧮

**Priorita:** Stredná
**Odhadovaný čas:** 1-2 týždne
**Účel:** Pomocné kalkulačky pre včelárov

#### Funkcie:

**5.1. Varroa Kalkulačka**
- Input: aktuálny počet kliešťov, dátum merania
- Output: projekcia rastu populácie (exponenciálny model)
- Graf vývoja na 30/60/90 dní
- Threshold indikátor (kedy ošetriť)
- Odporúčanie na ošetrenie
- Export výsledkov do kalendára (pripomienka)

**5.2. Matka - Timeline Kalkulačka**
- Input: dátum založenia (D), metóda (prielarvovanie/presadenie)
- Output: tabuľka míľnikov:
  - D+0: Založenie matôčnikov
  - D+5: Priloženie mriežky
  - D+7: Prehliadka plodu
  - D+10: Prehliadka matôčnikov
  - D+12: Zapečatenie matôčnikov
  - D+16: Presadenie do odkladákov
  - D+21: Kontrola kládky
- Color-coded timeline
- Export do kalendára (všetky míľniky)

**5.3. Feed Calculator (Bonus)**
- Input: target zásoby (kg), aktuálne zásoby
- Output: koľko krmiva treba (sirup 1:1, 3:2, fondant)
- Konverzia kg → litre → cukor + voda

#### Súbory na vytvorenie:
```
desktop/src/main/java/com/beekeeper/desktop/
├── controller/
│   └── CalculatorsController.java
├── calculator/
│   ├── VarroaCalculator.java
│   ├── QueenRearingCalculator.java
│   └── FeedCalculator.java
└── resources/view/
    └── calculators.fxml
```

#### Úlohy:
1. calculators.fxml - 3 sekcie (TabPane)
2. VarroaCalculator - matematický model rastu
3. QueenRearingCalculator - timeline generator
4. FeedCalculator - konverzie
5. CalculatorsController - UI binding
6. Integration s CalendarEventViewModel (export)
7. Testing

---

### **Fáza 6: Nastavenia & Konfigurácia** ⚙️

**Priorita:** Stredná
**Odhadovaný čas:** 3-5 dní
**Účel:** Užívateľské preferencie a konfigurácia

#### Funkcie:

**6.1. General Settings**
- Predvolená včelnica (pri otvorení app)
- Jazyk: Slovak / English (pre budúcnosť)
- Téma: Svetlá (budúcnosť: tmavá)
- Database location (zobrazenie cesty)

**6.2. Backup Settings**
- Auto-backup ON/OFF
- Backup interval (daily, weekly, monthly)
- Backup location (file chooser)
- Manual backup button
- Restore from backup button

**6.3. Calendar Settings**
- Predvolený typ udalosti
- Notifikácie (budúcnosť - OS notifications)
- Fenologický kalendár (import predvyplnených udalostí)

**6.4. Advanced**
- OpenAI API key (pre budúcu AI integráciu)
- Developer mode (zobrazenie debug info)
- Reset settings to default
- Export/Import settings (JSON)

#### Súbory na vytvorenie:
```
desktop/src/main/java/com/beekeeper/desktop/
├── controller/
│   └── SettingsController.java
├── service/
│   ├── BackupService.java
│   └── SettingsService.java
└── resources/view/
    └── settings.fxml
```

#### Úlohy:
1. settings.fxml - TabPane s 4 sekciami
2. SettingsService - load/save Settings entity
3. BackupService - backup/restore DB
4. SettingsController - UI binding
5. Integration s Main menu
6. Testing

---

### **Fáza 7: UI/UX Improvements** 🎨

**Priorita:** Nízka
**Odhadovaný čas:** 1-2 týždne
**Účel:** Zlepšenie používateľskej skúsenosti

#### Funkcie:

**7.1. Keyboard Shortcuts**
- Ctrl+N - Nový záznam (context-aware)
- Ctrl+E - Edit vybraný záznam
- Ctrl+D - Delete vybraný záznam
- Ctrl+R - Refresh tabuľku
- Ctrl+F - Focus na search bar
- Ctrl+1..6 - Switch tabs
- F5 - Refresh
- Esc - Zavrieť dialóg

**7.2. Advanced Filtering & Search**
- Search bar v každej tabuľke
- Live filtering (type-to-search)
- Filter podľa dátumu (date range picker)
- Filter podľa stavu (aktívne/neaktívne úle)
- Clear filters button

**7.3. Better Empty States**
- Ikony a text keď nie sú žiadne dáta
- Quick action buttons v empty state
- First-time user tooltips

**7.4. Tooltips & Help**
- Tooltip pre každé pole vo formulároch
- Help menu s user guide
- About dialog (verzia, autor, licence)
- Keyboard shortcuts cheat sheet

**7.5. UI Polish**
- Icons pre všetky tlačidlá
- Better color scheme (konzistentné farby)
- Hover effects na buttons
- Loading spinners
- Better error messages

#### Súbory na úpravu:
- Všetky existujúce FXML súbory
- Všetky Controller súbory
- `KeyboardShortcuts.java` (nový)
- `FilterHelper.java` (nový)
- CSS štýly (nový súbor)

#### Úlohy:
1. KeyboardShortcuts.java - definície shortcuts
2. Pridať shortcuts do všetkých controllers
3. FilterHelper - generic filtering logic
4. Search bars do všetkých tabuliek
5. Empty state layouts
6. Tooltip texty
7. Help dialog
8. CSS styling improvements
9. Icons (FontAwesome alebo Material Icons)
10. Testing

---

### **Fáza 8: PDF Reports** 📄

**Priorita:** Nízka
**Odhadovaný čas:** 1-2 týždne
**Účel:** Tlačiteľné reporty

#### Funkcie:

**8.1. Report Types**
- Inspection Report (jedna prehliadka)
- Hive Summary Report (všetky prehliadky úľa)
- Monthly Report (všetky úle za mesiac)
- Annual Report (ročný prehľad)
- Taxation Report (taxácia s rámikmi)
- Feeding Log Report

**8.2. Report Features**
- PDF export
- Header: logo, dátum, názov včelnice
- Tables s dátami
- Charts (optional)
- Slovak language
- Print-friendly formatting

**8.3. UI**
- Reports menu
- Report preview
- Save to file dialog
- Print dialog

#### Technológie:
- iText PDF 5.x (LGPL licencia)
- Alebo Apache PDFBox

#### Súbory na vytvorenie:
```
desktop/src/main/java/com/beekeeper/desktop/
├── report/
│   ├── PdfReportService.java
│   ├── InspectionReport.java
│   ├── HiveSummaryReport.java
│   ├── MonthlyReport.java
│   └── AnnualReport.java
└── controller/
    └── ReportsController.java (ak ešte neexistuje)
```

#### Úlohy:
1. Pridať iText dependency do build.gradle
2. PdfReportService - base class
3. Implementovať každý typ reportu
4. ReportsController - UI pre výber reportu
5. File chooser pre ukladanie
6. Testing s rôznymi dátami

---

### **Fáza 9: Lokalizácia (English)** 🌍

**Priorita:** Veľmi nízka
**Odhadovaný čas:** 1 týždeň
**Účel:** Podpora anglického jazyka

#### Funkcie:
- Resource bundles: `strings_sk.properties`, `strings_en.properties`
- Language selector v Settings
- Všetky texty cez resource bundle
- Date formatting podľa locale
- Number formatting podľa locale

#### Súbory na vytvorenie:
```
desktop/src/main/resources/
├── i18n/
│   ├── strings_sk.properties
│   └── strings_en.properties
```

#### Úlohy:
1. Extrahovať všetky hardcoded stringy
2. Vytvoriť strings_sk.properties
3. Vytvoriť strings_en.properties
4. ResourceBundle loader
5. Language selector v SettingsController
6. Update všetkých FXML súborov
7. Testing v oboch jazykoch

---

### **Fáza 10: Testovanie** 🧪

**Priorita:** Stredná (priebežne)
**Odhadovaný čas:** Kontinuálne
**Účel:** Kvalita a spoľahlivosť

#### Typy testov:

**10.1. Unit Tests**
- ViewModels (RxJava testing)
- Repositories (mock DAOs)
- Calculators (Varroa, QueenRearing)
- Utils (DateUtils, ChartUtils)

**10.2. Integration Tests**
- JDBC DAOs (in-memory SQLite)
- Database migrations
- Cascade deletes
- CRUD operations

**10.3. UI Tests**
- TestFX framework
- Form validation
- Navigation flow
- Error handling

**10.4. Performance Tests**
- Veľké datasety (1000+ záznamov)
- Memory leaks
- UI responsiveness

#### Nástroje:
- JUnit 5
- Mockito
- AssertJ
- TestFX (pre JavaFX UI testing)

#### Úlohy:
- Priebežne písať testy ku každej fáze
- CI/CD setup (GitHub Actions)
- Code coverage > 70%

---

### **Fáza 11: Android Reconversion** 📱

**Priorita:** Dlhodobá budúcnosť (6+ mesiacov)
**Odhadovaný čas:** 4-6 týždňov
**Účel:** Mobilná verzia aplikácie

#### Postup:
1. Použiť existujúci `app/` module (Android)
2. Rekonvertovať shared ViewModels do Android UI
3. Prepísať Fragments na použitie shared ViewModels
4. Room DAOs už existujú
5. Material Design UI
6. Mobile-optimized layouts
6. Testing na Android zariadeniach

#### Referencia:
- `CONVERSION_GUIDE.md` - detailný návod

---

### **Fáza 12: Cloud Sync** ☁️

**Priorita:** Dlhodobá budúcnosť (12+ mesiacov)
**Odhadovaný čas:** 8-12 týždňov
**Účel:** Synchronizácia medzi desktop a Android

#### Komponenty:

**Backend API:**
- Spring Boot / Node.js + Express
- PostgreSQL database
- REST API
- JWT authentication
- User registration/login

**Client-side:**
- Sync service v oboch platformách
- Push/Pull logic
- Conflict resolution
- Incremental sync (len zmeny)
- Last sync timestamp tracking

**Features:**
- Manual sync button
- Auto-sync interval (Settings)
- Sync status indicator
- Multi-device support
- Offline-first architecture

---

### **Fáza 13: AI Integrácia** 🤖

**Priorita:** Dlhodobá budúcnosť (18+ mesiacov)
**Odhadovaný čas:** 6-8 týždňov
**Účel:** Voice-to-data pre prehliadky

#### Funkcie:

**Desktop:**
- Microphone recording
- OpenAI Whisper API - transcription
- GPT-4 - extraction dát z textu
- Review & edit extracted data
- Save to inspection

**Android:**
- Rovnaké funkcie
- Optimalizované pre mobilné nahrávanie
- Background recording service

#### API Integration:
- OpenAI API key v Settings
- Whisper API calls
- GPT-4 API calls s custom prompt
- Parsing GPT response do Inspection entity

---

## 🎯 Odporúčané Poradie Implementácie

### **Krátkodbé** (1-2 mesiace):
1. **Fáza 4: Analytika & Grafy** - najužitočnejšie pre bežné používanie
2. **Fáza 5: Kalkulačky** - praktické nástroje
3. **Fáza 6: Nastavenia** - konfigurácia a backup

### **Strednodobé** (3-6 mesiacov):
4. **Fáza 7: UI/UX Improvements** - lepší user experience
5. **Fáza 8: PDF Reports** - profesionálne výstupy
6. **Fáza 10: Testovanie** - priebežne

### **Dlhodobé** (6+ mesiacov):
7. **Fáza 9: Lokalizácia** (ak potrebuješ angličtinu)
8. **Fáza 11: Android Reconversion** (ak chceš mobilnú verziu)
9. **Fáza 12: Cloud Sync** (ak potrebuješ multi-device)
10. **Fáza 13: AI Integrácia** (pokročilá feature)

---

## 📝 Poznámky

### Excel Import (Jednorazový Preklop)
Excel import/export **nie je súčasťou plánovaných fáz**, pretože:
- Import sa použije len raz (historické dáta 2020-2025)
- Lepšie riešenie: jednorazový migračný skript (Python/Java)
- Neplýtvať časom na UI v aplikácii

**Riešenia pre jednorazový preklop:**
- Pozri sekciu nižšie: "Excel → DB Migration Strategies"

### Verziovanie
- Aktuálna verzia: **2.0.0** (Multi-platform Desktop)
- Fáza 4 complete: **2.1.0**
- Fáza 5 complete: **2.2.0**
- atď.

### Priorities môžu sa zmeniť
Toto je living document - priority sa môžu upravovať podľa potrieb.

---

## 🔗 Súvisiaca Dokumentácia

- **README.md** - Prehľad projektu
- **DESKTOP_SETUP.md** - Inštalácia a používanie
- **CONVERSION_GUIDE.md** - Multi-platform vývoj
- **PROJECT_STATE.md** - Aktuálny stav
- **TESTING.md** - Testing checklist

---

**Posledná aktualizácia:** 14. február 2025
**Stav:** Living document
**Next milestone:** Fáza 4 - Analytika & Grafy
