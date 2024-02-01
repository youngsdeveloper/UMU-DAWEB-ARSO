package com.um.arso.repositories;

import java.util.List;

import com.um.arso.repositories.especificacion.Specification;

/*
 *  Repositorio para entidades gestionadas con identificador.
 *  El parámetro T representa al tipo de datos de la entidad.
 *  El parámetro K es el tipo del identificador.
 */

public interface Repositorio <T, K> {
    
		
    K add(T entity) throws RepositorioException;
    
    void update(T entity) throws RepositorioException, EntidadNoEncontrada;
    
    void delete(T entity) throws RepositorioException, EntidadNoEncontrada;

    T getById(K id) throws RepositorioException, EntidadNoEncontrada;
    
	List<T> getAll() throws RepositorioException;

	List<K> getIds()throws RepositorioException;

    List<T> search(Specification<T> specification) throws RepositorioException;

}