package com.beekeeper.desktop.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Calculator for queen rearing timeline milestones.
 * Provides standard timeline based on start date.
 */
public class QueenRearingCalculator {

    /**
     * Calculates all milestones for queen rearing based on start date.
     *
     * @param startDate Start date in milliseconds (when queen cells are grafted)
     * @param method    Rearing method: "Štandardná", "Vytvorenie opačnenca", "Klietkovanie matky"
     * @return List of QueenMilestone objects with dates and descriptions
     */
    public static List<QueenMilestone> calculateMilestones(long startDate, String method) {
        if (method == null) {
            method = "Štandardná";
        }

        if (method.contains("opačnenc") || method.contains("opacnenc")) {
            return calculateOpacnenecMilestones(startDate);
        } else if (method.contains("Klietkov") || method.contains("Norsk")) {
            return calculateKlietkovanieMilestones(startDate);
        } else {
            return calculateStandardMilestones(startDate);
        }
    }

    /**
     * Štandardná metóda - pôvodný odchov matiek (základný timeline).
     */
    private static List<QueenMilestone> calculateStandardMilestones(long startDate) {
        List<QueenMilestone> milestones = new ArrayList<>();

        // Farby: #FFFFCC = AKCIA (včelár musí zasiahnuť), #FFFFFF = INFO (len sledovanie)

        // D+0 - AKCIA
        milestones.add(createMilestone(
                "Založenie matečníkov",
                0,
                startDate,
                "Prekladanie larvičiek do matečníkov. Ideálny vek larvičiek: 12-24 hodín.",
                "#FFFFCC" // AKCIA
        ));

        // D+5 - AKCIA
        milestones.add(createMilestone(
                "Priloženie mriežky",
                5,
                startDate,
                "Kontrola prijatia matečníkov. Priloženie materskej mriežky.",
                "#FFFFCC" // AKCIA
        ));

        // D+7 - AKCIA
        milestones.add(createMilestone(
                "Prehliadka plodu",
                7,
                startDate,
                "Kontrola matečníkov, odstránenie nekvalitných. Larvičky sa začínajú zavíčkovať.",
                "#FFFFCC" // AKCIA
        ));

        // D+10 - AKCIA
        milestones.add(createMilestone(
                "Prehliadka matečníkov",
                10,
                startDate,
                "Všetky matečníky zavíčkované. Kontrola správnosti zavíčkovania.",
                "#FFFFCC" // AKCIA
        ));

        // D+12 - INFO
        milestones.add(createMilestone(
                "Zavíčkovanie matečníkov",
                12,
                startDate,
                "Matky sa vyvíjajú v zavíčkovaných matečníkoch. Príprava oddielkov.",
                "#FFFFFF" // INFO
        ));

        // D+16 - AKCIA
        milestones.add(createMilestone(
                "Presadenie do oddielkov",
                16,
                startDate,
                "Matečníky tesne pred vyhryznutím. Presadenie do oddielkov alebo oplodniačikov.",
                "#FFFFCC" // AKCIA
        ));

        // D+21 - AKCIA
        milestones.add(createMilestone(
                "Kontrola kládky",
                21,
                startDate,
                "Kontrola kládky mladých matiek. Matky začínajú klásť vajíčka.",
                "#FFFFCC" // AKCIA
        ));

        // Already sorted chronologically by definition
        return milestones;
    }

