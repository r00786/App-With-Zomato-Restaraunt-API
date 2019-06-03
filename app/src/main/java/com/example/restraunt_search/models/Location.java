
package com.example.restraunt_search.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;


public class Location extends RealmObject  implements Parcelable{

    @Expose
    private String address;
    @Expose
    private String city;
    @SerializedName("city_id")
    private Long cityId;
    @SerializedName("country_id")
    private Long countryId;
    @Expose
    private String latitude;
    @Expose
    private String locality;
    @SerializedName("locality_verbose")
    private String localityVerbose;
    @Expose
    private String longitude;
    @Expose
    private String zipcode;

    protected Location(Parcel in) {
        address = in.readString();
        city = in.readString();
        if (in.readByte() == 0) {
            cityId = null;
        } else {
            cityId = in.readLong();
        }
        if (in.readByte() == 0) {
            countryId = null;
        } else {
            countryId = in.readLong();
        }
        latitude = in.readString();
        locality = in.readString();
        localityVerbose = in.readString();
        longitude = in.readString();
        zipcode = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public Location() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getLocalityVerbose() {
        return localityVerbose;
    }

    public void setLocalityVerbose(String localityVerbose) {
        this.localityVerbose = localityVerbose;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(city);
        if (cityId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(cityId);
        }
        if (countryId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(countryId);
        }
        dest.writeString(latitude);
        dest.writeString(locality);
        dest.writeString(localityVerbose);
        dest.writeString(longitude);
        dest.writeString(zipcode);
    }
}
