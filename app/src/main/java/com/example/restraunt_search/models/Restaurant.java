
package com.example.restraunt_search.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Restaurant extends RealmObject {
    @PrimaryKey
    private String id = String.valueOf(hashCode());

    private RestaurantInner restaurant;


    public RestaurantInner getRestaurant() {
        return restaurant;
    }


    public void setRetaurant(RestaurantInner restaurant) {
        this.restaurant = restaurant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
