-- Taxation Filter Translations (24 new keys = 48 translations SK+EN)
-- Generated: 2025-02-20
-- Purpose: Add translation keys for taxation filtering UI

INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES
-- Filter labels and prompts
(lower(hex(randomblob(16))), 'label.filters', 'sk', 'Filtre', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.filters', 'en', 'Filters', 'label', datetime('now')),

(lower(hex(randomblob(16))), 'filter.date_range', 'sk', 'Dátumový rozsah', 'filter', datetime('now')),
(lower(hex(randomblob(16))), 'filter.date_range', 'en', 'Date Range', 'filter', datetime('now')),

(lower(hex(randomblob(16))), 'filter.date_from', 'sk', 'Od', 'filter', datetime('now')),
(lower(hex(randomblob(16))), 'filter.date_from', 'en', 'From', 'filter', datetime('now')),

(lower(hex(randomblob(16))), 'filter.date_to', 'sk', 'Do', 'filter', datetime('now')),
(lower(hex(randomblob(16))), 'filter.date_to', 'en', 'To', 'filter', datetime('now')),

(lower(hex(randomblob(16))), 'filter.hive_name', 'sk', 'Názov úľa', 'filter', datetime('now')),
(lower(hex(randomblob(16))), 'filter.hive_name', 'en', 'Hive Name', 'filter', datetime('now')),

(lower(hex(randomblob(16))), 'filter.hive_name_prompt', 'sk', 'Hľadať úľ...', 'filter', datetime('now')),
(lower(hex(randomblob(16))), 'filter.hive_name_prompt', 'en', 'Search hive...', 'filter', datetime('now')),

(lower(hex(randomblob(16))), 'filter.free_space', 'sk', 'Voľné miesto (dm²)', 'filter', datetime('now')),
(lower(hex(randomblob(16))), 'filter.free_space', 'en', 'Free Space (dm²)', 'filter', datetime('now')),

(lower(hex(randomblob(16))), 'filter.min', 'sk', 'Min', 'filter', datetime('now')),
(lower(hex(randomblob(16))), 'filter.min', 'en', 'Min', 'filter', datetime('now')),

(lower(hex(randomblob(16))), 'filter.max', 'sk', 'Max', 'filter', datetime('now')),
(lower(hex(randomblob(16))), 'filter.max', 'en', 'Max', 'filter', datetime('now')),

(lower(hex(randomblob(16))), 'filter.has_brood', 'sk', 'Má plod', 'filter', datetime('now')),
(lower(hex(randomblob(16))), 'filter.has_brood', 'en', 'Has Brood', 'filter', datetime('now')),

(lower(hex(randomblob(16))), 'filter.has_starter_frames', 'sk', 'Má matečníky', 'filter', datetime('now')),
(lower(hex(randomblob(16))), 'filter.has_starter_frames', 'en', 'Has Queen Cells', 'filter', datetime('now')),

(lower(hex(randomblob(16))), 'filter.needs_supering', 'sk', 'Potrebuje stokovanie', 'filter', datetime('now')),
(lower(hex(randomblob(16))), 'filter.needs_supering', 'en', 'Needs Supering', 'filter', datetime('now')),

(lower(hex(randomblob(16))), 'filter.temperature_range', 'sk', 'Teplota (°C)', 'filter', datetime('now')),
(lower(hex(randomblob(16))), 'filter.temperature_range', 'en', 'Temperature (°C)', 'filter', datetime('now')),

(lower(hex(randomblob(16))), 'filter.frame_count_range', 'sk', 'Počet rámikov', 'filter', datetime('now')),
(lower(hex(randomblob(16))), 'filter.frame_count_range', 'en', 'Frame Count', 'filter', datetime('now')),

(lower(hex(randomblob(16))), 'filter.strength_range', 'sk', 'Sila (1-10)', 'filter', datetime('now')),
(lower(hex(randomblob(16))), 'filter.strength_range', 'en', 'Strength (1-10)', 'filter', datetime('now')),

-- Filter buttons
(lower(hex(randomblob(16))), 'button.apply_filters', 'sk', 'Použiť filtre', 'button', datetime('now')),
(lower(hex(randomblob(16))), 'button.apply_filters', 'en', 'Apply Filters', 'button', datetime('now')),

(lower(hex(randomblob(16))), 'button.clear_filters', 'sk', 'Vymazať filtre', 'button', datetime('now')),
(lower(hex(randomblob(16))), 'button.clear_filters', 'en', 'Clear Filters', 'button', datetime('now')),

-- Table columns for calculated values
(lower(hex(randomblob(16))), 'table.free_space_dm', 'sk', 'Voľné (dm²)', 'table', datetime('now')),
(lower(hex(randomblob(16))), 'table.free_space_dm', 'en', 'Free (dm²)', 'table', datetime('now')),

(lower(hex(randomblob(16))), 'table.strength', 'sk', 'Sila', 'table', datetime('now')),
(lower(hex(randomblob(16))), 'table.strength', 'en', 'Strength', 'table', datetime('now')),

-- Status messages
(lower(hex(randomblob(16))), 'status.filters_applied', 'sk', 'Filtre aplikované', 'status', datetime('now')),
(lower(hex(randomblob(16))), 'status.filters_applied', 'en', 'Filters applied', 'status', datetime('now')),

(lower(hex(randomblob(16))), 'status.filters_cleared', 'sk', 'Filtre vymazané', 'status', datetime('now')),
(lower(hex(randomblob(16))), 'status.filters_cleared', 'en', 'Filters cleared', 'status', datetime('now')),

(lower(hex(randomblob(16))), 'status.taxations_filtered', 'sk', '%d taxácií (filtrované)', 'status', datetime('now')),
(lower(hex(randomblob(16))), 'status.taxations_filtered', 'en', '%d taxations (filtered)', 'status', datetime('now')),

(lower(hex(randomblob(16))), 'label.actions', 'sk', 'Akcie', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.actions', 'en', 'Actions', 'label', datetime('now'));
