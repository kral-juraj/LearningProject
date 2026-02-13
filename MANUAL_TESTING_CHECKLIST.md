# Kontrolný Zoznam Manuálneho Testovania - Desktop Aplikácia

**Projekt:** Beekeeper Desktop Application
**Verzia:** 1.0
**Dátum vytvorenia:** 2026-02-13
**Posledná aktualizácia:** 2026-02-13

---

## Ako Používať Tento Dokument

- [ ] označuje neotestovanú funkcionalitu
- [x] označuje otestovanú a funkčnú funkcionalitu
- [⚠] označuje nájdený problém (pridať poznámku)
- [🐛] označuje kritickú chybu

**Pred každým releaseom musí byť všetko [ ] zmenené na [x]**

---

## 🚀 Spustenie Aplikácie

### Základné Spustenie
- [ ] Aplikácia sa spustí bez chýb: `gradle desktop:run`
- [ ] Zobrazí sa hlavné okno s TabPane
- [ ] Všetky 6 tabov sú viditeľné (Včelnice, Úle, Prehliadky, Krmenie, Taxácie, Kalendár)
- [ ] Tab "Včelnice" je aktívny pri spustení
- [ ] Tab "Kalendár" je povolený pri spustení
- [ ] Taby "Úle", "Prehliadky", "Krmenie", "Taxácie" sú zakázané (disable=true)

### Stabilita na macOS
- [ ] Žiadne NSTrackingRectTag warninga v konzole
- [ ] Gesty trackpadu fungujú správne
- [ ] Žiadne graphical glitches pri prepínaní tabov

---

## 📅 KALENDÁR UDALOSTÍ (Globálny Tab)

### Zobrazenie Zoznamu
- [ ] Tab "Kalendár" je vždy povolený
- [ ] Tabuľka zobrazuje stĺpce: Dátum, Názov, Typ, Popis, Dokončené
- [ ] Dátumy sú správne naformátované (dd.MM.yyyy HH:mm)
- [ ] Typy udalostí majú slovenské popisky (Prehliadka, Krmenie, atď.)
- [ ] Dokončené zobrazuje "Áno"/"Nie"
- [ ] Status bar ukazuje počet udalostí (napr. "5 udalostí")

### Vytvorenie Novej Udalosti
- [ ] Tlačidlo "Nová udalosť" otvorí dialóg
- [ ] Dialóg má všetkých 11 polí:
  - [ ] Názov (TextField) - povinné
  - [ ] Dátum (DatePicker) - povinné
  - [ ] Čas (HH:MM TextFieldy) - validácia 0-23, 0-59
  - [ ] Typ (ComboBox s 5 možnosťami) - povinné
  - [ ] Včelnica (ComboBox, nullable)
  - [ ] Úľ (ComboBox, nullable)
  - [ ] Popis (TextField)
  - [ ] Dokončené (CheckBox)
  - [ ] Poznámky (TextArea)
- [ ] ComboBox včelníc načíta dáta z databázy
- [ ] Výber včelnice načíta úle pre túto včelnicu do ComboBox úľov
- [ ] Možnosť "(Žiadna včelnica)" a "(Žiadny úľ)" v ComboBoxoch
- [ ] Validácia: názov a dátum sú povinné
- [ ] Validácia: hodina 0-23, minúta 0-59
- [ ] Tlačidlo "Uložiť" vytvorí udalosť v databáze
- [ ] Tlačidlo "Zrušiť" zatvorí dialóg bez uloženia
- [ ] Po úspešnom uložení: zelená správa "Udalosť úspešne vytvorená"
- [ ] Tabuľka sa automaticky aktualizuje

### Úprava Existujúcej Udalosti
- [ ] Vybrať udalosť v tabuľke
- [ ] Tlačidlo "Upraviť" sa povolí
- [ ] Dialóg sa otvorí s predvyplnenými hodnotami
- [ ] Všetky polia obsahujú správne hodnoty
- [ ] Dátum a čas sú správne extrahované z timestampu
- [ ] Včelnica a úľ sú správne vybraté v ComboBoxoch
- [ ] Zmeny sa uložia do databázy
- [ ] Status: "Udalosť úspešne aktualizovaná"

