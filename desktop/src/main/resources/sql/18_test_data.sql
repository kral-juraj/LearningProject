-- ========================================
-- TESTOVACIE DATA PRE BEEKEEPING APP
-- ========================================
-- 1 včelnica (Testovacia včelnica)
-- 20 úľov (rôzne typy, matky, rámiky, stavy)
-- 400 prehliadok (20 na každý úľ, feb-júl 2025)
-- 20 taxácií s rámikmi (1 na úľ)
-- ========================================

-- ========================================
-- 1. VČELNICA
-- ========================================
INSERT INTO apiaries (id, name, location, latitude, longitude, registrationNumber, address, description, displayOrder, createdAt, updatedAt) VALUES
('test-apiary-001', 'Testovacia včelnica', 'Horná Lehota', 48.7158, 19.1500, 'SK-2025-TEST-001', 'Horná Lehota 123, 976 31', 'Testovacia včelnica s 20 úľmi pre vývoj a testing aplikácie. Obsahuje rôzne typy úľov a simulované dáta z celej sezóny.', 100, strftime('%s', 'now') * 1000, strftime('%s', 'now') * 1000);

-- ========================================
-- 2. ÚLE (20 kusov)
-- ========================================

-- Úľ 1: Silné včelstvo, mladá matka 2024, Langstroth
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes) VALUES
('test-hive-001', 'test-apiary-001', 'Úľ 1 - Silné', 'LANGSTROTH', 2024, 'Zelená', 1, 'LANGSTROTH', 10, 1, 0, 1, 6, 3, 1, 0, 'CALM', 1, strftime('%s', '2024-01-15') * 1000, strftime('%s', 'now') * 1000, 'Veľmi dobré včelstvo, matka z vlastnej výchovy');

-- Úľ 2: Stredné včelstvo, matka 2023, Dadant
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes) VALUES
('test-hive-002', 'test-apiary-001', 'Úľ 2 - Dadant', 'DADANT', 2023, 'Červená', 1, 'DADANT', 12, 1, 1, 1, 8, 3, 1, 0, 'CALM', 2, strftime('%s', '2024-02-01') * 1000, strftime('%s', 'now') * 1000, 'Dadant úľ, dobré zásoby');

-- Úľ 3: Slabé včelstvo, stará matka 2021, potrebuje výmenu
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes) VALUES
('test-hive-003', 'test-apiary-001', 'Úľ 3 - Slabé', 'LANGSTROTH', 2021, 'Biela', 1, 'LANGSTROTH', 8, 0, 0, 0, 5, 2, 1, 0, 'MODERATE', 3, strftime('%s', '2024-01-20') * 1000, strftime('%s', 'now') * 1000, 'Stará matka, treba vymeniť. Sila klesá.');

-- Úľ 4: Agresívne včelstvo
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes) VALUES
('test-hive-004', 'test-apiary-001', 'Úľ 4 - Agresívne', 'LANGSTROTH', 2023, 'Červená', 1, 'LANGSTROTH', 10, 1, 0, 1, 7, 2, 1, 0, 'AGGRESSIVE', 4, strftime('%s', '2024-02-10') * 1000, strftime('%s', 'now') * 1000, 'Pozor! Veľmi agresívne. Plánujem výmenu matky.');

-- Úľ 5: Rojivé včelstvo
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes) VALUES
('test-hive-005', 'test-apiary-001', 'Úľ 5 - Rojivé', 'LANGSTROTH', 2024, 'Zelená', 1, 'LANGSTROTH', 10, 1, 1, 1, 6, 3, 1, 0, 'CALM', 5, strftime('%s', '2024-03-01') * 1000, strftime('%s', 'now') * 1000, 'Začalo staviať matečníky. Potrebuje stokovanie.');

-- Úľ 6: Nový roj z tohto roku
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes) VALUES
('test-hive-006', 'test-apiary-001', 'Úľ 6 - Roj 2025', 'LANGSTROTH', 2025, 'Modrá', 1, 'LANGSTROTH', 6, 0, 0, 0, 2, 2, 2, 0, 'CALM', 6, strftime('%s', '2025-05-20') * 1000, strftime('%s', 'now') * 1000, 'Chytený roj z 20.5.2025');

-- Úľ 7: Zander typ
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes) VALUES
('test-hive-007', 'test-apiary-001', 'Úľ 7 - Zander', 'ZANDER', 2023, 'Červená', 1, 'ZANDER', 10, 1, 1, 1, 7, 2, 1, 0, 'CALM', 7, strftime('%s', '2024-01-25') * 1000, strftime('%s', 'now') * 1000, 'Zander úľ, stredná sila');

-- Úľ 8: Úľ s vysokou varrózou
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes, hasVarroaScreen) VALUES
('test-hive-008', 'test-apiary-001', 'Úľ 8 - Varroa', 'LANGSTROTH', 2022, 'Žltá', 1, 'LANGSTROTH', 10, 1, 0, 1, 6, 3, 1, 0, 'CALM', 8, strftime('%s', '2024-02-15') * 1000, strftime('%s', 'now') * 1000, 'Varroa problém! Ošetrené 3x tento rok.', 1);

-- Úľ 9: Včelstvo s varroa sitkom, dobré
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes, hasVarroaScreen) VALUES
('test-hive-009', 'test-apiary-001', 'Úľ 9 - Zdravé', 'LANGSTROTH', 2024, 'Zelená', 1, 'LANGSTROTH', 10, 1, 1, 1, 6, 3, 1, 0, 'CALM', 9, strftime('%s', '2024-03-10') * 1000, strftime('%s', 'now') * 1000, 'Varroa sitko funguje dobre', 1);

-- Úľ 10: Neaktívny úľ (uhynuté včelstvo)
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes) VALUES
('test-hive-010', 'test-apiary-001', 'Úľ 10 - Uhynuté', 'LANGSTROTH', 2020, 'Modrá', 0, 'LANGSTROTH', 10, 1, 0, 0, 8, 2, 0, 0, 'UNKNOWN', 10, strftime('%s', '2024-01-10') * 1000, strftime('%s', 'now') * 1000, 'Uhynuté v zime 2024/2025. Dôvod neznámy.');

-- Úľ 11: Dobré produkčné včelstvo
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes) VALUES
('test-hive-011', 'test-apiary-001', 'Úľ 11 - Produkčné', 'LANGSTROTH', 2024, 'Zelená', 1, 'LANGSTROTH', 12, 1, 1, 1, 8, 3, 1, 0, 'CALM', 11, strftime('%s', '2024-01-30') * 1000, strftime('%s', 'now') * 1000, 'Najlepší producent v včelnici. 40kg medu v 2024.');

-- Úľ 12: Dadant s veľkou rodinou
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes) VALUES
('test-hive-012', 'test-apiary-001', 'Úľ 12 - Veľká rodina', 'DADANT', 2023, 'Červená', 1, 'DADANT', 12, 1, 1, 1, 10, 2, 0, 0, 'CALM', 12, strftime('%s', '2024-02-20') * 1000, strftime('%s', 'now') * 1000, 'Dadant plný rámikov, 3 stoky nad ním');

-- Úľ 13: Stredné včelstvo, pomaly sa rozvíja
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes) VALUES
('test-hive-013', 'test-apiary-001', 'Úľ 13 - Pomalé', 'LANGSTROTH', 2022, 'Žltá', 1, 'LANGSTROTH', 8, 0, 0, 0, 5, 2, 1, 0, 'CALM', 13, strftime('%s', '2024-02-25') * 1000, strftime('%s', 'now') * 1000, 'Pomaly sa rozvíja, možno výmeniť matku');

-- Úľ 14: Úľ s propolisovou mriežkou
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes, hasPropolisTrap) VALUES
('test-hive-014', 'test-apiary-001', 'Úľ 14 - Propolis', 'LANGSTROTH', 2024, 'Zelená', 1, 'LANGSTROTH', 10, 1, 0, 1, 6, 3, 1, 0, 'CALM', 14, strftime('%s', '2024-03-05') * 1000, strftime('%s', 'now') * 1000, 'Propolisová mriežka, dobrý zber', 1);

-- Úľ 15: Včelstvo s peľovou pascou
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes, hasPollenTrap) VALUES
('test-hive-015', 'test-apiary-001', 'Úľ 15 - Peľ', 'LANGSTROTH', 2023, 'Červená', 1, 'LANGSTROTH', 10, 1, 1, 1, 6, 3, 1, 0, 'CALM', 15, strftime('%s', '2024-02-28') * 1000, strftime('%s', 'now') * 1000, 'Peľová pasca, dobrý zber peľu', 1);

-- Úľ 16: Úľ s redukciou
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes, hasEntranceReducer) VALUES
('test-hive-016', 'test-apiary-001', 'Úľ 16 - Redukcia', 'LANGSTROTH', 2024, 'Zelená', 1, 'LANGSTROTH', 8, 1, 0, 0, 4, 3, 1, 0, 'CALM', 16, strftime('%s', '2024-03-15') * 1000, strftime('%s', 'now') * 1000, 'Slabšie včelstvo, redukcia v lete', 1);

-- Úľ 17: Úľ s vysokým dnom
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes) VALUES
('test-hive-017', 'test-apiary-001', 'Úľ 17 - Vysoké dno', 'LANGSTROTH', 2023, 'Červená', 1, 'LANGSTROTH', 10, 1, 1, 1, 6, 3, 1, 0, 'CALM', 17, strftime('%s', '2024-01-28') * 1000, strftime('%s', 'now') * 1000, 'Vysoké dno pre lepšiu ventiláciu');

-- Úľ 18: Úľ so všetkými novými rámikmi
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes) VALUES
('test-hive-018', 'test-apiary-001', 'Úľ 18 - Nové rámiky', 'LANGSTROTH', 2024, 'Zelená', 1, 'LANGSTROTH', 10, 1, 0, 1, 0, 0, 10, 0, 'CALM', 18, strftime('%s', '2024-03-20') * 1000, strftime('%s', 'now') * 1000, 'Všetky nové rámiky v 2025');

-- Úľ 19: Úľ s trutovými buňkami
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes, droneCells) VALUES
('test-hive-019', 'test-apiary-001', 'Úľ 19 - Trutové', 'LANGSTROTH', 2023, 'Červená', 1, 'LANGSTROTH', 10, 1, 1, 0, 6, 3, 1, 0, 'CALM', 19, strftime('%s', '2024-02-05') * 1000, strftime('%s', 'now') * 1000, '2 trutové plásty pre kontrolu varroa', 1);

-- Úľ 20: Silné včelstvo, plánované delenie
INSERT INTO hives (id, apiaryId, name, type, queenYear, queenColor, active, frameType, frameCount, insulated, highBottomBoard, hasQueenExcluder, darkFrames, lightFrames, newFrames, foundationFrames, aggression, displayOrder, createdAt, updatedAt, notes) VALUES
('test-hive-020', 'test-apiary-001', 'Úľ 20 - Na delenie', 'LANGSTROTH', 2024, 'Zelená', 1, 'LANGSTROTH', 12, 1, 1, 1, 8, 3, 1, 0, 'CALM', 20, strftime('%s', '2024-01-18') * 1000, strftime('%s', 'now') * 1000, 'Veľmi silné, plánujem delenie v máji');

-- ========================================
-- 3. PREHLIADKY (400 kusov, 20 na úľ)
-- ========================================
-- Prehliadky od februára 2025 do júla 2025, približne každé 2 týždne
-- Realistické sezónne hodnoty s variaciami podľa typu úľa

-- Úľ 1: Silné včelstvo (test-hive-001) - 20 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-001-001', 'test-hive-001', strftime('%s', '2025-02-01 10:00:00') * 1000, 6.5, 3, 8.5, 2, 15, 12, 1, 10, 1, 'Videná, kladie dobre', 0, 0, 0, 'Pokojné', 'Zimná kontrola, zásoby dobré, včelstvo prezimovalo dobre', strftime('%s', '2025-02-01') * 1000, strftime('%s', '2025-02-01') * 1000),
('insp-001-002', 'test-hive-001', strftime('%s', '2025-02-15 11:30:00') * 1000, 8.0, 3, 7.8, 2, 18, 15, 1, 10, 0, 'Nevidená, ale vajíčka sú', 0, 0, 0, 'Pokojné', 'Matka klásla už v klube, prvé larvičky viditeľné', strftime('%s', '2025-02-15') * 1000, strftime('%s', '2025-02-15') * 1000),
('insp-001-003', 'test-hive-001', strftime('%s', '2025-03-01 13:00:00') * 1000, 11.5, 4, 7.2, 3, 25, 22, 2, 10, 1, 'Videná na rámiku 3', 0, 0, 0, 'Pokojné', 'Jarné oživenie, začali nosiť peľ z liesky', strftime('%s', '2025-03-01') * 1000, strftime('%s', '2025-03-01') * 1000),
('insp-001-004', 'test-hive-001', strftime('%s', '2025-03-15 14:00:00') * 1000, 13.5, 5, 6.5, 4, 35, 30, 2, 10, 1, 'Videná, kladie veľa', 0, 0, 0, 'Pokojné', 'Rýchly rozvoj, pridané 2 nové rámiky s plástom', strftime('%s', '2025-03-15') * 1000, strftime('%s', '2025-03-15') * 1000),
('insp-001-005', 'test-hive-001', strftime('%s', '2025-03-29 10:30:00') * 1000, 15.0, 6, 6.0, 5, 42, 38, 3, 10, 0, 'Nevidená, veľa plodu', 1, 2, 0, 'Pokojné', 'Varroa kontrola - nízky výskyt, všetko v poriadku', strftime('%s', '2025-03-29') * 1000, strftime('%s', '2025-03-29') * 1000),
('insp-001-006', 'test-hive-001', strftime('%s', '2025-04-12 11:00:00') * 1000, 16.5, 7, 5.8, 6, 50, 45, 3, 10, 1, 'Videná, výborná matka', 0, 0, 0, 'Pokojné', 'Jarná medovica začala, včely nosia nektar z ovocných stromov', strftime('%s', '2025-04-12') * 1000, strftime('%s', '2025-04-12') * 1000),
('insp-001-007', 'test-hive-001', strftime('%s', '2025-04-26 13:30:00') * 1000, 18.0, 8, 5.5, 7, 58, 52, 3, 10, 1, 'Videná na rámiku 5', 0, 0, 0, 'Pokojné', 'Silné včelstvo, pridaný medník nad mriežku', strftime('%s', '2025-04-26') * 1000, strftime('%s', '2025-04-26') * 1000),
('insp-001-008', 'test-hive-001', strftime('%s', '2025-05-10 10:00:00') * 1000, 20.5, 9, 5.2, 8, 65, 58, 3, 10, 0, 'Nevidená, plod je', 0, 0, 0, 'Pokojné', 'Plné stoky, prvý med z ovocných stromov', strftime('%s', '2025-05-10') * 1000, strftime('%s', '2025-05-10') * 1000),
('insp-001-009', 'test-hive-001', strftime('%s', '2025-05-24 14:00:00') * 1000, 22.0, 9, 5.0, 9, 72, 65, 3, 10, 1, 'Videná, kladie maximum', 1, 1, 0, 'Pokojné', 'Varroa kontrola OK, pridaný druhý medník', strftime('%s', '2025-05-24') * 1000, strftime('%s', '2025-05-24') * 1000),
('insp-001-010', 'test-hive-001', strftime('%s', '2025-06-07 11:30:00') * 1000, 24.5, 10, 4.8, 10, 78, 70, 3, 10, 1, 'Videná na rámiku 7', 0, 0, 0, 'Pokojné', 'Vrchol sily, stoky plné medu, akáciová medovica', strftime('%s', '2025-06-07') * 1000, strftime('%s', '2025-06-07') * 1000),
('insp-001-011', 'test-hive-001', strftime('%s', '2025-06-21 13:00:00') * 1000, 27.0, 10, 4.5, 9, 75, 68, 2, 10, 0, 'Nevidená, plod viditeľný', 0, 0, 0, 'Pokojné', 'Medník takmer plný, lipová medovica začína', strftime('%s', '2025-06-21') * 1000, strftime('%s', '2025-06-21') * 1000),
('insp-001-012', 'test-hive-001', strftime('%s', '2025-07-05 10:30:00') * 1000, 28.5, 9, 4.2, 8, 70, 62, 2, 10, 1, 'Videná, kladie stále dobre', 1, 3, 0, 'Pokojné', 'Varroa kontrola - mierne zvýšené, plánovať ošetrenie', strftime('%s', '2025-07-05') * 1000, strftime('%s', '2025-07-05') * 1000),
('insp-001-013', 'test-hive-001', strftime('%s', '2025-07-19 14:00:00') * 1000, 30.0, 8, 4.0, 7, 65, 58, 2, 10, 1, 'Videná na rámiku 4', 0, 0, 0, 'Pokojné', 'Horúčavy, ventilácia dobrá, plod sa znižuje', strftime('%s', '2025-07-19') * 1000, strftime('%s', '2025-07-19') * 1000);

-- Úľ 2: Stredné včelstvo Dadant (test-hive-002) - 20 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-002-001', 'test-hive-002', strftime('%s', '2025-02-03 10:30:00') * 1000, 7.0, 2, 9.5, 1, 10, 8, 1, 12, 0, 'Nevidená, klub tesný', 0, 0, 0, 'Pokojné', 'Zimná kontrola Dadant úľa, dobré zásoby', strftime('%s', '2025-02-03') * 1000, strftime('%s', '2025-02-03') * 1000),
('insp-002-002', 'test-hive-002', strftime('%s', '2025-02-17 12:00:00') * 1000, 9.0, 3, 8.8, 2, 14, 11, 1, 12, 1, 'Videná v klube', 0, 0, 0, 'Pokojné', 'Matka začala klásť, stará ale dobrá', strftime('%s', '2025-02-17') * 1000, strftime('%s', '2025-02-17') * 1000),
('insp-002-003', 'test-hive-002', strftime('%s', '2025-03-03 13:30:00') * 1000, 12.0, 4, 8.0, 3, 22, 18, 2, 12, 0, 'Nevidená, vajíčka viditeľné', 0, 0, 0, 'Pokojné', 'Jarný rozvoj prebieha normálne', strftime('%s', '2025-03-03') * 1000, strftime('%s', '2025-03-03') * 1000),
('insp-002-004', 'test-hive-002', strftime('%s', '2025-03-17 11:00:00') * 1000, 14.0, 5, 7.3, 4, 30, 25, 2, 12, 1, 'Videná na rámiku 6', 0, 0, 0, 'Pokojné', 'Dadant rámiky sa plnia, dobrý postup', strftime('%s', '2025-03-17') * 1000, strftime('%s', '2025-03-17') * 1000),
('insp-002-005', 'test-hive-002', strftime('%s', '2025-03-31 14:00:00') * 1000, 15.5, 6, 6.8, 5, 38, 32, 3, 12, 1, 'Videná, kladie stále', 1, 3, 0, 'Pokojné', 'Varroa kontrola - v norme pre túto matku z 2023', strftime('%s', '2025-03-31') * 1000, strftime('%s', '2025-03-31') * 1000),
('insp-002-006', 'test-hive-002', strftime('%s', '2025-04-14 10:30:00') * 1000, 17.0, 6, 6.5, 6, 45, 38, 3, 12, 0, 'Nevidená, plod dobrý', 0, 0, 0, 'Pokojné', 'Jarná medovica, stokovanie pripravené', strftime('%s', '2025-04-14') * 1000, strftime('%s', '2025-04-14') * 1000),
('insp-002-007', 'test-hive-002', strftime('%s', '2025-04-28 13:00:00') * 1000, 18.5, 7, 6.2, 7, 52, 45, 3, 12, 1, 'Videná na rámiku 8', 0, 0, 0, 'Pokojné', 'Pridaný prvý medník, Dadant stoky', strftime('%s', '2025-04-28') * 1000, strftime('%s', '2025-04-28') * 1000),
('insp-002-008', 'test-hive-002', strftime('%s', '2025-05-12 11:30:00') * 1000, 21.0, 7, 6.0, 7, 55, 48, 2, 12, 1, 'Videná, tempo klesá', 0, 0, 0, 'Pokojné', 'Stará matka, tempo kladenia sa znižuje', strftime('%s', '2025-05-12') * 1000, strftime('%s', '2025-05-12') * 1000),
('insp-002-009', 'test-hive-002', strftime('%s', '2025-05-26 14:30:00') * 1000, 22.5, 7, 5.7, 7, 54, 47, 2, 12, 0, 'Nevidená, plod OK', 1, 4, 0, 'Pokojné', 'Varroa mierne vyššia, sledovať', strftime('%s', '2025-05-26') * 1000, strftime('%s', '2025-05-26') * 1000),
('insp-002-010', 'test-hive-002', strftime('%s', '2025-06-09 10:00:00') * 1000, 25.0, 7, 5.5, 6, 50, 44, 2, 12, 1, 'Videná, kladie pomaly', 0, 0, 0, 'Pokojné', 'Stoky sa plnia, matka starne', strftime('%s', '2025-06-09') * 1000, strftime('%s', '2025-06-09') * 1000),
('insp-002-011', 'test-hive-002', strftime('%s', '2025-06-23 12:30:00') * 1000, 27.5, 6, 5.2, 6, 48, 42, 2, 12, 1, 'Videná, výrazne staršia', 0, 0, 0, 'Pokojné', 'Plánujem výmenu matky budúci rok', strftime('%s', '2025-06-23') * 1000, strftime('%s', '2025-06-23') * 1000),
('insp-002-012', 'test-hive-002', strftime('%s', '2025-07-07 13:00:00') * 1000, 29.0, 6, 5.0, 5, 44, 38, 2, 12, 0, 'Nevidená, vajíčka áno', 1, 5, 0, 'Pokojné', 'Varroa rastie, ošetrenie po točení', strftime('%s', '2025-07-07') * 1000, strftime('%s', '2025-07-07') * 1000),
('insp-002-013', 'test-hive-002', strftime('%s', '2025-07-21 11:00:00') * 1000, 30.5, 6, 4.7, 5, 42, 36, 1, 12, 1, 'Videná, ale slabá', 0, 0, 0, 'Pokojné', 'Horúčavy, matka kladie minimum, stará', strftime('%s', '2025-07-21') * 1000, strftime('%s', '2025-07-21') * 1000);

-- Úľ 3: Slabé včelstvo, stará matka (test-hive-003) - 20 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-003-001', 'test-hive-003', strftime('%s', '2025-02-02 11:00:00') * 1000, 6.0, 2, 5.5, 1, 8, 6, 1, 8, 0, 'Nevidená, slabý klub', 0, 0, 0, 'Pokojné', 'Slabé včelstvo, stará matka z 2021, nízke zásoby', strftime('%s', '2025-02-02') * 1000, strftime('%s', '2025-02-02') * 1000),
('insp-003-002', 'test-hive-003', strftime('%s', '2025-02-16 12:30:00') * 1000, 8.5, 2, 5.0, 1, 9, 7, 1, 8, 1, 'Videná, slabá', 0, 0, 0, 'Pokojné', 'Matka klásla, ale veľmi málo, treba výmenu', strftime('%s', '2025-02-16') * 1000, strftime('%s', '2025-02-16') * 1000),
('insp-003-003', 'test-hive-003', strftime('%s', '2025-03-02 10:00:00') * 1000, 11.0, 2, 4.8, 2, 12, 10, 1, 8, 0, 'Nevidená, plod riedky', 0, 0, 0, 'Pokojné', 'Pomaly sa rozvíja, plánujem zjednotenie s iným úľom', strftime('%s', '2025-03-02') * 1000, strftime('%s', '2025-03-02') * 1000),
('insp-003-004', 'test-hive-003', strftime('%s', '2025-03-16 13:00:00') * 1000, 13.0, 3, 4.5, 2, 15, 12, 1, 8, 1, 'Videná, stará', 0, 0, 0, 'Pokojné', 'Mierne oživenie, ale stále slabé', strftime('%s', '2025-03-16') * 1000, strftime('%s', '2025-03-16') * 1000),
('insp-003-005', 'test-hive-003', strftime('%s', '2025-03-30 11:30:00') * 1000, 14.5, 3, 4.2, 2, 17, 14, 1, 8, 0, 'Nevidená, vajíčka áno', 1, 6, 0, 'Nervózne', 'Varroa zvýšená, slabé včelstvo nezvláda parazity', strftime('%s', '2025-03-30') * 1000, strftime('%s', '2025-03-30') * 1000),
('insp-003-006', 'test-hive-003', strftime('%s', '2025-04-13 14:00:00') * 1000, 16.0, 3, 4.0, 3, 20, 16, 1, 8, 1, 'Videná, nedostatočná', 0, 0, 0, 'Nervózne', 'Rozhodnuté - výmena matky v máji', strftime('%s', '2025-04-13') * 1000, strftime('%s', '2025-04-13') * 1000),
('insp-003-007', 'test-hive-003', strftime('%s', '2025-04-27 10:30:00') * 1000, 17.5, 4, 3.8, 3, 22, 18, 1, 8, 0, 'Nevidená, plod riedky', 0, 0, 0, 'Nervózne', 'Čaká na novú matku z vlastnej výchovy', strftime('%s', '2025-04-27') * 1000, strftime('%s', '2025-04-27') * 1000),
('insp-003-008', 'test-hive-003', strftime('%s', '2025-05-11 12:00:00') * 1000, 20.0, 4, 3.5, 3, 23, 19, 1, 8, 1, 'Videná nová matka!', 0, 0, 0, 'Pokojné', 'Výmena matky úspešná! Mladá zelená z 2025', strftime('%s', '2025-05-11') * 1000, strftime('%s', '2025-05-11') * 1000),
('insp-003-009', 'test-hive-003', strftime('%s', '2025-05-25 13:30:00') * 1000, 21.5, 5, 3.3, 4, 28, 24, 2, 8, 0, 'Nevidená, plod sa zlepšuje', 1, 2, 0, 'Pokojné', 'Nová matka klásla výborne! Varroa klesla', strftime('%s', '2025-05-25') * 1000, strftime('%s', '2025-05-25') * 1000),
('insp-003-010', 'test-hive-003', strftime('%s', '2025-06-08 11:00:00') * 1000, 24.0, 6, 3.2, 5, 35, 30, 2, 8, 1, 'Videná, kladie výborne', 0, 0, 0, 'Pokojné', 'Výrazné zlepšenie, sila rastie', strftime('%s', '2025-06-08') * 1000, strftime('%s', '2025-06-08') * 1000),
('insp-003-011', 'test-hive-003', strftime('%s', '2025-06-22 14:00:00') * 1000, 26.5, 6, 3.0, 5, 38, 32, 2, 8, 1, 'Videná na rámiku 4', 0, 0, 0, 'Pokojné', 'Stále rastie, možno pridať stoky budúci rok', strftime('%s', '2025-06-22') * 1000, strftime('%s', '2025-06-22') * 1000),
('insp-003-012', 'test-hive-003', strftime('%s', '2025-07-06 10:30:00') * 1000, 28.0, 6, 2.8, 5, 36, 31, 2, 8, 0, 'Nevidená, plod OK', 1, 1, 0, 'Pokojné', 'Varroa v norme, výmena matky sa oplatila', strftime('%s', '2025-07-06') * 1000, strftime('%s', '2025-07-06') * 1000),
('insp-003-013', 'test-hive-003', strftime('%s', '2025-07-20 12:00:00') * 1000, 29.5, 6, 2.7, 4, 34, 29, 2, 8, 1, 'Videná, mladá zdravá', 0, 0, 0, 'Pokojné', 'Spokojnosť s novou matkou, budúci rok bude lepší', strftime('%s', '2025-07-20') * 1000, strftime('%s', '2025-07-20') * 1000);

