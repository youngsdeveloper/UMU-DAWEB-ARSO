package com.um.arso.services;

import java.io.IOException;
import java.util.List;

import com.um.arso.models.Opinion;
import com.um.arso.models.Valoracion;
import com.um.arso.retrofit.ApiInterface;
import com.um.arso.retrofit.OpinionesApiClient;

public class ServicioOpinionesRetrofit implements IServicioOpiniones{

    @Override
    public String darAltaOpinion(String recurso) {

        ApiInterface opinionesApiClient = OpinionesApiClient.getApiClient().create(ApiInterface.class);
        Opinion op;
        try {
            op = opinionesApiClient.crearRecurso(recurso).execute().body();
            return op.getId();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

        
    }

    @Override
    public List<Valoracion> getValoraciones(String recurso) {
        
        ApiInterface opinionesApiClient = OpinionesApiClient.getApiClient().create(ApiInterface.class);
        try {
            return opinionesApiClient.getOpinionByRecurso(recurso).execute().body().getValoraciones();
        } catch (IOException e) {
            return null;
        }

    }
    
}
