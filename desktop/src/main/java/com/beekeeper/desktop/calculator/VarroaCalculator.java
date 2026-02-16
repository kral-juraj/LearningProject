package com.beekeeper.desktop.calculator;

import com.beekeeper.shared.i18n.TranslationManager;

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
        TranslationManager tm = TranslationManager.getInstance();
        if (count < THRESHOLD_OK) {
            return tm.get("varroa.status.ok");
        } else if (count < THRESHOLD_WARNING) {
            return tm.get("varroa.status.warning");
        } else if (count < THRESHOLD_CRITICAL) {
            return tm.get("varroa.status.warning");
        } else {
            return tm.get("varroa.status.critical");
        }
    }

    /**
     * Generates treatment recommendation based on projected count.
     */
    private static String getRecommendation(int projectedCount, int days, boolean broodPresent,
                                            VarroaParameters params) {
        TranslationManager tm = TranslationManager.getInstance();
        StringBuilder sb = new StringBuilder();

        sb.append(tm.get("varroa.projection_for_days", days, projectedCount)).append("\n");
        sb.append(tm.get(broodPresent ? "varroa.model_with_brood" : "varroa.model_without_brood")).append("\n");
        sb.append(tm.get("varroa.drone_cells", params.getDroneBroodPercentage() * 100)).append("\n\n");

        if (projectedCount < THRESHOLD_OK) {
            sb.append(tm.get("varroa.rec.ok.title")).append("\n");
            sb.append(tm.get("varroa.rec.ok.detail1")).append("\n");
            sb.append(tm.get("varroa.rec.ok.detail2"));
        } else if (projectedCount < THRESHOLD_WARNING) {
            sb.append(tm.get("varroa.rec.low_warning.title")).append("\n");
            sb.append(tm.get("varroa.rec.low_warning.detail1")).append("\n");
            sb.append(tm.get("varroa.rec.low_warning.detail2")).append("\n");
            sb.append(tm.get("varroa.rec.low_warning.detail3")).append("\n\n");
            sb.append(tm.get("varroa.rec.low_warning.action"));
        } else if (projectedCount < THRESHOLD_CRITICAL) {
            sb.append(tm.get("varroa.rec.high_warning.title")).append("\n");
            sb.append(tm.get("varroa.rec.high_warning.detail1")).append("\n");
            sb.append(tm.get("varroa.rec.high_warning.detail2")).append("\n\n");
            sb.append(tm.get("varroa.rec.high_warning.options")).append("\n");
            sb.append(tm.get("varroa.rec.high_warning.option1")).append("\n");
            sb.append(tm.get("varroa.rec.high_warning.option2")).append("\n");
            sb.append(tm.get("varroa.rec.high_warning.option3"));
        } else {
            sb.append(tm.get("varroa.rec.critical.title")).append("\n");
            sb.append(tm.get("varroa.rec.critical.detail1")).append("\n");
            sb.append(tm.get("varroa.rec.critical.detail2")).append("\n\n");
            sb.append(tm.get("varroa.rec.critical.steps")).append("\n");
            sb.append(tm.get("varroa.rec.critical.step1")).append("\n");
            sb.append(tm.get("varroa.rec.critical.step2")).append("\n");
            sb.append(tm.get("varroa.rec.critical.step3")).append("\n");
            sb.append(tm.get("varroa.rec.critical.step4"));
        }

        return sb.toString();
    }
}
