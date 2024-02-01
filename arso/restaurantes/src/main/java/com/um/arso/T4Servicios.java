package com.um.arso;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.um.arso.dto.RestauranteDTO;
import com.um.arso.dto.SitioTuristicoDTO;
import com.um.arso.repositories.EntidadNoEncontrada;
import com.um.arso.repositories.RepositorioException;
import com.um.arso.services.FactoriaServicios;
import com.um.arso.services.IServicioRestaurantes;

public class T4Servicios {
    public static void main(String[] args) throws RepositorioException, EntidadNoEncontrada {
        

        IServicioRestaurantes servicio = FactoriaServicios.getServicio(IServicioRestaurantes.class); 
        
        RestauranteDTO r1 = new RestauranteDTO();
        r1.setNombre("NEW AtlasOne " + UUID.randomUUID());
        r1.setLat(38.0228159);
        r1.setLng(-1.1735258);
        r1.setCiudad("Murcia");
        r1.setFechaAlta(LocalDate.now());

        String id = servicio.create(r1);

        
        List<SitioTuristicoDTO> sitioTuristicos = servicio.getSitiosTuristicosCercanos(id);

        System.out.println(sitioTuristicos);

        

    }
}
