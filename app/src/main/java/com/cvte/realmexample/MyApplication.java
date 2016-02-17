package com.cvte.realmexample;

import android.app.Application;
import android.util.Log;

import com.cvte.realmexample.model.Users;

import java.util.UUID;

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

        Realm realm = Realm.getDefaultInstance();
        Log.e("lh", "before save " + realm.allObjects(Users.class).size());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Users users = new Users();
                users.setId(UUID.randomUUID().toString());
                users.setName("test");
                realm.copyToRealm(users);
            }
        }, new Realm.Transaction.Callback() {
            @Override
            public void onSuccess() {
                //realm already update
                Log.e("lh", "after save " + realm.allObjects(Users.class).size());
                realm.close();
            }
        });
    }
}
