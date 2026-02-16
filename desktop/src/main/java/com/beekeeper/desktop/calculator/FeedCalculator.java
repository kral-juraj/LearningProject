package com.beekeeper.desktop.calculator;

import com.beekeeper.shared.i18n.TranslationManager;

/**
 * Calculator for bee feed requirements.
 * Converts weight to volume and calculates sugar/water ratios for different feed types.
 */
public class FeedCalculator {

    // Feed type constants
    public static final String SYRUP_1_1 = "Sirup 1:1";
    public static final String SYRUP_3_2 = "Sirup 3:2";
    public static final String FONDANT = "Fondant";

    /**
     * Calculates feed requirements based on current and target stores.
     *
     * @param currentStores Current honey stores in kg
     * @param targetStores  Target honey stores in kg
     * @param feedType      Type of feed (SYRUP_1_1, SYRUP_3_2, or FONDANT)
     * @return FeedResult with calculated amounts and instructions
     */
    public static FeedResult calculate(double currentStores, double targetStores, String feedType) {
        TranslationManager tm = TranslationManager.getInstance();

        // Calculate needed amount
        double neededKg = targetStores - currentStores;

        if (neededKg <= 0) {
            return new FeedResult(0, 0, 0, 0, tm.get("feed.no_feeding_needed"));
        }

        // Calculate based on feed type
        double neededLiters;
        double sugarKg;
        double waterLiters;
        String instructions;

        switch (feedType) {
            case SYRUP_1_1:
                neededLiters = convertKgToLiters(neededKg, SYRUP_1_1);
                sugarKg = neededKg * 0.5;
                waterLiters = neededKg * 0.5;
                instructions = buildInstructions(SYRUP_1_1, sugarKg, waterLiters);
                break;

            case SYRUP_3_2:
                neededLiters = convertKgToLiters(neededKg, SYRUP_3_2);
                sugarKg = neededKg * 0.6;
                waterLiters = neededKg * 0.4;
                instructions = buildInstructions(SYRUP_3_2, sugarKg, waterLiters);
                break;

            case FONDANT:
                neededLiters = neededKg; // Fondant is solid, no volume conversion
                sugarKg = 0;
                waterLiters = 0;
                instructions = tm.get("feed.fondant_ready");
                break;

            default:
                return new FeedResult(0, 0, 0, 0, tm.get("feed.unknown_type"));
        }

        return new FeedResult(neededKg, neededLiters, sugarKg, waterLiters, instructions);
    }

    /**
     * Converts weight (kg) to volume (liters) based on feed type.
     */
    private static double convertKgToLiters(double kg, String feedType) {
        switch (feedType) {
            case SYRUP_1_1:
                return kg * 0.8; // 1 kg syrup ≈ 0.8 L
            case SYRUP_3_2:
                return kg * 0.75; // 1 kg concentrated syrup ≈ 0.75 L
            case FONDANT:
                return kg; // Solid, 1:1 ratio
            default:
                return kg;
        }
    }

    /**
     * Builds preparation instructions for syrup.
     */
    private static String buildInstructions(String feedType, double sugarKg, double waterLiters) {
        TranslationManager tm = TranslationManager.getInstance();
        StringBuilder sb = new StringBuilder();

        sb.append(tm.get("feed.preparation", feedType)).append("\n\n");
        sb.append(tm.get("feed.step1", waterLiters)).append("\n");
        sb.append(tm.get("feed.step2", sugarKg)).append("\n");
        sb.append(tm.get("feed.step3")).append("\n");
        sb.append(tm.get("feed.step4")).append("\n");
        sb.append(tm.get("feed.step5")).append("\n\n");

        if (feedType.equals(SYRUP_1_1)) {
            sb.append(tm.get("feed.note_1_1"));
        } else if (feedType.equals(SYRUP_3_2)) {
            sb.append(tm.get("feed.note_3_2"));
        }

        return sb.toString();
    }

    /**
     * Formats a double value to 1 decimal place.
     */
    public static String formatAmount(double value) {
        return String.format("%.1f", value);
    }
}
