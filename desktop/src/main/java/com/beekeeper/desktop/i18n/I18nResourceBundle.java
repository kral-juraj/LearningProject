package com.beekeeper.desktop.i18n;

import com.beekeeper.shared.i18n.TranslationManager;

import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * Custom ResourceBundle that delegates to TranslationManager.
 * Enables JavaFX FXML files to use %key syntax for translations.
 *
 * Use case: Set this as ResourceBundle when loading FXML files.
 * FXML: <Button text="%button.add"/> will call handleGetObject("button.add").
 */
public class I18nResourceBundle extends ResourceBundle {

    private final TranslationManager manager;

    /**
     * Create resource bundle that delegates to TranslationManager.
     *
     * @param manager TranslationManager instance with loaded translations
     */
    public I18nResourceBundle(TranslationManager manager) {
        this.manager = manager;
    }

    /**
     * Get translated value for key.
     * Called by JavaFX when processing %key syntax in FXML.
     *
     * @param key Translation key
     * @return Translated string
     */
    @Override
    protected Object handleGetObject(String key) {
        return manager.get(key);
    }

    /**
     * Check if key exists in translations.
     * JavaFX calls this before handleGetObject() to verify resource exists.
     *
     * @param key Translation key
     * @return true if translation exists, false otherwise
     */
    @Override
    public boolean containsKey(String key) {
        String value = manager.get(key);
        // TranslationManager returns "[key]" for missing keys
        return value != null && !value.startsWith("[");
    }

    /**
     * Get all keys (not needed for FXML usage).
     *
     * @return Empty enumeration
     */
    @Override
    public Enumeration<String> getKeys() {
        // Not needed for FXML usage
        return Collections.emptyEnumeration();
    }
}
