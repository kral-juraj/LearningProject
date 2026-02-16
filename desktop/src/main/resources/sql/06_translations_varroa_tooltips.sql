-- Varroa Settings Dialog - Tooltip translations
INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES

-- Reproduction section tooltips
(lower(hex(randomblob(16))), 'varroa.tooltip.worker_offspring', 'sk',
'Koľko životaschopných samičiek vznikne z jedného reprodukčného cyklu v robotničej bunke. Vyššia hodnota = rýchlejší rast populácie. Rozsah: 0-3, default: 1.3 (na základe výskumu)',
'varroa', datetime('now')),
(lower(hex(randomblob(16))), 'varroa.tooltip.worker_offspring', 'en',
'How many viable female offspring result from one reproduction cycle in a worker cell. Higher value = faster population growth. Range: 0-3, default: 1.3 (based on research)',
'varroa', datetime('now')),

(lower(hex(randomblob(16))), 'varroa.tooltip.drone_offspring', 'sk',
'Koľko životaschopných samičiek vznikne z jedného reprodukčného cyklu v trúdej bunke. Trúdie bunky umožňujú viac potomstva vďaka dlhšiemu času zapečatenia. Rozsah: 0-5, default: 2.4',
'varroa', datetime('now')),
(lower(hex(randomblob(16))), 'varroa.tooltip.drone_offspring', 'en',
'How many viable female offspring result from one reproduction cycle in a drone cell. Drone cells allow more offspring due to longer capping period. Range: 0-5, default: 2.4',
'varroa', datetime('now')),

(lower(hex(randomblob(16))), 'varroa.tooltip.worker_failure', 'sk',
'Percento reprodukčných cyklov, ktoré nezanechajú žiadne životaschopné potomstvo v robotničej bunke. Príčiny: neplodnosť, nedostatok času, úmrtie. Default: 25%',
'varroa', datetime('now')),
(lower(hex(randomblob(16))), 'varroa.tooltip.worker_failure', 'en',
'Percentage of reproduction cycles that leave no viable offspring in worker cells. Causes: infertility, insufficient time, death. Default: 25%',
'varroa', datetime('now')),

(lower(hex(randomblob(16))), 'varroa.tooltip.drone_failure', 'sk',
'Percento reprodukčných cyklov, ktoré nezanechajú žiadne životaschopné potomstvo v trúdej bunke. Nižšie než pri robotniciach vďaka dlhšiemu času. Default: 12.5%',
'varroa', datetime('now')),
(lower(hex(randomblob(16))), 'varroa.tooltip.drone_failure', 'en',
'Percentage of reproduction cycles that leave no viable offspring in drone cells. Lower than worker cells due to longer development time. Default: 12.5%',
'varroa', datetime('now')),

-- Life cycle section tooltips
(lower(hex(randomblob(16))), 'varroa.tooltip.worker_cycle', 'sk',
'Čas od vniknutia kliešťa do robotničej bunky po vylúčenie potomstva (zapečatenie až otvorenie). Robotničia bunka je zapečatená 12 dní. Default: 12 dní',
'varroa', datetime('now')),
(lower(hex(randomblob(16))), 'varroa.tooltip.worker_cycle', 'en',
'Time from mite invasion into worker cell until offspring emergence (capping to uncapping). Worker cell is capped for 12 days. Default: 12 days',
'varroa', datetime('now')),

(lower(hex(randomblob(16))), 'varroa.tooltip.drone_cycle', 'sk',
'Čas od vniknutia kliešťa do trúdej bunky po vylúčenie potomstva. Trúdia bunka je zapečatená dlhšie (14-15 dní), čo umožňuje viac potomstva. Default: 15 dní',
'varroa', datetime('now')),
(lower(hex(randomblob(16))), 'varroa.tooltip.drone_cycle', 'en',
'Time from mite invasion into drone cell until offspring emergence. Drone cell is capped longer (14-15 days), allowing more offspring. Default: 15 days',
'varroa', datetime('now')),

