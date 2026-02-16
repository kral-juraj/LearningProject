package com.beekeeper.shared.i18n;

import java.util.Map;

/**
 * Interface for loading translations from platform-specific storage.
 * Desktop uses JDBC, Android would use Room.
 *
 * Use case: Platform-specific implementations load translations from database
 * and return them as a map for the TranslationManager.
 */
public interface TranslationLoader {

    /**
     * Load all translations for the specified language.
     *
     * @param language Language code (e.g., "sk", "en")
     * @return Map of translation keys to values
     */
    Map<String, String> loadTranslations(String language);
}
