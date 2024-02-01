package com.um.arso.events;

import com.um.arso.models.Valoracion;

public class EventoValoracionCreada {
    

    private String opinionID;
    private Valoracion nuevaValoracion;
    private int numValoraciones;
    private double avgValoraciones;


    public String getOpinionID() {
        return this.opinionID;
    }

    public void setOpinionID(String opinionID) {
        this.opinionID = opinionID;
    }

    public Valoracion getNuevaValoracion() {
        return this.nuevaValoracion;
    }

    public void setNuevaValoracion(Valoracion nuevaValoracion) {
        this.nuevaValoracion = nuevaValoracion;
    }

    public int getNumValoraciones() {
        return this.numValoraciones;
    }

    public void setNumValoraciones(int numValoraciones) {
        this.numValoraciones = numValoraciones;
    }

    public double getAvgValoraciones() {
        return this.avgValoraciones;
    }

    public void setAvgValoraciones(double avgValoraciones) {
        this.avgValoraciones = avgValoraciones;
    }


}
