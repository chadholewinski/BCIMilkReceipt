package com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb;

/**
 * Created by ChadHolewinski on 12/6/2017.
 */

public class dbPlant
{
    public String TABLE_PLANT = "MilkReceipt_Plant";
    public String PLANT_COLUMN_PKPLANTID = "pkPlantID";
    public String PLANT_COLUMN_PLANTNAME = "PlantName";
    public String PLANT_COLUMN_PLANTNUMBER = "PlantNumber";
    public String PLANT_COLUMN_BTUNUMBER = "BTUNumber";
    public String PLANT_COLUMN_ADDRESS = "Address";
    public String PLANT_COLUMN_CITYSTATEZIP = "CityStateZip";
    public String PLANT_COLUMN_LATITUDE = "Latitude";
    public String PLANT_COLUMN_LONGITUDE = "Longitude";
    public String PLANT_COLUMN_ACTIVE = "Active";
    public String PLANT_COLUMN_INSERTDATE = "InsertDate";
    public String PLANT_COLUMN_MODIFIEDDATE = "ModifiedDate";

    String pkPlantID;
    String PlantName;
    String PlantNumber;
    String BTUNumber;
    String Address;
    String CityStateZip;
    Double Latitude;
    Double Longitude;
    Integer Active;
    String InsertDate;
    String ModifiedDate;

    public dbPlant()
    {

    }

    public String getPkPlantID() {
        return pkPlantID;
    }

    public String getPlantName() {
        return PlantName;
    }

    public String getPlantNumber() {
        return PlantNumber;
    }

    public String getBTUNumber() {
        return BTUNumber;
    }

    public String getAddress() {
        return Address;
    }

    public String getCityStateZip() {
        return CityStateZip;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public Integer getActive() {
        return Active;
    }

    public String getInsertDate() {
        return InsertDate;
    }

    public String getModifiedDate() {
        return ModifiedDate;
    }

    public void setPkPlantID(String pkPlantID) {
        this.pkPlantID = pkPlantID;
    }

    public void setPlantName(String plantName) {
        PlantName = plantName;
    }

    public void setPlantNumber(String plantNumber) {
        PlantNumber = plantNumber;
    }

    public void setBTUNumber(String BTUNumber) {
        this.BTUNumber = BTUNumber;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setCityStateZip(String cityStateZip) {
        CityStateZip = cityStateZip;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public void setActive(Integer active) {
        Active = active;
    }

    public void setInsertDate(String insertDate) {
        InsertDate = insertDate;
    }

    public void setModifiedDate(String modifiedDate) {
        ModifiedDate = modifiedDate;
    }
}
