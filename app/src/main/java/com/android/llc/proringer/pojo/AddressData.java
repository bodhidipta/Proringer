package com.android.llc.proringer.pojo;

/**
 * Created by manishsethia on 11/07/17.
 */

public class AddressData {
    private String formatted_address = "";
    private String state_code = "";
    private String country_code = "";
    private String zip_code = "";
    private String city="";
    private String latitude = "";
    private String longitude = "";

    public AddressData(String formatted_address, String state_code, String country_code, String zip_code) {
        this.formatted_address = formatted_address;
        this.state_code = state_code;
        this.country_code = country_code;
        this.zip_code = zip_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public String getState_code() {
        return state_code;
    }

    public String getCountry_code() {
        return country_code;
    }

    public String getZip_code() {
        return zip_code;
    }
}
