package com.test.testgreen.api;

import com.test.testgreen.model.APIModel;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Nick Demenko on 28.03.2017.
 */

public interface APIService {

    @GET("web/index.php/api/trips?from_date=20150101&to_date=20180301")
    Call<APIModel> loadRoute();

}
