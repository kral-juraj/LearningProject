# Audit hardcoded stringov - Nové súbory

Dátum: 2025-02-17
Status: ✅ VŠETKY OPRAVENÉ

## Súbory s nájdenými hardcoded stringmi (OPRAVENÉ)

### 1. ✅ HiveActivityDialog.java
**Nájdené:** 5 hardcoded prompt textov
**Riadky:** 148-170
```java
"napr. 2 medeníky" → tm.get("prompt.supers_example_2")
"napr. 3 medeníky" → tm.get("prompt.supers_example_3")
"napr. nízke dno" → tm.get("prompt.bottom_board_low")
"napr. vysoké dno" → tm.get("prompt.bottom_board_high")
"napr. 15 kg" → tm.get("prompt.honey_harvest_kg")
```

### 2. ✅ HiveCard.java
**Nájdené:** 1 hardcoded exception message
**Riadok:** 60
```java
"Failed to load HiveCard FXML" → tm.get("exception.failed_to_load_hive_card_fxml")
```

### 3. ✅ HiveViewModel.java
**Nájdené:** 13 hardcoded error/success messages
**Riadky:** 64, 77, 99, 104, 119, 146, 151, 160, 174, 179, 194, 199, 226
```java
"Chyba pri načítaní úľov: ..." → tm.get("error.loading_hives", ...)
"Názov úľa nemôže byť prázdny" → tm.get("validation.hive_name_required")
"Úľ úspešne vytvorený" → tm.get("success.hive_created")
"Chyba pri vytváraní úľa: ..." → tm.get("error.creating_hive", ...)
"Úľ úspešne aktualizovaný" → tm.get("success.hive_updated")
"Chyba pri aktualizácii úľa: ..." → tm.get("error.updating_hive", ...)
"Úľ úspešne zmazaný" → tm.get("success.hive_deleted")
"Chyba pri mazaní úľa: ..." → tm.get("error.deleting_hive", ...)
"Chyba pri aktualizácii poradia: ..." → tm.get("error.updating_order", ...)
```

## Súbory BEZ hardcoded stringov (PREVERENÉ)

### Desktop Layer (Java)
- ✅ **HiveListController.java** - Už používa tm.get() všade
- ✅ **HiveActivityHistoryDialog.java** - Všetky stringy cez tm.get()
- ✅ **HiveDialog.java** - Kompletne prelož (používa translation keys pre všetko)
- ✅ **JdbcHiveActivityDao.java** - Len technické SQL exception messages (akceptovateľné)
- ✅ **JdbcHiveDao.java** - Len SQL queries, žiadne user-facing stringy
- ✅ **DatabaseManager.java** - Len SQL DDL statements a migration logic

### Shared Layer (Java)
- ✅ **Hive.java** - POJO entity, žiadne stringy
- ✅ **HiveActivity.java** - POJO entity, žiadne stringy
- ✅ **HiveRepository.java** - Business logic, žiadne user-facing stringy
- ✅ **HiveActivityDao.java** - Interface, žiadne implementácie
- ✅ **FrameType.java** - Enum s translation keys (hive.frame.*)
- ✅ **HiveActivityType.java** - Enum s translation keys (hive.activity.*)
- ✅ **HiveType.java** - Enum s translation keys (hive.type.*)
- ✅ **TranslationManager.java** - i18n infraštruktúra, žiadne hardcoded stringy

### Resources
- ✅ **hive_card.fxml** - FXML s %translation.keys
- ✅ **hive_list.fxml** - FXML s %translation.keys
- ✅ **09_translations_hive_extended.sql** - SQL translations
- ✅ **10_translations_activity_history.sql** - SQL translations
- ✅ **11_translations_hive_dialog_fixes.sql** - SQL translations
- ✅ **12_translations_alert_buttons.sql** - SQL translations
- ✅ **13_translations_hardcoded_fixes.sql** - SQL translations (nový súbor s opravami)

### Test Files
- ✅ **JdbcHiveDaoTest.java** - Test kód, akceptovateľné anglické stringy
- ✅ **HiveRepositoryTest.java** - Test kód, akceptovateľné anglické stringy
- ✅ **HiveViewModelTest.java** - Test kód, akceptovateľné anglické stringy
- ✅ **HiveDragAndDropIntegrationTest.java** - Test kód, akceptovateľné
- ✅ **HiveHealthIndicatorsIntegrationTest.java** - Test kód, akceptovateľné

## Vytvorené preklady

### SQL súbor: 13_translations_hardcoded_fixes.sql

**Prompt kategória (10 prekladov):**
- prompt.supers_example_2 (SK: "napr. 2 medeníky", EN: "e.g. 2 supers")
- prompt.supers_example_3 (SK: "napr. 3 medeníky", EN: "e.g. 3 supers")
- prompt.bottom_board_low (SK: "napr. nízke dno", EN: "e.g. low bottom board")
- prompt.bottom_board_high (SK: "napr. vysoké dno", EN: "e.g. high bottom board")
- prompt.honey_harvest_kg (SK: "napr. 15 kg", EN: "e.g. 15 kg")

**Exception kategória (4 preklady):**
- exception.hive_activity_not_found (SK/EN s {0} parametrom)
- exception.failed_to_load_hive_card_fxml (SK/EN)

**Error kategória (10 prekladov):**
- error.loading_hives (SK/EN s {0} parametrom)
- error.creating_hive (SK/EN s {0} parametrom)
- error.updating_hive (SK/EN s {0} parametrom)
- error.deleting_hive (SK/EN s {0} parametrom)
- error.updating_order (SK/EN s {0} parametrom)

**Success kategória (6 prekladov):**
- success.hive_created (SK/EN)
- success.hive_updated (SK/EN)
- success.hive_deleted (SK/EN)

**Validation kategória (2 preklady):**
- validation.hive_name_required (SK/EN)

**Celkovo:** 32 nových prekladov (16 SK + 16 EN)

## Verifikácia

✅ Všetky preklady vložené do databázy ~/beekeeper-desktop.db
✅ Build úspešný: `gradle desktop:build -x :app:compileDebugJavaWithJavac`
✅ Kompilácia: `gradle shared:compileJava desktop:compileJava` - SUCCESS
✅ Aplikácia sa spúšťa bez chýb
✅ Dodržané CLAUDE.md pravidlá pre i18n (žiadne hardcoded user-facing stringy)

## Poznámky

### Akceptovateľné technické stringy (nie user-facing):
- SQL queries v DAO vrstvách (SELECT, INSERT, UPDATE, DELETE)
- SQL exception messages pre debugging ("HiveActivity not found: abc123")
- PropertyValueFactory parametre ("dateFormatted", "typeFormatted")
- CSS styling stringy ("-fx-text-fill: red;")
- FXML resource paths ("/view/hive_card.fxml")
- Test assertion messages v anglickom jazyku

### Úpravy v HiveViewModel:
HiveViewModel teraz používa TranslationManager.getInstance() pre všetky user-facing messages:
```java
private final TranslationManager tm;

public HiveViewModel(HiveRepository repository, SchedulerProvider schedulerProvider) {
    this.repository = repository;
    this.schedulerProvider = schedulerProvider;
    this.tm = TranslationManager.getInstance(); // ✅ Pridané
}
```

## Zhrnutie

**Skontrolované súbory:** 24 Java súborov + 5 FXML/SQL
**Nájdené hardcoded stringy:** 19 (v 3 súboroch)
**Opravené:** ✅ 19/19
**Status:** 🎉 100% ČISTÉ - žiadne hardcoded user-facing stringy
