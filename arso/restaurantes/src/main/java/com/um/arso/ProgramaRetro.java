package com.um.arso;

import java.io.IOException;

import com.um.arso.dto.RestauranteDTO;
import com.um.arso.repositories.RepositorioException;
import com.um.arso.services.FactoriaServicios;
import com.um.arso.services.IServicioRestaurantes;

public class ProgramaRetro {
    public static void main(String[] args) throws IOException, RepositorioException {
        
        RestauranteDTO restauranteDTO = new RestauranteDTO();
        restauranteDTO.setNombre("hola mundo1212");

        IServicioRestaurantes servicioRestaurantes = FactoriaServicios.getServicio(IServicioRestaurantes.class);
        servicioRestaurantes.create(restauranteDTO);
    }
}
