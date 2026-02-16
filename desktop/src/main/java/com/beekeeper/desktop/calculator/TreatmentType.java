package com.beekeeper.desktop.calculator;

/**
 * Predefined Varroa treatment types with typical effectiveness values.
 * Based on research and practical beekeeping experience.
 */
public enum TreatmentType {
    // Chemical treatments
    THYMOL("Tymol", 0.70, "Éterický olej, organické ošetrenie",
            "Tymol je prírodný éterický olej z tymiánu. Odpára sa pri teplote 15-30°C a pôsobí 2-4 týždne.\n\n" +
            "Efektivita: 70-80%\n" +
            "Teplota: 15-30°C (optimálne 20-25°C)\n" +
            "Časovanie: Júl - September (po vytočení medu)\n" +
            "Plod: Môže byť prítomný\n\n" +
            "Výhody: Organický, žiadne rezíduá v mede\n" +
            "Nevýhody: Závislý od teploty, pomalé pôsobenie"),

    FORMIC_ACID("Kyselina mravčia", 0.90, "Vysoká efektivita, pôsobí aj v plode",
            "Kyselina mravčia je organická kyselina, ktorá preniká do zapečatených buniek.\n\n" +
            "Efektivita: 85-95%\n" +
            "Teplota: 12-25°C (nad 25°C riziko poškodenia včiel)\n" +
            "Časovanie: Júl - September (pred medsezónou)\n" +
            "Plod: Efektívna aj s plodom!\n\n" +
            "Výhody: Zabíja kliešte aj v bunke, organická\n" +
            "Nevýhody: Citlivá na teplotu, môže ovplyvniť matku"),

    OXALIC_ACID("Kyselina šťavelová", 0.95, "Najvyššia efektivita, len BEZ plodu",
            "Kyselina šťavelová je najúčinnejšie organické ošetrenie, ale funguje len na phoretické kliešte.\n\n" +
            "Efektivita: 90-99%\n" +
            "Teplota: Pod 10°C (zimné ošetrenie)\n" +
            "Časovanie: November - December (zima, BEZ plodu!)\n" +
            "Plod: MUSÍ byť bez plodu!\n\n" +
            "Aplikácia: Kvapkanie (5ml na medziúlečok) alebo odparenie\n" +
            "Výhody: Najvyššia efektivita, rýchle pôsobenie\n" +
            "Nevýhody: Funguje len keď nie je plod"),

    AMITRAZ("Amitraz", 0.92, "Syntetický prípravok, pásy",
            "Amitraz je syntetický akaricíd vo forme pásov umiestnených medzi rámikmi.\n\n" +
            "Efektivita: 90-95%\n" +
            "Teplota: Širší teplotný rozsah\n" +
            "Časovanie: August - September (po medsezóne)\n" +
            "Plod: Môže byť prítomný\n" +
            "Aplikácia: Pásy na 6-8 týždňov\n\n" +
            "Výhody: Spoľahlivý, dlhodobé pôsobenie\n" +
            "Nevýhody: Syntetický, riziko rezíduí, možná rezistencia"),

    FLUMETHRIN("Flumethrin (Bayvarol)", 0.90, "Syntetický prípravok, pásy",
            "Flumethrin je syntetický pyretroid vo forme kontaktných pásov.\n\n" +
            "Efektivita: 85-90%\n" +
            "Teplota: Širší teplotný rozsah\n" +
            "Časovanie: August - Október\n" +
            "Plod: Môže byť prítomný\n" +
            "Aplikácia: Pásy na 6 týždňov\n\n" +
            "Výhody: Jednoduché použitie, dlhodobé pôsobenie\n" +
            "Nevýhody: Syntetický, rastúca rezistencia kliešťov"),

    // Mechanical interventions (NO chemistry)
    DRONE_BROOD_REMOVAL("Vyrezanie trúdieho plodu", 0.70, "Biotechnická metóda bez chémie",
            "Vyrezávanie trúdieho plodu je najúčinnejšia biotechnická metóda. Kliešte preferujú trúdí plod 10x viac!\n\n" +
            "Efektivita: 60-70% pri pravidelnom vyrezávaní\n" +
            "Časovanie: Apríl - Júl (aktívne tvorenie trúdov)\n" +
            "Frekvencia: Každých 9-10 dní (pred zapečatením)\n\n" +
            "Postup:\n" +
            "1. Dajte do úľa prázdny rámik alebo lovovací rámik\n" +
            "2. Matka ho naplní trúdím plodom\n" +
            "3. Pred zapečatením (D+9-10) rámik vyriežte a zlikvidujte\n" +
            "4. Dajte nový prázdny rámik\n\n" +
            "Výhody: BEZ chémie, ekologické, znižuje rojivosť\n" +
            "Nevýhody: Pracné, vyžaduje pravidelnosť"),

    QUEEN_CAGING("Zaklietkovanie matky", 0.50, "Príprava na následné ošetrenie",
            "Zaklietkovanie matky je prípravná biotechnická metóda pred chemickým ošetrením.\n\n" +
            "Efektivita: 50% (príprava, nie liečba!)\n" +
            "Časovanie: Júl - August (pred medsezónou)\n" +
            "Trvanie: 21-24 dní (1 reprodukčný cyklus včely)\n\n" +
            "Postup:\n" +
            "1. Uzavrieť matku do klietky (nesmie klásť)\n" +
            "2. Počkať 21 dní - všetok plod vyliahne\n" +
            "3. Všetky kliešte sú na včelách (phoretické)\n" +
            "4. Aplikovať kyselinu šťavelová (95% účinnosť!)\n" +
            "5. Vypustiť matku\n\n" +
            "Výhody: Sprístupní všetky kliešte na ošetrenie\n" +
            "Nevýhody: Slabší národ (21 dní bez nových včiel), vyžaduje chemické ošetrenie"),

