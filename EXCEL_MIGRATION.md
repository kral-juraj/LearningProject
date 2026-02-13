# Excel → SQLite Migration Guide

**Účel:** Jednorazový preklop historických dát z Excel súborov (2020-2025) do SQLite databázy
**Dátum:** 14. február 2025

---

## 📋 Prehľad

Tento dokument popisuje rôzne spôsoby, ako jednorazovo naimportovať historické dáta z tvojich Excel súborov do desktop aplikácie bez potreby implementovať import funkciu v UI.

---

## 🎯 Cieľ

Preklopiť dáta z Excel súborov do SQLite databázy:
- **Excel súbory:** Úľový denník 2020-2025 (5 súborov)
- **Cieľová DB:** `~/beekeeper-desktop.db`
- **Tabuľky:** apiaries, hives, inspections, feedings, taxations, taxation_frames, calendar_events

---

## 🛠️ Riešenie 1: Python Skript (ODPORÚČANÉ)

**Výhody:**
- Najjednoduchšie a najrýchlejšie
- Pandas = mocný nástroj na prácu s Excelom
- Žiadne dependencies navyše v Java projekte
- Môžeš debugovať a testovať po častiach

**Nevýhody:**
- Potrebuješ mať Python nainštalovaný

### 1.1 Inštalácia

```bash
# Nainštaluj Python dependencies
pip install pandas openpyxl sqlite3
```

### 1.2 Python Skript

Vytvor súbor `excel_to_db.py`:

