-- Opravy a nové preklady pre HiveDialog
-- Dátum: 2026-02-17

INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES
-- Chýbajúci label pre typ úľa
(lower(hex(randomblob(16))), 'label.hive_type', 'sk', 'Typ úľa', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.hive_type', 'en', 'Hive Type', 'label', datetime('now')),

-- Nové polia ku matke
(lower(hex(randomblob(16))), 'label.aggression', 'sk', 'Agresivita včiel', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.aggression', 'en', 'Bee Aggression', 'label', datetime('now')),

(lower(hex(randomblob(16))), 'label.chalk_brood', 'sk', 'Vápenaťenie plodu', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.chalk_brood', 'en', 'Chalk Brood', 'label', datetime('now')),

(lower(hex(randomblob(16))), 'label.drone_cells', 'sk', 'Trudica', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.drone_cells', 'en', 'Drone Cells', 'label', datetime('now')),

(lower(hex(randomblob(16))), 'label.drone_laying_queen', 'sk', 'Trudokľadná matka', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.drone_laying_queen', 'en', 'Drone-Laying Queen', 'label', datetime('now')),

-- Aktualizované názvy tabov
(lower(hex(randomblob(16))), 'label.basic_info_equipment', 'sk', 'Základné & Vybavenie', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.basic_info_equipment', 'en', 'Basic & Equipment', 'label', datetime('now')),

(lower(hex(randomblob(16))), 'label.frames', 'sk', 'Rámiky', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.frames', 'en', 'Frames', 'label', datetime('now')),

-- Nové sekcie
(lower(hex(randomblob(16))), 'label.queen', 'sk', 'Matka', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.queen', 'en', 'Queen', 'label', datetime('now')),

(lower(hex(randomblob(16))), 'label.queen_info', 'sk', 'Informácie o matke', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.queen_info', 'en', 'Queen Information', 'label', datetime('now')),

(lower(hex(randomblob(16))), 'label.colony_health', 'sk', 'Zdravotný stav včelstva', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'label.colony_health', 'en', 'Colony Health', 'label', datetime('now')),

-- Aggression levels
(lower(hex(randomblob(16))), 'aggression.low', 'sk', 'Nízka', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'aggression.low', 'en', 'Low', 'label', datetime('now')),

(lower(hex(randomblob(16))), 'aggression.medium', 'sk', 'Stredná', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'aggression.medium', 'en', 'Medium', 'label', datetime('now')),

(lower(hex(randomblob(16))), 'aggression.high', 'sk', 'Vysoká', 'label', datetime('now')),
(lower(hex(randomblob(16))), 'aggression.high', 'en', 'High', 'label', datetime('now'));