    BROOD_BREAK_BIO("Plodová prestávka (biotechnická)", 0.30, "Len prerušenie plodu, BEZ chémie",
            "Biotechnická plodová prestávka BEZ použitia chemických prípravkov.\n\n" +
            "Efektivita: 30-40% (len spomalenie rastu)\n" +
            "Časovanie: Júl - August\n" +
            "Trvanie: 21-24 dní\n\n" +
            "Postup:\n" +
            "1. Odstrániť matku alebo zakliетkovať\n" +
            "2. Všetok plod vyliahne za 21 dní\n" +
            "3. Kliešte sú phoretické, ale stále žijú\n" +
            "4. Vrátiť/vypustiť matku\n\n" +
            "Poznámka: Samotná prestávka nezabíja kliešte, len spomaľuje ich rast!\n\n" +
            "Výhody: BEZ chémie\n" +
            "Nevýhody: Nízka efektivita, slabší národ"),

    BROOD_BREAK_FULL("Úplné ošetrenie s plodovou prestávkou", 0.90, "Prestávka + kyselina šťavelová",
            "Kombinované ošetrenie: plodová prestávka + následná aplikácia kyseliny šťavelovej.\n\n" +
            "Efektivita: 90-95% (najúčinnejšie!)\n" +
            "Časovanie: Júl - August (prestávka) + November - December (kyselina)\n" +
            "Trvanie: 21-24 dní prestávka + ošetrenie\n\n" +
            "Postup:\n" +
            "1. Vyvolať plodovú prestávku (odstrániť/zakliетkovať matku)\n" +
            "2. Po 21 dňoch všetky kliešte sú phoretické\n" +
            "3. Aplikovať kyselinu šťavelová\n" +
            "4. Vrátiť/vypustiť matku alebo dať novú\n\n" +
            "Výhody: Najvyššia efektivita, organická kyselina\n" +
            "Nevýhody: Náročné, slabší národ, riziko straty matky"),

    // Passive biotechnical methods
    TRAP_COMB("Lovovacie rámky", 0.30, "Pasívna biotechnická metóda",
            "Lovovacie rámky (trap combs) sú rámiky s medzistienkami určené pre trúdí plod.\n\n" +
            "Efektivita: 25-35% pri pravidelnom vyrezávaní\n" +
            "Časovanie: Apríl - September (aktívna sezóna)\n" +
            "Frekvencia: Vyrezávať každých 10-12 dní\n\n" +
            "Postup:\n" +
            "1. Umiestniť lovovací rámik na okraj plástu\n" +
            "2. Matka ho naplní plodom (preferuje trúdí bunky)\n" +
            "3. Kliešte invadujú bunky\n" +
            "4. Pred zapečatením rámik vyriežte\n" +
            "5. Zlikvidovať plod, rámik vrátiť\n\n" +
            "Výhody: BEZ chémie, pasívne, nízka pracnosť\n" +
            "Nevýhody: Nižšia efektivita, vyžaduje pravidelnosť"),

    SCREENED_BOTTOM("Dno s mriežkou", 0.10, "Celoročná pasívna ochrana",
            "Dno úľa s mriežkou umožňuje opadnutým kliešťom prepadnúť von z úľa.\n\n" +
            "Efektivita: 10-20% (pasívne zníženie)\n" +
            "Časovanie: Celoročne\n" +
            "Bez zásahu včelára\n\n" +
            "Princíp:\n" +
            "- Kliešte prirodzene opadávajú z včiel\n" +
            "- Mriežka zabráni ich návratu do úľa\n" +
            "- Umožňuje aj monitorovanie prirodzeného opadu\n\n" +
            "Výhody: BEZ práce, BEZ chémie, celoročné\n" +
            "Nevýhody: Veľmi nízka efektivita, nie je liečba"),

    CUSTOM("Vlastné liečenie", 0.80, "Definujte vlastnú metódu",
            "Vlastná metóda liečenia s vlastnou efektivitou.\n\n" +
            "Použite pre:\n" +
            "- Experimentálne metódy\n" +
            "- Kombinované prístupy\n" +
            "- Lokálne špecifické prípravky\n\n" +
            "Zadajte názov, efektivitu a poznámku.");

    private final String displayName;
    private final double defaultEffectiveness;
    private final String description;
    private final String detailedInfo;

    TreatmentType(String displayName, double defaultEffectiveness, String description, String detailedInfo) {
        this.displayName = displayName;
        this.defaultEffectiveness = defaultEffectiveness;
        this.description = description;
        this.detailedInfo = detailedInfo;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getDefaultEffectiveness() {
        return defaultEffectiveness;
    }

    public String getDescription() {
        return description;
    }

    public String getDetailedInfo() {
        return detailedInfo;
    }

    /**
     * Get effectiveness as percentage (0-100).
     */
    public double getDefaultEffectivenessPercent() {
        return defaultEffectiveness * 100.0;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Find TreatmentType by display name.
     */
    public static TreatmentType fromDisplayName(String displayName) {
        for (TreatmentType type : values()) {
            if (type.displayName.equals(displayName)) {
                return type;
            }
        }
        return CUSTOM;
    }
}
