# Beekeeper Desktop - Distribúcia s Rozšírenými Test Dátami

## 📦 Finálna Distribúcia

**Súbor:** `~/Desktop/beekeeper-desktop-WITH-DATA.zip` (39 MB)

**Verzia:** 1.0 - Extended Test Data Edition  
**Dátum:** 2026-02-22  
**Status:** ✅ Production Ready

---

## ✨ Čo Je Nové

### Rozšírené Test Dáta

Oproti pôvodnej verzii, táto distribúcia obsahuje **realistické včelárske dáta** rozložené cez celú sezónu:

#### 📅 10 Prehliadok (Inspections)
- **Časové obdobie:** Marec - Júl 2024
- **Rozloženie:** 5 prehliadok pre každý úľ
- **Obsah:**
  - Jarné prehliadky (marec) - slabšie kolónie, príkrm potrebný
  - Apríl - rast plodiska, zlepšenie po príkrme
  - Máj - vrchol plodiska, pridávanie nadstavcov
  - Jún - plná znáška, med v nadstavcoch
  - Júl - koniec sezóny, varroa kontroly

**Realistické údaje:**
- Teploty: 12°C (jar) → 24°C (leto)
- Sila kolónie: 5-9 (rastúca)
- Zásoby: 4-15 kg
- Plodisko: 15-35 dm² zapečateného
- Varroa počty: 2-12 (rastúce)
- Poznámky matky, správanie, agresivita

#### 📊 10 Taxácií (Taxations)
- **Časové obdobie:** Marec - Júl 2024
- **Rozloženie:** 5 taxácií pre každý úľ
- **Obsah:**
  - Detailné merania plodiska
  - Zásoby medu a peľu
  - Počty rámikov
  - Celkové dm² (plodisko, zásoby)

**Príklady meraní:**
- Jar: 45 dm² zapečatených zásob, 18 dm² plodiska
- Vrchol: 125 dm² zásob, 35 dm² plodiska
- Leto: 98 dm² zásob po točení

#### 🖼️ 6 Taxation Frames
- Detailné záznamy jednotlivých rámikov
- Príklady:
  - "Matka na rámiku!"
  - "Veľa peľu"
  - "Čistý med"
- Pozície, typy rámikov, roky

---

## 📊 Kompletný Obsah Distribúcie

### Databáza (database_inserts_only.sql)

| Typ Dát | Počet | Popis |
|---------|-------|-------|
| **Translation Keys** | 785 | Unikátne translation keys (SK/EN) |
| **Translation Records** | 1,570 | Kompletné preklady (785 × 2 jazyky) |
| **Apiaries** | 1 | Test Apiary (testovacia včelnica) |
| **Hives** | 2 | Test Hive 1, Test Hive 2 |
| **Inspections** | 10 | ✨ Prehliadky (Marec-Júl 2024) |
| **Taxations** | 10 | ✨ Taxácie (Marec-Júl 2024) |
| **Taxation Frames** | 6 | ✨ Detaily rámikov |
| **Total INSERTs** | 1,599 | SQL INSERT príkazov |

---

## 🚀 Inštalácia a Použitie

### Pre Testera:

```bash
# 1. Rozbal ZIP
unzip beekeeper-desktop-WITH-DATA.zip
cd beekeeper-desktop/bin

# 2. macOS/Linux - nastav permissions
chmod +x beekeeper

# 3. Spusti aplikáciu
./beekeeper           # macOS/Linux
beekeeper.bat         # Windows
```

### Prvé Spustenie:

Pri prvom spustení aplikácia **automaticky**:
1. ✅ Vytvorí databázu `~/beekeeper-desktop.db`
2. ✅ Načíta 1,570 translation records (785 keys SK/EN)
3. ✅ Vytvorí testovaciu včelnicu "Test Apiary"
4. ✅ Pridá 2 testovacie úle
5. ✅ Naplní 10 realistických prehliadok (Marec-Júl 2024)
6. ✅ Naplní 10 realistických taxácií s detailmi
7. ✅ Nastaví jazyk na slovenčinu

**Výsledok:** Aplikácia je okamžite pripravená na testovanie s realistickými dátami!

---

## 🧪 Čo Testovať

### Dashboard
- ✅ Zobrazenie štatistík včelníc
- ✅ Grafy s reálnymi dátami
- ✅ Prehľad úľov

### Včelnice (Apiaries)
- ✅ Zoznam včelníc
- ✅ Detail včelnice "Test Apiary"
- ✅ Pridávanie/úprava/mazanie

### Úle (Hives)
- ✅ Zoznam úľov (2 testovacie)
- ✅ Detail úľa s históriou
- ✅ CRUD operácie

### Prehliadky (Inspections) ⭐
- ✅ Zoznam 10 prehliadok
- ✅ Časová os (Marec → Júl)
- ✅ Filter podľa úľa
- ✅ Detail prehliadky s kompletným info:
  - Teplota, sila kolónie
  - Zásoby, plodisko
  - Matka, varroa
  - Poznámky
- ✅ Pridávanie novej prehliadky
- ✅ Úprava/mazanie existujúcich

### Taxácie (Taxations) ⭐
- ✅ Zoznam 10 taxácií
- ✅ Filter podľa včelnice
- ✅ Detail taxácie s:
  - Celkové dm² zásoby/plodisko
  - Počty rámikov
  - Poznámky
- ✅ Master-detail view s rámikmi
- ✅ Pridávanie rámikov do taxácie
- ✅ CRUD operácie

