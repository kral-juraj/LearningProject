-- QueenRearingCalculator milestone translations - STANDARD METHOD
INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES
-- Day 0
(lower(hex(randomblob(16))), 'queen.milestone.start_cells.name', 'sk', 'Založenie matečníkov', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.start_cells.name', 'en', 'Start Queen Cells', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.start_cells.desc', 'sk', 'Prekladanie larvičiek do matečníkov. Ideálny vek larvičiek: 12-24 hodín.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.start_cells.desc', 'en', 'Grafting larvae into queen cells. Ideal larva age: 12-24 hours.', 'queen', datetime('now')),

-- Day 1
(lower(hex(randomblob(16))), 'queen.milestone.acceptance_check.name', 'sk', 'Priloženie mriežky', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.acceptance_check.name', 'en', 'Add Queen Excluder', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.acceptance_check.desc', 'sk', 'Kontrola prijatia matečníkov. Priloženie materskej mriežky.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.acceptance_check.desc', 'en', 'Check cell acceptance. Add queen excluder.', 'queen', datetime('now')),

-- Day 4
(lower(hex(randomblob(16))), 'queen.milestone.inspect_cells.name', 'sk', 'Prehliadka matečníkov', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.inspect_cells.name', 'en', 'Inspect Queen Cells', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.inspect_cells.desc', 'sk', 'Kontrola matečníkov, odstránenie nekvalitných. Larvičky sa začínajú zavíčkovať.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.inspect_cells.desc', 'en', 'Inspect cells, remove poor quality ones. Larvae starting to be capped.', 'queen', datetime('now')),

-- Day 6
(lower(hex(randomblob(16))), 'queen.milestone.check_capped.name', 'sk', 'Prehliadka matečníkov', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.check_capped.name', 'en', 'Inspect Queen Cells', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.check_capped.desc', 'sk', 'Všetky matečníky zavíčkované. Kontrola správnosti zavíčkovania.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.check_capped.desc', 'en', 'All cells capped. Check proper capping.', 'queen', datetime('now')),

-- Day 8
(lower(hex(randomblob(16))), 'queen.milestone.capping_complete.name', 'sk', 'Zavíčkovanie matečníkov', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.capping_complete.name', 'en', 'Cell Capping Complete', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.capping_complete.desc', 'sk', 'Matky sa vyvíjajú v zavíčkovaných matečníkoch. Príprava oddielkov.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.capping_complete.desc', 'en', 'Queens developing in capped cells. Prepare nucleus boxes.', 'queen', datetime('now')),

-- Day 11
(lower(hex(randomblob(16))), 'queen.milestone.place_cells.name', 'sk', 'Presadenie', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.place_cells.name', 'en', 'Place Cells', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.place_cells.desc', 'sk', 'Matečníky tesne pred vyhryznutím. Presadenie do oddielkov alebo oplodniačikov.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.place_cells.desc', 'en', 'Cells close to emergence. Place in nucs or mating boxes.', 'queen', datetime('now')),

-- Day 13
(lower(hex(randomblob(16))), 'queen.milestone.check_laying.name', 'sk', 'Kontrola kládky', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.check_laying.name', 'en', 'Check Laying', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.check_laying.desc', 'sk', 'Kontrola kládky mladých matiek. Matky začínajú klásť vajíčka.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.milestone.check_laying.desc', 'en', 'Check young queen laying. Queens start laying eggs.', 'queen', datetime('now'));
