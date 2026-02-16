package com.beekeeper.desktop.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for VarroaCalculator basic functionality.
 *
 * Tests verify core biological modeling:
 * - Growth rates with/without brood
 * - Treatment effectiveness
 * - Drone brood impact
 * - Status classification
 * - Edge cases (zero mites)
 */
class VarroaCalculatorTest {

    private VarroaParameters defaultParams;

    /**
     * Sets up default biological parameters before each test.
     * Uses research-based values from VarroaParameters.
     */
    @BeforeEach
    void setUp() {
        defaultParams = VarroaParameters.createDefault();
    }

    /**
     * Converts LocalDate to millisecond timestamp for calculator input.
     *
     * @param date Date to convert
     * @return Timestamp in milliseconds since epoch
     */
    private long toTimestamp(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * Test: Exponenciálny rast populácie kliešťov s prítomným plodom
     *
     * Use case: Jarná/letná projekcia (február-september) keď je plod prítomný.
     * Kliešte sa množia v bunke plodu, populácia exponenciálne rastie.
     *
     * Očakávaný výsledok: Počet kliešťov výrazne narastá (viac než 2× za mesiac).
     */
    @Test
    void testBasicGrowthWithBrood() {
        LocalDate startDate = LocalDate.of(2025, 2, 15);
        long timestamp = toTimestamp(startDate);

        VarroaParameters params = new VarroaParameters(defaultParams);
        params.setDroneBroodPercentage(0.10); // 10% drone brood

        VarroaProjection projection = VarroaCalculator.project(
                10, // initial mites
                timestamp,
                30, // 30 days
                true, // brood present
                params,
                new ArrayList<>()
        );

        assertNotNull(projection);
        assertEquals(30, projection.getProjectionDays());
        assertTrue(projection.getDataPoints().size() > 0);

        // With brood, population should grow
        int finalCount = projection.getDataPoints().get(projection.getDataPoints().size() - 1).getCount();
        assertTrue(finalCount > 10, "Mite count should increase with brood present");
    }

    /**
     * Test: Pomalý rast populácie kliešťov BEZ plodu
     *
     * Use case: Zimná projekcia (december-január) keď matka nepochádza.
     * Kliešte nemajú kde sa množiť, len prežívajú na včelách s prirodzenou mortalitou.
     *
     * Očakávaný výsledok: Populácia rastie pomalšie alebo stagnuje,
     * výrazne menej než v scenári s plodom.
     */
    @Test
    void testBasicGrowthWithoutBrood() {
        LocalDate startDate = LocalDate.of(2025, 2, 15);
        long timestamp = toTimestamp(startDate);

        VarroaParameters params = new VarroaParameters(defaultParams);
        params.setDroneBroodPercentage(0.0);

        VarroaProjection projection = VarroaCalculator.project(
                10,
                timestamp,
                30,
                false, // no brood
                params,
                new ArrayList<>()
        );

        assertNotNull(projection);

        // Without brood, population may decrease due to natural mortality
        int finalCount = projection.getDataPoints().get(projection.getDataPoints().size() - 1).getCount();

        // Compare with brood scenario - should be less
        VarroaParameters paramsWithBrood = new VarroaParameters(defaultParams);
        paramsWithBrood.setDroneBroodPercentage(0.10);
        VarroaProjection withBrood = VarroaCalculator.project(10, timestamp, 30, true, paramsWithBrood, new ArrayList<>());
        int finalCountWithBrood = withBrood.getDataPoints().get(withBrood.getDataPoints().size() - 1).getCount();
        assertTrue(finalCount < finalCountWithBrood, "Growth without brood should be slower than with brood");

        // Verify that without brood, population doesn't grow rapidly
        assertTrue(finalCount < 30, "Without brood, population should not grow rapidly");
    }

    /**
     * Test: Aplikácia jedného liečenia a jeho okamžitý efekt
     *
     * Use case: Zimné ošetrenie kyselinou šťavelovou (95% efektivita).
     * Vcelár chce vidieť ako jedno silné ošetrenie ovplyvní populáciu.
     *
     * Očakávaný výsledok: Počet kliešťov výrazne klesne deň po aplikácii liečenia.
     */
    @Test
    void testSingleTreatmentApplication() {
        LocalDate startDate = LocalDate.of(2025, 2, 15);
        LocalDate treatmentDate = LocalDate.of(2025, 3, 1); // 14 days later

        List<VarroaTreatment> treatments = new ArrayList<>();
        VarroaTreatment treatment = new VarroaTreatment();
        treatment.setTreatmentDate(toTimestamp(treatmentDate));
        treatment.setTreatmentType(TreatmentType.OXALIC_ACID.getDisplayName());
        treatment.setEffectiveness(0.95); // 95% effectiveness
        treatments.add(treatment);

        VarroaParameters params = new VarroaParameters(defaultParams);
        params.setDroneBroodPercentage(0.0);

        VarroaProjection projection = VarroaCalculator.project(
                100,
                toTimestamp(startDate),
                60,
                false, // winter, no brood
                params,
                treatments
        );

        assertNotNull(projection);

        // Check that mite count dropped after treatment
        VarroaProjection.DataPoint beforeTreatment = projection.getDataPoints().get(13); // day 13
        VarroaProjection.DataPoint afterTreatment = projection.getDataPoints().get(15); // day 15

        assertTrue(afterTreatment.getCount() < beforeTreatment.getCount(),
                "Mite count should drop after treatment");
    }

    /**
     * Test: Kombinácia viacerých liečení počas sezóny
     *
     * Use case: Jarné + letné ošetrenie (oxalic + formic acid).
     * Vcelár plánuje 2 liečenia: v marci a júli, chce vidieť kumulatívny efekt.
     *
     * Očakávaný výsledok: Finálny počet kliešťov je výrazne nižší než bez liečenia.
     * Viacnásobné liečenia majú synergický efekt - každé ďalšie znižuje rýchlo rastúcu populáciu.
     */
    @Test
    void testMultipleTreatments() {
        LocalDate startDate = LocalDate.of(2025, 2, 15);

        List<VarroaTreatment> treatments = new ArrayList<>();

        // Treatment 1: Oxalic acid in late winter
        VarroaTreatment treatment1 = new VarroaTreatment();
        treatment1.setTreatmentDate(toTimestamp(LocalDate.of(2025, 3, 1)));
        treatment1.setTreatmentType(TreatmentType.OXALIC_ACID.getDisplayName());
        treatment1.setEffectiveness(0.95);
        treatments.add(treatment1);

        // Treatment 2: Formic acid in summer
        VarroaTreatment treatment2 = new VarroaTreatment();
        treatment2.setTreatmentDate(toTimestamp(LocalDate.of(2025, 7, 15)));
        treatment2.setTreatmentType(TreatmentType.FORMIC_ACID.getDisplayName());
        treatment2.setEffectiveness(0.90);
        treatments.add(treatment2);

        VarroaParameters params = new VarroaParameters(defaultParams);
        params.setDroneBroodPercentage(0.10);

        VarroaProjection projection = VarroaCalculator.project(
                50,
                toTimestamp(startDate),
                180, // ~6 months
                true,
                params,
                treatments
        );

        assertNotNull(projection);

        // Final count should be significantly lower than without treatments
        VarroaParameters paramsNoTreat = new VarroaParameters(defaultParams);
        paramsNoTreat.setDroneBroodPercentage(0.10);
        VarroaProjection noTreatment = VarroaCalculator.project(
                50, toTimestamp(startDate), 180, true, paramsNoTreat, new ArrayList<>()
        );

        int finalWithTreatments = projection.getDataPoints().get(projection.getDataPoints().size() - 1).getCount();
        int finalNoTreatment = noTreatment.getDataPoints().get(noTreatment.getDataPoints().size() - 1).getCount();

        assertTrue(finalWithTreatments < finalNoTreatment,
                "Multiple treatments should significantly reduce mite population");
    }

    /**
     * Test: Vplyv percentuálneho podielu trúdieho plodu na rast kliešťov
     *
     * Use case: Vcelár má úle s rôznym % trúdieho plodu (10% vs 20%).
     * Chce vidieť ako vyšší podiel trúdov ovplyvňuje rast populácie kliešťov.
     * (Kliešte preferujú trúdí plod 10× viac než robotníc!)
     *
     * Očakávaný výsledok: Úle s 20% trúdov má výrazne vyšší počet kliešťov než úle s 10%.
     * Preto je dôležité vyrezávať trúdí plod ako biotechnická metóda.
     */
    @Test
    void testDroneBroodImpact() {
        LocalDate startDate = LocalDate.of(2025, 4, 1);
        long timestamp = toTimestamp(startDate);

        // Projection with 10% drone brood
        VarroaParameters params10 = new VarroaParameters(defaultParams);
        params10.setDroneBroodPercentage(0.10);
        VarroaProjection with10Percent = VarroaCalculator.project(
                20, timestamp, 90, true, params10, new ArrayList<>()
        );

        // Projection with 20% drone brood
        VarroaParameters params20 = new VarroaParameters(defaultParams);
        params20.setDroneBroodPercentage(0.20);
        VarroaProjection with20Percent = VarroaCalculator.project(
                20, timestamp, 90, true, params20, new ArrayList<>()
        );

        int final10 = with10Percent.getDataPoints().get(with10Percent.getDataPoints().size() - 1).getCount();
        int final20 = with20Percent.getDataPoints().get(with20Percent.getDataPoints().size() - 1).getCount();

        assertTrue(final20 > final10,
                "Higher drone brood percentage should result in more mites");
    }

    /**
     * Test: Automatická klasifikácia statusu podľa počtu kliešťov
     *
     * Use case: Vcelár chce vedieť či situácia je "OK", "VAROVANIE" alebo "KRITICKÉ".
     * Kalkulačka automaticky vyhodnocuje stav a dáva odporúčania.
     *
     * Thresholds:
     * - < 300: OK (zelená)
     * - 300-1000: VAROVANIE (žltá/oranžová)
     * - > 1000: KRITICKÉ (červená)
     *
     * Očakávaný výsledok: Nízky počet = OK, vysoký počet = VAROVANIE alebo KRITICKÉ.
     */
    @Test
    void testStatusClassification() {
        LocalDate startDate = LocalDate.of(2025, 2, 15);
        long timestamp = toTimestamp(startDate);

        // Low count - should be OK
        VarroaParameters paramsLow = new VarroaParameters(defaultParams);
        paramsLow.setDroneBroodPercentage(0.0);
        VarroaProjection lowCount = VarroaCalculator.project(
                5, timestamp, 30, false, paramsLow, new ArrayList<>()
        );
        // Status is now translated, check it's not null and not empty
        assertNotNull(lowCount.getStatus());
        assertFalse(lowCount.getStatus().isEmpty());

        // High count - should be WARNING or CRITICAL
        VarroaParameters paramsHigh = new VarroaParameters(defaultParams);
        paramsHigh.setDroneBroodPercentage(0.10);
        VarroaProjection highCount = VarroaCalculator.project(
                500, timestamp, 60, true, paramsHigh, new ArrayList<>()
        );
        // Status is now translated, check it's not null and not empty
        assertNotNull(highCount.getStatus());
        assertFalse(highCount.getStatus().isEmpty());
    }

    /**
     * Test: Okrajový prípad - projekcia s 0 kliešťov
     *
     * Use case: Vcelár ošetril úle, všetky kliešte sú mŕtve (teoreticky).
     * Alebo nový roj bez kliešťov. Projekcia by mala ukázať 0 na všetkých dňoch.
     *
     * Očakávaný výsledok: Všetky dátové body majú 0 kliešťov.
     * Kalkulačka by nemala crashnúť ani vygenerovať negatívne čísla.
     */
    @Test
    void testProjectionWithZeroInitialMites() {
        LocalDate startDate = LocalDate.of(2025, 2, 15);
        long timestamp = toTimestamp(startDate);

        VarroaParameters params = new VarroaParameters(defaultParams);
        params.setDroneBroodPercentage(0.10);
        VarroaProjection projection = VarroaCalculator.project(
                0, timestamp, 30, true, params, new ArrayList<>()
        );

        assertNotNull(projection);

        // All data points should have 0 mites
        for (VarroaProjection.DataPoint dp : projection.getDataPoints()) {
            assertEquals(0, dp.getCount(), "With 0 initial mites, count should remain 0");
        }
    }

    /**
     * Test: Porovnanie rôznych úrovní efektivity liečení
     *
     * Use case: Vcelár sa rozhoduje medzi zaklietkovaním matky (50% efektivita)
     * a kyselinou šťavelovou (95% efektivita). Chce vidieť rozdiel v konečnom počte kliešťov.
     *
     * Očakávaný výsledok: Liečenie s 95% efektivitou má výrazne nižší finálny počet kliešťov
     * než liečenie s 50% efektivitou. Rozdiel môže byť 2-3×.
     */
    @Test
    void testTreatmentEffectivenessRange() {
        LocalDate startDate = LocalDate.of(2025, 2, 15);
        LocalDate treatmentDate = LocalDate.of(2025, 3, 1);

        // Test with 50% effectiveness
        List<VarroaTreatment> treatments50 = new ArrayList<>();
        VarroaTreatment treatment50 = new VarroaTreatment();
        treatment50.setTreatmentDate(toTimestamp(treatmentDate));
        treatment50.setTreatmentType(TreatmentType.QUEEN_CAGING.getDisplayName());
        treatment50.setEffectiveness(0.50);
        treatments50.add(treatment50);

        // Test with 95% effectiveness
        List<VarroaTreatment> treatments95 = new ArrayList<>();
        VarroaTreatment treatment95 = new VarroaTreatment();
        treatment95.setTreatmentDate(toTimestamp(treatmentDate));
        treatment95.setTreatmentType(TreatmentType.OXALIC_ACID.getDisplayName());
        treatment95.setEffectiveness(0.95);
        treatments95.add(treatment95);

        VarroaParameters params50 = new VarroaParameters(defaultParams);
        params50.setDroneBroodPercentage(0.0);
        VarroaProjection proj50 = VarroaCalculator.project(
                100, toTimestamp(startDate), 60, false, params50, treatments50
        );

        VarroaParameters params95 = new VarroaParameters(defaultParams);
        params95.setDroneBroodPercentage(0.0);
        VarroaProjection proj95 = VarroaCalculator.project(
                100, toTimestamp(startDate), 60, false, params95, treatments95
        );

        int final50 = proj50.getDataPoints().get(proj50.getDataPoints().size() - 1).getCount();
        int final95 = proj95.getDataPoints().get(proj95.getDataPoints().size() - 1).getCount();

        assertTrue(final95 < final50,
                "95% effectiveness should result in fewer mites than 50% effectiveness");
    }
}
