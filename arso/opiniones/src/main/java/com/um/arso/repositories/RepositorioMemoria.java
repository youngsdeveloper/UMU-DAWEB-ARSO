package com.um.arso.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.um.arso.repositories.especificacion.Specification;
import com.um.arso.utils.Utils;


public class RepositorioMemoria<T extends Identificable> implements RepositorioString<T> {

	private HashMap<String, T> entidades = new HashMap<>();
	
	@Override
	public String add(T entity) throws RepositorioException {
		
		String id = Utils.createId();
		entity.setId(id);
		this.entidades.put(id, entity);		
		
		return id;
	}

	@Override
	public void update(T entity) throws RepositorioException, EntidadNoEncontrada {
		
		if (! this.entidades.containsKey(entity.getId()))
			throw new EntidadNoEncontrada(entity.getId() + " no existe en el repositorio");
		
		this.entidades.put(entity.getId(), entity);
	}

	@Override
	public void delete(T entity) throws RepositorioException, EntidadNoEncontrada {
		
		if (! this.entidades.containsKey(entity.getId()))
			throw new EntidadNoEncontrada(entity.getId() + " no existe en el repositorio");
		
		this.entidades.remove(entity.getId());
	}

	@Override
	public T getById(String id) throws RepositorioException, EntidadNoEncontrada {
		
		if (! this.entidades.containsKey(id))
			throw new EntidadNoEncontrada(id + " no existe en el repositorio");
		
		return this.entidades.get(id);
	}

	@Override
	public List<T> getAll() throws RepositorioException {
		
		return new ArrayList<>(this.entidades.values());
	}

	@Override
	public List<String> getIds() throws RepositorioException {
		
		return new ArrayList<>(this.entidades.keySet());
	}

	@Override
	public List<T> search(Specification<T> specification) throws RepositorioException {

		List<T> result_list = new ArrayList<T>();

		for(T object:getAll()){
			if(specification.isSatisfied(object)){
				result_list.add(object);
			}
		}

		result_list.sort(specification.getComparator());
		
		return result_list;

	}

}