package com.um.arso.repositories.especificacion;

import java.util.Comparator;

import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.um.arso.models.Restaurante;

public class OrderByNombreDesc implements Specification<Restaurante> {

    @Override
    public Boolean isSatisfied(Restaurante object) {
        return null;
    }

    @Override
    public Bson toFilter() {
        return Filters.empty();
    }

    @Override
    public Comparator<Restaurante> getComparator() {
        return (Restaurante r1,Restaurante r2) -> r2.getNombre().compareTo(r1.getNombre());
    }

    @Override
    public Bson toComparator() {
        return Sorts.descending("nombre");
    }


    @Override
    public SpecificationType getType() {
        return SpecificationType.COMPARATOR;
    }
    
}
