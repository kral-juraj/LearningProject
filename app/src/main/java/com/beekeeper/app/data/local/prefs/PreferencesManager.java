package com.beekeeper.app.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.beekeeper.app.util.Constants;

public class PreferencesManager {

    private final SharedPreferences prefs;

    public PreferencesManager(Context context) {
        this.prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
    }

    // OpenAI API Key
    public void setOpenAiApiKey(String apiKey) {
        prefs.edit().putString(Constants.PREF_OPENAI_API_KEY, apiKey).apply();
    }

    public String getOpenAiApiKey() {
        return prefs.getString(Constants.PREF_OPENAI_API_KEY, "");
    }

    public boolean hasOpenAiApiKey() {
        String key = getOpenAiApiKey();
        return key != null && !key.isEmpty();
    }

    // Default Apiary ID
    public void setDefaultApiaryId(String apiaryId) {
        prefs.edit().putString(Constants.PREF_DEFAULT_APIARY_ID, apiaryId).apply();
    }

    public String getDefaultApiaryId() {
        return prefs.getString(Constants.PREF_DEFAULT_APIARY_ID, null);
    }

    // Recording Auto Delete Days
    public void setRecordingAutoDeleteDays(int days) {
        prefs.edit().putInt(Constants.PREF_RECORDING_AUTO_DELETE_DAYS, days).apply();
    }

    public int getRecordingAutoDeleteDays() {
        return prefs.getInt(Constants.PREF_RECORDING_AUTO_DELETE_DAYS, 30);
    }

    // Notification Enabled
    public void setNotificationEnabled(boolean enabled) {
        prefs.edit().putBoolean(Constants.PREF_NOTIFICATION_ENABLED, enabled).apply();
    }

    public boolean isNotificationEnabled() {
        return prefs.getBoolean(Constants.PREF_NOTIFICATION_ENABLED, true);
    }

    // Sync Enabled (for future)
    public void setSyncEnabled(boolean enabled) {
        prefs.edit().putBoolean(Constants.PREF_SYNC_ENABLED, enabled).apply();
    }

    public boolean isSyncEnabled() {
        return prefs.getBoolean(Constants.PREF_SYNC_ENABLED, false);
    }

    // Clear all preferences
    public void clear() {
        prefs.edit().clear().apply();
    }
}