(lower(hex(randomblob(16))), 'varroa.tooltip.phoretic_phase', 'sk',
'Čas, ktorý kliešť strávi na dospelej včele medzi reprodukčnými cyklami. V tejto fáze sa kliešť živí hemolymfou a hľadá ďalšiu bunku. Default: 5.5 dní',
'varroa', datetime('now')),
(lower(hex(randomblob(16))), 'varroa.tooltip.phoretic_phase', 'en',
'Time the mite spends on adult bees between reproduction cycles. During this phase, mite feeds on hemolymph and searches for next cell. Default: 5.5 days',
'varroa', datetime('now')),

-- Behavior section tooltips
(lower(hex(randomblob(16))), 'varroa.tooltip.drone_preference', 'sk',
'O koľkokrát sú trúdie bunky atraktívnejšie pre kliešte než robotničie bunky. Výskum ukázal, že kliešte preferujú trúdie bunky 8-12x viac. Vyššia hodnota = väčší podiel kliešťov ide do trúdov. Default: 10x',
'varroa', datetime('now')),
(lower(hex(randomblob(16))), 'varroa.tooltip.drone_preference', 'en',
'How many times more attractive drone cells are to mites than worker cells. Research shows mites prefer drone cells 8-12x more. Higher value = more mites enter drone brood. Default: 10x',
'varroa', datetime('now')),

(lower(hex(randomblob(16))), 'varroa.tooltip.drone_brood_percent', 'sk',
'Aké percento celkového plodu tvorí trúdí plod. Bežne 10-15%, závisí od ročného obdobia a manažmentu včelára. Vyššie percento = rýchlejší rast populácie kliešťov. Default: 10%',
'varroa', datetime('now')),
(lower(hex(randomblob(16))), 'varroa.tooltip.drone_brood_percent', 'en',
'What percentage of total brood is drone brood. Typically 10-15%, depends on season and beekeeper management. Higher percentage = faster mite population growth. Default: 10%',
'varroa', datetime('now')),

(lower(hex(randomblob(16))), 'varroa.tooltip.cell_invasion', 'sk',
'Aké percento foretických kliešťov (na včelách) úspešne vnikne do buniek každý deň. Závisí od dostupnosti vhodného plodu a zdravia kolónie. Default: 20%',
'varroa', datetime('now')),
(lower(hex(randomblob(16))), 'varroa.tooltip.cell_invasion', 'en',
'What percentage of phoretic mites (on bees) successfully invade cells each day. Depends on availability of suitable brood and colony health. Default: 20%',
'varroa', datetime('now')),

-- Mortality section tooltips
(lower(hex(randomblob(16))), 'varroa.tooltip.mortality_per_cycle', 'sk',
'Percento kliešťov, ktoré zahynú počas kompletného reprodukčného cyklu (foretická fáza + rozmnožovanie). Zahŕňa úmrtnosť z dôvodu veku, chorôb a hygieny včiel. Default: 17.5%',
'varroa', datetime('now')),
(lower(hex(randomblob(16))), 'varroa.tooltip.mortality_per_cycle', 'en',
'Percentage of mites that die during complete reproduction cycle (phoretic phase + reproduction). Includes mortality from age, disease, and bee hygiene. Default: 17.5%',
'varroa', datetime('now')),

(lower(hex(randomblob(16))), 'varroa.tooltip.phoretic_mortality', 'sk',
'Denná úmrtnosť kliešťov, ktoré sa nachádzajú na dospelých včelách (foretická fáza). Vyššia v lete kvôli aktivite včiel a očisťovaniu. Default: 1% denne',
'varroa', datetime('now')),
(lower(hex(randomblob(16))), 'varroa.tooltip.phoretic_mortality', 'en',
'Daily mortality of mites on adult bees (phoretic phase). Higher in summer due to bee activity and grooming. Default: 1% per day',
'varroa', datetime('now'));
