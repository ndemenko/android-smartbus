package com.test.testgreen.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.testgreen.api.APIService;
import com.test.testgreen.event.MyDataEvent;
import com.test.testgreen.fragment.RoutesListFragment;
import com.test.testgreen.logger.Logger;
import com.test.testgreen.model.APIModel;
import com.test.testgreen.model.Route;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QueryService extends Service {
    public final static String TAG = QueryService.class.getSimpleName();
    ExecutorService executorService;
    ParseTask parseTask;

    public void onCreate() {
        super.onCreate();
        executorService = Executors.newFixedThreadPool(1);
        Log.d(TAG, "onCreate");

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        parseTask = new ParseTask();
        executorService.execute(parseTask);
        Logger.d(TAG, "onStart");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public class ParseTask implements Runnable {
        boolean bool = false;
        List<Route> routesList;
        Realm realmHelper = Realm.getInstance(getApplicationContext());
        public boolean getBool(){
            return bool;
        }
        @Override
        public void run() {
            bool = true;
            Logger.d(TAG, "Connection ok");
            EventBus.getDefault().post(new MyDataEvent(RoutesListFragment.STATUS_START));
            try {
                Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .setDateFormat("yyyy-MM-dd")
                        .create();
                routesList = new ArrayList<>();

                APIService service;
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://smartbus.gmoby.org/")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                service = retrofit.create(APIService.class);
                Call<APIModel> call = service.loadRoute();
                call.enqueue(new Callback<APIModel>() {
                    @Override
                    public void onResponse(Call<APIModel> call, Response<APIModel> response) {
                        Logger.d(TAG, "onResponse()");
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            routesList.add(response.body().getData().get(i));
                        }
                        realmHelper.beginTransaction();
                        realmHelper.clear(Route.class);
                        realmHelper.copyToRealm(routesList);
                        realmHelper.commitTransaction();
                        realmHelper.close();

                        EventBus.getDefault().post(new MyDataEvent(RoutesListFragment.STATUS_FINISH));
                        bool = false;
                        Logger.d(TAG, String.valueOf(routesList.size()));
                    }

                    @Override
                    public void onFailure(Call<APIModel> call, Throwable t) {

                    }
                });
            } catch (Exception e) {
                //show button if error is exist
                EventBus.getDefault().post(new MyDataEvent(RoutesListFragment.STATUS_ERROR));
                Logger.e(TAG, "Exception:" + e);
            }
        }
    }//--class ParseTask
}