```python
import pandas as pd
import sqlite3
from datetime import datetime
import uuid

# Pripoj sa na SQLite databázu
db_path = '/Users/juraj.kral/beekeeper-desktop.db'
conn = sqlite3.connect(db_path)
cursor = conn.cursor()

# Helper funkcia na generovanie UUID
def generate_id():
    return str(uuid.uuid4())

# Helper funkcia na konverziu dátumu na timestamp (ms)
def date_to_timestamp(date_str):
    """Konvertuje '14.02.2025' na timestamp v ms"""
    if pd.isna(date_str):
        return None
    try:
        dt = datetime.strptime(str(date_str), '%d.%m.%Y')
        return int(dt.timestamp() * 1000)
    except:
        return None

# Helper funkcia na konverziu datetime na timestamp
def datetime_to_timestamp(dt):
    """Konvertuje pandas datetime na timestamp v ms"""
    if pd.isna(dt):
        return None
    return int(dt.timestamp() * 1000)

# 1. VČELNICE (Apiaries)
def import_apiaries(excel_file):
    """Import včelníc z Excelu"""
    # Predpokladám, že máš sheet "Včelnice" v Exceli
    df = pd.read_excel(excel_file, sheet_name='Vcielnice')

    for index, row in df.iterrows():
        apiary_id = generate_id()
        cursor.execute("""
            INSERT INTO apiaries (id, name, location, notes, createdAt, updatedAt)
            VALUES (?, ?, ?, ?, ?, ?)
        """, (
            apiary_id,
            row['Názov'],  # Upraviť podľa tvojich stĺpcov
            row['Lokalita'],
            row.get('Poznámky', ''),
            datetime_to_timestamp(datetime.now()),
            datetime_to_timestamp(datetime.now())
        ))
        print(f"✓ Včelnica: {row['Názov']}")

    conn.commit()

# 2. ÚLE (Hives)
def import_hives(excel_file, apiary_id):
    """Import úľov pre danú včelnicu"""
    df = pd.read_excel(excel_file, sheet_name='Ule')

    for index, row in df.iterrows():
        hive_id = generate_id()
        cursor.execute("""
            INSERT INTO hives (id, apiaryId, name, hiveType, queenYear, notes,
                             isActive, createdAt, updatedAt)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """, (
            hive_id,
            apiary_id,
            row['Názov'],  # napr. "U1", "L1"
            row.get('Typ', 'LANGSTROTH'),
            row.get('Rok matky', None),
            row.get('Poznámky', ''),
            1,  # active
            datetime_to_timestamp(datetime.now()),
            datetime_to_timestamp(datetime.now())
        ))
        print(f"✓ Úľ: {row['Názov']}")

    conn.commit()

# 3. PREHLIADKY (Inspections)
def import_inspections(excel_file, hive_id):
    """Import prehliadok z kalendára"""
    df = pd.read_excel(excel_file, sheet_name='Kalendar')

    for index, row in df.iterrows():
        if pd.isna(row.get('Dátum')):
            continue

        inspection_id = generate_id()
        cursor.execute("""
            INSERT INTO inspections (
                id, hiveId, inspectionDate, temperature, strengthEstimate,
                totalFrames, broodFrames, pollenFrames, cappedBroodDm,
                uncappedBroodDm, foodStoresKg, queenSeen, queenNote,
                varroa, varroaCount, aggression, behavior, notes,
                createdAt, updatedAt
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """, (
            inspection_id,
            hive_id,
            date_to_timestamp(row['Dátum']),
            row.get('Teplota', None),
            row.get('Sila', None),
            row.get('Rámiky celkom', None),
            row.get('Rámiky s plodom', None),
            row.get('Rámiky s peľom', None),
            row.get('Zavíčkovaný plod', None),
            row.get('Otvorený plod', None),
            row.get('Zásoby kg', None),
            1 if row.get('Matka videná') == 'áno' else 0,
            row.get('Poznámka matka', ''),
            1 if row.get('Varroa') == 'áno' else 0,
            row.get('Počet varroa', None),
            row.get('Agresivita', None),
            row.get('Správanie', ''),
            row.get('Poznámky', ''),
            datetime_to_timestamp(datetime.now()),
            datetime_to_timestamp(datetime.now())
        ))
        print(f"✓ Prehliadka: {row['Dátum']}")

    conn.commit()

# 4. KRMENIE (Feeding)
def import_feeding(excel_file, hive_id):
    """Import krmenia"""
    df = pd.read_excel(excel_file, sheet_name='Krmenie')

    for index, row in df.iterrows():
        if pd.isna(row.get('Dátum')):
            continue

        feeding_id = generate_id()

        # Mapovanie typu krmiva
        feed_type_map = {
            'Sirup 1:1': 'SYRUP_1_1',
            'Sirup 3:2': 'SYRUP_3_2',
            'Fondant': 'FONDANT',
            'Peľ': 'POLLEN_PATTY'
        }
        feed_type = feed_type_map.get(row.get('Typ'), 'SYRUP_1_1')

        cursor.execute("""
            INSERT INTO feedings (
                id, hiveId, feedingDate, feedType, weightBefore, weightAfter,
                amountKg, notes, createdAt, updatedAt
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """, (
            feeding_id,
            hive_id,
            date_to_timestamp(row['Dátum']),
            feed_type,
            row.get('Hmotnosť pred', None),
            row.get('Hmotnosť po', None),
            row.get('Množstvo kg', None),
            row.get('Poznámky', ''),
            datetime_to_timestamp(datetime.now()),
            datetime_to_timestamp(datetime.now())
        ))
        print(f"✓ Krmenie: {row['Dátum']} - {feed_type}")

    conn.commit()

# 5. TAXÁCIE (Taxations)
def import_taxations(excel_file, hive_id):
    """Import taxácií s rámikmi"""
    df = pd.read_excel(excel_file, sheet_name='Taxacie')

    for index, row in df.iterrows():
        if pd.isna(row.get('Dátum')):
            continue

        taxation_id = generate_id()

        # Hlavička taxácie
        cursor.execute("""
            INSERT INTO taxations (
                id, hiveId, taxationDate, temperature, totalFrames,
                foodStoresKg, notes, createdAt, updatedAt,
                totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm,
                totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """, (
            taxation_id,
            hive_id,
            date_to_timestamp(row['Dátum']),
            row.get('Teplota', None),
            row.get('Počet rámikov', None),
            row.get('Zásoby kg', None),
            row.get('Poznámky', ''),
            datetime_to_timestamp(datetime.now()),
            datetime_to_timestamp(datetime.now()),
            0, 0, 0, 0, 0, 0  # Agregované hodnoty (budú prepočítané)
        ))

        # Rámiky (ak máš detail v inom sheete)
        # import_taxation_frames(excel_file, taxation_id)

        print(f"✓ Taxácia: {row['Dátum']}")

    conn.commit()

# 6. KALENDÁR (Calendar Events)
def import_calendar_events(excel_file, apiary_id=None):
    """Import kalendárových udalostí"""
    df = pd.read_excel(excel_file, sheet_name='Kalendar_Udalosti')

    for index, row in df.iterrows():
        if pd.isna(row.get('Dátum')):
            continue

        event_id = generate_id()

        # Mapovanie typu udalosti
        event_type_map = {
            'Prehliadka': 'INSPECTION',
            'Krmenie': 'FEEDING',
            'Ošetrenie': 'TREATMENT',
            'Stokovanie': 'HARVEST',
            'Pripomienka': 'REMINDER'
        }
        event_type = event_type_map.get(row.get('Typ'), 'REMINDER')

        cursor.execute("""
            INSERT INTO calendar_events (
                id, eventDate, title, description, eventType,
                hiveId, apiaryId, completed, notes, createdAt, updatedAt
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """, (
            event_id,
            date_to_timestamp(row['Dátum']),
            row.get('Názov', ''),
            row.get('Popis', ''),
            event_type,
            None,  # alebo konkrétne hiveId ak máš
            apiary_id,
            0,  # not completed
            row.get('Poznámky', ''),
            datetime_to_timestamp(datetime.now()),
            datetime_to_timestamp(datetime.now())
        ))
        print(f"✓ Udalosť: {row['Dátum']} - {row['Názov']}")

    conn.commit()

# HLAVNÝ IMPORT WORKFLOW
def main():
    print("🐝 Excel → SQLite Migration")
    print("=" * 50)

    # Cesta k tvojmu Excel súboru
    excel_file = '/Users/juraj.kral/IdeaProjects/LearningProject/Úľový denník 2025.xlsx'

    try:
        # 1. Import včelníc
        print("\n📍 Importujem včelnice...")
        import_apiaries(excel_file)

        # Získaj ID prvej včelnice (pre demo)
        cursor.execute("SELECT id FROM apiaries LIMIT 1")
        apiary_id = cursor.fetchone()[0]

        # 2. Import úľov
        print("\n🏠 Importujem úle...")
        import_hives(excel_file, apiary_id)

        # Získaj ID prvého úľa (pre demo)
        cursor.execute("SELECT id FROM hives LIMIT 1")
        hive_id = cursor.fetchone()[0]

        # 3. Import prehliadok
        print("\n🔍 Importujem prehliadky...")
        import_inspections(excel_file, hive_id)

        # 4. Import krmenia
        print("\n🍯 Importujem krmenie...")
        import_feeding(excel_file, hive_id)

        # 5. Import taxácií
        print("\n📊 Importujem taxácie...")
        import_taxations(excel_file, hive_id)

        # 6. Import kalendárových udalostí
        print("\n📅 Importujem kalendár...")
        import_calendar_events(excel_file, apiary_id)

        print("\n" + "=" * 50)
        print("✅ Import dokončený!")

        # Zobraz štatistiky
        cursor.execute("SELECT COUNT(*) FROM apiaries")
        print(f"Včelnice: {cursor.fetchone()[0]}")

        cursor.execute("SELECT COUNT(*) FROM hives")
        print(f"Úle: {cursor.fetchone()[0]}")

        cursor.execute("SELECT COUNT(*) FROM inspections")
        print(f"Prehliadky: {cursor.fetchone()[0]}")

        cursor.execute("SELECT COUNT(*) FROM feedings")
        print(f"Krmenia: {cursor.fetchone()[0]}")

        cursor.execute("SELECT COUNT(*) FROM calendar_events")
        print(f"Udalosti: {cursor.fetchone()[0]}")

    except Exception as e:
        print(f"❌ Chyba: {e}")
        conn.rollback()
    finally:
        conn.close()

if __name__ == "__main__":
    main()
```

