package com.example.restraunt_search.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RestaurantInner extends RealmObject implements Parcelable {
    @Expose
    private String apikey;

    @Expose
    private String cuisines;
    @Expose
    private String currency;
    @PrimaryKey
    @Expose
    private String id;

    @Expose
    private Location location;

    @Expose
    @SerializedName("name")
    private String name;
    @SerializedName("featured_image")
    private String featuredImage;


    private transient boolean isBookMarked;



    public RestaurantInner() {
    }

    protected RestaurantInner(Parcel in) {
        apikey = in.readString();
        cuisines = in.readString();
        currency = in.readString();
        id = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
        name = in.readString();
        featuredImage = in.readString();
    }

    public static final Creator<RestaurantInner> CREATOR = new Creator<RestaurantInner>() {
        @Override
        public RestaurantInner createFromParcel(Parcel in) {
            return new RestaurantInner(in);
        }

        @Override
        public RestaurantInner[] newArray(int size) {
            return new RestaurantInner[size];
        }
    };

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }


    public String getCuisines() {
        return cuisines;
    }

    public void setCuisines(String cuisines) {
        this.cuisines = cuisines;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isBookMarked() {
        return isBookMarked;
    }

    public void setBookMarked(boolean bookMarked) {
        isBookMarked = bookMarked;
    }

    public String getFeatured_image() {
        return featuredImage;
    }

    public void setFeatured_image(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(apikey);
        dest.writeString(cuisines);
        dest.writeString(currency);
        dest.writeString(id);
        dest.writeParcelable(location, flags);
        dest.writeString(name);
        dest.writeString(featuredImage);
    }
}
