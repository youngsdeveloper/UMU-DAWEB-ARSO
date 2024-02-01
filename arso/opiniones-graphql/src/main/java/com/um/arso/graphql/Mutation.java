package com.um.arso.graphql;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import com.um.arso.repositories.EntidadNoEncontrada;
import com.um.arso.repositories.RepositorioException;
import com.um.arso.services.FactoriaServicios;
import com.um.arso.services.IServicioOpiniones;

public class Mutation implements GraphQLRootResolver {
    
    private IServicioOpiniones servicio = FactoriaServicios.getServicio(IServicioOpiniones.class);

    public String crearRecurso(String nombre) throws RepositorioException{
        return servicio.createRecurso(nombre);
    }
   public boolean registrarValoracion(String id, String email, double calificacion, String comentario)
            throws RepositorioException, EntidadNoEncontrada{

        return servicio.registrarValoracion(id, email, calificacion, comentario);
    }
    public boolean eliminarOpinion(String id) throws RepositorioException, EntidadNoEncontrada {
        return servicio.eliminarOpinion(id);
    }
}
