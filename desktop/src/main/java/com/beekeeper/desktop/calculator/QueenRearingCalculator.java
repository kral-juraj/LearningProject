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
     * @param method    Rearing method (currently not used, reserved for future)
     * @return List of QueenMilestone objects with dates and descriptions
     */
    public static List<QueenMilestone> calculateMilestones(long startDate, String method) {
        List<QueenMilestone> milestones = new ArrayList<>();

        // D+0: Grafting
        milestones.add(createMilestone(
                "Založenie matôčnikov",
                0,
                startDate,
                "Prekladanie larvičiek do matôčnikov. Ideálny vek larvičiek: 12-24 hodín.",
                "#90EE90" // light green
        ));

        // D+5: Add excluder
        milestones.add(createMilestone(
                "Priloženie mriežky",
                5,
                startDate,
                "Kontrola prijatia matôčnikov. Priloženie materskej mriežky.",
                "#98FB98" // pale green
        ));

        // D+7: Check brood
        milestones.add(createMilestone(
                "Prehliadka plodu",
                7,
                startDate,
                "Kontrola matôčnikov, odstránenie nekvaliných. Larvičky sa začínajú zapečatovať.",
                "#FFD700" // gold
        ));

        // D+10: Check queen cells
        milestones.add(createMilestone(
                "Prehliadka matôčnikov",
                10,
                startDate,
                "Všetky matôčníky zapečatené. Kontrola správnosti zapečatenia.",
                "#FFA500" // orange
        ));

        // D+12: Sealed cells
        milestones.add(createMilestone(
                "Zapečatenie matôčnikov",
                12,
                startDate,
                "Matky sa vyvíjajú v zapečatených matôčníkoch. Príprava odkladákov.",
                "#FF8C00" // dark orange
        ));

        // D+16: Move to nucs
        milestones.add(createMilestone(
                "Presadenie do odkladákov",
                16,
                startDate,
                "Matôčníky tesne pred vyhryznutím. Presadenie do odkladákov.",
                "#FF6347" // tomato
        ));

        // D+21: Check for laying
        milestones.add(createMilestone(
                "Kontrola kládky",
                21,
                startDate,
                "Kontrola kládky mladých matiek. Matky začínajú klásť vajíčka.",
                "#FF4500" // orange-red
        ));

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
