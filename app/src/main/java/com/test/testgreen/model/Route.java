package com.test.testgreen.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;

public class Route extends RealmObject implements Serializable {

    @SerializedName("from_city")
    @Expose
    private City fromCity = new City();

    @SerializedName("to_city")
    @Expose
    private City toCity = new City();

    @SerializedName("id")
    @Expose
    private String routeId;

    @SerializedName("from_date")
    @Expose
    private Date fromDate;

    @SerializedName("from_time")
    @Expose
    private String fromTime;

    @SerializedName("from_info")
    @Expose
    private String fromInfo;

    @SerializedName("to_date")
    @Expose
    private Date toDate;

    @SerializedName("to_time")
    @Expose
    private String toTime;

    @SerializedName("to_info")
    @Expose
    private String toInfo;

    @SerializedName("bus_id")
    @Expose
    private String busId;

    @SerializedName("reservation_count")
    @Expose
    private String reservationCount;

    @Expose
    private String price;

    @Expose
    private String info;

    public Route() {
    }

    public Route(String routeId, String from, String idFrom, String to, String idTo, String price, Date fromDate, String fromTime,
                 String fromInfo, Date toDate, String toTime, String toInfo, String info, String busId, String reservationCount) {
        fromCity.setCity(from);
        fromCity.setIdCity(idFrom);
        toCity.setCity(to);
        toCity.setIdCity(idTo);

        this.routeId = routeId;
        this.price = price;
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.fromInfo = fromInfo;
        this.toDate = toDate;
        this.toTime = toTime;
        this.toInfo = toInfo;
        this.info = info;
        this.busId = busId;
        this.reservationCount = reservationCount;
    }

    public City getFromCity() {
        return fromCity;
    }

    public void setFromCity(City fromCity) {
        this.fromCity = fromCity;
    }

    public City getToCity() {
        return toCity;
    }

    public void setToCity(City toCity) {
        this.toCity = toCity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getFromInfo() {
        return fromInfo;
    }

    public void setFromInfo(String fromInfo) {
        this.fromInfo = fromInfo;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getToInfo() {
        return toInfo;
    }

    public void setToInfo(String toInfo) {
        this.toInfo = toInfo;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getReservationCount() {
        return reservationCount;
    }

    public void setReservationCount(String reservationCount) {
        this.reservationCount = reservationCount;
    }

}

