-- Frame Type Translations (30 new keys = 60 translations SK+EN)
-- Generated: 2025-02-20
-- Purpose: Add translation keys for all frame type variants

INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES
-- B (Brda)
(lower(hex(randomblob(16))), 'hive.frame.b', 'sk', 'B (Brda) - 420×275 mm', 'frame', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.b', 'en', 'B (Brda) - 420×275 mm', 'frame', datetime('now')),

-- Langstroth variants
(lower(hex(randomblob(16))), 'hive.frame.langstroth_1_2', 'sk', 'Langstroth 1/2 - 420×135 mm', 'frame', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.langstroth_1_2', 'en', 'Langstroth 1/2 - 420×135 mm', 'frame', datetime('now')),

(lower(hex(randomblob(16))), 'hive.frame.langstroth_2_3', 'sk', 'Langstroth 2/3 - 420×176 mm (medník)', 'frame', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.langstroth_2_3', 'en', 'Langstroth 2/3 - 420×176 mm (super)', 'frame', datetime('now')),

(lower(hex(randomblob(16))), 'hive.frame.langstroth_3_4', 'sk', 'Langstroth 3/4 - 420×203 mm', 'frame', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.langstroth_3_4', 'en', 'Langstroth 3/4 - 420×203 mm', 'frame', datetime('now')),

(lower(hex(randomblob(16))), 'hive.frame.langstroth_1_1', 'sk', 'Langstroth 1/1 - 420×232 mm (plodisko)', 'frame', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.langstroth_1_1', 'en', 'Langstroth 1/1 - 420×232 mm (brood)', 'frame', datetime('now')),

(lower(hex(randomblob(16))), 'hive.frame.langstroth_jumbo', 'sk', 'Langstroth Jumbo - 420×285 mm', 'frame', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.langstroth_jumbo', 'en', 'Langstroth Jumbo - 420×285 mm', 'frame', datetime('now')),

(lower(hex(randomblob(16))), 'hive.frame.langstroth', 'sk', 'Langstroth - 420×232 mm', 'frame', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.langstroth', 'en', 'Langstroth - 420×232 mm', 'frame', datetime('now')),

-- Dadant variants
(lower(hex(randomblob(16))), 'hive.frame.dadant_plodisko', 'sk', 'Dadant Plodisko - 420×300 mm', 'frame', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.dadant_plodisko', 'en', 'Dadant Brood - 420×300 mm', 'frame', datetime('now')),

(lower(hex(randomblob(16))), 'hive.frame.dadant_mednik_1_2', 'sk', 'Dadant Medník 1/2 - 420×145 mm', 'frame', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.dadant_mednik_1_2', 'en', 'Dadant Super 1/2 - 420×145 mm', 'frame', datetime('now')),

(lower(hex(randomblob(16))), 'hive.frame.dadant_blatt', 'sk', 'Dadant Blatt - 420×470 mm', 'frame', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.dadant_blatt', 'en', 'Dadant Blatt - 420×470 mm', 'frame', datetime('now')),

(lower(hex(randomblob(16))), 'hive.frame.dadant', 'sk', 'Dadant - 420×300 mm', 'frame', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.dadant', 'en', 'Dadant - 420×300 mm', 'frame', datetime('now')),

-- Zander variants
(lower(hex(randomblob(16))), 'hive.frame.zander_cely', 'sk', 'Zander Celý - 420×220 mm (plodisko)', 'frame', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.zander_cely', 'en', 'Zander Full - 420×220 mm (brood)', 'frame', datetime('now')),

(lower(hex(randomblob(16))), 'hive.frame.zander_2_3', 'sk', 'Zander 2/3 - 420×159 mm (medník)', 'frame', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.zander_2_3', 'en', 'Zander 2/3 - 420×159 mm (super)', 'frame', datetime('now')),

(lower(hex(randomblob(16))), 'hive.frame.zander_1_2', 'sk', 'Zander 1/2 - 420×110 mm (nízky)', 'frame', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.zander_1_2', 'en', 'Zander 1/2 - 420×110 mm (shallow)', 'frame', datetime('now')),

(lower(hex(randomblob(16))), 'hive.frame.zander_1_5', 'sk', 'Zander 1,5 - 420×337 mm (vysoký)', 'frame', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.zander_1_5', 'en', 'Zander 1.5 - 420×337 mm (deep)', 'frame', datetime('now')),

(lower(hex(randomblob(16))), 'hive.frame.zander', 'sk', 'Zander - 420×220 mm', 'frame', datetime('now')),
(lower(hex(randomblob(16))), 'hive.frame.zander', 'en', 'Zander - 420×220 mm', 'frame', datetime('now'));