    /**
     * Metóda: Vytvorenie opačnenca
     * Cieľ: Utlmiť rojovú náladu, vytvoriť novú rodinu, bezplodové obdobie na preliečenie,
     * maximalizácia lietavok pred znaskou.
     */
    private static List<QueenMilestone> calculateOpacnenecMilestones(long startDate) {
        List<QueenMilestone> milestones = new ArrayList<>();

        // Farby: #FFFFCC = AKCIA (včelár musí zasiahnuť), #FFFFFF = INFO (len sledovanie)

        // D+0 - AKCIA
        milestones.add(createMilestone(
                "Vytvorenie opačnenca",
                0,
                startDate,
                "Odobrať matku + 2-3 plodové plasty + 3 zásobné plasty so včelami. " +
                "V pôvodnom úli včely natiahnu núdzové matečníky.",
                "#FFFFCC" // AKCIA
        ));

        // D+5 - AKCIA
        milestones.add(createMilestone(
                "Vylamať zavíčkované matečníky",
                5,
                startDate,
                "Kontrola matečníkov, vylamať všetky okrem jedného najlepšieho.",
                "#FFFFCC" // AKCIA
        ));

        // D+7 - AKCIA
        milestones.add(createMilestone(
                "Zvoliť si len 1 matečník",
                7,
                startDate,
                "Finálna kontrola, nechať len jeden kvalitný matečník.",
                "#FFFFCC" // AKCIA
        ));

        // D+9 - AKCIA
        milestones.add(createMilestone(
                "Školkovanie matečníkov",
                9,
                startDate,
                "Kontrola stavu matečníkov, prípadné doškôlkovanie.",
                "#FFFFCC" // AKCIA
        ));

        // D+13 - INFO
        milestones.add(createMilestone(
                "Liahnutie matky",
                13,
                startDate,
                "Mladá matka sa vylíahla z matečníka.",
                "#FFFFFF" // INFO
        ));

        // D+13 - AKCIA (Varroa pasca - príprava)
        milestones.add(createMilestone(
                "Pripraviť varroa pascu v oddielku",
                13,
                startDate,
                "Príprava rámika na varroa pascu v oddielku (nová rodina z opačnenca).",
                "#FFFFCC" // AKCIA
        ));

        // D+16 - AKCIA (Varroa pasca - vloženie vajíčka)
        milestones.add(createMilestone(
                "Vložiť varroa pascu (vajíčka)",
                16,
                startDate,
                "Vložiť rámik s vajíčkami zo zdrojového úľa. " +
                "Foretické kliešte zlezú z včiel na tento rámik a invadujú bunky.",
                "#FFFFCC" // AKCIA
        ));

        // D+19 - INFO
        milestones.add(createMilestone(
                "Pohlavné dospievanie matky",
                19,
                startDate,
                "Matka pohlavne dospievа (D+19 až D+21). Pripravuje sa na páriace prelety.",
                "#FFFFFF" // INFO
        ));

        // D+19 - AKCIA (Varroa pasca - vloženie larvy)
        milestones.add(createMilestone(
                "Vložiť varroa pascu (larvy)",
                19,
                startDate,
                "Vložiť rámik s larvami (viac lariev = viac kliešťov). " +
                "Alternatívna pasca s otvoreným plodom.",
                "#FFFFCC" // AKCIA
        ));

        // D+20 - INFO
        milestones.add(createMilestone(
                "Posledný starý robotníci plod",
                20,
                startDate,
                "Posledný starý liahnuci sa robotníci plod zo starej matky.",
                "#FFFFFF" // INFO
        ));

        // D+22 - INFO
        milestones.add(createMilestone(
                "Orientačné páriace prelety",
                22,
                startDate,
                "Matka robí orientačné a páriace prelety (D+20 až D+25).",
                "#FFFFFF" // INFO
        ));

        // D+23 - INFO
        milestones.add(createMilestone(
                "Posledný starý trúdi plod",
                23,
                startDate,
                "Posledný starý liahnuci sa trúdi plod zo starej matky.",
                "#FFFFFF" // INFO
        ));

        // D+25 - AKCIA (Bezplodové obdobie - preliečenie)
        milestones.add(createMilestone(
                "Bezplodové obdobie - preliečenie",
                25,
                startDate,
                "Bezplodové obdobie (D+24 až D+26). Ideálny čas na preliečenie proti varroa. " +
                "Možnosť varroa pasca alebo fumigácia.",
                "#FFFFCC" // AKCIA
        ));

        // D+25 - AKCIA (Varroa pasca - odstránenie vajíčka)
        milestones.add(createMilestone(
                "Odstrániť varroa pascu (vajíčka)",
                25,
                startDate,
                "Odstrániť a zlikvidovať zavíčkovaný rámik s kliešťami. " +
                "Mechanické odstránenie veľkého množstva kliešťov.",
                "#FFFFCC" // AKCIA
        ));

        // D+27 - INFO
        milestones.add(createMilestone(
                "Začiatok kládenia novej matky",
                27,
                startDate,
                "Mladá matka začína klásť vajíčka (D+26 až D+29).",
                "#FFFFFF" // INFO
        ));

        // D+30 - AKCIA (Varroa pasca - odstránenie larvy)
        milestones.add(createMilestone(
                "Odstrániť varroa pascu (larvy)",
                30,
                startDate,
                "Odstrániť a zlikvidovať rámik s larvami a kliešťami. " +
                "Druhá fáza mechanického odstránenia.",
                "#FFFFCC" // AKCIA
        ));

        // D+32 - AKCIA
        milestones.add(createMilestone(
                "Kontrola plodovania novej matky",
                32,
                startDate,
                "Kontrola či mladá matka správne plodí (D+31 až D+33). Hľadať vajíčka a larvičky.",
                "#FFFFCC" // AKCIA
        ));

        // D+41 - INFO
        milestones.add(createMilestone(
                "Posledné staré lietavky",
                41,
                startDate,
                "Narodenie posledných starých lietavok (zo starého plodu).",
                "#FFFFFF" // INFO
        ));

        // D+68 - INFO
        milestones.add(createMilestone(
                "Prvé nové lietavky",
                68,
                startDate,
                "Prvé lietavky z nového plodu začínajú lietať.",
                "#FFFFFF" // INFO
        ));

        // D+76 - INFO
        milestones.add(createMilestone(
                "Úmrtie starých lietavok",
                76,
                startDate,
                "Úmrtie posledných starých lietavok. Rodina už má len mladé včely.",
                "#FFFFFF" // INFO
        ));

        // Sort by dayOffset to ensure chronological order
        milestones.sort((m1, m2) -> Integer.compare(m1.getDayOffset(), m2.getDayOffset()));

        return milestones;
    }

