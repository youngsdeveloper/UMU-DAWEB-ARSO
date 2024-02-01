package com.um.arso.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RestauranteDTO {
    private String id;
    private String nombre;
    private Double lat;
    private Double lng;

    private List<SitioTuristicoDTO> sitioTuristicos = new ArrayList<>();

    private List<PlatoDTO> platos = new ArrayList<>();

    private String ciudad;
    private LocalDate fechaAlta;

    private Integer numValoraciones;
    private Float avgValoraciones;
    private String idOpinion;
    private String idGestor;


    public String getId() {
        return id;
    }
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

    public List<SitioTuristicoDTO> getSitioTuristicos() {
        return this.sitioTuristicos;
    }

    public void setSitioTuristicos(List<SitioTuristicoDTO> sitioTuristicos) {
        this.sitioTuristicos = sitioTuristicos;
    }

    public List<PlatoDTO> getPlatos() {
        return this.platos;
    }

    public void setPlatos(List<PlatoDTO> platos) {
        this.platos = platos;
    }

    public String getCiudad() {
        return this.ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public LocalDate getFechaAlta() {
        return this.fechaAlta;
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
        if (!(o instanceof RestauranteDTO)) {
            return false;
        }
        RestauranteDTO restaurante = (RestauranteDTO) o;
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
