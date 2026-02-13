package com.beekeeper.app.data.local.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.beekeeper.app.data.local.dao.ApiaryDao;
import com.beekeeper.app.data.local.dao.CalendarEventDao;
import com.beekeeper.app.data.local.dao.FeedingDao;
import com.beekeeper.app.data.local.dao.HiveDao;
import com.beekeeper.app.data.local.dao.InspectionDao;
import com.beekeeper.app.data.local.dao.InspectionRecordingDao;
import com.beekeeper.app.data.local.dao.SettingsDao;
import com.beekeeper.app.data.local.dao.TaxationDao;
import com.beekeeper.app.data.local.dao.TaxationFrameDao;
import com.beekeeper.app.data.local.entity.Apiary;
import com.beekeeper.app.data.local.entity.CalendarEvent;
import com.beekeeper.app.data.local.entity.Feeding;
import com.beekeeper.app.data.local.entity.Hive;
import com.beekeeper.app.data.local.entity.Inspection;
import com.beekeeper.app.data.local.entity.InspectionRecording;
import com.beekeeper.app.data.local.entity.Settings;
import com.beekeeper.app.data.local.entity.Taxation;
import com.beekeeper.app.data.local.entity.TaxationFrame;
import com.beekeeper.app.util.Constants;

@Database(
    entities = {
        Apiary.class,
        Hive.class,
        Inspection.class,
        InspectionRecording.class,
        Feeding.class,
        Taxation.class,
        TaxationFrame.class,
        CalendarEvent.class,
        Settings.class
    },
    version = Constants.DATABASE_VERSION,
    exportSchema = true
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    // DAO abstract methods
    public abstract ApiaryDao apiaryDao();
    public abstract HiveDao hiveDao();
    public abstract InspectionDao inspectionDao();
    public abstract InspectionRecordingDao inspectionRecordingDao();
    public abstract FeedingDao feedingDao();
    public abstract TaxationDao taxationDao();
    public abstract TaxationFrameDao taxationFrameDao();
    public abstract CalendarEventDao calendarEventDao();
    public abstract SettingsDao settingsDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            Constants.DATABASE_NAME
                        )
                        .fallbackToDestructiveMigration()
                        .build();
                }
            }
        }
        return INSTANCE;
    }
}
