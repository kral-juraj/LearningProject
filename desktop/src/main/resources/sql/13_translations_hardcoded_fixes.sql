-- Fix all hardcoded strings in new classes
-- Date: 2025-02-17
-- Categories: prompt, error, exception

INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES

-- Prompt texts for HiveActivityDialog auto-suggestions (Slovak)
(lower(hex(randomblob(16))), 'prompt.supers_example_2', 'sk', 'napr. 2 medeníky', 'prompt', datetime('now')),
(lower(hex(randomblob(16))), 'prompt.supers_example_3', 'sk', 'napr. 3 medeníky', 'prompt', datetime('now')),
(lower(hex(randomblob(16))), 'prompt.bottom_board_low', 'sk', 'napr. nízke dno', 'prompt', datetime('now')),
(lower(hex(randomblob(16))), 'prompt.bottom_board_high', 'sk', 'napr. vysoké dno', 'prompt', datetime('now')),
(lower(hex(randomblob(16))), 'prompt.honey_harvest_kg', 'sk', 'napr. 15 kg', 'prompt', datetime('now')),

-- Prompt texts for HiveActivityDialog auto-suggestions (English)
(lower(hex(randomblob(16))), 'prompt.supers_example_2', 'en', 'e.g. 2 supers', 'prompt', datetime('now')),
(lower(hex(randomblob(16))), 'prompt.supers_example_3', 'en', 'e.g. 3 supers', 'prompt', datetime('now')),
(lower(hex(randomblob(16))), 'prompt.bottom_board_low', 'en', 'e.g. low bottom board', 'prompt', datetime('now')),
(lower(hex(randomblob(16))), 'prompt.bottom_board_high', 'en', 'e.g. high bottom board', 'prompt', datetime('now')),
(lower(hex(randomblob(16))), 'prompt.honey_harvest_kg', 'en', 'e.g. 15 kg', 'prompt', datetime('now')),

-- Exception messages for JdbcHiveActivityDao (Slovak)
(lower(hex(randomblob(16))), 'exception.hive_activity_not_found', 'sk', 'Aktivita úľa nebola nájdená: {0}', 'exception', datetime('now')),
(lower(hex(randomblob(16))), 'exception.failed_to_load_hive_card_fxml', 'sk', 'Nepodarilo sa načítať FXML karty úľa', 'exception', datetime('now')),

-- Exception messages for JdbcHiveActivityDao (English)
(lower(hex(randomblob(16))), 'exception.hive_activity_not_found', 'en', 'Hive activity not found: {0}', 'exception', datetime('now')),
(lower(hex(randomblob(16))), 'exception.failed_to_load_hive_card_fxml', 'en', 'Failed to load HiveCard FXML', 'exception', datetime('now')),

-- HiveViewModel error and success messages (Slovak)
(lower(hex(randomblob(16))), 'error.loading_hives', 'sk', 'Chyba pri načítaní úľov: {0}', 'error', datetime('now')),
(lower(hex(randomblob(16))), 'validation.hive_name_required', 'sk', 'Názov úľa nemôže byť prázdny', 'validation', datetime('now')),
(lower(hex(randomblob(16))), 'success.hive_created', 'sk', 'Úľ úspešne vytvorený', 'success', datetime('now')),
(lower(hex(randomblob(16))), 'error.creating_hive', 'sk', 'Chyba pri vytváraní úľa: {0}', 'error', datetime('now')),
(lower(hex(randomblob(16))), 'success.hive_updated', 'sk', 'Úľ úspešne aktualizovaný', 'success', datetime('now')),
(lower(hex(randomblob(16))), 'error.updating_hive', 'sk', 'Chyba pri aktualizácii úľa: {0}', 'error', datetime('now')),
(lower(hex(randomblob(16))), 'success.hive_deleted', 'sk', 'Úľ úspešne zmazaný', 'success', datetime('now')),
(lower(hex(randomblob(16))), 'error.deleting_hive', 'sk', 'Chyba pri mazaní úľa: {0}', 'error', datetime('now')),
(lower(hex(randomblob(16))), 'error.updating_order', 'sk', 'Chyba pri aktualizácii poradia: {0}', 'error', datetime('now')),

-- HiveViewModel error and success messages (English)
(lower(hex(randomblob(16))), 'error.loading_hives', 'en', 'Error loading hives: {0}', 'error', datetime('now')),
(lower(hex(randomblob(16))), 'validation.hive_name_required', 'en', 'Hive name cannot be empty', 'validation', datetime('now')),
(lower(hex(randomblob(16))), 'success.hive_created', 'en', 'Hive created successfully', 'success', datetime('now')),
(lower(hex(randomblob(16))), 'error.creating_hive', 'en', 'Error creating hive: {0}', 'error', datetime('now')),
(lower(hex(randomblob(16))), 'success.hive_updated', 'en', 'Hive updated successfully', 'success', datetime('now')),
(lower(hex(randomblob(16))), 'error.updating_hive', 'en', 'Error updating hive: {0}', 'error', datetime('now')),
(lower(hex(randomblob(16))), 'success.hive_deleted', 'en', 'Hive deleted successfully', 'success', datetime('now')),
(lower(hex(randomblob(16))), 'error.deleting_hive', 'en', 'Error deleting hive: {0}', 'error', datetime('now')),
(lower(hex(randomblob(16))), 'error.updating_order', 'en', 'Error updating order: {0}', 'error', datetime('now'));