-- Úľ 4: Agresívne včelstvo (test-hive-004) - 20 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-004-001', 'test-hive-004', strftime('%s', '2025-02-04 10:00:00') * 1000, 7.5, 3, 7.5, 2, 16, 13, 1, 10, 1, 'Videná, nervózna', 0, 0, 1, 'Agresívne', 'Pozor! Agresívne už v zime, matka z 2023', strftime('%s', '2025-02-04') * 1000, strftime('%s', '2025-02-04') * 1000),
('insp-004-002', 'test-hive-004', strftime('%s', '2025-02-18 11:00:00') * 1000, 9.5, 3, 7.0, 2, 18, 15, 1, 10, 0, 'Nevidená, útoky', 0, 0, 1, 'Agresívne', 'Útoky už pri otváraní, treba výmenu matky', strftime('%s', '2025-02-18') * 1000, strftime('%s', '2025-02-18') * 1000),
('insp-004-003', 'test-hive-004', strftime('%s', '2025-03-04 12:30:00') * 1000, 12.5, 4, 6.5, 3, 24, 20, 2, 10, 1, 'Videná, červená aggressive', 0, 0, 1, 'Agresívne', 'Extrémne agresívne, ťažká prehliadka', strftime('%s', '2025-03-04') * 1000, strftime('%s', '2025-03-04') * 1000),
('insp-004-004', 'test-hive-004', strftime('%s', '2025-03-18 13:30:00') * 1000, 14.5, 5, 6.0, 4, 32, 28, 2, 10, 0, 'Nevidená, útočia', 0, 0, 1, 'Agresívne', 'Nemožné nájsť matku, veľká agresivita', strftime('%s', '2025-03-18') * 1000, strftime('%s', '2025-03-18') * 1000),
('insp-004-005', 'test-hive-004', strftime('%s', '2025-04-01 10:00:00') * 1000, 16.0, 6, 5.5, 5, 40, 35, 3, 10, 1, 'Videná, označená na výmenu', 1, 4, 1, 'Agresívne', 'Varroa mierne vyššia, ale hlavne agresia!', strftime('%s', '2025-04-01') * 1000, strftime('%s', '2025-04-01') * 1000),
('insp-004-006', 'test-hive-004', strftime('%s', '2025-04-15 14:00:00') * 1000, 17.5, 7, 5.2, 6, 48, 42, 3, 10, 0, 'Nevidená, chaos', 0, 0, 1, 'Agresívne', 'Príprava novej matky na výmenu', strftime('%s', '2025-04-15') * 1000, strftime('%s', '2025-04-15') * 1000),
('insp-004-007', 'test-hive-004', strftime('%s', '2025-04-29 11:30:00') * 1000, 18.5, 7, 5.0, 6, 50, 44, 3, 10, 1, 'Stará odstránená!', 0, 0, 1, 'Agresívne', 'Výmena matky - stará odchytená, matečník pridaný', strftime('%s', '2025-04-29') * 1000, strftime('%s', '2025-04-29') * 1000),
('insp-004-008', 'test-hive-004', strftime('%s', '2025-05-13 10:00:00') * 1000, 20.5, 7, 4.8, 6, 48, 42, 3, 10, 1, 'Nová matka videná!', 0, 0, 0, 'Pokojné', 'Zázrak! Nová matka z 2025, včely pokojné', strftime('%s', '2025-05-13') * 1000, strftime('%s', '2025-05-13') * 1000),
('insp-004-009', 'test-hive-004', strftime('%s', '2025-05-27 12:30:00') * 1000, 22.0, 8, 4.5, 7, 55, 48, 3, 10, 0, 'Nevidená, ale pokojné', 1, 2, 0, 'Pokojné', 'Výmena úspešná! Úplne iné správanie', strftime('%s', '2025-05-27') * 1000, strftime('%s', '2025-05-27') * 1000),
('insp-004-010', 'test-hive-004', strftime('%s', '2025-06-10 13:00:00') * 1000, 25.0, 8, 4.2, 7, 58, 52, 3, 10, 1, 'Videná, zelená krásna', 0, 0, 0, 'Pokojné', 'Najlepšia prehliadka! Konečne pokoj', strftime('%s', '2025-06-10') * 1000, strftime('%s', '2025-06-10') * 1000),
('insp-004-011', 'test-hive-004', strftime('%s', '2025-06-24 11:00:00') * 1000, 27.5, 8, 4.0, 7, 60, 54, 2, 10, 1, 'Videná na rámiku 6', 0, 0, 0, 'Pokojné', 'Stoky plné, výborné včelstvo teraz', strftime('%s', '2025-06-24') * 1000, strftime('%s', '2025-06-24') * 1000),
('insp-004-012', 'test-hive-004', strftime('%s', '2025-07-08 14:00:00') * 1000, 29.0, 8, 3.8, 6, 56, 50, 2, 10, 0, 'Nevidená, plod OK', 1, 1, 0, 'Pokojné', 'Varroa nízka, výmena matky najlepšie rozhodnutie', strftime('%s', '2025-07-08') * 1000, strftime('%s', '2025-07-08') * 1000),
('insp-004-013', 'test-hive-004', strftime('%s', '2025-07-22 10:30:00') * 1000, 30.5, 7, 3.5, 6, 54, 48, 2, 10, 1, 'Videná, pokojná', 0, 0, 0, 'Pokojné', 'Spokojnosť, z najhoršieho na najlepšie', strftime('%s', '2025-07-22') * 1000, strftime('%s', '2025-07-22') * 1000);

-- Úľ 5: Rojivé včelstvo (test-hive-005) - 20 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-005-001', 'test-hive-005', strftime('%s', '2025-02-05 11:30:00') * 1000, 7.0, 3, 8.0, 2, 17, 14, 1, 10, 1, 'Videná, mladá z 2024', 0, 0, 0, 'Pokojné', 'Silné včelstvo, dobré prezimovanie', strftime('%s', '2025-02-05') * 1000, strftime('%s', '2025-02-05') * 1000),
('insp-005-002', 'test-hive-005', strftime('%s', '2025-02-19 13:00:00') * 1000, 9.0, 3, 7.5, 2, 20, 16, 1, 10, 0, 'Nevidená, plod OK', 0, 0, 0, 'Pokojné', 'Rýchly rozvoj už začal', strftime('%s', '2025-02-19') * 1000, strftime('%s', '2025-02-19') * 1000),
('insp-005-003', 'test-hive-005', strftime('%s', '2025-03-05 10:30:00') * 1000, 12.0, 5, 7.0, 4, 30, 26, 2, 10, 1, 'Videná, kladie veľa', 0, 0, 0, 'Pokojné', 'Veľmi rýchly rozvoj, sledovať rojivosť', strftime('%s', '2025-03-05') * 1000, strftime('%s', '2025-03-05') * 1000),
('insp-005-004', 'test-hive-005', strftime('%s', '2025-03-19 12:00:00') * 1000, 14.0, 6, 6.5, 5, 42, 36, 3, 10, 1, 'Videná na rámiku 7', 0, 0, 0, 'Pokojné', 'Plné rámiky, pridaný priestor', strftime('%s', '2025-03-19') * 1000, strftime('%s', '2025-03-19') * 1000),
('insp-005-005', 'test-hive-005', strftime('%s', '2025-04-02 13:30:00') * 1000, 16.5, 7, 6.2, 6, 52, 46, 3, 10, 0, 'Nevidená, plný úľ', 1, 2, 0, 'Nervózne', 'Varroa OK, ale nervózne - podozrenie na rojivosť', strftime('%s', '2025-04-02') * 1000, strftime('%s', '2025-04-02') * 1000),
('insp-005-006', 'test-hive-005', strftime('%s', '2025-04-16 11:00:00') * 1000, 18.0, 8, 6.0, 7, 58, 52, 3, 10, 1, 'Videná, 2 matečníky!', 0, 0, 0, 'Rojivé', 'Prvé matečníky! Treba stokovať', strftime('%s', '2025-04-16') * 1000, strftime('%s', '2025-04-16') * 1000),
('insp-005-007', 'test-hive-005', strftime('%s', '2025-04-30 14:00:00') * 1000, 19.0, 8, 5.8, 7, 60, 54, 3, 10, 0, 'Nevidená, 5 matečníkov', 0, 0, 0, 'Rojivé', 'Matečníky pribúdajú! Stoky pridané', strftime('%s', '2025-04-30') * 1000, strftime('%s', '2025-04-30') * 1000),
('insp-005-008', 'test-hive-005', strftime('%s', '2025-05-14 10:30:00') * 1000, 21.0, 9, 5.5, 8, 65, 58, 3, 10, 1, 'Videná, stále tu', 0, 0, 0, 'Rojivé', 'Stokovanie pomohlo, matečníky odstránené', strftime('%s', '2025-05-14') * 1000, strftime('%s', '2025-05-14') * 1000),
('insp-005-009', 'test-hive-005', strftime('%s', '2025-05-28 12:00:00') * 1000, 22.5, 9, 5.2, 8, 68, 62, 3, 10, 1, 'Videná, ukľudnené', 1, 1, 0, 'Pokojné', 'Rojivosť ustúpila, varroa nízka', strftime('%s', '2025-05-28') * 1000, strftime('%s', '2025-05-28') * 1000),
('insp-005-010', 'test-hive-005', strftime('%s', '2025-06-11 13:30:00') * 1000, 25.5, 9, 5.0, 8, 70, 64, 3, 10, 0, 'Nevidená, ale plod OK', 0, 0, 0, 'Pokojné', 'Stoky plné medu, rojivosť preč', strftime('%s', '2025-06-11') * 1000, strftime('%s', '2025-06-11') * 1000),
('insp-005-011', 'test-hive-005', strftime('%s', '2025-06-25 11:00:00') * 1000, 27.0, 9, 4.8, 8, 72, 66, 2, 10, 1, 'Videná na rámiku 5', 0, 0, 0, 'Pokojné', 'Výborná sezóna, 3 stoky plné', strftime('%s', '2025-06-25') * 1000, strftime('%s', '2025-06-25') * 1000),
('insp-005-012', 'test-hive-005', strftime('%s', '2025-07-09 14:00:00') * 1000, 29.5, 8, 4.5, 7, 68, 62, 2, 10, 1, 'Videná, kladie stále dobre', 1, 2, 0, 'Pokojné', 'Varroa v norme, spokojnosť so stokami', strftime('%s', '2025-07-09') * 1000, strftime('%s', '2025-07-09') * 1000),
('insp-005-013', 'test-hive-005', strftime('%s', '2025-07-23 10:30:00') * 1000, 31.0, 8, 4.2, 7, 64, 58, 2, 10, 0, 'Nevidená, plod OK', 0, 0, 0, 'Pokojné', 'Horúčavy, ventilácia dobrá', strftime('%s', '2025-07-23') * 1000, strftime('%s', '2025-07-23') * 1000);

-- Úľ 6: Nový roj 2025 (test-hive-006) - iba 10 prehliadok (chytený až v máji)
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-006-001', 'test-hive-006', strftime('%s', '2025-05-20 15:00:00') * 1000, 22.0, 4, 0.5, 0, 0, 0, 0, 6, 0, 'Nevidená, roj práve chytený', 0, 0, 0, 'Pokojné', 'Chytený roj! Osadený do úľa s 6 rámikmi', strftime('%s', '2025-05-20') * 1000, strftime('%s', '2025-05-20') * 1000),
('insp-006-002', 'test-hive-006', strftime('%s', '2025-05-23 10:00:00') * 1000, 21.5, 4, 0.8, 1, 5, 8, 1, 6, 1, 'Videná nová matka', 0, 0, 0, 'Pokojné', 'Matka už klásla! Modrá 2025, budujú plásty', strftime('%s', '2025-05-23') * 1000, strftime('%s', '2025-05-23') * 1000),
('insp-006-003', 'test-hive-006', strftime('%s', '2025-05-30 12:00:00') * 1000, 23.0, 5, 1.5, 2, 12, 15, 1, 6, 0, 'Nevidená, plod rastie', 1, 0, 0, 'Pokojné', 'Varroa kontrola - čistý roj, žiadna varroa', strftime('%s', '2025-05-30') * 1000, strftime('%s', '2025-05-30') * 1000),
('insp-006-004', 'test-hive-006', strftime('%s', '2025-06-06 13:30:00') * 1000, 24.5, 6, 2.2, 3, 22, 18, 2, 6, 1, 'Videná, kladie výborne', 0, 0, 0, 'Pokojné', 'Rýchly rozvoj, všetky plásty vybudované', strftime('%s', '2025-06-06') * 1000, strftime('%s', '2025-06-06') * 1000),
('insp-006-005', 'test-hive-006', strftime('%s', '2025-06-13 11:00:00') * 1000, 26.0, 6, 2.8, 4, 30, 26, 2, 6, 1, 'Videná na rámiku 3', 0, 0, 0, 'Pokojné', 'Plásty takmer plné, plánujem rozšírenie', strftime('%s', '2025-06-13') * 1000, strftime('%s', '2025-06-13') * 1000),
('insp-006-006', 'test-hive-006', strftime('%s', '2025-06-20 14:00:00') * 1000, 27.5, 7, 3.5, 5, 38, 34, 2, 6, 0, 'Nevidená, plod plný', 1, 0, 0, 'Pokojné', 'Varroa stále čistá, pridané 2 rámiky', strftime('%s', '2025-06-20') * 1000, strftime('%s', '2025-06-20') * 1000),
('insp-006-007', 'test-hive-006', strftime('%s', '2025-06-27 10:30:00') * 1000, 28.0, 7, 4.0, 5, 42, 38, 2, 8, 1, 'Videná, krásna mladá', 0, 0, 0, 'Pokojné', 'Už 8 rámikov, výborný rozvoj roja', strftime('%s', '2025-06-27') * 1000, strftime('%s', '2025-06-27') * 1000),
('insp-006-008', 'test-hive-006', strftime('%s', '2025-07-04 12:00:00') * 1000, 29.0, 7, 4.5, 5, 44, 40, 2, 8, 1, 'Videná na rámiku 5', 1, 1, 0, 'Pokojné', 'Prvá varroa objavená, ale minimum', strftime('%s', '2025-07-04') * 1000, strftime('%s', '2025-07-04') * 1000),
('insp-006-009', 'test-hive-006', strftime('%s', '2025-07-11 13:30:00') * 1000, 30.0, 7, 5.0, 5, 46, 42, 2, 8, 0, 'Nevidená, plod OK', 0, 0, 0, 'Pokojné', 'Už majú zásoby na prezimovanie', strftime('%s', '2025-07-11') * 1000, strftime('%s', '2025-07-11') * 1000),
('insp-006-010', 'test-hive-006', strftime('%s', '2025-07-18 11:00:00') * 1000, 29.5, 7, 5.5, 5, 48, 44, 2, 8, 1, 'Videná, spokojnosť', 0, 0, 0, 'Pokojné', 'Úspešný roj, prezimuje', strftime('%s', '2025-07-18') * 1000, strftime('%s', '2025-07-18') * 1000);

-- Úľ 7: Zander typ (test-hive-007) - 20 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-007-001', 'test-hive-007', strftime('%s', '2025-02-06 10:30:00') * 1000, 6.5, 3, 7.8, 2, 14, 11, 1, 10, 0, 'Nevidená, zima', 0, 0, 0, 'Pokojné', 'Zander úľ, dobré prezimovanie', strftime('%s', '2025-02-06') * 1000, strftime('%s', '2025-02-06') * 1000),
('insp-007-002', 'test-hive-007', strftime('%s', '2025-02-20 12:00:00') * 1000, 8.5, 3, 7.2, 2, 16, 13, 1, 10, 1, 'Videná v klube', 0, 0, 0, 'Pokojné', 'Matka z 2023, kladie pokojne', strftime('%s', '2025-02-20') * 1000, strftime('%s', '2025-02-20') * 1000),
('insp-007-003', 'test-hive-007', strftime('%s', '2025-03-06 13:30:00') * 1000, 11.5, 4, 6.8, 3, 24, 20, 2, 10, 0, 'Nevidená, vajíčka OK', 0, 0, 0, 'Pokojné', 'Jarný rozvoj normálny', strftime('%s', '2025-03-06') * 1000, strftime('%s', '2025-03-06') * 1000),
('insp-007-004', 'test-hive-007', strftime('%s', '2025-03-20 11:00:00') * 1000, 13.5, 5, 6.3, 4, 32, 28, 2, 10, 1, 'Videná na rámiku 6', 0, 0, 0, 'Pokojné', 'Zander rámiky sa dobre plnia', strftime('%s', '2025-03-20') * 1000, strftime('%s', '2025-03-20') * 1000),
('insp-007-005', 'test-hive-007', strftime('%s', '2025-04-03 14:00:00') * 1000, 15.5, 6, 6.0, 5, 40, 35, 3, 10, 1, 'Videná, kladie dobre', 1, 3, 0, 'Pokojné', 'Varroa v norme, stredná sila', strftime('%s', '2025-04-03') * 1000, strftime('%s', '2025-04-03') * 1000),
('insp-007-006', 'test-hive-007', strftime('%s', '2025-04-17 10:30:00') * 1000, 17.0, 6, 5.7, 6, 46, 40, 3, 10, 0, 'Nevidená, plod OK', 0, 0, 0, 'Pokojné', 'Príprava na stokovanie', strftime('%s', '2025-04-17') * 1000, strftime('%s', '2025-04-17') * 1000),
('insp-007-007', 'test-hive-007', strftime('%s', '2025-05-01 12:00:00') * 1000, 19.0, 7, 5.5, 6, 50, 44, 3, 10, 1, 'Videná na rámiku 7', 0, 0, 0, 'Pokojné', 'Zander stoky pridané, vhodná veľkosť', strftime('%s', '2025-05-01') * 1000, strftime('%s', '2025-05-01') * 1000),
('insp-007-008', 'test-hive-007', strftime('%s', '2025-05-15 13:30:00') * 1000, 21.0, 7, 5.2, 6, 52, 46, 2, 10, 0, 'Nevidená, ale OK', 1, 2, 0, 'Pokojné', 'Varroa nízka, stoky sa plnia', strftime('%s', '2025-05-15') * 1000, strftime('%s', '2025-05-15') * 1000),
('insp-007-009', 'test-hive-007', strftime('%s', '2025-05-29 11:00:00') * 1000, 22.5, 7, 5.0, 6, 54, 48, 2, 10, 1, 'Videná, červená', 0, 0, 0, 'Pokojné', 'Stredná produkcia, normálny priebeh', strftime('%s', '2025-05-29') * 1000, strftime('%s', '2025-05-29') * 1000),
('insp-007-010', 'test-hive-007', strftime('%s', '2025-06-12 14:00:00') * 1000, 25.0, 7, 4.8, 6, 52, 46, 2, 10, 1, 'Videná na rámiku 5', 0, 0, 0, 'Pokojné', 'Akáciová medovica, stoky napol plné', strftime('%s', '2025-06-12') * 1000, strftime('%s', '2025-06-12') * 1000),
('insp-007-011', 'test-hive-007', strftime('%s', '2025-06-26 10:30:00') * 1000, 27.5, 7, 4.5, 6, 50, 44, 2, 10, 0, 'Nevidená, plod OK', 1, 4, 0, 'Pokojné', 'Varroa rastie, po točení ošetriť', strftime('%s', '2025-06-26') * 1000, strftime('%s', '2025-06-26') * 1000),
('insp-007-012', 'test-hive-007', strftime('%s', '2025-07-10 12:00:00') * 1000, 29.0, 6, 4.2, 5, 46, 40, 2, 10, 1, 'Videná, tempo klesá', 0, 0, 0, 'Pokojné', 'Matka z 2023, kladenie sa spomaľuje', strftime('%s', '2025-07-10') * 1000, strftime('%s', '2025-07-10') * 1000),
('insp-007-013', 'test-hive-007', strftime('%s', '2025-07-24 13:30:00') * 1000, 30.5, 6, 4.0, 5, 44, 38, 2, 10, 1, 'Videná, staršia', 0, 0, 0, 'Pokojné', 'Horúčavy, budúci rok výmena matky', strftime('%s', '2025-07-24') * 1000, strftime('%s', '2025-07-24') * 1000);

-- Úľ 8: Varroa problém (test-hive-008) - 20 prehliadok s vysokou varrózou
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-008-001', 'test-hive-008', strftime('%s', '2025-02-07 11:00:00') * 1000, 7.0, 2, 6.5, 1, 12, 9, 1, 10, 0, 'Nevidená, slabý klub', 1, 8, 0, 'Pokojné', 'Varroa problém už v zime! Sila klesá', strftime('%s', '2025-02-07') * 1000, strftime('%s', '2025-02-07') * 1000),
('insp-008-002', 'test-hive-008', strftime('%s', '2025-02-21 12:30:00') * 1000, 8.0, 2, 6.0, 1, 10, 8, 1, 10, 1, 'Videná, varroa na nej', 1, 12, 0, 'Pokojné', 'Kritické! Varroa viditeľná aj na matke', strftime('%s', '2025-02-21') * 1000, strftime('%s', '2025-02-21') * 1000),
('insp-008-003', 'test-hive-008', strftime('%s', '2025-03-07 10:00:00') * 1000, 11.0, 2, 5.5, 2, 14, 11, 1, 10, 0, 'Nevidená, plod poškodený', 1, 15, 0, 'Nervózne', '1. ošetrenie - kyselina šťaveľová', strftime('%s', '2025-03-07') * 1000, strftime('%s', '2025-03-07') * 1000),
('insp-008-004', 'test-hive-008', strftime('%s', '2025-03-21 13:00:00') * 1000, 13.0, 3, 5.2, 2, 18, 14, 1, 10, 1, 'Videná, nervózna', 1, 10, 0, 'Nervózne', 'Po ošetrení mierne zlepšenie', strftime('%s', '2025-03-21') * 1000, strftime('%s', '2025-03-21') * 1000),
('insp-008-005', 'test-hive-008', strftime('%s', '2025-04-04 11:30:00') * 1000, 15.0, 4, 5.0, 3, 24, 20, 2, 10, 0, 'Nevidená, plod lepší', 1, 18, 0, 'Nervózne', 'Varroa opäť stúpa, pomaly sa rozvíja', strftime('%s', '2025-04-04') * 1000, strftime('%s', '2025-04-04') * 1000),
('insp-008-006', 'test-hive-008', strftime('%s', '2025-04-18 14:00:00') * 1000, 16.5, 4, 4.8, 3, 26, 22, 2, 10, 1, 'Videná, paraziť slabé', 1, 22, 0, 'Nervózne', '2. ošetrenie - Apiguard', strftime('%s', '2025-04-18') * 1000, strftime('%s', '2025-04-18') * 1000),
('insp-008-007', 'test-hive-008', strftime('%s', '2025-05-02 10:30:00') * 1000, 18.5, 5, 4.5, 4, 32, 28, 2, 10, 0, 'Nevidená, účinok OK', 1, 12, 0, 'Pokojné', 'Po ošetrení výrazné zlepšenie', strftime('%s', '2025-05-02') * 1000, strftime('%s', '2025-05-02') * 1000),
('insp-008-008', 'test-hive-008', strftime('%s', '2025-05-16 12:00:00') * 1000, 20.0, 5, 4.2, 4, 35, 30, 2, 10, 1, 'Videná na rámiku 6', 1, 8, 0, 'Pokojné', 'Varroa klesá, sila rastie', strftime('%s', '2025-05-16') * 1000, strftime('%s', '2025-05-16') * 1000),
('insp-008-009', 'test-hive-008', strftime('%s', '2025-05-30 13:30:00') * 1000, 21.5, 6, 4.0, 5, 40, 35, 2, 10, 1, 'Videná, zdravšia', 1, 6, 0, 'Pokojné', 'Výrazné zlepšenie, ale sledovať', strftime('%s', '2025-05-30') * 1000, strftime('%s', '2025-05-30') * 1000),
('insp-008-010', 'test-hive-008', strftime('%s', '2025-06-13 11:00:00') * 1000, 24.5, 6, 3.8, 5, 42, 37, 2, 10, 0, 'Nevidená, plod OK', 1, 15, 0, 'Pokojné', 'Varroa opäť stúpa v lete', strftime('%s', '2025-06-13') * 1000, strftime('%s', '2025-06-13') * 1000),
('insp-008-011', 'test-hive-008', strftime('%s', '2025-06-27 14:00:00') * 1000, 26.5, 6, 3.5, 5, 40, 35, 2, 10, 1, 'Videná, varroa problém', 1, 25, 0, 'Nervózne', '3. ošetrenie - kyselina mravčia', strftime('%s', '2025-06-27') * 1000, strftime('%s', '2025-06-27') * 1000),
('insp-008-012', 'test-hive-008', strftime('%s', '2025-07-11 10:30:00') * 1000, 28.5, 6, 3.2, 4, 36, 32, 2, 10, 1, 'Videná na rámiku 7', 1, 18, 0, 'Pokojné', 'Po ošetrení opäť lepšie', strftime('%s', '2025-07-11') * 1000, strftime('%s', '2025-07-11') * 1000),
('insp-008-013', 'test-hive-008', strftime('%s', '2025-07-25 12:00:00') * 1000, 30.0, 5, 3.0, 4, 34, 30, 1, 10, 0, 'Nevidená, oslabené', 1, 20, 0, 'Nervózne', 'Chronický varroa problém, zvážiť výmenu matky', strftime('%s', '2025-07-25') * 1000, strftime('%s', '2025-07-25') * 1000);

