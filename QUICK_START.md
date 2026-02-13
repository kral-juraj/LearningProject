# Quick Start - Pokračovanie v Práci

## 🚀 Rýchly Štart

### Kde sme skončili?
**Dátum:** 13. február 2025
**Status:** Fáza 2 - 40% Complete
**Hotové:** Apiary & Hive Management s plným CRUD

### Spustenie projektu
```bash
cd /Users/juraj.kral/IdeaProjects/LearningProject
# Otvorte v Android Studio alebo:
./gradlew build
./gradlew installDebug
```

## 📋 Čo Funguje

### ✅ Môžete testovať:
1. Menu → Včelnice
2. Pridať včelnicu (FAB +)
3. Upraviť/zmazať včelnicu
4. [TODO] Otvoriť včelnicu → zoznam úľov
5. Pridať úle (U1, L1, atď.)
6. Upraviť/zmazať úle

### ⏳ Ešte nefunguje:
- Navigácia medzi obrazovkami
- Prehliadky úľov
- Krmenie
- Taxácie
- Kalendár
- Kalkulačky
- Audio/Video recording
- Excel import

## 🎯 Najbližšie 3 Úlohy

### 1. NAVIGATION (1-2h) - Najjednoduchšie
**Súbory na úpravu:**
- `app/build.gradle` - pridať Safe Args
- `nav_graph.xml` - pridať actions
- `ApiaryListFragment.java` - implementovať navigáciu

**Kód:**
```java
// ApiaryAdapter click:
Navigation.findNavController(view).navigate(
    R.id.action_apiaryList_to_hiveList,
    HiveListFragment.newInstance(apiary.getId(), apiary.getName())
);
```

### 2. INSPECTION ENTRY (4-6h) - Najdôležitejšie
**Nové súbory:**
- `InspectionViewModel.java`
- `InspectionEntryFragment.java`
- `InspectionAdapter.java` (pre históriu)
- `fragment_inspection_entry.xml`

**Formulár polia:**
- DatePicker + TimePicker
- Teplota (číslo)
- Sila včelstva (1-10)
- Rámiky s plodom
- Matka videná (checkbox)
- Poznámky (text)

### 3. HIVE DETAIL (3-4h) - Pre kontext
**Nový súbor:**
- `HiveDetailFragment.java` s TabLayout

**4 taby:**
- Prehľad
- História prehliadok
- História krmenia
- História taxácií

## 📁 Dôležité Súbory

### Pre navigáciu:
- `nav_graph.xml` - definície obrazoviek
- `MainActivity.java` - NavController

### Pre dátové operácie:
- `*ViewModel.java` - business logic
- `*Repository.java` - database prístup
- `*Dao.java` - SQL queries

### Pre UI:
- `fragment_*.xml` - layouts
- `*Fragment.java` - UI logic
- `*Adapter.java` - RecyclerView

## 💡 Užitočné Snippety

### Vytvoriť ViewModel
```java
public class InspectionViewModel extends BaseViewModel {
    private final InspectionRepository repository;
    private final MutableLiveData<List<Inspection>> inspections = new MutableLiveData<>();

    public InspectionViewModel() {
        this.repository = new InspectionRepository(
            BeekeeperApplication.getInstance().getDatabase().inspectionDao()
        );
    }

    public void loadInspections(String hiveId) {
        addDisposable(repository.getInspectionsByHiveId(hiveId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(inspections::setValue, /* error */));
    }
}
```

### DatePicker Dialog
```java
Calendar calendar = Calendar.getInstance();
DatePickerDialog picker = new DatePickerDialog(
    requireContext(),
    (view, year, month, day) -> {
        // Uložiť dátum
    },
    calendar.get(Calendar.YEAR),
    calendar.get(Calendar.MONTH),
    calendar.get(Calendar.DAY_OF_MONTH)
);
picker.show();
```

### Navigation s args
```java
Bundle args = new Bundle();
args.putString("hiveId", hive.getId());
args.putString("hiveName", hive.getName());
Navigation.findNavController(view)
    .navigate(R.id.action_to_detail, args);
```

## 🐛 Časté Problémy

### Build fails
```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

### Database schema changed
1. Zvýšiť `DATABASE_VERSION` v `Constants.java`
2. Pridať migration alebo `.fallbackToDestructiveMigration()`

### ViewBinding not generated
1. Build → Clean Project
2. Build → Rebuild Project
3. Reštartovať Android Studio

### RxJava subscription errors
- Vždy dispose v `onCleared()`
- Použiť `addDisposable()` z BaseViewModel

## 📚 Dokumentácia

Pred začatím práce prečítajte:
1. `PROJECT_CONTEXT.md` - Celý kontext projektu
2. `PHASE_2_PROGRESS.md` - Detailný progress
3. `README.md` - Prehľad architektúry

## 🔗 Užitočné Linky

- Android Docs: https://developer.android.com
- Room: https://developer.android.com/training/data-storage/room
- Navigation: https://developer.android.com/guide/navigation
- RxJava: https://reactivex.io

## ✅ Checklist Pred Commitom

- [ ] Aplikácia sa buildne bez chýb
- [ ] Aplikácia sa spustí bez crashu
- [ ] Nové funkcie testované manuálne
- [ ] Žiadne hardcoded strings (použiť strings.xml)
- [ ] ViewBinding použitý správne
- [ ] Disposables správne dispose-nuté
- [ ] Aktualizovať PHASE_2_PROGRESS.md

## 🎯 Odporúčaný Postup

**Session 1 (2h):** Navigation
- Pridať Safe Args
- Implementovať Apiary → Hive navigation
- Testovať flow

**Session 2 (4h):** Inspection Form
- Vytvoriť ViewModel + Repository
- Vytvoriť formulár layout
- Implementovať uloženie

**Session 3 (2h):** Inspection Display
- Adapter pre históriu
- Detail view
- Testovať celý flow

**Session 4 (3h):** Hive Detail
- TabLayout s 4 tabmi
- Integrovať inšpekcie
- Polish UI

---

**Posledná aktualizácia:** 13.2.2025
**Next Step:** Navigation Setup
**Status:** 🟢 Ready to Continue
