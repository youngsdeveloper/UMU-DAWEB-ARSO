package com.um.arso.retrofit;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpinionesApiClient {

    public static Retrofit getApiClient(){


        String baseURL = System.getenv("OPINIONES_URL");

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        return retrofit;
    

    }
}
