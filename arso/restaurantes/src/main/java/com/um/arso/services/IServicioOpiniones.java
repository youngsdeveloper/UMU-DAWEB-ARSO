package com.um.arso.services;


import java.util.List;
import com.um.arso.models.*;

public interface IServicioOpiniones {

    public String darAltaOpinion(String recurso);
    public List<Valoracion> getValoraciones(String recurso);
}