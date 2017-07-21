package com.test.testgreen.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by Nick Demenko on 10.03.2017.
 */

public class City extends RealmObject implements Serializable {

    @SerializedName("name")
    @Expose
    private String city;

    @SerializedName("id")
    @Expose
    private String idCity;

    public City() {
    }

    public City(String city, String idCity) {
        this.city = city;
        this.idCity = idCity;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIdCity() {
        return idCity;
    }

    public void setIdCity(String idCity) {
        this.idCity = idCity;
    }
}
