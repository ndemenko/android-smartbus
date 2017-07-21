package com.test.testgreen.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Nick Demenko on 28.03.2017.
 */

public class APIModel implements Serializable {
    @Expose
    private boolean success;

    @Expose
    private List<Route> data;

    public APIModel() {
    }

    public APIModel(boolean success, List<Route> data) {
        this.success = success;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Route> getData() {
        return data;
    }

    public void setData(List<Route> data) {
        this.data = data;
    }
}