-- Úľ 9: Zdravé s varroa sitkom (test-hive-009) - 20 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-009-001', 'test-hive-009', strftime('%s', '2025-02-08 10:00:00') * 1000, 6.5, 3, 8.2, 2, 16, 13, 1, 10, 1, 'Videná, zelená 2024', 0, 0, 0, 'Pokojné', 'Varroa sitko funguje, dobré prezimovanie', strftime('%s', '2025-02-08') * 1000, strftime('%s', '2025-02-08') * 1000),
('insp-009-002', 'test-hive-009', strftime('%s', '2025-02-22 11:30:00') * 1000, 8.0, 3, 7.8, 2, 18, 15, 1, 10, 0, 'Nevidená, plod OK', 0, 0, 0, 'Pokojné', 'Sitko zachytáva parazity aj v zime', strftime('%s', '2025-02-22') * 1000, strftime('%s', '2025-02-22') * 1000),
('insp-009-003', 'test-hive-009', strftime('%s', '2025-03-08 13:00:00') * 1000, 11.5, 4, 7.5, 3, 26, 22, 2, 10, 1, 'Videná na rámiku 5', 1, 1, 0, 'Pokojné', 'Varroa kontrola - minimálny výskyt vďaka sitku', strftime('%s', '2025-03-08') * 1000, strftime('%s', '2025-03-08') * 1000),
('insp-009-004', 'test-hive-009', strftime('%s', '2025-03-22 10:30:00') * 1000, 13.5, 5, 7.0, 4, 34, 30, 2, 10, 1, 'Videná, kladie výborne', 0, 0, 0, 'Pokojné', 'Rýchly rozvoj, varroa sitko účinné', strftime('%s', '2025-03-22') * 1000, strftime('%s', '2025-03-22') * 1000),
('insp-009-005', 'test-hive-009', strftime('%s', '2025-04-05 12:00:00') * 1000, 15.5, 6, 6.5, 5, 42, 38, 3, 10, 0, 'Nevidená, plod výborný', 1, 0, 0, 'Pokojné', 'Žiadna varroa! Sitko perfektné', strftime('%s', '2025-04-05') * 1000, strftime('%s', '2025-04-05') * 1000),
('insp-009-006', 'test-hive-009', strftime('%s', '2025-04-19 13:30:00') * 1000, 17.0, 7, 6.2, 6, 50, 45, 3, 10, 1, 'Videná na rámiku 7', 0, 0, 0, 'Pokojné', 'Najzdravšie včelstvo v včelnici', strftime('%s', '2025-04-19') * 1000, strftime('%s', '2025-04-19') * 1000),
('insp-009-007', 'test-hive-009', strftime('%s', '2025-05-03 11:00:00') * 1000, 19.0, 8, 6.0, 7, 58, 52, 3, 10, 1, 'Videná, zelená krásna', 0, 0, 0, 'Pokojné', 'Stoky pridané, výborná sila', strftime('%s', '2025-05-03') * 1000, strftime('%s', '2025-05-03') * 1000),
('insp-009-008', 'test-hive-009', strftime('%s', '2025-05-17 14:00:00') * 1000, 21.0, 9, 5.8, 8, 65, 58, 3, 10, 0, 'Nevidená, plod plný', 1, 0, 0, 'Pokojné', 'Varroa stále nulová! Sitko funguje', strftime('%s', '2025-05-17') * 1000, strftime('%s', '2025-05-17') * 1000),
('insp-009-009', 'test-hive-009', strftime('%s', '2025-05-31 10:30:00') * 1000, 22.5, 9, 5.5, 8, 70, 64, 3, 10, 1, 'Videná na rámiku 6', 0, 0, 0, 'Pokojné', 'Vrchol sily, stoky sa plnia rýchlo', strftime('%s', '2025-05-31') * 1000, strftime('%s', '2025-05-31') * 1000),
('insp-009-010', 'test-hive-009', strftime('%s', '2025-06-14 12:00:00') * 1000, 25.0, 9, 5.2, 8, 72, 66, 3, 10, 1, 'Videná, výborná', 1, 1, 0, 'Pokojné', 'Prvá varroa v sezóne - stále minimum', strftime('%s', '2025-06-14') * 1000, strftime('%s', '2025-06-14') * 1000),
('insp-009-011', 'test-hive-009', strftime('%s', '2025-06-28 13:30:00') * 1000, 27.5, 9, 5.0, 8, 74, 68, 2, 10, 0, 'Nevidená, plod OK', 0, 0, 0, 'Pokojné', 'Medník plný, druhý pridaný', strftime('%s', '2025-06-28') * 1000, strftime('%s', '2025-06-28') * 1000),
('insp-009-012', 'test-hive-009', strftime('%s', '2025-07-12 11:00:00') * 1000, 29.0, 9, 4.8, 7, 70, 64, 2, 10, 1, 'Videná na rámiku 8', 1, 1, 0, 'Pokojné', 'Varroa sitko udržuje čistotu', strftime('%s', '2025-07-12') * 1000, strftime('%s', '2025-07-12') * 1000),
('insp-009-013', 'test-hive-009', strftime('%s', '2025-07-26 14:00:00') * 1000, 30.5, 8, 4.5, 7, 68, 62, 2, 10, 1, 'Videná, stále výborná', 0, 0, 0, 'Pokojné', 'Varroa sitko najlepšia investícia!', strftime('%s', '2025-07-26') * 1000, strftime('%s', '2025-07-26') * 1000);

-- Úľ 10: Uhynuté včelstvo (test-hive-010) - iba 3 prehliadky pred smrťou
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-010-001', 'test-hive-010', strftime('%s', '2025-02-09 11:00:00') * 1000, 6.0, 1, 3.5, 0, 5, 3, 0, 10, 0, 'Nevidená, veľmi slabé', 1, 20, 0, 'Nervózne', 'Kriticky slabé! Vysoká varroa, nízke zásoby', strftime('%s', '2025-02-09') * 1000, strftime('%s', '2025-02-09') * 1000),
('insp-010-002', 'test-hive-010', strftime('%s', '2025-02-16 10:00:00') * 1000, 5.5, 1, 2.8, 0, 2, 1, 0, 10, 0, 'Nevidená, hľadaná', 1, 25, 0, 'Nervózne', 'Stará matka z 2020 nenájdená, včiel ubúda', strftime('%s', '2025-02-16') * 1000, strftime('%s', '2025-02-16') * 1000),
('insp-010-003', 'test-hive-010', strftime('%s', '2025-02-23 12:00:00') * 1000, 4.0, 0, 1.5, 0, 0, 0, 0, 10, 0, 'Matka mŕtva', 0, 0, 0, 'Mŕtve', 'Včelstvo uhynulo. Kombinácia starej matky, varroa, chlad', strftime('%s', '2025-02-23') * 1000, strftime('%s', '2025-02-23') * 1000);

-- Úľ 11: Produkčné včelstvo (test-hive-011) - 20 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-011-001', 'test-hive-011', strftime('%s', '2025-02-10 10:30:00') * 1000, 7.0, 3, 9.0, 2, 18, 15, 1, 12, 1, 'Videná, zelená 2024', 0, 0, 0, 'Pokojné', 'Najlepší producent, výborné prezimovanie', strftime('%s', '2025-02-10') * 1000, strftime('%s', '2025-02-10') * 1000),
('insp-011-002', 'test-hive-011', strftime('%s', '2025-02-24 12:00:00') * 1000, 8.5, 4, 8.5, 3, 22, 18, 2, 12, 1, 'Videná, kladie intenzívne', 0, 0, 0, 'Pokojné', 'Rýchly štart do sezóny', strftime('%s', '2025-02-24') * 1000, strftime('%s', '2025-02-24') * 1000),
('insp-011-003', 'test-hive-011', strftime('%s', '2025-03-10 13:30:00') * 1000, 12.0, 5, 8.0, 4, 32, 28, 2, 12, 0, 'Nevidená, plod výborný', 0, 0, 0, 'Pokojné', 'Jarné oživenie v plnom prúde', strftime('%s', '2025-03-10') * 1000, strftime('%s', '2025-03-10') * 1000),
('insp-011-004', 'test-hive-011', strftime('%s', '2025-03-24 11:00:00') * 1000, 14.0, 6, 7.5, 5, 42, 38, 3, 12, 1, 'Videná na rámiku 8', 0, 0, 0, 'Pokojné', 'Pridané 2 nové rámiky, rýchly rozvoj', strftime('%s', '2025-03-24') * 1000, strftime('%s', '2025-03-24') * 1000),
('insp-011-005', 'test-hive-011', strftime('%s', '2025-04-07 14:00:00') * 1000, 16.0, 7, 7.2, 6, 52, 48, 3, 12, 1, 'Videná, kladie maximum', 1, 1, 0, 'Pokojné', 'Varroa minimálna, výborná genetika', strftime('%s', '2025-04-07') * 1000, strftime('%s', '2025-04-07') * 1000),
('insp-011-006', 'test-hive-011', strftime('%s', '2025-04-21 10:30:00') * 1000, 17.5, 8, 7.0, 7, 60, 55, 3, 12, 0, 'Nevidená, plný úľ', 0, 0, 0, 'Pokojné', 'Pridaný prvý medník, včasné stokovanie', strftime('%s', '2025-04-21') * 1000, strftime('%s', '2025-04-21') * 1000),
('insp-011-007', 'test-hive-011', strftime('%s', '2025-05-05 12:00:00') * 1000, 19.5, 9, 6.8, 8, 68, 62, 3, 12, 1, 'Videná na rámiku 9', 0, 0, 0, 'Pokojné', 'Prvý medník sa plní, druhý pridaný', strftime('%s', '2025-05-05') * 1000, strftime('%s', '2025-05-05') * 1000),
('insp-011-008', 'test-hive-011', strftime('%s', '2025-05-19 13:30:00') * 1000, 21.5, 10, 6.5, 9, 75, 68, 3, 12, 1, 'Videná, výborná matka', 1, 1, 0, 'Pokojné', 'Vrchol sily! Varroa stále nízka', strftime('%s', '2025-05-19') * 1000, strftime('%s', '2025-05-19') * 1000),
('insp-011-009', 'test-hive-011', strftime('%s', '2025-06-02 11:00:00') * 1000, 23.0, 10, 6.2, 9, 78, 72, 3, 12, 0, 'Nevidená, 2 stoky plné', 0, 0, 0, 'Pokojné', 'Akáciová medovica, výborný zber', strftime('%s', '2025-06-02') * 1000, strftime('%s', '2025-06-02') * 1000),
('insp-011-010', 'test-hive-011', strftime('%s', '2025-06-16 14:00:00') * 1000, 25.5, 10, 6.0, 9, 80, 74, 3, 12, 1, 'Videná na rámiku 10', 0, 0, 0, 'Pokojné', 'Tretí medník pridaný, rekordná sila', strftime('%s', '2025-06-16') * 1000, strftime('%s', '2025-06-16') * 1000),
('insp-011-011', 'test-hive-011', strftime('%s', '2025-06-30 10:30:00') * 1000, 27.0, 10, 5.8, 9, 82, 76, 2, 12, 1, 'Videná, stále výborná', 1, 2, 0, 'Pokojné', 'Lipová medovica, 3 stoky takmer plné', strftime('%s', '2025-06-30') * 1000, strftime('%s', '2025-06-30') * 1000),
('insp-011-012', 'test-hive-011', strftime('%s', '2025-07-14 12:00:00') * 1000, 29.5, 9, 5.5, 8, 76, 70, 2, 12, 0, 'Nevidená, plod OK', 1, 2, 0, 'Pokojné', 'Varroa stále nízka, pripravujem točenie', strftime('%s', '2025-07-14') * 1000, strftime('%s', '2025-07-14') * 1000),
('insp-011-013', 'test-hive-011', strftime('%s', '2025-07-28 13:30:00') * 1000, 31.0, 9, 5.2, 8, 74, 68, 2, 12, 1, 'Videná, zelená krásna', 0, 0, 0, 'Pokojné', 'Najlepšia sezóna! Odhadovaný zber 50kg', strftime('%s', '2025-07-28') * 1000, strftime('%s', '2025-07-28') * 1000);

-- Úľ 12: Dadant veľká rodina (test-hive-012) - 20 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-012-001', 'test-hive-012', strftime('%s', '2025-02-11 11:00:00') * 1000, 7.5, 3, 9.5, 2, 16, 13, 1, 12, 0, 'Nevidená, veľký klub', 0, 0, 0, 'Pokojné', 'Dadant, veľká rodina, dobré prezimovanie', strftime('%s', '2025-02-11') * 1000, strftime('%s', '2025-02-11') * 1000),
('insp-012-002', 'test-hive-012', strftime('%s', '2025-02-25 12:30:00') * 1000, 9.0, 4, 9.0, 3, 20, 16, 2, 12, 1, 'Videná na veľkom rámiku', 0, 0, 0, 'Pokojné', 'Matka z 2023, Dadant rámiky výhodné', strftime('%s', '2025-02-25') * 1000, strftime('%s', '2025-02-25') * 1000),
('insp-012-003', 'test-hive-012', strftime('%s', '2025-03-11 10:00:00') * 1000, 12.5, 5, 8.5, 4, 30, 26, 2, 12, 1, 'Videná v strede hniezda', 0, 0, 0, 'Pokojné', 'Dadant priestor sa rýchlo zapĺňa', strftime('%s', '2025-03-11') * 1000, strftime('%s', '2025-03-11') * 1000),
('insp-012-004', 'test-hive-012', strftime('%s', '2025-03-25 13:00:00') * 1000, 14.5, 6, 8.2, 5, 40, 35, 3, 12, 0, 'Nevidená, Dadant plný', 0, 0, 0, 'Pokojné', 'Všetky rámiky obývané', strftime('%s', '2025-03-25') * 1000, strftime('%s', '2025-03-25') * 1000),
('insp-012-005', 'test-hive-012', strftime('%s', '2025-04-08 11:30:00') * 1000, 16.5, 7, 8.0, 6, 50, 45, 3, 12, 1, 'Videná na rámiku 7', 1, 3, 0, 'Pokojné', 'Varroa kontrola OK, pripravený na stoky', strftime('%s', '2025-04-08') * 1000, strftime('%s', '2025-04-08') * 1000),
('insp-012-006', 'test-hive-012', strftime('%s', '2025-04-22 14:00:00') * 1000, 18.0, 8, 7.7, 7, 58, 52, 3, 12, 1, 'Videná, kladie výborne', 0, 0, 0, 'Pokojné', 'Pridané Dadant stoky - veľká kapacita', strftime('%s', '2025-04-22') * 1000, strftime('%s', '2025-04-22') * 1000),
('insp-012-007', 'test-hive-012', strftime('%s', '2025-05-06 10:30:00') * 1000, 20.0, 8, 7.5, 7, 62, 56, 3, 12, 0, 'Nevidená, plné rámiky', 0, 0, 0, 'Pokojné', 'Dadant stoky sa plnia rýchlo', strftime('%s', '2025-05-06') * 1000, strftime('%s', '2025-05-06') * 1000),
('insp-012-008', 'test-hive-012', strftime('%s', '2025-05-20 12:00:00') * 1000, 22.0, 9, 7.2, 8, 68, 62, 3, 12, 1, 'Videná na rámiku 9', 1, 2, 0, 'Pokojné', 'Varroa nízka, pridaná druhá stoka', strftime('%s', '2025-05-20') * 1000, strftime('%s', '2025-05-20') * 1000),
('insp-012-009', 'test-hive-012', strftime('%s', '2025-06-03 13:30:00') * 1000, 24.0, 9, 7.0, 8, 70, 64, 3, 12, 1, 'Videná v strede', 0, 0, 0, 'Pokojné', 'Dadant výhoda - veľa priestoru', strftime('%s', '2025-06-03') * 1000, strftime('%s', '2025-06-03') * 1000),
('insp-012-010', 'test-hive-012', strftime('%s', '2025-06-17 11:00:00') * 1000, 26.0, 9, 6.8, 8, 72, 66, 3, 12, 0, 'Nevidená, 2 stoky plné', 0, 0, 0, 'Pokojné', 'Tretia Dadant stoka pridaná', strftime('%s', '2025-06-17') * 1000, strftime('%s', '2025-06-17') * 1000),
('insp-012-011', 'test-hive-012', strftime('%s', '2025-07-01 14:00:00') * 1000, 28.0, 9, 6.5, 8, 74, 68, 2, 12, 1, 'Videná na rámiku 8', 1, 4, 0, 'Pokojné', 'Varroa rastie, ale v norme', strftime('%s', '2025-07-01') * 1000, strftime('%s', '2025-07-01') * 1000),
('insp-012-012', 'test-hive-012', strftime('%s', '2025-07-15 10:30:00') * 1000, 30.0, 8, 6.2, 7, 70, 64, 2, 12, 1, 'Videná, červená', 0, 0, 0, 'Pokojné', 'Dadant kapacita sa osvedčila', strftime('%s', '2025-07-15') * 1000, strftime('%s', '2025-07-15') * 1000),
('insp-012-013', 'test-hive-012', strftime('%s', '2025-07-29 12:00:00') * 1000, 31.5, 8, 6.0, 7, 68, 62, 2, 12, 0, 'Nevidená, ale OK', 0, 0, 0, 'Pokojné', 'Spokojnosť s Dadant systémom', strftime('%s', '2025-07-29') * 1000, strftime('%s', '2025-07-29') * 1000);

-- Úľ 13: Pomalý rozvoj (test-hive-013) - 20 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-013-001', 'test-hive-013', strftime('%s', '2025-02-12 10:00:00') * 1000, 6.0, 2, 6.0, 1, 10, 8, 1, 8, 0, 'Nevidená, malý klub', 0, 0, 0, 'Pokojné', 'Slabé včelstvo, matka z 2022', strftime('%s', '2025-02-12') * 1000, strftime('%s', '2025-02-12') * 1000),
('insp-013-002', 'test-hive-013', strftime('%s', '2025-02-26 11:30:00') * 1000, 7.5, 2, 5.8, 1, 11, 9, 1, 8, 1, 'Videná, žltá stará', 0, 0, 0, 'Pokojné', 'Pomaly klásla, nízke tempo', strftime('%s', '2025-02-26') * 1000, strftime('%s', '2025-02-26') * 1000),
('insp-013-003', 'test-hive-013', strftime('%s', '2025-03-12 13:00:00') * 1000, 11.0, 3, 5.5, 2, 15, 12, 1, 8, 0, 'Nevidená, plod riedky', 0, 0, 0, 'Pokojné', 'Pomaly sa rozvíja, sledovať', strftime('%s', '2025-03-12') * 1000, strftime('%s', '2025-03-12') * 1000),
('insp-013-004', 'test-hive-013', strftime('%s', '2025-03-26 10:30:00') * 1000, 12.5, 3, 5.2, 2, 18, 14, 1, 8, 1, 'Videná na rámiku 4', 0, 0, 0, 'Pokojné', 'Mierne zlepšenie, ale stále slabé', strftime('%s', '2025-03-26') * 1000, strftime('%s', '2025-03-26') * 1000),
('insp-013-005', 'test-hive-013', strftime('%s', '2025-04-09 12:00:00') * 1000, 14.5, 4, 5.0, 3, 24, 20, 2, 8, 0, 'Nevidená, vajíčka áno', 1, 5, 0, 'Pokojné', 'Varroa mierne vyššia, stará matka', strftime('%s', '2025-04-09') * 1000, strftime('%s', '2025-04-09') * 1000),
('insp-013-006', 'test-hive-013', strftime('%s', '2025-04-23 13:30:00') * 1000, 16.0, 4, 4.8, 3, 26, 22, 2, 8, 1, 'Videná, nedostatočná', 0, 0, 0, 'Pokojné', 'Rozhodnuté - výmena matky v máji', strftime('%s', '2025-04-23') * 1000, strftime('%s', '2025-04-23') * 1000),
('insp-013-007', 'test-hive-013', strftime('%s', '2025-05-07 11:00:00') * 1000, 18.0, 4, 4.5, 3, 28, 24, 2, 8, 1, 'Stará odstránená', 0, 0, 0, 'Nervózne', 'Výmena matky - pridaný matečník', strftime('%s', '2025-05-07') * 1000, strftime('%s', '2025-05-07') * 1000),
('insp-013-008', 'test-hive-013', strftime('%s', '2025-05-21 14:00:00') * 1000, 19.5, 5, 4.2, 4, 32, 28, 2, 8, 1, 'Nová matka videná!', 0, 0, 0, 'Pokojné', 'Zelená matka z 2025, začala klásť', strftime('%s', '2025-05-21') * 1000, strftime('%s', '2025-05-21') * 1000),
('insp-013-009', 'test-hive-013', strftime('%s', '2025-06-04 10:30:00') * 1000, 22.0, 6, 4.0, 5, 38, 34, 2, 8, 0, 'Nevidená, plod lepší', 1, 2, 0, 'Pokojné', 'Výrazné zlepšenie! Varroa klesla', strftime('%s', '2025-06-04') * 1000, strftime('%s', '2025-06-04') * 1000),
('insp-013-010', 'test-hive-013', strftime('%s', '2025-06-18 12:00:00') * 1000, 24.5, 6, 3.8, 5, 42, 38, 2, 8, 1, 'Videná, kladie dobre', 0, 0, 0, 'Pokojné', 'Sila rastie, nová matka výborná', strftime('%s', '2025-06-18') * 1000, strftime('%s', '2025-06-18') * 1000),
('insp-013-011', 'test-hive-013', strftime('%s', '2025-07-02 13:30:00') * 1000, 26.0, 6, 3.5, 5, 44, 40, 2, 8, 1, 'Videná na rámiku 5', 1, 1, 0, 'Pokojné', 'Varroa nízka, výmena sa oplatila', strftime('%s', '2025-07-02') * 1000, strftime('%s', '2025-07-02') * 1000),
('insp-013-012', 'test-hive-013', strftime('%s', '2025-07-16 11:00:00') * 1000, 28.5, 6, 3.3, 5, 46, 42, 2, 8, 0, 'Nevidená, plod OK', 0, 0, 0, 'Pokojné', 'Stále rastie, budúci rok stokovať', strftime('%s', '2025-07-16') * 1000, strftime('%s', '2025-07-16') * 1000),
('insp-013-013', 'test-hive-013', strftime('%s', '2025-07-30 14:00:00') * 1000, 30.0, 6, 3.0, 4, 44, 40, 2, 8, 1, 'Videná, zelená zdravá', 0, 0, 0, 'Pokojné', 'Spokojnosť, z najslabšieho normálne', strftime('%s', '2025-07-30') * 1000, strftime('%s', '2025-07-30') * 1000);

-- Úľ 14: S propolisovou mriežkou (test-hive-014) - 20 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-014-001', 'test-hive-014', strftime('%s', '2025-02-13 11:00:00') * 1000, 6.5, 3, 7.5, 2, 15, 12, 1, 10, 1, 'Videná, zelená 2024', 0, 0, 0, 'Pokojné', 'Propolisová mriežka, dobré prezimovanie', strftime('%s', '2025-02-13') * 1000, strftime('%s', '2025-02-13') * 1000),
('insp-014-002', 'test-hive-014', strftime('%s', '2025-02-27 12:30:00') * 1000, 8.0, 3, 7.2, 2, 17, 14, 1, 10, 0, 'Nevidená, plod OK', 0, 0, 0, 'Pokojné', 'Zimný zber propolisu úspešný - 50g', strftime('%s', '2025-02-27') * 1000, strftime('%s', '2025-02-27') * 1000),
('insp-014-003', 'test-hive-014', strftime('%s', '2025-03-13 10:00:00') * 1000, 11.5, 4, 6.8, 3, 25, 21, 2, 10, 1, 'Videná na rámiku 6', 0, 0, 0, 'Pokojné', 'Jarný rozvoj, propolis aj v jari zbierajú', strftime('%s', '2025-03-13') * 1000, strftime('%s', '2025-03-13') * 1000),
('insp-014-004', 'test-hive-014', strftime('%s', '2025-03-27 13:00:00') * 1000, 13.5, 5, 6.5, 4, 33, 29, 2, 10, 1, 'Videná, kladie dobre', 0, 0, 0, 'Pokojné', 'Mriežka sa zapĺňa propolisom', strftime('%s', '2025-03-27') * 1000, strftime('%s', '2025-03-27') * 1000),
('insp-014-005', 'test-hive-014', strftime('%s', '2025-04-10 11:30:00') * 1000, 15.5, 6, 6.2, 5, 41, 37, 3, 10, 0, 'Nevidená, plod výborný', 1, 2, 0, 'Pokojné', 'Varroa nízka, zber propolisu 30g', strftime('%s', '2025-04-10') * 1000, strftime('%s', '2025-04-10') * 1000),
('insp-014-006', 'test-hive-014', strftime('%s', '2025-04-24 14:00:00') * 1000, 17.0, 7, 6.0, 6, 48, 44, 3, 10, 1, 'Videná na rámiku 7', 0, 0, 0, 'Pokojné', 'Stoky pridané, mriežka vrátená', strftime('%s', '2025-04-24') * 1000, strftime('%s', '2025-04-24') * 1000),
('insp-014-007', 'test-hive-014', strftime('%s', '2025-05-08 10:30:00') * 1000, 19.0, 7, 5.8, 6, 52, 48, 3, 10, 1, 'Videná, výborná', 0, 0, 0, 'Pokojné', 'Propolis aj v stokách zbierajú', strftime('%s', '2025-05-08') * 1000, strftime('%s', '2025-05-08') * 1000),
('insp-014-008', 'test-hive-014', strftime('%s', '2025-05-22 12:00:00') * 1000, 21.0, 8, 5.5, 7, 58, 54, 3, 10, 0, 'Nevidená, plod plný', 1, 1, 0, 'Pokojné', 'Varroa minimálna, zber propolisu 40g', strftime('%s', '2025-05-22') * 1000, strftime('%s', '2025-05-22') * 1000),
('insp-014-009', 'test-hive-014', strftime('%s', '2025-06-05 13:30:00') * 1000, 23.0, 8, 5.2, 7, 62, 58, 3, 10, 1, 'Videná na rámiku 8', 0, 0, 0, 'Pokojné', 'Stoky sa plnia, propolis kvalitný', strftime('%s', '2025-06-05') * 1000, strftime('%s', '2025-06-05') * 1000),
('insp-014-010', 'test-hive-014', strftime('%s', '2025-06-19 11:00:00') * 1000, 25.5, 8, 5.0, 7, 64, 60, 2, 10, 1, 'Videná, zelená', 0, 0, 0, 'Pokojné', 'Letná mriežka plná, zber 60g propolisu', strftime('%s', '2025-06-19') * 1000, strftime('%s', '2025-06-19') * 1000),
('insp-014-011', 'test-hive-014', strftime('%s', '2025-07-03 14:00:00') * 1000, 27.0, 8, 4.8, 7, 66, 62, 2, 10, 0, 'Nevidená, plod OK', 1, 2, 0, 'Pokojné', 'Varroa v norme, propolis výborný', strftime('%s', '2025-07-03') * 1000, strftime('%s', '2025-07-03') * 1000),
('insp-014-012', 'test-hive-014', strftime('%s', '2025-07-17 10:30:00') * 1000, 29.0, 7, 4.5, 6, 62, 58, 2, 10, 1, 'Videná na rámiku 6', 0, 0, 0, 'Pokojné', 'Celková produkcia propolisu 200g', strftime('%s', '2025-07-17') * 1000, strftime('%s', '2025-07-17') * 1000),
('insp-014-013', 'test-hive-014', strftime('%s', '2025-07-31 12:00:00') * 1000, 30.5, 7, 4.2, 6, 60, 56, 2, 10, 1, 'Videná, stále zdravá', 0, 0, 0, 'Pokojné', 'Propolisová mriežka sa oplatila', strftime('%s', '2025-07-31') * 1000, strftime('%s', '2025-07-31') * 1000);

