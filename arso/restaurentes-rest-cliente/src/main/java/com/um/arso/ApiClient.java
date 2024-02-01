package com.um.arso;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ApiClient {
    

    public static Retrofit getApiClient(){


        
        

        ObjectMapper mapper = new ObjectMapper(); 
        mapper.registerModule(new JSR310Module());
        mapper.setDateFormat(new SimpleDateFormat());

        Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://localhost:8080/api/")
                            .addConverterFactory(JacksonConverterFactory.create(mapper))
                            .build();

        return retrofit;
    }
}
