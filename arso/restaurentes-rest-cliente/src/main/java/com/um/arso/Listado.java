package com.um.arso;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.um.arso.models.Restaurante;

@XmlRootElement
public class Listado {

	private List<Restaurante> restaurantes;
	
    
    public List<Restaurante> getRestaurantes() {
        return restaurantes;
    }
    public void setRestaurantes(List<Restaurante> restaurantes) {
        this.restaurantes = restaurantes;
    }
}