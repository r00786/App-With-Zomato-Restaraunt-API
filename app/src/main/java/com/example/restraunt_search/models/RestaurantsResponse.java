
package com.example.restraunt_search.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class RestaurantsResponse {

    @Expose
    private List<Restaurant> restaurants;
    @SerializedName("results_found")
    private int resultsFound;
    @SerializedName("results_shown")
    private int resultsShown;
    @SerializedName("results_start")
    private int resultsStart;
    private transient boolean hasToPaginate;

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public int getResultsFound() {
        return resultsFound;
    }

    public void setResultsFound(int resultsFound) {
        this.resultsFound = resultsFound;
    }

    public int getResultsShown() {
        return resultsShown;
    }

    public void setResultsShown(int resultsShown) {
        this.resultsShown = resultsShown;
    }

    public int getResultsStart() {
        return resultsStart;
    }

    public void setResultsStart(int resultsStart) {
        this.resultsStart = resultsStart;
    }

    public boolean isHasToPaginate() {
        return hasToPaginate;
    }

    public void setHasToPaginate(boolean hasToPaginate) {
        this.hasToPaginate = hasToPaginate;
    }
}
