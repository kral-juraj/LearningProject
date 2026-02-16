package com.beekeeper.desktop.calculator;

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
        // Calculate needed amount
        double neededKg = targetStores - currentStores;

        if (neededKg <= 0) {
            return new FeedResult(0, 0, 0, 0,
                    "Včelstvo má dostatok zásob. Krmenie nie je potrebné.");
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
                instructions = "Fondant je už hotový výrobok. Podávajte priamo na rámky.";
                break;

            default:
                return new FeedResult(0, 0, 0, 0, "Neznámy typ krmiva.");
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
        StringBuilder sb = new StringBuilder();

        sb.append("Príprava ").append(feedType).append(":\n\n");
        sb.append(String.format("1. Zohrejte %.1f L vody na 50-60°C\n", waterLiters));
        sb.append(String.format("2. Postupne rozpúšťajte %.1f kg cukru za miešania\n", sugarKg));
        sb.append("3. Miešajte až do úplného rozpustenia\n");
        sb.append("4. Nechajte vychladnúť na 30-35°C\n");
        sb.append("5. Podávajte do kŕmidiel večer\n\n");

        if (feedType.equals(SYRUP_1_1)) {
            sb.append("Poznámka: Sirup 1:1 je vhodný na jarné a letné prikrmovanie.");
        } else if (feedType.equals(SYRUP_3_2)) {
            sb.append("Poznámka: Sirup 3:2 je vhodný na jesenné zakrmovanie na zimu.");
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
