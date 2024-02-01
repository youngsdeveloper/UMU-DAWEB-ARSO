package com.um.arso;

import com.um.arso.repositories.EntidadNoEncontrada;
import com.um.arso.repositories.RepositorioException;
import com.um.arso.services.FactoriaServicios;
import com.um.arso.services.IServicioOpiniones;

public class TestServicio {
    public static void main(String[] args) throws RepositorioException, EntidadNoEncontrada {
        
        IServicioOpiniones iServicioOpiniones = FactoriaServicios.getServicio(IServicioOpiniones.class);
    
        String id = iServicioOpiniones.createRecurso("Restaurante Pepe");

        iServicioOpiniones.registrarValoracion(id, "kike@um.es", 2, null);

        /*
        System.out.println(iServicioOpiniones.recuperarOpinion(id));

        iServicioOpiniones.eliminarOpinion("642c76f46a266f7c04e99007");*/
    }
}