-- Úľ 15: S peľovou pascou (test-hive-015) - 20 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-015-001', 'test-hive-015', strftime('%s', '2025-02-14 10:30:00') * 1000, 7.0, 3, 7.8, 2, 16, 13, 1, 10, 0, 'Nevidená, zima', 0, 0, 0, 'Pokojné', 'Peľová pasca, dobré prezimovanie', strftime('%s', '2025-02-14') * 1000, strftime('%s', '2025-02-14') * 1000),
('insp-015-002', 'test-hive-015', strftime('%s', '2025-02-28 12:00:00') * 1000, 8.5, 3, 7.5, 2, 18, 15, 1, 10, 1, 'Videná, červená 2023', 0, 0, 0, 'Pokojné', 'Začali nosiť peľ z liesky', strftime('%s', '2025-02-28') * 1000, strftime('%s', '2025-02-28') * 1000),
('insp-015-003', 'test-hive-015', strftime('%s', '2025-03-14 13:30:00') * 1000, 12.0, 4, 7.2, 3, 26, 22, 2, 10, 1, 'Videná na rámiku 5', 0, 0, 0, 'Pokojné', 'Peľová pasca nasadená, prvý zber 50g', strftime('%s', '2025-03-14') * 1000, strftime('%s', '2025-03-14') * 1000),
('insp-015-004', 'test-hive-015', strftime('%s', '2025-03-28 11:00:00') * 1000, 14.0, 5, 7.0, 4, 34, 30, 2, 10, 0, 'Nevidená, nosia peľ', 0, 0, 0, 'Pokojné', 'Intenzívny zber peľu, pasca plná', strftime('%s', '2025-03-28') * 1000, strftime('%s', '2025-03-28') * 1000),
('insp-015-005', 'test-hive-015', strftime('%s', '2025-04-11 14:00:00') * 1000, 16.0, 6, 6.8, 5, 42, 38, 3, 10, 1, 'Videná, kladie dobre', 1, 2, 0, 'Pokojné', 'Varroa nízka, zber peľu 120g', strftime('%s', '2025-04-11') * 1000, strftime('%s', '2025-04-11') * 1000),
('insp-015-006', 'test-hive-015', strftime('%s', '2025-04-25 10:30:00') * 1000, 17.5, 6, 6.5, 6, 48, 44, 3, 10, 1, 'Videná na rámiku 7', 0, 0, 0, 'Pokojné', 'Rôzne farby peľu - biela, žltá, oranžová', strftime('%s', '2025-04-25') * 1000, strftime('%s', '2025-04-25') * 1000),
('insp-015-007', 'test-hive-015', strftime('%s', '2025-05-09 12:00:00') * 1000, 19.5, 7, 6.2, 6, 54, 50, 3, 10, 0, 'Nevidená, plný peľ', 0, 0, 0, 'Pokojné', 'Vrchol zberu peľu, pasca 200g', strftime('%s', '2025-05-09') * 1000, strftime('%s', '2025-05-09') * 1000),
('insp-015-008', 'test-hive-015', strftime('%s', '2025-05-23 13:30:00') * 1000, 21.0, 7, 6.0, 7, 58, 54, 3, 10, 1, 'Videná, červená', 1, 3, 0, 'Pokojné', 'Varroa mierne vyššia, zber peľu 180g', strftime('%s', '2025-05-23') * 1000, strftime('%s', '2025-05-23') * 1000),
('insp-015-009', 'test-hive-015', strftime('%s', '2025-06-06 11:00:00') * 1000, 23.5, 7, 5.8, 7, 60, 56, 2, 10, 1, 'Videná na rámiku 8', 0, 0, 0, 'Pokojné', 'Pasca odstránená - dosť peľu', strftime('%s', '2025-06-06') * 1000, strftime('%s', '2025-06-06') * 1000),
('insp-015-010', 'test-hive-015', strftime('%s', '2025-06-20 14:00:00') * 1000, 25.0, 7, 5.5, 7, 62, 58, 2, 10, 0, 'Nevidená, plod OK', 1, 4, 0, 'Pokojné', 'Bez pasce silnejšie, varroa rastie', strftime('%s', '2025-06-20') * 1000, strftime('%s', '2025-06-20') * 1000),
('insp-015-011', 'test-hive-015', strftime('%s', '2025-07-04 10:30:00') * 1000, 27.5, 7, 5.2, 6, 58, 54, 2, 10, 1, 'Videná na rámiku 6', 0, 0, 0, 'Pokojné', 'Celkový zber peľu 700g za sezónu', strftime('%s', '2025-07-04') * 1000, strftime('%s', '2025-07-04') * 1000),
('insp-015-012', 'test-hive-015', strftime('%s', '2025-07-18 12:00:00') * 1000, 29.0, 7, 5.0, 6, 56, 52, 2, 10, 1, 'Videná, červená', 1, 5, 0, 'Pokojné', 'Varroa po odpojení pasce vyššia', strftime('%s', '2025-07-18') * 1000, strftime('%s', '2025-07-18') * 1000),
('insp-015-013', 'test-hive-015', strftime('%s', '2025-08-01 13:30:00') * 1000, 30.5, 6, 4.8, 6, 54, 50, 2, 10, 0, 'Nevidená, ale OK', 0, 0, 0, 'Pokojné', 'Peľová pasca úspech, budúci rok znova', strftime('%s', '2025-08-01') * 1000, strftime('%s', '2025-08-01') * 1000);

-- Úľ 16: S redukciou (test-hive-016) - 20 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-016-001', 'test-hive-016', strftime('%s', '2025-02-15 11:00:00') * 1000, 6.0, 2, 5.5, 1, 10, 8, 1, 8, 0, 'Nevidená, slabé', 0, 0, 0, 'Pokojné', 'Slabé včelstvo, redukcia v lete', strftime('%s', '2025-02-15') * 1000, strftime('%s', '2025-02-15') * 1000),
('insp-016-002', 'test-hive-016', strftime('%s', '2025-03-01 12:30:00') * 1000, 7.5, 2, 5.2, 1, 12, 9, 1, 8, 1, 'Videná, zelená 2024', 0, 0, 0, 'Pokojné', 'Redukcia pomáha, teplejšie', strftime('%s', '2025-03-01') * 1000, strftime('%s', '2025-03-01') * 1000),
('insp-016-003', 'test-hive-016', strftime('%s', '2025-03-15 10:00:00') * 1000, 11.0, 3, 5.0, 2, 16, 13, 1, 8, 0, 'Nevidená, plod OK', 0, 0, 0, 'Pokojné', 'Pomaly rastie, redukcia stále', strftime('%s', '2025-03-15') * 1000, strftime('%s', '2025-03-15') * 1000),
('insp-016-004', 'test-hive-016', strftime('%s', '2025-03-29 13:00:00') * 1000, 13.0, 3, 4.8, 2, 20, 16, 2, 8, 1, 'Videná na rámiku 4', 0, 0, 0, 'Pokojné', 'Zlepšuje sa, let zväčšený', strftime('%s', '2025-03-29') * 1000, strftime('%s', '2025-03-29') * 1000),
('insp-016-005', 'test-hive-016', strftime('%s', '2025-04-12 11:30:00') * 1000, 15.0, 4, 4.5, 3, 26, 22, 2, 8, 1, 'Videná, kladie lepšie', 1, 3, 0, 'Pokojné', 'Varroa kontrola OK, sila rastie', strftime('%s', '2025-04-12') * 1000, strftime('%s', '2025-04-12') * 1000),
('insp-016-006', 'test-hive-016', strftime('%s', '2025-04-26 14:00:00') * 1000, 16.5, 5, 4.2, 4, 32, 28, 2, 8, 0, 'Nevidená, plod dobrý', 0, 0, 0, 'Pokojné', 'Redukcia polovičná, viac priestoru', strftime('%s', '2025-04-26') * 1000, strftime('%s', '2025-04-26') * 1000),
('insp-016-007', 'test-hive-016', strftime('%s', '2025-05-10 10:30:00') * 1000, 18.5, 5, 4.0, 4, 36, 32, 2, 8, 1, 'Videná na rámiku 5', 0, 0, 0, 'Pokojné', 'Redukcia odstránená úplne', strftime('%s', '2025-05-10') * 1000, strftime('%s', '2025-05-10') * 1000),
('insp-016-008', 'test-hive-016', strftime('%s', '2025-05-24 12:00:00') * 1000, 20.0, 6, 3.8, 5, 40, 36, 2, 8, 1, 'Videná, zelená', 1, 2, 0, 'Pokojné', 'Bez redukcie silnejšie, varroa nízka', strftime('%s', '2025-05-24') * 1000, strftime('%s', '2025-05-24') * 1000),
('insp-016-009', 'test-hive-016', strftime('%s', '2025-06-07 13:30:00') * 1000, 22.5, 6, 3.5, 5, 44, 40, 2, 8, 0, 'Nevidená, plod OK', 0, 0, 0, 'Pokojné', 'Normálna sila už, plný let', strftime('%s', '2025-06-07') * 1000, strftime('%s', '2025-06-07') * 1000),
('insp-016-010', 'test-hive-016', strftime('%s', '2025-06-21 11:00:00') * 1000, 24.5, 6, 3.3, 5, 46, 42, 2, 8, 1, 'Videná na rámiku 6', 0, 0, 0, 'Pokojné', 'Zlepšenie vďaka postupnému otváraniu', strftime('%s', '2025-06-21') * 1000, strftime('%s', '2025-06-21') * 1000),
('insp-016-011', 'test-hive-016', strftime('%s', '2025-07-05 14:00:00') * 1000, 26.5, 6, 3.0, 5, 48, 44, 2, 8, 1, 'Videná, zdravá', 1, 3, 0, 'Pokojné', 'Varroa v norme, redukcia pomohla', strftime('%s', '2025-07-05') * 1000, strftime('%s', '2025-07-05') * 1000),
('insp-016-012', 'test-hive-016', strftime('%s', '2025-07-19 10:30:00') * 1000, 28.0, 6, 2.8, 4, 46, 42, 2, 8, 0, 'Nevidená, ale OK', 0, 0, 0, 'Pokojné', 'Stabilné včelstvo, redukcia na jeseň', strftime('%s', '2025-07-19') * 1000, strftime('%s', '2025-07-19') * 1000),
('insp-016-013', 'test-hive-016', strftime('%s', '2025-08-02 12:00:00') * 1000, 29.5, 6, 2.5, 4, 44, 40, 2, 8, 1, 'Videná, zelená', 0, 0, 0, 'Pokojné', 'Redukcia sa osvedčila pre slabé', strftime('%s', '2025-08-02') * 1000, strftime('%s', '2025-08-02') * 1000);

-- Úľ 17: S vysokým dnom (test-hive-017) - 20 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-017-001', 'test-hive-017', strftime('%s', '2025-02-16 10:00:00') * 1000, 6.5, 3, 7.5, 2, 15, 12, 1, 10, 0, 'Nevidená, ventilácia dobrá', 0, 0, 0, 'Pokojné', 'Vysoké dno, menej vlhkosti v zime', strftime('%s', '2025-02-16') * 1000, strftime('%s', '2025-02-16') * 1000),
('insp-017-002', 'test-hive-017', strftime('%s', '2025-03-02 11:30:00') * 1000, 8.0, 3, 7.2, 2, 17, 14, 1, 10, 1, 'Videná, červená 2023', 0, 0, 0, 'Pokojné', 'Suché hniezdo, bez plesní', strftime('%s', '2025-03-02') * 1000, strftime('%s', '2025-03-02') * 1000),
('insp-017-003', 'test-hive-017', strftime('%s', '2025-03-16 13:00:00') * 1000, 11.5, 4, 7.0, 3, 24, 20, 2, 10, 1, 'Videná na rámiku 6', 0, 0, 0, 'Pokojné', 'Výborná ventilácia, zdravé plásty', strftime('%s', '2025-03-16') * 1000, strftime('%s', '2025-03-16') * 1000),
('insp-017-004', 'test-hive-017', strftime('%s', '2025-03-30 10:30:00') * 1000, 13.5, 5, 6.8, 4, 32, 28, 2, 10, 0, 'Nevidená, plod OK', 0, 0, 0, 'Pokojné', 'Jarný rozvoj výborný', strftime('%s', '2025-03-30') * 1000, strftime('%s', '2025-03-30') * 1000),
('insp-017-005', 'test-hive-017', strftime('%s', '2025-04-13 12:00:00') * 1000, 15.5, 6, 6.5, 5, 40, 36, 3, 10, 1, 'Videná, kladie dobre', 1, 2, 0, 'Pokojné', 'Varroa nízka, vysoké dno výhoda', strftime('%s', '2025-04-13') * 1000, strftime('%s', '2025-04-13') * 1000),
('insp-017-006', 'test-hive-017', strftime('%s', '2025-04-27 13:30:00') * 1000, 17.0, 7, 6.2, 6, 48, 44, 3, 10, 1, 'Videná na rámiku 7', 0, 0, 0, 'Pokojné', 'Stoky pridané, ventilácia výborná', strftime('%s', '2025-04-27') * 1000, strftime('%s', '2025-04-27') * 1000),
('insp-017-007', 'test-hive-017', strftime('%s', '2025-05-11 11:00:00') * 1000, 19.0, 7, 6.0, 6, 52, 48, 3, 10, 0, 'Nevidená, plod výborný', 0, 0, 0, 'Pokojné', 'Žiadna vlhkosť v úle', strftime('%s', '2025-05-11') * 1000, strftime('%s', '2025-05-11') * 1000),
('insp-017-008', 'test-hive-017', strftime('%s', '2025-05-25 14:00:00') * 1000, 21.0, 8, 5.8, 7, 58, 54, 3, 10, 1, 'Videná, červená', 1, 2, 0, 'Pokojné', 'Varroa nízka, ventilácia pomáha', strftime('%s', '2025-05-25') * 1000, strftime('%s', '2025-05-25') * 1000),
('insp-017-009', 'test-hive-017', strftime('%s', '2025-06-08 10:30:00') * 1000, 23.5, 8, 5.5, 7, 62, 58, 3, 10, 1, 'Videná na rámiku 8', 0, 0, 0, 'Pokojné', 'V horúčave lepšia cirkulácia', strftime('%s', '2025-06-08') * 1000, strftime('%s', '2025-06-08') * 1000),
('insp-017-010', 'test-hive-017', strftime('%s', '2025-06-22 12:00:00') * 1000, 25.5, 8, 5.2, 7, 64, 60, 2, 10, 0, 'Nevidená, plod OK', 0, 0, 0, 'Pokojné', 'Menej ventilácie pri vstupe potrebné', strftime('%s', '2025-06-22') * 1000, strftime('%s', '2025-06-22') * 1000),
('insp-017-011', 'test-hive-017', strftime('%s', '2025-07-06 13:30:00') * 1000, 27.5, 8, 5.0, 7, 66, 62, 2, 10, 1, 'Videná na rámiku 6', 1, 3, 0, 'Pokojné', 'Varroa v norme, vysoké dno super', strftime('%s', '2025-07-06') * 1000, strftime('%s', '2025-07-06') * 1000),
('insp-017-012', 'test-hive-017', strftime('%s', '2025-07-20 11:00:00') * 1000, 29.0, 7, 4.8, 6, 62, 58, 2, 10, 1, 'Videná, červená', 0, 0, 0, 'Pokojné', 'Vysoké dno sa oplatilo', strftime('%s', '2025-07-20') * 1000, strftime('%s', '2025-07-20') * 1000),
('insp-017-013', 'test-hive-017', strftime('%s', '2025-08-03 14:00:00') * 1000, 30.5, 7, 4.5, 6, 60, 56, 2, 10, 0, 'Nevidená, ale OK', 0, 0, 0, 'Pokojné', 'Budúci rok všetky úle s vysokým dnom', strftime('%s', '2025-08-03') * 1000, strftime('%s', '2025-08-03') * 1000);

-- Úľ 18: Nové rámiky (test-hive-018) - 20 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-018-001', 'test-hive-018', strftime('%s', '2025-02-17 11:00:00') * 1000, 7.0, 3, 7.0, 2, 14, 11, 1, 10, 1, 'Videná, zelená 2024', 0, 0, 0, 'Pokojné', 'Všetky nové rámiky v 2025', strftime('%s', '2025-02-17') * 1000, strftime('%s', '2025-02-17') * 1000),
('insp-018-002', 'test-hive-018', strftime('%s', '2025-03-03 12:30:00') * 1000, 8.5, 3, 6.8, 2, 16, 13, 1, 10, 0, 'Nevidená, nové plásty', 0, 0, 0, 'Pokojné', 'Včely stavajú nové plásty', strftime('%s', '2025-03-03') * 1000, strftime('%s', '2025-03-03') * 1000),
('insp-018-003', 'test-hive-018', strftime('%s', '2025-03-17 10:00:00') * 1000, 11.5, 4, 6.5, 3, 22, 18, 2, 10, 1, 'Videná na novom rámiku', 0, 0, 0, 'Pokojné', 'Plásty sa dobre stavajú', strftime('%s', '2025-03-17') * 1000, strftime('%s', '2025-03-17') * 1000),
('insp-018-004', 'test-hive-018', strftime('%s', '2025-03-31 13:00:00') * 1000, 13.5, 5, 6.2, 4, 30, 26, 2, 10, 1, 'Videná, kladie na nové', 0, 0, 0, 'Pokojné', 'Matka rada klásla na nových plárstoch', strftime('%s', '2025-03-31') * 1000, strftime('%s', '2025-03-31') * 1000),
('insp-018-005', 'test-hive-018', strftime('%s', '2025-04-14 11:30:00') * 1000, 15.5, 6, 6.0, 5, 38, 34, 3, 10, 0, 'Nevidená, plod na všetkých', 1, 1, 0, 'Pokojné', 'Varroa minimálna na nových plárstoch!', strftime('%s', '2025-04-14') * 1000, strftime('%s', '2025-04-14') * 1000),
('insp-018-006', 'test-hive-018', strftime('%s', '2025-04-28 14:00:00') * 1000, 17.0, 7, 5.8, 6, 46, 42, 3, 10, 1, 'Videná na rámiku 8', 0, 0, 0, 'Pokojné', 'Nové plásty výhoda pre zdravie', strftime('%s', '2025-04-28') * 1000, strftime('%s', '2025-04-28') * 1000),
('insp-018-007', 'test-hive-018', strftime('%s', '2025-05-12 10:30:00') * 1000, 19.5, 8, 5.5, 7, 54, 50, 3, 10, 1, 'Videná, zelená výborná', 0, 0, 0, 'Pokojné', 'Všetky plásty už vybudované', strftime('%s', '2025-05-12') * 1000, strftime('%s', '2025-05-12') * 1000),
('insp-018-008', 'test-hive-018', strftime('%s', '2025-05-26 12:00:00') * 1000, 21.5, 8, 5.2, 7, 60, 56, 3, 10, 0, 'Nevidená, krásne plásty', 1, 0, 0, 'Pokojné', 'Žiadna varroa! Nové plásty čisté', strftime('%s', '2025-05-26') * 1000, strftime('%s', '2025-05-26') * 1000),
('insp-018-009', 'test-hive-018', strftime('%s', '2025-06-09 13:30:00') * 1000, 24.0, 9, 5.0, 8, 66, 62, 3, 10, 1, 'Videná na rámiku 7', 0, 0, 0, 'Pokojné', 'Najčistejšie plásty v včelnici', strftime('%s', '2025-06-09') * 1000, strftime('%s', '2025-06-09') * 1000),
('insp-018-010', 'test-hive-018', strftime('%s', '2025-06-23 11:00:00') * 1000, 26.0, 9, 4.8, 8, 70, 66, 3, 10, 1, 'Videná, zelená', 0, 0, 0, 'Pokojné', 'Svetlé plásty, bez chorôb', strftime('%s', '2025-06-23') * 1000, strftime('%s', '2025-06-23') * 1000),
('insp-018-011', 'test-hive-018', strftime('%s', '2025-07-07 14:00:00') * 1000, 28.0, 9, 4.5, 8, 72, 68, 2, 10, 0, 'Nevidená, plod výborný', 1, 1, 0, 'Pokojné', 'Prvá varroa - stále minimum', strftime('%s', '2025-07-07') * 1000, strftime('%s', '2025-07-07') * 1000),
('insp-018-012', 'test-hive-018', strftime('%s', '2025-07-21 10:30:00') * 1000, 29.5, 8, 4.2, 7, 68, 64, 2, 10, 1, 'Videná na rámiku 6', 0, 0, 0, 'Pokojné', 'Výmena plástov sa oplatila!', strftime('%s', '2025-07-21') * 1000, strftime('%s', '2025-07-21') * 1000),
('insp-018-013', 'test-hive-018', strftime('%s', '2025-08-04 12:00:00') * 1000, 31.0, 8, 4.0, 7, 66, 62, 2, 10, 1, 'Videná, zelená zdravá', 0, 0, 0, 'Pokojné', 'Každé 3 roky vymeniť všetky plásty', strftime('%s', '2025-08-04') * 1000, strftime('%s', '2025-08-04') * 1000);

-- Úľ 19: S trutovými buňkami (test-hive-019) - 20 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-019-001', 'test-hive-019', strftime('%s', '2025-02-18 10:30:00') * 1000, 6.5, 3, 7.2, 2, 14, 11, 1, 10, 0, 'Nevidená, s trutmi', 0, 0, 0, 'Pokojné', '2 trutové plásty pre varroa kontrolu', strftime('%s', '2025-02-18') * 1000, strftime('%s', '2025-02-18') * 1000),
('insp-019-002', 'test-hive-019', strftime('%s', '2025-03-04 12:00:00') * 1000, 8.0, 3, 7.0, 2, 16, 13, 1, 10, 1, 'Videná, červená 2023', 0, 0, 0, 'Pokojné', 'Trutové plásty ešte prázdne', strftime('%s', '2025-03-04') * 1000, strftime('%s', '2025-03-04') * 1000),
('insp-019-003', 'test-hive-019', strftime('%s', '2025-03-18 13:30:00') * 1000, 11.5, 4, 6.8, 3, 24, 20, 2, 10, 1, 'Videná na rámiku 7', 0, 0, 0, 'Pokojné', 'Matka začala klásť do trutových buniek', strftime('%s', '2025-03-18') * 1000, strftime('%s', '2025-03-18') * 1000),
('insp-019-004', 'test-hive-019', strftime('%s', '2025-04-01 11:00:00') * 1000, 13.5, 5, 6.5, 4, 32, 28, 2, 10, 0, 'Nevidená, truti rastú', 0, 0, 0, 'Pokojné', 'Trutový plod v 2 rámikoch', strftime('%s', '2025-04-01') * 1000, strftime('%s', '2025-04-01') * 1000),
('insp-019-005', 'test-hive-019', strftime('%s', '2025-04-15 14:00:00') * 1000, 15.5, 6, 6.2, 5, 40, 36, 3, 10, 1, 'Videná, kladie všade', 1, 8, 0, 'Pokojné', 'Varroa zachytená v trutovom plode!', strftime('%s', '2025-04-15') * 1000, strftime('%s', '2025-04-15') * 1000),
('insp-019-006', 'test-hive-019', strftime('%s', '2025-04-29 10:30:00') * 1000, 17.0, 6, 6.0, 5, 44, 40, 3, 10, 1, 'Videná na rámiku 8', 0, 0, 0, 'Pokojné', 'Trutové rámiky odstránené, varroa s nimi', strftime('%s', '2025-04-29') * 1000, strftime('%s', '2025-04-29') * 1000),
('insp-019-007', 'test-hive-019', strftime('%s', '2025-05-13 12:00:00') * 1000, 19.5, 7, 5.8, 6, 50, 46, 3, 10, 0, 'Nevidená, nové truty', 0, 0, 0, 'Pokojné', 'Pridané nové trutové rámiky', strftime('%s', '2025-05-13') * 1000, strftime('%s', '2025-05-13') * 1000),
('insp-019-008', 'test-hive-019', strftime('%s', '2025-05-27 13:30:00') * 1000, 21.5, 7, 5.5, 6, 54, 50, 3, 10, 1, 'Videná, červená', 1, 12, 0, 'Pokojné', 'Opäť varroa v trutovom plode zachytená', strftime('%s', '2025-05-27') * 1000, strftime('%s', '2025-05-27') * 1000),
('insp-019-009', 'test-hive-019', strftime('%s', '2025-06-10 11:00:00') * 1000, 24.0, 7, 5.2, 6, 56, 52, 3, 10, 1, 'Videná na rámiku 6', 0, 0, 0, 'Pokojné', '2. odstránenie trutových rámikov', strftime('%s', '2025-06-10') * 1000, strftime('%s', '2025-06-10') * 1000),
('insp-019-010', 'test-hive-019', strftime('%s', '2025-06-24 14:00:00') * 1000, 26.5, 8, 5.0, 7, 60, 56, 2, 10, 0, 'Nevidená, plod OK', 1, 2, 0, 'Pokojné', 'Varroa v pracovnom plode nízka!', strftime('%s', '2025-06-24') * 1000, strftime('%s', '2025-06-24') * 1000),
('insp-019-011', 'test-hive-019', strftime('%s', '2025-07-08 10:30:00') * 1000, 28.0, 8, 4.8, 7, 62, 58, 2, 10, 1, 'Videná na rámiku 7', 0, 0, 0, 'Pokojné', 'Trutové buňky metóda funguje!', strftime('%s', '2025-07-08') * 1000, strftime('%s', '2025-07-08') * 1000),
('insp-019-012', 'test-hive-019', strftime('%s', '2025-07-22 12:00:00') * 1000, 29.5, 7, 4.5, 6, 58, 54, 2, 10, 1, 'Videná, červená', 1, 1, 0, 'Pokojné', 'Celkovo 3x odstránené truty s varrózou', strftime('%s', '2025-07-22') * 1000, strftime('%s', '2025-07-22') * 1000),
('insp-019-013', 'test-hive-019', strftime('%s', '2025-08-05 13:30:00') * 1000, 31.0, 7, 4.2, 6, 56, 52, 2, 10, 0, 'Nevidená, ale OK', 0, 0, 0, 'Pokojné', 'Biotechnická metóda úspešná', strftime('%s', '2025-08-05') * 1000, strftime('%s', '2025-08-05') * 1000);

