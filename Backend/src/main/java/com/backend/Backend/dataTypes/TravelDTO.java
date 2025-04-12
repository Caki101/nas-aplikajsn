package com.backend.Backend.dataTypes;

public class TravelDTO {
    private String date;
    private String place;

    public TravelDTO() {
    }

    public TravelDTO(String date, String place) {
        this.date = date;
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
