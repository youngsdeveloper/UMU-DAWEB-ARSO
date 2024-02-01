package com.um.arso;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.um.arso.dto.PlatoDTO;
import com.um.arso.dto.RestauranteDTO;
import com.um.arso.repositories.EntidadNoEncontrada;
import com.um.arso.repositories.RepositorioException;
import com.um.arso.services.FactoriaServicios;
import com.um.arso.services.IServicioRestaurantes;

public class TestTarea4 {

    static IServicioRestaurantes servicio;

    @BeforeAll
	public static void setUp() {

        servicio = FactoriaServicios.getServicio(IServicioRestaurantes.class); 

	}
    
    @Test
    public void crearRestaurante() throws RepositorioException, EntidadNoEncontrada{
        
        RestauranteDTO r1 = new RestauranteDTO();
        r1.setNombre("TEST CREATE RESTAURANTE " + UUID.randomUUID());
        r1.setLat(1.0);
        r1.setLng(1.0);
        r1.setCiudad("Murcia");
        r1.setFechaAlta(LocalDate.now());

        String id = servicio.create(r1);

        assertNotNull(id);
        assertNotNull(servicio.findRestaurante(id));

        servicio.removeRestaurante(id);
    }

    @Test
    public void addPlato() throws RepositorioException, EntidadNoEncontrada{

        RestauranteDTO r1 = new RestauranteDTO();
        r1.setNombre("TEST CREATE RESTAURANTE " + UUID.randomUUID());
        r1.setLat(1.0);
        r1.setLng(1.0);
        r1.setCiudad("Murcia");
        r1.setFechaAlta(LocalDate.now());

        String id = servicio.create(r1);

        assertTrue(r1.getPlatos().isEmpty());

        PlatoDTO p1 = new PlatoDTO();
        p1.setNombre("PlatoDTO test");
        p1.setDescripcion("PlatoDTO test");
        p1.setPrecio(5.0);

        servicio.addPlato(id, p1);

        r1 = servicio.findRestaurante(id);

        assertTrue(r1.getPlatos().size()==1);
        assertTrue(r1.getPlatos().get(0).equals(p1));

        servicio.removeRestaurante(id);
    }

    @Test
    public void removePlato() throws RepositorioException, EntidadNoEncontrada{

        RestauranteDTO r1 = new RestauranteDTO();
        r1.setNombre("TEST CREATE RESTAURANTE " + UUID.randomUUID());
        r1.setLat(1.0);
        r1.setLng(1.0);
        r1.setCiudad("Murcia");
        r1.setFechaAlta(LocalDate.now());

        String id = servicio.create(r1);

        assertTrue(r1.getPlatos().isEmpty());

        PlatoDTO p1 = new PlatoDTO();
        p1.setNombre("PlatoDTO test");
        p1.setDescripcion("PlatoDTO test");
        p1.setPrecio(5.0);

        servicio.addPlato(id, p1);

        r1 = servicio.findRestaurante(id);

        assertTrue(r1.getPlatos().size()==1);
        assertTrue(r1.getPlatos().get(0).equals(p1));

        servicio.removePlato(id, p1.getNombre());

        r1 = servicio.findRestaurante(id);

        assertTrue(r1.getPlatos().size()==0);


        servicio.removeRestaurante(id);
    }

    @Test
    public void platoDuplicado() throws RepositorioException, EntidadNoEncontrada{

        RestauranteDTO r1 = new RestauranteDTO();
        r1.setNombre("TEST CREATE RESTAURANTE " + UUID.randomUUID());
        r1.setLat(1.0);
        r1.setLng(1.0);
        r1.setCiudad("Murcia");
        r1.setFechaAlta(LocalDate.now());

        String id = servicio.create(r1);

        assertTrue(r1.getPlatos().isEmpty());

        PlatoDTO p1 = new PlatoDTO();
        p1.setNombre("PlatoDTO test");
        p1.setDescripcion("PlatoDTO test");
        p1.setPrecio(5.0);

        servicio.addPlato(id, p1);

        PlatoDTO p2 = new PlatoDTO();
        p2.setNombre("PlatoDTO test");
        p2.setDescripcion("PlatoDTO test 2");
        p2.setPrecio(6.0);

        servicio.addPlato(id, p2);

        r1=servicio.findRestaurante(id);

        assertTrue(r1.getPlatos().size()==1);

        servicio.removeRestaurante(id);
    }