### Mazanie Udalosti
- [ ] Vybrať udalosť
- [ ] Tlačidlo "Zmazať" sa povolí
- [ ] Zobrazí sa potvrdzovací dialóg
- [ ] Tlačidlo "OK" zmaže udalosť z databázy
- [ ] Tlačidlo "Zrušiť" nezmaze udalosť
- [ ] Status: "Udalosť úspešne zmazaná"
- [ ] Tabuľka sa aktualizuje

### Prepínanie Dokončenia
- [ ] Vybrať udalosť
- [ ] Tlačidlo "Prepnúť dokončenie" sa povolí
- [ ] Kliknutie zmení stav completed v databáze
- [ ] Stĺpec "Dokončené" sa aktualizuje (Áno ↔ Nie)
- [ ] Status: "Udalosť úspešne aktualizovaná"

### Obnovenie Zoznamu
- [ ] Tlačidlo "Obnoviť" znovu načíta dáta z databázy
- [ ] Žiadne chybové hlásenia

---

## 🍯 SPRÁVA KRMENIA (Tab viazaný na úľ)

### Povolenie Tabu
- [ ] Tab "Krmenie" je zakázaný pri spustení
- [ ] Vybrať včelnicu v tabe "Včelnice"
- [ ] Kliknúť "Zobraziť úle"
- [ ] Vybrať úľ v tabe "Úle"
- [ ] Kliknúť "Zobraziť prehliadky"
- [ ] Tab "Krmenie" sa povolí

### Zobrazenie Zoznamu
- [ ] Tabuľka zobrazuje stĺpce: Dátum, Typ krmiva, Množstvo (kg), Hmotnosť pred (kg), Hmotnosť po (kg)
- [ ] Dátumy správne naformátované
- [ ] Typy krmiva majú slovenské popisky (Sirup 1:1, Sirup 3:2, Fondán, Peľová placka)
- [ ] Status bar: "X kŕmení"

### Vytvorenie Nového Krmenia
- [ ] Tlačidlo "Nové krmenie" otvorí dialóg
- [ ] Dialóg má všetkých 9 polí:
  - [ ] Dátum (DatePicker) - povinné
  - [ ] Čas (HH:MM) - validácia 0-23, 0-59
  - [ ] Typ krmiva (ComboBox s 4 možnosťami) - povinné
  - [ ] Hmotnosť pred (TextField, double)
  - [ ] Hmotnosť po (TextField, double)
  - [ ] Množstvo (TextField, double) - povinné, > 0
  - [ ] Poznámky (TextArea)
- [ ] **Automatický výpočet:** Zadať hmotnosť pred (napr. 20) a po (napr. 25)
- [ ] Pole "Množstvo" sa automaticky vypočíta (25 - 20 = 5)
- [ ] Validácia: dátum a množstvo > 0 sú povinné
- [ ] Uloženie vytvorí záznam v databáze
- [ ] Status: "Krmenie úspešne vytvorené"

### Úprava Krmenia
- [ ] Vybrať krmenie
- [ ] Tlačidlo "Upraviť" sa povolí
- [ ] Dialóg s predvyplnenými hodnotami
- [ ] Zmeny sa uložia
- [ ] Status: "Krmenie úspešne aktualizované"

### Mazanie Krmenia
- [ ] Vybrať krmenie
- [ ] "Zmazať" → potvrdzovací dialóg
- [ ] Zmazanie z databázy
- [ ] Status: "Krmenie úspešne zmazané"

---

## 🔍 PREHLIADKY (Kompletný Formulár)

### Vytvorenie Novej Prehliadky
- [ ] Tlačidlo "Nová prehliadka" otvorí dialóg
- [ ] Dialóg je ScrollPane (kvôli veľkosti)
- [ ] **Sekcia 1: Základné údaje**
  - [ ] DatePicker + hodina/minúta (validácia)
  - [ ] Teplota (TextField, double)
- [ ] **Sekcia 2: Sila a zásoby**
  - [ ] Odhad sily: Slider 1-10 + dynamický Label zobrazuje hodnotu
  - [ ] Zásoby (TextField, double)
