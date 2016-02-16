package com.cvte.realmexample.model.dao;
import java.lang.reflect.Field;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import rx.Observable;

/**
 * Created by mluhui on 16/2/1.
 */
public class RealmDao<T extends RealmObject> {

    public static <R extends RealmObject> RealmDao<R> getDao(Class<R> realmClazz) {
        return new RealmDao<>(realmClazz);
    }

    private Class<T> mClazz;
    private String mPrimaryKey;

    protected RealmDao(Class<T> clazz) {
        mClazz = clazz;
        Class<PrimaryKey> primaryKeyClass = PrimaryKey.class;
        for (Field field :
                clazz.getDeclaredFields()) {
            if (field.getAnnotation(primaryKeyClass) != null) {
                mPrimaryKey = field.getName();
                break;
            }
        }
    }

    public Observable<T> getAsync(String primaryKeyValue, Realm realm) {
        return getAsyncObservable(primaryKeyValue, realm).take(1);
    }

    public Observable<T> getAsyncObservable(String primaryKeyValue, Realm realm) {
        return realm
                .where(mClazz)
                .equalTo(mPrimaryKey, primaryKeyValue)
                .findFirstAsync()
                .asObservable()
                .filter(RealmObject::isLoaded)
                .cast(mClazz);
    }
}
