package com.um.arso.repositories;

import java.io.IOException;
import com.um.arso.models.Restaurante;
import com.um.arso.utils.PropertiesReader;

public class RepositorioRestaurantesMongo extends RepositorioMongo<Restaurante> {

    @Override
    public Class<Restaurante> getType() {
        return Restaurante.class;
    }


    @Override
    public String getDatabaseName() {
        return "arso";
    }

    @Override
    public String getMongoURL() {

        // Es el ENV o lo que haya en la propiedad...

        
		String mongoURL = System.getenv("MONGO_URL");

		if(mongoURL==null){
			PropertiesReader propertiesReader;
			try {
				propertiesReader = new PropertiesReader("application.properties");
				mongoURL = propertiesReader.getProperty("MONGO_URL");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

        return mongoURL;
    }

    @Override
    public String getCollectionName() {
        return "restaurante";
    }

    
}
