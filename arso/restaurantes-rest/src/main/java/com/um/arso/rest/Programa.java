package com.um.arso.rest;

import com.um.arso.repositories.RepositorioException;
import com.um.arso.services.FactoriaServicios;
import com.um.arso.services.IServicioRestaurantes;

public class Programa {
    public static void main(String[] args) {


        IServicioRestaurantes servicio = FactoriaServicios.getServicio(IServicioRestaurantes.class);

        try {
            System.out.println(servicio.getAllRestaurantes());
        } catch (RepositorioException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
