package com.um.arso.repositories.especificacion;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

public class OrSpecification<T> implements Specification<T> {


    private List<Specification<T>> specifications;

    public OrSpecification(List<Specification<T>> specifications) {
        this.specifications = specifications;
    }

    @Override
    public Boolean isSatisfied(T object) {
        return specifications.stream().anyMatch(s -> s.isSatisfied(object));
    }

    @Override
    public Bson toFilter() {

        if(specifications.isEmpty()){
            return Filters.empty();
        }

        List<Bson> filters = specifications.stream().filter(s->!s.toFilter().equals(Filters.empty())).map(s -> s.toFilter()).collect(Collectors.toList());

        return Filters.or(filters);

    }
    
    @Override
    public Comparator<T> getComparator() {
        // Primero pasamos criterios de ordenacion y luego filtros
        return specifications.get(0).getComparator();
    }

    @Override
    public Bson toComparator() {
        // Usamos todos los comparadores por orden
        return Sorts.orderBy(specifications.stream().map(s->s.toComparator()).collect(Collectors.toList()));
    }


    @Override
    public SpecificationType getType() {
        return SpecificationType.GROUP;
    }
}
