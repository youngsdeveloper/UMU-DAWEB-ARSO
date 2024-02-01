package com.um.arso.services;

import java.util.List;

import com.um.arso.dto.PlatoDTO;
import com.um.arso.dto.RestauranteDTO;
import com.um.arso.dto.SitioTuristicoDTO;
import com.um.arso.models.Valoracion;
import com.um.arso.repositories.EntidadNoEncontrada;
import com.um.arso.repositories.RepositorioException;


public interface IServicioRestaurantes {

	/** 
	 * Alta de un restaurante
	 * 
	 * @param restaurante debe ser valida respecto al modelo de dominio
	 * @return identificador del restaurante
	 */
	String create(RestauranteDTO restaurante) throws RepositorioException;
	
	/**
	 * Actualización de un restaurante.
	 * @param restaurante debe ser valida respecto al modelo de dominio
	 */
	void update(RestauranteDTO restaurante) throws RepositorioException, EntidadNoEncontrada;
	

    /**
	 * Obtener sitios turísticos próximos.
	 * @param id id de restaurante valido respecto al modelo de dominio
     * @return Lista de sitios turisticos
	 */
	List<SitioTuristicoDTO> getSitiosTuristicosCercanos(String id) throws RepositorioException, EntidadNoEncontrada;


    /**
	 * Establecer sitios turísticos destacados
	 * @param id id de restaurante valido respecto al modelo de dominio
     * @param sitioTuristicos lista de sitios turisticos
	 */
	void setSitiosTuristicos(String id, List<SitioTuristicoDTO> sitioTuristicos) throws RepositorioException, EntidadNoEncontrada;


    /**
	 * Añadir un plato al restaurante
	 * @param id id de restaurante valido respecto al modelo de dominio
     * @param plato Objeto plato
	 */
	void addPlato(String id, PlatoDTO plato) throws RepositorioException, EntidadNoEncontrada;


	/**
	 * Actualizar un plato del restaurante
	 * @param id id de restaurante valido respecto al modelo de dominio
     * @param plato Nombre del plato
	 */
	void updatePlato(String id, String nombrePlato, PlatoDTO plato) throws RepositorioException, EntidadNoEncontrada;

    /**
	 * Borrar un plato del restaurante
	 * @param id id de restaurante valido respecto al modelo de dominio
     * @param plato Nombre del plato
	 */
	void removePlato(String id, String plato) throws RepositorioException, EntidadNoEncontrada;


    /**
	 * Borrar un restaurante
	 * @param id id de restaurante valido respecto al modelo de dominio
	 */
	void removeRestaurante(String id) throws RepositorioException, EntidadNoEncontrada;

    /**
	 * Listado de restaurantes
     * @return Lista de restaurantes
	 */
	List<RestauranteDTO> getAllRestaurantes() throws RepositorioException;


	/**
	 * Obtener restaurante
     * @return Restaurante
	 */
	RestauranteDTO findRestaurante(String id) throws RepositorioException, EntidadNoEncontrada;


	/**
	 * Da de alta un restaurante en el servicio opiniones
     * @return Restaurante
	 */
	void altaOpiniones(RestauranteDTO restauranteDTO) throws RepositorioException, EntidadNoEncontrada;


	/**
	 * Obtener valoraciones de un restaurante por su nombre de recurso
     * @return Restaurante
	 */
	List<Valoracion> getValoraciones(String recurso) throws RepositorioException, EntidadNoEncontrada;


	/**
	 * Obtener valoraciones de un restaurante por su nombre de recurso
     * @return Restaurante
	 */
	List<RestauranteDTO> searchBy(String propiedad, Object valor) throws RepositorioException, EntidadNoEncontrada;

}