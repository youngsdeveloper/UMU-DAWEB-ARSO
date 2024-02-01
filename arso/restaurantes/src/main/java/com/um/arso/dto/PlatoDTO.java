package com.um.arso.dto;

import java.util.Objects;

public class PlatoDTO implements Cloneable{
    String nombre;
    String descripcion;
    double precio;


    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return this.precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        PlatoDTO p = new PlatoDTO();
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        return p;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PlatoDTO)) {
            return false;
        }
        PlatoDTO plato = (PlatoDTO) o;
        return Objects.equals(nombre, plato.nombre) && Objects.equals(descripcion, plato.descripcion) && precio == plato.precio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, descripcion, precio);
    }

}
