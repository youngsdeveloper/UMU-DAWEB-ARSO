package com.um.arso.repositories.especificacion;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;

public class EqualsSpecification<T> implements Specification<T>{


    private String propiedad;
    private Object valor;



    public EqualsSpecification(String propiedad, Object valor) {
        this.propiedad = propiedad;
        this.valor = valor;
    }
    

    @Override
    public Boolean isSatisfied(T object) {

        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(propiedad, object.getClass());
        
            Object v = descriptor.getReadMethod().invoke(object);
            if (v == valor) {
                return true;
            }
            if (v != null && valor == null || v == null && valor != null) {
                return false;
            }
            return valor.equals(v);

        
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public Bson toFilter() {

        
        return Filters.eq(propiedad, valor);
    }


    @Override
    public Comparator<T> getComparator() {
        return (a1, a2) -> 0; //Empty Comparator
    }


    @Override
    public Bson toComparator() {
        return Filters.empty();
    }
    

    @Override
    public SpecificationType getType() {
        return SpecificationType.FILTER;
    }
}
