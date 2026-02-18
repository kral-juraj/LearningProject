-- Fix missing translations and incorrect format strings
-- Generated: 2026-02-18

-- Add missing label.location
INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES
(lower(hex(randomblob(16))), 'label.location', 'sk', 'Lokalita', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.location', 'en', 'Location', 'label', datetime('now'));

-- Fix status.apiaries_count format (use {0} instead of %d)
UPDATE translations SET value = '{0} včelníc' WHERE key = 'status.apiaries_count' AND language = 'sk';
UPDATE translations SET value = '{0} apiaries' WHERE key = 'status.apiaries_count' AND language = 'en';

-- Fix status.hives_count format (use {0} instead of %d)
UPDATE translations SET value = '{0} úľov' WHERE key = 'status.hives_count' AND language = 'sk';
UPDATE translations SET value = '{0} hives' WHERE key = 'status.hives_count' AND language = 'en';
