package com.um.arso;

import java.util.List;
import java.util.UUID;

import com.um.arso.models.Restaurante;
import com.um.arso.repositories.EntidadNoEncontrada;
import com.um.arso.repositories.FactoriaRepositorios;
import com.um.arso.repositories.Repositorio;
import com.um.arso.repositories.RepositorioException;

public class T3Repositories {
    public static void main(String[] args) {
        
        Repositorio<Restaurante,String> repositorio = FactoriaRepositorios.getRepositorio(Restaurante.class);
        
        Restaurante r1 = new Restaurante();
        r1.setNombre("Rincon de pepe " + UUID.randomUUID());
        r1.setLat(1.0);
        r1.setLng(1.0);

        try {
            repositorio.add(r1);
        } catch (RepositorioException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Restaurante r2 = new Restaurante();
        r2.setNombre("Rincon de pepe2" + UUID.randomUUID());
        r2.setLat(1.0);
        r2.setLng(1.0);

        try {
            repositorio.add(r2);
        } catch (RepositorioException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         
        try {
            for (Restaurante r:repositorio.getAll()) {
                System.out.println(r);
            }
        } catch (RepositorioException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            for (String s:repositorio.getIds()) {
                System.out.println(s);
            }
        } catch (RepositorioException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            List<Restaurante> restaurantes = repositorio.getAll();
            System.out.println("Eliminando..." + restaurantes.get(0).getId());
            repositorio.delete(restaurantes.get(0));

        } catch (RepositorioException | EntidadNoEncontrada e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            for (String s:repositorio.getIds()) {
                System.out.println(s);
            }
        } catch (RepositorioException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            List<Restaurante> restaurantes = repositorio.getAll();

            Restaurante toUpdate = restaurantes.get(0);
            System.out.println("Actualizando..." + toUpdate.getId());
            
            toUpdate.setNombre("ACTUALIZADOOOOOO" + toUpdate.getNombre());
            
            repositorio.update(toUpdate);

        } catch (RepositorioException | EntidadNoEncontrada e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            for (Restaurante r:repositorio.getAll()) {
                System.out.println(r);
            }
        } catch (RepositorioException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

    }
}
