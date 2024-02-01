package com.um.arso.graphql;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import com.um.arso.models.Opinion;
import com.um.arso.repositories.EntidadNoEncontrada;
import com.um.arso.repositories.RepositorioException;
import com.um.arso.services.FactoriaServicios;
import com.um.arso.services.IServicioOpiniones;

public class Query implements GraphQLRootResolver{

    private IServicioOpiniones servicio = FactoriaServicios.getServicio(IServicioOpiniones.class);

    public List<Opinion> findAll() throws RepositorioException{
        return servicio.findAll();
    }

    public Opinion recuperarOpinion(String id) throws RepositorioException, EntidadNoEncontrada {
        return servicio.recuperarOpinion(id);
    }
}
