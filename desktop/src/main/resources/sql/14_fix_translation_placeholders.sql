-- Fix translation placeholders from {0} to %s for String.format() compatibility
-- Date: 2025-02-21
-- Reason: TranslationManager.get(key, params) uses String.format(), not manual {0} replacement

-- Replace {0}, {1}, {2}, {3} with %s for String.format() compatibility
UPDATE translations SET value = REPLACE(value, '{0}', '%s') WHERE value LIKE '%{0}%';
UPDATE translations SET value = REPLACE(value, '{1}', '%s') WHERE value LIKE '%{1}%';
UPDATE translations SET value = REPLACE(value, '{2}', '%s') WHERE value LIKE '%{2}%';
UPDATE translations SET value = REPLACE(value, '{3}', '%s') WHERE value LIKE '%{3}%';

-- Verify no more {N} placeholders remain
SELECT 'Remaining {N} placeholders (should be 0):' as status, COUNT(*) as count
FROM translations
WHERE value LIKE '%{%}%';

-- Show sample of fixed translations
SELECT key, language, value
FROM translations
WHERE key IN ('error.creating_hive', 'dialog.hive_history.title', 'chart.tooltip.date')
ORDER BY key, language;
