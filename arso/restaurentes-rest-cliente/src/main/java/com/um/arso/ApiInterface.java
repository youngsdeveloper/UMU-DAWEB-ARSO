package com.um.arso;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.Call;

import java.util.List;

import com.um.arso.models.Plato;
import com.um.arso.models.Restaurante;
import com.um.arso.models.SitioTuristico;


public interface ApiInterface {

    @GET("restaurantes")
	Call<List<Restaurante>> getRestaurantes();

    @GET("restaurantes/{id}")
	Call<Restaurante> getRestaurante(@Path("id") String id);

    @POST("restaurantes")
	Call<Void> createRestaurante(@Body Restaurante r);

    @DELETE("restaurantes/{id}")
	Call<Void> deleteRestaurante(@Path("id") String id);

    @PUT("restaurantes/{id}")
	Call<Void> updateRestaurante(@Path("id") String id, @Body Restaurante r);

    @GET("restaurantes/{id}/sitios-turisticos")
	Call<List<SitioTuristico>> getSitiosTuristicos(@Path("id") String id);

    @PUT("restaurantes/{id}/sitios-turisticos")
	Call<Void> setSitiosTuristicos(@Path("id") String id,@Body List<SitioTuristico> sitiosTuristicos);

    @POST("restaurantes/{id}/platos/")
	Call<Void> crearPlato(@Path("id") String id,@Body Plato plato);

    @DELETE("restaurantes/{id}/platos/{nombre}")
	Call<Void> eliminarPlato(@Path("id") String id, @Path("nombre") String nombre);

    @PUT("restaurantes/{id}/platos/{nombre}")
	Call<Void> actualizarPlato(@Path("id") String id, @Path("nombre") String nombre, @Body Plato plato);

}
