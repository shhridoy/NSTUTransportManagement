package com.shhridoy.transportmanagementnstu.myObjects;

public class BusSchedule {

    private String key = "";
    private String bus_title = "";
    private String bus_type = "";
    private String start_point = "";
    private String end_point = "";
    private String start_time = "";
    private String vote = "0";

    public BusSchedule(String key, String bus_title, String bus_type, String start_point, String end_point, String start_time, String vote) {
        this.key = key;
        this.bus_title = bus_title;
        this.bus_type = bus_type;
        this.start_point = start_point;
        this.end_point = end_point;
        this.start_time = start_time;
        this.vote = vote;
    }


    public String getKey() {
        return key;
    }

    public String getBus_title() {
        return bus_title;
    }

    public String getBus_type() {
        return bus_type;
    }

    public String getStart_point() {
        return start_point;
    }

    public String getEnd_point() {
        return end_point;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getVote() {
        return vote;
    }
}