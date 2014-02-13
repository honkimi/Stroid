package com.honkimi.app;

import android.app.Application;

import com.honkimi.stroid.StroidMigrationManager;

/**
 * Created by kiminari.homma on 14/02/13.
 */
public class StroidSampleApplication extends Application {

    StroidMigrationManager.StroidConf dbConf = new StroidMigrationManager.StroidConf() {
        @Override
        public String getDBName() {
            return "StroidSample";
        }

        @Override
        public int getDBVersion() {
            return 1;
        }
    };

    @Override
    public void onCreate() {
        StroidMigrationManager mng = StroidMigrationManager.getInstance();
        mng.setConf(dbConf);

        mng.add(new NoteMigration());
    }
}
