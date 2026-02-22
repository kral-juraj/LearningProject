================================================================================
  BEEKEEPER DESKTOP - Aplikacia na spravu vcelnic
  Verzia 1.0.0 - Finalna distribucia
================================================================================

OBSAH BALIKA
------------
beekeeper-desktop/
├── bin/
│   ├── beekeeper              (Spustenie pre macOS/Linux)
│   ├── beekeeper.bat          (Spustenie pre Windows)
│   ├── desktop                (Gradle skript - alternativne)
│   └── desktop.bat            (Gradle skript - alternativne)
├── lib/
│   └── *.jar                  (Aplikacia + zavislosti - 38 MB)
├── sql/
│   ├── 00_init_data.sql       (531 prekladov SK/EN)
│   └── 18_test_data.sql       (Testovacie data)
└── README.txt                 (Tento subor)


SYSTEMOVE POZIADAVKY
--------------------
- Java 17 alebo vyssia (https://adoptium.net/)
- Operacny system: Windows 10+, macOS 10.14+, Linux (Ubuntu 20.04+)
- RAM: 512 MB
- Miesto na disku: 200 MB


RYCHLA INSTALACIA
-----------------
1. Rozbalit ZIP archiv
2. Otvorit terminal/cmd v priecinku beekeeper-desktop/bin
3. Spustit:
   - macOS/Linux: ./beekeeper
   - Windows: beekeeper.bat

POZNAMKA: Na macOS treba najprv povolit spustenie:
   chmod +x beekeeper
   ./beekeeper


PRVE SPUSTENIE
--------------
Pri prvom spusteni aplikacia automaticky:
1. Vytvori databazu v ~/beekeeper-desktop.db
2. Nacita 531 prekladov (slovencina + anglictina)
3. Vytvori testovacie data:
   - 1 vcelnica "Test Apiary"
   - 2 testovaci ule
4. Nastavi jazyk na slovencinu


CO FUNGUJE
----------
✅ Sprava vcelnic (pridavanie, upravovanie, mazanie)
✅ Sprava ulov (stav, typ, poznamky)
✅ Prehliadky (zaznamy o prehliadkach)
✅ Krmenie (typy krmiva, mnozstvo)
✅ Taxacie (meracie ramceky)
✅ Kalendar udalosti
✅ Dashboard s prehlad om
✅ Kalkulacky (Varroa, matky, krmivo)
✅ Dvojjazycnost (SK/EN prepinanie v menu)
✅ Automaticka inicializacia databazy


ZAKLADNE POUZITIE
------------------
1. Hlavne menu:
   - Subor → Ukoncit
   - Jazyk → Slovencina (SK) / English (EN)
   - Napoveda → O aplikacii

2. Bocny panel (lavy):
   - Prehlad - Dashboard
   - Vcelnice - Zoznam vcelnic
   - Ule - Zoznam ulov
   - Prehliadky - Zaznamy
   - Krmenie - Zaznamy
   - Taxacie - Merania
   - Kalendar - Planovanie
   - Kalkulacky - Nastroje

3. Pridavanie zaznamov:
   - Kliknite na "Pridat..." v hornej casti
   - Vyplnte formular
   - Ulozit


ZALOHOVANIE DAT
---------------
Databaza: ~/beekeeper-desktop.db (164 KB)

Zaloha:
1. Zatvorte aplikaciu
2. Skopirujte beekeeper-desktop.db
3. Ulozit na bezpecne miesto

Obnovenie:
1. Zatvorte aplikaciu
2. Skopirujte zalohu spat do ~/ (prepiste)
3. Spustite aplikaciu


RIESENIE PROBLEMOV
------------------
Problem: "Java not found"
Riesenie: Instalujte Javu 17+ z https://adoptium.net/

Problem: macOS bezpecnostne upozornenie
Riesenie: Systemove nastavenia → Bezpecnost → Povolit
         Alebo: Pravy klik na beekeeper → Open

Problem: Aplikacia sa nespusti (JavaFX error)
Riesenie: Skontrolujte ze mate Javu 17 alebo vyssiu:
         java -version

Problem: Prazdna databaza
Riesenie: Vymazte ~/beekeeper-desktop.db a spustite znovu

Problem: Chybajuce preklady
Riesenie: Vymazte databazu, aplikacia ju znovu vytvori s prekladmi


TECHNICKE INFORMACIE
---------------------
- Aplikacia: JavaFX 21.0.2
- Databaza: SQLite 3.45.1
- Architektura: MVVM + RxJava2
- Preklady: 531 klucov (SK/EN)
- Vyvoj: Java 17
- Distribucia: 38 MB ZIP


ZMENY VO VERZII 1.0.0
----------------------
✅ Kompletna inicializacia databazy pri prvom spusteni
✅ Automaticke nacitanie 531 prekladov
✅ Automaticke vytvorenie testovacich dat
✅ Plna podpora SK/EN jazykov
✅ Funkčne launcher skripty pre Windows/macOS/Linux
✅ Optimalizovana velkost distribucie (38 MB)


KONTAKT
-------
Pre otazky a podporu:
GitHub: https://github.com/your-repo/beekeeper-app
Email: support@beekeeper-app.com


================================================================================
Dakujeme ze pouzivate Beekeeper Desktop!
Uspesne vcelarenie! 🐝
================================================================================
