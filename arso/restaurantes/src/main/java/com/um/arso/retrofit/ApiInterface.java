package com.um.arso.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import com.um.arso.models.*;


public interface ApiInterface {

    @GET("opiniones/recurso/{recurso}")
	Call<Opinion> getOpinionByRecurso(@Path("recurso") String recurso);


    @POST("opiniones")
    @FormUrlEncoded
	Call<Opinion> crearRecurso(@Field("nombre") String recurso);

}
