package com.primesys.VehicalTracking.Dto;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by pt002 on 17/2/17.
 */

public class DirToatalInfo {
    
            
    
     String   distance;
        String  duration;
    String   duration_in_traffic;
    String  end_address;
    LatLng end_location;
    String   start_address;

    public LatLng getEnd_location() {
        return end_location;
    }

    public void setEnd_location(LatLng end_location) {
        this.end_location = end_location;
    }

    public LatLng getStart_location() {
        return start_location;
    }

    public void setStart_location(LatLng start_location) {
        this.start_location = start_location;
    }

    LatLng       start_location;
    ArrayList<Geo_DirDTO>    steps;
    String   via_waypoint;
    String       overview_polyline;
    String      summary;
    String       warnings;
    String        waypoint_order;


    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration_in_traffic() {
        return duration_in_traffic;
    }

    public void setDuration_in_traffic(String duration_in_traffic) {
        this.duration_in_traffic = duration_in_traffic;
    }

    public String getEnd_address() {
        return end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }



    public String getStart_address() {
        return start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }


    public ArrayList<Geo_DirDTO> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Geo_DirDTO> steps) {
        this.steps = steps;
    }

    public String getVia_waypoint() {
        return via_waypoint;
    }

    public void setVia_waypoint(String via_waypoint) {
        this.via_waypoint = via_waypoint;
    }

    public String getOverview_polyline() {
        return overview_polyline;
    }

    public void setOverview_polyline(String overview_polyline) {
        this.overview_polyline = overview_polyline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getWarnings() {
        return warnings;
    }

    public void setWarnings(String warnings) {
        this.warnings = warnings;
    }

    public String getWaypoint_order() {
        return waypoint_order;
    }

    public void setWaypoint_order(String waypoint_order) {
        this.waypoint_order = waypoint_order;
    }



}
