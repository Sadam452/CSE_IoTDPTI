package com.example.alwaqt;
public class MainModel {
    String asrAzaan,asrNamaaz,fajrAzaan,fajrNamaaz,ishaAzaan,ishaNamaaz,
            jummah,location,maghribAzaan,maghribNamaaz,mosqueID,mosqueName,sunrise,sunset,zuharAzaan,zuharNamaaz;
    MainModel(){
        //default constructor, do's nothing
    }
    public MainModel(String asrAzaan, String asrNamaaz, String fajrAzaan, String fajrNamaaz, String ishaAzaan, String ishaNamaaz, String jummah, String location, String maghribAzaan, String maghribNamaaz,String mosqueID, String mosqueName, String sunrise, String sunset, String zuharAzaan, String zuharNamaaz) {
        this.asrAzaan = asrAzaan;
        this.asrNamaaz = asrNamaaz;
        this.fajrAzaan = fajrAzaan;
        this.fajrNamaaz = fajrNamaaz;
        this.ishaAzaan = ishaAzaan;
        this.ishaNamaaz = ishaNamaaz;
        this.jummah = jummah;
        this.location = location;
        this.maghribAzaan = maghribAzaan;
        this.maghribNamaaz = maghribNamaaz;
        this.mosqueID = mosqueID;
        this.mosqueName = mosqueName;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.zuharAzaan = zuharAzaan;
        this.zuharNamaaz = zuharNamaaz;
    }

    public String getAsrAzaan() {
        return asrAzaan;
    }

    public void setAsrAzaan(String asrAzaan) {
        this.asrAzaan = asrAzaan;
    }

    public String getAsrNamaaz() {
        return asrNamaaz;
    }

    public void setAsrNamaaz(String asrNamaaz) {
        this.asrNamaaz = asrNamaaz;
    }

    public String getFajrAzaan() {
        return fajrAzaan;
    }

    public void setFajrAzaan(String fajrAzaan) {
        this.fajrAzaan = fajrAzaan;
    }

    public String getFajrNamaaz() {
        return fajrNamaaz;
    }

    public void setFajrNamaaz(String fajrNamaaz) {
        this.fajrNamaaz = fajrNamaaz;
    }

    public String getIshaAzaan() {
        return ishaAzaan;
    }

    public void setIshaAzaan(String ishaAzaan) {
        this.ishaAzaan = ishaAzaan;
    }

    public String getIshaNamaaz() {
        return ishaNamaaz;
    }

    public void setIshaNamaaz(String ishaNamaaz) {
        this.ishaNamaaz = ishaNamaaz;
    }

    public String getJummah() {
        return jummah;
    }

    public void setJummah(String jummah) {
        this.jummah = jummah;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMaghribAzaan() {
        return maghribAzaan;
    }

    public void setMaghribAzaan(String maghribAzaan) {
        this.maghribAzaan = maghribAzaan;
    }

    public String getMaghribNamaaz() {
        return maghribNamaaz;
    }

    public void setMaghribNamaaz(String maghribNamaaz) {
        this.maghribNamaaz = maghribNamaaz;
    }

    public String getMosqueName() {
        return mosqueName;
    }

    public void setMosqueName(String mosqueName) {
        this.mosqueName = mosqueName;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getZuharAzaan() {
        return zuharAzaan;
    }

    public void setZuharAzaan(String zuharAzaan) {
        this.zuharAzaan = zuharAzaan;
    }

    public String getZuharNamaaz() {
        return zuharNamaaz;
    }

    public void setZuharNamaaz(String zuharNamaaz) {
        this.zuharNamaaz = zuharNamaaz;
    }

    public String getMosqueID() {
        return mosqueID;
    }

    public void setMosqueID(String mosqueID) {
        this.mosqueID = mosqueID;
    }

}
