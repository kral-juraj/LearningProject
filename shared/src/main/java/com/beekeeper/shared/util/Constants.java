package com.beekeeper.shared.util;

public class Constants {

    // Database
    public static final String DATABASE_NAME = "beekeeper_db";
    public static final int DATABASE_VERSION = 1;

    // Shared Preferences
    public static final String PREFS_NAME = "beekeeper_prefs";
    public static final String PREF_OPENAI_API_KEY = "openai_api_key";
    public static final String PREF_DEFAULT_APIARY_ID = "default_apiary_id";
    public static final String PREF_RECORDING_AUTO_DELETE_DAYS = "recording_auto_delete_days";
    public static final String PREF_NOTIFICATION_ENABLED = "notification_enabled";
    public static final String PREF_SYNC_ENABLED = "sync_enabled";

    // OpenAI API
    public static final String OPENAI_BASE_URL = "https://api.openai.com/";
    public static final String WHISPER_MODEL = "whisper-1";
    public static final String GPT_MODEL = "gpt-4-turbo-preview";
    public static final int OPENAI_TIMEOUT_SECONDS = 120;

    // Recording
    public static final String RECORDING_DIR = "recordings";
    public static final String AUDIO_FILE_EXTENSION = ".m4a";
    public static final String VIDEO_FILE_EXTENSION = ".mp4";
    public static final int AUDIO_SAMPLE_RATE = 44100;
    public static final int AUDIO_BIT_RATE = 128000;

    // Hive Types
    public static final String HIVE_TYPE_VERTICAL = "VERTICAL";
    public static final String HIVE_TYPE_HORIZONTAL = "HORIZONTAL";
    public static final String HIVE_TYPE_NUKE = "NUKE";

    // Recording Types
    public static final String RECORDING_TYPE_AUDIO = "AUDIO";
    public static final String RECORDING_TYPE_VIDEO = "VIDEO";

    // Feed Types
    public static final String FEED_TYPE_SYRUP_1_1 = "SYRUP_1_1";
    public static final String FEED_TYPE_SYRUP_3_2 = "SYRUP_3_2";
    public static final String FEED_TYPE_FONDANT = "FONDANT";
    public static final String FEED_TYPE_POLLEN_PATTY = "POLLEN_PATTY";

    // Frame Types
    public static final String FRAME_TYPE_BROOD = "BROOD";
    public static final String FRAME_TYPE_HONEY = "HONEY";
    public static final String FRAME_TYPE_FOUNDATION = "FOUNDATION";
    public static final String FRAME_TYPE_DRAWN = "DRAWN";
    public static final String FRAME_TYPE_DARK = "DARK";

    // Calendar Event Types
    public static final String EVENT_TYPE_INSPECTION = "INSPECTION";
    public static final String EVENT_TYPE_FEEDING = "FEEDING";
    public static final String EVENT_TYPE_TREATMENT = "TREATMENT";
    public static final String EVENT_TYPE_HARVEST = "HARVEST";
    public static final String EVENT_TYPE_REMINDER = "REMINDER";

    // Varroa Calculation
    public static final double VARROA_GROWTH_RATE = 2.0;
    public static final int VARROA_GROWTH_PERIOD_DAYS = 21;
    public static final int CELLS_PER_DECIMETER = 800;

    // Date Formats
    public static final String DATE_FORMAT_FULL = "dd.MM.yyyy HH:mm";
    public static final String DATE_FORMAT_DATE = "dd.MM.yyyy";
    public static final String DATE_FORMAT_TIME = "HH:mm";

    private Constants() {
        // Prevent instantiation
    }
}
