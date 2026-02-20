-- Fix translation: "stokované" → "medník"
-- Add new percentage filters for free space and capped stores

-- Update existing "needs_supering" translation (will be removed from UI but keep for reference)
UPDATE translations
SET value = 'Potrebuje medník'
WHERE key = 'filter.needs_supering' AND language = 'sk';

UPDATE translations
SET value = 'Needs honey super'
WHERE key = 'filter.needs_supering' AND language = 'en';

-- Add new percentage filter translations
INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES
(lower(hex(randomblob(16))), 'filter.free_space_percent', 'sk', 'Voľné miesto %', 'filter', datetime('now')),
(lower(hex(randomblob(16))), 'filter.free_space_percent', 'en', 'Free Space %', 'filter', datetime('now')),

(lower(hex(randomblob(16))), 'filter.capped_stores_percent', 'sk', 'Zapečatené zásoby %', 'filter', datetime('now')),
(lower(hex(randomblob(16))), 'filter.capped_stores_percent', 'en', 'Capped Stores %', 'filter', datetime('now')),

(lower(hex(randomblob(16))), 'symbol.percent', 'sk', '%', 'symbol', datetime('now')),
(lower(hex(randomblob(16))), 'symbol.percent', 'en', '%', 'symbol', datetime('now'));

SELECT '✅ Translations updated' as status;
