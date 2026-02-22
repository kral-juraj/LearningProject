# Databáza - Lokácia a Správa

## 📂 Lokácia Databázy

### V Distribúcii (PORTABLE)

Pri spustení aplikácie z distribúcie sa databáza automaticky vytvorí v **data/** podadresári:

```
beekeeper-desktop/          ← Distribučný adresár
├── bin/
│   ├── beekeeper           ← Launcher
│   └── beekeeper.bat
├── lib/                    ← JAR súbory
├── sql/                    ← Init skripty
├── data/                   ← ✨ Automaticky vytvorený
│   └── beekeeper.db        ← Databáza tu
└── README.txt
```

**Výhody:**
- ✅ **Portable** - celá aplikácia vrátane dát je v jednom adresári
- ✅ **Zálohovanie** - jednoducho skopíruješ celý adresár
- ✅ **Multi-user** - každý užívateľ má vlastnú kópiu s vlastnými dátami
- ✅ **USB stick** - môžeš nosiť aplikáciu + dáta na USB
- ✅ **Žiadne konflikty** - dáta sú oddelené od systémových súborov

### Pri Vývoji (gradle desktop:run)

Pri spustení cez Gradle sa databáza vytvára v module adresári:

```
LearningProject/
└── desktop/
    └── data/
        └── beekeeper.db
```

---

## 🚀 Použitie

### Prvé Spustenie

```bash
cd beekeeper-desktop
./bin/beekeeper

# Automaticky sa vytvorí:
# - data/ adresár
# - data/beekeeper.db s 785 translation keys
# - 1 včelnica, 2 úle, 10 prehliadok, 10 taxácií
```

### Overenie Lokácie

Po spustení aplikácie pozri konzolu:
```
Created data directory: /path/to/beekeeper-desktop/data
Initializing database at: /path/to/beekeeper-desktop/data/beekeeper.db
```

---

## 💾 Zálohovanie

### Kompletná Záloha (Odporúčané)

Skopíruj celý distribučný adresár:
```bash
# Vytvor zálohu
cp -r beekeeper-desktop beekeeper-desktop-backup-2024-03-09

# Alebo ZIP
zip -r beekeeper-backup.zip beekeeper-desktop/
```

**Obsahuje:**
- ✅ Aplikáciu (bin/, lib/)
- ✅ Databázu (data/beekeeper.db)
- ✅ Všetky dáta (včelnice, úle, prehliadky, taxácie)

### Záloha Len Databázy

```bash
cd beekeeper-desktop
cp data/beekeeper.db data/beekeeper-backup-$(date +%Y%m%d).db
```

---

## 🔄 Obnovenie Zálohy

### Z Kompletnej Zálohy

```bash
# Obnov celý adresár
cp -r beekeeper-desktop-backup-2024-03-09 beekeeper-desktop
```

### Len Databázy

```bash
cd beekeeper-desktop/data
cp beekeeper-backup-20240309.db beekeeper.db
```

---

## 📦 Prenos Medzi Počítačmi

### Scénár 1: Preniesť Celú Aplikáciu + Dáta

```bash
# Na počítači A
zip -r beekeeper-complete.zip beekeeper-desktop/
# Presun ZIP na počítač B
# Rozbal a spusti
```

### Scénár 2: Preniesť Len Databázu

```bash
# Na počítači A
cd beekeeper-desktop/data
cp beekeeper.db /tmp/my-beekeeper-data.db

# Presun súbor na počítač B
# Na počítači B
cd beekeeper-desktop/data
cp /tmp/my-beekeeper-data.db beekeeper.db
```

---

## 🔧 Technické Detaily

### Implementácia (Main.java)

```java
// Use current working directory (where user extracted the app)
String currentDir = System.getProperty("user.dir");

// Create 'data' subdirectory if it doesn't exist
java.io.File dataDir = new java.io.File(currentDir, "data");
if (!dataDir.exists()) {
    dataDir.mkdirs();
}

// Database will be in: <distribution>/data/beekeeper.db
String dbPath = new java.io.File(dataDir, "beekeeper.db").getAbsolutePath();
DatabaseManager.initialize(dbPath);
```

---

## ✅ Výhody Novej Lokácie

| Aspekt | Stará Lokácia (home dir) | Nová Lokácia (data/) |
|--------|--------------------------|----------------------|
| **Portable** | ❌ Nie | ✅ Áno |
| **Zálohovanie** | ❌ Komplikované | ✅ Jednoduché |
| **Multi-user** | ❌ Zdieľaná DB | ✅ Každý má vlastnú |
| **USB Stick** | ❌ Nie | ✅ Áno |
| **Čisté oddelenie** | ❌ V home dir | ✅ Všetko v jednom |

---

**Vytvorené:** 2026-02-22
**Verzia:** 1.0 Portable Edition
**Status:** ✅ Production Ready
