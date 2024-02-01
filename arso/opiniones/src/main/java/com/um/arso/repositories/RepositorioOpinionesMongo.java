package com.um.arso.repositories;

import com.um.arso.models.Opinion;

public class RepositorioOpinionesMongo extends RepositorioMongo<Opinion> {

    @Override
    public Class<Opinion> getType() {
        return Opinion.class;
    }

    @Override
    public String getDatabaseName() {
        return "arso";       
    }

    @Override
    public String getMongoURL() {
        // Obtener de una variabe de entorno (Docker)
        return System.getenv("MONGO_URL");
    }

    @Override
    public String getCollectionName() {
        return "opinion";
    }

    
    
}
