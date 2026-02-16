package com.beekeeper.desktop.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Biologically accurate calculator for Varroa destructor population dynamics.
 * Based on peer-reviewed research modeling reproductive cycles, mortality, and brood preferences.
 *
 * Model simulates:
 * - Phoretic mites (on adult bees)
 * - Mites in worker brood cells (12-day cycle)
 * - Mites in drone brood cells (15-day cycle)
 * - Daily mortality and reproduction
 */
public class VarroaCalculator {

    // Treatment thresholds (mite counts)
    private static final int THRESHOLD_OK = 300;
    private static final int THRESHOLD_WARNING = 1000;
    private static final int THRESHOLD_CRITICAL = 3000;

    /**
     * Projects Varroa mite population over specified days using biological model (no treatments).
     *
     * @param initialCount   Initial phoretic mite count from measurement
     * @param measurementDate Date of measurement (milliseconds)
     * @param days           Number of days to project
     * @param broodPresent   Whether brood is present (if false, no reproduction occurs)
     * @param parameters     Biological parameters for the model
     * @return VarroaProjection with daily data points and recommendations
     */
    public static VarroaProjection project(int initialCount, long measurementDate,
                                           int days, boolean broodPresent,
                                           VarroaParameters parameters) {
        return project(initialCount, measurementDate, days, broodPresent, parameters, new ArrayList<>());
    }

    /**
     * Projects Varroa mite population with treatments applied.
     *
     * @param initialCount   Initial phoretic mite count from measurement
     * @param measurementDate Date of measurement (milliseconds)
     * @param days           Number of days to project
     * @param broodPresent   Whether brood is present
     * @param parameters     Biological parameters for the model
     * @param treatments     List of treatments to apply during projection
     * @return VarroaProjection with daily data points and treatment effects
     */
    public static VarroaProjection project(int initialCount, long measurementDate,
                                           int days, boolean broodPresent,
                                           VarroaParameters parameters,
                                           List<VarroaTreatment> treatments) {
        VarroaProjection projection = new VarroaProjection(initialCount, measurementDate, days);

        // Initialize state
        double phoreticMites = initialCount;
        double mitesInWorkerCells = 0;
        double mitesInDroneCells = 0;
        double totalMites = phoreticMites;

        long criticalDate = measurementDate;
        boolean criticalReached = false;

        // Day 0 data point
        projection.getDataPoints().add(
                new VarroaProjection.DataPoint(0, (int) Math.round(totalMites), measurementDate)
        );

        // Simulate each day
        for (int day = 1; day <= days; day++) {
            long dateMillis = measurementDate + TimeUnit.DAYS.toMillis(day);

            // Check if treatment should be applied today
            VarroaTreatment todaysTreatment = null;
            for (VarroaTreatment treatment : treatments) {
                // Check if treatment date matches this day (within same day)
                long daysSinceMeasurement = (treatment.getTreatmentDate() - measurementDate) / TimeUnit.DAYS.toMillis(1);
                if (daysSinceMeasurement == day) {
                    todaysTreatment = treatment;
                    break;
                }
            }

            if (broodPresent) {
                // === CELL INVASION ===
                double invadingMites = phoreticMites * parameters.getCellInvasionRate();

                double droneAvailability = parameters.getDroneBroodPercentage();
                double dronePreference = parameters.getDronePreference();

                double totalWeight = (droneAvailability * dronePreference) + (1.0 - droneAvailability);
                double probDroneEntry = (droneAvailability * dronePreference) / totalWeight;

                double mitesToDrone = invadingMites * probDroneEntry;
                double mitesToWorker = invadingMites * (1.0 - probDroneEntry);

                mitesInDroneCells += mitesToDrone;
                mitesInWorkerCells += mitesToWorker;
                phoreticMites -= invadingMites;

                // === EMERGENCE & REPRODUCTION ===
                double workerCycleLength = parameters.getWorkerCycleLength();
                double emergingWorkerMites = mitesInWorkerCells / workerCycleLength;

                double droneCycleLength = parameters.getDroneCycleLength();
                double emergingDroneMites = mitesInDroneCells / droneCycleLength;

                double workerOffspring = emergingWorkerMites *
                        parameters.getWorkerOffspring() *
                        (1.0 - parameters.getWorkerFailureRate());

                double droneOffspring = emergingDroneMites *
                        parameters.getDroneOffspring() *
                        (1.0 - parameters.getDroneFailureRate());

                phoreticMites += emergingWorkerMites + workerOffspring;
                phoreticMites += emergingDroneMites + droneOffspring;

                mitesInWorkerCells -= emergingWorkerMites;
                mitesInDroneCells -= emergingDroneMites;

                // === MORTALITY ===
                phoreticMites *= (1.0 - parameters.getPhoreticMortalityPerDay());
            } else {
                phoreticMites *= (1.0 - parameters.getPhoreticMortalityPerDay());
            }

            // === APPLY TREATMENT ===
            if (todaysTreatment != null) {
                double effectiveness = todaysTreatment.getEffectiveness();
                phoreticMites *= (1.0 - effectiveness);
                mitesInWorkerCells *= (1.0 - effectiveness);
                mitesInDroneCells *= (1.0 - effectiveness);
            }

            // Calculate total mite population
            totalMites = phoreticMites + mitesInWorkerCells + mitesInDroneCells;

            // Record data point
            int roundedTotal = (int) Math.round(totalMites);
            projection.getDataPoints().add(
                    new VarroaProjection.DataPoint(day, roundedTotal, dateMillis)
            );

            // Track when critical threshold is reached
            if (!criticalReached && roundedTotal >= THRESHOLD_CRITICAL) {
                criticalDate = dateMillis;
                criticalReached = true;
            }
        }

        // Get final count
        int finalCount = projection.getDataPoints().get(days).getCount();

        // Set status and recommendation
        String status = getStatus(finalCount);
        String recommendation = getRecommendation(finalCount, days, broodPresent, parameters);

        projection.setStatus(status);
        projection.setRecommendation(recommendation);
        projection.setCriticalDate(criticalDate);

        return projection;
    }

