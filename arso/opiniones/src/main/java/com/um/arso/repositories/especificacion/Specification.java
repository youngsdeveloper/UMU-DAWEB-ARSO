package com.um.arso.repositories.especificacion;

import java.util.Comparator;

import org.bson.conversions.Bson;

public interface Specification<T> {
    Boolean isSatisfied(T object);
    Bson toFilter();
    SpecificationType getType();

    Comparator<T> getComparator();
    Bson toComparator();

}

