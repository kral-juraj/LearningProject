-- Translations for extended Apiary features
-- Generated: 2026-02-18

INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES

-- Registration Number
(lower(hex(randomblob(16))), 'label.registration_number', 'sk', 'Registračné číslo', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.registration_number', 'en', 'Registration Number', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.registration_number', 'sk', 'Evidenčné číslo včelnice', 'tooltip', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.registration_number', 'en', 'Apiary registration ID', 'tooltip', datetime('now')),

-- Address
(lower(hex(randomblob(16))), 'label.address', 'sk', 'Adresa', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.address', 'en', 'Address', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.apiary_address', 'sk', 'Detailná adresa včelnice', 'tooltip', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.apiary_address', 'en', 'Detailed apiary address', 'tooltip', datetime('now')),

-- Description
(lower(hex(randomblob(16))), 'label.description', 'sk', 'Popis', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.description', 'en', 'Description', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.apiary_description', 'sk', 'Popis alebo poznámky k včelnici', 'tooltip', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.apiary_description', 'en', 'Description or notes about the apiary', 'tooltip', datetime('now')),

-- Apiary tooltips
(lower(hex(randomblob(16))), 'tooltip.apiary_name', 'sk', 'Názov včelnice', 'tooltip', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.apiary_name', 'en', 'Apiary name', 'tooltip', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.apiary_location', 'sk', 'Lokalita alebo obec', 'tooltip', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.apiary_location', 'en', 'Location or municipality', 'tooltip', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.drag_to_reorder', 'sk', 'Ťahajte pre zmenu poradia', 'tooltip', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.drag_to_reorder', 'en', 'Drag to reorder', 'tooltip', datetime('now')),

-- Dashboard labels
(lower(hex(randomblob(16))), 'label.apiary_dashboard', 'sk', 'Prehľad včelnice', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.apiary_dashboard', 'en', 'Apiary Dashboard', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.hive_statistics', 'sk', 'Štatistiky úľov', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.hive_statistics', 'en', 'Hive Statistics', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.total_hives', 'sk', 'Celkový počet úľov', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.total_hives', 'en', 'Total Hives', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.active_hives', 'sk', 'Aktívne úle', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.active_hives', 'en', 'Active Hives', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.inactive_hives', 'sk', 'Neaktívne úle', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.inactive_hives', 'en', 'Inactive Hives', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.hives_by_type', 'sk', 'Úle podľa typu', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.hives_by_type', 'en', 'Hives by Type', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.no_hives', 'sk', 'Žiadne úle', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.no_hives', 'en', 'No hives', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.not_specified', 'sk', 'Neuvedené', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.not_specified', 'en', 'Not specified', 'label', datetime('now')),

-- Dialog headers
(lower(hex(randomblob(16))), 'dialog.apiary.header_add', 'sk', 'Vyplňte údaje o včelnici', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.apiary.header_add', 'en', 'Fill in apiary details', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.apiary.header_edit', 'sk', 'Upravte údaje o včelnici', 'dialog', datetime('now')),
(lower(hex(randomblob(16))), 'dialog.apiary.header_edit', 'en', 'Edit apiary details', 'dialog', datetime('now')),

-- Prompts
(lower(hex(randomblob(16))), 'prompt.optional', 'sk', 'Nepovinné', 'prompt', datetime('now')),
(lower(hex(randomblob(16))), 'prompt.optional', 'en', 'Optional', 'prompt', datetime('now'));
