package com.example.restraunt_search.data;

import java.util.List;

import io.realm.RealmObject;

/**
 * Interface for the local data
 */
public interface CachedData {
    public <T extends RealmObject> void setRealmData(T itemsToSaveLocally);

    public <T extends RealmObject> void deleteRealmData(String primaryKey,Class<T> clzz,String id);


    public <T extends RealmObject> List<T> getRealmResults(Class<T> objType);
}