-- Úľ 20: Silné na delenie (test-hive-020) - 20 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-020-001', 'test-hive-020', strftime('%s', '2025-02-19 11:00:00') * 1000, 7.5, 4, 8.5, 3, 20, 16, 2, 12, 1, 'Videná, zelená 2024', 0, 0, 0, 'Pokojné', 'Veľmi silné včelstvo, kandidát na delenie', strftime('%s', '2025-02-19') * 1000, strftime('%s', '2025-02-19') * 1000),
('insp-020-002', 'test-hive-020', strftime('%s', '2025-03-05 12:30:00') * 1000, 9.0, 5, 8.2, 4, 28, 24, 2, 12, 1, 'Videná, kladie intenzívne', 0, 0, 0, 'Pokojné', 'Rýchly rozvoj, sledovať rojivosť', strftime('%s', '2025-03-05') * 1000, strftime('%s', '2025-03-05') * 1000),
('insp-020-003', 'test-hive-020', strftime('%s', '2025-03-19 10:00:00') * 1000, 12.5, 6, 8.0, 5, 38, 34, 3, 12, 0, 'Nevidená, plný úľ', 0, 0, 0, 'Pokojné', 'Veľká sila už v marci', strftime('%s', '2025-03-19') * 1000, strftime('%s', '2025-03-19') * 1000),
('insp-020-004', 'test-hive-020', strftime('%s', '2025-04-02 13:00:00') * 1000, 14.5, 7, 7.8, 6, 48, 44, 3, 12, 1, 'Videná na rámiku 9', 0, 0, 0, 'Pokojné', 'Príprava na delenie v máji', strftime('%s', '2025-04-02') * 1000, strftime('%s', '2025-04-02') * 1000),
('insp-020-005', 'test-hive-020', strftime('%s', '2025-04-16 11:30:00') * 1000, 16.5, 8, 7.5, 7, 56, 52, 3, 12, 1, 'Videná, výborná matka', 1, 1, 0, 'Pokojné', 'Varroa minimálna, pripravené na delenie', strftime('%s', '2025-04-16') * 1000, strftime('%s', '2025-04-16') * 1000),
('insp-020-006', 'test-hive-020', strftime('%s', '2025-04-30 14:00:00') * 1000, 18.5, 9, 7.2, 8, 64, 60, 3, 12, 0, 'Nevidená, plod na všetkých', 0, 0, 0, 'Pokojné', 'Všetky rámiky obývané, čas deliť', strftime('%s', '2025-04-30') * 1000, strftime('%s', '2025-04-30') * 1000),
('insp-020-007', 'test-hive-020', strftime('%s', '2025-05-14 10:30:00') * 1000, 20.5, 7, 7.0, 6, 50, 46, 3, 12, 1, 'Videná tu, delenie hotové', 0, 0, 0, 'Pokojné', 'Delenie úspešné! Polovina rámikov oddelená', strftime('%s', '2025-05-14') * 1000, strftime('%s', '2025-05-14') * 1000),
('insp-020-008', 'test-hive-020', strftime('%s', '2025-05-28 12:00:00') * 1000, 22.0, 7, 6.8, 6, 52, 48, 3, 12, 1, 'Videná, kladie znova plno', 1, 2, 0, 'Pokojné', 'Po delení sa rýchlo obnovuje', strftime('%s', '2025-05-28') * 1000, strftime('%s', '2025-05-28') * 1000),
('insp-020-009', 'test-hive-020', strftime('%s', '2025-06-11 13:30:00') * 1000, 24.5, 8, 6.5, 7, 58, 54, 3, 12, 0, 'Nevidená, opäť silné', 0, 0, 0, 'Pokojné', 'Už opäť plná sila!', strftime('%s', '2025-06-11') * 1000, strftime('%s', '2025-06-11') * 1000),
('insp-020-010', 'test-hive-020', strftime('%s', '2025-06-25 11:00:00') * 1000, 26.5, 9, 6.2, 8, 64, 60, 3, 12, 1, 'Videná na rámiku 8', 0, 0, 0, 'Pokojné', 'Neuverieteľný rozvoj po delení', strftime('%s', '2025-06-25') * 1000, strftime('%s', '2025-06-25') * 1000),
('insp-020-011', 'test-hive-020', strftime('%s', '2025-07-09 14:00:00') * 1000, 28.5, 9, 6.0, 8, 68, 64, 3, 12, 1, 'Videná, zelená zdravá', 1, 2, 0, 'Pokojné', 'Varroa nízka, delenie úspešné', strftime('%s', '2025-07-09') * 1000, strftime('%s', '2025-07-09') * 1000),
('insp-020-012', 'test-hive-020', strftime('%s', '2025-07-23 10:30:00') * 1000, 30.0, 9, 5.8, 8, 70, 66, 2, 12, 0, 'Nevidená, plod výborný', 0, 0, 0, 'Pokojné', 'Obe časti delenia prosperujú', strftime('%s', '2025-07-23') * 1000, strftime('%s', '2025-07-23') * 1000),
('insp-020-013', 'test-hive-020', strftime('%s', '2025-08-06 12:00:00') * 1000, 31.5, 8, 5.5, 7, 66, 62, 2, 12, 1, 'Videná, zelená výborná', 0, 0, 0, 'Pokojné', 'Z 1 úľa sú 2, oba silné!', strftime('%s', '2025-08-06') * 1000, strftime('%s', '2025-08-06') * 1000);

-- ========================================
-- ZHRNUTIE PREHLIADOK
-- ========================================
-- Celkovo: 400 prehliadok
-- Úle 1-9, 11-20: 20 prehliadok každý = 19 × 20 = 380 prehliadok
-- Úľ 10 (uhynutý): 3 prehliadky
-- Úľ 6 (roj): 10 prehliadok (chytený až v máji)
-- Spolu: 380 + 3 + 10 = 393 prehliadok
--
-- Poznámka: Niektoré úle majú 13 prehliadok namiesto 20 kvôli mesačnému rozdeleniu
-- Presný počet: 13 × 19 úľov + 3 (úľ 10) + 10 (úľ 6) = 247 + 3 + 10 = 260 prehliadok
-- Chýba: 400 - 260 = 140 prehliadok
-- Pridávame dodatočné prehliadky pre úle 1-20

-- ========================================
-- DODATOČNÉ PREHLIADKY (140 kusov)
-- ========================================
-- Dopĺňame každému úľu ďalších 7 prehliadok (20 úľov × 7 = 140)

-- Úľ 1 - dodatočných 7 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-001-014', 'test-hive-001', strftime('%s', '2025-02-08 11:00:00') * 1000, 7.0, 3, 8.2, 2, 16, 13, 1, 10, 1, 'Videná v klube', 0, 0, 0, 'Pokojné', 'Kontrola po zimnej prehliadke', strftime('%s', '2025-02-08') * 1000, strftime('%s', '2025-02-08') * 1000),
('insp-001-015', 'test-hive-001', strftime('%s', '2025-02-22 14:00:00') * 1000, 9.0, 3, 7.9, 2, 19, 16, 1, 10, 0, 'Nevidená, vajíčka OK', 0, 0, 0, 'Pokojné', 'Matka intenzívne klásla', strftime('%s', '2025-02-22') * 1000, strftime('%s', '2025-02-22') * 1000),
('insp-001-016', 'test-hive-001', strftime('%s', '2025-03-08 10:30:00') * 1000, 12.0, 4, 7.4, 3, 27, 24, 2, 10, 1, 'Videná na rámiku 4', 0, 0, 0, 'Pokojné', 'Jarný zber peľu začal', strftime('%s', '2025-03-08') * 1000, strftime('%s', '2025-03-08') * 1000),
('insp-001-017', 'test-hive-001', strftime('%s', '2025-03-22 13:00:00') * 1000, 14.0, 5, 6.8, 4, 37, 33, 2, 10, 1, 'Videná, kladie maximum', 0, 0, 0, 'Pokojné', 'Rana prehliadka pred stokami', strftime('%s', '2025-03-22') * 1000, strftime('%s', '2025-03-22') * 1000),
('insp-001-018', 'test-hive-001', strftime('%s', '2025-04-05 11:30:00') * 1000, 16.0, 6, 6.2, 5, 44, 40, 3, 10, 0, 'Nevidená, veľa plodu', 0, 0, 0, 'Pokojné', 'Jarná medovica v plnom prúde', strftime('%s', '2025-04-05') * 1000, strftime('%s', '2025-04-05') * 1000),
('insp-001-019', 'test-hive-001', strftime('%s', '2025-04-19 14:00:00') * 1000, 17.5, 7, 5.9, 6, 52, 47, 3, 10, 1, 'Videná na rámiku 6', 0, 0, 0, 'Pokojné', 'Kontrola pred pridaním stokov', strftime('%s', '2025-04-19') * 1000, strftime('%s', '2025-04-19') * 1000),
('insp-001-020', 'test-hive-001', strftime('%s', '2025-05-03 10:00:00') * 1000, 19.5, 8, 5.5, 7, 60, 55, 3, 10, 1, 'Videná, zelená výborná', 1, 1, 0, 'Pokojné', 'Stoky sa začínajú plniť', strftime('%s', '2025-05-03') * 1000, strftime('%s', '2025-05-03') * 1000);

-- Úľ 2 - dodatočných 7 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-002-014', 'test-hive-002', strftime('%s', '2025-02-10 12:00:00') * 1000, 7.5, 2, 9.2, 1, 11, 9, 1, 12, 1, 'Videná, červená', 0, 0, 0, 'Pokojné', 'Dadant zásoby vynikajúce', strftime('%s', '2025-02-10') * 1000, strftime('%s', '2025-02-10') * 1000),
('insp-002-015', 'test-hive-002', strftime('%s', '2025-02-24 13:30:00') * 1000, 9.5, 3, 8.5, 2, 15, 12, 1, 12, 0, 'Nevidená, klub OK', 0, 0, 0, 'Pokojné', 'Pomaly sa rozvíja', strftime('%s', '2025-02-24') * 1000, strftime('%s', '2025-02-24') * 1000),
('insp-002-016', 'test-hive-002', strftime('%s', '2025-03-10 11:00:00') * 1000, 12.5, 4, 8.3, 3, 24, 20, 2, 12, 1, 'Videná na rámiku 7', 0, 0, 0, 'Pokojné', 'Stará ale stále kladie', strftime('%s', '2025-03-10') * 1000, strftime('%s', '2025-03-10') * 1000),
('insp-002-017', 'test-hive-002', strftime('%s', '2025-03-24 14:00:00') * 1000, 14.5, 5, 7.6, 4, 32, 27, 2, 12, 1, 'Videná, tempo klesá', 0, 0, 0, 'Pokojné', 'Matka z 2023, koniec kariéry', strftime('%s', '2025-03-24') * 1000, strftime('%s', '2025-03-24') * 1000),
('insp-002-018', 'test-hive-002', strftime('%s', '2025-04-07 10:30:00') * 1000, 16.5, 6, 7.0, 5, 40, 34, 3, 12, 0, 'Nevidená, vajíčka áno', 1, 3, 0, 'Pokojné', 'Varroa začína rásť', strftime('%s', '2025-04-07') * 1000, strftime('%s', '2025-04-07') * 1000),
('insp-002-019', 'test-hive-002', strftime('%s', '2025-04-21 12:00:00') * 1000, 18.0, 6, 6.7, 6, 47, 40, 3, 12, 1, 'Videná na rámiku 9', 0, 0, 0, 'Pokojné', 'Kladie, ale pomaly', strftime('%s', '2025-04-21') * 1000, strftime('%s', '2025-04-21') * 1000),
('insp-002-020', 'test-hive-002', strftime('%s', '2025-05-05 13:30:00') * 1000, 19.5, 7, 6.4, 7, 53, 46, 3, 12, 1, 'Videná, stará červená', 0, 0, 0, 'Pokojné', 'Posledná sezóna tejto matky', strftime('%s', '2025-05-05') * 1000, strftime('%s', '2025-05-05') * 1000);

-- Úľ 3 - dodatočných 7 prehliadok
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-003-014', 'test-hive-003', strftime('%s', '2025-02-09 10:30:00') * 1000, 6.5, 2, 5.7, 1, 9, 7, 1, 8, 1, 'Videná, biela stará', 0, 0, 0, 'Pokojné', 'Kriticky slabé, zvážiť zjednotenie', strftime('%s', '2025-02-09') * 1000, strftime('%s', '2025-02-09') * 1000),
('insp-003-015', 'test-hive-003', strftime('%s', '2025-02-23 12:00:00') * 1000, 9.0, 2, 5.3, 1, 10, 8, 1, 8, 0, 'Nevidená, minimum plodu', 0, 0, 0, 'Nervózne', 'Matka z 2021 na konci', strftime('%s', '2025-02-23') * 1000, strftime('%s', '2025-02-23') * 1000),
('insp-003-016', 'test-hive-003', strftime('%s', '2025-03-09 13:30:00') * 1000, 11.5, 2, 5.0, 2, 13, 11, 1, 8, 1, 'Videná, nedostatočná', 0, 0, 0, 'Nervózne', 'Rozhodnuté - výmena nevyhnutná', strftime('%s', '2025-03-09') * 1000, strftime('%s', '2025-03-09') * 1000),
('insp-003-017', 'test-hive-003', strftime('%s', '2025-03-23 11:00:00') * 1000, 13.5, 3, 4.7, 2, 16, 13, 1, 8, 0, 'Nevidená, plod riedky', 1, 7, 0, 'Nervózne', 'Varroa vysoká, slabé včelstvo', strftime('%s', '2025-03-23') * 1000, strftime('%s', '2025-03-23') * 1000),
('insp-003-018', 'test-hive-003', strftime('%s', '2025-04-06 14:00:00') * 1000, 15.5, 3, 4.4, 3, 19, 15, 1, 8, 1, 'Videná naposledy', 0, 0, 0, 'Nervózne', 'Príprava na výmenu', strftime('%s', '2025-04-06') * 1000, strftime('%s', '2025-04-06') * 1000),
('insp-003-019', 'test-hive-003', strftime('%s', '2025-04-20 10:30:00') * 1000, 17.0, 3, 4.1, 3, 21, 17, 1, 8, 0, 'Matečník pridaný', 0, 0, 0, 'Nervózne', 'Stará odstránená, čaká sa na novú', strftime('%s', '2025-04-20') * 1000, strftime('%s', '2025-04-20') * 1000),
('insp-003-020', 'test-hive-003', strftime('%s', '2025-05-04 12:00:00') * 1000, 18.5, 4, 3.9, 3, 24, 20, 2, 8, 1, 'Nová zelená!', 0, 0, 0, 'Pokojné', 'Úspešná výmena, nádej na lepšie', strftime('%s', '2025-05-04') * 1000, strftime('%s', '2025-05-04') * 1000);

-- Úľ 4-9 - dodatočných 7 prehliadok každý (42 prehliadok)
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
-- Úľ 4
('insp-004-014', 'test-hive-004', strftime('%s', '2025-02-11 11:30:00') * 1000, 8.0, 3, 7.2, 2, 17, 14, 1, 10, 0, 'Nevidená, útočia', 0, 0, 1, 'Agresívne', 'Zimná agresia pokračuje', strftime('%s', '2025-02-11') * 1000, strftime('%s', '2025-02-11') * 1000),
('insp-004-015', 'test-hive-004', strftime('%s', '2025-02-25 13:00:00') * 1000, 10.0, 3, 6.8, 2, 19, 16, 1, 10, 1, 'Videná, aggressive', 0, 0, 1, 'Agresívne', 'Potrebná výmena naliehavo', strftime('%s', '2025-02-25') * 1000, strftime('%s', '2025-02-25') * 1000),
('insp-004-016', 'test-hive-004', strftime('%s', '2025-03-11 10:00:00') * 1000, 13.0, 4, 6.3, 3, 26, 22, 2, 10, 0, 'Ne videná, chaos', 0, 0, 1, 'Agresívne', 'Extrémne ťažká prehliadka', strftime('%s', '2025-03-11') * 1000, strftime('%s', '2025-03-11') * 1000),
('insp-004-017', 'test-hive-004', strftime('%s', '2025-03-25 12:00:00') * 1000, 15.0, 5, 6.0, 4, 34, 30, 2, 10, 1, 'Videná, červená zlá', 0, 0, 1, 'Agresívne', 'Výchova novej matky pripravená', strftime('%s', '2025-03-25') * 1000, strftime('%s', '2025-03-25') * 1000),
('insp-004-018', 'test-hive-004', strftime('%s', '2025-04-08 13:30:00') * 1000, 16.5, 6, 5.7, 5, 42, 37, 3, 10, 0, 'Nevidená, útočné', 0, 0, 1, 'Agresívne', 'Posledné dni agresívnej matky', strftime('%s', '2025-04-08') * 1000, strftime('%s', '2025-04-08') * 1000),
('insp-004-019', 'test-hive-004', strftime('%s', '2025-04-22 11:00:00') * 1000, 18.0, 6, 5.4, 5, 46, 40, 3, 10, 0, 'Stará odstránená', 0, 0, 1, 'Agresívne', 'Výmena prebieha', strftime('%s', '2025-04-22') * 1000, strftime('%s', '2025-04-22') * 1000),
('insp-004-020', 'test-hive-004', strftime('%s', '2025-05-06 14:00:00') * 1000, 19.5, 7, 5.2, 6, 52, 46, 3, 10, 1, 'Nová zelená videná', 0, 0, 0, 'Pokojné', 'Zázračná premena!', strftime('%s', '2025-05-06') * 1000, strftime('%s', '2025-05-06') * 1000),
-- Úľ 5
('insp-005-014', 'test-hive-005', strftime('%s', '2025-02-12 10:00:00') * 1000, 7.5, 3, 7.8, 2, 18, 15, 1, 10, 1, 'Videná, zelená mladá', 0, 0, 0, 'Pokojné', 'Mladá matka výborne prezimovala', strftime('%s', '2025-02-12') * 1000, strftime('%s', '2025-02-12') * 1000),
('insp-005-015', 'test-hive-005', strftime('%s', '2025-02-26 11:30:00') * 1000, 9.5, 4, 7.3, 2, 21, 17, 1, 10, 0, 'Nevidená, intenzívny plod', 0, 0, 0, 'Pokojné', 'Rýchly rozvoj začal', strftime('%s', '2025-02-26') * 1000, strftime('%s', '2025-02-26') * 1000),
('insp-005-016', 'test-hive-005', strftime('%s', '2025-03-12 13:00:00') * 1000, 12.5, 5, 6.8, 4, 32, 28, 2, 10, 1, 'Videná na rámiku 6', 0, 0, 0, 'Pokojné', 'Plný úľ, sledovať rojivosť', strftime('%s', '2025-03-12') * 1000, strftime('%s', '2025-03-12') * 1000),
('insp-005-017', 'test-hive-005', strftime('%s', '2025-03-26 10:30:00') * 1000, 14.5, 6, 6.4, 5, 44, 38, 3, 10, 1, 'Videná, kladie maximum', 0, 0, 0, 'Nervózne', 'Príznaky rojivosti', strftime('%s', '2025-03-26') * 1000, strftime('%s', '2025-03-26') * 1000),
('insp-005-018', 'test-hive-005', strftime('%s', '2025-04-09 12:00:00') * 1000, 16.0, 7, 6.1, 6, 54, 48, 3, 10, 0, 'Nevidená, matečníky', 0, 0, 0, 'Rojivé', 'Prvý matečník objavený', strftime('%s', '2025-04-09') * 1000, strftime('%s', '2025-04-09') * 1000),
('insp-005-019', 'test-hive-005', strftime('%s', '2025-04-23 13:30:00') * 1000, 18.5, 8, 5.9, 7, 62, 56, 3, 10, 1, 'Videná, 3 matečníky', 0, 0, 0, 'Rojivé', 'Stokovanie nutné', strftime('%s', '2025-04-23') * 1000, strftime('%s', '2025-04-23') * 1000),
('insp-005-020', 'test-hive-005', strftime('%s', '2025-05-07 11:00:00') * 1000, 20.0, 8, 5.6, 7, 64, 58, 3, 10, 1, 'Videná, stoky pridané', 0, 0, 0, 'Pokojné', 'Stokovanie zabralo', strftime('%s', '2025-05-07') * 1000, strftime('%s', '2025-05-07') * 1000),
-- Úľ 6 (roj) - dodatočných 10 prehliadok (celkovo bude mať 20)
('insp-006-011', 'test-hive-006', strftime('%s', '2025-06-01 10:00:00') * 1000, 23.5, 7, 3.2, 5, 40, 36, 2, 8, 1, 'Videná, modrá zdravá', 0, 0, 0, 'Pokojné', 'Roj sa dobre ujal', strftime('%s', '2025-06-01') * 1000, strftime('%s', '2025-06-01') * 1000),
('insp-006-012', 'test-hive-006', strftime('%s', '2025-06-10 12:00:00') * 1000, 25.0, 7, 3.6, 5, 42, 38, 2, 8, 0, 'Nevidená, plod dobrý', 0, 0, 0, 'Pokojné', 'Rýchle budovanie', strftime('%s', '2025-06-10') * 1000, strftime('%s', '2025-06-10') * 1000),
('insp-006-013', 'test-hive-006', strftime('%s', '2025-06-17 13:30:00') * 1000, 26.5, 7, 4.2, 5, 44, 40, 2, 8, 1, 'Videná na rámiku 4', 1, 0, 0, 'Pokojné', 'Varroa stále čistý roj', strftime('%s', '2025-06-17') * 1000, strftime('%s', '2025-06-17') * 1000),
('insp-006-014', 'test-hive-006', strftime('%s', '2025-06-24 11:00:00') * 1000, 27.5, 7, 4.7, 5, 46, 42, 2, 8, 1, 'Videná, modrá', 0, 0, 0, 'Pokojné', 'Už 8 rámikov obsadených', strftime('%s', '2025-06-24') * 1000, strftime('%s', '2025-06-24') * 1000),
('insp-006-015', 'test-hive-006', strftime('%s', '2025-07-01 14:00:00') * 1000, 28.5, 7, 5.2, 5, 48, 44, 2, 8, 0, 'Nevidená, ale OK', 0, 0, 0, 'Pokojné', 'Roj prosperuje', strftime('%s', '2025-07-01') * 1000, strftime('%s', '2025-07-01') * 1000),
('insp-006-016', 'test-hive-006', strftime('%s', '2025-07-08 10:30:00') * 1000, 29.5, 7, 5.6, 5, 50, 46, 2, 8, 1, 'Videná na rámiku 6', 1, 1, 0, 'Pokojné', 'Prvá varroa v roji', strftime('%s', '2025-07-08') * 1000, strftime('%s', '2025-07-08') * 1000),
('insp-006-017', 'test-hive-006', strftime('%s', '2025-07-15 12:00:00') * 1000, 30.0, 7, 6.0, 5, 52, 48, 2, 8, 1, 'Videná, modrá krásna', 0, 0, 0, 'Pokojné', 'Zásoby nazbierané', strftime('%s', '2025-07-15') * 1000, strftime('%s', '2025-07-15') * 1000),
('insp-006-018', 'test-hive-006', strftime('%s', '2025-07-22 13:30:00') * 1000, 29.0, 7, 6.5, 5, 54, 50, 2, 8, 0, 'Nevidená, plod OK', 0, 0, 0, 'Pokojné', 'Pripravené na zimu', strftime('%s', '2025-07-22') * 1000, strftime('%s', '2025-07-22') * 1000),
('insp-006-019', 'test-hive-006', strftime('%s', '2025-07-29 11:00:00') * 1000, 28.5, 7, 7.0, 5, 56, 52, 2, 8, 1, 'Videná, zdravá', 1, 1, 0, 'Pokojné', 'Varroa minimálna', strftime('%s', '2025-07-29') * 1000, strftime('%s', '2025-07-29') * 1000),
('insp-006-020', 'test-hive-006', strftime('%s', '2025-08-05 14:00:00') * 1000, 28.0, 7, 7.5, 5, 58, 54, 2, 8, 1, 'Videná, modrá', 0, 0, 0, 'Pokojné', 'Úspešný roj prezimuje', strftime('%s', '2025-08-05') * 1000, strftime('%s', '2025-08-05') * 1000),
-- Úľ 7
('insp-007-014', 'test-hive-007', strftime('%s', '2025-02-13 11:30:00') * 1000, 7.0, 3, 7.5, 2, 15, 12, 1, 10, 1, 'Videná, červená', 0, 0, 0, 'Pokojné', 'Zander prezimoval dobre', strftime('%s', '2025-02-13') * 1000, strftime('%s', '2025-02-13') * 1000),
('insp-007-015', 'test-hive-007', strftime('%s', '2025-02-27 13:00:00') * 1000, 9.0, 3, 7.0, 2, 17, 14, 1, 10, 0, 'Nevidená, plod OK', 0, 0, 0, 'Pokojné', 'Normálny rozvoj', strftime('%s', '2025-02-27') * 1000, strftime('%s', '2025-02-27') * 1000),
('insp-007-016', 'test-hive-007', strftime('%s', '2025-03-13 10:00:00') * 1000, 12.0, 4, 6.6, 3, 26, 22, 2, 10, 1, 'Videná na rámiku 7', 0, 0, 0, 'Pokojné', 'Jarné oživenie', strftime('%s', '2025-03-13') * 1000, strftime('%s', '2025-03-13') * 1000),
('insp-007-017', 'test-hive-007', strftime('%s', '2025-03-27 12:00:00') * 1000, 14.0, 5, 6.1, 4, 34, 30, 2, 10, 1, 'Videná, kladie OK', 0, 0, 0, 'Pokojné', 'Zander rámiky sa plnia', strftime('%s', '2025-03-27') * 1000, strftime('%s', '2025-03-27') * 1000),
('insp-007-018', 'test-hive-007', strftime('%s', '2025-04-10 13:30:00') * 1000, 16.0, 6, 5.8, 5, 42, 37, 3, 10, 0, 'Nevidená, vajíčka áno', 1, 3, 0, 'Pokojné', 'Varroa kontrola OK', strftime('%s', '2025-04-10') * 1000, strftime('%s', '2025-04-10') * 1000),
('insp-007-019', 'test-hive-007', strftime('%s', '2025-04-24 11:00:00') * 1000, 17.5, 6, 5.6, 6, 48, 42, 3, 10, 1, 'Videná na rámiku 8', 0, 0, 0, 'Pokojné', 'Pripravený na stoky', strftime('%s', '2025-04-24') * 1000, strftime('%s', '2025-04-24') * 1000),
('insp-007-020', 'test-hive-007', strftime('%s', '2025-05-08 14:00:00') * 1000, 19.5, 7, 5.4, 6, 54, 48, 3, 10, 1, 'Videná, červená', 0, 0, 0, 'Pokojné', 'Zander stoky fungujú', strftime('%s', '2025-05-08') * 1000, strftime('%s', '2025-05-08') * 1000),
-- Úľ 8
('insp-008-014', 'test-hive-008', strftime('%s', '2025-02-14 10:00:00') * 1000, 7.5, 2, 6.2, 1, 11, 9, 1, 10, 1, 'Videná, varroa na nej', 1, 10, 0, 'Nervózne', 'Kritická varroa situácia', strftime('%s', '2025-02-14') * 1000, strftime('%s', '2025-02-14') * 1000),
('insp-008-015', 'test-hive-008', strftime('%s', '2025-02-28 11:30:00') * 1000, 8.5, 2, 5.8, 1, 12, 10, 1, 10, 0, 'Nevidená, varroa všade', 1, 14, 0, 'Nervózne', 'Naliehavé ošetrenie potrebné', strftime('%s', '2025-02-28') * 1000, strftime('%s', '2025-02-28') * 1000),
('insp-008-016', 'test-hive-008', strftime('%s', '2025-03-14 13:00:00') * 1000, 11.5, 2, 5.4, 2, 16, 13, 1, 10, 1, 'Videná, oslabená', 1, 16, 0, 'Nervózne', 'Po ošetrení kyselina šťaveľová', strftime('%s', '2025-03-14') * 1000, strftime('%s', '2025-03-14') * 1000),
('insp-008-017', 'test-hive-008', strftime('%s', '2025-03-28 10:30:00') * 1000, 13.5, 3, 5.1, 2, 20, 16, 1, 10, 0, 'Nevidená, mierne lepšie', 1, 11, 0, 'Nervózne', 'Ošetrenie zabralo čiastočne', strftime('%s', '2025-03-28') * 1000, strftime('%s', '2025-03-28') * 1000),
('insp-008-018', 'test-hive-008', strftime('%s', '2025-04-11 12:00:00') * 1000, 15.5, 4, 4.9, 3, 26, 22, 2, 10, 1, 'Videná, žltá', 1, 20, 0, 'Nervózne', 'Varroa opäť stúpa', strftime('%s', '2025-04-11') * 1000, strftime('%s', '2025-04-11') * 1000),
('insp-008-019', 'test-hive-008', strftime('%s', '2025-04-25 13:30:00') * 1000, 17.0, 4, 4.7, 3, 28, 24, 2, 10, 0, 'Nevidená, problém', 1, 24, 0, 'Nervózne', '2. ošetrenie - Apiguard', strftime('%s', '2025-04-25') * 1000, strftime('%s', '2025-04-25') * 1000),
('insp-008-020', 'test-hive-008', strftime('%s', '2025-05-09 11:00:00') * 1000, 19.0, 5, 4.4, 4, 34, 30, 2, 10, 1, 'Videná, trochu lepšie', 1, 14, 0, 'Pokojné', 'Po ošetrení zlepšenie', strftime('%s', '2025-05-09') * 1000, strftime('%s', '2025-05-09') * 1000),
-- Úľ 9
('insp-009-014', 'test-hive-009', strftime('%s', '2025-02-15 11:00:00') * 1000, 7.0, 3, 8.0, 2, 17, 14, 1, 10, 0, 'Nevidená, sitko OK', 0, 0, 0, 'Pokojné', 'Varroa sitko zachytáva', strftime('%s', '2025-02-15') * 1000, strftime('%s', '2025-02-15') * 1000),
('insp-009-015', 'test-hive-009', strftime('%s', '2025-03-01 12:30:00') * 1000, 8.5, 3, 7.6, 2, 19, 16, 1, 10, 1, 'Videná, zelená', 0, 0, 0, 'Pokojné', 'Sitko plné roztoča', strftime('%s', '2025-03-01') * 1000, strftime('%s', '2025-03-01') * 1000),
('insp-009-016', 'test-hive-009', strftime('%s', '2025-03-15 10:00:00') * 1000, 12.0, 4, 7.3, 3, 28, 24, 2, 10, 1, 'Videná na rámiku 6', 1, 0, 0, 'Pokojné', 'Žiadna varroa na včelách!', strftime('%s', '2025-03-15') * 1000, strftime('%s', '2025-03-15') * 1000),
('insp-009-017', 'test-hive-009', strftime('%s', '2025-03-29 13:00:00') * 1000, 14.0, 5, 6.8, 4, 36, 32, 2, 10, 0, 'Nevidená, plod výborný', 0, 0, 0, 'Pokojné', 'Sitko funguje perfektne', strftime('%s', '2025-03-29') * 1000, strftime('%s', '2025-03-29') * 1000),
('insp-009-018', 'test-hive-009', strftime('%s', '2025-04-12 11:30:00') * 1000, 16.0, 6, 6.4, 5, 44, 40, 3, 10, 1, 'Videná na rámiku 8', 0, 0, 0, 'Pokojné', 'Najzdravšie včelstvo', strftime('%s', '2025-04-12') * 1000, strftime('%s', '2025-04-12') * 1000),
('insp-009-019', 'test-hive-009', strftime('%s', '2025-04-26 14:00:00') * 1000, 17.5, 7, 6.1, 6, 52, 48, 3, 10, 1, 'Videná, zelená výborná', 1, 0, 0, 'Pokojné', 'Stále nulová varroa', strftime('%s', '2025-04-26') * 1000, strftime('%s', '2025-04-26') * 1000),
('insp-009-020', 'test-hive-009', strftime('%s', '2025-05-10 10:30:00') * 1000, 19.5, 8, 5.9, 7, 60, 56, 3, 10, 0, 'Nevidená, plod plný', 0, 0, 0, 'Pokojné', 'Varroa sitko - najlepšia investícia', strftime('%s', '2025-05-10') * 1000, strftime('%s', '2025-05-10') * 1000);

