-- Realistic Test Taxations with Frames
-- Generated: 2025-02-20
-- Purpose: Replace old taxations with realistic dm² values
-- Note: 2 taxations intentionally have negative free space (for testing validation)

-- Clean up old test taxations
DELETE FROM taxation_frames WHERE taxationId LIKE 'tax-%';
DELETE FROM taxations WHERE id LIKE 'tax-%';

-- Tax-001: Úľ 1 - Silné (LANGSTROTH_1_1, 10 frames = 97 dm² capacity)
-- Occupied: 35 + 25 + 8 = 68 dm² → Free: 29 dm² ✅
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes,
                       totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm,
                       totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames,
                       createdAt, updatedAt)
SELECT 'tax-001', id, strftime('%s', '2025-02-15') * 1000, 18.5, 10, 8.5, 'Silné včelstvo, dobrý plod',
       8, 15, 10, 22, 13, 0,
       strftime('%s', 'now') * 1000, strftime('%s', 'now') * 1000
FROM hives WHERE name = 'Úľ 1 - Silné';

-- Tax-002: Úľ 2 - Dadant (DADANT_PLODISKO, 10 frames = 126 dm² capacity)
-- Occupied: 45 + 35 + 12 = 92 dm² → Free: 34 dm² ✅
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes,
                       totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm,
                       totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames,
                       createdAt, updatedAt)
SELECT 'tax-002', id, strftime('%s', '2025-02-14') * 1000, 19.0, 10, 10.2, 'Dadant rodina, veľa plodu',
       12, 20, 15, 28, 17, 1,
       strftime('%s', 'now') * 1000, strftime('%s', 'now') * 1000
FROM hives WHERE name = 'Úľ 2 - Dadant';

-- Tax-003: Úľ 3 - Slabé (LANGSTROTH_2_3, 8 frames = 59 dm² capacity)
-- Occupied: 18 + 15 + 5 = 38 dm² → Free: 21 dm² ✅
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes,
                       totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm,
                       totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames,
                       createdAt, updatedAt)
SELECT 'tax-003', id, strftime('%s', '2025-02-13') * 1000, 17.5, 8, 4.5, 'Slabé včelstvo po zime',
       5, 9, 6, 11, 7, 0,
       strftime('%s', 'now') * 1000, strftime('%s', 'now') * 1000
FROM hives WHERE name = 'Úľ 3 - Slabé';

-- Tax-004: Úľ 4 - Agresívne (LANGSTROTH_1_1, 9 frames = 87 dm² capacity)
-- Occupied: 32 + 22 + 7 = 61 dm² → Free: 26 dm² ✅
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes,
                       totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm,
                       totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames,
                       createdAt, updatedAt)
SELECT 'tax-004', id, strftime('%s', '2025-02-12') * 1000, 18.0, 9, 7.0, 'Veľmi agresívne, silná rodina',
       7, 13, 9, 20, 12, 0,
       strftime('%s', 'now') * 1000, strftime('%s', 'now') * 1000
FROM hives WHERE name = 'Úľ 4 - Agresívne';

-- Tax-005: Úľ 5 - Rojivé (LANGSTROTH_JUMBO, 10 frames = 120 dm² capacity)
-- ⚠️ INTENTIONALLY NEGATIVE: Occupied: 55 + 45 + 15 = 115 dm² → Free: 5 dm²
-- (Realistic but tight - almost full)
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes,
                       totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm,
                       totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames,
                       createdAt, updatedAt)
SELECT 'tax-005', id, strftime('%s', '2025-02-11') * 1000, 19.5, 10, 12.5, 'Rojové nálady, plné rámiky',
       15, 25, 20, 34, 21, 3,
       strftime('%s', 'now') * 1000, strftime('%s', 'now') * 1000
FROM hives WHERE name = 'Úľ 5 - Rojivé';

-- Tax-006: Úľ 6 - Roj 2025 (LANGSTROTH, 6 frames = 58 dm² capacity)
-- Occupied: 12 + 10 + 3 = 25 dm² → Free: 33 dm² ✅
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes,
                       totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm,
                       totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames,
                       createdAt, updatedAt)
SELECT 'tax-006', id, strftime('%s', '2025-02-10') * 1000, 16.5, 6, 3.2, 'Mladý roj, potrebuje kŕmenie',
       3, 6, 4, 7, 5, 0,
       strftime('%s', 'now') * 1000, strftime('%s', 'now') * 1000
FROM hives WHERE name = 'Úľ 6 - Roj 2025';

-- Tax-007: Úľ 7 - Zander (ZANDER_CELY, 10 frames = 92 dm² capacity)
-- Occupied: 28 + 20 + 6 = 54 dm² → Free: 38 dm² ✅
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes,
                       totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm,
                       totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames,
                       createdAt, updatedAt)
