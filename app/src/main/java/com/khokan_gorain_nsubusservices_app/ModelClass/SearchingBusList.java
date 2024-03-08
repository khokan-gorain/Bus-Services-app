package com.khokan_gorain_nsubusservices_app.ModelClass;

public class SearchingBusList {

    int id,status,bus_number,dv_id;
    Double latitude_gmap,longitude_gmap;
    String dv_name,dv_phone_no,from_area,to_area,dv_profile_img;

    public SearchingBusList(int id, int status, int bus_number, int dv_id, Double latitude_gmap,
                            Double longitude_gmap, String dv_name, String dv_phone_no, String from_area,
                            String to_area, String dv_profile_img) {
        this.id = id;
        this.status = status;
        this.bus_number = bus_number;
        this.dv_id = dv_id;
        this.latitude_gmap = latitude_gmap;
        this.longitude_gmap = longitude_gmap;
        this.dv_name = dv_name;
        this.dv_phone_no = dv_phone_no;
        this.from_area = from_area;
        this.to_area = to_area;
        this.dv_profile_img = dv_profile_img;
    }

    public String getDv_profile_img() {
        return dv_profile_img;
    }

    public void setDv_profile_img(String dv_profile_img) {
        this.dv_profile_img = dv_profile_img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getBus_number() {
        return bus_number;
    }

    public void setBus_number(int bus_number) {
        this.bus_number = bus_number;
    }

    public int getDv_id() {
        return dv_id;
    }

    public void setDv_id(int dv_id) {
        this.dv_id = dv_id;
    }

    public Double getLatitude_gmap() {
        return latitude_gmap;
    }

    public void setLatitude_gmap(Double latitude_gmap) {
        this.latitude_gmap = latitude_gmap;
    }

    public Double getLongitude_gmap() {
        return longitude_gmap;
    }

    public void setLongitude_gmap(Double longitude_gmap) {
        this.longitude_gmap = longitude_gmap;
    }

    public String getDv_name() {
        return dv_name;
    }

    public void setDv_name(String dv_name) {
        this.dv_name = dv_name;
    }

    public String getDv_phone_no() {
        return dv_phone_no;
    }

    public void setDv_phone_no(String dv_phone_no) {
        this.dv_phone_no = dv_phone_no;
    }

    public String getFrom_area() {
        return from_area;
    }

    public void setFrom_area(String from_area) {
        this.from_area = from_area;
    }

    public String getTo_area() {
        return to_area;
    }

    public void setTo_area(String to_area) {
        this.to_area = to_area;
    }
}
