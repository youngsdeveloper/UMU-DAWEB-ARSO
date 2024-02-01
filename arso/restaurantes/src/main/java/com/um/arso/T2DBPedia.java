package com.um.arso;

import java.util.List;


import com.um.arso.models.PlaceDetails;
import com.um.arso.models.SitioTuristico;
import com.um.arso.services.SitiosTuristicosServices;

public class T2DBPedia {
    public static void main(String[] args) {

        // Probar con 30100 (Murcia)

        List<SitioTuristico> places = SitiosTuristicosServices.getInstance().findByPostalCode(30100);
        

        for(SitioTuristico place:places){
            PlaceDetails pd = SitiosTuristicosServices.getInstance().finPlaceDetails(place);
            System.out.println(pd);
            System.out.println("--------------");
        }
        
    }

}
