package com.feign.pay.sdk.common.util;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalCoordinates;

/**
 * @ClassName: GeoUtil
 * @author: Wu.bo
 * @date: 2021/1/6 13:54
 * @version: 1.0
 * @since:
 * @description: 地理位置工具
 */
public class GeoUtil {

    /**
     * 计算两个地点距离
     * @param longitudeFrom
     * @param latitudeFrom
     * @param longitudeTo
     * @param latitudeTo
     * @return
     */
    public static Double getDistance(double longitudeFrom, double latitudeFrom, double longitudeTo, double latitudeTo){
        GlobalCoordinates source = new GlobalCoordinates(latitudeFrom, longitudeFrom);
        GlobalCoordinates target = new GlobalCoordinates(latitudeTo, longitudeTo);
        return new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.Sphere, source, target).getEllipsoidalDistance();
    }

    public static Double getDistance(String longitudeFrom, String latitudeFrom, String longitudeTo, String latitudeTo){
        GlobalCoordinates source = new GlobalCoordinates(Double.parseDouble(latitudeFrom), Double.parseDouble(longitudeFrom));
        GlobalCoordinates target = new GlobalCoordinates(Double.parseDouble(latitudeTo), Double.parseDouble(longitudeTo));
        return new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.Sphere, source, target).getEllipsoidalDistance();
    }
}
