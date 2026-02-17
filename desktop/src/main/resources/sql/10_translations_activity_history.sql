-- Hive activity history translations
-- Generated: 2026-02-17
-- Categories: dialog, label, prompt, tooltip, button, success, error, status

INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES

-- Dialog titles and headers
(lower(hex(randomblob(16))), 'dialog.activity.header_add', 'sk', 'Pridanie novej aktivity', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.activity.header_add', 'en', 'Add new activity', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.activity.header_edit', 'sk', 'Úprava aktivity', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.activity.header_edit', 'en', 'Edit activity', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.hive_history.title', 'sk', 'História úľa: {0}', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.hive_history.title', 'en', 'Hive history: {0}', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.hive_history.header', 'sk', 'Chronologický záznam zmien a udalostí', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.hive_history.header', 'en', 'Chronological record of changes and events', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.delete_activity.title', 'sk', 'Zmazať aktivitu', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.delete_activity.title', 'en', 'Delete activity', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.delete_activity.header', 'sk', 'Naozaj chcete zmazať túto aktivitu?', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.delete_activity.header', 'en', 'Do you really want to delete this activity?', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.delete_activity.content', 'sk', 'Táto operácia sa nedá vrátiť späť.', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.delete_activity.content', 'en', 'This operation cannot be undone.', 'dialog', datetime('now')),

-- Labels
(lower(hex(randomblob(16))), 'label.old_value', 'sk', 'Pôvodná hodnota', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.old_value', 'en', 'Old value', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.new_value', 'sk', 'Nová hodnota', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.new_value', 'en', 'New value', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.description', 'sk', 'Popis', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.description', 'en', 'Description', 'label', datetime('now')),

-- Prompts
(lower(hex(randomblob(16))), 'prompt.select_activity_type', 'sk', 'Vyberte typ aktivity', 'prompt', datetime('now')),
(lower(hex(randomblob(16))), 'prompt.select_activity_type', 'en', 'Select activity type', 'prompt', datetime('now')),
(lower(hex(randomblob(16))), 'prompt.optional', 'sk', 'Voliteľné', 'prompt', datetime('now')),
(lower(hex(randomblob(16))), 'prompt.optional', 'en', 'Optional', 'prompt', datetime('now')),
(lower(hex(randomblob(16))), 'prompt.optional_description', 'sk', 'Voliteľný popis aktivity', 'prompt', datetime('now')),
(lower(hex(randomblob(16))), 'prompt.optional_description', 'en', 'Optional activity description', 'prompt', datetime('now')),

-- Tooltips
(lower(hex(randomblob(16))), 'tooltip.old_value', 'sk', 'Pôvodná hodnota pred zmenou (napr. "2 medeníky")', 'tooltip', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.old_value', 'en', 'Original value before change (e.g. "2 supers")', 'tooltip', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.new_value', 'sk', 'Nová hodnota po zmene (napr. "3 medeníky")', 'tooltip', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.new_value', 'en', 'New value after change (e.g. "3 supers")', 'tooltip', datetime('now')),

-- Success messages
(lower(hex(randomblob(16))), 'success.activity_created', 'sk', 'Aktivita úspešne vytvorená', 'success', datetime('now')),
(lower(hex(randomblob(16))), 'success.activity_created', 'en', 'Activity created successfully', 'success', datetime('now')),
(lower(hex(randomblob(16))), 'success.activity_updated', 'sk', 'Aktivita úspešne aktualizovaná', 'success', datetime('now')),
(lower(hex(randomblob(16))), 'success.activity_updated', 'en', 'Activity updated successfully', 'success', datetime('now')),
(lower(hex(randomblob(16))), 'success.activity_deleted', 'sk', 'Aktivita úspešne zmazaná', 'success', datetime('now')),
(lower(hex(randomblob(16))), 'success.activity_deleted', 'en', 'Activity deleted successfully', 'success', datetime('now')),

-- Error messages
(lower(hex(randomblob(16))), 'error.loading_activities', 'sk', 'Chyba pri načítaní aktivít: {0}', 'error', datetime('now')),
(lower(hex(randomblob(16))), 'error.loading_activities', 'en', 'Error loading activities: {0}', 'error', datetime('now')),
(lower(hex(randomblob(16))), 'error.saving_activity', 'sk', 'Chyba pri ukladaní aktivity: {0}', 'error', datetime('now')),
(lower(hex(randomblob(16))), 'error.saving_activity', 'en', 'Error saving activity: {0}', 'error', datetime('now')),
(lower(hex(randomblob(16))), 'error.updating_activity', 'sk', 'Chyba pri aktualizácii aktivity: {0}', 'error', datetime('now')),
(lower(hex(randomblob(16))), 'error.updating_activity', 'en', 'Error updating activity: {0}', 'error', datetime('now')),
(lower(hex(randomblob(16))), 'error.deleting_activity', 'sk', 'Chyba pri mazaní aktivity: {0}', 'error', datetime('now')),
(lower(hex(randomblob(16))), 'error.deleting_activity', 'en', 'Error deleting activity: {0}', 'error', datetime('now')),

-- Status messages
(lower(hex(randomblob(16))), 'status.activities_count', 'sk', 'Počet aktivít: {0}', 'status', datetime('now')),
(lower(hex(randomblob(16))), 'status.activities_count', 'en', 'Activities count: {0}', 'status', datetime('now'));
