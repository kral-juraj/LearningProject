package com.beekeeper.desktop.calculator;

import com.beekeeper.shared.i18n.TranslationManager;

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
        TranslationManager tm = TranslationManager.getInstance();
        List<QueenMilestone> milestones = new ArrayList<>();

        // Farby: #FFFFCC = AKCIA (včelár musí zasiahnuť), #FFFFFF = INFO (len sledovanie)

        // D+0 - AKCIA
        milestones.add(createMilestone(
                tm.get("queen.std.start_cells"),
                0,
                startDate,
                tm.get("queen.std.start_cells.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+5 - AKCIA
        milestones.add(createMilestone(
                tm.get("queen.std.add_excluder"),
                5,
                startDate,
                tm.get("queen.std.add_excluder.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+7 - AKCIA
        milestones.add(createMilestone(
                tm.get("queen.std.inspect_brood"),
                7,
                startDate,
                tm.get("queen.std.inspect_brood.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+10 - AKCIA
        milestones.add(createMilestone(
                tm.get("queen.std.inspect_cells"),
                10,
                startDate,
                tm.get("queen.std.inspect_cells.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+12 - INFO
        milestones.add(createMilestone(
                tm.get("queen.std.capping_complete"),
                12,
                startDate,
                tm.get("queen.std.capping_complete.desc"),
                "#FFFFFF" // INFO
        ));

        // D+16 - AKCIA
        milestones.add(createMilestone(
                tm.get("queen.std.place_cells"),
                16,
                startDate,
                tm.get("queen.std.place_cells.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+21 - AKCIA
        milestones.add(createMilestone(
                tm.get("queen.std.check_laying"),
                21,
                startDate,
                tm.get("queen.std.check_laying.desc"),
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
        TranslationManager tm = TranslationManager.getInstance();
        List<QueenMilestone> milestones = new ArrayList<>();

        // Farby: #FFFFCC = AKCIA (včelár musí zasiahnuť), #FFFFFF = INFO (len sledovanie)

        // D+0 - AKCIA
        milestones.add(createMilestone(
                tm.get("queen.split.make_split"),
                0,
                startDate,
                tm.get("queen.split.make_split.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+5 - AKCIA
        milestones.add(createMilestone(
                tm.get("queen.split.break_cells"),
                5,
                startDate,
                tm.get("queen.split.break_cells.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+7 - AKCIA
        milestones.add(createMilestone(
                tm.get("queen.split.choose_one"),
                7,
                startDate,
                tm.get("queen.split.choose_one.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+9 - AKCIA
        milestones.add(createMilestone(
                tm.get("queen.split.nurse_cells"),
                9,
                startDate,
                tm.get("queen.split.nurse_cells.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+13 - INFO
        milestones.add(createMilestone(
                tm.get("queen.split.queen_emerges"),
                13,
                startDate,
                tm.get("queen.split.queen_emerges.desc"),
                "#FFFFFF" // INFO
        ));

        // D+13 - AKCIA (Varroa pasca - príprava)
        milestones.add(createMilestone(
                tm.get("queen.split.prepare_trap"),
                13,
                startDate,
                tm.get("queen.split.prepare_trap.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+16 - AKCIA (Varroa pasca - vloženie vajíčka)
        milestones.add(createMilestone(
                tm.get("queen.split.insert_trap_eggs"),
                16,
                startDate,
                tm.get("queen.split.insert_trap_eggs.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+19 - INFO
        milestones.add(createMilestone(
                tm.get("queen.split.sexual_maturity"),
                19,
                startDate,
                tm.get("queen.split.sexual_maturity.desc"),
                "#FFFFFF" // INFO
        ));

        // D+19 - AKCIA (Varroa pasca - vloženie larvy)
        milestones.add(createMilestone(
                tm.get("queen.split.insert_trap_larvae"),
                19,
                startDate,
                tm.get("queen.split.insert_trap_larvae.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+20 - INFO
        milestones.add(createMilestone(
                tm.get("queen.split.last_worker_brood"),
                20,
                startDate,
                tm.get("queen.split.last_worker_brood.desc"),
                "#FFFFFF" // INFO
        ));

        // D+22 - INFO
        milestones.add(createMilestone(
                tm.get("queen.split.mating_flights"),
                22,
                startDate,
                tm.get("queen.split.mating_flights.desc"),
                "#FFFFFF" // INFO
        ));

        // D+23 - INFO
        milestones.add(createMilestone(
                tm.get("queen.split.last_drone_brood"),
                23,
                startDate,
                tm.get("queen.split.last_drone_brood.desc"),
                "#FFFFFF" // INFO
        ));

        // D+25 - AKCIA (Bezplodové obdobie - preliečenie)
        milestones.add(createMilestone(
                tm.get("queen.split.broodless_treatment"),
                25,
                startDate,
                tm.get("queen.split.broodless_treatment.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+25 - AKCIA (Varroa pasca - odstránenie vajíčka)
        milestones.add(createMilestone(
                tm.get("queen.split.remove_trap_eggs"),
                25,
                startDate,
                tm.get("queen.split.remove_trap_eggs.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+27 - INFO
        milestones.add(createMilestone(
                tm.get("queen.split.new_queen_laying"),
                27,
                startDate,
                tm.get("queen.split.new_queen_laying.desc"),
                "#FFFFFF" // INFO
        ));

        // D+30 - AKCIA (Varroa pasca - odstránenie larvy)
        milestones.add(createMilestone(
                tm.get("queen.split.remove_trap_larvae"),
                30,
                startDate,
                tm.get("queen.split.remove_trap_larvae.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+32 - AKCIA
        milestones.add(createMilestone(
                tm.get("queen.split.check_laying"),
                32,
                startDate,
                tm.get("queen.split.check_laying.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+41 - INFO
        milestones.add(createMilestone(
                tm.get("queen.split.old_foragers_birth"),
                41,
                startDate,
                tm.get("queen.split.old_foragers_birth.desc"),
                "#FFFFFF" // INFO
        ));

        // D+68 - INFO
        milestones.add(createMilestone(
                tm.get("queen.split.new_foragers_start"),
                68,
                startDate,
                tm.get("queen.split.new_foragers_start.desc"),
                "#FFFFFF" // INFO
        ));

        // D+76 - INFO
        milestones.add(createMilestone(
                tm.get("queen.split.old_foragers_death"),
                76,
                startDate,
                tm.get("queen.split.old_foragers_death.desc"),
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
        TranslationManager tm = TranslationManager.getInstance();
        List<QueenMilestone> milestones = new ArrayList<>();

        // Farby: #FFFFCC = AKCIA (včelár musí zasiahnuť), #FFFFFF = INFO (len sledovanie)

        // D+0 - AKCIA
        milestones.add(createMilestone(
                tm.get("queen.caging.cage_queen"),
                0,
                startDate,
                tm.get("queen.caging.cage_queen.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+21 - INFO
        milestones.add(createMilestone(
                tm.get("queen.caging.last_worker_brood"),
                21,
                startDate,
                tm.get("queen.caging.last_worker_brood.desc"),
                "#FFFFFF" // INFO
        ));

        // D+24 - INFO
        milestones.add(createMilestone(
                tm.get("queen.caging.last_drone_brood"),
                24,
                startDate,
                tm.get("queen.caging.last_drone_brood.desc"),
                "#FFFFFF" // INFO
        ));

        // D+25 - INFO
        milestones.add(createMilestone(
                tm.get("queen.caging.broodless_start"),
                25,
                startDate,
                tm.get("queen.caging.broodless_start.desc"),
                "#FFFFFF" // INFO
        ));

        // D+25 - AKCIA (Presypanie)
        milestones.add(createMilestone(
                tm.get("queen.caging.shook_swarm"),
                25,
                startDate,
                tm.get("queen.caging.shook_swarm.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+26 - AKCIA (Varroa pasca - vloženie)
        milestones.add(createMilestone(
                tm.get("queen.caging.insert_trap"),
                26,
                startDate,
                tm.get("queen.caging.insert_trap.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+30 - AKCIA (Preliečenie)
        milestones.add(createMilestone(
                tm.get("queen.caging.oxalic_treatment"),
                30,
                startDate,
                tm.get("queen.caging.oxalic_treatment.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+31 - AKCIA (Vypustenie matky)
        milestones.add(createMilestone(
                tm.get("queen.caging.release_queen"),
                31,
                startDate,
                tm.get("queen.caging.release_queen.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+31 - INFO
        milestones.add(createMilestone(
                tm.get("queen.caging.laying_resumes"),
                31,
                startDate,
                tm.get("queen.caging.laying_resumes.desc"),
                "#FFFFFF" // INFO
        ));

        // D+35 - AKCIA (Varroa pasca - odstránenie)
        milestones.add(createMilestone(
                tm.get("queen.caging.remove_trap"),
                35,
                startDate,
                tm.get("queen.caging.remove_trap.desc"),
                "#FFFFCC" // AKCIA
        ));

        // D+42 - INFO
        milestones.add(createMilestone(
                tm.get("queen.caging.old_foragers_birth"),
                42,
                startDate,
                tm.get("queen.caging.old_foragers_birth.desc"),
                "#FFFFFF" // INFO
        ));

        // D+51 - INFO
        milestones.add(createMilestone(
                tm.get("queen.caging.first_new_brood"),
                51,
                startDate,
                tm.get("queen.caging.first_new_brood.desc"),
                "#FFFFFF" // INFO
        ));

        // D+72 - INFO
        milestones.add(createMilestone(
                tm.get("queen.caging.new_foragers_start"),
                72,
                startDate,
                tm.get("queen.caging.new_foragers_start.desc"),
                "#FFFFFF" // INFO
        ));

        // D+77 - INFO
        milestones.add(createMilestone(
                tm.get("queen.caging.old_foragers_death"),
                77,
                startDate,
                tm.get("queen.caging.old_foragers_death.desc"),
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
