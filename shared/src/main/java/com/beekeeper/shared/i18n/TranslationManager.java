package com.beekeeper.shared.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Manages application translations loaded from database.
 * Thread-safe singleton providing fast O(1) translation lookups.
 *
 * Use case: All UI components query this manager for translated strings.
 * Initialize once in Main.java, then use getInstance() everywhere.
 */
public class TranslationManager {

    private static TranslationManager instance;
    private Map<String, String> translations;
    private String currentLanguage;
    private static final String DEFAULT_LANGUAGE = "sk";

    private TranslationManager() {
        translations = new HashMap<>();
        currentLanguage = DEFAULT_LANGUAGE;
    }

    /**
     * Get singleton instance of TranslationManager.
     *
     * @return Singleton instance
     */
    public static synchronized TranslationManager getInstance() {
        if (instance == null) {
            instance = new TranslationManager();
        }
        return instance;
    }

    /**
     * Load translations from loader (JDBC, Room, etc.) for specified language.
     * This replaces all currently loaded translations.
     *
     * Use case: Called once during application startup in Main.java.
     *
     * @param loader Platform-specific translation loader
     * @param language Language code (e.g., "sk", "en")
     */
    public void loadTranslations(TranslationLoader loader, String language) {
        this.currentLanguage = language;
        this.translations = loader.loadTranslations(language);
    }

    /**
     * Get translated string for key in current language.
     * Returns [key] if translation not found (makes missing translations obvious).
     *
     * Use case: Called by controllers, dialogs, and ViewModels to get UI strings.
     *
     * @param key Translation key (e.g., "button.add_apiary")
     * @return Translated string, or [key] if not found
     */
    public String get(String key) {
        String value = translations.get(key);
        return value != null ? value : "[" + key + "]";
    }

    /**
     * Get formatted string with parameters using String.format() syntax.
     * Supports standard Java format specifiers: %s, %d, %.2f, etc.
     *
     * Use case: Dynamic messages with variables, like "Date: %s" or "Count: %d".
     *
     * @param key Translation key
     * @param params Parameters to substitute in the format string
     * @return Formatted translated string
     */
    public String get(String key, Object... params) {
        String template = get(key);

        // Return unformatted if template is missing (already shows [key])
        if (template.startsWith("[") && template.endsWith("]")) {
            return template;
        }

        try {
            // Use String.format for standard Java format specifiers (%s, %d, %.0f%%, etc.)
            return String.format(template, params);
        } catch (Exception e) {
            // If formatting fails, return template with error indication
            return template + " [format error]";
        }
    }

    /**
     * Get current language code.
     *
     * @return Language code (e.g., "sk", "en")
     */
    public String getCurrentLanguage() {
        return currentLanguage;
    }

    /**
     * Get current locale for date/number formatting.
     *
     * @return Locale object based on current language
     */
    public Locale getCurrentLocale() {
        return new Locale(currentLanguage);
    }
}
