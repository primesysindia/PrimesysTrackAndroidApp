package com.primesys.VehicalTracking.Dto;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pt002 on 15/2/17.
 */

public class Geo_DirDTO {
    String html_instructions ;
    String travel_mode ;
    String maneuver;

    String distance_text ;
    String distance_value ;

    String duration_text ;
    String duration_value ;

    String start_lat ;
    String start_lon ;

    String end_lat ;

    public String getHtml_instructions() {
        return html_instructions;
    }

    public void setHtml_instructions(String html_instructions) {
        this.html_instructions = html_instructions;
    }

    public String getTravel_mode() {
        return travel_mode;
    }

    public void setTravel_mode(String travel_mode) {
        this.travel_mode = travel_mode;
    }

    public String getManeuver() {
        return maneuver;
    }

    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }

    public String getDistance_text() {
        return distance_text;
    }

    public void setDistance_text(String distance_text) {
        this.distance_text = distance_text;
    }

    public String getDistance_value() {
        return distance_value;
    }

    public void setDistance_value(String distance_value) {
        this.distance_value = distance_value;
    }

    public String getDuration_text() {
        return duration_text;
    }

    public void setDuration_text(String duration_text) {
        this.duration_text = duration_text;
    }

    public String getDuration_value() {
        return duration_value;
    }

    public void setDuration_value(String duration_value) {
        this.duration_value = duration_value;
    }

    public String getStart_lat() {
        return start_lat;
    }

    public void setStart_lat(String start_lat) {
        this.start_lat = start_lat;
    }

    public String getStart_lon() {
        return start_lon;
    }

    public void setStart_lon(String start_lon) {
        this.start_lon = start_lon;
    }

    public String getEnd_lat() {
        return end_lat;
    }

    public void setEnd_lat(String end_lat) {
        this.end_lat = end_lat;
    }

    public String getEnd_lon() {
        return end_lon;
    }

    public void setEnd_lon(String end_lon) {
        this.end_lon = end_lon;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public List<LatLng> getMylist() {
        return mylist;
    }

    public void setMylist(List<LatLng> mylist) {
        this.mylist = mylist;
    }

    public List getPath() {
        return path;
    }

    public void setPath(List path) {
        this.path = path;
    }

    String end_lon ;

    String polyline ;
    List<LatLng> mylist ;
    List path ;


}
