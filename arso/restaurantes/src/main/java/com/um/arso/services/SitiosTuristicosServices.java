package com.um.arso.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.um.arso.models.PlaceDetails;
import com.um.arso.models.SitioTuristico;

public class SitiosTuristicosServices {
    
    private static SitiosTuristicosServices instance;

    public static SitiosTuristicosServices getInstance() {
        if(instance==null)
            instance = new SitiosTuristicosServices();
        return instance;
    }

    public List<SitioTuristico> findByPostalCode(int postal_code){

        List<SitioTuristico> places = new LinkedList<>();

        // 1. Obtener una factoría
        DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();

        // 2. Pedir a la factoría la construcción del analizador
        try {
            DocumentBuilder analizador = factoria.newDocumentBuilder();
            Document documento = analizador.parse("http://api.geonames.org/findNearbyWikipedia?username=arsoJavierJH&postalcode="+String.valueOf(postal_code)+"&country=es&lang=es");    
            
            NodeList elementos = documento.getDocumentElement().getElementsByTagName("entry");
            for(int i=0;i<elementos.getLength();i++){
                Element elemento = (Element)elementos.item(i);
                
                // Crear objeto SitioTuristico

                String wikiURL = elemento.getElementsByTagName("wikipediaUrl").item(0).getTextContent();
                wikiURL = URLDecoder.decode(wikiURL, StandardCharsets.UTF_8);

                SitioTuristico place = new SitioTuristico();
                place.setTitle(elemento.getElementsByTagName("title").item(0).getTextContent());
                place.setSummary(elemento.getElementsByTagName("summary").item(0).getTextContent());
                place.setLat(Double.parseDouble(elemento.getElementsByTagName("lat").item(0).getTextContent()));
                place.setLng(Double.parseDouble(elemento.getElementsByTagName("lng").item(0).getTextContent()));
                place.setDistance(Double.parseDouble(elemento.getElementsByTagName("distance").item(0).getTextContent()));
                place.setWikiURL(wikiURL);
                places.add(place);
            }

        
        } catch (Exception e) {
            // TODO: handle exception
        }

        return places;
    }

    public List<SitioTuristico> findByLocation(Double lat, Double lng){

        List<SitioTuristico> places = new LinkedList<>();

        // 1. Obtener una factoría
        DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();

        // 2. Pedir a la factoría la construcción del analizador
        try {
            DocumentBuilder analizador = factoria.newDocumentBuilder();
            Document documento = analizador.parse("http://api.geonames.org/findNearbyWikipedia?username=arsoJavierJH&lat="+String.valueOf(lat)+"&lng="+String.valueOf(lng)+"&country=es&lang=es");    
            
            NodeList elementos = documento.getDocumentElement().getElementsByTagName("entry");
            for(int i=0;i<elementos.getLength();i++){
                Element elemento = (Element)elementos.item(i);
                
                // Crear objeto SitioTuristico

                String wikiURL = elemento.getElementsByTagName("wikipediaUrl").item(0).getTextContent();
                wikiURL = URLDecoder.decode(wikiURL, StandardCharsets.UTF_8);

                SitioTuristico place = new SitioTuristico();
                place.setTitle(elemento.getElementsByTagName("title").item(0).getTextContent());
                place.setSummary(elemento.getElementsByTagName("summary").item(0).getTextContent());
                place.setLat(Double.parseDouble(elemento.getElementsByTagName("lat").item(0).getTextContent()));
                place.setLng(Double.parseDouble(elemento.getElementsByTagName("lng").item(0).getTextContent()));
                place.setDistance(Double.parseDouble(elemento.getElementsByTagName("distance").item(0).getTextContent()));
                place.setWikiURL(wikiURL);
                places.add(place);
            }

        
        } catch (Exception e) {
            // TODO: handle exception
        }

        return places;
    }

    public PlaceDetails finPlaceDetails(SitioTuristico p){
        PlaceDetails pd = new PlaceDetails();

        try {

            String wikiKey = p.getWikiURL().split("/wiki/")[1];
            InputStream input = new URL("https://es.dbpedia.org/data/"+wikiKey+".json").openStream();
            JsonReader jsonReader = Json.createReader(input);

            JsonObject obj = jsonReader.readObject();

            JsonObject resource = obj.getJsonObject("http://es.dbpedia.org/resource/"+wikiKey);


            JsonArray resumeJsonArray = resource.getJsonArray("http://dbpedia.org/ontology/abstract");
            if(resumeJsonArray!=null){
                pd.setResumen(resumeJsonArray.getJsonObject(0).getJsonString("value").getString());
            }
            
            

            List<String> cats = new LinkedList<String>();

            JsonArray categorias = resource.getJsonArray("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
            if(categorias!=null){
                for(JsonObject categoria:categorias.getValuesAs(JsonObject.class)){
                    cats.add(categoria.getJsonString("value").getString());
                }
                pd.setCategorias(cats);
            }
            

            

            List<String> links = new LinkedList<String>();

            JsonArray enlaces = resource.getJsonArray("http://dbpedia.org/ontology/wikiPageExternalLink");
            if(enlaces!=null){
                for(JsonObject enlace:enlaces.getValuesAs(JsonObject.class)){
                    links.add(enlace.getJsonString("value").getString());
                }
    
                pd.setEnlaces(links);
            }
            
            JsonArray imagenJsonArray = resource.getJsonArray("http://es.dbpedia.org/property/imagen");
            
            if(imagenJsonArray!=null){
                pd.setImagen(imagenJsonArray.getJsonObject(0).getJsonString("value").getString());
            }
                


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return pd;
    }

}