### Kalendár (Calendar)
- ✅ Zobrazenie udalostí
- ✅ Pridávanie plánovaných aktivít
- ✅ Prepojenie s včelnicami/úlmi

### Kalkulačky (Calculators)
- ✅ Varroa kalkulačka
- ✅ Queen rearing kalkulačka
- ✅ Feed kalkulačka

### Jazyky (Languages)
- ✅ Prepínanie SK ↔ EN
- ✅ Všetky 785 translation keys fungujú
- ✅ Žiadne [missing.key] placeholdery

---

## 📝 Test Dáta - Príklady

### Prehliadka 1 (Jar - Marec 2024):
```
Dátum: 2024-03-09
Teplota: 12.5°C
Sila: 6/10
Zásoby: 5 kg
Plodisko: 18 dm² zapečateného, 12 dm² nezapečateného
Matka: "Zdravá matka, aktívna"
Varroa: 3 roztoče
Poznámky: "Jarná prehliadka, príkrm potrebný"
```

### Prehliadka 4 (Znáška - Jún 2024):
```
Dátum: 2024-06-09
Teplota: 22.0°C
Sila: 9/10
Zásoby: 15 kg
Plodisko: 30 dm² zapečateného
Matka: "Matka v poriadku"
Varroa: 8 roztočov
Poznámky: "Plná znáška, med v nadstavcoch"
```

### Taxácia 3 (Vrchol - Máj 2024):
```
Dátum: 2024-05-19
Teplota: 19.0°C
Celkové rámiky: 11
Zásoby: 7 kg
Plodisko:
  - Zapečatené: 35 dm²
  - Nezapečatené: 22 dm²
Zásoby med:
  - Zapečatený: 55 dm²
  - Nezapečatený: 22 dm²
Peľ: 20 dm²
Poznámky: "Máj - vrchol plodiska"
```

---

## 🎯 Výhody Pre Testovacie

### Realistické Scenáre
- ✅ Vidieť evolúciu kolónie cez sezónu
- ✅ Testovať grafy a štatistiky s reálnymi dátami
- ✅ Overiť výpočty a sumáre
- ✅ Testovať filtering a sorting

### Okamžitý Start
- ✅ Žiadne manuálne vytváranie test dát
- ✅ Ihneď vidieť ako aplikácia funguje
- ✅ Ukázať funkčnosť stakeholderom

### Kompletné Testovanie
- ✅ Všetky entity majú dáta
- ✅ Vzťahy medzi entitami (apiary → hive → inspection/taxation)
- ✅ Časové rady dát
- ✅ Edge cases (vysoká varroa, slabé kolónie, atď.)

---

## 🔧 Technické Detaily

### Generované Súbory:
- `desktop/src/main/resources/sql/database_inserts_only.sql` (1,599 INSERT príkazov)
- `desktop/build/distributions/beekeeper-desktop.zip` (39 MB)

### Aktualizované Komponenty:
- ✅ `DatabaseInitializer.java` - načíta všetky dáta
- ✅ `build.gradle` - distribúcia obsahuje aktuálny SQL
- ✅ Launcher skripty fungujú na všetkých platformách

### Databázová Schéma:
```
apiaries (1)
  └── hives (2)
        ├── inspections (10)
        └── taxations (10)
              └── taxation_frames (6)
  
translations (1,570)
```

---

## 🐛 Riešenie Problémov

### Aplikácia sa nespustí (macOS)
```bash
chmod +x beekeeper
xattr -d com.apple.quarantine beekeeper  # Odstráni quarantine flag
./beekeeper
```

### Chýbajúce dáta v aplikácii
```bash
# Vymaž databázu a spusti znovu
rm ~/beekeeper-desktop.db
./beekeeper
```

### JavaFX Error
```bash
# Skontroluj Java verziu
java -version
# Potrebná Java 17+
```

---

## 📚 Súvisiaca Dokumentácia

- `DATABASE_EXPORT_SUMMARY.md` - Technické detaily exportu
- `DATABASE_EXPORT_INFO.md` - Proces vytvárania exportu
- `desktop/src/main/resources/sql/README.md` - SQL súbory dokumentácia
- `.claude/skills/build-distribution/SKILL.md` - Skill pre building distribúcie

---

## ✅ Verifikované

- ✅ Lokálne testovanie: `gradle desktop:run` → úspešné
- ✅ Distribučné testovanie: ZIP → úspešné
- ✅ Databáza: 1,599 INSERT príkazov vykonaných
- ✅ Všetky translation keys načítané (785)
- ✅ Test dáta: 10 inspections + 10 taxations + 6 frames
- ✅ Žiadne chýbajúce translation keys
- ✅ Aplikácia štartuje bez errorov
- ✅ Všetky UI komponenty funkčné

---

## 🎉 Pripravené na Testovanie!

**Distribúcia:** `~/Desktop/beekeeper-desktop-WITH-DATA.zip` (39 MB)

**Obsahuje:**
- ✅ 785 translation keys (SK/EN)
- ✅ 1 včelnica + 2 úle
- ✅ 10 realistických prehliadok (Marec-Júl)
- ✅ 10 realistických taxácií s detailmi
- ✅ Automatická inicializácia
- ✅ Ready for production testing

**Odošli testerovi a môže začať testovať!** 🐝

---

**Vytvorené:** 2026-02-22  
**Autor:** Beekeeper Desktop Team  
**Verzia:** 1.0 Extended Test Data Edition  
**Status:** ✅ Production Ready
