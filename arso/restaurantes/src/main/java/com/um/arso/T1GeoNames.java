package com.um.arso;

import java.util.List;

import com.um.arso.models.SitioTuristico;
import com.um.arso.services.SitiosTuristicosServices;


public class T1GeoNames {
    public static void main(String[] args) {

        // Probar con 30100 (Murcia)

        List<SitioTuristico> places = SitiosTuristicosServices.getInstance().findByPostalCode(30100);
        System.out.println(places);
        
    }

}
