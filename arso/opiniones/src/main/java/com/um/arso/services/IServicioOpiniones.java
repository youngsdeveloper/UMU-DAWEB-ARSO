package com.um.arso.services;

import java.util.List;

import com.um.arso.models.Opinion;
import com.um.arso.repositories.EntidadNoEncontrada;
import com.um.arso.repositories.RepositorioException;

public interface IServicioOpiniones {
    



    /** 
	 * Obtener todos los recursos
	 * 
	 * @return Lista de opiniones
	 */
	List<Opinion> findAll() throws RepositorioException;

    /** 
	 * Alta de un recurso
	 * 
	 * @param nombre Nombre del recurso
	 * @return identificador de la opinion
	 */
	String createRecurso(String nombre) throws RepositorioException;

    /** 
	 * Registrar valoracion
	 * 
	 * @param id id de la opinion
     * @param email Correo electrónico del usuario
     * @param califiacion Calificación de 1 a 5
     * @param comentario Comentario (opcional)

	 */
	boolean registrarValoracion(String id, String email, double calificacion, String comentario) throws RepositorioException, EntidadNoEncontrada;


    /** 
	 * Recuperar opinión
	 * 
	 * @param id id de la opinion
	 * @return Opinion

	 */
	Opinion recuperarOpinion(String id) throws RepositorioException, EntidadNoEncontrada;

    /** 
	 * Eliminar opinión
	 * 
	 * @param id id de la opinion
	 * @return Opinion

	 */
	boolean eliminarOpinion(String id) throws RepositorioException, EntidadNoEncontrada;


}
