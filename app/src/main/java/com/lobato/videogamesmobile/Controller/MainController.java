package com.lobato.videogamesmobile.Controller;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainController {
    private static Retrofit retrofit = null;
    private final static String URL = "https://lobatolab.tech";
    static Retrofit getclient(){
        if(retrofit!= null) return retrofit;
        OkHttpClient client = new OkHttpClient.Builder().build();
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }
}