- [ ] **Sekcia 3: Rámiky a plod**
  - [ ] Celkový počet rámikov (TextField, int)
  - [ ] Plodové rámiky (TextField, int)
  - [ ] Peľové rámiky (TextField, int)
  - [ ] Zapečatený plod dm (TextField, int)
  - [ ] Nezapečatený plod dm (TextField, int)
- [ ] **Sekcia 4: Matka**
  - [ ] Matka videná (CheckBox)
  - [ ] Poznámka o matke (TextField)
- [ ] **Sekcia 5: Varroa**
  - [ ] Varroa prítomná (CheckBox)
  - [ ] Počet varry (TextField, int) - **povolený len ak je varroa zaškrtnutá**
  - [ ] Otestovať: Nezaškrtnúť varroa → pole počet varry je disabled
  - [ ] Zaškrtnúť varroa → pole sa povolí
- [ ] **Sekcia 6: Správanie**
  - [ ] Agresivita: Slider 1-5 + dynamický Label
  - [ ] Správanie (TextField)
- [ ] **Sekcia 7: Poznámky**
  - [ ] TextArea (5 riadkov, wrap text)
- [ ] Všetky slidery fungujú a aktualizujú Label
- [ ] Validácia číselných polí
- [ ] Uloženie: všetkých 23 polí sa zapíše do databázy
- [ ] Status: "Prehliadka úspešne vytvorená"

### Úprava Prehliadky
- [ ] Vybrať prehliadku v tabuľke
- [ ] Tlačidlo "Zobraziť" (bývalé "Upraviť")
- [ ] Dialóg s predvyplnenými hodnotami
- [ ] Všetkých 23 polí má správne hodnoty
- [ ] Sliders sú na správnej pozícii
- [ ] CheckBoxy majú správny stav
- [ ] Varroa binding funguje pri úprave
- [ ] Uloženie zmien
- [ ] Status: "Prehliadka úspešne aktualizovaná"

---

## 📊 TAXÁCIE (Master-Detail Formulár)

### Vytvorenie Novej Taxácie
- [ ] Tlačidlo "Nová taxácia" otvorí dialóg
- [ ] **Horná časť - Hlavička taxácie:**
  - [ ] Dátum + čas (DatePicker + HH:MM)
  - [ ] Teplota (TextField, double)
  - [ ] Celkový počet rámikov (TextField, int)
  - [ ] Zásoby (TextField, double)
  - [ ] Poznámky (TextArea)
- [ ] **Dolná časť - Rámiky:**
  - [ ] Tabuľka s 5 stĺpcami: Pozícia, Typ, Zapečatený (dm), Nezapečatený (dm), Peľ (dm)
  - [ ] ToolBar s tlačidlami: Pridať rámik, Upraviť rámik, Zmazať rámik
  - [ ] Tlačidlá "Upraviť" a "Zmazať" sú disabled kým nie je vybraný rámik

### Pridanie Rámika do Taxácie
- [ ] Tlačidlo "Pridať rámik" otvorí dialóg rámika
- [ ] Dialóg rámika má 15 polí:
  - [ ] Pozícia (TextField, int) - povinné
  - [ ] Typ rámika (ComboBox: PLODOVÝ, MEDOVÝ, OSNOVA, VYSTAVENÝ, TMAVÝ) - povinné
  - [ ] Zavíčkovaný plod dm (TextField, int)
  - [ ] Otvorený plod dm (TextField, int)
  - [ ] Peľ dm (TextField, int)
  - [ ] Zavíčkované zásoby dm (TextField, int) - **NOVÉ POLE**
  - [ ] Nezavíčkované zásoby dm (TextField, int) - **NOVÉ POLE**
  - [ ] Rok rámika (TextField, int)
  - [ ] Stavebný rámik (CheckBox)
  - [ ] Má matku (CheckBox)
  - [ ] Má klietku (CheckBox)
  - [ ] Opačnenec (CheckBox)
  - [ ] Poznámky (TextArea)
