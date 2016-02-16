package com.cvte.realmexample;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by mluhui on 16/2/1.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build());
    }
}
