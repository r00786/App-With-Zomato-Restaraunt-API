package com.example.restraunt_search.data;

import com.example.restraunt_search.di.DataInterface;
import com.example.restraunt_search.models.RestaurantsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Class to Handle All the Data Related Requests
 */
public class DataManager implements DataInterface {
    private final NetworkRequestManager ntReq;

    public DataManager(NetworkRequestManager ntReq) {
        this.ntReq = ntReq;
    }

    @Override
    public Single<RestaurantsResponse> getRestaurants(Map<String, String> qMap) {
        return ntReq.getRestaurants(qMap);
    }

    @Override
    public <T extends RealmObject> void setRealmData(T itemsToSaveLocally) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(itemsToSaveLocally);
        realm.commitTransaction();
        realm.close();


    }

    @Override
    public <T extends RealmObject> void deleteRealmData(String primaryKey, Class<T> clzz, String id) {
        Realm realm = Realm.getDefaultInstance();
        T obj = realm.where(clzz).equalTo(primaryKey, id).findFirst();
        if (obj == null) return;
        realm.beginTransaction();
        obj.deleteFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public <T extends RealmObject> List<T> getRealmResults(Class<T> objType) {
        Realm realm = Realm.getDefaultInstance();
        ArrayList<T> ts = new ArrayList<>(realm.copyFromRealm(realm.where(objType).findAll()));
        realm.close();
        return ts;

    }


}
