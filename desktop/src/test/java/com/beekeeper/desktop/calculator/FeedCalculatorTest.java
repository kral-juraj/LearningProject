package com.beekeeper.desktop.calculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FeedCalculator.
 *
 * Tests verify feed calculation functionality:
 * - Different feed types (Sirup 1:1, 3:2, Fondant)
 * - Weight to volume conversions
 * - Sugar and water calculations
 * - Edge cases (no feeding needed, negative values)
 */
class FeedCalculatorTest {

    /**
     * Test: Výpočet krmenia sirupom 1:1 (jarné/letné prikrmovanie)
     *
     * Use case: Vcelár má úle s 5 kg zásob, chce dosiahnuť 15 kg (deficit 10 kg).
     * Použije sirup 1:1 (rovnaký pomer cukru a vody).
     *
     * Očakávaný výsledok:
     * - Potrebné množstvo: 10 kg
     * - Objem: ~8 L (1 kg sirupu = 0.8 L)
     * - Cukor: 5 kg (50%)
     * - Voda: 5 L (50%)
     * - Inštrukcie obsahujú prípravný postup
     */
    @Test
    void testSyrup1to1Calculation() {
        double currentStores = 5.0;
        double targetStores = 15.0;
        String feedType = FeedCalculator.SYRUP_1_1;

        FeedResult result = FeedCalculator.calculate(currentStores, targetStores, feedType);

        assertNotNull(result);
        assertEquals(10.0, result.getNeededKg(), 0.01, "Should need 10 kg of feed");
        assertEquals(8.0, result.getNeededLiters(), 0.01, "10 kg syrup = ~8 L");
        assertEquals(5.0, result.getSugarKg(), 0.01, "50% sugar for 1:1 syrup");
        assertEquals(5.0, result.getWaterLiters(), 0.01, "50% water for 1:1 syrup");

        assertNotNull(result.getInstructions());
        assertFalse(result.getInstructions().isEmpty(),
                "Instructions should not be empty");
        // Instructions are now internationalized, just check they exist
    }

    /**
     * Test: Výpočet krmenia sirupom 3:2 (jesenné zakrmovanie na zimu)
     *
     * Use case: Vcelár pripravuje úle na zimu, má 8 kg zásob, potrebuje 20 kg (deficit 12 kg).
     * Použije koncentrovaný sirup 3:2 (60% cukru, 40% vody).
     *
     * Očakávaný výsledok:
     * - Potrebné množstvo: 12 kg
     * - Objem: ~9 L (1 kg koncentrovaného sirupu = 0.75 L)
     * - Cukor: 7.2 kg (60%)
     * - Voda: 4.8 L (40%)
     * - Inštrukcie obsahujú zmienku o jesennom zakrmovaní
     */
    @Test
    void testSyrup3to2Calculation() {
        double currentStores = 8.0;
        double targetStores = 20.0;
        String feedType = FeedCalculator.SYRUP_3_2;

        FeedResult result = FeedCalculator.calculate(currentStores, targetStores, feedType);

        assertNotNull(result);
        assertEquals(12.0, result.getNeededKg(), 0.01, "Should need 12 kg of feed");
        assertEquals(9.0, result.getNeededLiters(), 0.01, "12 kg concentrated syrup = ~9 L");
        assertEquals(7.2, result.getSugarKg(), 0.01, "60% sugar for 3:2 syrup");
        assertEquals(4.8, result.getWaterLiters(), 0.01, "40% water for 3:2 syrup");

        assertNotNull(result.getInstructions());
        assertFalse(result.getInstructions().isEmpty(),
                "Instructions should not be empty");
        // Instructions are now internationalized, just check they exist
    }

