package com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb;

import java.util.Date;

/**
 * Created by ChadHolewinski on 12/6/2017.
 */

public class dbGeoLocation
{
    public String TABLE_GEOLOCATION = "MilkReceipt_GeoLocation";
    public String GEOLOCATION_COLUMN_PKGEOLOCATIONID = "pkGeoLocationID";
    public String GEOLOCATION_COLUMN_LATITUDE = "Latitude";
    public String GEOLOCATION_COLUMN_LONGITUDE = "Longitude";
    public String GEOLOCATION_COLUMN_ACCURRACY = "Accurracy";
    public String GEOLOCATION_COLUMN_HEADINGDIRECTION = "HeadingDirection";
    public String GEOLOCATION_COLUMN_SPEED = "Speed";
    public String GEOLOCATION_COLUMN_ALTITUDE = "Altitude";
    public String GEOLOCATION_COLUMN_TRANSMITTED = "Transmitted";
    public String GEOLOCATION_COLUMN_TRANSMITTEDDATE = "TransmittedDate";
    public String GEOLOCATION_COLUMN_INSERTDATE = "InsertedDate";

    String pkGeoLocationID;
    Double Latitude;
    Double Longitude;
    Double Accurracy;
    String HeadingDirection;
    Double Speed;
    Double Altitude;
    Integer Transmitted;
    Date TransmittedDate;
    Date InsertDate;

    public dbGeoLocation()
    {

    }

    public String getPkGeoLocationID() {
        return pkGeoLocationID;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public Double getAccurracy() {
        return Accurracy;
    }

    public String getHeadingDirection() {
        return HeadingDirection;
    }

    public Double getSpeed() {
        return Speed;
    }

    public Double getAltitude() {
        return Altitude;
    }

    public Integer getTransmitted() {
        return Transmitted;
    }

    public Date getTransmittedDate() {
        return TransmittedDate;
    }

    public Date getInsertDate() {
        return InsertDate;
    }

    public void setPkGeoLocationID(String pkGeoLocationID) { this.pkGeoLocationID = pkGeoLocationID; }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public void setAccurracy(Double accurracy) {
        Accurracy = accurracy;
    }

    public void setHeadingDirection(String headingDirection) { HeadingDirection = headingDirection; }

    public void setSpeed(Double speed) {
        Speed = speed;
    }

    public void setAltitude(Double altitude) {
        Altitude = altitude;
    }

    public void setTransmitted(Integer transmitted) {
        Transmitted = transmitted;
    }

    public void setTransmittedDate(Date transmittedDate) {
        TransmittedDate = transmittedDate;
    }

    public void setInsertDate(Date insertDate) {
        InsertDate = insertDate;
    }
}
