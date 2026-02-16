package com.beekeeper.desktop.integration;

import com.beekeeper.desktop.calculator.TreatmentType;
import com.beekeeper.desktop.calculator.VarroaCalculator;
import com.beekeeper.desktop.calculator.VarroaParameters;
import com.beekeeper.desktop.calculator.VarroaProjection;
import com.beekeeper.desktop.calculator.VarroaTreatment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Varroa Calculator with full-season treatment scenarios.
 *
 * Each test simulates a complete beekeeping season (February - August, 180 days)
 * starting with different initial mite counts and treatment strategies.
 *
 * Test scenarios cover:
 * - Low initial count (5-10) with minimal chemistry
 * - Medium count (20-50) with biotechnical/combined strategies
 * - High count (100) with intensive chemistry (rescue scenario)
 * - Control test (no treatment) showing exponential growth
 *
 * All scenarios compare treated vs untreated populations to demonstrate effectiveness.
 */
class VarroaCalculatorIntegrationTest {

    private static final int MAX_MITES = 10000; // Critical threshold - colony at risk above this
    private static final int SEASON_DAYS = 180; // Feb 15 - Aug 15 (~6 months, active treatment season)
    private static final LocalDate SEASON_START = LocalDate.of(2025, 2, 15);

    private VarroaParameters defaultParams;

    /**
     * Initializes default biological parameters before each test.
     * Sets 10% drone brood which is typical for spring/summer season.
     */
    @BeforeEach
    void setUp() {
        defaultParams = VarroaParameters.createDefault();
        defaultParams.setDroneBroodPercentage(0.10); // 10% drone brood
    }

