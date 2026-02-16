package com.beekeeper.desktop.util;

import com.beekeeper.desktop.db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

/**
 * One-time migration to populate translations table with all UI strings.
 * Run once during first deployment, or manually to reset translations.
 *
 * Use case: Populates database with 570+ Slovak and English translations
 * organized by category (menu, button, label, error, etc.).
 *
 * To run: Execute main() method or call migrate() from application startup.
 */
public class TranslationMigration {

    /**
     * Execute migration to populate translations table.
     * Clears existing translations and inserts all Slovak/English pairs.
     */
    public static void migrate() throws SQLException {
        System.out.println("Starting translation migration...");

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);

            // Clear existing translations
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("DELETE FROM translations");
                System.out.println("Cleared existing translations");
            }

            // Insert all translations by category
            insertAppTranslations(conn);
            insertMenuTranslations(conn);
            insertTabTranslations(conn);
            insertButtonTranslations(conn);
            insertLabelTranslations(conn);
            insertTableTranslations(conn);
            insertDialogTranslations(conn);
            insertStatusTranslations(conn);
            insertErrorTranslations(conn);
            insertSuccessTranslations(conn);
            insertValidationTranslations(conn);
            insertCalculatorTranslations(conn);
            insertTreatmentTranslations(conn);
            insertMilestoneTranslations(conn);
            insertFeedTypeTranslations(conn);
            insertEventTypeTranslations(conn);
            insertQueenColorTranslations(conn);

            conn.commit();
            System.out.println("Translation migration completed successfully!");
        }
    }

    /**
     * Insert a translation pair (Slovak and English) into database.
     *
     * @param conn Connection with active transaction
     * @param key Translation key
     * @param sk Slovak value
     * @param en English value
     * @param category Category for organization
     */
    private static void insert(Connection conn, String key, String sk, String en,
                               String category) throws SQLException {
        String sql = "INSERT INTO translations (id, key, language, value, category, createdAt) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        long now = System.currentTimeMillis();

        // Slovak
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, key);
            stmt.setString(3, "sk");
            stmt.setString(4, sk);
            stmt.setString(5, category);
            stmt.setLong(6, now);
            stmt.executeUpdate();
        }

        // English
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, key);
            stmt.setString(3, "en");
            stmt.setString(4, en);
            stmt.setString(5, category);
            stmt.setLong(6, now);
            stmt.executeUpdate();
        }
    }

    /**
     * Application-level strings (title, version, etc.)
     */
    private static void insertAppTranslations(Connection conn) throws SQLException {
        insert(conn, "app.title", "Beekeeper Desktop - Včelársky denník", "Beekeeper Desktop - Beekeeping Journal", "app");
        insert(conn, "app.version", "Beekeeper Desktop v1.0", "Beekeeper Desktop v1.0", "app");
    }

    /**
     * Menu items
     */
    private static void insertMenuTranslations(Connection conn) throws SQLException {
        insert(conn, "menu.file", "Súbor", "File", "menu");
        insert(conn, "menu.settings", "Nastavenia", "Settings", "menu");
        insert(conn, "menu.exit", "Ukončiť", "Exit", "menu");
        insert(conn, "menu.help", "Nápoveda", "Help", "menu");
        insert(conn, "menu.about", "O aplikácii", "About", "menu");
        insert(conn, "menu.language", "Jazyk", "Language", "menu");
    }

    /**
     * Tab labels
     */
    private static void insertTabTranslations(Connection conn) throws SQLException {
        insert(conn, "tab.apiaries", "Včelnice", "Apiaries", "tab");
        insert(conn, "tab.hives", "Úle", "Hives", "tab");
        insert(conn, "tab.inspections", "Prehliadky", "Inspections", "tab");
        insert(conn, "tab.feeding", "Krmenie", "Feeding", "tab");
        insert(conn, "tab.taxation", "Taxácie", "Frame Surveys", "tab");
        insert(conn, "tab.calendar", "Kalendár", "Calendar", "tab");
        insert(conn, "tab.calculators", "Kalkulačky", "Calculators", "tab");
    }

    /**
     * Button labels
     */
    private static void insertButtonTranslations(Connection conn) throws SQLException {
        // Common buttons
        insert(conn, "button.add", "Pridať", "Add", "button");
        insert(conn, "button.edit", "Upraviť", "Edit", "button");
        insert(conn, "button.delete", "Zmazať", "Delete", "button");
        insert(conn, "button.save", "Uložiť", "Save", "button");
        insert(conn, "button.cancel", "Zrušiť", "Cancel", "button");
        insert(conn, "button.refresh", "Obnoviť", "Refresh", "button");
        insert(conn, "button.calculate", "Vypočítať", "Calculate", "button");
        insert(conn, "button.clear", "Vymazať", "Clear", "button");
        insert(conn, "button.close", "Zavrieť", "Close", "button");
        insert(conn, "button.ok", "OK", "OK", "button");

        // Specific buttons
        insert(conn, "button.add_apiary", "Pridať včelnicu", "Add Apiary", "button");
        insert(conn, "button.add_hive", "Pridať úľ", "Add Hive", "button");
        insert(conn, "button.add_inspection", "Pridať prehliadku", "Add Inspection", "button");
        insert(conn, "button.add_feeding", "Pridať krmenie", "Add Feeding", "button");
        insert(conn, "button.add_taxation", "Pridať taxáciu", "Add Survey", "button");
        insert(conn, "button.add_event", "Pridať udalosť", "Add Event", "button");
        insert(conn, "button.add_frame", "Pridať rámik", "Add Frame", "button");
        insert(conn, "button.add_treatment", "Pridať", "Add", "button");

        insert(conn, "button.show_hives", "Zobraziť úle", "Show Hives", "button");
        insert(conn, "button.show_inspections", "Zobraziť prehliadky", "Show Inspections", "button");
        insert(conn, "button.export_calendar", "Export do kalendára", "Export to Calendar", "button");
        insert(conn, "button.settings", "Nastavenia...", "Settings...", "button");
        insert(conn, "button.toggle_active", "Prepnúť aktívnosť", "Toggle Active", "button");
    }

    /**
     * Form field labels
     */
    private static void insertLabelTranslations(Connection conn) throws SQLException {
        // Common labels
        insert(conn, "label.name", "Názov", "Name", "label");
        insert(conn, "label.location", "Lokalita", "Location", "label");
        insert(conn, "label.date", "Dátum", "Date", "label");
        insert(conn, "label.notes", "Poznámky", "Notes", "label");
        insert(conn, "label.type", "Typ", "Type", "label");

        // Apiary fields
        insert(conn, "label.latitude", "Zemepisná šírka", "Latitude", "label");
        insert(conn, "label.longitude", "Zemepisná dĺžka", "Longitude", "label");

        // Hive fields
        insert(conn, "label.apiary", "Včelnica", "Apiary", "label");
        insert(conn, "label.hive_type", "Typ úľa", "Hive Type", "label");
        insert(conn, "label.queen_id", "ID matky", "Queen ID", "label");
        insert(conn, "label.queen_year", "Rok matky", "Queen Year", "label");
        insert(conn, "label.queen_color", "Farba matky", "Queen Color", "label");
        insert(conn, "label.active", "Aktívny", "Active", "label");

        // Inspection fields
        insert(conn, "label.hive", "Úľ", "Hive", "label");
        insert(conn, "label.inspection_date", "Dátum prehliadky", "Inspection Date", "label");
        insert(conn, "label.time", "Čas", "Time", "label");
        insert(conn, "label.hour", "Hodina", "Hour", "label");
        insert(conn, "label.minute", "Minúta", "Minute", "label");
        insert(conn, "label.temperature", "Teplota (°C)", "Temperature (°C)", "label");
        insert(conn, "label.strength_estimate", "Sila", "Strength", "label");
        insert(conn, "label.food_stores_kg", "Zásoby (kg)", "Food Stores (kg)", "label");
        insert(conn, "label.brood_frames", "Rámiky s plodom", "Brood Frames", "label");
        insert(conn, "label.capped_brood_dm", "Zapečatený plod (dm²)", "Capped Brood (dm²)", "label");
        insert(conn, "label.uncapped_brood_dm", "Nezapečatený plod (dm²)", "Uncapped Brood (dm²)", "label");
        insert(conn, "label.pollen_frames", "Rámiky s peľom", "Pollen Frames", "label");
        insert(conn, "label.total_frames", "Celkom rámikov", "Total Frames", "label");
        insert(conn, "label.queen_seen", "Matka videná", "Queen Seen", "label");
        insert(conn, "label.queen_note", "Poznámka k matke", "Queen Note", "label");
        insert(conn, "label.varroa", "Varroa prítomná", "Varroa Present", "label");
        insert(conn, "label.varroa_count", "Počet varroa", "Varroa Count", "label");
        insert(conn, "label.aggression", "Agresivita", "Aggression", "label");
        insert(conn, "label.behavior", "Správanie", "Behavior", "label");

        // Feeding fields
        insert(conn, "label.feeding_date", "Dátum krmenia", "Feeding Date", "label");
        insert(conn, "label.weight_before", "Váha pred (kg)", "Weight Before (kg)", "label");
        insert(conn, "label.weight_after", "Váha po (kg)", "Weight After (kg)", "label");
        insert(conn, "label.feed_type", "Typ krmiva", "Feed Type", "label");
        insert(conn, "label.amount_kg", "Množstvo (kg)", "Amount (kg)", "label");
        insert(conn, "label.difference", "Rozdiel", "Difference", "label");

        // Taxation fields
        insert(conn, "label.taxation_date", "Dátum taxácie", "Survey Date", "label");
        insert(conn, "label.position", "Pozícia", "Position", "label");
        insert(conn, "label.frame_type", "Typ rámika", "Frame Type", "label");
        insert(conn, "label.frame_year", "Rok rámika", "Frame Year", "label");
        insert(conn, "label.is_starter", "Opačnenec", "Starter", "label");
        insert(conn, "label.has_queen", "Matka", "Queen", "label");
        insert(conn, "label.has_cage", "Klietka", "Cage", "label");
        insert(conn, "label.has_nuc_box", "Núkleo box", "Nuc Box", "label");
        insert(conn, "label.pollen_dm", "Peľ (dm²)", "Pollen (dm²)", "label");
        insert(conn, "label.capped_stores_dm", "Zapečatené zásoby (dm²)", "Capped Stores (dm²)", "label");
        insert(conn, "label.uncapped_stores_dm", "Nezapečatené zásoby (dm²)", "Uncapped Stores (dm²)", "label");

        // Calendar fields
        insert(conn, "label.title", "Názov", "Title", "label");
        insert(conn, "label.description", "Popis", "Description", "label");
        insert(conn, "label.event_date", "Dátum udalosti", "Event Date", "label");
        insert(conn, "label.event_type", "Typ udalosti", "Event Type", "label");
        insert(conn, "label.completed", "Dokončené", "Completed", "label");

        // Calculator fields
        insert(conn, "label.varroa_count", "Počet kliešťov", "Mite Count", "label");
        insert(conn, "label.measurement_date", "Dátum merania", "Measurement Date", "label");
        insert(conn, "label.brood_present", "Plod prítomný", "Brood Present", "label");
        insert(conn, "label.projection", "Projekcia", "Projection", "label");
        insert(conn, "label.days", "dní", "days", "label");
        insert(conn, "label.drone_brood_percent", "Trúdí plod (%)", "Drone Brood (%)", "label");
        insert(conn, "label.treatment_plan", "Plán liečenia:", "Treatment Plan:", "label");
        insert(conn, "label.effectiveness", "Efekt. (%)", "Effect. (%)", "label");
        insert(conn, "label.queen_breeding", "Chov matiek", "Queen Breeding", "label");
        insert(conn, "label.grafting_date", "Dátum očkovania", "Grafting Date", "label");
        insert(conn, "label.num_cups", "Počet mušelí", "Number of Cups", "label");
    }

    /**
     * Table column headers
     */
    private static void insertTableTranslations(Connection conn) throws SQLException {
        // Apiary table
        insert(conn, "table.name", "Názov", "Name", "table");
        insert(conn, "table.location", "Lokalita", "Location", "table");

        // Hive table
        insert(conn, "table.hive_name", "Názov úľa", "Hive Name", "table");
        insert(conn, "table.type", "Typ", "Type", "table");
        insert(conn, "table.queen_year", "Rok matky", "Queen Year", "table");
        insert(conn, "table.active", "Aktívny", "Active", "table");

        // Inspection table
        insert(conn, "table.date", "Dátum", "Date", "table");
        insert(conn, "table.temperature", "Teplota", "Temperature", "table");
        insert(conn, "table.strength", "Sila", "Strength", "table");
        insert(conn, "table.brood_frames", "Plod (rámiky)", "Brood Frames", "table");

        // Feeding table
        insert(conn, "table.feed_type", "Typ krmiva", "Feed Type", "table");
        insert(conn, "table.amount", "Množstvo", "Amount", "table");

        // Taxation table
        insert(conn, "table.hive", "Úľ", "Hive", "table");
        insert(conn, "table.total_frames", "Celkom rámikov", "Total Frames", "table");
        insert(conn, "table.food_stores", "Zásoby", "Food Stores", "table");
        insert(conn, "table.starter_frames", "Opačnence", "Starters", "table");

        // Calendar table
        insert(conn, "table.title", "Názov", "Title", "table");
        insert(conn, "table.event_type", "Typ", "Type", "table");
        insert(conn, "table.completed", "Dokončené", "Completed", "table");

        // Treatment table
        insert(conn, "table.treatment_date", "Dátum", "Date", "table");
        insert(conn, "table.treatment_type", "Typ liečenia", "Treatment Type", "table");
        insert(conn, "table.effectiveness", "Efekt. (%)", "Effect. (%)", "table");
        insert(conn, "table.note", "Poznámka", "Note", "table");

        // Frame table
        insert(conn, "table.position", "Poz.", "Pos.", "table");
        insert(conn, "table.frame_type", "Typ", "Type", "table");
        insert(conn, "table.capped_brood", "Zapečat. plod", "Capped Brood", "table");
        insert(conn, "table.uncapped_brood", "Nezapečat. plod", "Uncapped Brood", "table");
        insert(conn, "table.pollen", "Peľ", "Pollen", "table");
    }

    /**
     * Dialog titles and headers
     */
    private static void insertDialogTranslations(Connection conn) throws SQLException {
        // Apiary dialogs
        insert(conn, "dialog.add_apiary.title", "Pridať včelnicu", "Add Apiary", "dialog");
        insert(conn, "dialog.edit_apiary.title", "Upraviť včelnicu", "Edit Apiary", "dialog");
        insert(conn, "dialog.delete_apiary.title", "Zmazať včelnicu", "Delete Apiary", "dialog");
        insert(conn, "dialog.delete_apiary.header", "Zmazať včelnicu: %s", "Delete Apiary: %s", "dialog");
        insert(conn, "dialog.delete_apiary.content", "Naozaj chcete zmazať túto včelnicu? Toto zmaže aj všetky úle a záznamy.", "Are you sure you want to delete this apiary? This will also delete all hives and records.", "dialog");

        // Hive dialogs
        insert(conn, "dialog.add_hive.title", "Pridať úľ", "Add Hive", "dialog");
        insert(conn, "dialog.edit_hive.title", "Upraviť úľ", "Edit Hive", "dialog");
        insert(conn, "dialog.delete_hive.title", "Zmazať úľ", "Delete Hive", "dialog");
        insert(conn, "dialog.delete_hive.header", "Zmazať úľ: %s", "Delete Hive: %s", "dialog");
        insert(conn, "dialog.delete_hive.content", "Naozaj chcete zmazať tento úľ? Toto zmaže aj všetky záznamy.", "Are you sure you want to delete this hive? This will also delete all records.", "dialog");

        // Inspection dialogs
        insert(conn, "dialog.add_inspection.title", "Pridať prehliadku", "Add Inspection", "dialog");
        insert(conn, "dialog.edit_inspection.title", "Upraviť prehliadku", "Edit Inspection", "dialog");
        insert(conn, "dialog.delete_inspection.title", "Zmazať prehliadku", "Delete Inspection", "dialog");
        insert(conn, "dialog.delete_inspection.header", "Zmazať prehliadku", "Delete Inspection", "dialog");
        insert(conn, "dialog.delete_inspection.content", "Naozaj chcete zmazať túto prehliadku?", "Are you sure you want to delete this inspection?", "dialog");

        // Feeding dialogs
        insert(conn, "dialog.add_feeding.title", "Pridať krmenie", "Add Feeding", "dialog");
        insert(conn, "dialog.edit_feeding.title", "Upraviť krmenie", "Edit Feeding", "dialog");
        insert(conn, "dialog.delete_feeding.title", "Zmazať krmenie", "Delete Feeding", "dialog");
        insert(conn, "dialog.delete_feeding.header", "Zmazať krmenie", "Delete Feeding", "dialog");
        insert(conn, "dialog.delete_feeding.content", "Naozaj chcete zmazať toto krmenie?", "Are you sure you want to delete this feeding?", "dialog");

        // Taxation dialogs
        insert(conn, "dialog.add_taxation.title", "Pridať taxáciu", "Add Survey", "dialog");
        insert(conn, "dialog.edit_taxation.title", "Upraviť taxáciu", "Edit Survey", "dialog");
        insert(conn, "dialog.delete_taxation.title", "Zmazať taxáciu", "Delete Survey", "dialog");
        insert(conn, "dialog.delete_taxation.header", "Zmazať taxáciu", "Delete Survey", "dialog");
        insert(conn, "dialog.delete_taxation.content", "Naozaj chcete zmazať túto taxáciu?", "Are you sure you want to delete this survey?", "dialog");

        // Frame dialogs
        insert(conn, "dialog.add_frame.title", "Pridať rámik", "Add Frame", "dialog");
        insert(conn, "dialog.edit_frame.title", "Upraviť rámik", "Edit Frame", "dialog");
        insert(conn, "dialog.delete_frame.title", "Zmazať rámik", "Delete Frame", "dialog");
        insert(conn, "dialog.delete_frame.header", "Zmazať rámik", "Delete Frame", "dialog");
        insert(conn, "dialog.delete_frame.content", "Naozaj chcete zmazať tento rámik?", "Are you sure you want to delete this frame?", "dialog");

        // Calendar dialogs
        insert(conn, "dialog.add_event.title", "Pridať udalosť", "Add Event", "dialog");
        insert(conn, "dialog.edit_event.title", "Upraviť udalosť", "Edit Event", "dialog");
        insert(conn, "dialog.delete_event.title", "Zmazať udalosť", "Delete Event", "dialog");
        insert(conn, "dialog.delete_event.header", "Zmazať udalosť", "Delete Event", "dialog");
        insert(conn, "dialog.delete_event.content", "Naozaj chcete zmazať túto udalosť?", "Are you sure you want to delete this event?", "dialog");

        // Language change dialog
        insert(conn, "dialog.language_changed.title", "Jazyk zmenený", "Language Changed", "dialog");
        insert(conn, "dialog.language_changed.header", "Jazyk bol zmenený", "Language has been changed", "dialog");
        insert(conn, "dialog.language_changed.content", "Reštartujte aplikáciu pre zobrazenie zmien.", "Please restart the application to see the changes.", "dialog");

        // Error dialog
        insert(conn, "dialog.error.title", "Chyba", "Error", "dialog");
        insert(conn, "dialog.warning.title", "Upozornenie", "Warning", "dialog");
        insert(conn, "dialog.info.title", "Informácia", "Information", "dialog");
        insert(conn, "dialog.confirm.title", "Potvrdenie", "Confirmation", "dialog");
    }

    /**
     * Status messages
     */
    private static void insertStatusTranslations(Connection conn) throws SQLException {
        insert(conn, "status.ready", "Pripravený", "Ready", "status");
        insert(conn, "status.loading", "Načítavam...", "Loading...", "status");
        insert(conn, "status.saving", "Ukladám...", "Saving...", "status");
        insert(conn, "status.deleting", "Mažem...", "Deleting...", "status");
        insert(conn, "status.calculating", "Počítam...", "Calculating...", "status");
        insert(conn, "status.select_apiary", "Vyberte včelnicu", "Select apiary", "status");
        insert(conn, "status.select_hive", "Vyberte úľ", "Select hive", "status");
        insert(conn, "status.no_data", "Žiadne dáta", "No data", "status");
    }

    /**
     * Error messages
     */
    private static void insertErrorTranslations(Connection conn) throws SQLException {
        insert(conn, "error.loading_apiaries", "Chyba pri načítaní včelníc: %s", "Error loading apiaries: %s", "error");
        insert(conn, "error.loading_hives", "Chyba pri načítaní úľov: %s", "Error loading hives: %s", "error");
        insert(conn, "error.loading_inspections", "Chyba pri načítaní prehliadok: %s", "Error loading inspections: %s", "error");
        insert(conn, "error.loading_feedings", "Chyba pri načítaní krmení: %s", "Error loading feedings: %s", "error");
        insert(conn, "error.loading_taxations", "Chyba pri načítaní taxácií: %s", "Error loading surveys: %s", "error");
        insert(conn, "error.loading_events", "Chyba pri načítaní udalostí: %s", "Error loading events: %s", "error");

        insert(conn, "error.saving_apiary", "Chyba pri ukladaní včelnice: %s", "Error saving apiary: %s", "error");
        insert(conn, "error.saving_hive", "Chyba pri ukladaní úľa: %s", "Error saving hive: %s", "error");
        insert(conn, "error.saving_inspection", "Chyba pri ukladaní prehliadky: %s", "Error saving inspection: %s", "error");
        insert(conn, "error.saving_feeding", "Chyba pri ukladaní krmenia: %s", "Error saving feeding: %s", "error");
        insert(conn, "error.saving_taxation", "Chyba pri ukladaní taxácie: %s", "Error saving survey: %s", "error");
        insert(conn, "error.saving_event", "Chyba pri ukladaní udalosti: %s", "Error saving event: %s", "error");

        insert(conn, "error.deleting_apiary", "Chyba pri mazaní včelnice: %s", "Error deleting apiary: %s", "error");
        insert(conn, "error.deleting_hive", "Chyba pri mazaní úľa: %s", "Error deleting hive: %s", "error");
        insert(conn, "error.deleting_inspection", "Chyba pri mazaní prehliadky: %s", "Error deleting inspection: %s", "error");
        insert(conn, "error.deleting_feeding", "Chyba pri mazaní krmenia: %s", "Error deleting feeding: %s", "error");
        insert(conn, "error.deleting_taxation", "Chyba pri mazaní taxácie: %s", "Error deleting survey: %s", "error");
        insert(conn, "error.deleting_event", "Chyba pri mazaní udalosti: %s", "Error deleting event: %s", "error");

        insert(conn, "error.no_selection", "Vyberte položku", "Please select an item", "error");
        insert(conn, "error.invalid_number", "Neplatné číslo", "Invalid number", "error");
        insert(conn, "error.invalid_date", "Neplatný dátum", "Invalid date", "error");
        insert(conn, "error.calculation_failed", "Výpočet zlyhal: %s", "Calculation failed: %s", "error");
    }

    /**
     * Success messages
     */
    private static void insertSuccessTranslations(Connection conn) throws SQLException {
        insert(conn, "success.apiary_created", "Včelnica úspešne vytvorená", "Apiary created successfully", "success");
        insert(conn, "success.apiary_updated", "Včelnica úspešne aktualizovaná", "Apiary updated successfully", "success");
        insert(conn, "success.apiary_deleted", "Včelnica úspešne zmazaná", "Apiary deleted successfully", "success");

        insert(conn, "success.hive_created", "Úľ úspešne vytvorený", "Hive created successfully", "success");
        insert(conn, "success.hive_updated", "Úľ úspešne aktualizovaný", "Hive updated successfully", "success");
        insert(conn, "success.hive_deleted", "Úľ úspešne zmazaný", "Hive deleted successfully", "success");

        insert(conn, "success.inspection_created", "Prehliadka úspešne vytvorená", "Inspection created successfully", "success");
        insert(conn, "success.inspection_updated", "Prehliadka úspešne aktualizovaná", "Inspection updated successfully", "success");
        insert(conn, "success.inspection_deleted", "Prehliadka úspešne zmazaná", "Inspection deleted successfully", "success");

        insert(conn, "success.feeding_created", "Krmenie úspešne vytvorené", "Feeding created successfully", "success");
        insert(conn, "success.feeding_updated", "Krmenie úspešne aktualizované", "Feeding updated successfully", "success");
        insert(conn, "success.feeding_deleted", "Krmenie úspešne zmazané", "Feeding deleted successfully", "success");

        insert(conn, "success.taxation_created", "Taxácia úspešne vytvorená", "Survey created successfully", "success");
        insert(conn, "success.taxation_updated", "Taxácia úspešne aktualizovaná", "Survey updated successfully", "success");
        insert(conn, "success.taxation_deleted", "Taxácia úspešne zmazaná", "Survey deleted successfully", "success");

        insert(conn, "success.event_created", "Udalosť úspešne vytvorená", "Event created successfully", "success");
        insert(conn, "success.event_updated", "Udalosť úspešne aktualizovaná", "Event updated successfully", "success");
        insert(conn, "success.event_deleted", "Udalosť úspešne zmazaná", "Event deleted successfully", "success");

        insert(conn, "success.exported_to_calendar", "Exportované do kalendára", "Exported to calendar", "success");
    }

    /**
     * Validation messages
     */
    private static void insertValidationTranslations(Connection conn) throws SQLException {
        insert(conn, "validation.required", "Toto pole je povinné", "This field is required", "validation");
        insert(conn, "validation.name_required", "Názov je povinný", "Name is required", "validation");
        insert(conn, "validation.date_required", "Dátum je povinný", "Date is required", "validation");
        insert(conn, "validation.hive_required", "Úľ je povinný", "Hive is required", "validation");
        insert(conn, "validation.invalid_number", "Zadajte platné číslo", "Enter a valid number", "validation");
        insert(conn, "validation.number_positive", "Číslo musí byť kladné", "Number must be positive", "validation");
        insert(conn, "validation.number_non_negative", "Číslo nesmie byť záporné", "Number cannot be negative", "validation");
    }

    /**
     * Calculator-specific strings
     */
    private static void insertCalculatorTranslations(Connection conn) throws SQLException {
        // Varroa Calculator
        insert(conn, "calculator.varroa.title", "🐝 Varroa Kalkulačka", "🐝 Varroa Calculator", "calculator");
        insert(conn, "calculator.varroa.current_count", "Aktuálny počet kliešťov:", "Current mite count:", "calculator");
        insert(conn, "calculator.varroa.projected_count", "Odhadovaný počet:", "Projected count:", "calculator");
        insert(conn, "calculator.varroa.recommendation", "Odporúčanie:", "Recommendation:", "calculator");
        insert(conn, "calculator.varroa.no_treatment", "Liečba nie je potrebná", "No treatment needed", "calculator");
        insert(conn, "calculator.varroa.treatment_soon", "Naplánujte liečbu čoskoro", "Plan treatment soon", "calculator");
        insert(conn, "calculator.varroa.treatment_urgent", "Liečba naliehavá!", "Treatment urgent!", "calculator");
        insert(conn, "calculator.varroa.chart_title", "Projekcia populácie varroa", "Varroa Population Projection", "calculator");

        // Queen Breeding Calculator
        insert(conn, "calculator.queen.title", "👑 Chov matiek", "👑 Queen Breeding", "calculator");
        insert(conn, "calculator.queen.timeline", "Časová os:", "Timeline:", "calculator");
        insert(conn, "calculator.queen.generate", "Generovať časovú os", "Generate Timeline", "calculator");
    }

    /**
     * Treatment type translations
     */
    private static void insertTreatmentTranslations(Connection conn) throws SQLException {
        insert(conn, "treatment.thymol", "Tymol (85%)", "Thymol (85%)", "treatment");
        insert(conn, "treatment.formic_acid", "Kyselina mravčia (95%)", "Formic Acid (95%)", "treatment");
        insert(conn, "treatment.oxalic_acid", "Kyselina šťaveľová (90%)", "Oxalic Acid (90%)", "treatment");
        insert(conn, "treatment.amitraz", "Amitraz (95%)", "Amitraz (95%)", "treatment");
        insert(conn, "treatment.drone_brood_removal", "Odstránenie trúdieho plodu (80%)", "Drone Brood Removal (80%)", "treatment");
        insert(conn, "treatment.queen_caging", "Klietkovanie matky (30%)", "Queen Caging (30%)", "treatment");
    }

    /**
     * Queen rearing milestone translations
     */
    private static void insertMilestoneTranslations(Connection conn) throws SQLException {
        insert(conn, "milestone.grafting", "Očkovanie", "Grafting", "milestone");
        insert(conn, "milestone.checking_acceptance", "Kontrola prijatia", "Checking Acceptance", "milestone");
        insert(conn, "milestone.queen_cell_capping", "Zavíčkovanie matečníkov", "Queen Cell Capping", "milestone");
        insert(conn, "milestone.queen_emergence", "Liahnutie matky", "Queen Emergence", "milestone");
        insert(conn, "milestone.mating_flight", "Oblet matky", "Mating Flight", "milestone");
        insert(conn, "milestone.egg_laying_start", "Začiatok kladenia", "Egg Laying Start", "milestone");
    }

    /**
     * Feed type translations
     */
    private static void insertFeedTypeTranslations(Connection conn) throws SQLException {
        insert(conn, "feedtype.syrup_1_1", "Sirup 1:1", "Syrup 1:1", "feedtype");
        insert(conn, "feedtype.syrup_2_1", "Sirup 2:1", "Syrup 2:1", "feedtype");
        insert(conn, "feedtype.fondant", "Fondant", "Fondant", "feedtype");
        insert(conn, "feedtype.apiinvert", "ApiInvert", "ApiInvert", "feedtype");
        insert(conn, "feedtype.pollen_patty", "Peľová placka", "Pollen Patty", "feedtype");
        insert(conn, "feedtype.honey", "Med", "Honey", "feedtype");
    }

    /**
     * Event type translations
     */
    private static void insertEventTypeTranslations(Connection conn) throws SQLException {
        insert(conn, "eventtype.inspection", "Prehliadka", "Inspection", "eventtype");
        insert(conn, "eventtype.feeding", "Krmenie", "Feeding", "eventtype");
        insert(conn, "eventtype.treatment", "Liečenie", "Treatment", "eventtype");
        insert(conn, "eventtype.harvest", "Medovanie", "Harvest", "eventtype");
        insert(conn, "eventtype.queen_rearing", "Chov matiek", "Queen Rearing", "eventtype");
        insert(conn, "eventtype.swarm_control", "Rojové opatrenie", "Swarm Control", "eventtype");
        insert(conn, "eventtype.maintenance", "Údržba", "Maintenance", "eventtype");
        insert(conn, "eventtype.other", "Iné", "Other", "eventtype");
    }

    /**
     * Queen color translations
     */
    private static void insertQueenColorTranslations(Connection conn) throws SQLException {
        insert(conn, "queencolor.white", "Biela (1/6)", "White (1/6)", "queencolor");
        insert(conn, "queencolor.yellow", "Žltá (2/7)", "Yellow (2/7)", "queencolor");
        insert(conn, "queencolor.red", "Červená (3/8)", "Red (3/8)", "queencolor");
        insert(conn, "queencolor.green", "Zelená (4/9)", "Green (4/9)", "queencolor");
        insert(conn, "queencolor.blue", "Modrá (5/0)", "Blue (5/0)", "queencolor");
    }

    /**
     * Run migration standalone.
     */
    public static void main(String[] args) {
        try {
            // Initialize database
            String userHome = System.getProperty("user.home");
            String dbPath = userHome + "/beekeeper-desktop.db";
            DatabaseManager.initialize(dbPath);

            // Run migration
            migrate();

            System.out.println("\nMigration complete! Total translations inserted.");
        } catch (SQLException e) {
            System.err.println("Migration failed:");
            e.printStackTrace();
        }
    }
}
