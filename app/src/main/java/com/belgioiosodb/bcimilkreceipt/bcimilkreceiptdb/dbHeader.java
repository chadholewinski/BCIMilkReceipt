package com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb;

/**
 * Created by ChadHolewinski on 12/6/2017.
 */

public class dbHeader
{
    public String TABLE_HEADER = "MilkReceipt_Header";
    public String HEADER_COLUMN_PKHEADERID = "pkHeaderID";
    public String HEADER_COLUMN_FKPROFILEID = "fkProfileID";
    public String HEADER_COLUMN_TICKETNUMBER = "TicketNumber";
    public String HEADER_COLUMN_ROUTEIDENTIFIER = "RouteIdentifier";
    public String HEADER_COLUMN_TRUCKLICENSENUMBER = "TruckLicenseNumber";
    public String HEADER_COLUMN_STARTMILEAGE = "StartMileage";
    public String HEADER_COLUMN_ENDMILEAGE = "EndMileage";
    public String HEADER_COLUMN_TOTALMILEAGE = "TotalMileage";
    public String HEADER_COLUMN_FINISHED = "Finished";
    public String HEADER_COLUMN_WAITINGFORSCALEDATA = "WaitingForScaleData";
    public String HEADER_COLUMN_TRANSMITTED = "Transmitted";
    public String HEADER_COLUMN_TRANSMITTEDDATE = "TransmittedDate";
    public String HEADER_COLUMN_INSERTDATE = "InsertDate";
    public String HEADER_COLUMN_MODIFIEDDATE = "ModifiedDate";

    String pkHeaderID;
    String fkProfileID;
    String TicketNumber;
    String RouteIdentifier;
    String TruckLicenseNumber;
    Integer StartMileage;
    Integer EndMileage;
    Integer TotalMileage;
    Integer Finished;
    Integer WaitingForScaleData;
    Integer Transmitted;
    String TransmittedDate;
    String InsertDate;
    String ModifiedDate;

    public dbHeader()
    {
    }

    public String getPkHeaderID() {
        return pkHeaderID;
    }

    public String getFkProfileID() {
        return fkProfileID;
    }

    public String getTicketNumber() {
        return TicketNumber;
    }

    public String getRouteIdentifier() {
        return RouteIdentifier;
    }

    public String getTruckLicenseNumber() {
        return TruckLicenseNumber;
    }

    public Integer getStartMileage() {
        return StartMileage;
    }

    public Integer getEndMileage() {
        return EndMileage;
    }

    public Integer getTotalMileage() {
        return TotalMileage;
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

    public void setPkHeaderID(String pkHeaderID) {
        this.pkHeaderID = pkHeaderID;
    }

    public void setFkProfileID(String fkProfileID) {
        this.fkProfileID = fkProfileID;
    }

    public void setTicketNumber(String ticketNumber) {
        TicketNumber = ticketNumber;
    }

    public void setRouteIdentifier(String routeIdentifier) {
        RouteIdentifier = routeIdentifier;
    }

    public void setTruckLicenseNumber(String truckLicenseNumber) { TruckLicenseNumber = truckLicenseNumber; }

    public void setStartMileage(Integer startMileage) {
        StartMileage = startMileage;
    }

    public void setEndMileage(Integer endMileage) {
        EndMileage = endMileage;
    }

    public void setTotalMileage(Integer totalMileage) {
        TotalMileage = totalMileage;
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