    /**
     * Metóda: Klietkovanie matky + Norské zimovanie
     * Cieľ: Bezplodové obdobie na preliečenie bez vytvárania novej rodiny.
     * Po vylíahnutí plodu -> presypanie na medzistienky (obnova diela, zníženie vírusov).
     */
    private static List<QueenMilestone> calculateKlietkovanieMilestones(long startDate) {
        List<QueenMilestone> milestones = new ArrayList<>();

        // Farby: #FFFFCC = AKCIA (včelár musí zasiahnuť), #FFFFFF = INFO (len sledovanie)

        // D+0 - AKCIA
        milestones.add(createMilestone(
                "Klietkovanie matky",
                0,
                startDate,
                "Matku zaklietkovať do klietky. Matka prestane klásť vajíčka.",
                "#FFFFCC" // AKCIA
        ));

        // D+21 - INFO
        milestones.add(createMilestone(
                "Posledný starý robotníci plod",
                21,
                startDate,
                "Posledný starý liahnuci sa robotníci plod vylíahnutý.",
                "#FFFFFF" // INFO
        ));

        // D+24 - INFO
        milestones.add(createMilestone(
                "Posledný starý trúdi plod",
                24,
                startDate,
                "Posledný starý liahnuci sa trúdi plod vylíahnutý.",
                "#FFFFFF" // INFO
        ));

        // D+25 - INFO
        milestones.add(createMilestone(
                "Bezplodové obdobie - začiatok",
                25,
                startDate,
                "Začiatok bezplodového obdobia. Všetok plod sa vylíahol.",
                "#FFFFFF" // INFO
        ));

        // D+25 - AKCIA (Presypanie)
        milestones.add(createMilestone(
                "Presypanie na medzistienky",
                25,
                startDate,
                "Norské zimovanie: Presypať včely na medzistienky, presunúť medníky. " +
                "Obnova diela, získanie nových plastov, zníženie tlaku vírusov. " +
                "Ak nie je znaška, prikrmiť roztokom 1:1 (3x 0.5L).",
                "#FFFFCC" // AKCIA
        ));

        // D+26 - AKCIA (Varroa pasca - vloženie)
        milestones.add(createMilestone(
                "Vložiť varroa pascu (vajíčka)",
                26,
                startDate,
                "Počas bezplodového obdobia vložiť rámik s vajíčkami. " +
                "Foretické kliešte zlezú na rámik.",
                "#FFFFCC" // AKCIA
        ));

        // D+30 - AKCIA (Preliečenie)
        milestones.add(createMilestone(
                "Preliečenie kyselinou šťavelovou",
                30,
                startDate,
                "Preliečenie pokapom kyselinou šťavelovou. Možná aj fumigácia.",
                "#FFFFCC" // AKCIA
        ));

        // D+31 - AKCIA (Vypustenie matky)
        milestones.add(createMilestone(
                "Vypustenie matky z klietky",
                31,
                startDate,
                "Matku vypustiť z klietky. Matka začne opäť klásť vajíčka.",
                "#FFFFCC" // AKCIA
        ));

        // D+31 - INFO
        milestones.add(createMilestone(
                "Začiatok kládenia matky",
                31,
                startDate,
                "Matka začína klásť vajíčka po plodovej pauze.",
                "#FFFFFF" // INFO
        ));

        // D+35 - AKCIA (Varroa pasca - odstránenie)
        milestones.add(createMilestone(
                "Odstrániť varroa pascu",
                35,
                startDate,
                "Odstrániť a zlikvidovať zavíčkovaný rámik s kliešťami. " +
                "Kombinuje sa s presypaním na MS a preliečením.",
                "#FFFFCC" // AKCIA
        ));

        // D+42 - INFO
        milestones.add(createMilestone(
                "Posledné staré lietavky - narodenie",
                42,
                startDate,
                "Narodenie posledných starých lietavok (D+24+21 = D+45, ale Excel ukazuje D+42).",
                "#FFFFFF" // INFO
        ));

        // D+51 - INFO
        milestones.add(createMilestone(
                "Prvý plod po pauze",
                51,
                startDate,
                "Prvý liahnuci sa plod po plodovej pauze (D+31+20).",
                "#FFFFFF" // INFO
        ));

        // D+72 - INFO
        milestones.add(createMilestone(
                "Prvé nové lietavky",
                72,
                startDate,
                "Prvé lietavky z nového plodu začínajú lietať (D+51+21).",
                "#FFFFFF" // INFO
        ));

        // D+77 - INFO
        milestones.add(createMilestone(
                "Posledné staré lietavky - úmrtie",
                77,
                startDate,
                "Úmrtie posledných starých lietavok. Výrazné slabnutie rodiny.",
                "#FFFFFF" // INFO
        ));

        // Sort by dayOffset to ensure chronological order
        milestones.sort((m1, m2) -> Integer.compare(m1.getDayOffset(), m2.getDayOffset()));

        return milestones;
    }

    /**
     * Creates a single milestone with calculated date.
     */
    private static QueenMilestone createMilestone(String name, int dayOffset, long baseDate,
                                                  String description, String color) {
        long milestoneDate = baseDate + TimeUnit.DAYS.toMillis(dayOffset);
        return new QueenMilestone(name, dayOffset, milestoneDate, description, color);
    }
}
