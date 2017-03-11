package com.bohdan.gubar.youtubetest.application;

import android.app.Application;

import com.bohdan.gubar.youtubetest.retrofit.RetrofitApi;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.jakewharton.threetenabp.AndroidThreeTen;

import io.realm.Realm;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gubar on 10.03.2017.
 */

public class MyApp extends Application {


    public static final String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    Retrofit retrofit;
    static RetrofitApi retrofitApi;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        Realm.init(getApplicationContext());
        AndroidThreeTen.init(this);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitApi = retrofit.create(RetrofitApi.class);

    }

    public static RetrofitApi getRetrofitApi(){
        return retrofitApi;
    }
}