### 1.3 Spustenie

```bash
# Ulož skript ako excel_to_db.py
# Uprav názvy stĺpcov podľa tvojho Excelu
python excel_to_db.py
```

### 1.4 Prispôsobenie

**Upraviť pred spustením:**
1. Cestu k Excel súboru
2. Názvy sheetov (`sheet_name='...'`)
3. Názvy stĺpcov (`row['Názov']` → tvoj stĺpec)
4. Mapovanie hodnôt (typy krmiva, typy udalostí)

---

## 🛠️ Riešenie 2: Java Standalone Skript

**Výhody:**
- Používaš jazyk, ktorý už poznáš
- Môžeš použiť existujúce entity a DAOs
- Ľahko reusable

**Nevýhody:**
- Viac kódu ako v Pythone
- Potrebuješ Apache POI dependency

### 2.1 Vytvor samostatný module

V `settings.gradle` pridaj:
```gradle
include 'migration'
```

### 2.2 Migration Module

Vytvor `migration/build.gradle`:
```gradle
plugins {
    id 'java'
}

dependencies {
    implementation project(':shared')
    implementation 'org.apache.poi:poi:5.2.5'
    implementation 'org.apache.poi:poi-ooxml:5.2.5'
    implementation 'org.xerial:sqlite-jdbc:3.45.1.0'
    implementation 'io.reactivex.rxjava2:rxjava:2.2.21'
}
```