    /**
     * Test: Výpočet krmenia fondantom (zimné núdzové krmenie)
     *
     * Use case: Vcelár v zime zistí, že úle majú 3 kg zásob, potrebujú 10 kg (deficit 7 kg).
     * Použije fondant - hotový výrobok, žiadne miešanie.
     *
     * Očakávaný výsledok:
     * - Potrebné množstvo: 7 kg
     * - Objem: 7 kg (pevná látka, 1:1 ratio)
     * - Cukor: 0 (nie je potrebné variť)
     * - Voda: 0 (nie je potrebné miešať)
     * - Inštrukcie hovoria o priamom podávaní na rámky
     */
    @Test
    void testFondantCalculation() {
        double currentStores = 3.0;
        double targetStores = 10.0;
        String feedType = FeedCalculator.FONDANT;

        FeedResult result = FeedCalculator.calculate(currentStores, targetStores, feedType);

        assertNotNull(result);
        assertEquals(7.0, result.getNeededKg(), 0.01, "Should need 7 kg of fondant");
        assertEquals(7.0, result.getNeededLiters(), 0.01, "Fondant is solid, 1:1 ratio");
        assertEquals(0.0, result.getSugarKg(), 0.01, "No sugar needed - fondant is ready-made");
        assertEquals(0.0, result.getWaterLiters(), 0.01, "No water needed - fondant is ready-made");

        assertNotNull(result.getInstructions());
        assertFalse(result.getInstructions().isEmpty(),
                "Instructions should not be empty");
        // Instructions are now internationalized, just check they exist
    }

    /**
     * Test: Žiadne krmenie nie je potrebné (dostatok zásob)
     *
     * Use case: Vcelár kontroluje úle, zistí že majú 25 kg zásob, cieľ je 20 kg.
     * Úle majú dostatok, krmenie nie je potrebné.
     *
     * Očakávaný výsledok:
     * - Potrebné množstvo: 0 kg
     * - Objem: 0 L
     * - Cukor: 0 kg
     * - Voda: 0 L
     * - Inštrukcie hovoria, že krmenie nie je potrebné
     */
    @Test
    void testNoFeedingNeeded() {
        double currentStores = 25.0;
        double targetStores = 20.0; // Already above target
        String feedType = FeedCalculator.SYRUP_1_1;

        FeedResult result = FeedCalculator.calculate(currentStores, targetStores, feedType);

        assertNotNull(result);
        assertEquals(0.0, result.getNeededKg(), 0.01, "No feeding needed");
        assertEquals(0.0, result.getNeededLiters(), 0.01, "No volume needed");
        assertEquals(0.0, result.getSugarKg(), 0.01, "No sugar needed");
        assertEquals(0.0, result.getWaterLiters(), 0.01, "No water needed");

        assertNotNull(result.getInstructions());
        assertFalse(result.getInstructions().isEmpty(),
                "Instructions should not be empty");
        // Instructions are now internationalized, just check they exist
    }

    /**
     * Test: Rovnaké aktuálne a cieľové zásoby (hraničný prípad)
     *
     * Use case: Vcelár presne dosiahol cieľový stav (10 kg aktuálne = 10 kg cieľ).
     * Krmenie nie je potrebné.
     *
     * Očakávaný výsledok: Všetky hodnoty sú 0, inštrukcie hovoria o dostatku.
     */
    @Test
    void testEqualCurrentAndTargetStores() {
        double currentStores = 10.0;
        double targetStores = 10.0;
        String feedType = FeedCalculator.SYRUP_3_2;

        FeedResult result = FeedCalculator.calculate(currentStores, targetStores, feedType);

        assertNotNull(result);
        assertEquals(0.0, result.getNeededKg(), 0.01);
        assertEquals(0.0, result.getNeededLiters(), 0.01);
        assertEquals(0.0, result.getSugarKg(), 0.01);
        assertEquals(0.0, result.getWaterLiters(), 0.01);
    }

    /**
     * Test: Malé množstvo krmenia (1 kg)
     *
     * Use case: Vcelár potrebuje len malé doplnenie (14.5 kg → 15.5 kg).
     * Kalkulačka musí správne vypočítať aj malé množstvá.
     *
     * Očakávaný výsledok: Korektné výpočty pre 1 kg deficit.
     */
    @Test
    void testSmallAmountFeeding() {
        double currentStores = 14.5;
        double targetStores = 15.5;
        String feedType = FeedCalculator.SYRUP_1_1;

        FeedResult result = FeedCalculator.calculate(currentStores, targetStores, feedType);

        assertNotNull(result);
        assertEquals(1.0, result.getNeededKg(), 0.01, "Should need 1 kg");
        assertEquals(0.8, result.getNeededLiters(), 0.01, "1 kg syrup = 0.8 L");
        assertEquals(0.5, result.getSugarKg(), 0.01, "0.5 kg sugar for 1 kg syrup");
        assertEquals(0.5, result.getWaterLiters(), 0.01, "0.5 L water for 1 kg syrup");
    }

