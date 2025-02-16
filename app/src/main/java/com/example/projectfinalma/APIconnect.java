package com.example.projectfinalma;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIconnect {
    private static final String BASE_URL = "http://10.0.2.2:3000/";
    //private static final String BASE_URL = "http://192.168.148.183:3000/";
   // private static final String BASE_URL = "https://l5sftx2x-3000.uks1.devtunnels.ms/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
