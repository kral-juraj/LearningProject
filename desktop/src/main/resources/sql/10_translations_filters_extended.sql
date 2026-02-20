-- Extended Taxation Filter Translations (6 new keys = 12 translations SK+EN)
-- Generated: 2025-02-20
-- Purpose: Add translation keys for brood, stores, and pollen range filters

INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES
-- Brood range filter
(lower(hex(randomblob(16))), 'filter.brood_range', 'sk', 'Plod (dm²)', 'filter', datetime('now')),
(lower(hex(randomblob(16))), 'filter.brood_range', 'en', 'Brood (dm²)', 'filter', datetime('now')),

-- Stores range filter
(lower(hex(randomblob(16))), 'filter.stores_range', 'sk', 'Zásoby (dm²)', 'filter', datetime('now')),
(lower(hex(randomblob(16))), 'filter.stores_range', 'en', 'Stores (dm²)', 'filter', datetime('now')),

-- Pollen range filter
(lower(hex(randomblob(16))), 'filter.pollen_range', 'sk', 'Peľ (dm²)', 'filter', datetime('now')),
(lower(hex(randomblob(16))), 'filter.pollen_range', 'en', 'Pollen (dm²)', 'filter', datetime('now'));
