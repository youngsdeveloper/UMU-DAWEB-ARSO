package com.um.arso.models;


public class SitioTuristico {

    private String title;
    private String summary;
    private Double lat;
    private Double lng;
    private String wikiURL;
    private Double distance;
    
    private PlaceDetails placeDetails;
    //DONE Revisar si unir dos clases y renombrar

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public String getWikiURL() {
        return this.wikiURL;
    }

    public void setWikiURL(String wikiURL) {
        this.wikiURL = wikiURL;
    }

    public Double getDistance() {
        return this.distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public PlaceDetails getPlaceDetails() {
        return placeDetails;
    }
    public void setPlaceDetails(PlaceDetails placeDetails) {
        this.placeDetails = placeDetails;
    }



    @Override
    public String toString() {
        return "{" +
            " title='" + getTitle() + "'" +
            ", summary='" + getSummary() + "'" +
            ", lat='" + getLat() + "'" +
            ", lng='" + getLng() + "'" +
            ", wikiURL='" + getWikiURL() + "'" +
            ", distance='" + getDistance() + "'" +
            ", placeDetails='" + getPlaceDetails() + "'" +
            "}";
    }
   

}