### 2.3 Migration Main Class

```java
package com.beekeeper.migration;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.sql.*;
import java.util.UUID;

public class ExcelMigration {

    private Connection conn;

    public ExcelMigration(String dbPath) throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

    public void migrate(String excelFilePath) throws Exception {
        FileInputStream file = new FileInputStream(excelFilePath);
        Workbook workbook = new XSSFWorkbook(file);

        // 1. Včelnice
        Sheet apiariesSheet = workbook.getSheet("Vcielnice");
        String apiaryId = importApiaries(apiariesSheet);

        // 2. Úle
        Sheet hivesSheet = workbook.getSheet("Ule");
        String hiveId = importHives(hivesSheet, apiaryId);

        // 3. Prehliadky
        Sheet inspSheet = workbook.getSheet("Kalendar");
        importInspections(inspSheet, hiveId);

        // atď...

        workbook.close();
        file.close();
    }

    private String importApiaries(Sheet sheet) throws SQLException {
        String apiaryId = UUID.randomUUID().toString();

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header

            String sql = "INSERT INTO apiaries (id, name, location, notes, createdAt, updatedAt) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, apiaryId);
                stmt.setString(2, getCellValue(row.getCell(0)));
                stmt.setString(3, getCellValue(row.getCell(1)));
                stmt.setString(4, getCellValue(row.getCell(2)));
                stmt.setLong(5, System.currentTimeMillis());
                stmt.setLong(6, System.currentTimeMillis());
                stmt.executeUpdate();
            }
        }

        return apiaryId;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }

    public static void main(String[] args) {
        try {
            String dbPath = System.getProperty("user.home") + "/beekeeper-desktop.db";
            String excelPath = "/Users/juraj.kral/IdeaProjects/LearningProject/Úľový denník 2025.xlsx";

            ExcelMigration migration = new ExcelMigration(dbPath);
            migration.migrate(excelPath);

            System.out.println("✅ Migration complete!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### 2.4 Spustenie

```bash
gradle migration:run
```

---

## 🛠️ Riešenie 3: CSV Export + DB Browser

**Výhody:**
- Najjednoduchšie (GUI nástroj)
- Žiadne programovanie
- Visual control

**Nevýhody:**
- Manuálna práca
- Vhodné len pre malé množstvo dát

### 3.1 Postup

1. **Export z Excelu do CSV:**
   - Otvor Excel
   - File → Save As → CSV (Comma delimited)
   - Ulož každý sheet ako samostatný CSV

2. **Nainštaluj DB Browser for SQLite:**
   ```bash
   brew install --cask db-browser-for-sqlite
   ```

3. **Import CSV do SQLite:**
   - Otvor `~/beekeeper-desktop.db` v DB Browser
   - File → Import → Table from CSV file
   - Vyber CSV súbor
   - Mapuj stĺpce na tabuľku
   - Import

4. **Opakuj pre každú tabuľku**

---

## 🛠️ Riešenie 4: SQL INSERT Scripty

**Výhody:**
- Plná kontrola
- Môžeš editovať pred importom
- Reusable

**Nevýhody:**
- Najpomalšie (manuálne písanie)
- Error-prone

### 4.1 Vytvor SQL skript

```sql
-- insert_data.sql

