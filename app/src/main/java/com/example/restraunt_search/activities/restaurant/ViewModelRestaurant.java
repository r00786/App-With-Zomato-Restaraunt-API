package com.example.restraunt_search.activities.restaurant;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.restraunt_search.models.Restaurant;
import com.example.restraunt_search.models.RestaurantsResponse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Observable;

public class ViewModelRestaurant extends ViewModel implements RepositoryRestaurantCallbacks.ResponseCallbacks {
    private MutableLiveData<RestaurantsResponse> liveData = new MutableLiveData<>();
    private MutableLiveData<List<Restaurant>> bookmarks = new MutableLiveData<>();
    private Set<String> removedItems;
    private int resultsAvailable;
    private int resultsStart;
    private int resultsShown;
    private boolean isLoading;
    private boolean isPaginating;


    private final RepositoryRestaurantCallbacks repositoryCallbacks;

    @Inject
    public ViewModelRestaurant(RepositoryRestaurantCallbacks repositoryCallbacks) {
        this.repositoryCallbacks = repositoryCallbacks;
    }

    /**
     * Get Restaurants from the Repository
     * @param query query to be searched on the network
     */
    public void getRestaurants(String query) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("q", query);
        queryMap.put("start", String.valueOf((resultsShown + resultsStart)));
        queryMap.put("count", String.valueOf(10));
        repositoryCallbacks.getRestaurantData(queryMap, this);
        isLoading = true;
    }

    /**
     * Fetched Restaurant Response from the server
     * @param response Parsed Response From the Server
     */
    @Override
    public void restaurantResponse(RestaurantsResponse response) {
        if (response == null) return;
        if (response.getRestaurants() == null) return;
        resultsAvailable = response.getResultsFound();
        resultsStart = response.getResultsStart();
        resultsShown = response.getResultsShown();
        response.setHasToPaginate(isPaginating);
        getLiveData().setValue(response);
        isLoading = false;
        isPaginating = false;
    }

    /**
     * Restaurant Items present in the local database
     * @param response items fetched from the local database
     */
    @Override
    public void bookMarkedItems(List<Restaurant> response) {
        bookmarks.setValue(response);
    }

    /***
     * Live Data For The Restaurant Items
     * @return live Data for the restaurant items
     */
    public MutableLiveData<RestaurantsResponse> getLiveData() {
        return liveData;
    }

    /**
     * to check if it is the last segment for pagination
     * @return
     */
    public boolean getIsLastPage() {
        return (resultsStart + resultsShown) >= resultsAvailable;
    }

    /**
     * to check whether Loading is ongoing or not
     * @return boolean for loading
     */
    public boolean isLoading() {
        return isLoading;
    }

    /**
     * method to paginate with the query specified
     * @param query query for the items to paginate
     */
    public void paginate(String query) {
        isPaginating = true;
        getRestaurants(query);
    }

    /**
     * clearing the composite disposables to end the on going processes if activity ends
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        repositoryCallbacks.clearCompositeDisposable();
    }

    /**
     * getting all the bookmarked restaurants
     */
    public void getBookMarkedRestaurants() {
        repositoryCallbacks.getBookMarkedItems(this);
    }

    /**
     * resetting all the parameters
     * @param isPaginating to paginate or not
     */
    public void setHaveToPaginate(boolean isPaginating) {
        resultsStart = 0;
        resultsShown = 0;
        resultsAvailable = 0;
        this.isPaginating = isPaginating;
    }

    /**
     * to save or delete item in the repository
     * @param restaurant item to delete
     * @param save true to save false to delete
     */
    public void saveOrDeleteFromDB(Restaurant restaurant, boolean save) {
        repositoryCallbacks.saveOrDeleteFromDb(restaurant, save);
    }

    /**
     * Add Bookmarks for updating in the restaurant list
     * @param id item to be added in the bookmarks
     * @param bookMarkedItems Saved Set in Shared PReferences
     * @param save to remove or save
     */
    public void addOrRemoveFromSet(String id, Set<String> bookMarkedItems, boolean save) {
        if (save) {
            bookMarkedItems.add(id);
        } else {
            bookMarkedItems.remove(id);
        }
    }

    /**
     * get live data for the bookmarks
     * @return
     */
    public MutableLiveData<List<Restaurant>> getBookmarks() {
        return bookmarks;
    }

    /**
     * check and update the restaurant list with the saved bookmarks
     * @param bookMarkedItems Set in the Shared PReferences
     * @param restaurants Items from the network
     * @return updated list with the bookmarks
     */
    public RestaurantsResponse checkForBookmarks(Set<String> bookMarkedItems, RestaurantsResponse restaurants) {
        Observable<Restaurant> obs = Observable.fromIterable(restaurants.getRestaurants()).map(restaurant -> {
            if (bookMarkedItems.contains(restaurant.getRestaurant().getId())) {
                restaurant.getRestaurant().setBookMarked(true);
            }
            return restaurant;
        });
        obs.subscribe(success -> {
        }, err -> {
        });
        return restaurants;
    }

    /**
     * Adding to the Set consisting of the Removed BookMarks
     * @param item item to add to Removed Book Marks List
     */
    public void addToRemovedBookMarks(String item) {
        if (removedItems == null) removedItems = new HashSet<>();
        removedItems.add(item);
    }

    /**
     * getter for the removed bookmarks
     * @return set of the removed book marks
     */
    public Set<String> getRemovedItems() {
        if (removedItems == null) removedItems = new HashSet<>();
        return removedItems;
    }
}
