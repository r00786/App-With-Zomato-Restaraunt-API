package com.example.restraunt_search.activities.restaurant;

import com.example.restraunt_search.data.DataManager;
import com.example.restraunt_search.models.Restaurant;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmObject;

public class RepositoryRestaurant implements RepositoryRestaurantCallbacks {
    private final DataManager dataManager;
    private CompositeDisposable compositeDisposable;

    @Inject
    public RepositoryRestaurant(DataManager dataManager) {
        this.dataManager = dataManager;
        compositeDisposable = new CompositeDisposable();
    }

    /**
     * Getting Restaurants From  The Network     *
     * @param qMap Query Map of items
     * @param responseCallbacks callbacks to receive the response
     */
    @Override
    public void getRestaurantData(Map<String, String> qMap, ResponseCallbacks responseCallbacks) {
        compositeDisposable.add(dataManager.getRestaurants(qMap).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(responseCallbacks::restaurantResponse, err -> {
                }));

    }

    /**
     * Saving or deleting items from the database
     * @param realmObject Object to be added to the database
     * @param save delete or add to the database
     */
    @Override
    public void saveOrDeleteFromDb(RealmObject realmObject, boolean save) {
        if (save) {
            dataManager.setRealmData(realmObject);
        } else {
            dataManager.deleteRealmData("id", Restaurant.class, ((Restaurant) realmObject).getId());
        }
    }

    /**
     * getting  all the book marked items from the database
     * @param responseCallbacks callback method to send the fetched items to viewmodel
     */
    @Override
    public void getBookMarkedItems(ResponseCallbacks responseCallbacks) {
        responseCallbacks.bookMarkedItems(dataManager.getRealmResults(Restaurant.class));
    }

    /**
     * Clear the ongoing network or any process if activity is killed
     */
    @Override
    public void clearCompositeDisposable() {
        compositeDisposable.clear();
    }
}