- [ ] Validácia: pozícia a typ sú povinné
- [ ] Po uložení sa rámik pridá do tabuľky v dialógu taxácie
- [ ] Typ rámika zobrazuje slovenskú podobu
- [ ] **NOVÉ:** Zavíčkované a nezavíčkované zásoby sa správne ukladajú do DB

### Úprava Rámika v Taxácii
- [ ] Vybrať rámik v tabuľke
- [ ] Tlačidlo "Upraviť rámik" sa povolí
- [ ] Dialóg s predvyplnenými hodnotami rámika
- [ ] Zmeny sa uložia a tabuľka sa aktualizuje

### Mazanie Rámika z Taxácie
- [ ] Vybrať rámik
- [ ] "Zmazať rámik" → potvrdzovací dialóg
- [ ] Rámik sa odstráni z tabuľky (ešte nie z DB, len lokálne)

### Uloženie Taxácie s Rámikmi
- [ ] Vytvoriť taxáciu s hlavičkou
- [ ] Pridať 3-5 rámikov
- [ ] Kliknúť "Uložiť"
- [ ] **Kritické:** Hlavička taxácie + všetky rámiky sa uložia v jednej transakcii
- [ ] Status: "Taxácia úspešne vytvorená"
- [ ] Overiť v databáze:
  - [ ] SELECT * FROM taxations WHERE id = 'test-id'
  - [ ] SELECT * FROM taxation_frames WHERE taxationId = 'test-id'
  - [ ] Počet rámikov v DB = počet pridaných rámikov

### Zobrazenie/Úprava Existujúcej Taxácie
- [ ] Vybrať taxáciu v tabuľke
- [ ] Tlačidlo "Zobraziť/Upraviť" sa povolí
- [ ] Dialóg sa otvorí s hlavičkou + načítanými rámikmi z DB
- [ ] Tabuľka rámikov zobrazuje všetky rámiky
- [ ] Možnosť pridať/upraviť/zmazať rámiky
- [ ] Uloženie zmien

### Agregované Dáta v Hlavnej Tabuľke
- [ ] **NOVÉ:** Hlavná tabuľka taxácií zobrazuje 9 stĺpcov:
  - [ ] Dátum
  - [ ] Teplota (°C)
  - [ ] Počet rámikov
  - [ ] Zásoby (kg)
  - [ ] Peľ (dm) - súčet zo všetkých rámikov
  - [ ] Zav. zásoby (dm) - súčet zavíčkovaných zásob zo všetkých rámikov
  - [ ] Nezav. zásoby (dm) - súčet nezavíčkovaných zásob zo všetkých rámikov
  - [ ] Zav. plod (dm) - súčet zavíčkovaného plodu zo všetkých rámikov
  - [ ] Otv. plod (dm) - súčet otvoreného plodu zo všetkých rámikov
- [ ] Po uložení taxácie s rámikmi sa súčty automaticky vypočítajú a zobrazia v tabuľke
- [ ] Overiť výpočet: ak pridáte 3 rámiky s peľom 10, 20, 30 dm → tabuľka zobrazí "60" v stĺpci "Peľ (dm)"

### Mazanie Taxácie s Kaskádou
- [ ] Vybrať taxáciu
- [ ] "Zmazať" → potvrdzovací dialóg s upozornením na kaskádu
- [ ] Text obsahuje: "Všetky rámiky budú tiež zmazané."
- [ ] Potvrdenie zmaže taxáciu
- [ ] **Kritické:** Overiť kaskádové mazanie v DB:
  - [ ] SELECT * FROM taxation_frames WHERE taxationId = 'zmazane-id'
  - [ ] Výsledok musí byť prázdny (0 riadkov)
- [ ] Status: "Taxácia úspešne zmazaná"

---

## 🔗 INTEGRAČNÉ TESTY (Prepojenie Tabov)

