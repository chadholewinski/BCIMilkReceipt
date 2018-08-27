package com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb;

/**
 * Created by ChadHolewinski on 12/6/2017.
 */

public class dbLine
{
    public String TABLE_LINE = "MilkReceipt_Line";
    public String LINE_COLUMN_PKLINEID = "pkLineID";
    public String LINE_COLUMN_FKHEADERID = "fkHeaderID";
    public String LINE_COLUMN_TANK = "Tank";
    public String LINE_COLUMN_PRODUCER = "Producer";
    public String LINE_COLUMN_COMPANY = "Company";
    public String LINE_COLUMN_DIVISION = "Division";
    public String LINE_COLUMN_TYPE = "Type";
    public String LINE_COLUMN_GAUGERODMAJOR = "GaugeRodMajor";
    public String LINE_COLUMN_GAUGERODMINOR = "GaugeRodMinor";
    public String LINE_COLUMN_CONVERTEDLBS = "ConvertedLBS";
    public String LINE_COLUMN_TEMPERATURE = "Temperature";
    public String LINE_COLUMN_PICKUPDATE = "PickupDate";
    public String LINE_COLUMN_DFATICKET = "DFATicket";
    public String LINE_COLUMN_LABCODE = "LabCode";
    public String LINE_COLUMN_LATITUDE = "Latitude";
    public String LINE_COLUMN_LONGITUDE = "Longitude";
    public String LINE_COLUMN_ACCURRACY = "Accurracy";
    public String LINE_COLUMN_FINISHED = "Finished";
    public String LINE_COLUMN_WAITINGFORSCALEDATA = "WaitingForScaleData";
    public String LINE_COLUMN_TRANSMITTED = "Transmitted";
    public String LINE_COLUMN_TRANSMITTEDDATE = "TransmittedDate";
    public String LINE_COLUMN_INSERTDATE = "InsertDate";
    public String LINE_COLUMN_MODIFIEDDATE = "ModifiedDate";

    String pkLineID;
    String fkHeaderID;
    String Tank;
    String Producer;
    String Company;
    String Division;
    String Type;
    Integer GaugeRodMajor;
    Integer GaugeRodMinor;
    Integer ConvertedLBS;
    Double Temperature;
    String PickupDate;
    String DFATicket;
    String LabCode;
    Double Latitude;
    Double Longitude;
    Double Accurracy;
    Integer Finished;
    Integer WaitingForScaleData;
    Integer Transmitted;
    String TransmittedDate;
    String InsertDate;
    String ModifiedDate;

    public dbLine()
    {
    }

    public String getPkLineID() {
        return pkLineID;
    }

    public String getFkHeaderID() {
        return fkHeaderID;
    }

    public String getTank() {
        return Tank;
    }

    public String getProducer() {
        return Producer;
    }

    public String getCompany() {
        return Company;
    }

    public String getDivision() {
        return Division;
    }

    public String getType() {
        return Type;
    }

    public Integer getGaugeRodMajor() {
        return GaugeRodMajor;
    }

    public Integer getGaugeRodMinor() {
        return GaugeRodMinor;
    }

    public Integer getConvertedLBS() {
        return ConvertedLBS;
    }

    public Double getTemperature() {
        return Temperature;
    }

    public String getPickupDate() {
        return PickupDate;
    }

    public String getDFATicket() {
        return DFATicket;
    }

    public String getLabCode() {
        return LabCode;
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

    public Integer getFinished() {
        return Finished;
    }

    public Integer getWaitingForScaleData() {
        return WaitingForScaleData;
    }

    public Integer getTransmitted() {
        return Transmitted;
    }

    public String getTransmittedDate() {
        return TransmittedDate;
    }

    public String getInsertDate() {
        return InsertDate;
    }

    public String getModifiedDate() {
        return ModifiedDate;
    }

    public void setPkLineID(String pkLineID) {
        this.pkLineID = pkLineID;
    }

    public void setFkHeaderID(String fkHeaderID) {
        this.fkHeaderID = fkHeaderID;
    }

    public void setTank(String tank) {
        Tank = tank;
    }

    public void setProducer(String producer) {
        Producer = producer;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public void setDivision(String division) {
        Division = division;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setGaugeRodMajor(Integer gaugeRodMajor) {
        GaugeRodMajor = gaugeRodMajor;
    }

    public void setGaugeRodMinor(Integer gaugeRodMinor) {
        GaugeRodMinor = gaugeRodMinor;
    }

    public void setConvertedLBS(Integer convertedLBS) {
        ConvertedLBS = convertedLBS;
    }

    public void setTemperature(Double temperature) {
        Temperature = temperature;
    }

    public void setPickupDate(String pickupDate) {
        PickupDate = pickupDate;
    }

    public void setDFATicket(String DFATicket) {
        this.DFATicket = DFATicket;
    }

    public void setLabCode(String labCode) {
        LabCode = labCode;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public void setAccurracy(Double accurracy) {
        Accurracy = accurracy;
    }

    public void setFinished(Integer finished) {
        Finished = finished;
    }

    public void setWaitingForScaleData(Integer waitingForScaleData) { WaitingForScaleData = waitingForScaleData; }

    public void setTransmitted(Integer transmitted) {
        Transmitted = transmitted;
    }

    public void setTransmittedDate(String transmittedDate) {
        TransmittedDate = transmittedDate;
    }

    public void setInsertDate(String insertDate) {
        InsertDate = insertDate;
    }

    public void setModifiedDate(String modifiedDate) {
        ModifiedDate = modifiedDate;
    }
}
