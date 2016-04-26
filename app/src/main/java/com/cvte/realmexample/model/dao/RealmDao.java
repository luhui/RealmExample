package com.cvte.realmexample.model.dao;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by mluhui on 16/2/1.
 */
public class RealmDao<T extends RealmObject> {

    public static <R extends RealmObject> RealmDao<R> getDao(Class<R> realmClazz) {
        return new RealmDao<>(realmClazz);
    }

    private Class<T> mClazz;
    private String mPrimaryKey;
    private List<Field> relationFields = new ArrayList<>();

    protected RealmDao(Class<T> clazz) {
        mClazz = clazz;
        Class<PrimaryKey> primaryKeyClass = PrimaryKey.class;
        for (Field field :
                clazz.getDeclaredFields()) {
            if (field.getAnnotation(primaryKeyClass) != null) {
                mPrimaryKey = field.getName();
                break;
            }
            if (field.getType().getSuperclass() == RealmObject.class || (field.getType() == RealmList.class)) {
                relationFields.add(field);
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
                .filter(realmObject -> realmObject.isLoaded())
                .cast(mClazz);
    }

    @SuppressWarnings("unchecked")
    public void delete(T t, Realm realm) {
        for (Field field :
                relationFields) {
            try {
                Object object = field.get(t);
                if (object instanceof RealmList) {
                    RealmList list = (RealmList) object;
                    for (Object o :
                            list) {
                        if (o instanceof RealmObject) {
                            RealmObject ro = (RealmObject) o;
                            RealmDao dao = getDao(ro.getClass());
                            dao.delete(ro, realm);
                        }
                    }
                    list.clear();
                } else  {
                    RealmObject ro = (RealmObject) object;
                    RealmDao dao = getDao(ro.getClass());
                    dao.delete(ro, realm);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        t.removeFromRealm();
    }
}