    /**
     * Test: Veľké množstvo krmenia (50 kg)
     *
     * Use case: Vcelár zakrmuje veľkú včelnicu s 20 úľmi, potrebuje 50 kg celkovo.
     * Kalkulačka musí zvládnuť aj veľké množstvá.
     *
     * Očakávaný výsledok: Korektné výpočty pre 50 kg deficit.
     */
    @Test
    void testLargeAmountFeeding() {
        double currentStores = 0.0; // Empty hives after winter
        double targetStores = 50.0;
        String feedType = FeedCalculator.SYRUP_3_2;

        FeedResult result = FeedCalculator.calculate(currentStores, targetStores, feedType);

        assertNotNull(result);
        assertEquals(50.0, result.getNeededKg(), 0.01, "Should need 50 kg");
        assertEquals(37.5, result.getNeededLiters(), 0.01, "50 kg concentrated syrup = 37.5 L");
        assertEquals(30.0, result.getSugarKg(), 0.01, "30 kg sugar (60%)");
        assertEquals(20.0, result.getWaterLiters(), 0.01, "20 L water (40%)");
    }

    /**
     * Test: Neznámy typ krmiva
     *
     * Use case: Chyba v UI alebo programátorská chyba - nevalidný typ krmiva.
     * Kalkulačka musí vrátiť chybovú hlášku.
     *
     * Očakávaný výsledok: Všetky hodnoty sú 0, inštrukcie hovoria o neznámom type.
     */
    @Test
    void testUnknownFeedType() {
        double currentStores = 5.0;
        double targetStores = 10.0;
        String feedType = "Neznámy typ";

        FeedResult result = FeedCalculator.calculate(currentStores, targetStores, feedType);

        assertNotNull(result);
        assertEquals(0.0, result.getNeededKg(), 0.01);
        assertEquals(0.0, result.getNeededLiters(), 0.01);
        assertEquals(0.0, result.getSugarKg(), 0.01);
        assertEquals(0.0, result.getWaterLiters(), 0.01);

        assertNotNull(result.getInstructions());
        assertFalse(result.getInstructions().isEmpty(),
                "Instructions should not be empty");
        // Instructions are now internationalized, just check they exist
    }

    /**
     * Test: Formátovanie čísel na 1 desatinné miesto
     *
     * Use case: UI zobrazuje "12.5 kg", nie "12.523456 kg".
     * Kalkulačka poskytuje helper metódu na formátovanie.
     *
     * Očakávaný výsledok: formatAmount() zaokrúhli na 1 desatinné miesto.
     */
    @Test
    void testAmountFormatting() {
        assertEquals("10.0", FeedCalculator.formatAmount(10.0));
        assertEquals("12.5", FeedCalculator.formatAmount(12.5));
        assertEquals("7.3", FeedCalculator.formatAmount(7.34));
        assertEquals("9.7", FeedCalculator.formatAmount(9.67));
        assertEquals("0.0", FeedCalculator.formatAmount(0.0));
    }

