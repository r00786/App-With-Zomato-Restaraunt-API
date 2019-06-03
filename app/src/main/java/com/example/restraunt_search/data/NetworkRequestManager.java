package com.example.restraunt_search.data;

import com.example.restraunt_search.models.RestaurantsResponse;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Interface for all the network queries
 */
public interface NetworkRequestManager {
    @GET("search")
    Single<RestaurantsResponse> getRestaurants(@QueryMap Map<String, String> qMap);
}
