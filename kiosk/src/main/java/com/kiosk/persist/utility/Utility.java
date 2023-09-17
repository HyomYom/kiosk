package com.kiosk.persist.utility;


import org.springframework.stereotype.Component;


@Component
public class Utility {

    public double getDistance(double lat1, double lon1, double lat2, double lon2, String unit){
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));

        dist = (rad2deg(Math.acos(dist))) * 60 * 1.1515;
        if(unit.equals("kilometer")){
            dist = dist * 1.609334;
        } else if (unit.equals("meter")) {
            dist = dist * 1609.334;
        }
        return dist;
    }

    // decimal degrees to radians
    private double deg2rad(double deg){
        return(deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad){
        return (rad * 180.0 / Math.PI);
    }
}