-- Úľ 11-20 - dodatočných 7 prehliadok každý (70 prehliadok)
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
-- Úľ 11
('insp-011-014', 'test-hive-011', strftime('%s', '2025-02-17 11:00:00') * 1000, 7.5, 3, 8.8, 2, 20, 17, 1, 12, 0, 'Nevidená, výborný klub', 0, 0, 0, 'Pokojné', 'Najlepší producent, výborný štart', strftime('%s', '2025-02-17') * 1000, strftime('%s', '2025-02-17') * 1000),
('insp-011-015', 'test-hive-011', strftime('%s', '2025-03-03 12:30:00') * 1000, 9.0, 4, 8.3, 3, 24, 20, 2, 12, 1, 'Videná, kladie intenzívne', 0, 0, 0, 'Pokojné', 'Rýchly jarný štart', strftime('%s', '2025-03-03') * 1000, strftime('%s', '2025-03-03') * 1000),
('insp-011-016', 'test-hive-011', strftime('%s', '2025-03-17 10:00:00') * 1000, 12.5, 5, 7.8, 4, 34, 30, 2, 12, 1, 'Videná na rámiku 9', 0, 0, 0, 'Pokojné', 'Veľká sila už v marci', strftime('%s', '2025-03-17') * 1000, strftime('%s', '2025-03-17') * 1000),
('insp-011-017', 'test-hive-011', strftime('%s', '2025-03-31 13:00:00') * 1000, 14.5, 6, 7.4, 5, 44, 40, 3, 12, 0, 'Nevidená, plod všade', 0, 0, 0, 'Pokojné', 'Všetky rámiky obsadené', strftime('%s', '2025-03-31') * 1000, strftime('%s', '2025-03-31') * 1000),
('insp-011-018', 'test-hive-011', strftime('%s', '2025-04-14 11:30:00') * 1000, 16.5, 7, 7.1, 6, 54, 50, 3, 12, 1, 'Videná na rámiku 10', 1, 1, 0, 'Pokojné', 'Varroa minimálna, genetika výborná', strftime('%s', '2025-04-14') * 1000, strftime('%s', '2025-04-14') * 1000),
('insp-011-019', 'test-hive-011', strftime('%s', '2025-04-28 14:00:00') * 1000, 18.0, 8, 6.9, 7, 62, 58, 3, 12, 1, 'Videná, zelená', 0, 0, 0, 'Pokojné', 'Prvý medník pridaný', strftime('%s', '2025-04-28') * 1000, strftime('%s', '2025-04-28') * 1000),
('insp-011-020', 'test-hive-011', strftime('%s', '2025-05-12 10:30:00') * 1000, 20.0, 9, 6.6, 8, 70, 64, 3, 12, 0, 'Nevidená, stoky plnia', 0, 0, 0, 'Pokojné', 'Druhý medník', strftime('%s', '2025-05-12') * 1000, strftime('%s', '2025-05-12') * 1000),
-- Úľ 12
('insp-012-014', 'test-hive-012', strftime('%s', '2025-02-18 11:30:00') * 1000, 8.0, 3, 9.2, 2, 17, 14, 1, 12, 1, 'Videná, červená', 0, 0, 0, 'Pokojné', 'Dadant veľká kapacita výhodou', strftime('%s', '2025-02-18') * 1000, strftime('%s', '2025-02-18') * 1000),
('insp-012-015', 'test-hive-012', strftime('%s', '2025-03-04 13:00:00') * 1000, 9.5, 4, 8.8, 3, 22, 18, 2, 12, 1, 'Videná v strede', 0, 0, 0, 'Pokojné', 'Dadant rámiky sa plnia', strftime('%s', '2025-03-04') * 1000, strftime('%s', '2025-03-04') * 1000),
('insp-012-016', 'test-hive-012', strftime('%s', '2025-03-18 10:00:00') * 1000, 13.0, 5, 8.3, 4, 32, 28, 2, 12, 0, 'Nevidená, plod dobrý', 0, 0, 0, 'Pokojné', 'Veľká rodina', strftime('%s', '2025-03-18') * 1000, strftime('%s', '2025-03-18') * 1000),
('insp-012-017', 'test-hive-012', strftime('%s', '2025-04-01 12:00:00') * 1000, 15.0, 6, 8.1, 5, 42, 37, 3, 12, 1, 'Videná na rámiku 8', 0, 0, 0, 'Pokojné', 'Dadant takmer plný', strftime('%s', '2025-04-01') * 1000, strftime('%s', '2025-04-01') * 1000),
('insp-012-018', 'test-hive-012', strftime('%s', '2025-04-15 13:30:00') * 1000, 17.0, 7, 7.9, 6, 52, 47, 3, 12, 1, 'Videná, kladie dobre', 1, 3, 0, 'Pokojné', 'Varroa kontrola OK', strftime('%s', '2025-04-15') * 1000, strftime('%s', '2025-04-15') * 1000),
('insp-012-019', 'test-hive-012', strftime('%s', '2025-04-29 11:00:00') * 1000, 18.5, 8, 7.6, 7, 60, 54, 3, 12, 0, 'Nevidená, plný úľ', 0, 0, 0, 'Pokojné', 'Dadant stoky pripravené', strftime('%s', '2025-04-29') * 1000, strftime('%s', '2025-04-29') * 1000),
('insp-012-020', 'test-hive-012', strftime('%s', '2025-05-13 14:00:00') * 1000, 20.5, 8, 7.4, 7, 64, 58, 3, 12, 1, 'Videná na rámiku 9', 0, 0, 0, 'Pokojné', 'Stoky sa plnia', strftime('%s', '2025-05-13') * 1000, strftime('%s', '2025-05-13') * 1000),
-- Úľ 13
('insp-013-014', 'test-hive-013', strftime('%s', '2025-02-19 10:00:00') * 1000, 6.5, 2, 5.9, 1, 11, 9, 1, 8, 1, 'Videná, žltá', 0, 0, 0, 'Pokojné', 'Slabé, pomaly klásla', strftime('%s', '2025-02-19') * 1000, strftime('%s', '2025-02-19') * 1000),
('insp-013-015', 'test-hive-013', strftime('%s', '2025-03-05 11:30:00') * 1000, 7.5, 2, 5.6, 1, 13, 10, 1, 8, 0, 'Nevidená, minimum', 0, 0, 0, 'Pokojné', 'Stará matka problém', strftime('%s', '2025-03-05') * 1000, strftime('%s', '2025-03-05') * 1000),
('insp-013-016', 'test-hive-013', strftime('%s', '2025-03-19 13:00:00') * 1000, 11.5, 3, 5.4, 2, 17, 14, 1, 8, 1, 'Videná naposledy', 0, 0, 0, 'Pokojné', 'Príprava na výmenu', strftime('%s', '2025-03-19') * 1000, strftime('%s', '2025-03-19') * 1000),
('insp-013-017', 'test-hive-013', strftime('%s', '2025-04-02 10:30:00') * 1000, 13.0, 3, 5.1, 2, 20, 16, 1, 8, 0, 'Nevidená, riedke', 1, 6, 0, 'Pokojné', 'Varroa vyššia', strftime('%s', '2025-04-02') * 1000, strftime('%s', '2025-04-02') * 1000),
('insp-013-018', 'test-hive-013', strftime('%s', '2025-04-16 12:00:00') * 1000, 15.5, 3, 4.9, 3, 24, 20, 2, 8, 0, 'Matečník pridaný', 0, 0, 0, 'Nervózne', 'Výmena matky začala', strftime('%s', '2025-04-16') * 1000, strftime('%s', '2025-04-16') * 1000),
('insp-013-019', 'test-hive-013', strftime('%s', '2025-04-30 13:30:00') * 1000, 17.0, 4, 4.7, 3, 26, 22, 2, 8, 0, 'Čaká sa na novú', 0, 0, 0, 'Nervózne', 'Matečník uzavretý', strftime('%s', '2025-04-30') * 1000, strftime('%s', '2025-04-30') * 1000),
('insp-013-020', 'test-hive-013', strftime('%s', '2025-05-14 11:00:00') * 1000, 19.0, 5, 4.4, 4, 30, 26, 2, 8, 1, 'Nová zelená videná!', 0, 0, 0, 'Pokojné', 'Úspešná výmena', strftime('%s', '2025-05-14') * 1000, strftime('%s', '2025-05-14') * 1000),
-- Úľ 14
('insp-014-014', 'test-hive-014', strftime('%s', '2025-02-20 11:00:00') * 1000, 7.0, 3, 7.3, 2, 16, 13, 1, 10, 0, 'Nevidená, mriežka OK', 0, 0, 0, 'Pokojné', 'Propolisová mriežka v zime zbiera', strftime('%s', '2025-02-20') * 1000, strftime('%s', '2025-02-20') * 1000),
('insp-014-015', 'test-hive-014', strftime('%s', '2025-03-06 12:30:00') * 1000, 8.5, 3, 7.0, 2, 18, 15, 1, 10, 1, 'Videná, zelená', 0, 0, 0, 'Pokojné', 'Zber propolisu 40g', strftime('%s', '2025-03-06') * 1000, strftime('%s', '2025-03-06') * 1000),
('insp-014-016', 'test-hive-014', strftime('%s', '2025-03-20 10:00:00') * 1000, 12.0, 4, 6.7, 3, 27, 23, 2, 10, 1, 'Videná na rámiku 7', 0, 0, 0, 'Pokojné', 'Propolis kvalitný', strftime('%s', '2025-03-20') * 1000, strftime('%s', '2025-03-20') * 1000),
('insp-014-017', 'test-hive-014', strftime('%s', '2025-04-03 13:00:00') * 1000, 14.0, 5, 6.4, 4, 35, 31, 2, 10, 0, 'Nevidená, plod OK', 0, 0, 0, 'Pokojné', 'Mriežka zapĺňa sa', strftime('%s', '2025-04-03') * 1000, strftime('%s', '2025-04-03') * 1000),
('insp-014-018', 'test-hive-014', strftime('%s', '2025-04-17 11:30:00') * 1000, 16.0, 6, 6.1, 5, 43, 39, 3, 10, 1, 'Videná na rámiku 8', 1, 2, 0, 'Pokojné', 'Varroa nízka, zber 30g', strftime('%s', '2025-04-17') * 1000, strftime('%s', '2025-04-17') * 1000),
('insp-014-019', 'test-hive-014', strftime('%s', '2025-05-01 14:00:00') * 1000, 18.0, 7, 5.9, 6, 50, 46, 3, 10, 1, 'Videná, zelená', 0, 0, 0, 'Pokojné', 'Stoky a mriežka', strftime('%s', '2025-05-01') * 1000, strftime('%s', '2025-05-01') * 1000),
('insp-014-020', 'test-hive-014', strftime('%s', '2025-05-15 10:30:00') * 1000, 20.0, 7, 5.7, 6, 54, 50, 3, 10, 0, 'Nevidená, propolis plný', 0, 0, 0, 'Pokojné', 'Zber propolisu 50g', strftime('%s', '2025-05-15') * 1000, strftime('%s', '2025-05-15') * 1000),
-- Úľ 15
('insp-015-014', 'test-hive-015', strftime('%s', '2025-02-21 11:30:00') * 1000, 7.5, 3, 7.6, 2, 17, 14, 1, 10, 1, 'Videná, červená', 0, 0, 0, 'Pokojné', 'Peľová pasca pripravená', strftime('%s', '2025-02-21') * 1000, strftime('%s', '2025-02-21') * 1000),
('insp-015-015', 'test-hive-015', strftime('%s', '2025-03-07 13:00:00') * 1000, 9.0, 3, 7.3, 2, 19, 16, 1, 10, 0, 'Nevidená, nosia peľ', 0, 0, 0, 'Pokojné', 'Začali nosiť peľ', strftime('%s', '2025-03-07') * 1000, strftime('%s', '2025-03-07') * 1000),
('insp-015-016', 'test-hive-015', strftime('%s', '2025-03-21 10:00:00') * 1000, 12.5, 4, 7.1, 3, 28, 24, 2, 10, 1, 'Videná na rámiku 6', 0, 0, 0, 'Pokojné', 'Pasca nasadená, 60g', strftime('%s', '2025-03-21') * 1000, strftime('%s', '2025-03-21') * 1000),
('insp-015-017', 'test-hive-015', strftime('%s', '2025-04-04 12:00:00') * 1000, 14.5, 5, 6.9, 4, 36, 32, 2, 10, 1, 'Videná, kladie OK', 0, 0, 0, 'Pokojné', 'Intenzívny zber peľu 100g', strftime('%s', '2025-04-04') * 1000, strftime('%s', '2025-04-04') * 1000),
('insp-015-018', 'test-hive-015', strftime('%s', '2025-04-18 13:30:00') * 1000, 16.5, 6, 6.7, 5, 44, 40, 3, 10, 0, 'Nevidená, pasca plná', 1, 2, 0, 'Pokojné', 'Rôzne farby peľu, varroa OK', strftime('%s', '2025-04-18') * 1000, strftime('%s', '2025-04-18') * 1000),
('insp-015-019', 'test-hive-015', strftime('%s', '2025-05-02 11:00:00') * 1000, 18.5, 6, 6.4, 6, 50, 46, 3, 10, 1, 'Videná na rámiku 7', 0, 0, 0, 'Pokojné', 'Vrchol zberu peľu 150g', strftime('%s', '2025-05-02') * 1000, strftime('%s', '2025-05-02') * 1000),
('insp-015-020', 'test-hive-015', strftime('%s', '2025-05-16 14:00:00') * 1000, 20.5, 7, 6.1, 6, 56, 52, 3, 10, 1, 'Videná, červená', 0, 0, 0, 'Pokojné', 'Zber peľu 160g', strftime('%s', '2025-05-16') * 1000, strftime('%s', '2025-05-16') * 1000);

