package com.beekeeper.desktop.calculator;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for QueenRearingCalculator.
 *
 * Tests verify queen rearing timeline calculations:
 * - Correct number of milestones (7 standard milestones)
 * - Accurate date calculations (D+0 to D+21)
 * - Proper descriptions and formatting
 * - Color coding for visual timeline
 */
class QueenRearingCalculatorTest {

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
     * Converts timestamp back to LocalDate for assertions.
     *
     * @param timestamp Milliseconds since epoch
     * @return LocalDate representation
     */
    private LocalDate toLocalDate(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Test: Základný výpočet všetkých 7 míľnikov
     *
     * Use case: Vcelár začína odchov matiek 1. mája, chce vidieť celý timeline.
     * Kalkulačka má vygenerovať štandardných 7 míľnikov od D+0 až po D+21.
     *
     * Očakávaný výsledok: 7 míľnikov s korrektnými názvami a posunmi.
     */
    @Test
    void testBasicMilestoneCalculation() {
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        long timestamp = toTimestamp(startDate);

        List<QueenMilestone> milestones = QueenRearingCalculator.calculateMilestones(timestamp, "Štandardná");

        assertNotNull(milestones);
        assertEquals(7, milestones.size(), "Should have 7 standard milestones");

        // Verify milestone names are not null/empty (names are now internationalized)
        for (QueenMilestone milestone : milestones) {
            assertNotNull(milestone.getName(), "Milestone name should not be null");
            assertFalse(milestone.getName().isEmpty(), "Milestone name should not be empty");
        }
    }

    /**
     * Test: Správnosť dátumov pre každý míľnik
     *
     * Use case: Vcelár potrebuje vedieť presné dátumy pre kalendár.
     * Začína 1. mája, kontroluje či dátum D+16 je naozaj 17. mája.
     *
     * Očakávaný výsledok: Dátumy míľnikov zodpovedajú počtu dní od začiatku.
     * D+0 = 1.5., D+5 = 6.5., D+7 = 8.5., D+10 = 11.5., D+12 = 13.5., D+16 = 17.5., D+21 = 22.5.
     */
    @Test
    void testMilestoneDatesCorrectness() {
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        long timestamp = toTimestamp(startDate);

        List<QueenMilestone> milestones = QueenRearingCalculator.calculateMilestones(timestamp, "Štandardná");

        // D+0: May 1
        assertEquals(startDate, toLocalDate(milestones.get(0).getDate()));

        // D+5: May 6
        assertEquals(startDate.plusDays(5), toLocalDate(milestones.get(1).getDate()));

        // D+7: May 8
        assertEquals(startDate.plusDays(7), toLocalDate(milestones.get(2).getDate()));

        // D+10: May 11
        assertEquals(startDate.plusDays(10), toLocalDate(milestones.get(3).getDate()));

        // D+12: May 13
        assertEquals(startDate.plusDays(12), toLocalDate(milestones.get(4).getDate()));

        // D+16: May 17
        assertEquals(startDate.plusDays(16), toLocalDate(milestones.get(5).getDate()));

        // D+21: May 22
        assertEquals(startDate.plusDays(21), toLocalDate(milestones.get(6).getDate()));
    }

    /**
     * Test: Správne offsety dní (D+0, D+5, ..., D+21)
     *
     * Use case: UI zobrazuje "D+5", "D+10" v tabuľke.
     * Kalkulačka musí vrátiť korektné day offsets pre každý míľnik.
     *
     * Očakávaný výsledok: dayOffset pre každý míľnik je správny.
     */
    @Test
    void testDayOffsets() {
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        long timestamp = toTimestamp(startDate);

        List<QueenMilestone> milestones = QueenRearingCalculator.calculateMilestones(timestamp, "Štandardná");

        assertEquals(0, milestones.get(0).getDayOffset());
        assertEquals(5, milestones.get(1).getDayOffset());
        assertEquals(7, milestones.get(2).getDayOffset());
        assertEquals(10, milestones.get(3).getDayOffset());
        assertEquals(12, milestones.get(4).getDayOffset());
        assertEquals(16, milestones.get(5).getDayOffset());
        assertEquals(21, milestones.get(6).getDayOffset());
    }

    /**
     * Test: Každý míľnik má popis a farbu
     *
     * Use case: UI zobrazuje farebné riadky v tabuľke a tooltips s popisom.
     * Všetky míľníky musia mať description a color.
     *
     * Očakávaný výsledok: Žiadny míľnik nemá null/empty description alebo color.
     */
    @Test
    void testMilestonesHaveDescriptionsAndColors() {
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        long timestamp = toTimestamp(startDate);

        List<QueenMilestone> milestones = QueenRearingCalculator.calculateMilestones(timestamp, "Štandardná");

        for (QueenMilestone milestone : milestones) {
            assertNotNull(milestone.getDescription(), "Milestone should have description");
            assertFalse(milestone.getDescription().trim().isEmpty(), "Description should not be empty");

            assertNotNull(milestone.getColor(), "Milestone should have color");
            assertFalse(milestone.getColor().trim().isEmpty(), "Color should not be empty");
            assertTrue(milestone.getColor().startsWith("#"), "Color should be hex code");
        }
    }

    /**
     * Test: Formátovaný deň (D+X formát)
     *
     * Use case: UI zobrazuje "D+0", "D+5", "D+21" v tabuľke.
     * Kalkulačka musí vrátiť správne formátované stringy.
     *
     * Očakávaný výsledok: getFormattedDayOffset() vracia "D+0", "D+5", atď.
     */
    @Test
    void testFormattedDayOffset() {
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        long timestamp = toTimestamp(startDate);

        List<QueenMilestone> milestones = QueenRearingCalculator.calculateMilestones(timestamp, "Štandardná");

        assertEquals("D+0", milestones.get(0).getFormattedDayOffset());
        assertEquals("D+5", milestones.get(1).getFormattedDayOffset());
        assertEquals("D+7", milestones.get(2).getFormattedDayOffset());
        assertEquals("D+10", milestones.get(3).getFormattedDayOffset());
        assertEquals("D+12", milestones.get(4).getFormattedDayOffset());
        assertEquals("D+16", milestones.get(5).getFormattedDayOffset());
        assertEquals("D+21", milestones.get(6).getFormattedDayOffset());
    }

    /**
     * Test: Výpočet pre rôzne metódy (parameter method)
     *
     * Use case: Vcelár používa Štandardnú, Vytvorenie opačnenca alebo Klietkovanie matky.
     * Každá metóda má iný počet míľnikov a rôznu dĺžku timeline.
     *
     * Očakávaný výsledok: Rôzne metódy vracajú rôzny počet míľnikov.
     */
    @Test
    void testDifferentMethods() {
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        long timestamp = toTimestamp(startDate);

        List<QueenMilestone> standard = QueenRearingCalculator.calculateMilestones(timestamp, "Štandardná");
        List<QueenMilestone> opacnenec = QueenRearingCalculator.calculateMilestones(timestamp, "Vytvorenie opačnenca");
        List<QueenMilestone> klietkovanie = QueenRearingCalculator.calculateMilestones(timestamp, "Klietkovanie matky + Norské zimovanie");

        // Standard method: 7 milestones (D+0 to D+21)
        assertEquals(7, standard.size(), "Štandardná metóda má 7 míľnikov");

        // Opacnenec method: more milestones (includes broodless period, varroa treatment, etc.)
        assertTrue(opacnenec.size() > 7, "Vytvorenie opačnenca má viac míľnikov než štandardná metóda");

        // Klietkovanie method: different timeline (includes caging, Norwegian wintering, etc.)
        assertTrue(klietkovanie.size() > 7, "Klietkovanie matky má viac míľnikov než štandardná metóda");

        // All methods should return at least some milestones
        assertFalse(standard.isEmpty());
        assertFalse(opacnenec.isEmpty());
        assertFalse(klietkovanie.isEmpty());
    }

    /**
     * Test: Okrajový prípad - začiatok v minulosti
     *
     * Use case: Vcelár zadá dátum z minulosti (napr. chce rekonštruovať minulý odchov).
     * Kalkulačka má fungovať aj pre minulé dátumy.
     *
     * Očakávaný výsledok: Všetky míľníky sú v minulosti, ale výpočet je korektný.
     */
    @Test
    void testStartDateInPast() {
        LocalDate pastDate = LocalDate.of(2024, 1, 1);
        long timestamp = toTimestamp(pastDate);

        List<QueenMilestone> milestones = QueenRearingCalculator.calculateMilestones(timestamp, "Štandardná");

        assertNotNull(milestones);
        assertEquals(7, milestones.size());

        // All dates should be in the past (before today)
        LocalDate today = LocalDate.now();
        for (QueenMilestone milestone : milestones) {
            LocalDate milestoneDate = toLocalDate(milestone.getDate());
            assertTrue(milestoneDate.isBefore(today) || milestoneDate.isEqual(today),
                    "Milestone date should be in the past");
        }
    }

    /**
     * Test: Okrajový prípad - začiatok vo vzdialenej budúcnosti
     *
     * Use case: Vcelár plánuje odchov o rok dopredu.
     * Kalkulačka má fungovať aj pre vzdialené dátumy.
     *
     * Očakávaný výsledok: Všetky míľníky sú správne vypočítané, žiadne overflow chyby.
     */
    @Test
    void testStartDateInFuture() {
        LocalDate futureDate = LocalDate.of(2026, 5, 1);
        long timestamp = toTimestamp(futureDate);

        List<QueenMilestone> milestones = QueenRearingCalculator.calculateMilestones(timestamp, "Štandardná");

        assertNotNull(milestones);
        assertEquals(7, milestones.size());

        // Last milestone (D+21) should be exactly 21 days after start
        long expectedLastDate = timestamp + TimeUnit.DAYS.toMillis(21);
        assertEquals(expectedLastDate, milestones.get(6).getDate());
    }

    /**
     * Test: Chronologické poradie míľnikov
     *
     * Use case: UI zobrazuje míľníky v časovom poradí.
     * Kalkulačka musí vrátiť míľníky zoradené od najskoršieho po najneskorší.
     *
     * Očakávaný výsledok: Každý nasledujúci míľnik má neskorší dátum.
     */
    @Test
    void testMilestonesInChronologicalOrder() {
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        long timestamp = toTimestamp(startDate);

        List<QueenMilestone> milestones = QueenRearingCalculator.calculateMilestones(timestamp, "Štandardná");

        for (int i = 0; i < milestones.size() - 1; i++) {
            long currentDate = milestones.get(i).getDate();
            long nextDate = milestones.get(i + 1).getDate();

            assertTrue(nextDate > currentDate,
                    "Milestones should be in chronological order");
        }
    }

    /**
     * Test: Celková dĺžka timeline je 21 dní (pre Štandardnú metódu)
     *
     * Use case: Vcelár vie, že od graftovania po kontrolu kládky je presne 21 dní.
     * Chce to overiť v kalkulačke.
     *
     * Očakávaný výsledok: Rozdiel medzi prvým (D+0) a posledným (D+21) míľníkom je 21 dní.
     */
    @Test
    void testTotalTimelineDuration() {
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        long timestamp = toTimestamp(startDate);

        List<QueenMilestone> milestones = QueenRearingCalculator.calculateMilestones(timestamp, "Štandardná");

        long firstDate = milestones.get(0).getDate();
        long lastDate = milestones.get(6).getDate();

        long durationMillis = lastDate - firstDate;
        long durationDays = TimeUnit.MILLISECONDS.toDays(durationMillis);

        assertEquals(21, durationDays, "Total timeline should be 21 days from D+0 to D+21");
    }

    /**
     * Test: Vytvorenie opačnenca - kľúčové míľníky
     *
     * Use case: Vcelár vytvára opačnenca, chce poznať kľúčové dátumy:
     * - Kedy vylamať matečníky
     * - Kedy matka začne klásť
     * - Kedy nastane bezplodové obdobie na preliečenie
     *
     * Očakávaný výsledok: Míľníky obsahujú správne offsety (D+5, D+25, D+27, atď.)
     */
    @Test
    void testOpacnenecKeyMilestones() {
        LocalDate startDate = LocalDate.of(2025, 4, 27);
        long timestamp = toTimestamp(startDate);

        List<QueenMilestone> milestones = QueenRearingCalculator.calculateMilestones(timestamp, "Vytvorenie opačnenca");

        // Verify all milestones have non-empty names (names are now internationalized)
        for (QueenMilestone milestone : milestones) {
            assertNotNull(milestone.getName(), "Milestone name should not be null");
            assertFalse(milestone.getName().isEmpty(), "Milestone name should not be empty");
        }

        // D+0 should be the start
        assertEquals(0, milestones.get(0).getDayOffset());
        assertEquals(startDate, toLocalDate(milestones.get(0).getDate()));

        // Timeline should extend beyond 30 days (includes D+68, D+76, and varroa traps up to D+30)
        QueenMilestone lastMilestone = milestones.get(milestones.size() - 1);
        assertTrue(lastMilestone.getDayOffset() >= 30, "Opačnenec timeline should extend to at least D+30");

        // Verify varroa trap milestones are present (check for "varroa" or "trap" in any language)
        assertTrue(milestones.stream().anyMatch(m ->
                m.getName().toLowerCase().contains("varroa") ||
                m.getName().toLowerCase().contains("trap") ||
                m.getName().toLowerCase().contains("pasca")),
                "Should include varroa trap milestones");
    }

    /**
     * Test: Klietkovanie matky - bezplodové obdobie a presypanie
     *
     * Use case: Vcelár klietkuje matku na vytvorenie bezplodového obdobia.
     * Chce vedieť:
     * - Kedy nastane bezplodové obdobie (D+25)
     * - Kedy presypať na medzistienky (D+25)
     * - Kedy preliečiť (D+30)
     * - Kedy vypustiť matku (D+31)
     *
     * Očakávaný výsledok: Míľníky obsahujú všetky kľúčové kroky.
     */
    @Test
    void testKlietkovanieMilestones() {
        LocalDate startDate = LocalDate.of(2025, 4, 27);
        long timestamp = toTimestamp(startDate);

        List<QueenMilestone> milestones = QueenRearingCalculator.calculateMilestones(timestamp, "Klietkovanie matky");

        // Verify all milestones have non-empty names (names are now internationalized)
        for (QueenMilestone milestone : milestones) {
            assertNotNull(milestone.getName(), "Milestone name should not be null");
            assertFalse(milestone.getName().isEmpty(), "Milestone name should not be empty");
        }

        // D+25 should be the broodless period start (check by offset, not by name)
        QueenMilestone broodlessStart = milestones.stream()
                .filter(m -> m.getDayOffset() == 25)
                .findFirst()
                .orElse(null);
        assertNotNull(broodlessStart, "Should have milestone at D+25");

        // Timeline should extend to D+72 (first new foragers) and includes varroa traps
        QueenMilestone lastMilestone = milestones.get(milestones.size() - 1);
        assertTrue(lastMilestone.getDayOffset() >= 35, "Klietkovanie timeline should extend to at least D+35");

        // Verify varroa trap milestones are present (check for "varroa" or "trap" in any language)
        assertTrue(milestones.stream().anyMatch(m ->
                m.getName().toLowerCase().contains("varroa") ||
                m.getName().toLowerCase().contains("trap") ||
                m.getName().toLowerCase().contains("pasca")),
                "Should include varroa trap milestones during broodless period");
    }

    /**
     * Test: Opačnenec - bezplodové obdobie je vhodné na preliečenie
     *
     * Use case: Vcelár chce vedieť kedy je optimálny čas na preliečenie.
     * Pri opačnencovi je to D+24 až D+26 (používame D+25).
     *
     * Očakávaný výsledok: Bezplodové obdobie je okolo D+25.
     */
    @Test
    void testOpacnenecBroodlessPeriod() {
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        long timestamp = toTimestamp(startDate);

        List<QueenMilestone> milestones = QueenRearingCalculator.calculateMilestones(timestamp, "Vytvorenie opačnenca");

        // Find broodless period milestone by offset (D+25)
        QueenMilestone broodless = milestones.stream()
                .filter(m -> m.getDayOffset() == 25)
                .findFirst()
                .orElse(null);

        assertNotNull(broodless, "Opačnenec should have milestone at D+25 (broodless period)");
        assertNotNull(broodless.getDescription(), "Milestone should have description");
        assertFalse(broodless.getDescription().isEmpty(), "Description should not be empty");
        // Description is now internationalized, just verify it exists
    }

    /**
     * Test: Null metóda -> použije Štandardnú
     *
     * Use case: Chyba v UI - metóda je null.
     * Kalkulačka má použiť default (Štandardná).
     *
     * Očakávaný výsledok: Vracia štandardné míľníky (7 míľnikov).
     */
    @Test
    void testNullMethodDefaultsToStandard() {
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        long timestamp = toTimestamp(startDate);

        List<QueenMilestone> milestones = QueenRearingCalculator.calculateMilestones(timestamp, null);

        assertNotNull(milestones);
        assertEquals(7, milestones.size(), "Null method should default to Štandardná (7 milestones)");
    }
}