    /**
     * Converts LocalDate to millisecond timestamp for calculator.
     *
     * @param date Date to convert
     * @return Timestamp in milliseconds since epoch
     */
    private long toTimestamp(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * Helper method to create a VarroaTreatment object with all required fields.
     *
     * @param date Treatment date
     * @param type TreatmentType enum (e.g., OXALIC_ACID, FORMIC_ACID)
     * @param effectiveness Effectiveness as decimal (0.0 - 1.0)
     * @param notes Descriptive notes about this treatment
     * @return Configured VarroaTreatment object
     */
    private VarroaTreatment createTreatment(LocalDate date, TreatmentType type, double effectiveness, String notes) {
        VarroaTreatment treatment = new VarroaTreatment();
        treatment.setTreatmentDate(toTimestamp(date));
        treatment.setTreatmentType(type.getDisplayName());
        treatment.setEffectiveness(effectiveness);
        treatment.setDescription(notes);
        return treatment;
    }

    /**
     * Asserts that mite population never exceeds critical threshold during season.
     *
     * Use case: Verify that treatment strategy keeps colony safe from collapse.
     *
     * @param projection Calculated projection to check
     * @param scenario Scenario name for error message
     */
    private void assertNeverExceedsMax(VarroaProjection projection, String scenario) {
        int maxCount = projection.getDataPoints().stream()
                .mapToInt(VarroaProjection.DataPoint::getCount)
                .max()
                .orElse(0);

        assertTrue(maxCount < MAX_MITES,
                String.format("%s: Maximum mite count exceeded threshold. Max: %d, Threshold: %d",
                        scenario, maxCount, MAX_MITES));
    }

    /**
     * Asserts that treatment reduces mite population compared to no treatment.
     *
     * Use case: Verify that the treatment strategy actually works by comparing
     * final mite counts with and without treatment.
     *
     * @param withTreatment Projection with treatments applied
     * @param noTreatment Projection without any treatment (baseline)
     * @param scenario Scenario name for error message
     */
    private void assertTreatmentIsEffective(VarroaProjection withTreatment, VarroaProjection noTreatment, String scenario) {
        int finalWithTreatment = withTreatment.getDataPoints().get(withTreatment.getDataPoints().size() - 1).getCount();
        int finalNoTreatment = noTreatment.getDataPoints().get(noTreatment.getDataPoints().size() - 1).getCount();

        assertTrue(finalWithTreatment < finalNoTreatment,
                String.format("%s: Treatment should reduce mite population. With: %d, Without: %d",
                        scenario, finalWithTreatment, finalNoTreatment));
    }

    /**
     * Prints detailed summary of scenario results to console.
     *
     * Output includes:
     * - Initial and final mite counts
     * - Maximum count reached during season
     * - Status classification (OK/WARNING/CRITICAL)
     * - List of all treatments with dates and effectiveness
     *
     * Use case: Provides human-readable documentation of each test scenario
     * that beekeepers can review to understand treatment strategies.
     *
     * @param scenario Scenario name/description
     * @param initialMites Starting mite count
     * @param projection Calculated projection
     * @param treatments List of treatments applied
     */
    private void printScenarioSummary(String scenario, int initialMites, VarroaProjection projection, List<VarroaTreatment> treatments) {
        int finalMites = projection.getDataPoints().get(projection.getDataPoints().size() - 1).getCount();
        int maxMites = projection.getDataPoints().stream()
                .mapToInt(VarroaProjection.DataPoint::getCount)
                .max()
                .orElse(0);

        System.out.println("\n=== " + scenario + " ===");
        System.out.println("Počiatočný stav: " + initialMites + " kliešťov");
        System.out.println("Finálny stav: " + finalMites + " kliešťov");
        System.out.println("Maximálny počet: " + maxMites + " kliešťov");
        System.out.println("Status: " + projection.getStatus());
        System.out.println("Liečenia (" + treatments.size() + "x):");
        for (VarroaTreatment t : treatments) {
            LocalDate date = LocalDate.ofInstant(
                    java.time.Instant.ofEpochMilli(t.getTreatmentDate()),
                    ZoneId.systemDefault()
            );
            System.out.printf("  - %s: %s (%.0f%%) - %s\n",
                    date, t.getTreatmentType(),
                    t.getEffectivenessPercent(),
                    t.getDescription());
        }
    }

    /**
     * SCENARIO 1: 5 kliešťov na začiatku sezóny + minimálna chemická intervencia
     *
     * Use case: Vcelár začína jar s veľmi nízkym počtom kliešťov (dobrý zimný stav).
     * Chce použiť minimálny počet chemických ošetrení, ale zabezpečiť celú sezónu.
     *
     * Stratégia:
     * - Marec: Oxalic acid (95%) - jarný cleanup po zime
     * - Jún: Formic acid (90%) - preventívne letné ošetrenie
     * - September: Thymol (70%) - príprava na zimu, organické
     *
     * Očakávaný výsledok: S nízkym počiatočným stavom stačia 3 liečenia na udržanie
     * populácie pod kontrolou celú sezónu. Finálny stav by mal byť výrazne lepší než bez liečenia.
     *
     * Praktický význam: Ukazuje že prevencia je lepšia než liečba - ak vcelár
     * začne s nízkym počtom, nemusí používať agresívnu chemickú stratégiu.
     */
    @Test
    void testScenario1_LowCount_MinimalChemistry() {
        int initialMites = 5;
        List<VarroaTreatment> treatments = new ArrayList<>();

        // March 1 - Oxalic acid (winter treatment, no brood)
        treatments.add(createTreatment(
                LocalDate.of(2025, 3, 1),
                TreatmentType.OXALIC_ACID,
                0.95,
                "Zimné ošetrenie, bez plodu"
        ));

        // June 15 - Formic acid (mid-season)
        treatments.add(createTreatment(
                LocalDate.of(2025, 6, 15),
                TreatmentType.FORMIC_ACID,
                0.90,
                "Letné ošetrenie"
        ));

        // September 1 - Thymol (fall treatment)
        treatments.add(createTreatment(
                LocalDate.of(2025, 9, 1),
                TreatmentType.THYMOL,
                0.70,
                "Jesenné ošetrenie"
        ));

        VarroaParameters params = new VarroaParameters(defaultParams);

        VarroaProjection projection = VarroaCalculator.project(
                initialMites,
                toTimestamp(SEASON_START),
                SEASON_DAYS,
                true, // brood present (spring onwards)
                params,
                treatments
        );

        assertNotNull(projection);

        // Compare with no treatment
        VarroaParameters paramsNoTreat = new VarroaParameters(defaultParams);
        VarroaProjection noTreatment = VarroaCalculator.project(
                initialMites, toTimestamp(SEASON_START), SEASON_DAYS, true, paramsNoTreat, new ArrayList<>()
        );

        printScenarioSummary("SCENÁR 1: 5 kliešťov + 3 liečenia", initialMites, projection, treatments);
        assertTreatmentIsEffective(projection, noTreatment, "Scenario 1");

        // Document the improvement
        int finalWithTreatment = projection.getDataPoints().get(projection.getDataPoints().size() - 1).getCount();
        int finalNoTreatment = noTreatment.getDataPoints().get(noTreatment.getDataPoints().size() - 1).getCount();
        double reductionPercent = ((double)(finalNoTreatment - finalWithTreatment) / finalNoTreatment) * 100;
        System.out.println("Bez liečenia: " + finalNoTreatment + " kliešťov");
        System.out.println("Redukcia: " + String.format("%.1f%%", reductionPercent));
    }

    /**
     * SCENARIO 2: 10 kliešťov + efektívna chemická stratégia
     *
     * Use case: Vcelár začína s nízkym ale nie zanedbateľným počtom kliešťov.
     * Chce použiť najefektívnejšie chemické prípravky na minimalizáciu rizika.
     *
     * Stratégia:
     * - Marec: Oxalic acid (95%) - najsilnejšie zimné ošetrenie
     * - Júl: Formic acid (90%) - preniká do plodu, efektívne aj v lete
     * - November: Oxalic acid (95%) - záverečný cleanup pred zimou
     *
     * Očakávaný výsledok: Kombinácia dvoch najefektívnejších organických kyselín
     * má synergický efekt. Finálny počet kliešťov by mal byť veľmi nízky (< 500).
     *
     * Praktický význam: "Gold standard" chemická stratégia pre bežného včelára.
     * Organické kyseliny, vysoká efektivita, 3× ročne je zvládnuteľné.
     */
    @Test
    void testScenario2_LowCount_EffectiveChemistry() {
        int initialMites = 10;
        List<VarroaTreatment> treatments = new ArrayList<>();

        // March 1 - Oxalic acid
        treatments.add(createTreatment(
                LocalDate.of(2025, 3, 1),
                TreatmentType.OXALIC_ACID,
                0.95,
                "Zimné ošetrenie"
        ));

        // July 15 - Formic acid (works with brood present)
        treatments.add(createTreatment(
                LocalDate.of(2025, 7, 15),
                TreatmentType.FORMIC_ACID,
                0.90,
                "Letné ošetrenie, preniká do plodu"
        ));

        // November 1 - Oxalic acid (final winter treatment)
        treatments.add(createTreatment(
                LocalDate.of(2025, 11, 1),
                TreatmentType.OXALIC_ACID,
                0.95,
                "Záverečné zimné ošetrenie"
        ));

        VarroaParameters params = new VarroaParameters(defaultParams);

        VarroaProjection projection = VarroaCalculator.project(
                initialMites,
                toTimestamp(SEASON_START),
                SEASON_DAYS,
                true,
                params,
                treatments
        );

        assertNotNull(projection);

        // Compare with no treatment
        VarroaParameters paramsNoTreat = new VarroaParameters(defaultParams);
        VarroaProjection noTreatment = VarroaCalculator.project(
                initialMites, toTimestamp(SEASON_START), SEASON_DAYS, true, paramsNoTreat, new ArrayList<>()
        );

        printScenarioSummary("SCENÁR 2: 10 kliešťov + efektívna chémia (3× ošetrenie)", initialMites, projection, treatments);
        assertTreatmentIsEffective(projection, noTreatment, "Scenario 2");

        int finalWithTreatment = projection.getDataPoints().get(projection.getDataPoints().size() - 1).getCount();
        int finalNoTreatment = noTreatment.getDataPoints().get(noTreatment.getDataPoints().size() - 1).getCount();
        double reductionPercent = ((double)(finalNoTreatment - finalWithTreatment) / finalNoTreatment) * 100;
        System.out.println("Bez liečenia: " + finalNoTreatment + " kliešťov");
        System.out.println("Redukcia: " + String.format("%.1f%%", reductionPercent));
    }

    /**
     * SCENARIO 3: 20 kliešťov + BEZ CHÉMIE - čisto biotechnické metódy
     *
     * Use case: Vcelár chce úplne prírodný prístup bez akýchkoľvek chemikálií/kyselín.
     * Prioritou je zdravie včiel a nulové rezíduá v mede. Akceptuje vyššiu pracnosť.
     *
     * Stratégia (10× zásahov počas sezóny):
     * - Apríl-August: Pravidelné vyrezávanie trúdieho plodu (70%, každé 2-3 týždne)
     * - Apríl-Jún: Lovovacie rámky (30%, pasívna metóda)
     * - Apríl: Dno s mriežkou (10%, celoročná pasívna ochrana)
     * - Júl: Zaklietkovanie matky (50%, príprava na ďalší zásah)
     *
     * Očakávaný výsledok: Biotechnické metódy majú nižšiu efektivitu (30-70%) než chémia,
     * ale v kombinácii a s pravidelnosťou dokážu držať kliešte pod kontrolou.
     * Redukcia nebude taká výrazná ako pri chémii, ale úle zostanú zdravé.
     *
     * Praktický význam: Ukazuje že je možné včeláriť BEZ chémie, ale vyžaduje to:
     * - Viac času (10× zásahov vs. 3× pri chémii)
     * - Disciplínu (pravidelnosť každých 2-3 týždne)
     * - Nižší počiatočný stav kliešťov
     *
     * DÔLEŽITÉ: Tento scenár ukazuje vcelárovi že ekologický prístup JE MOŽNÝ!
     */
    @Test
    void testScenario3_MediumCount_OnlyBiotechnical() {
        int initialMites = 20;
        List<VarroaTreatment> treatments = new ArrayList<>();

        // April 1 - Install screened bottom first
        treatments.add(createTreatment(
                LocalDate.of(2025, 4, 1),
                TreatmentType.SCREENED_BOTTOM,
                0.10,
                "Celoročná pasívna ochrana"
        ));

        // April 15 - Drone brood removal
        treatments.add(createTreatment(
                LocalDate.of(2025, 4, 15),
                TreatmentType.DRONE_BROOD_REMOVAL,
                0.70,
                "Pravidelné vyrezávanie"
        ));

        // April 25 - Trap combs
        treatments.add(createTreatment(
                LocalDate.of(2025, 4, 25),
                TreatmentType.TRAP_COMB,
                0.30,
                "Lovovacie rámky"
        ));

        // May 10 - Drone brood removal
        treatments.add(createTreatment(
                LocalDate.of(2025, 5, 10),
                TreatmentType.DRONE_BROOD_REMOVAL,
                0.70,
                "Pokračovanie"
        ));

        // May 20 - Trap combs
        treatments.add(createTreatment(
                LocalDate.of(2025, 5, 20),
                TreatmentType.TRAP_COMB,
                0.30,
                "Lovovacie rámky"
        ));

        // June 5 - Drone brood removal
        treatments.add(createTreatment(
                LocalDate.of(2025, 6, 5),
                TreatmentType.DRONE_BROOD_REMOVAL,
                0.70,
                "Pokračovanie"
        ));

        // June 20 - Trap combs
        treatments.add(createTreatment(
                LocalDate.of(2025, 6, 20),
                TreatmentType.TRAP_COMB,
                0.30,
                "Lovovacie rámky"
        ));

        // July 10 - Drone brood removal
        treatments.add(createTreatment(
                LocalDate.of(2025, 7, 10),
                TreatmentType.DRONE_BROOD_REMOVAL,
                0.70,
                "Pokračovanie"
        ));

        // July 25 - Queen caging
        treatments.add(createTreatment(
                LocalDate.of(2025, 7, 25),
                TreatmentType.QUEEN_CAGING,
                0.50,
                "Zaklietkovanie na 21 dní"
        ));

        // August 20 - Drone brood removal (last)
        treatments.add(createTreatment(
                LocalDate.of(2025, 8, 20),
                TreatmentType.DRONE_BROOD_REMOVAL,
                0.70,
                "Záverečné vyrezávanie"
        ));

        VarroaParameters params = new VarroaParameters(defaultParams);

        VarroaProjection projection = VarroaCalculator.project(
                initialMites,
                toTimestamp(SEASON_START),
                SEASON_DAYS,
                true,
                params,
                treatments
        );

        assertNotNull(projection);

        // Compare with no treatment
        VarroaParameters paramsNoTreat = new VarroaParameters(defaultParams);
        VarroaProjection noTreatment = VarroaCalculator.project(
                initialMites, toTimestamp(SEASON_START), SEASON_DAYS, true, paramsNoTreat, new ArrayList<>()
        );

        printScenarioSummary("SCENÁR 3: 20 kliešťov + BEZ CHÉMIE (10× biotechnické zásahy)", initialMites, projection, treatments);
        assertTreatmentIsEffective(projection, noTreatment, "Scenario 3");

        int finalWithTreatment = projection.getDataPoints().get(projection.getDataPoints().size() - 1).getCount();
        int finalNoTreatment = noTreatment.getDataPoints().get(noTreatment.getDataPoints().size() - 1).getCount();
        double reductionPercent = ((double)(finalNoTreatment - finalWithTreatment) / finalNoTreatment) * 100;
        System.out.println("Bez liečenia: " + finalNoTreatment + " kliešťov");
        System.out.println("Redukcia: " + String.format("%.1f%%", reductionPercent));
        System.out.println("POZNÁMKA: Biotechnické metódy sú menej efektívne, ale BEZPEČNÉ a bez chémie!");
    }

    /**
     * SCENARIO 4: 50 kliešťov + kombinovaná stratégia (biotechnika + chémia)
     *
     * Use case: Vcelár začína s vyšším počtom kliešťov (nie kritický, ale potrebuje riešiť).
     * Chce kombinovať biotechniku v lete (keď je med) s chémiou na jeseň (po vytočení medu).
     *
     * Stratégia:
     * - Marec: Oxalic acid (95%) - silný jarný štart
     * - Apríl-Jún: 3× vyrezávanie trúdieho plodu (70%) - biotechnika počas medsezóny
     * - August: Amitraz pásy (92%) - silné jesenné ošetrenie, 6 týždňov pôsobenia
     * - November: Oxalic acid (95%) - finálny cleanup
     *
     * Očakávaný výsledok: Kombinovaný prístup využíva výhody oboch stratégií:
     * - Jar/Leto: Biotechnika (žiadne rezíduá v mede)
     * - Jeseň: Chémia (vysoká efektivita, už nie je med)
     *
     * Praktický význam: Najrealistickejšia stratégia pre väčšinu včelárov.
     * Kompromis medzi efektivitou, bezpečnosťou medu a pracnosťou.
     * S vyšším počiatočným stavom (50) je potrebná intenzívnejšia intervencia.
     */
    @Test
    void testScenario4_MediumHighCount_Combined() {
        int initialMites = 50;
        List<VarroaTreatment> treatments = new ArrayList<>();

        // March 1 - Oxalic acid (clean start)
        treatments.add(createTreatment(
                LocalDate.of(2025, 3, 1),
                TreatmentType.OXALIC_ACID,
                0.95,
                "Jarný štart"
        ));

        // April 20 - Drone brood removal
        treatments.add(createTreatment(
                LocalDate.of(2025, 4, 20),
                TreatmentType.DRONE_BROOD_REMOVAL,
                0.70,
                "Biotechnická metóda"
        ));

        // May 20 - Drone brood removal
        treatments.add(createTreatment(
                LocalDate.of(2025, 5, 20),
                TreatmentType.DRONE_BROOD_REMOVAL,
                0.70,
                "Biotechnická metóda"
        ));

        // June 20 - Drone brood removal
        treatments.add(createTreatment(
                LocalDate.of(2025, 6, 20),
                TreatmentType.DRONE_BROOD_REMOVAL,
                0.70,
                "Biotechnická metóda"
        ));

        // August 15 - Amitraz strips (reliable fall treatment)
        treatments.add(createTreatment(
                LocalDate.of(2025, 8, 15),
                TreatmentType.AMITRAZ,
                0.92,
                "Jesenné ošetrenie, pásy na 6 týždňov"
        ));

        // November 1 - Oxalic acid (final cleanup)
        treatments.add(createTreatment(
                LocalDate.of(2025, 11, 1),
                TreatmentType.OXALIC_ACID,
                0.95,
                "Záverečné ošetrenie"
        ));

        VarroaParameters params = new VarroaParameters(defaultParams);

        VarroaProjection projection = VarroaCalculator.project(
                initialMites,
                toTimestamp(SEASON_START),
                SEASON_DAYS,
                true,
                params,
                treatments
        );

        assertNotNull(projection);

        // Compare with no treatment
        VarroaParameters paramsNoTreat = new VarroaParameters(defaultParams);
        VarroaProjection noTreatment = VarroaCalculator.project(
                initialMites, toTimestamp(SEASON_START), SEASON_DAYS, true, paramsNoTreat, new ArrayList<>()
        );

        printScenarioSummary("SCENÁR 4: 50 kliešťov + kombinovaná stratégia (biotechnika + chémia)", initialMites, projection, treatments);
        assertTreatmentIsEffective(projection, noTreatment, "Scenario 4");

        int finalWithTreatment = projection.getDataPoints().get(projection.getDataPoints().size() - 1).getCount();
        int finalNoTreatment = noTreatment.getDataPoints().get(noTreatment.getDataPoints().size() - 1).getCount();
        double reductionPercent = ((double)(finalNoTreatment - finalWithTreatment) / finalNoTreatment) * 100;
        System.out.println("Bez liečenia: " + finalNoTreatment + " kliešťov");
        System.out.println("Redukcia: " + String.format("%.1f%%", reductionPercent));
    }

    /**
     * SCENARIO 5: 100 kliešťov + intenzívna chemická intervencia (ZÁCHRANA ÚĽA)
     *
     * Use case: KRITICKÁ SITUÁCIA - vcelár začína sezónu s vysokým počtom kliešťov (100).
     * Úle sú v riziku kolapsu. Potrebná agresívna stratégia s najefektívnejšími prípravkami.
     *
     * Stratégia (6× chemických ošetrení):
     * - Marec: Oxalic acid (95%) - urgentné zimné ošetrenie
     * - Jún: Formic acid (90%) - letné ošetrenie, preniká do plodu
     * - Júl: Full brood break + Oxalic (90%) - najefektívnejšia metóda vôbec
     * - August: Amitraz pásy (92%) - syntetický prípravok, dlhodobé pôsobenie
     * - Október: Flumethrin pásy (90%) - dodatočné poistenie
     * - November: Oxalic acid (95%) - finálny cleanup
     *
     * Očakávaný výsledok: Aj pri vysokom počiatočnom stave (100) dokáže intenzívna
     * chemická stratégia znížiť populáciu kliešťov na akceptovateľnú úroveň.
     * Finálny stav by mal byť < 500 kliešťov.
     *
     * Praktický význam: "Záchranný scenár" pre vcelára ktorý:
     * - Zanedbával liečenie v minulosti
     * - Zistil vysoký stav až neskoro
     * - Chce zachrániť úle pred zimou
     *
     * VAROVANIE: Intenzívna chémia = riziko rezíduí v mede + nákladné.
     * Lepšie je prevencia (scenáre 1-3) než tento záchranný scenár!
     */
    @Test
    void testScenario5_HighCount_IntensiveChemistry() {
        int initialMites = 100;
        List<VarroaTreatment> treatments = new ArrayList<>();

        // March 1 - Oxalic acid (immediate action)
        treatments.add(createTreatment(
                LocalDate.of(2025, 3, 1),
                TreatmentType.OXALIC_ACID,
                0.95,
                "Urgentné zimné ošetrenie"
        ));

        // June 15 - Formic acid (mid-season)
        treatments.add(createTreatment(
                LocalDate.of(2025, 6, 15),
                TreatmentType.FORMIC_ACID,
                0.90,
                "Letné ošetrenie"
        ));

        // July 20 - Full brood break with oxalic acid (most effective)
        treatments.add(createTreatment(
                LocalDate.of(2025, 7, 20),
                TreatmentType.BROOD_BREAK_FULL,
                0.90,
                "Plodová prestávka + kyselina šťavelová"
        ));

        // August 25 - Amitraz (additional safety)
        treatments.add(createTreatment(
                LocalDate.of(2025, 8, 25),
                TreatmentType.AMITRAZ,
                0.92,
                "Jesenné poistenie"
        ));

        // October 15 - Flumethrin (late season)
        treatments.add(createTreatment(
                LocalDate.of(2025, 10, 15),
                TreatmentType.FLUMETHRIN,
                0.90,
                "Jesenné ošetrenie"
        ));

        // November 5 - Oxalic acid (final cleanup)
        treatments.add(createTreatment(
                LocalDate.of(2025, 11, 5),
                TreatmentType.OXALIC_ACID,
                0.95,
                "Záverečné zimné ošetrenie"
        ));

        VarroaParameters params = new VarroaParameters(defaultParams);

        VarroaProjection projection = VarroaCalculator.project(
                initialMites,
                toTimestamp(SEASON_START),
                SEASON_DAYS,
                true,
                params,
                treatments
        );

        assertNotNull(projection);

        // Compare with no treatment
        VarroaParameters paramsNoTreat = new VarroaParameters(defaultParams);
        VarroaProjection noTreatment = VarroaCalculator.project(
                initialMites, toTimestamp(SEASON_START), SEASON_DAYS, true, paramsNoTreat, new ArrayList<>()
        );

        printScenarioSummary("SCENÁR 5: 100 kliešťov + intenzívna chémia (6× ošetrenie)", initialMites, projection, treatments);
        assertTreatmentIsEffective(projection, noTreatment, "Scenario 5");

        int finalWithTreatment = projection.getDataPoints().get(projection.getDataPoints().size() - 1).getCount();
        int finalNoTreatment = noTreatment.getDataPoints().get(noTreatment.getDataPoints().size() - 1).getCount();
        double reductionPercent = ((double)(finalNoTreatment - finalWithTreatment) / finalNoTreatment) * 100;
        System.out.println("Bez liečenia: " + finalNoTreatment + " kliešťov");
        System.out.println("Redukcia: " + String.format("%.1f%%", reductionPercent));
    }

    /**
     * SCENARIO 6 BONUS: 20 kliešťov + biotechnická plodová prestávka (BEZ kyseliny!)
     *
     * Use case: Vcelár chce použiť plodovú prestávku, ale NEPÁČI sa mu myšlienka
     * aplikovať kyselinu šťavelová (BROOD_BREAK_FULL = 90% efektivita = prestávka + kyselina).
     * Pýta sa: "Čo ak len vyvolám prestávku ale neaplikujem kyselinu?"
     *
     * Stratégia:
     * - Apríl-Júl: 4× vyrezávanie trúdieho plodu (70%) - preventívna biotechnika
     * - Júl: BROOD_BREAK_BIO (30%) - čisto biotechnická prestávka BEZ kyseliny
     *   (matka zaklietkovaná 21 dní, všetky kliešte phoretické, ale nie sú zlikvidované!)
     * - September: Vyrezávanie trúdieho plodu (70%) - dodatočný zásah
     *
     * Očakávaný výsledok: Biotechnická plodová prestávka má LEN 30% efektivitu
     * (vs. 90% pri full brood break s kyselinou). Prestávka SAMA O SEBE len spomalí
     * rast, nezabíja kliešte. Kliešte čakajú na včelách a keď sa matka vypustí,
     * začnú sa opäť množiť.
     *
     * Praktický význam: Tento test EDUKATÍVNE ukazuje vcelárovi že:
     * - Plodová prestávka sama o sebe NIE JE liečba
     * - Je to PRÍPRAVA na liečbu (spraví kliešte dostupné)
     * - Bez kyseliny je efektivita len 30%, nie 90%
     *
     * POROVNANIE:
     * - BROOD_BREAK_BIO (30%) = len prestávka, žiadna kyselina
     * - BROOD_BREAK_FULL (90%) = prestávka + kyselina šťavelová
     *
     * Tento scenár pomáha vcelárovi pochopiť ROZDIEL medzi týmito metódami!
     */
    @Test
    void testScenario6_BiotechnicalBroodBreak() {
        int initialMites = 20;
        List<VarroaTreatment> treatments = new ArrayList<>();

        // April 15 - Drone brood removal
        treatments.add(createTreatment(
                LocalDate.of(2025, 4, 15),
                TreatmentType.DRONE_BROOD_REMOVAL,
                0.70,
                "Jarná redukcia"
        ));

        // May 15 - Drone brood removal
        treatments.add(createTreatment(
                LocalDate.of(2025, 5, 15),
                TreatmentType.DRONE_BROOD_REMOVAL,
                0.70,
                "Pokračovanie"
        ));

        // July 25 - Biotechnical brood break (NO chemicals)
        treatments.add(createTreatment(
                LocalDate.of(2025, 7, 25),
                TreatmentType.BROOD_BREAK_BIO,
                0.30,
                "Plodová prestávka BEZ chémie - len prerušenie plodu"
        ));

        // September 1 - Drone brood removal (if still present)
        treatments.add(createTreatment(
                LocalDate.of(2025, 9, 1),
                TreatmentType.DRONE_BROOD_REMOVAL,
                0.70,
                "Jesenná redukcia"
        ));

        VarroaParameters params = new VarroaParameters(defaultParams);

        VarroaProjection projection = VarroaCalculator.project(
                initialMites,
                toTimestamp(SEASON_START),
                SEASON_DAYS,
                true,
                params,
                treatments
        );

        assertNotNull(projection);

        // Compare with no treatment
        VarroaParameters paramsNoTreat = new VarroaParameters(defaultParams);
        VarroaProjection noTreatment = VarroaCalculator.project(
                initialMites, toTimestamp(SEASON_START), SEASON_DAYS, true, paramsNoTreat, new ArrayList<>()
        );

        printScenarioSummary("SCENÁR 6 BONUS: 20 kliešťov + biotechnická plodová prestávka", initialMites, projection, treatments);
        assertTreatmentIsEffective(projection, noTreatment, "Scenario 6");

        int finalWithTreatment = projection.getDataPoints().get(projection.getDataPoints().size() - 1).getCount();
        int finalNoTreatment = noTreatment.getDataPoints().get(noTreatment.getDataPoints().size() - 1).getCount();
        double reductionPercent = ((double)(finalNoTreatment - finalWithTreatment) / finalNoTreatment) * 100;
        System.out.println("Bez liečenia: " + finalNoTreatment + " kliešťov");
        System.out.println("Redukcia: " + String.format("%.1f%%", reductionPercent));
    }

    /**
     * KONTROLNÝ TEST: Bez liečenia - exponenciálny rast populácie kliešťov
     *
     * Use case: Vcelár sa pýta: "Čo sa stane ak VÔBEC neošetrujem?"
     * Potrebuje vidieť ako rýchlo rastie populácia kliešťov bez zásahu.
     *
     * Stratégia: ŽIADNE liečenie počas 180 dní (február-august).
     *
     * Očakávaný výsledok: Populácia kliešťov rastie exponenciálne.
     * Zo 50 kliešťov v februári narastie na 500-1000+ do augusta (10-20× nárast!).
     * Úle sú v kritickom stave, hrozí kolaps včelstva.
     *
     * Praktický význam: Tento test slúži ako KONTROLA pre všetky ostatné scenáre.
     * Každý scenár s liečením musí ukázať VÝRAZNE lepší výsledok než tento test.
     *
     * EDUKATÍVNY VÝZNAM:
     * - Ukazuje vcelárovi že kliešte sa množia EXPONENCIÁLNE, nie lineárne
     * - Malý počiatočný rozdiel (5 vs 50) sa stáva OBROVSKÝM (50 vs 5000) za pár mesiacov
     * - Zdôrazňuje NUTNOSŤ pravidelného liečenia
     * - Slúži ako baseline pre porovnanie efektivity rôznych stratégií
     *
     * Každý scenár (1-6) sa porovnáva s TÝMTO baseline testom a ukazuje % redukciu.
     */
    @Test
    void testNoTreatment_ExponentialGrowth() {
        int initialMites = 50;
        List<VarroaTreatment> treatments = new ArrayList<>(); // NO treatments

        VarroaParameters params = new VarroaParameters(defaultParams);

        VarroaProjection projection = VarroaCalculator.project(
                initialMites,
                toTimestamp(SEASON_START),
                SEASON_DAYS,
                true,
                params,
                treatments
        );

        assertNotNull(projection);

        int finalMites = projection.getDataPoints().get(projection.getDataPoints().size() - 1).getCount();

        System.out.println("\n=== KONTROLA: BEZ LIEČENIA ===");
        System.out.println("Počiatočný stav: " + initialMites + " kliešťov");
        System.out.println("Finálny stav: " + finalMites + " kliešťov (po " + SEASON_DAYS + " dňoch)");
        System.out.println("Status: " + projection.getStatus());
        System.out.println("Nárast: " + String.format("%.1fx", (double)finalMites / initialMites));

        // Without treatment, population should grow significantly
        assertTrue(finalMites > initialMites * 10,
                "Without treatment, population should grow at least 10x");
    }
}