### Tok: Včelnice → Úle → Prehliadky/Krmenie/Taxácie
- [ ] Spustiť aplikáciu
- [ ] Tab "Úle" je disabled
- [ ] Tab "Včelnice": vybrať včelnicu
- [ ] Kliknúť "Zobraziť úle"
- [ ] Tab "Úle" sa povolí a stane sa aktívnym
- [ ] Taby "Prehliadky", "Krmenie", "Taxácie" sú stále disabled
- [ ] Tab "Úle": vybrať úľ
- [ ] Kliknúť "Zobraziť prehliadky"
- [ ] Taby "Prehliadky", "Krmenie", "Taxácie" sa povolia
- [ ] Tab "Prehliadky" sa stane aktívnym
- [ ] Prepnúť na tab "Krmenie" → zobrazí krmenia pre vybraný úľ
- [ ] Prepnúť na tab "Taxácie" → zobrazí taxácie pre vybraný úľ
- [ ] Prepnúť na tab "Prehliadky" → zobrazí prehliadky pre vybraný úľ

### Zmena Výberu Úľa
- [ ] Vybrať úľ A → prehliadky/krmenie/taxácie pre úľ A
- [ ] Prepnúť späť na tab "Úle"
- [ ] Vybrať úľ B
- [ ] Kliknúť "Zobraziť prehliadky"
- [ ] Tab "Prehliadky" zobrazuje dáta pre úľ B (nie úľ A)
- [ ] Tab "Krmenie" zobrazuje dáta pre úľ B
- [ ] Tab "Taxácie" zobrazuje dáta pre úľ B

---

## 💾 DATABÁZOVÁ PERZISTENCIA

### Overenie Uloženia Kalendárových Udalostí
```sql
-- Po vytvorení udalosti
SELECT * FROM calendar_events ORDER BY eventDate DESC LIMIT 5;
-- Overiť: title, description, eventType, hiveId, apiaryId, completed, notes, eventDate
```

### Overenie Uloženia Krmenia
```sql
-- Po vytvorení krmenia
SELECT * FROM feedings WHERE hiveId = 'test-hive-id' ORDER BY feedingDate DESC;
-- Overiť: feedType, amountKg, weightBefore, weightAfter, notes
```

### Overenie Uloženia Prehliadky (23 polí)
```sql
-- Po vytvorení prehliadky
SELECT * FROM inspections WHERE hiveId = 'test-hive-id' ORDER BY inspectionDate DESC LIMIT 1;
-- Overiť všetkých 23 polí vrátane:
-- temperature, strengthEstimate, foodStoresKg, broodFrames, totalFrames,
-- pollenFrames, cappedBroodDm, uncappedBroodDm, queenSeen, queenNote,
-- varroa, varroaCount, aggression, behavior, notes
```

### Overenie Uloženia Taxácie s Rámikmi
```sql
-- Po vytvorení taxácie
SELECT * FROM taxations WHERE hiveId = 'test-hive-id' ORDER BY taxationDate DESC LIMIT 1;
-- Poznamenať si taxationId

-- Overiť rámiky
SELECT * FROM taxation_frames WHERE taxationId = 'taxationId-z-hore';
-- Overiť: position, frameType, cappedBroodDm, uncappedBroodDm, pollenDm,
-- frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes
```

### Overenie Kaskádového Mazania
```sql
-- Pred zmazaním taxácie
SELECT COUNT(*) FROM taxation_frames WHERE taxationId = 'test-taxation-id';
-- Poznamenať si počet (napr. 5)

-- Zmazať taxáciu cez UI

-- Po zmazaní
SELECT COUNT(*) FROM taxation_frames WHERE taxationId = 'test-taxation-id';
-- MUSÍ byť 0 (kaskáda funguje)
```

---

## 🎨 UI/UX KONTROLY

### Responzívnosť
- [ ] Zmena veľkosti okna: všetky komponenty sa správne prispôsobia
- [ ] TableView: stĺpce sa proporcionálne roztiahnu (CONSTRAINED_RESIZE_POLICY)
- [ ] Dialógy: ScrollPane funguje pri malej výške okna

### Validačné Chyby
- [ ] Prázdny názov udalosti → "Názov je povinný"
- [ ] Neplatná hodina (napr. 25) → "Hodina musí byť medzi 0 a 23"
- [ ] Neplatná minúta (napr. 70) → "Minúta musí byť medzi 0 a 59"
- [ ] Neplatná teplota (napr. "abc") → "Teplota musí byť číslo"
- [ ] Množstvo <= 0 pri krmení → "Množstvo musí byť väčšie ako 0"
- [ ] Chybové dialógy majú typ ERROR a červený text

