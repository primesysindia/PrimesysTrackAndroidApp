package com.primesys.VehicalTracking.Dto;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by pt002 on 17/2/17.
 */

public class snapdto {
    LatLng Location;
    String  originalIndex,placeId;

    public LatLng getLocation() {
        return Location;
    }

    public void setLocation(LatLng location) {
        Location = location;
    }

    public String getOriginalIndex() {
        return originalIndex;
    }

    public void setOriginalIndex(String originalIndex) {
        this.originalIndex = originalIndex;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