SELECT 'tax-007', id, strftime('%s', '2025-02-09') * 1000, 18.0, 10, 6.5, 'Zander úľ, priemerná sila',
       6, 12, 8, 17, 11, 0,
       strftime('%s', 'now') * 1000, strftime('%s', 'now') * 1000
FROM hives WHERE name = 'Úľ 7 - Zander';

-- Tax-008: Úľ 8 - Varroa (LANGSTROTH, 9 frames = 87 dm² capacity)
-- Occupied: 25 + 18 + 5 = 48 dm² → Free: 39 dm² ✅
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes,
                       totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm,
                       totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames,
                       createdAt, updatedAt)
SELECT 'tax-008', id, strftime('%s', '2025-02-08') * 1000, 17.0, 9, 5.5, 'Zvýšený počet varroa, treba ošetriť',
       5, 11, 7, 15, 10, 0,
       strftime('%s', 'now') * 1000, strftime('%s', 'now') * 1000
FROM hives WHERE name = 'Úľ 8 - Varroa';

-- Tax-009: Úľ 9 - Zdravé (LANGSTROTH, 10 frames = 97 dm² capacity)
-- Occupied: 33 + 24 + 7 = 64 dm² → Free: 33 dm² ✅
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes,
                       totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm,
                       totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames,
                       createdAt, updatedAt)
SELECT 'tax-009', id, strftime('%s', '2025-02-07') * 1000, 18.5, 10, 8.0, 'Výborný stav, bez problémov',
       7, 14, 10, 20, 13, 0,
       strftime('%s', 'now') * 1000, strftime('%s', 'now') * 1000
FROM hives WHERE name = 'Úľ 9 - Zdravé';

-- Tax-010: Úľ 11 - Produkčné (LANGSTROTH, 12 frames = 116 dm² capacity)
-- 🔴 INTENTIONALLY UNREALISTIC (for testing): Occupied: 55 + 60 + 20 = 135 dm² → Free: -19 dm² ❌
-- This shows what happens with bad data entry
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes,
                       totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm,
                       totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames,
                       createdAt, updatedAt)
SELECT 'tax-010', id, strftime('%s', '2025-02-06') * 1000, 20.0, 12, 15.0, '⚠️ TEST: Nerealistické dm² hodnoty!',
       20, 35, 25, 33, 22, 2,
       strftime('%s', 'now') * 1000, strftime('%s', 'now') * 1000
FROM hives WHERE name = 'Úľ 11 - Produkčné';

-- Tax-011: Úľ 12 - Veľká rodina (DADANT, 12 frames = 151 dm² capacity)
-- Occupied: 50 + 40 + 15 = 105 dm² → Free: 46 dm² ✅
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes,
                       totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm,
                       totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames,
                       createdAt, updatedAt)
SELECT 'tax-011', id, strftime('%s', '2025-02-05') * 1000, 19.5, 12, 11.5, 'Veľká silná rodina',
       15, 23, 17, 31, 19, 1,
       strftime('%s', 'now') * 1000, strftime('%s', 'now') * 1000
FROM hives WHERE name = 'Úľ 12 - Veľká rodina';

-- Tax-012: Úľ 14 - Propolis (LANGSTROTH, 10 frames = 97 dm² capacity)
-- 🔴 INTENTIONALLY UNREALISTIC: Occupied: 45 + 50 + 25 = 120 dm² → Free: -23 dm² ❌
INSERT INTO taxations (id, hiveId, taxationDate, temperature, totalFrames, foodStoresKg, notes,
                       totalPollenDm, totalCappedStoresDm, totalUncappedStoresDm,
                       totalCappedBroodDm, totalUncappedBroodDm, totalStarterFrames,
                       createdAt, updatedAt)
SELECT 'tax-012', id, strftime('%s', '2025-02-04') * 1000, 18.0, 10, 13.0, '⚠️ TEST: Chybné dm² hodnoty - viditeľné červenou!',
       25, 28, 22, 27, 18, 0,
       strftime('%s', 'now') * 1000, strftime('%s', 'now') * 1000
FROM hives WHERE name = 'Úľ 14 - Propolis';

-- Summary
SELECT '✅ Test taxations regenerated' as status;
SELECT
    COUNT(*) as total_taxations,
    SUM(CASE
        WHEN (totalPollenDm + totalCappedStoresDm + totalUncappedStoresDm +
              totalCappedBroodDm + totalUncappedBroodDm) < totalFrames * 10
        THEN 1 ELSE 0
    END) as realistic_count,
    SUM(CASE
        WHEN (totalPollenDm + totalCappedStoresDm + totalUncappedStoresDm +
              totalCappedBroodDm + totalUncappedBroodDm) >= totalFrames * 10
        THEN 1 ELSE 0
    END) as unrealistic_count
FROM taxations
WHERE id LIKE 'tax-%';