### Status Bar Správy
- [ ] Zelené správy pri úspechu (napr. "Udalosť úspešne vytvorená")
- [ ] Červené správy pri chybe (napr. "Chyba: ...")
- [ ] Čierne správy pri načítaní (napr. "5 udalostí", "10 kŕmení")

### ComboBox Načítanie
- [ ] ComboBox včelníc: načítava sa z databázy, nie hardcoded
- [ ] ComboBox úľov: načítava sa na základe vybranej včelnice
- [ ] ComboBox má možnosť "(Žiadna včelnica)" / "(Žiadny úľ)"
- [ ] Enum ComboBoxx majú slovenské popisky, nie anglické konštanty

---

## 🔧 CHYBOVÉ STAVY A RECOVERY

### Chýbajúca Databáza
- [ ] Ak chýba database.db → aplikácia vytvorí novú
- [ ] Žiadny crash pri prvom spustení

### Prázdne Tabuľky
- [ ] Prázdna tabuľka kalendára → status "0 udalostí"
- [ ] Prázdna tabuľka krmení → status "0 kŕmení"
- [ ] Žiadne NullPointerException

### Zlyhanie Databázovej Operácie
- [ ] Simulovať chybu (napr. lock databázy)
- [ ] Aplikácia zobrazí červenú chybovú správu
- [ ] Aplikácia nespadne

### Nevalidné Dáta v Databáze
- [ ] Timestamp = 0 → DateTimeConverter vracia null, žiadny crash
- [ ] Null hodnoty v stringoch → zobrazuje prázdny string

---

## 🚦 REGRESNÉ TESTY (Po Každej Zmene)

Tieto testy spustiť vždy po úprave kódu:

- [ ] Gradle build: `gradle desktop:build` → úspech
- [ ] Unit testy: `gradle desktop:test` → 41/41 passed
- [ ] Aplikácia sa spustí: `gradle desktop:run` → bez chýb
- [ ] Základný tok: Včelnice → Úle → Prehliadky funguje
- [ ] Vytvorenie 1 záznamu v každej funkcii (kalendár, krmenie, prehliadka, taxácia)
- [ ] Žiadne exception v konzole počas práce

---

## 📝 POZNÁMKY A NÁJDENÉ PROBLÉMY

### Formát Poznámok:
```
[Dátum] [⚠/🐛/✅] [Funkcia] Popis problému
- Reprodukcia: Kroky na zopakovanie
- Očakávané: Čo by sa malo stať
- Aktuálne: Čo sa stane
- Priorita: Kritická/Vysoká/Nízka
- Status: [✅ OPRAVENÉ / ⏳ V PROCESE / 🔴 NETRIEDENÉ]
```

### Nájdené Problémy:

```
[2026-02-13] [🐛] [Taxácie] Nová taxácia sa nedá vytvoriť - NULL taxationId pre rámiky
- Reprodukcia: Vytvoriť novú taxáciu, pridať rámik, uložiť
- Očakávané: Taxácia + rámiky sa uložia do DB
- Aktuálne: Error - rámiky majú taxationId = null
- Priorita: Kritická
- Príčina: TaxationDialog negeneroval ID pre novú taxáciu, rámiky dostali null
- Riešenie: Pridané pole temporaryTaxationId, generuje sa UUID v konštruktore
- Status: ✅ OPRAVENÉ (2026-02-13)
```

```
[2026-02-13] [🐛] [Kalendár] Nový event sa po pridaní nezobrazí v tabuľke
- Reprodukcia: Vytvoriť event s ľubovoľným dátumom, uložiť
- Očakávané: Event sa zobrazí v tabuľke
- Aktuálne: Event je v DB, ale nezobrazí sa v tabuľke (iba budúce eventy viditeľné)
- Priorita: Vysoká
- Príčina: loadUpcomingEvents() načítava iba eventy s eventDate >= currentDate a completed = 0
- Riešenie: Pridaná metóda loadAllEvents() a getAll() v DAO, ViewModel teraz načítava všetky eventy
- Status: ✅ OPRAVENÉ (2026-02-13)
```