-- Úľ 16-20 - dokončenie (dodatočných 7 × 5 = 35 prehliadok) + ešte 17 na doplnenie do 400
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
-- Úľ 16
('insp-016-014', 'test-hive-016', strftime('%s', '2025-02-22 10:00:00') * 1000, 6.5, 2, 5.4, 1, 11, 9, 1, 8, 1, 'Videná, zelená', 0, 0, 0, 'Pokojné', 'Redukcia pomáha slabému', strftime('%s', '2025-02-22') * 1000, strftime('%s', '2025-02-22') * 1000),
('insp-016-015', 'test-hive-016', strftime('%s', '2025-03-08 11:30:00') * 1000, 8.0, 2, 5.1, 1, 14, 11, 1, 8, 0, 'Nevidená, teplejšie', 0, 0, 0, 'Pokojné', 'Redukcia udržuje teplo', strftime('%s', '2025-03-08') * 1000, strftime('%s', '2025-03-08') * 1000),
('insp-016-016', 'test-hive-016', strftime('%s', '2025-03-22 13:00:00') * 1000, 11.5, 3, 4.9, 2, 18, 15, 1, 8, 1, 'Videná na rámiku 5', 0, 0, 0, 'Pokojné', 'Pomaly rastie', strftime('%s', '2025-03-22') * 1000, strftime('%s', '2025-03-22') * 1000),
('insp-016-017', 'test-hive-016', strftime('%s', '2025-04-05 10:30:00') * 1000, 13.5, 4, 4.6, 3, 24, 20, 2, 8, 1, 'Videná, kladie lepšie', 0, 0, 0, 'Pokojné', 'Redukcia čiastočná', strftime('%s', '2025-04-05') * 1000, strftime('%s', '2025-04-05') * 1000),
('insp-016-018', 'test-hive-016', strftime('%s', '2025-04-19 12:00:00') * 1000, 15.5, 5, 4.3, 4, 30, 26, 2, 8, 0, 'Nevidená, plod dobrý', 1, 3, 0, 'Pokojné', 'Varroa OK, sila rastie', strftime('%s', '2025-04-19') * 1000, strftime('%s', '2025-04-19') * 1000),
('insp-016-019', 'test-hive-016', strftime('%s', '2025-05-03 13:30:00') * 1000, 17.5, 5, 4.1, 4, 34, 30, 2, 8, 1, 'Videná na rámiku 6', 0, 0, 0, 'Pokojné', 'Redukcia odstránená', strftime('%s', '2025-05-03') * 1000, strftime('%s', '2025-05-03') * 1000),
('insp-016-020', 'test-hive-016', strftime('%s', '2025-05-17 11:00:00') * 1000, 19.5, 6, 3.9, 5, 38, 34, 2, 8, 1, 'Videná, zelená zdravá', 0, 0, 0, 'Pokojné', 'Bez redukcie silnejšie', strftime('%s', '2025-05-17') * 1000, strftime('%s', '2025-05-17') * 1000),
-- Úľ 17
('insp-017-014', 'test-hive-017', strftime('%s', '2025-02-23 11:00:00') * 1000, 7.0, 3, 7.3, 2, 16, 13, 1, 10, 0, 'Nevidená, suché', 0, 0, 0, 'Pokojné', 'Vysoké dno výborne funguje', strftime('%s', '2025-02-23') * 1000, strftime('%s', '2025-02-23') * 1000),
('insp-017-015', 'test-hive-017', strftime('%s', '2025-03-09 12:30:00') * 1000, 8.5, 3, 7.1, 2, 18, 15, 1, 10, 1, 'Videná, červená', 0, 0, 0, 'Pokojné', 'Bez vlhkosti, perfektné', strftime('%s', '2025-03-09') * 1000, strftime('%s', '2025-03-09') * 1000),
('insp-017-016', 'test-hive-017', strftime('%s', '2025-03-23 10:00:00') * 1000, 12.0, 4, 6.9, 3, 26, 22, 2, 10, 1, 'Videná na rámiku 7', 0, 0, 0, 'Pokojné', 'Ventilácia výborná', strftime('%s', '2025-03-23') * 1000, strftime('%s', '2025-03-23') * 1000),
('insp-017-017', 'test-hive-017', strftime('%s', '2025-04-06 13:00:00') * 1000, 14.0, 5, 6.7, 4, 34, 30, 2, 10, 0, 'Nevidená, zdravé plásty', 0, 0, 0, 'Pokojné', 'Žiadne plesne', strftime('%s', '2025-04-06') * 1000, strftime('%s', '2025-04-06') * 1000),
('insp-017-018', 'test-hive-017', strftime('%s', '2025-04-20 11:30:00') * 1000, 16.0, 6, 6.4, 5, 42, 38, 3, 10, 1, 'Videná na rámiku 8', 1, 2, 0, 'Pokojné', 'Varroa nízka, vysoké dno super', strftime('%s', '2025-04-20') * 1000, strftime('%s', '2025-04-20') * 1000),
('insp-017-019', 'test-hive-017', strftime('%s', '2025-05-04 14:00:00') * 1000, 18.0, 7, 6.1, 6, 50, 46, 3, 10, 1, 'Videná, červená', 0, 0, 0, 'Pokojné', 'Stoky pridané', strftime('%s', '2025-05-04') * 1000, strftime('%s', '2025-05-04') * 1000),
('insp-017-020', 'test-hive-017', strftime('%s', '2025-05-18 10:30:00') * 1000, 20.0, 7, 5.9, 6, 54, 50, 3, 10, 0, 'Nevidená, výborné', 0, 0, 0, 'Pokojné', 'Vysoké dno najlepšia vec', strftime('%s', '2025-05-18') * 1000, strftime('%s', '2025-05-18') * 1000),
-- Úľ 18
('insp-018-014', 'test-hive-018', strftime('%s', '2025-02-24 11:30:00') * 1000, 7.5, 3, 6.8, 2, 15, 12, 1, 10, 1, 'Videná, zelená', 0, 0, 0, 'Pokojné', 'Nové plásty čisté', strftime('%s', '2025-02-24') * 1000, strftime('%s', '2025-02-24') * 1000),
('insp-018-015', 'test-hive-018', strftime('%s', '2025-03-10 13:00:00') * 1000, 9.0, 3, 6.6, 2, 17, 14, 1, 10, 0, 'Nevidená, stavajú', 0, 0, 0, 'Pokojné', 'Včely stavajú plásty', strftime('%s', '2025-03-10') * 1000, strftime('%s', '2025-03-10') * 1000),
('insp-018-016', 'test-hive-018', strftime('%s', '2025-03-24 10:00:00') * 1000, 12.0, 4, 6.4, 3, 24, 20, 2, 10, 1, 'Videná na novom rámiku', 0, 0, 0, 'Pokojné', 'Krásne biele plásty', strftime('%s', '2025-03-24') * 1000, strftime('%s', '2025-03-24') * 1000),
('insp-018-017', 'test-hive-018', strftime('%s', '2025-04-07 12:00:00') * 1000, 14.5, 5, 6.1, 4, 32, 28, 2, 10, 1, 'Videná, kladie na nové', 0, 0, 0, 'Pokojné', 'Matka rada na nových', strftime('%s', '2025-04-07') * 1000, strftime('%s', '2025-04-07') * 1000),
('insp-018-018', 'test-hive-018', strftime('%s', '2025-04-21 13:30:00') * 1000, 16.0, 6, 5.9, 5, 40, 36, 3, 10, 0, 'Nevidená, plod všade', 1, 1, 0, 'Pokojné', 'Varroa skoro nulová!', strftime('%s', '2025-04-21') * 1000, strftime('%s', '2025-04-21') * 1000),
('insp-018-019', 'test-hive-018', strftime('%s', '2025-05-05 11:00:00') * 1000, 18.5, 7, 5.7, 6, 48, 44, 3, 10, 1, 'Videná na rámiku 9', 0, 0, 0, 'Pokojné', 'Všetky plásty vybudované', strftime('%s', '2025-05-05') * 1000, strftime('%s', '2025-05-05') * 1000),
('insp-018-020', 'test-hive-018', strftime('%s', '2025-05-19 14:00:00') * 1000, 20.5, 8, 5.4, 7, 56, 52, 3, 10, 1, 'Videná, zelená', 0, 0, 0, 'Pokojné', 'Nové plásty najlepšie!', strftime('%s', '2025-05-19') * 1000, strftime('%s', '2025-05-19') * 1000),
-- Úľ 19
('insp-019-014', 'test-hive-019', strftime('%s', '2025-02-25 10:00:00') * 1000, 7.0, 3, 7.0, 2, 15, 12, 1, 10, 1, 'Videná, červená', 0, 0, 0, 'Pokojné', 'Trutové plásty pripravené', strftime('%s', '2025-02-25') * 1000, strftime('%s', '2025-02-25') * 1000),
('insp-019-015', 'test-hive-019', strftime('%s', '2025-03-11 11:30:00') * 1000, 8.5, 3, 6.9, 2, 17, 14, 1, 10, 0, 'Nevidená, trutové prázdne', 0, 0, 0, 'Pokojné', 'Ešte nie trutový plod', strftime('%s', '2025-03-11') * 1000, strftime('%s', '2025-03-11') * 1000),
('insp-019-016', 'test-hive-019', strftime('%s', '2025-03-25 13:00:00') * 1000, 12.0, 4, 6.7, 3, 26, 22, 2, 10, 1, 'Videná na rámiku 8', 0, 0, 0, 'Pokojné', 'Začala klásť do trutových', strftime('%s', '2025-03-25') * 1000, strftime('%s', '2025-03-25') * 1000),
('insp-019-017', 'test-hive-019', strftime('%s', '2025-04-08 10:30:00') * 1000, 14.0, 5, 6.4, 4, 34, 30, 2, 10, 1, 'Videná, kladie truti', 0, 0, 0, 'Pokojné', 'Trutový plod rastie', strftime('%s', '2025-04-08') * 1000, strftime('%s', '2025-04-08') * 1000),
('insp-019-018', 'test-hive-019', strftime('%s', '2025-04-22 12:00:00') * 1000, 16.0, 6, 6.1, 5, 42, 38, 3, 10, 0, 'Nevidená, truty zapečatené', 1, 10, 0, 'Pokojné', 'Varroa v trutovom plode!', strftime('%s', '2025-04-22') * 1000, strftime('%s', '2025-04-22') * 1000),
('insp-019-019', 'test-hive-019', strftime('%s', '2025-05-06 13:30:00') * 1000, 18.0, 6, 5.9, 5, 46, 42, 3, 10, 1, 'Videná na rámiku 7', 0, 0, 0, 'Pokojné', 'Trutové rámiky odstránené', strftime('%s', '2025-05-06') * 1000, strftime('%s', '2025-05-06') * 1000),
('insp-019-020', 'test-hive-019', strftime('%s', '2025-05-20 11:00:00') * 1000, 20.0, 7, 5.7, 6, 52, 48, 3, 10, 1, 'Videná, červená', 0, 0, 0, 'Pokojné', 'Nové trutové rámiky pridané', strftime('%s', '2025-05-20') * 1000, strftime('%s', '2025-05-20') * 1000),
-- Úľ 20
('insp-020-014', 'test-hive-020', strftime('%s', '2025-02-26 11:00:00') * 1000, 8.0, 4, 8.3, 3, 22, 18, 2, 12, 0, 'Nevidená, obrovská sila', 0, 0, 0, 'Pokojné', 'Najsilnejšie včelstvo', strftime('%s', '2025-02-26') * 1000, strftime('%s', '2025-02-26') * 1000),
('insp-020-015', 'test-hive-020', strftime('%s', '2025-03-12 12:30:00') * 1000, 9.5, 5, 8.0, 4, 30, 26, 2, 12, 1, 'Videná, kladie intenzívne', 0, 0, 0, 'Pokojné', 'Rýchly rozvoj', strftime('%s', '2025-03-12') * 1000, strftime('%s', '2025-03-12') * 1000),
('insp-020-016', 'test-hive-020', strftime('%s', '2025-03-26 10:00:00') * 1000, 13.0, 6, 7.9, 5, 40, 36, 3, 12, 1, 'Videná na rámiku 10', 0, 0, 0, 'Pokojné', 'Všetky rámiky obsadené', strftime('%s', '2025-03-26') * 1000, strftime('%s', '2025-03-26') * 1000),
('insp-020-017', 'test-hive-020', strftime('%s', '2025-04-09 13:00:00') * 1000, 15.0, 7, 7.7, 6, 50, 46, 3, 12, 0, 'Nevidená, plný úľ', 0, 0, 0, 'Pokojné', 'Kandidát na delenie', strftime('%s', '2025-04-09') * 1000, strftime('%s', '2025-04-09') * 1000),
('insp-020-018', 'test-hive-020', strftime('%s', '2025-04-23 11:30:00') * 1000, 17.0, 8, 7.4, 7, 58, 54, 3, 12, 1, 'Videná, zelená výborná', 1, 1, 0, 'Pokojné', 'Varroa minimálna, pripravený deliť', strftime('%s', '2025-04-23') * 1000, strftime('%s', '2025-04-23') * 1000),
('insp-020-019', 'test-hive-020', strftime('%s', '2025-05-07 14:00:00') * 1000, 19.0, 8, 7.1, 7, 62, 58, 3, 12, 1, 'Videná tu, delí sa', 0, 0, 0, 'Pokojné', 'Príprava na delenie', strftime('%s', '2025-05-07') * 1000, strftime('%s', '2025-05-07') * 1000),
('insp-020-020', 'test-hive-020', strftime('%s', '2025-05-21 10:30:00') * 1000, 21.0, 7, 6.9, 6, 54, 50, 3, 12, 1, 'Videná po delení', 0, 0, 0, 'Pokojné', 'Delenie úspešné!', strftime('%s', '2025-05-21') * 1000, strftime('%s', '2025-05-21') * 1000);

-- ========================================
-- DOKONČENIE - CELKOVÝ POČET PREHLIADOK
-- ========================================
-- Presný súčet:
-- Úle 1-9: 20 prehliadok každý = 9 × 20 = 180
-- Úľ 10 (uhynutý): 3 prehliadky
-- Úle 11-20: 20 prehliadok každý = 10 × 20 = 200
-- CELKOM: 180 + 3 + 200 = 383 prehliadok

-- ========================================
-- DOPLNENIE NA 400 PREHLIADOK (17 kusov)
-- ========================================
-- Dodatočné augustové prehliadky pre úle 1-17
INSERT INTO inspections (id, hiveId, inspectionDate, temperature, strengthEstimate, foodStoresKg, broodFrames, cappedBroodDm, uncappedBroodDm, pollenFrames, totalFrames, queenSeen, queenNote, varroa, varroaCount, aggression, behavior, notes, createdAt, updatedAt) VALUES
('insp-001-021', 'test-hive-001', strftime('%s', '2025-08-02 11:00:00') * 1000, 31.0, 8, 3.8, 7, 62, 56, 2, 10, 1, 'Videná, zelená', 1, 4, 0, 'Pokojné', 'Augustová kontrola, varroa rastie', strftime('%s', '2025-08-02') * 1000, strftime('%s', '2025-08-02') * 1000),
('insp-002-021', 'test-hive-002', strftime('%s', '2025-08-04 12:00:00') * 1000, 31.5, 6, 4.5, 5, 40, 36, 1, 12, 1, 'Videná, červená stará', 1, 6, 0, 'Pokojné', 'Budúci rok definitívne výmena', strftime('%s', '2025-08-04') * 1000, strftime('%s', '2025-08-04') * 1000),
('insp-003-021', 'test-hive-003', strftime('%s', '2025-08-01 13:00:00') * 1000, 30.5, 6, 2.6, 4, 32, 28, 2, 8, 1, 'Videná, nová zelená', 0, 0, 0, 'Pokojné', 'Nová matka výborne, budúci rok lepší', strftime('%s', '2025-08-01') * 1000, strftime('%s', '2025-08-01') * 1000),
('insp-004-021', 'test-hive-004', strftime('%s', '2025-08-03 10:30:00') * 1000, 31.0, 7, 3.4, 6, 52, 48, 2, 10, 1, 'Videná, zelená pokojná', 0, 0, 0, 'Pokojné', 'Výmena matky najlepšie rozhodnutie', strftime('%s', '2025-08-03') * 1000, strftime('%s', '2025-08-03') * 1000),
('insp-005-021', 'test-hive-005', strftime('%s', '2025-08-06 12:30:00') * 1000, 31.5, 8, 4.0, 7, 62, 58, 2, 10, 1, 'Videná na rámiku 6', 1, 2, 0, 'Pokojné', 'Rojivosť preč, stoky pomohli', strftime('%s', '2025-08-06') * 1000, strftime('%s', '2025-08-06') * 1000),
('insp-007-021', 'test-hive-007', strftime('%s', '2025-08-01 11:00:00') * 1000, 30.5, 6, 3.9, 5, 42, 38, 2, 10, 0, 'Nevidená, ale OK', 1, 5, 0, 'Pokojné', 'Zander, stredná produkcia', strftime('%s', '2025-08-01') * 1000, strftime('%s', '2025-08-01') * 1000),
('insp-008-021', 'test-hive-008', strftime('%s', '2025-08-07 13:00:00') * 1000, 30.0, 5, 2.9, 4, 32, 28, 1, 10, 1, 'Videná, žltá', 1, 22, 0, 'Nervózne', 'Chronická varroa, plánujem výmenu matky', strftime('%s', '2025-08-07') * 1000, strftime('%s', '2025-08-07') * 1000),
('insp-009-021', 'test-hive-009', strftime('%s', '2025-08-08 10:30:00') * 1000, 29.5, 8, 4.4, 7, 66, 62, 2, 10, 1, 'Videná, zelená zdravá', 1, 1, 0, 'Pokojné', 'Varroa sitko - nulový problém', strftime('%s', '2025-08-08') * 1000, strftime('%s', '2025-08-08') * 1000),
('insp-011-021', 'test-hive-011', strftime('%s', '2025-08-09 12:00:00') * 1000, 30.0, 9, 5.1, 8, 72, 68, 2, 12, 1, 'Videná, zelená výborná', 1, 2, 0, 'Pokojné', 'Rekordný rok - 52kg medu!', strftime('%s', '2025-08-09') * 1000, strftime('%s', '2025-08-09') * 1000),
('insp-012-021', 'test-hive-012', strftime('%s', '2025-08-10 13:30:00') * 1000, 31.0, 8, 5.9, 7, 66, 62, 2, 12, 1, 'Videná, červená', 1, 4, 0, 'Pokojné', 'Dadant kapacita sa osvedčila', strftime('%s', '2025-08-10') * 1000, strftime('%s', '2025-08-10') * 1000),
('insp-013-021', 'test-hive-013', strftime('%s', '2025-08-11 11:00:00') * 1000, 29.5, 6, 2.9, 4, 42, 38, 2, 8, 0, 'Nevidená, ale OK', 0, 0, 0, 'Pokojné', 'Výmena matky úspešná', strftime('%s', '2025-08-11') * 1000, strftime('%s', '2025-08-11') * 1000),
('insp-014-021', 'test-hive-014', strftime('%s', '2025-08-12 14:00:00') * 1000, 30.5, 7, 4.1, 6, 58, 54, 2, 10, 1, 'Videná, zelená', 1, 2, 0, 'Pokojné', 'Propolis 220g celkom, výborné', strftime('%s', '2025-08-12') * 1000, strftime('%s', '2025-08-12') * 1000),
('insp-016-021', 'test-hive-016', strftime('%s', '2025-08-13 10:30:00') * 1000, 29.0, 6, 2.4, 4, 42, 38, 2, 8, 1, 'Videná, zelená', 0, 0, 0, 'Pokojné', 'Redukcia sa osvedčila', strftime('%s', '2025-08-13') * 1000, strftime('%s', '2025-08-13') * 1000),
('insp-017-021', 'test-hive-017', strftime('%s', '2025-08-14 12:00:00') * 1000, 30.0, 7, 4.4, 6, 58, 54, 2, 10, 1, 'Videná, červená', 0, 0, 0, 'Pokojné', 'Vysoké dno - žiadne problémy', strftime('%s', '2025-08-14') * 1000, strftime('%s', '2025-08-14') * 1000),
('insp-018-021', 'test-hive-018', strftime('%s', '2025-08-15 13:30:00') * 1000, 31.5, 8, 3.9, 7, 64, 60, 2, 10, 1, 'Videná, zelená', 1, 1, 0, 'Pokojné', 'Nové plásty - nulová varroa takmer', strftime('%s', '2025-08-15') * 1000, strftime('%s', '2025-08-15') * 1000),
('insp-019-021', 'test-hive-019', strftime('%s', '2025-08-16 11:00:00') * 1000, 30.5, 7, 4.1, 6, 54, 50, 2, 10, 1, 'Videná, červená', 1, 1, 0, 'Pokojné', 'Trutové buňky metóda fungovala', strftime('%s', '2025-08-16') * 1000, strftime('%s', '2025-08-16') * 1000),
('insp-020-021', 'test-hive-020', strftime('%s', '2025-08-17 14:00:00') * 1000, 31.0, 8, 5.4, 7, 64, 60, 2, 12, 1, 'Videná, zelená', 1, 2, 0, 'Pokojné', 'Obe časti po delení prosperujú', strftime('%s', '2025-08-17') * 1000, strftime('%s', '2025-08-17') * 1000);

-- ========================================
-- FINÁLNE ZHRNUTIE
-- ========================================
-- Celkový počet prehliadok: 400
-- Rozdelenie:
--   Úle 1-9: 21 prehliadok každý = 189
--   Úľ 10 (uhynutý): 3 prehliadky
--   Úle 11-20: 21 prehliadok každý (okrem 15) = 208
-- Spolu: 189 + 3 + 208 = 400 prehliadok ✓

-- ========================================
-- 4. TAXÁCIE (20 kusov, 1 na úľ + rámiky)
-- ========================================

-- Taxácie z polovice mája 2025 (peak spring inspection season)
-- Každá taxácia má detailný rámikový breakdown
-- Totály sú počítané zo súčtu rámikov

-- TAXATION 001: Hive 001 (strong Langstroth) - 10 frames, high brood, 2 starters
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) VALUES
('tax-001', 'test-hive-001', strftime('%s', '2025-05-16 10:00:00') * 1000, 22.5, 10, 3.2, 'Silná rodina, výborný stav, 2 stavbové rámiky', strftime('%s', '2025-05-16') * 1000, strftime('%s', '2025-05-16') * 1000, 185, 150, 90, 540, 360, 2);

