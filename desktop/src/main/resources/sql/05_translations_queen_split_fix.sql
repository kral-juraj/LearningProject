-- Fix split method translation keys to match code
INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES
-- choose_one (currently using select_one in DB, need choose_one)
(lower(hex(randomblob(16))), 'queen.split.choose_one', 'sk', 'Zvoliť si len 1 matečník', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.choose_one', 'en', 'Choose Only 1 Cell', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.choose_one.desc', 'sk', 'Finálna kontrola, nechať len jeden kvalitný matečník.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.choose_one.desc', 'en', 'Final check, leave only one quality cell.', 'queen', datetime('now')),

-- nurse_cells (currently using cell_training in DB, need nurse_cells)
(lower(hex(randomblob(16))), 'queen.split.nurse_cells', 'sk', 'Školkovanie matečníkov', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.nurse_cells', 'en', 'Nurse Queen Cells', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.nurse_cells.desc', 'sk', 'Kontrola stavu matečníkov, prípadné doškôlkovanie.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.nurse_cells.desc', 'en', 'Check cell status, additional nursing if needed.', 'queen', datetime('now')),

-- prepare_trap (currently using varroa_trap_prep in DB, need prepare_trap)
(lower(hex(randomblob(16))), 'queen.split.prepare_trap', 'sk', 'Pripraviť varroa pascu v oddielku', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.prepare_trap', 'en', 'Prepare Varroa Trap in Nuc', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.prepare_trap.desc', 'sk', 'Príprava rámika na varroa pascu v oddielku (nová rodina z opačnenca).', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.prepare_trap.desc', 'en', 'Prepare frame for varroa trap in nucleus box (new colony from split).', 'queen', datetime('now')),

-- insert_trap_eggs (currently using varroa_trap_eggs in DB, need insert_trap_eggs)
(lower(hex(randomblob(16))), 'queen.split.insert_trap_eggs', 'sk', 'Vložiť varroa pascu (vajíčka)', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.insert_trap_eggs', 'en', 'Insert Varroa Trap (eggs)', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.insert_trap_eggs.desc', 'sk', 'Vložiť rámik s vajíčkami zo zdrojového úľa. Foretické kliešte zlezú z včiel na tento rámik a invadujú bunky.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.insert_trap_eggs.desc', 'en', 'Insert frame with eggs from source hive. Phoretic mites will invade cells.', 'queen', datetime('now')),

-- insert_trap_larvae (currently using varroa_trap_larvae in DB, need insert_trap_larvae)
(lower(hex(randomblob(16))), 'queen.split.insert_trap_larvae', 'sk', 'Vložiť varroa pascu (larvy)', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.insert_trap_larvae', 'en', 'Insert Varroa Trap (larvae)', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.insert_trap_larvae.desc', 'sk', 'Vložiť rámik s larvami (viac lariev = viac kliešťov). Alternatívna pasca s otvoreným plodom.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.insert_trap_larvae.desc', 'en', 'Insert frame with larvae (more larvae = more mites). Alternative trap with open brood.', 'queen', datetime('now')),

-- last_worker_brood (currently using last_old_worker in DB, need last_worker_brood)
(lower(hex(randomblob(16))), 'queen.split.last_worker_brood', 'sk', 'Posledný starý robotníci plod', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.last_worker_brood', 'en', 'Last Old Worker Brood', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.last_worker_brood.desc', 'sk', 'Posledný starý liahnuci sa robotníci plod zo starej matky.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.last_worker_brood.desc', 'en', 'Last old worker brood emerging from old queen.', 'queen', datetime('now')),

-- mating_flights (currently using orientation_flights in DB, need mating_flights)
(lower(hex(randomblob(16))), 'queen.split.mating_flights', 'sk', 'Orientačné páriace prelety', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.mating_flights', 'en', 'Orientation & Mating Flights', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.mating_flights.desc', 'sk', 'Matka robí orientačné a páriace prelety (D+20 až D+25).', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.mating_flights.desc', 'en', 'Queen makes orientation and mating flights (D+20 to D+25).', 'queen', datetime('now')),

-- last_drone_brood (currently using last_old_drone in DB, need last_drone_brood)
(lower(hex(randomblob(16))), 'queen.split.last_drone_brood', 'sk', 'Posledný starý trúdi plod', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.last_drone_brood', 'en', 'Last Old Drone Brood', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.last_drone_brood.desc', 'sk', 'Posledný starý liahnuci sa trúdi plod zo starej matky.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.last_drone_brood.desc', 'en', 'Last old drone brood emerging from old queen.', 'queen', datetime('now')),

-- new_queen_laying (currently using laying_starts in DB, need new_queen_laying)
(lower(hex(randomblob(16))), 'queen.split.new_queen_laying', 'sk', 'Začiatok kládenia novej matky', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.new_queen_laying', 'en', 'New Queen Starts Laying', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.new_queen_laying.desc', 'sk', 'Mladá matka začína klásť vajíčka (D+26 až D+29).', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.new_queen_laying.desc', 'en', 'Young queen starts laying eggs (D+26 to D+29).', 'queen', datetime('now')),

-- new_foragers_start (currently using first_new_foragers in DB, need new_foragers_start)
(lower(hex(randomblob(16))), 'queen.split.new_foragers_start', 'sk', 'Prvé nové lietavky', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.new_foragers_start', 'en', 'First New Foragers', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.new_foragers_start.desc', 'sk', 'Prvé lietavky z nového plodu začínajú lietať.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.split.new_foragers_start.desc', 'en', 'First foragers from new brood start flying.', 'queen', datetime('now'));