-- Včelnice
INSERT INTO apiaries (id, name, location, notes, createdAt, updatedAt)
VALUES
('uuid-1', 'Včelnica Záhradka', 'Bratislava', '', 1707926400000, 1707926400000),
('uuid-2', 'Včelnica Kopec', 'Trnava', '', 1707926400000, 1707926400000);

-- Úle
INSERT INTO hives (id, apiaryId, name, hiveType, queenYear, notes, isActive, createdAt, updatedAt)
VALUES
('hive-1', 'uuid-1', 'U1', 'LANGSTROTH', 2024, '', 1, 1707926400000, 1707926400000),
('hive-2', 'uuid-1', 'L1', 'LANGSTROTH', 2023, '', 1, 1707926400000, 1707926400000);

-- Prehliadky
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, ...)
VALUES
('insp-1', 'hive-1', 1707926400000, 18.5, 8, ...);

-- atď...
```

### 4.2 Spustenie

```bash
sqlite3 ~/beekeeper-desktop.db < insert_data.sql
```

---

## 🎯 Odporúčanie

**Najlepšie riešenie: Python skript (Riešenie 1)**

**Prečo:**
- Najrýchlejšie na napísanie
- Pandas má výborné Excel parsing
- Ľahko debugovateľné
- Môžeš to spustiť viackrát s rôznymi Excel súbormi

**Workflow:**
1. Stiahni Python skript z tohto dokumentu
2. Uprav názvy sheetov a stĺpcov podľa tvojho Excelu
3. Spusti skript pre každý rok (2020-2025)
4. Otvor desktop app a over dáta

---

## 📝 Checklist Pre Migráciu

- [ ] Zálohuj aktuálnu databázu (`cp ~/beekeeper-desktop.db ~/beekeeper-backup.db`)
- [ ] Skontroluj štruktúru svojho Excelu (názvy sheetov, stĺpcov)
- [ ] Vyber riešenie (odporúčam Python)
- [ ] Uprav skript podľa svojich dát
- [ ] Testuj na jednom malom Excel súbore najprv
- [ ] Spusti migráciu pre všetky roky (2020-2025)
- [ ] Over dáta v desktop aplikácii
- [ ] Skontroluj foreign keys (apiaryId, hiveId)
- [ ] Skontroluj počty záznamov
- [ ] Zmaž migračný skript (alebo ulož do `scripts/`)

---

## ⚠️ Časté Problémy

### Problem 1: Dátumy
**Symptóm:** Nesprávne konvertované dátumy

**Riešenie:**
- Excel môže mať dátumy ako číslo (days since 1900)
- Použiť `pd.to_datetime()` v Pythone
- Alebo manuálne parsovať formát

### Problem 2: NULL hodnoty
**Symptóm:** `NOT NULL constraint failed`

**Riešenie:**
- Skontroluj ktoré polia sú povinné v SQL schéme
- Použiť default hodnoty: `row.get('Stĺpec', '')`

### Problem 3: Foreign Keys
**Symptóm:** `FOREIGN KEY constraint failed`

**Riešenie:**
- Najprv import apiaries, potom hives, potom inspections
- Použiť správne UUID z predchádzajúcich insertov

### Problem 4: Duplicate IDs
**Symptóm:** `UNIQUE constraint failed: apiaries.id`

**Riešenie:**
- Vždy generuj nové UUID pre každý záznam
- Alebo vymaž databázu pred importom: `rm ~/beekeeper-desktop.db`

---

## 🔗 Užitočné Linky

- **Pandas Docs:** https://pandas.pydata.org/docs/
- **Apache POI:** https://poi.apache.org/
- **SQLite Browser:** https://sqlitebrowser.org/
- **Python openpyxl:** https://openpyxl.readthedocs.io/

---

**Posledná aktualizácia:** 14. február 2025
**Status:** Odporúčanie: Python skript
