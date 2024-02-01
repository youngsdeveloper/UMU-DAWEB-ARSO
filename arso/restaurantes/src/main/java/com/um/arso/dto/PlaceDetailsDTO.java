package com.um.arso.dto;

import java.util.List;

public class PlaceDetailsDTO {
    private String resumen;
    private List<String> categorias;
    private List<String> enlaces;
    private String imagen;


    public String getResumen() {
        return this.resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public List<String> getCategorias() {
        return this.categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public List<String> getEnlaces() {
        return this.enlaces;
    }

    public void setEnlaces(List<String> enlaces) {
        this.enlaces = enlaces;
    }

    public String getImagen() {
        return this.imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }


    
}
