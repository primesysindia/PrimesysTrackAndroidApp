package com.primesys.VehicalTracking.Dto;

public class SmallestDistanceDto {

    String distance,poleName;

    public int getPolePosition() {
        return polePosition;
    }

    public void setPolePosition(int polePosition) {
        this.polePosition = polePosition;
    }

    int polePosition;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPoleName() {
        return poleName;
    }

    public void setPoleName(String poleName) {
        this.poleName = poleName;
    }
}
