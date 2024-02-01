package com.um.arso;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.um.arso.models.Plato;
import com.um.arso.models.Restaurante;
import com.um.arso.models.SitioTuristico;

import retrofit2.Response;



public class TestRest {


    static ApiInterface apiInterface;

    @BeforeAll
	public static void setUp() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        
	}

    public Restaurante mockRestaurante(){
        // Localización: Plaza Circular (Murcia)
        Restaurante r = new Restaurante();
        r.setLat(37.9916987); 
        r.setLng(-1.1323049);
        r.setNombre("Restaurante de TEST: "+UUID.randomUUID());
        r.setCiudad("Murcia");
        r.setFechaAlta(LocalDate.now());
        return r;
    }

    public Plato mockPlato(){
        Plato p = new Plato();
        p.setNombre("Paella de marisco, TEST: " + UUID.randomUUID());
        p.setDescripcion("Random description");
        p.setPrecio(20.0);
        return p;
    }

    @Test
    public void createRestaurante(){
        try {
            
            // Probamos que no deja crear un restaurante si nombre
            Restaurante r_test = new Restaurante();
            Response<Void> res = apiInterface.createRestaurante(r_test).execute();

            assertEquals(res.code(), 400);


            r_test.setNombre("Nombre");
            res = apiInterface.createRestaurante(r_test).execute();
            assertEquals(res.code(), 400);

            r_test.setCiudad("Murcia");
            res = apiInterface.createRestaurante(r_test).execute();
            assertEquals(res.code(), 400);

            r_test.setCiudad(null);
            r_test.setLat(0.0);
            r_test.setLng(0.0);
            res = apiInterface.createRestaurante(r_test).execute();
            assertEquals(res.code(), 400);

            res = apiInterface.createRestaurante(mockRestaurante()).execute();
            assertEquals(res.code(), 201);

            String location = res.headers().get("Location");
            assertNotNull(location);

            String[] location_array = location.split("/");

            String id = location_array[location_array.length-1];


            Response<Restaurante> res2 = apiInterface.getRestaurante(id).execute();
            assertEquals(res2.code(), 200);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getRestaurante(){
        try {

            // Primero probamos un ID que no es valido
            Response<Restaurante> res = apiInterface.
                                                getRestaurante("id no valido")
                                                .execute();

            assertEquals(res.code(),400);

            // Ahora probamos un id que tiene un formato válido pero que no existe

            res = apiInterface.
                                                getRestaurante("643426735a59cf54589e48dd")
                                                .execute();

            assertEquals(res.code(),404);

            // Por ultimo probamos que devuelve bien un restaurante aleatorio

            Restaurante random = mockRestaurante();

            Response<Void> res2 = apiInterface.createRestaurante(random).execute();

            String[] location_path = res2.headers().get("Location").split("/");

            String id = location_path[location_path.length-1];

            random.setId(id);

            Restaurante restaurante = apiInterface.
                                                getRestaurante(id)
                                                .execute()
                                                .body();

            assertEquals(restaurante, random);

            assertNotNull(restaurante.getCiudad());
            assertNotNull(restaurante.getFechaAlta());
            assertNotNull(restaurante.getId());
            assertNotNull(restaurante.getLat());
            assertNotNull(restaurante.getLng());
            assertNotNull(restaurante.getPlatos());
            assertNotNull(restaurante.getSitioTuristicos());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void eliminarRestaurante(){
        try {

             // Primero probamos un ID que no es valido
             Response<Void> res = apiInterface.
             deleteRestaurante("id no valido")
             .execute();

            assertEquals(res.code(),400);

            // Ahora probamos un id que tiene un formato válido pero que no existe

            res = apiInterface.
                        deleteRestaurante("643426735a59cf54589e48dd")
                        .execute();

            assertEquals(res.code(),404);

            // Por ultimo probamos que elimina bien un restaurante aleatorio

            Restaurante random = mockRestaurante();

            res = apiInterface.createRestaurante(random).execute();

            String[] location_path = res.headers().get("Location").split("/");

            String id = location_path[location_path.length-1];

            res = apiInterface.deleteRestaurante(id).execute();
            assertEquals(res.code(), 204);

            Response<Restaurante> res2 = apiInterface.getRestaurante(id).execute();
            assertEquals(res2.code(), 404);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void actualizarRestaurante(){
        try {

             // Primero probamos un ID que no es valido
            Response<Void> res = apiInterface.
             updateRestaurante("id no valido", new Restaurante())
             .execute();

            assertEquals(res.code(),400);


            Restaurante r1 = new Restaurante();
            r1.setId("123");
            res = apiInterface.
             updateRestaurante("321", r1)
             .execute();

             assertEquals(res.code(),400);


            // Ahora probamos un id que tiene un formato válido pero que no existe
            Restaurante r2 = new Restaurante();
            r2.setId("643426735a59cf54589e48dd");
            res = apiInterface.
                        updateRestaurante("643426735a59cf54589e48dd",r2)
                        .execute();

            assertEquals(res.code(),404);

            // Vamos a crear un restaurante inicial aleatorio

            Restaurante random = mockRestaurante();

            res = apiInterface.createRestaurante(random).execute();

            String[] location_path = res.headers().get("Location").split("/");

            String id = location_path[location_path.length-1];
            random.setId(id);

            // Vamos a probar como se actualizan algunos campos

            // Probamos a actualizar el nombre
            random.setNombre("nombre actualizado!!");
            res = apiInterface.updateRestaurante(id,random).execute();
            assertEquals(res.code(), 204);
            Response<Restaurante> res2 = apiInterface.getRestaurante(id).execute();
            assertEquals(res2.body(), random);

            // Probamos a actualizar el ciudad
            random.setCiudad("ciudad actualizado!!");
            res = apiInterface.updateRestaurante(id,random).execute();
            assertEquals(res.code(), 204);
            res2 = apiInterface.getRestaurante(id).execute();
            assertEquals(res2.body(), random);

            // Probamos a actualizar la latitud
            random.setLat(15.0);
            res = apiInterface.updateRestaurante(id,random).execute();
            assertEquals(res.code(), 204);
            res2 = apiInterface.getRestaurante(id).execute();
            assertEquals(res2.body(), random);

            // Probamos a actualizar la longitud
            random.setLng(18.0);
            res = apiInterface.updateRestaurante(id,random).execute();
            assertEquals(res.code(), 204);
            res2 = apiInterface.getRestaurante(id).execute();
            assertEquals(res2.body(), random);



        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Test
    public void getRestaurantes(){
        try {

            // Primero probamos un ID que no es valido
            List<Restaurante> restaurantes = apiInterface.getRestaurantes().execute().body();
                    
            assertNotNull(restaurantes);

            Restaurante random = mockRestaurante();
            apiInterface.createRestaurante(random).execute();

            List<Restaurante> restaurantes2 = apiInterface.getRestaurantes().execute().body();


            assertEquals(restaurantes.size()+1, restaurantes2.size());

            for (Restaurante restaurante : restaurantes2) {
                assertNotNull(restaurante.getCiudad());
                assertNotNull(restaurante.getFechaAlta());
                assertNotNull(restaurante.getId());
                assertNotNull(restaurante.getLat());
                assertNotNull(restaurante.getLng());
                assertNotNull(restaurante.getPlatos());
                assertNotNull(restaurante.getSitioTuristicos());
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void getSitioTuristicos(){
        try {


            // Primero probamos un ID que no es valido
            Response<List<SitioTuristico>> res = apiInterface.
                getSitiosTuristicos("id_no_valido")
                .execute();

            assertEquals(res.code(),400);

            // Ahora probamos un id que tiene un formato válido pero que no existe
            res = apiInterface.
                        getSitiosTuristicos("643426735a59cf54589e48dd")
                        .execute();

            assertEquals(res.code(),404);

            // Creamos un restaurante random

            Restaurante random = mockRestaurante();

            Response<Void> res2 = apiInterface.createRestaurante(random).execute();
            String[] location_path = res2.headers().get("Location").split("/");
            String id = location_path[location_path.length-1];

            // Ahora vemos que devuelve la lista para ese restaurante

            List<SitioTuristico> sitios = apiInterface.getSitiosTuristicos(id).execute().body();

            assertFalse(sitios.isEmpty()); // Debe devolver al menos uno (plaza circular)

            for(SitioTuristico s:sitios){
                assertNotNull(s.getTitle());
                assertNotNull(s.getSummary());
                assertNotNull(s.getLat());
                assertNotNull(s.getLng());
                assertNotNull(s.getWikiURL());
                assertNotNull(s.getDistance());

            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void setSitioTuristicos(){
        try {


            // Primero probamos un ID que no es valido
            Response<Void> res = apiInterface.
                setSitiosTuristicos("id_no_valido",new ArrayList<>())
                .execute();

            assertEquals(res.code(),400);

            // Ahora probamos un id que tiene un formato válido pero que no existe
            res = apiInterface.
                        setSitiosTuristicos("643426735a59cf54589e48dd",new ArrayList<>())
                        .execute();

            assertEquals(res.code(),404);

            // Creamos un restaurante random

            Restaurante random = mockRestaurante();

            res = apiInterface.createRestaurante(random).execute();
            String[] location_path = res.headers().get("Location").split("/");
            String id = location_path[location_path.length-1];
            random.setId(id);
            // Ahora vamos a agregar un sitio a ese restaurante

            List<SitioTuristico> sitios = apiInterface.getSitiosTuristicos(id).execute().body();

            res = apiInterface.setSitiosTuristicos(id, sitios).execute();

            assertEquals(res.code(), 204);

            Restaurante restaurante = apiInterface.getRestaurante(id).execute().body();

            random.setSitioTuristicos(sitios);

            assertEquals(restaurante, random);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void crearPlato(){
        try {


            // Primero probamos un ID que no es valido
            Response<Void> res = apiInterface.
                crearPlato("id_no_valido",new Plato())
                .execute();

            assertEquals(res.code(),400);

            // Ahora probamos un id que tiene un formato válido pero que no existe
            res = apiInterface.
                        crearPlato("643426735a59cf54589e48dd",new Plato())
                        .execute();

            assertEquals(res.code(),404);

            // Creamos un restaurante random

            Restaurante random = mockRestaurante();

            res = apiInterface.createRestaurante(random).execute();
            String[] location_path = res.headers().get("Location").split("/");
            String id = location_path[location_path.length-1];
            random.setId(id);


            // Ahora vamos a agregar un plato a ese restaurante
            Plato plato_random = mockPlato();
            res = apiInterface.crearPlato(id, plato_random).execute();
            assertEquals(res.code(), 204);

            Restaurante restaurante = apiInterface.getRestaurante(id).execute().body();
            assertTrue(restaurante.getPlatos().contains(plato_random));


            // Si volvemos a insertar el mismo plato, no debería insertarse
            res = apiInterface.crearPlato(id, plato_random).execute();
            assertEquals(res.code(), 204);

            Restaurante restaurante2 = apiInterface.getRestaurante(id).execute().body();
            assertEquals(restaurante.getPlatos(), restaurante2.getPlatos());

            // Si insertamos plato diferente, deberiamos tener dos platos.
            Plato plato_random_2 = mockPlato();
            res = apiInterface.crearPlato(id, plato_random_2).execute();
            assertEquals(res.code(), 204);

            Restaurante restaurante3 = apiInterface.getRestaurante(id).execute().body();
            assertEquals(restaurante3.getPlatos().size(),2);
            assertTrue(restaurante3.getPlatos().contains(plato_random));
            assertTrue(restaurante3.getPlatos().contains(plato_random_2));


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void eliminarPlato(){
        try {


            // Primero probamos un ID que no es valido
            Response<Void> res = apiInterface.
                eliminarPlato("id_no_valido","plato_inexistente")
                .execute();

            assertEquals(res.code(),400);

            // Ahora probamos un id que tiene un formato válido pero que no existe
            res = apiInterface.
                        eliminarPlato("643426735a59cf54589e48dd","plato_inexistente")
                        .execute();

            assertEquals(res.code(),404);

            // Creamos un restaurante random

            Restaurante random = mockRestaurante();

            res = apiInterface.createRestaurante(random).execute();
            String[] location_path = res.headers().get("Location").split("/");
            String id = location_path[location_path.length-1];
            random.setId(id);

            // Ahora probamos a eliminar un plato inexistente de un restaurante que si existe
            res = apiInterface.
                        eliminarPlato(id,"plato que no existe")
                        .execute();

            assertEquals(res.code(),404);


            // Ahora vamos a agregar un plato a ese restaurante
            Plato plato_random = mockPlato();
            res = apiInterface.crearPlato(id, plato_random).execute();
            assertEquals(res.code(), 204);

            // Eliminamos ese plato
            res = apiInterface.
                        eliminarPlato(id,plato_random.getNombre())
                        .execute();

            assertEquals(res.code(),204);

            Restaurante restaurante = apiInterface.getRestaurante(id).execute().body();
            assertTrue(restaurante.getPlatos().isEmpty());




        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void actualizarPlato(){
        try {


            // Primero probamos un ID que no es valido
            Response<Void> res = apiInterface.
                actualizarPlato("id_no_valido","plato_inexistente", new Plato())
                .execute();

            assertEquals(res.code(),400);

            // Ahora probamos un id que tiene un formato válido pero que no existe
            res = apiInterface.
                            actualizarPlato("643426735a59cf54589e48dd","plato_inexistente", new Plato())
                        .execute();

            assertEquals(res.code(),404);

            // Creamos un restaurante random

            Restaurante random = mockRestaurante();

            res = apiInterface.createRestaurante(random).execute();
            String[] location_path = res.headers().get("Location").split("/");
            String id = location_path[location_path.length-1];
            random.setId(id);

            // Ahora vamos a agregar un plato a ese restaurante
            Plato plato_random = mockPlato();
            res = apiInterface.crearPlato(id, plato_random).execute();
            assertEquals(res.code(), 204);



            // Actualizamos ese plato

            // 1.1 Actualizamos descripcion
            plato_random.setDescripcion("Descripcion actualizada");
            res = apiInterface.
                        actualizarPlato(id, plato_random.getNombre(), plato_random)
                        .execute();
            assertEquals(res.code(),204);
            Restaurante restaurante = apiInterface.getRestaurante(id).execute().body();
            assertTrue(restaurante.getPlatos().contains(plato_random));


            // 1.2 Actualizamos precio
            plato_random.setPrecio(99.99);
            res = apiInterface.
                        actualizarPlato(id, plato_random.getNombre(), plato_random)
                        .execute();
            assertEquals(res.code(),204);
            restaurante = apiInterface.getRestaurante(id).execute().body();
            assertTrue(restaurante.getPlatos().contains(plato_random));

            // 1.3 Actualizamos nombre
            String nombre_antiguo = plato_random.getNombre();
            plato_random.setNombre("Nombre nuevo del plato");
            res = apiInterface.
                        actualizarPlato(id, nombre_antiguo, plato_random)
                        .execute();
            assertEquals(res.code(),204);
            restaurante = apiInterface.getRestaurante(id).execute().body();
            assertTrue(restaurante.getPlatos().contains(plato_random));

            // 1.4 Comprobamos que no podemos actualizar con nombre antiguo
            res = apiInterface.
                        actualizarPlato(id, nombre_antiguo, plato_random)
                        .execute();
            assertEquals(res.code(),404);






        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    





    



}
