-- Fix standard method translation keys to match code
INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES
-- Day 7 - inspect_brood (currently missing)
(lower(hex(randomblob(16))), 'queen.std.inspect_brood', 'sk', 'Prehliadka plodu', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.std.inspect_brood', 'en', 'Inspect Brood', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.std.inspect_brood.desc', 'sk', 'Kontrola matečníkov, odstránenie nekvalitných. Larvičky sa začínajú zavíčkovať.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.std.inspect_brood.desc', 'en', 'Inspect cells, remove poor quality ones. Larvae starting to be capped.', 'queen', datetime('now')),

-- Day 10 - inspect_cells (currently missing)
(lower(hex(randomblob(16))), 'queen.std.inspect_cells', 'sk', 'Prehliadka matečníkov', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.std.inspect_cells', 'en', 'Inspect Queen Cells', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.std.inspect_cells.desc', 'sk', 'Všetky matečníky zavíčkované. Kontrola správnosti zavíčkovania.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.std.inspect_cells.desc', 'en', 'All cells capped. Check proper capping.', 'queen', datetime('now')),

-- Day 21 - check_laying (currently missing)
(lower(hex(randomblob(16))), 'queen.std.check_laying', 'sk', 'Kontrola kládky', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.std.check_laying', 'en', 'Check Laying', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.std.check_laying.desc', 'sk', 'Kontrola kládky mladých matiek. Matky začínajú klásť vajíčka.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.std.check_laying.desc', 'en', 'Check young queen laying. Queens start laying eggs.', 'queen', datetime('now'));
