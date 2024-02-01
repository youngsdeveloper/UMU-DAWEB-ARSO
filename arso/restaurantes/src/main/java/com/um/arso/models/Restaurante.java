package com.um.arso.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

import com.um.arso.repositories.Identificable;



public class Restaurante implements Identificable, Serializable{

    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    private String id;

    //DONE: Preguntar sobre como almacenar id (si ObjectId o String por lo de Identificable)
    private String nombre;
    private Double lat;
    private Double lng;

    private List<SitioTuristico> sitioTuristicos = new ArrayList<>();

    private List<Plato> platos = new ArrayList<>();

    private String ciudad;
    private LocalDate fechaAlta;

    private Integer numValoraciones;
    private Float avgValoraciones;
    private String idOpinion;

    private String idGestor;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getLat() {
        return this.lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return this.lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public List<SitioTuristico> getSitioTuristicos() {
        return this.sitioTuristicos;
    }

    public void setSitioTuristicos(List<SitioTuristico> sitioTuristicos) {
        this.sitioTuristicos = sitioTuristicos;
    }

    public List<Plato> getPlatos() {
        return platos;
    }
    public void setPlatos(List<Plato> plato) {
        this.platos = plato;
    }
    public boolean addPlato(Plato p){

        if(this.platos.stream().anyMatch(p2->p2.getNombre().equals(p.getNombre()))) return false;

        this.platos.add(p);
        return true;
    }

    public void updatePlato(String nombrePlato, Plato p){

        OptionalInt indexOpt = IntStream.range(0, platos.size())
                                .filter(i -> nombrePlato.equals(platos.get(i).getNombre()))
                                .findFirst();
        
        if(indexOpt.isPresent()){
            this.platos.set(indexOpt.getAsInt(), p);
        }
    }

    public boolean containsPlato(String plato){
        return this.platos.stream().anyMatch(p2->p2.getNombre().equals(plato));
    }

    public void removePlato(String p){
        this.platos.removeIf(p2->p2.getNombre().equals(p));
    }

    public String getCiudad() {
        return ciudad;
    }
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    public LocalDate getFechaAlta() {
        return fechaAlta;
    }
    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Integer getNumValoraciones() {
        return numValoraciones;
    }
    public Float getAvgValoraciones() {
        return avgValoraciones;
    }
    public String getIdOpinion() {
        return idOpinion;
    }

    public void setNumValoraciones(Integer numValoraciones) {
        this.numValoraciones = numValoraciones;
    }
    public void setAvgValoraciones(Float avgValoraciones) {
        this.avgValoraciones = avgValoraciones;
    }
    public void setIdOpinion(String idOpinion) {
        this.idOpinion = idOpinion;
    }

    public String getIdGestor() {
        return idGestor;
    }
    public void setIdGestor(String idGestor) {
        this.idGestor = idGestor;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Restaurante)) {
            return false;
        }
        Restaurante restaurante = (Restaurante) o;
        return Objects.equals(id, restaurante.id) && Objects.equals(nombre, restaurante.nombre) && Objects.equals(lat, restaurante.lat) && Objects.equals(lng, restaurante.lng) && Objects.equals(sitioTuristicos, restaurante.sitioTuristicos) && Objects.equals(platos, restaurante.platos) && Objects.equals(ciudad, restaurante.ciudad) && Objects.equals(fechaAlta, restaurante.fechaAlta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, lat, lng, sitioTuristicos, platos, ciudad, fechaAlta);
    }


    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", lat='" + getLat() + "'" +
            ", lng='" + getLng() + "'" +
            ", sitioTuristicos='" + getSitioTuristicos() + "'" +
            ", platos='" + getPlatos() + "'" +
            ", ciudad='" + getCiudad() + "'" +
            ", fechaAlta='" + getFechaAlta() + "'" +
            "}";
    }

    
}

    