    /**
     * Overload for backward compatibility (uses default parameters).
     */
    public static VarroaProjection project(int initialCount, long measurementDate,
                                           int days, boolean broodPresent) {
        return project(initialCount, measurementDate, days, broodPresent,
                VarroaParameters.createDefault());
    }

    /**
     * Determines status based on projected count.
     */
    private static String getStatus(int count) {
        if (count < THRESHOLD_OK) {
            return "OK";
        } else if (count < THRESHOLD_WARNING) {
            return "VAROVANIE";
        } else if (count < THRESHOLD_CRITICAL) {
            return "VAROVANIE";
        } else {
            return "KRITICKÉ";
        }
    }

    /**
     * Generates treatment recommendation based on projected count.
     */
    private static String getRecommendation(int projectedCount, int days, boolean broodPresent,
                                            VarroaParameters params) {
        StringBuilder sb = new StringBuilder();

        sb.append("Projekcia na ").append(days).append(" dní: ").append(projectedCount).append(" kliešťov\n");
        sb.append("Model: ").append(broodPresent ? "s plodom" : "bez plodu").append("\n");
        sb.append("Trúdie bunky: ").append(String.format("%.0f%%", params.getDroneBroodPercentage() * 100)).append("\n\n");

        if (projectedCount < THRESHOLD_OK) {
            sb.append("✓ Žiadna akcia potrebná\n");
            sb.append("Populácia kliešťov je pod kontrolou.\n");
            sb.append("Pokračujte v pravidelnom monitoringu každých 30 dní.");
        } else if (projectedCount < THRESHOLD_WARNING) {
            sb.append("⚠ Sledovať situáciu\n");
            sb.append("Populácia kliešťov rastie.\n");
            sb.append("Kontrolujte každých 14-21 dní.\n");
            sb.append("Pripravte si ošetrovacie prostriedky.\n\n");
            sb.append("Odporúčanie: Zvážte odstránenie trúdieho plodu.");
        } else if (projectedCount < THRESHOLD_CRITICAL) {
            sb.append("⚠ Plánovať ošetrenie\n");
            sb.append("Populácia kliešťov ohrozuje zdravie včelstva.\n");
            sb.append("Odporúčame ošetrenie v najbližších 7-14 dňoch.\n\n");
            sb.append("Možnosti:\n");
            sb.append("- Organické kyseliny (mravčia, šťavelová)\n");
            sb.append("- Odstrániť všetok trúdí plod\n");
            sb.append("- Biotechnické metódy (plodová prestávka)");
        } else {
            sb.append("🔴 KRITICKÉ - Ošetriť ihneď!\n");
            sb.append("Vysoká populácia kliešťov!\n");
            sb.append("Včelstvo je v ohrození kolapsu.\n\n");
            sb.append("Okamžité kroky:\n");
            sb.append("1. Aplikujte schválené ošetrenie DNES\n");
            sb.append("2. Odstráňte všetok trúdí plod\n");
            sb.append("3. Kontrolujte po 7 dňoch\n");
            sb.append("4. Opakujte ošetrenie ak je potrebné");
        }

        return sb.toString();
    }
}
