package com.um.arso.models;

import java.util.ArrayList;
import java.util.List;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

import com.um.arso.repositories.Identificable;

public class Opinion implements Identificable{
    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    private String id;
    
    private String recurso;
    
    private List<Valoracion> valoraciones = new ArrayList<>();

    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getRecurso() {
        return this.recurso;
    }

    public void setRecurso(String recurso) {
        this.recurso = recurso;
    }


    public List<Valoracion> getValoraciones() {
        return this.valoraciones;
    }

    public void setValoraciones(List<Valoracion> valoraciones) {
        this.valoraciones = valoraciones;
    }

    public void addValoracion(Valoracion v){
        this.valoraciones.removeIf(v2 -> v2.getEmail().equals(v.getEmail()));
        this.valoraciones.add(v);
    }

    public int getNumValoraciones(){
        return this.valoraciones.size();
    }

    public double getAvgCalificacion(){
        return this.valoraciones
                    .stream()
                    .map(v -> v.getCalificacion())
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0);
    }


    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", recurso='" + getRecurso() + "'" +
            ", valoraciones='" + getValoraciones() + "'" +
            ", avg='" + getAvgCalificacion() + "'" +
            "}";
    }



}
