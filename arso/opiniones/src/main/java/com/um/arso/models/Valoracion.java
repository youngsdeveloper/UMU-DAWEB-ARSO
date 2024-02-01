package com.um.arso.models;

import java.util.Objects;

public class Valoracion {
    private String email;
    private String fechaRegistro;
    private double calificacion;
    private String comentario;


    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFechaRegistro() {
        return this.fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public double getCalificacion() {
        return this.calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return this.comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Valoracion)) {
            return false;
        }
        Valoracion valoracion = (Valoracion) o;
        return Objects.equals(email, valoracion.email) && Objects.equals(fechaRegistro, valoracion.fechaRegistro) && calificacion == valoracion.calificacion && Objects.equals(comentario, valoracion.comentario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, fechaRegistro, calificacion, comentario);
    }

}
