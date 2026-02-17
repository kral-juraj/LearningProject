-- Settings Dialog Translations

INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES
-- Dialog title
(lower(hex(randomblob(16))), 'settings.dialog.title', 'sk', 'Nastavenia', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.dialog.title', 'en', 'Settings', 'settings', datetime('now')),

-- Tab names
(lower(hex(randomblob(16))), 'settings.tab.general', 'sk', 'Všeobecné', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.tab.general', 'en', 'General', 'settings', datetime('now')),

-- Section headers
(lower(hex(randomblob(16))), 'settings.section.date_formats', 'sk', 'Formáty dátumov a času', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.section.date_formats', 'en', 'Date and Time Formats', 'settings', datetime('now')),

-- Field labels
(lower(hex(randomblob(16))), 'settings.label.date_format', 'sk', 'Formát dátumu:', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.label.date_format', 'en', 'Date format:', 'settings', datetime('now')),

(lower(hex(randomblob(16))), 'settings.label.time_format', 'sk', 'Formát času:', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.label.time_format', 'en', 'Time format:', 'settings', datetime('now')),

(lower(hex(randomblob(16))), 'settings.label.datetime_format', 'sk', 'Formát dátumu a času:', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.label.datetime_format', 'en', 'Date and time format:', 'settings', datetime('now')),

(lower(hex(randomblob(16))), 'settings.label.preview', 'sk', 'Náhľad:', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.label.preview', 'en', 'Preview:', 'settings', datetime('now')),

(lower(hex(randomblob(16))), 'settings.label.format_examples', 'sk', 'Príklady formátov:', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.label.format_examples', 'en', 'Format examples:', 'settings', datetime('now')),

-- Format hints
(lower(hex(randomblob(16))), 'settings.hint.date_format', 'sk', 'd=deň, M=mesiac, y=rok (dd=01-31, MM=01-12, yyyy=2026)', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.hint.date_format', 'en', 'd=day, M=month, y=year (dd=01-31, MM=01-12, yyyy=2026)', 'settings', datetime('now')),

(lower(hex(randomblob(16))), 'settings.hint.time_format', 'sk', 'H=hodina, m=minúta, s=sekunda (HH=00-23, mm=00-59)', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.hint.time_format', 'en', 'H=hour, m=minute, s=second (HH=00-23, mm=00-59)', 'settings', datetime('now')),

(lower(hex(randomblob(16))), 'settings.hint.datetime_format', 'sk', 'Kombinácia formátu dátumu a času', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.hint.datetime_format', 'en', 'Combination of date and time format', 'settings', datetime('now')),

-- Format examples
(lower(hex(randomblob(16))), 'settings.example.dd_MM_yyyy', 'sk', '• dd.MM.yyyy → 31.12.2026 (slovenský)', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.example.dd_MM_yyyy', 'en', '• dd.MM.yyyy → 31.12.2026 (European)', 'settings', datetime('now')),

(lower(hex(randomblob(16))), 'settings.example.MM_dd_yyyy', 'sk', '• MM/dd/yyyy → 12/31/2026 (americký)', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.example.MM_dd_yyyy', 'en', '• MM/dd/yyyy → 12/31/2026 (US)', 'settings', datetime('now')),

(lower(hex(randomblob(16))), 'settings.example.yyyy_MM_dd', 'sk', '• yyyy-MM-dd → 2026-12-31 (ISO)', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.example.yyyy_MM_dd', 'en', '• yyyy-MM-dd → 2026-12-31 (ISO)', 'settings', datetime('now')),

(lower(hex(randomblob(16))), 'settings.example.d_M_yy', 'sk', '• d.M.yy → 31.12.26 (krátky)', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.example.d_M_yy', 'en', '• d.M.yy → 31.12.26 (short)', 'settings', datetime('now')),

-- Buttons
(lower(hex(randomblob(16))), 'settings.button.reset_defaults', 'sk', 'Obnoviť predvolené', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.button.reset_defaults', 'en', 'Reset to Defaults', 'settings', datetime('now')),

-- Preview states
(lower(hex(randomblob(16))), 'settings.preview.invalid', 'sk', 'Neplatný formát', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.preview.invalid', 'en', 'Invalid format', 'settings', datetime('now')),

-- Error messages
(lower(hex(randomblob(16))), 'settings.error.validation_failed', 'sk', 'Validácia zlyhala', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.error.validation_failed', 'en', 'Validation failed', 'settings', datetime('now')),

(lower(hex(randomblob(16))), 'settings.error.invalid_date_format', 'sk', 'Neplatný formát dátumu. Použite platný pattern (napr. dd.MM.yyyy)', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.error.invalid_date_format', 'en', 'Invalid date format. Use valid pattern (e.g., dd.MM.yyyy)', 'settings', datetime('now')),

(lower(hex(randomblob(16))), 'settings.error.invalid_time_format', 'sk', 'Neplatný formát času. Použite platný pattern (napr. HH:mm)', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.error.invalid_time_format', 'en', 'Invalid time format. Use valid pattern (e.g., HH:mm)', 'settings', datetime('now')),

(lower(hex(randomblob(16))), 'settings.error.invalid_datetime_format', 'sk', 'Neplatný formát dátumu a času. Použite platný pattern (napr. dd.MM.yyyy HH:mm)', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.error.invalid_datetime_format', 'en', 'Invalid datetime format. Use valid pattern (e.g., dd.MM.yyyy HH:mm)', 'settings', datetime('now')),

-- Restart prompt
(lower(hex(randomblob(16))), 'settings.restart.title', 'sk', 'Nastavenia zmenené', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.restart.title', 'en', 'Settings Changed', 'settings', datetime('now')),

(lower(hex(randomblob(16))), 'settings.restart.header', 'sk', 'Zmeny boli uložené', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.restart.header', 'en', 'Changes have been saved', 'settings', datetime('now')),

(lower(hex(randomblob(16))), 'settings.restart.content', 'sk', 'Aplikácia sa teraz zatvorí. Prosím reštartujte ju aby sa nové formáty dátumov aplikovali.', 'settings', datetime('now')),
(lower(hex(randomblob(16))), 'settings.restart.content', 'en', 'The application will now close. Please restart it to apply the new date formats.', 'settings', datetime('now'));