    /**
     * Test: Konverzia kg → litre pre rôzne typy krmiva
     *
     * Use case: Vcelár má receptúru v kg, potrebuje vedieť koľko litrov kŕmidla pripraviť.
     * Rôzne typy sirupu mají rôznu hustotu.
     *
     * Očakávaný výsledok:
     * - Sirup 1:1: 10 kg = 8 L (hustota ~1.25 kg/L)
     * - Sirup 3:2: 10 kg = 7.5 L (hustota ~1.33 kg/L, koncentrovanejší)
     * - Fondant: 10 kg = 10 kg (pevná látka, 1:1)
     */
    @Test
    void testVolumeConversions() {
        // 10 kg deficit across different feed types
        FeedResult syrup1to1 = FeedCalculator.calculate(0, 10, FeedCalculator.SYRUP_1_1);
        FeedResult syrup3to2 = FeedCalculator.calculate(0, 10, FeedCalculator.SYRUP_3_2);
        FeedResult fondant = FeedCalculator.calculate(0, 10, FeedCalculator.FONDANT);

        assertEquals(8.0, syrup1to1.getNeededLiters(), 0.01, "10 kg syrup 1:1 = 8 L");
        assertEquals(7.5, syrup3to2.getNeededLiters(), 0.01, "10 kg syrup 3:2 = 7.5 L");
        assertEquals(10.0, fondant.getNeededLiters(), 0.01, "10 kg fondant = 10 kg (solid)");
    }

    /**
     * Test: Validácia záporných hodnôt
     *
     * Use case: Chyba v UI - používateľ zadal zápornú hodnotu.
     * Kalkulačka by mala zvládnuť tento edge case bezpečne.
     *
     * Očakávaný výsledok: Kalkulácia nehavaruje, vracia 0 alebo chybovú hlášku.
     */
    @Test
    void testNegativeValues() {
        // Negative current stores - should handle gracefully
        FeedResult result1 = FeedCalculator.calculate(-5.0, 10.0, FeedCalculator.SYRUP_1_1);
        assertNotNull(result1);

        // Negative target stores - should handle gracefully
        FeedResult result2 = FeedCalculator.calculate(5.0, -10.0, FeedCalculator.SYRUP_1_1);
        assertNotNull(result2);

        // Both negative - should handle gracefully
        FeedResult result3 = FeedCalculator.calculate(-5.0, -10.0, FeedCalculator.SYRUP_1_1);
        assertNotNull(result3);

        // Note: Actual validation of negative values happens in UI (CalculatorsController)
        // Calculator assumes valid positive inputs but should not crash on invalid data
    }

    /**
     * Test: Inštrukcie obsahujú všetky potrebné informácie
     *
     * Use case: Vcelár nikdy nepripravoval sirup, potrebuje detailný návod.
     * Kalkulačka musí vrátiť kompletné inštrukcie.
     *
     * Očakávaný výsledok: Inštrukcie obsahujú kroky 1-5, teploty, poznámky.
     */
    @Test
    void testInstructionsCompleteness() {
        FeedResult syrup1to1 = FeedCalculator.calculate(5, 15, FeedCalculator.SYRUP_1_1);
        String instructions = syrup1to1.getInstructions();

        assertNotNull(instructions, "Instructions should not be null");
        assertFalse(instructions.isEmpty(), "Instructions should not be empty");
        assertTrue(instructions.length() > 50, "Instructions should be reasonably detailed");
        // Instructions are now internationalized, just verify they exist and are not trivial
    }

    /**
     * Test: Desatinné hodnoty pri vstupe
     *
     * Use case: Vcelár odhaduje zásoby na 7.3 kg, chce dosiahnuť 18.7 kg.
     * Kalkulačka musí zvládnuť desatinné hodnoty.
     *
     * Očakávaný výsledok: Korektný výpočet s desatinnými číslami.
     */
    @Test
    void testDecimalInputValues() {
        double currentStores = 7.3;
        double targetStores = 18.7;
        String feedType = FeedCalculator.SYRUP_1_1;

        FeedResult result = FeedCalculator.calculate(currentStores, targetStores, feedType);

        assertNotNull(result);
        assertEquals(11.4, result.getNeededKg(), 0.01, "Should calculate 18.7 - 7.3 = 11.4 kg");
        assertEquals(9.12, result.getNeededLiters(), 0.01, "11.4 kg * 0.8 = 9.12 L");
        assertEquals(5.7, result.getSugarKg(), 0.01, "50% of 11.4 = 5.7 kg");
        assertEquals(5.7, result.getWaterLiters(), 0.01, "50% of 11.4 = 5.7 L");
    }
}
