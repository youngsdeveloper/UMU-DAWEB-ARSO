package com.um.arso;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.um.arso.dto.RestauranteDTO;
import com.um.arso.models.Restaurante;
import com.um.arso.repositories.EntidadNoEncontrada;
import com.um.arso.repositories.FactoriaRepositorios;
import com.um.arso.repositories.Repositorio;
import com.um.arso.repositories.RepositorioException;
import com.um.arso.repositories.especificacion.EqualsSpecification;
import com.um.arso.repositories.especificacion.OrSpecification;
import com.um.arso.repositories.especificacion.OrderByNombreDesc;
import com.um.arso.repositories.especificacion.Specification;
import com.um.arso.services.FactoriaServicios;
import com.um.arso.services.IServicioRestaurantes;

public class TestEspecificacion {
    @Test
    public void testCiudadMurcia() throws RepositorioException, EntidadNoEncontrada{


        IServicioRestaurantes servicioRestaurantes = FactoriaServicios.getServicio(IServicioRestaurantes.class);
        
        List<RestauranteDTO> resDTO = servicioRestaurantes.searchBy("ciudad", "Murcia");

        System.out.println(resDTO);




    }

    @Test
    public void testMultipleSpec() throws RepositorioException, EntidadNoEncontrada{
        Repositorio<Restaurante,String> repositorio = FactoriaRepositorios.getRepositorio(Restaurante.class);
        

        List<Specification<Restaurante>> specifications = new ArrayList<>();


        specifications.add(new EqualsSpecification<>("ciudad", "Murcia3"));
        specifications.add(new EqualsSpecification<>("nombre", "La paella  mediteranea"));

        OrSpecification<Restaurante> orSpecification = new OrSpecification<Restaurante>(specifications);

        List<Restaurante> res = repositorio.search(orSpecification);
        System.out.println(res); 

        
    }

    @Test
    public void testSorting() throws RepositorioException, EntidadNoEncontrada{
        Repositorio<Restaurante,String> repositorio = FactoriaRepositorios.getRepositorio(Restaurante.class);
        

        List<Specification<Restaurante>> specifications = new ArrayList<>();

        specifications.add(new EqualsSpecification<>("ciudad", "Murcia"));
        specifications.add(new OrderByNombreDesc());

        OrSpecification<Restaurante> orSpecification = new OrSpecification<Restaurante>(specifications);

        List<Restaurante> res = repositorio.search(orSpecification);
        System.out.println(res); 

    }


}