```
[2026-02-13] [🐛] [Taxácie] "No Controller specified" chyba pri otvorení formulára
- Reprodukcia: Vytvoriť novú taxáciu → Error pri načítaní formulára
- Očakávané: Dialóg sa otvorí správne
- Aktuálne: JavaFX Error: "No Controller specified" v taxation_dialog.fxml:57
- Priorita: Kritická
- Príčina: FXML mal onAction="#handleAddFrame" atribúty, ale používal sa programatický prístup
- Riešenie: Odstránené onAction z FXML, pridané setOnAction() v TaxationDialog.java
- Status: ✅ OPRAVENÉ (2026-02-13)
```

```
[2026-02-13] [🐛] [Taxácie] "addFrameButton je null!" - dialóg sa nezobrazil
- Reprodukcia: Kliknúť "Nová taxácia" → žiadny popup
- Očakávané: Dialóg sa zobrazí
- Aktuálne: RuntimeException: addFrameButton je null!
- Priorita: Kritická
- Príčina: vbox.lookup("#addFrameButton") nenašlo tlačidlá vnorené v ToolBar
- Riešenie: Manuálne prechádzanie VBox children, hľadanie ToolBar a iterácia cez toolbar.getItems()
- Status: ✅ OPRAVENÉ (2026-02-13)
```

```
[2026-02-13] [⚠] [UX] Povinné polia nie sú vizuálne označené
- Reprodukcia: Otvoriť ľubovoľný formulár
- Očakávané: Povinné polia sú označené červenou * pred názvom
- Aktuálne: Všetky polia vyzerajú rovnako
- Priorita: Stredná
- Riešenie: Pridané HBox s červenou Label "*" pred názov povinného poľa vo všetkých FXML formulároch
- Upravené súbory:
  - calendar_event_dialog.fxml (Názov*, Dátum*, Typ*)
  - feeding_dialog.fxml (Dátum*, Typ krmiva*, Množstvo*)
  - inspection_dialog.fxml (Dátum*)
  - taxation_dialog.fxml (Dátum*)
  - taxation_frame_dialog.fxml (Pozícia*, Typ rámika*)
- Status: ✅ OPRAVENÉ (2026-02-13)
```

---

## ✅ POTVRDENIE KOMPLETNOSTI

Po dokončení VŠETKÝCH testov:

- [ ] Všetky checkboxy v tomto dokumente sú [x]
- [ ] Žiadne [⚠] ani [🐛] v sekcii poznámok
- [ ] Databáza obsahuje testovacie dáta vo všetkých tabuľkách
- [ ] Aplikácia beží stabilne minimálne 30 minút bez crashu
- [ ] Schválené vedúcim projektu / QA tímom

**Tester:** _________________
**Dátum:** _________________
**Schválil:** _________________

---

## 📌 PRÍLOHY

### A. Testovacie Dáta
```sql
-- Vložiť testovaciu včelnicu
INSERT INTO apiaries (id, name, location, latitude, longitude)
VALUES ('test-apiary-1', 'Testovacia Včelnica', 'Bratislava', 48.1486, 17.1077);

-- Vložiť testovací úľ
INSERT INTO hives (id, apiaryId, name, type, queenYear)
VALUES ('test-hive-1', 'test-apiary-1', 'Testovací Úľ 1', 'LANGSTROTH', 2024);
```

### B. SQL Dopyty na Verifikáciu
```sql
-- Počet záznamov v každej tabuľke
SELECT 'calendar_events' as table_name, COUNT(*) as count FROM calendar_events
UNION ALL
SELECT 'feedings', COUNT(*) FROM feedings
UNION ALL
SELECT 'inspections', COUNT(*) FROM inspections
UNION ALL
SELECT 'taxations', COUNT(*) FROM taxations
UNION ALL
SELECT 'taxation_frames', COUNT(*) FROM taxation_frames;
```

---

**Koniec Kontrolného Zoznamu**
*Tento dokument bude priebežne aktualizovaný s novými funkciami.*
