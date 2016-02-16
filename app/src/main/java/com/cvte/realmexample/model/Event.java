package com.cvte.realmexample.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mluhui on 16/1/23.
 */
public class Event extends RealmObject {
    @PrimaryKey
    private String id;

    private long time;
    private String name;

    private String userId;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
