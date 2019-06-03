package com.example.restraunt_search.activities.restaurant;

import com.example.restraunt_search.models.Restaurant;
import com.example.restraunt_search.models.RestaurantsResponse;

import java.util.List;
import java.util.Map;

import io.realm.RealmObject;

/**
 * Repository Callbacks to make loose coupling between viewmodel and repository
 * to be implemented by the repository
 */
public interface RepositoryRestaurantCallbacks {
    public void getRestaurantData(Map<String, String> qMap, ResponseCallbacks responseCallbacks);

    public void saveOrDeleteFromDb(RealmObject realmObject, boolean save);

    public void getBookMarkedItems(ResponseCallbacks responseCallbacks);


    public void clearCompositeDisposable();

    /**
     * Response Callbacks to be implemented by the ViewModel
     */
    public interface ResponseCallbacks {
        public void restaurantResponse(RestaurantsResponse response);

        public void bookMarkedItems(List<Restaurant> response);


    }
}
