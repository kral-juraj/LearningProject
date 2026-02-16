-- QueenRearingCalculator milestone translations - CAGING METHOD (Klietkovanie + Norské zimovanie)
INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES
-- Day 0
(lower(hex(randomblob(16))), 'queen.caging.cage_queen', 'sk', 'Klietkovanie matky', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.cage_queen', 'en', 'Cage Queen', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.cage_queen.desc', 'sk', 'Matku zaklietkovať do klietky. Matka prestane klásť vajíčka.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.cage_queen.desc', 'en', 'Cage the queen. Queen will stop laying eggs.', 'queen', datetime('now')),

-- Day 21
(lower(hex(randomblob(16))), 'queen.caging.last_worker_brood', 'sk', 'Posledný starý robotníci plod', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.last_worker_brood', 'en', 'Last Old Worker Brood', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.last_worker_brood.desc', 'sk', 'Posledný starý liahnuci sa robotníci plod vylíahnutý.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.last_worker_brood.desc', 'en', 'Last old worker brood has emerged.', 'queen', datetime('now')),

-- Day 24
(lower(hex(randomblob(16))), 'queen.caging.last_drone_brood', 'sk', 'Posledný starý trúdi plod', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.last_drone_brood', 'en', 'Last Old Drone Brood', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.last_drone_brood.desc', 'sk', 'Posledný starý liahnuci sa trúdi plod vylíahnutý.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.last_drone_brood.desc', 'en', 'Last old drone brood has emerged.', 'queen', datetime('now')),

-- Day 25 (info)
(lower(hex(randomblob(16))), 'queen.caging.broodless_start', 'sk', 'Bezplodové obdobie - začiatok', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.broodless_start', 'en', 'Broodless Period Start', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.broodless_start.desc', 'sk', 'Začiatok bezplodového obdobia. Všetok plod sa vylíahol.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.broodless_start.desc', 'en', 'Start of broodless period. All brood has emerged.', 'queen', datetime('now')),

-- Day 25 (action)
(lower(hex(randomblob(16))), 'queen.caging.shook_swarm', 'sk', 'Presypanie na medzistienky', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.shook_swarm', 'en', 'Shook Swarm onto Foundation', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.shook_swarm.desc', 'sk', 'Norské zimovanie: Presypať včely na medzistienky, presunúť medníky. Obnova diela, získanie nových plastov, zníženie tlaku vírusov. Ak nie je znaška, prikrmiť roztokom 1:1 (3x 0.5L).', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.shook_swarm.desc', 'en', 'Norwegian overwintering: Shake bees onto foundation, move honey supers. Comb renewal, fresh wax, reduced virus pressure. If no nectar flow, feed 1:1 syrup (3x 0.5L).', 'queen', datetime('now')),

-- Day 26
(lower(hex(randomblob(16))), 'queen.caging.insert_trap', 'sk', 'Vložiť varroa pascu (vajíčka)', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.insert_trap', 'en', 'Insert Varroa Trap (eggs)', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.insert_trap.desc', 'sk', 'Počas bezplodového obdobia vložiť rámik s vajíčkami. Foretické kliešte zlezú na rámik.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.insert_trap.desc', 'en', 'During broodless period insert frame with eggs. Phoretic mites will invade frame.', 'queen', datetime('now')),

-- Day 30
(lower(hex(randomblob(16))), 'queen.caging.oxalic_treatment', 'sk', 'Preliečenie kyselinou šťavelovou', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.oxalic_treatment', 'en', 'Oxalic Acid Treatment', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.oxalic_treatment.desc', 'sk', 'Preliečenie pokapom kyselinou šťavelovou. Možná aj fumigácia.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.oxalic_treatment.desc', 'en', 'Drip oxalic acid treatment. Fumigation also possible.', 'queen', datetime('now')),

-- Day 31 (action)
(lower(hex(randomblob(16))), 'queen.caging.release_queen', 'sk', 'Vypustenie matky z klietky', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.release_queen', 'en', 'Release Queen from Cage', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.release_queen.desc', 'sk', 'Matku vypustiť z klietky. Matka začne opäť klásť vajíčka.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.release_queen.desc', 'en', 'Release queen from cage. Queen will resume egg laying.', 'queen', datetime('now')),

-- Day 31 (info)
(lower(hex(randomblob(16))), 'queen.caging.laying_resumes', 'sk', 'Začiatok kládenia matky', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.laying_resumes', 'en', 'Queen Resumes Laying', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.laying_resumes.desc', 'sk', 'Matka začína klásť vajíčka po plodovej pauze.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.laying_resumes.desc', 'en', 'Queen resumes laying after brood break.', 'queen', datetime('now')),

-- Day 35
(lower(hex(randomblob(16))), 'queen.caging.remove_trap', 'sk', 'Odstrániť varroa pascu', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.remove_trap', 'en', 'Remove Varroa Trap', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.remove_trap.desc', 'sk', 'Odstrániť a zlikvidovať zavíčkovaný rámik s kliešťami. Kombinuje sa s presypaním na MS a preliečením.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.remove_trap.desc', 'en', 'Remove and destroy capped frame with mites. Combines with shook swarm and treatment.', 'queen', datetime('now')),

-- Day 42
(lower(hex(randomblob(16))), 'queen.caging.old_foragers_birth', 'sk', 'Posledné staré lietavky - narodenie', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.old_foragers_birth', 'en', 'Last Old Foragers - Birth', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.old_foragers_birth.desc', 'sk', 'Narodenie posledných starých lietavok (D+24+21 = D+45, ale Excel ukazuje D+42).', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.old_foragers_birth.desc', 'en', 'Birth of last old foragers (D+24+21 = D+45, but Excel shows D+42).', 'queen', datetime('now')),

-- Day 51
(lower(hex(randomblob(16))), 'queen.caging.first_new_brood', 'sk', 'Prvý plod po pauze', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.first_new_brood', 'en', 'First Brood After Break', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.first_new_brood.desc', 'sk', 'Prvý liahnuci sa plod po plodovej pauze (D+31+20).', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.first_new_brood.desc', 'en', 'First emerging brood after brood break (D+31+20).', 'queen', datetime('now')),

-- Day 72
(lower(hex(randomblob(16))), 'queen.caging.new_foragers_start', 'sk', 'Prvé nové lietavky', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.new_foragers_start', 'en', 'First New Foragers', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.new_foragers_start.desc', 'sk', 'Prvé lietavky z nového plodu začínajú lietať (D+51+21).', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.new_foragers_start.desc', 'en', 'First foragers from new brood start flying (D+51+21).', 'queen', datetime('now')),

-- Day 77
(lower(hex(randomblob(16))), 'queen.caging.old_foragers_death', 'sk', 'Posledné staré lietavky - úmrtie', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.old_foragers_death', 'en', 'Last Old Foragers - Death', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.old_foragers_death.desc', 'sk', 'Úmrtie posledných starých lietavok. Výrazné slabnutie rodiny.', 'queen', datetime('now')),
(lower(hex(randomblob(16))), 'queen.caging.old_foragers_death.desc', 'en', 'Death of last old foragers. Colony noticeably weaker.', 'queen', datetime('now'));
