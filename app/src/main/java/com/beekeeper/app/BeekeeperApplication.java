package com.beekeeper.app;

import android.app.Application;
import com.beekeeper.app.data.local.db.AppDatabase;

public class BeekeeperApplication extends Application {

    private static BeekeeperApplication instance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = AppDatabase.getInstance(this);
    }

    public static BeekeeperApplication getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