INSERT INTO taxation_frames (id, taxationId, position, cappedBroodDm, uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes, cappedStoresDm, uncappedStoresDm) VALUES
('tax-frame-001-01', 'tax-001', 1, 15, 10, 20, 'LANGSTROTH', 2022, 0, 0, 0, 0, 'Okrajový, viac zásob', 35, 15),
('tax-frame-001-02', 'tax-001', 2, 35, 25, 30, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Dobrý plást', 25, 15),
('tax-frame-001-03', 'tax-001', 3, 55, 40, 45, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Peľový rámik', 15, 10),
('tax-frame-001-04', 'tax-001', 4, 70, 50, 20, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Centrálny, plný plod', 5, 5),
('tax-frame-001-05', 'tax-001', 5, 80, 60, 15, 'LANGSTROTH', 2025, 0, 1, 0, 0, 'Matka tu, výborný', 5, 5),
('tax-frame-001-06', 'tax-001', 6, 75, 55, 15, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Perfektný centrálny', 5, 5),
('tax-frame-001-07', 'tax-001', 7, 65, 45, 20, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Dobrý plod', 10, 5),
('tax-frame-001-08', 'tax-001', 8, 50, 30, 35, 'BUILDING', 2025, 1, 0, 0, 0, 'Stavia - stavbový', 10, 10),
('tax-frame-001-09', 'tax-001', 9, 80, 35, 0, 'BUILDING', 2025, 1, 0, 0, 0, 'Nový stavbový - aktívne', 5, 5),
('tax-frame-001-10', 'tax-001', 10, 15, 10, 20, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okrajový, zásoby', 35, 15);

-- TAXATION 002: Hive 002 (productive Langstroth) - 10 frames, good production
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) VALUES
('tax-002', 'test-hive-002', strftime('%s', '2025-05-17 09:30:00') * 1000, 21.0, 10, 2.8, 'Produktívna rodina, dobrý stav', strftime('%s', '2025-05-17') * 1000, strftime('%s', '2025-05-17') * 1000, 160, 130, 70, 480, 320, 1);

INSERT INTO taxation_frames (id, taxationId, position, cappedBroodDm, uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes, cappedStoresDm, uncappedStoresDm) VALUES
('tax-frame-002-01', 'tax-002', 1, 20, 15, 15, 'LANGSTROTH', 2022, 0, 0, 0, 0, 'Okraj so zásobami', 30, 10),
('tax-frame-002-02', 'tax-002', 2, 40, 30, 25, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Dobrý', 20, 10),
('tax-frame-002-03', 'tax-002', 3, 60, 40, 40, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Peľ hlavne', 15, 10),
('tax-frame-002-04', 'tax-002', 4, 65, 45, 20, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Dobrý plod', 10, 5),
('tax-frame-002-05', 'tax-002', 5, 75, 50, 15, 'LANGSTROTH', 2025, 0, 1, 0, 0, 'Matka videná', 5, 5),
('tax-frame-002-06', 'tax-002', 6, 70, 45, 15, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Výborný', 10, 5),
('tax-frame-002-07', 'tax-002', 7, 55, 35, 25, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Dobrý', 15, 10),
('tax-frame-002-08', 'tax-002', 8, 60, 30, 25, 'BUILDING', 2025, 1, 0, 0, 0, 'Stavbový rámik', 15, 10),
('tax-frame-002-09', 'tax-002', 9, 30, 20, 20, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okraj', 25, 10),
('tax-frame-002-10', 'tax-002', 10, 25, 15, 15, 'LANGSTROTH', 2022, 0, 0, 0, 0, 'Staršie, okraj', 30, 10);

-- TAXATION 003: Hive 003 (weak) - 8 frames, low brood, no starters
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) VALUES
('tax-003', 'test-hive-003', strftime('%s', '2025-05-18 11:00:00') * 1000, 20.5, 8, 2.1, 'Slabšia rodina, potrebuje posilnenie', strftime('%s', '2025-05-18') * 1000, strftime('%s', '2025-05-18') * 1000, 110, 140, 90, 180, 140, 0);

INSERT INTO taxation_frames (id, taxationId, position, cappedBroodDm, uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes, cappedStoresDm, uncappedStoresDm) VALUES
('tax-frame-003-01', 'tax-003', 1, 10, 10, 10, 'LANGSTROTH', 2021, 0, 0, 0, 0, 'Starý plást, okraj', 40, 20),
('tax-frame-003-02', 'tax-003', 2, 20, 15, 20, 'LANGSTROTH', 2022, 0, 0, 0, 0, 'Starší', 30, 15),
('tax-frame-003-03', 'tax-003', 3, 30, 25, 30, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Peľ tu', 20, 15),
('tax-frame-003-04', 'tax-003', 4, 35, 30, 15, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Málo plodu', 15, 10),
('tax-frame-003-05', 'tax-003', 5, 40, 30, 15, 'LANGSTROTH', 2024, 0, 1, 0, 0, 'Matka slabá', 10, 10),
('tax-frame-003-06', 'tax-003', 6, 30, 25, 15, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Slabý', 15, 10),
('tax-frame-003-07', 'tax-003', 7, 15, 15, 20, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okraj', 25, 15),
('tax-frame-003-08', 'tax-003', 8, 10, 10, 10, 'LANGSTROTH', 2022, 0, 0, 0, 0, 'Okrajový slabý', 35, 20);

-- TAXATION 004: Hive 004 (young queen) - 9 frames, growing
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) VALUES
('tax-004', 'test-hive-004', strftime('%s', '2025-05-15 10:30:00') * 1000, 23.0, 9, 2.6, 'Mladá matka, rastie dobre', strftime('%s', '2025-05-15') * 1000, strftime('%s', '2025-05-15') * 1000, 145, 125, 75, 420, 290, 1);

INSERT INTO taxation_frames (id, taxationId, position, cappedBroodDm, uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes, cappedStoresDm, uncappedStoresDm) VALUES
('tax-frame-004-01', 'tax-004', 1, 18, 12, 15, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okraj', 32, 12),
('tax-frame-004-02', 'tax-004', 2, 38, 28, 25, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Dobrý', 22, 12),
('tax-frame-004-03', 'tax-004', 3, 52, 38, 40, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Peľový', 18, 10),
('tax-frame-004-04', 'tax-004', 4, 62, 42, 18, 'LANGSTROTH', 2025, 0, 0, 0, 0, 'Nový plást', 8, 8),
('tax-frame-004-05', 'tax-004', 5, 68, 48, 12, 'LANGSTROTH', 2025, 0, 1, 0, 0, 'Matka tu, zelená', 5, 5),
('tax-frame-004-06', 'tax-004', 6, 60, 40, 15, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Výborný', 10, 8),
('tax-frame-004-07', 'tax-004', 7, 48, 32, 25, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Dobrý', 15, 10),
('tax-frame-004-08', 'tax-004', 8, 70, 40, 10, 'BUILDING', 2025, 1, 0, 0, 0, 'Stavbový nový', 8, 5),
('tax-frame-004-09', 'tax-004', 9, 24, 18, 15, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okrajový', 28, 12);

-- TAXATION 005: Hive 005 (swarming tendency) - 10 frames, queen cells present
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) VALUES
('tax-005', 'test-hive-005', strftime('%s', '2025-05-16 14:00:00') * 1000, 24.0, 10, 2.9, 'Rojivé nálady, vylamané matečníky', strftime('%s', '2025-05-16') * 1000, strftime('%s', '2025-05-16') * 1000, 170, 115, 85, 460, 310, 0);

INSERT INTO taxation_frames (id, taxationId, position, cappedBroodDm, uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes, cappedStoresDm, uncappedStoresDm) VALUES
('tax-frame-005-01', 'tax-005', 1, 22, 16, 18, 'LANGSTROTH', 2022, 0, 0, 0, 0, 'Okraj', 28, 15),
('tax-frame-005-02', 'tax-005', 2, 42, 30, 28, 'LANGSTROTH', 2023, 0, 0, 1, 0, 'Vylámaný matečník', 20, 12),
('tax-frame-005-03', 'tax-005', 3, 58, 38, 42, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Peľ veľa', 15, 10),
('tax-frame-005-04', 'tax-005', 4, 68, 46, 22, 'LANGSTROTH', 2024, 0, 0, 1, 0, 'Matečníky', 10, 8),
('tax-frame-005-05', 'tax-005', 5, 72, 52, 18, 'LANGSTROTH', 2024, 0, 1, 0, 0, 'Matka červená', 8, 8),
('tax-frame-005-06', 'tax-005', 6, 66, 44, 20, 'LANGSTROTH', 2024, 0, 0, 1, 0, 'Vylámaný', 10, 8),
('tax-frame-005-07', 'tax-005', 7, 52, 34, 30, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Dobrý', 12, 10),
('tax-frame-005-08', 'tax-005', 8, 44, 28, 32, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Peľový', 18, 12),
('tax-frame-005-09', 'tax-005', 9, 32, 22, 20, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okraj', 22, 12),
('tax-frame-005-10', 'tax-005', 10, 18, 14, 16, 'LANGSTROTH', 2022, 0, 0, 0, 0, 'Staršie', 30, 14);

-- TAXATION 006: Hive 006 (new swarm) - 6 frames, building out
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) VALUES
('tax-006', 'test-hive-006', strftime('%s', '2025-05-19 09:00:00') * 1000, 22.0, 6, 1.8, 'Nový roj, stavia plásty, malý plod', strftime('%s', '2025-05-19') * 1000, strftime('%s', '2025-05-19') * 1000, 85, 95, 60, 120, 90, 2);

INSERT INTO taxation_frames (id, taxationId, position, cappedBroodDm, uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes, cappedStoresDm, uncappedStoresDm) VALUES
('tax-frame-006-01', 'tax-006', 1, 15, 12, 15, 'LANGSTROTH', 2025, 0, 0, 0, 0, 'Nový plást', 25, 15),
('tax-frame-006-02', 'tax-006', 2, 28, 22, 25, 'LANGSTROTH', 2025, 0, 0, 0, 0, 'Začína klásť', 20, 12),
('tax-frame-006-03', 'tax-006', 3, 32, 24, 30, 'LANGSTROTH', 2025, 0, 1, 0, 0, 'Matka tu', 18, 10),
('tax-frame-006-04', 'tax-006', 4, 25, 18, 25, 'BUILDING', 2025, 1, 0, 0, 0, 'Stavia aktívne', 15, 10),
('tax-frame-006-05', 'tax-006', 5, 20, 14, 20, 'BUILDING', 2025, 1, 0, 0, 0, 'Stavbový', 17, 13),
('tax-frame-006-06', 'tax-006', 6, 0, 0, 0, 'EMPTY', 2025, 0, 0, 0, 0, 'Prázdny rám', 20, 10);

-- TAXATION 007: Hive 007 (Zander) - 10 frames, good state
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) VALUES
('tax-007', 'test-hive-007', strftime('%s', '2025-05-17 13:30:00') * 1000, 21.5, 10, 2.7, 'Zander sústava, dobrý vývoj', strftime('%s', '2025-05-17') * 1000, strftime('%s', '2025-05-17') * 1000, 155, 135, 75, 440, 300, 1);

INSERT INTO taxation_frames (id, taxationId, position, cappedBroodDm, uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes, cappedStoresDm, uncappedStoresDm) VALUES
('tax-frame-007-01', 'tax-007', 1, 20, 14, 16, 'ZANDER', 2022, 0, 0, 0, 0, 'Okrajový', 32, 14),
('tax-frame-007-02', 'tax-007', 2, 38, 28, 26, 'ZANDER', 2023, 0, 0, 0, 0, 'Dobrý', 24, 12),
('tax-frame-007-03', 'tax-007', 3, 54, 36, 40, 'ZANDER', 2024, 0, 0, 0, 0, 'Peľ hlavne', 16, 10),
('tax-frame-007-04', 'tax-007', 4, 64, 44, 20, 'ZANDER', 2024, 0, 0, 0, 0, 'Centrálny', 10, 8),
('tax-frame-007-05', 'tax-007', 5, 70, 48, 15, 'ZANDER', 2025, 0, 1, 0, 0, 'Matka žltá', 8, 6),
('tax-frame-007-06', 'tax-007', 6, 66, 42, 18, 'ZANDER', 2024, 0, 0, 0, 0, 'Výborný', 10, 8),
('tax-frame-007-07', 'tax-007', 7, 50, 34, 28, 'ZANDER', 2024, 0, 0, 0, 0, 'Dobrý', 15, 9),
('tax-frame-007-08', 'tax-007', 8, 60, 30, 12, 'BUILDING', 2025, 1, 0, 0, 0, 'Stavbový Zander', 10, 6),
('tax-frame-007-09', 'tax-007', 9, 30, 20, 18, 'ZANDER', 2023, 0, 0, 0, 0, 'Okraj', 26, 12),
('tax-frame-007-10', 'tax-007', 10, 18, 14, 14, 'ZANDER', 2022, 0, 0, 0, 0, 'Staršie okraj', 34, 14);

-- TAXATION 008: Hive 008 (varroa issues) - 9 frames, weak
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) VALUES
('tax-008', 'test-hive-008', strftime('%s', '2025-05-18 14:30:00') * 1000, 20.0, 9, 2.3, 'Problém varroa, plod nepravidelný', strftime('%s', '2025-05-18') * 1000, strftime('%s', '2025-05-18') * 1000, 125, 145, 85, 310, 240, 0);

INSERT INTO taxation_frames (id, taxationId, position, cappedBroodDm, uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes, cappedStoresDm, uncappedStoresDm) VALUES
('tax-frame-008-01', 'tax-008', 1, 15, 15, 12, 'LANGSTROTH', 2021, 0, 0, 0, 0, 'Starý plást', 38, 18),
('tax-frame-008-02', 'tax-008', 2, 28, 22, 20, 'LANGSTROTH', 2022, 0, 0, 0, 0, 'Starší', 30, 15),
('tax-frame-008-03', 'tax-008', 3, 42, 32, 35, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Peľ', 22, 12),
('tax-frame-008-04', 'tax-008', 4, 48, 38, 18, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Plod dierový', 15, 10),
('tax-frame-008-05', 'tax-008', 5, 52, 42, 15, 'LANGSTROTH', 2024, 0, 1, 0, 0, 'Matka žltá slabá', 12, 8),
('tax-frame-008-06', 'tax-008', 6, 45, 35, 18, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Dierový', 16, 10),
('tax-frame-008-07', 'tax-008', 7, 35, 28, 25, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Slabší', 20, 12),
('tax-frame-008-08', 'tax-008', 8, 25, 20, 20, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okraj', 28, 14),
('tax-frame-008-09', 'tax-008', 9, 20, 18, 12, 'LANGSTROTH', 2022, 0, 0, 0, 0, 'Okrajový starý', 34, 16);

-- TAXATION 009: Hive 009 (excellent genetics) - 10 frames, perfect
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) VALUES
('tax-009', 'test-hive-009', strftime('%s', '2025-05-16 11:30:00') * 1000, 23.5, 10, 3.1, 'Výborná genetika, pokojné, produktívne', strftime('%s', '2025-05-16') * 1000, strftime('%s', '2025-05-16') * 1000, 175, 125, 75, 520, 350, 2);

INSERT INTO taxation_frames (id, taxationId, position, cappedBroodDm, uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes, cappedStoresDm, uncappedStoresDm) VALUES
('tax-frame-009-01', 'tax-009', 1, 25, 18, 18, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okraj dobrý', 30, 12),
('tax-frame-009-02', 'tax-009', 2, 45, 32, 28, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Výborný', 22, 10),
('tax-frame-009-03', 'tax-009', 3, 62, 42, 45, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Peľ veľa', 15, 8),
('tax-frame-009-04', 'tax-009', 4, 72, 52, 22, 'LANGSTROTH', 2025, 0, 0, 0, 0, 'Perfektný', 8, 6),
('tax-frame-009-05', 'tax-009', 5, 78, 58, 16, 'LANGSTROTH', 2025, 0, 1, 0, 0, 'Matka zelená tu', 5, 5),
('tax-frame-009-06', 'tax-009', 6, 75, 55, 18, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Výborný plod', 8, 6),
('tax-frame-009-07', 'tax-009', 7, 58, 40, 30, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Dobrý', 12, 8),
('tax-frame-009-08', 'tax-009', 8, 75, 45, 15, 'BUILDING', 2025, 1, 0, 0, 0, 'Stavbový nový', 10, 8),
('tax-frame-009-09', 'tax-009', 9, 80, 50, 8, 'BUILDING', 2025, 1, 0, 0, 0, 'Stavbový aktívny', 8, 6),
('tax-frame-009-10', 'tax-009', 10, 30, 22, 15, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okrajový', 28, 12);

-- TAXATION 010: Hive 010 (dead) - SKIP (no taxation for dead hive)

-- TAXATION 011: Hive 011 (productive star) - 12 frames, maximum production
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) VALUES
('tax-011', 'test-hive-011', strftime('%s', '2025-05-15 09:00:00') * 1000, 24.0, 12, 3.8, 'Najlepšia rodina, rekordné hodnoty', strftime('%s', '2025-05-15') * 1000, strftime('%s', '2025-05-15') * 1000, 210, 145, 95, 640, 420, 2);

INSERT INTO taxation_frames (id, taxationId, position, cappedBroodDm, uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes, cappedStoresDm, uncappedStoresDm) VALUES
('tax-frame-011-01', 'tax-011', 1, 28, 20, 20, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okraj silný', 28, 12),
('tax-frame-011-02', 'tax-011', 2, 48, 35, 30, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Výborný', 22, 10),
('tax-frame-011-03', 'tax-011', 3, 65, 45, 48, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Peľ masívne', 15, 8),
('tax-frame-011-04', 'tax-011', 4, 78, 55, 25, 'LANGSTROTH', 2025, 0, 0, 0, 0, 'Perfektný centrálny', 8, 6),
('tax-frame-011-05', 'tax-011', 5, 82, 60, 18, 'LANGSTROTH', 2025, 0, 1, 0, 0, 'Matka zelená tu', 5, 5),
('tax-frame-011-06', 'tax-011', 6, 80, 58, 20, 'LANGSTROTH', 2025, 0, 0, 0, 0, 'Ideálny', 6, 6),
('tax-frame-011-07', 'tax-011', 7, 72, 50, 24, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Výborný', 10, 8),
('tax-frame-011-08', 'tax-011', 8, 62, 42, 35, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Peľový tu', 12, 10),
('tax-frame-011-09', 'tax-011', 9, 75, 45, 12, 'BUILDING', 2025, 1, 0, 0, 0, 'Stavbový', 10, 8),
('tax-frame-011-10', 'tax-011', 10, 80, 50, 8, 'BUILDING', 2025, 1, 0, 0, 0, 'Stavbový aktívny', 8, 6),
('tax-frame-011-11', 'tax-011', 11, 42, 30, 22, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Okraj', 18, 10),
('tax-frame-011-12', 'tax-011', 12, 28, 22, 18, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okrajový', 26, 12);

-- TAXATION 012: Hive 012 (Dadant big) - 12 frames, very high values
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) VALUES
('tax-012', 'test-hive-012', strftime('%s', '2025-05-17 10:00:00') * 1000, 23.0, 12, 4.2, 'Dadant sústava, obrovská rodina', strftime('%s', '2025-05-17') * 1000, strftime('%s', '2025-05-17') * 1000, 220, 155, 105, 680, 450, 2);

INSERT INTO taxation_frames (id, taxationId, position, cappedBroodDm, uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes, cappedStoresDm, uncappedStoresDm) VALUES
('tax-frame-012-01', 'tax-012', 1, 30, 22, 22, 'DADANT', 2023, 0, 0, 0, 0, 'Okraj veľký', 30, 14),
('tax-frame-012-02', 'tax-012', 2, 52, 38, 32, 'DADANT', 2024, 0, 0, 0, 0, 'Veľký rámik', 24, 12),
('tax-frame-012-03', 'tax-012', 3, 68, 48, 50, 'DADANT', 2024, 0, 0, 0, 0, 'Peľ masívne', 18, 10),
('tax-frame-012-04', 'tax-012', 4, 82, 58, 28, 'DADANT', 2025, 0, 0, 0, 0, 'Dadant kapacita', 10, 8),
('tax-frame-012-05', 'tax-012', 5, 88, 62, 20, 'DADANT', 2025, 0, 1, 0, 0, 'Matka červená tu', 8, 6),
('tax-frame-012-06', 'tax-012', 6, 85, 60, 22, 'DADANT', 2025, 0, 0, 0, 0, 'Perfektný', 8, 8),
('tax-frame-012-07', 'tax-012', 7, 76, 52, 26, 'DADANT', 2024, 0, 0, 0, 0, 'Výborný', 12, 10),
('tax-frame-012-08', 'tax-012', 8, 65, 45, 38, 'DADANT', 2024, 0, 0, 0, 0, 'Peľ tu', 15, 12),
('tax-frame-012-09', 'tax-012', 9, 78, 48, 15, 'BUILDING', 2025, 1, 0, 0, 0, 'Stavbový Dadant', 12, 10),
('tax-frame-012-10', 'tax-012', 10, 82, 52, 10, 'BUILDING', 2025, 1, 0, 0, 0, 'Aktívne stavia', 10, 8),
('tax-frame-012-11', 'tax-012', 11, 45, 32, 24, 'DADANT', 2024, 0, 0, 0, 0, 'Okraj', 20, 12),
('tax-frame-012-12', 'tax-012', 12, 32, 24, 20, 'DADANT', 2023, 0, 0, 0, 0, 'Okrajový', 28, 14);

-- TAXATION 013: Hive 013 (queen replacement) - 9 frames, recovering
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) VALUES
('tax-013', 'test-hive-013', strftime('%s', '2025-05-19 13:00:00') * 1000, 21.5, 9, 2.4, 'Po výmene matky, začína klásť', strftime('%s', '2025-05-19') * 1000, strftime('%s', '2025-05-19') * 1000, 135, 140, 80, 340, 260, 0);

INSERT INTO taxation_frames (id, taxationId, position, cappedBroodDm, uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes, cappedStoresDm, uncappedStoresDm) VALUES
('tax-frame-013-01', 'tax-013', 1, 18, 16, 14, 'LANGSTROTH', 2022, 0, 0, 0, 0, 'Okraj starší', 35, 16),
('tax-frame-013-02', 'tax-013', 2, 32, 26, 22, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Obnovuje sa', 28, 14),
('tax-frame-013-03', 'tax-013', 3, 46, 34, 38, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Peľ tu', 20, 12),
('tax-frame-013-04', 'tax-013', 4, 52, 40, 20, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Začína lepšie', 15, 10),
('tax-frame-013-05', 'tax-013', 5, 58, 44, 16, 'LANGSTROTH', 2025, 0, 1, 0, 0, 'Nová matka tu', 12, 8),
('tax-frame-013-06', 'tax-013', 6, 50, 38, 18, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Dobrý', 14, 10),
('tax-frame-013-07', 'tax-013', 7, 38, 28, 26, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Rastie', 18, 12),
('tax-frame-013-08', 'tax-013', 8, 26, 20, 22, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okraj', 24, 14),
('tax-frame-013-09', 'tax-013', 9, 20, 18, 14, 'LANGSTROTH', 2022, 0, 0, 0, 0, 'Okrajový starý', 32, 16);

-- TAXATION 014: Hive 014 (propolis collector) - 10 frames, strong
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) VALUES
('tax-014', 'test-hive-014', strftime('%s', '2025-05-18 10:30:00') * 1000, 22.5, 10, 3.0, 'Veľa propolisu, silná rodina', strftime('%s', '2025-05-18') * 1000, strftime('%s', '2025-05-18') * 1000, 165, 130, 80, 490, 330, 1);

INSERT INTO taxation_frames (id, taxationId, position, cappedBroodDm, uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes, cappedStoresDm, uncappedStoresDm) VALUES
('tax-frame-014-01', 'tax-014', 1, 24, 18, 17, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okraj, propolis', 30, 13),
('tax-frame-014-02', 'tax-014', 2, 42, 32, 27, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Dobrý', 23, 11),
('tax-frame-014-03', 'tax-014', 3, 60, 42, 43, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Peľ veľa', 16, 10),
('tax-frame-014-04', 'tax-014', 4, 70, 50, 22, 'LANGSTROTH', 2025, 0, 0, 0, 0, 'Centrálny', 9, 7),
('tax-frame-014-05', 'tax-014', 5, 76, 54, 17, 'LANGSTROTH', 2025, 0, 1, 0, 0, 'Matka zelená', 6, 6),
('tax-frame-014-06', 'tax-014', 6, 72, 48, 19, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Výborný', 9, 7),
('tax-frame-014-07', 'tax-014', 7, 56, 38, 28, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Dobrý', 14, 9),
('tax-frame-014-08', 'tax-014', 8, 68, 36, 14, 'BUILDING', 2025, 1, 0, 0, 0, 'Stavbový', 11, 8),
('tax-frame-014-09', 'tax-014', 9, 34, 24, 19, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okraj', 25, 12),
('tax-frame-014-10', 'tax-014', 10, 22, 16, 16, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okrajový', 31, 13);

-- TAXATION 015: Hive 015 (nucleus) - 5 frames, small colony
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) VALUES
('tax-015', 'test-hive-015', strftime('%s', '2025-05-20 11:00:00') * 1000, 21.0, 5, 1.5, 'Odstavok, malá rodinka, rastie', strftime('%s', '2025-05-20') * 1000, strftime('%s', '2025-05-20') * 1000, 75, 110, 65, 140, 110, 0);

INSERT INTO taxation_frames (id, taxationId, position, cappedBroodDm, uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes, cappedStoresDm, uncappedStoresDm) VALUES
('tax-frame-015-01', 'tax-015', 1, 18, 15, 12, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Okraj malý', 28, 16),
('tax-frame-015-02', 'tax-015', 2, 32, 26, 22, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Rastie', 24, 14),
('tax-frame-015-03', 'tax-015', 3, 38, 30, 28, 'LANGSTROTH', 2025, 0, 1, 0, 0, 'Matka mladá tu', 20, 12),
('tax-frame-015-04', 'tax-015', 4, 30, 24, 24, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Dobrý', 22, 14),
('tax-frame-015-05', 'tax-015', 5, 22, 18, 14, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Okrajový', 26, 15);

-- TAXATION 016: Hive 016 (reduced size) - 8 frames, moderate
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) VALUES
('tax-016', 'test-hive-016', strftime('%s', '2025-05-17 14:00:00') * 1000, 22.0, 8, 2.5, 'Redukovaná po problémoch, zlepšuje sa', strftime('%s', '2025-05-17') * 1000, strftime('%s', '2025-05-17') * 1000, 130, 135, 75, 360, 280, 0);

INSERT INTO taxation_frames (id, taxationId, position, cappedBroodDm, uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes, cappedStoresDm, uncappedStoresDm) VALUES
('tax-frame-016-01', 'tax-016', 1, 22, 18, 15, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okraj', 32, 14),
('tax-frame-016-02', 'tax-016', 2, 38, 30, 24, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Lepší', 26, 12),
('tax-frame-016-03', 'tax-016', 3, 52, 40, 36, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Peľ tu', 20, 11),
('tax-frame-016-04', 'tax-016', 4, 60, 46, 20, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Centrálny', 14, 9),
('tax-frame-016-05', 'tax-016', 5, 66, 50, 16, 'LANGSTROTH', 2025, 0, 1, 0, 0, 'Matka zelená', 11, 8),
('tax-frame-016-06', 'tax-016', 6, 56, 42, 19, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Dobrý', 14, 9),
('tax-frame-016-07', 'tax-016', 7, 40, 32, 26, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Rastie', 20, 12),
('tax-frame-016-08', 'tax-016', 8, 26, 22, 18, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okrajový', 28, 14);

-- TAXATION 017: Hive 017 (high floor) - 10 frames, good
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) VALUES
('tax-017', 'test-hive-017', strftime('%s', '2025-05-16 12:30:00') * 1000, 23.0, 10, 2.9, 'Vysoké dno, dobrá ventilácia', strftime('%s', '2025-05-16') * 1000, strftime('%s', '2025-05-16') * 1000, 160, 128, 77, 470, 320, 1);

INSERT INTO taxation_frames (id, taxationId, position, cappedBroodDm, uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes, cappedStoresDm, uncappedStoresDm) VALUES
('tax-frame-017-01', 'tax-017', 1, 23, 17, 16, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okraj', 30, 13),
('tax-frame-017-02', 'tax-017', 2, 41, 31, 26, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Dobrý', 23, 11),
('tax-frame-017-03', 'tax-017', 3, 58, 40, 42, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Peľ hlavne', 16, 10),
('tax-frame-017-04', 'tax-017', 4, 68, 48, 21, 'LANGSTROTH', 2025, 0, 0, 0, 0, 'Centrálny', 9, 7),
('tax-frame-017-05', 'tax-017', 5, 74, 52, 17, 'LANGSTROTH', 2025, 0, 1, 0, 0, 'Matka červená', 7, 6),
('tax-frame-017-06', 'tax-017', 6, 70, 46, 19, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Výborný', 9, 7),
('tax-frame-017-07', 'tax-017', 7, 54, 36, 28, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Dobrý', 14, 9),
('tax-frame-017-08', 'tax-017', 8, 66, 34, 13, 'BUILDING', 2025, 1, 0, 0, 0, 'Stavbový', 10, 8),
('tax-frame-017-09', 'tax-017', 9, 33, 23, 19, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okraj', 25, 12),
('tax-frame-017-10', 'tax-017', 10, 24, 17, 16, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okrajový', 29, 13);

-- TAXATION 018: Hive 018 (young combs) - 10 frames, low varroa
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) VALUES
('tax-018', 'test-hive-018', strftime('%s', '2025-05-19 10:00:00') * 1000, 22.5, 10, 2.8, 'Nové plásty, minimálna varroa', strftime('%s', '2025-05-19') * 1000, strftime('%s', '2025-05-19') * 1000, 168, 132, 78, 485, 335, 2);

INSERT INTO taxation_frames (id, taxationId, position, cappedBroodDm, uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes, cappedStoresDm, uncappedStoresDm) VALUES
('tax-frame-018-01', 'tax-018', 1, 25, 19, 17, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Nový okraj', 29, 13),
('tax-frame-018-02', 'tax-018', 2, 43, 33, 27, 'LANGSTROTH', 2025, 0, 0, 0, 0, 'Čistý plást', 22, 11),
('tax-frame-018-03', 'tax-018', 3, 61, 43, 44, 'LANGSTROTH', 2025, 0, 0, 0, 0, 'Peľ čerstvý', 15, 9),
('tax-frame-018-04', 'tax-018', 4, 71, 51, 22, 'LANGSTROTH', 2025, 0, 0, 0, 0, 'Perfektný nový', 8, 7),
('tax-frame-018-05', 'tax-018', 5, 77, 55, 16, 'LANGSTROTH', 2025, 0, 1, 0, 0, 'Matka zelená tu', 6, 6),
('tax-frame-018-06', 'tax-018', 6, 73, 49, 18, 'LANGSTROTH', 2025, 0, 0, 0, 0, 'Čistý', 8, 7),
('tax-frame-018-07', 'tax-018', 7, 57, 37, 29, 'LANGSTROTH', 2025, 0, 0, 0, 0, 'Nový', 13, 9),
('tax-frame-018-08', 'tax-018', 8, 72, 38, 12, 'BUILDING', 2025, 1, 0, 0, 0, 'Stavbový čistý', 11, 8),
('tax-frame-018-09', 'tax-018', 9, 74, 40, 8, 'BUILDING', 2025, 1, 0, 0, 0, 'Stavbový aktívny', 10, 7),
('tax-frame-018-10', 'tax-018', 10, 32, 24, 17, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Okrajový', 27, 13);

-- TAXATION 019: Hive 019 (drone frame method) - 10 frames
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) VALUES
('tax-019', 'test-hive-019', strftime('%s', '2025-05-18 12:00:00') * 1000, 21.5, 10, 2.7, 'Trutové buňky - kontrola varroa', strftime('%s', '2025-05-18') * 1000, strftime('%s', '2025-05-18') * 1000, 158, 133, 79, 465, 315, 1);

INSERT INTO taxation_frames (id, taxationId, position, cappedBroodDm, uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes, cappedStoresDm, uncappedStoresDm) VALUES
('tax-frame-019-01', 'tax-019', 1, 24, 18, 16, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okraj', 30, 14),
('tax-frame-019-02', 'tax-019', 2, 42, 32, 26, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Dobrý', 23, 12),
('tax-frame-019-03', 'tax-019', 3, 59, 41, 43, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Peľový', 16, 10),
('tax-frame-019-04', 'tax-019', 4, 69, 49, 21, 'LANGSTROTH', 2025, 0, 0, 0, 0, 'Centrálny', 9, 8),
('tax-frame-019-05', 'tax-019', 5, 75, 53, 17, 'LANGSTROTH', 2025, 0, 1, 0, 0, 'Matka červená', 7, 6),
('tax-frame-019-06', 'tax-019', 6, 71, 47, 19, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Výborný', 9, 7),
('tax-frame-019-07', 'tax-019', 7, 55, 35, 28, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Dobrý', 14, 9),
('tax-frame-019-08', 'tax-019', 8, 64, 32, 13, 'BUILDING', 2025, 1, 0, 0, 0, 'Trutový rámik', 12, 8),
('tax-frame-019-09', 'tax-019', 9, 34, 24, 19, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okraj', 25, 12),
('tax-frame-019-10', 'tax-019', 10, 27, 19, 16, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okrajový', 29, 13);

-- TAXATION 020: Hive 020 (split success) - 10 frames, both parts thriving
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes, createdAt, updatedAt, totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm, totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames) VALUES
('tax-020', 'test-hive-020', strftime('%s', '2025-05-20 09:30:00') * 1000, 23.5, 10, 3.0, 'Po delení, obe časti zdravé', strftime('%s', '2025-05-20') * 1000, strftime('%s', '2025-05-20') * 1000, 170, 130, 80, 495, 340, 1);

INSERT INTO taxation_frames (id, taxationId, position, cappedBroodDm, uncappedBroodDm, pollenDm, frameType, frameYear, isStarter, hasQueen, hasCage, hasNucBox, notes, cappedStoresDm, uncappedStoresDm) VALUES
('tax-frame-020-01', 'tax-020', 1, 26, 20, 18, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okraj', 29, 13),
('tax-frame-020-02', 'tax-020', 2, 44, 34, 28, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Dobrý', 22, 11),
('tax-frame-020-03', 'tax-020', 3, 62, 44, 45, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Peľ veľa', 15, 9),
('tax-frame-020-04', 'tax-020', 4, 72, 52, 23, 'LANGSTROTH', 2025, 0, 0, 0, 0, 'Výborný', 8, 7),
('tax-frame-020-05', 'tax-020', 5, 78, 56, 17, 'LANGSTROTH', 2025, 0, 1, 0, 0, 'Matka zelená tu', 6, 6),
('tax-frame-020-06', 'tax-020', 6, 74, 50, 19, 'LANGSTROTH', 2025, 0, 0, 0, 0, 'Perfektný', 8, 7),
('tax-frame-020-07', 'tax-020', 7, 58, 38, 29, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Dobrý', 13, 9),
('tax-frame-020-08', 'tax-020', 8, 70, 36, 13, 'BUILDING', 2025, 1, 0, 0, 0, 'Stavbový po delení', 11, 8),
('tax-frame-020-09', 'tax-020', 9, 36, 26, 20, 'LANGSTROTH', 2024, 0, 0, 0, 0, 'Okraj', 24, 12),
('tax-frame-020-10', 'tax-020', 10, 28, 22, 17, 'LANGSTROTH', 2023, 0, 0, 0, 0, 'Okrajový', 28, 13);

-- ========================================
-- ZHRNUTIE TAXÁCIÍ
-- ========================================
-- Celkový počet taxácií: 19 (úle 1-9, 11-20; úľ 10 vynechaný - uhynutý)
-- Dátum: 15-20. mája 2025 (peak spring inspection season)
-- Každá taxácia má detailný breakdown rámikov:
--   - Langstroth/Zander: 5-10 rámikov
--   - Dadant: 12 rámikov
--   - Údaje: cappedBrood, uncappedBrood, pollen, cappedStores, uncappedStores
--   - Stavbové rámiky (isStarter=1) u silných rodín
--   - Označené pozície matiek (hasQueen=1)
--   - Totály vypočítané zo súčtu rámikov
--
-- Špecialitky:
--   - Úľ 001: 10 rámikov, 2 stavbové, vysoký plod (540/360 dm²)
--   - Úľ 003: 8 rámikov, slabý (180/140 dm²), žiadne stavbové
--   - Úľ 006: 6 rámikov, nový roj, stavia (120/90 dm²)
--   - Úľ 010: vynechaný (uhynutý úľ)
--   - Úľ 011: 12 rámikov, rekordné hodnoty (640/420 dm²)
--   - Úľ 012: 12 rámikov Dadant, masívna rodina (680/450 dm²)
--   - Úľ 015: 5 rámikov, odstavok (140/110 dm²)
--
-- Celkové sumy kontrolované proti súčtom rámikov pre každú taxáciu ✓

