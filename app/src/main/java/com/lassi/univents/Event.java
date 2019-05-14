package com.lassi.univents;

//Class to create a single event for the recyclerView.

import java.util.Date;

public class Event {
    private String e_name;
    private Date e_date;
    private String e_photo;
    private String e_location;
    private String e_type;
    private String e_description;
//    private int e_id;
    private int e_price;

    public Event() {
        // empty
    }

    public Event(String e_name, Date e_date, String e_photo, String e_location, String e_type, String e_description, int e_id , int e_price) {
        this.e_name = e_name;
        this.e_date = e_date;
        this.e_photo = e_photo;
        this.e_location = e_location;
        this.e_type = e_type;
        this.e_description = e_description;
//        this.e_id = e_id;
        this.e_price = e_price;
    }

//    public int getE_id() {
//        return e_id;
//    }

//    public void setE_id(int e_id) {
//        this.e_id = e_id;
//    }

    public void setE_date(Date e_date) {
        this.e_date = e_date;
    }

    public void setE_photo(String e_photo) {
        this.e_photo = e_photo;
    }

    public void setE_location(String e_location) {
        this.e_location = e_location;
    }

    public void setE_type(String e_type) {
        this.e_type = e_type;
    }

    public void setE_description(String e_description) {
        this.e_description = e_description;
    }

    public String getE_name() {
        return e_name;
    }

    public void setE_name(String e_name) {
        this.e_name = e_name;
    }

    public Date getE_date() {
        return e_date;
    }

    public String getE_photo() {
        return e_photo;
    }

    public String getE_location() {
        return e_location;
    }

    public String getE_type() {
        return e_type;
    }

    public String getE_description() {
        return e_description;
    }

    public int getE_price() { return e_price; }

    public void setE_price(int e_price) { this.e_price = e_price; }
}
