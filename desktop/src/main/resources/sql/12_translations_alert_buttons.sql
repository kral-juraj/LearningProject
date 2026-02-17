-- Preklady pre tlačidlá v Alert dialógoch
-- Dátum: 2026-02-17

INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES
-- Tlačidlá pre Alert dialógy
(lower(hex(randomblob(16))), 'button.ok', 'sk', 'OK', 'button', datetime('now')),
(lower(hex(randomblob(16))), 'button.ok', 'en', 'OK', 'button', datetime('now')),

(lower(hex(randomblob(16))), 'button.yes', 'sk', 'Áno', 'button', datetime('now')),
(lower(hex(randomblob(16))), 'button.yes', 'en', 'Yes', 'button', datetime('now')),

(lower(hex(randomblob(16))), 'button.no', 'sk', 'Nie', 'button', datetime('now')),
(lower(hex(randomblob(16))), 'button.no', 'en', 'No', 'button', datetime('now')),

-- Dialóg na zmazanie úľa
(lower(hex(randomblob(16))), 'dialog.delete_hive.title', 'sk', 'Zmazať úľ', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.delete_hive.title', 'en', 'Delete Hive', 'dialog', datetime('now')),

(lower(hex(randomblob(16))), 'dialog.delete_hive.header', 'sk', 'Naozaj chcete zmazať úľ "{0}"?', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.delete_hive.header', 'en', 'Do you really want to delete hive "{0}"?', 'dialog', datetime('now')),

(lower(hex(randomblob(16))), 'dialog.delete_hive.content', 'sk', 'Táto akcia je nenávratná. Všetky dáta spojené s týmto úľom budú trvalo odstránené.', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.delete_hive.content', 'en', 'This action cannot be undone. All data associated with this hive will be permanently deleted.', 'dialog', datetime('now'));
