package com.cvte.realmexample.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mluhui on 16/1/20.
 */
public class Users extends RealmObject {
    @PrimaryKey
    private String id;
    private String name;

    private RealmList<Games> games;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Games> getGames() {
        return games;
    }

    public void setGames(RealmList<Games> games) {
        this.games = games;
    }
}