    @Test
    public void actualizarRestaurante() throws RepositorioException, EntidadNoEncontrada{
        RestauranteDTO r1 = new RestauranteDTO();
        r1.setNombre("TEST UPDATE RESTAURANTE " + UUID.randomUUID());
        r1.setLat(1.0);
        r1.setLng(1.0);
        r1.setCiudad("Murcia");
        r1.setFechaAlta(LocalDate.now());

        String id = servicio.create(r1);

        String nuevoNombre = "NUEVO RESTAURANTE " + UUID.randomUUID();

        r1.setNombre(nuevoNombre);
        r1.setId(id);

        servicio.update(r1);

        r1 = servicio.findRestaurante(id);

        System.out.println(r1);

        assertEquals(r1.getNombre(), nuevoNombre);

        servicio.removeRestaurante(id);

    }

    @Test
    public void borrarRestaurante() throws RepositorioException, EntidadNoEncontrada{


        RestauranteDTO r1 = new RestauranteDTO();
        r1.setNombre("TEST BORRAR RESTAURANTE " + UUID.randomUUID());
        r1.setLat(1.0);
        r1.setLng(1.0);
        r1.setCiudad("Murcia");
        r1.setFechaAlta(LocalDate.now());
        
        String id = servicio.create(r1);

        r1.setId(id);
    
        List<RestauranteDTO> restaurantes = servicio.getAllRestaurantes();

        
        servicio.removeRestaurante(id);

        List<RestauranteDTO> restaurantes2 = servicio.getAllRestaurantes();

        System.out.println(restaurantes);
        System.out.println(r1);
    
        assertEquals(restaurantes.size()-1,restaurantes2.size());
        assertTrue(restaurantes.contains(r1));
        assertFalse(restaurantes2.contains(r1));

    }

    @Test
    public void updatePlato() throws RepositorioException, EntidadNoEncontrada, CloneNotSupportedException{

        RestauranteDTO r1 = new RestauranteDTO();
        r1.setNombre("TEST UPDATE RESTAURANTE " + UUID.randomUUID());
        r1.setLat(1.0);
        r1.setLng(1.0);
        r1.setCiudad("Murcia");
        r1.setFechaAlta(LocalDate.now());
        
        String id = servicio.create(r1);

        PlatoDTO p1 = new PlatoDTO();
        p1.setNombre("PlatoDTO test");
        p1.setDescripcion("PlatoDTO test");
        p1.setPrecio(5.0);

        servicio.addPlato(id, p1);

        PlatoDTO p1_original = (PlatoDTO)p1.clone();

        PlatoDTO p2 = new PlatoDTO();
        p2.setNombre("PlatoDTO test2");
        p2.setDescripcion("PlatoDTO test");
        p2.setPrecio(5.0);

        PlatoDTO p2_original = (PlatoDTO)p2.clone();

        servicio.addPlato(id, p2);

        p2.setNombre("NOMBRE CAMBIADO");
        p2.setPrecio(10.0);

        servicio.updatePlato(id, "PlatoDTO test2", p2);

        r1 = servicio.findRestaurante(id);

        assertTrue(r1.getPlatos().contains(p2));

        p1.setDescripcion("DESC CAMBIADA");
        servicio.updatePlato(id, "PlatoDTO test", p1);

        r1 = servicio.findRestaurante(id);

        assertTrue(r1.getPlatos().contains(p1));



        assertFalse(r1.getPlatos().contains(p1_original));
        assertFalse(r1.getPlatos().contains(p2_original));


        servicio.removeRestaurante(id);

    

    }

    
    
}
