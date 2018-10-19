package com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb;

import java.util.Date;

/**
 * Created by ChadHolewinski on 12/6/2017.
 */

public class dbReceive
{
    public String TABLE_RECEIVE = "MilKReceipt_Receive";
    public String RECEIVE_COLUMN_PKRECEIVEID = "pkReceiveID";
    public String RECEIVE_COLUMN_FKHEADERID = "fkHeaderID";
    public String RECEIVE_COLUMN_FKPLANTID = "fkPlantID";
    public String RECEIVE_COLUMN_FKPLANTORIGINALID = "fkPlantOriginalID";
    public String RECEIVE_COLUMN_DRUGTESTDEVICE = "DrugTestDevice";
    public String RECEIVE_COLUMN_DRUGTESTRESULT = "DrugTestResult";
    public String RECEIVE_COLUMN_RECEIVEDATETIME = "ReceiveDateTime";
    public String RECEIVE_COLUMN_TANK = "Tank";
    public String RECEIVE_COLUMN_SCALEMETER = "ScaleMeter";
    public String RECEIVE_COLUMN_TOPSEAL = "TopSeal";
    public String RECEIVE_COLUMN_BOTTOMSEAL = "BottomSeal";
    public String RECEIVE_COLUMN_RECEIVEDLBS = "ReceivedLBS";
    public String RECEIVE_COLUMN_LOADTEMP = "LoadTemp";
    public String RECEIVE_COLUMN_INTAKENUMBER = "IntakeNumber";
    public String RECEIVE_COLUMN_FINISHED = "Finished";
    public String RECEIVE_COLUMN_WAITINGFORSCALEDATA = "WaitingForScaleData";
    public String RECEIVE_COLUMN_TRANSMITTED = "Transmitted";
    public String RECEIVE_COLUMN_TRANSMITTEDDATE = "TransmittedDate";
    public String RECEIVE_COLUMN_INSERTDATE = "InsertDate";
    public String RECEIVE_COLUMN_MODIFIEDDATE = "ModifiedDate";

    String pkReceiveID;
    String fkHeaderID;
    String fkPlantID;
    String fkPlantOriginalID;
    String DrugTestDevice;
    String DrugTestResult;
    Date ReceiveDateTime;
    String Tank;
    Integer ScaleMeter;
    String TopSeal;
    String BottomSeal;
    Integer ReceivedLBS;
    Integer LoadTemp;
    Integer IntakeNumber;
    Integer Finished;
    Integer WaitingForScaleData;
    Integer Transmitted;
    Date TransmittedDate;
    Date InsertDate;
    Date ModifiedDate;

    public dbReceive()
    {

    }

    public String getPkReceiveID() {
        return pkReceiveID;
    }

    public String getFkHeaderID() {
        return fkHeaderID;
    }

    public String getFkPlantID() {
        return fkPlantID;
    }

    public String getFkPlantOriginalID() {
        return fkPlantOriginalID;
    }

    public String getDrugTestDevice() {
        return DrugTestDevice;
    }

    public String getDrugTestResult() {
        return DrugTestResult;
    }

    public Date getReceiveDateTime() {
        return ReceiveDateTime;
    }

    public String getTank() {
        return Tank;
    }

    public Integer getScaleMeter() {
        return ScaleMeter;
    }

    public String getTopSeal() {
        return TopSeal;
    }

    public String getBottomSeal() {
        return BottomSeal;
    }

    public Integer getReceivedLBS() {
        return ReceivedLBS;
    }

    public Integer getLoadTemp() {
        return LoadTemp;
    }

    public Integer getIntakeNumber() {
        return IntakeNumber;
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

    public Date getTransmittedDate() {
        return TransmittedDate;
    }

    public Date getInsertDate() {
        return InsertDate;
    }

    public Date getModifiedDate() {
        return ModifiedDate;
    }

    public void setPkReceiveID(String pkReceiveID) {
        this.pkReceiveID = pkReceiveID;
    }

    public void setFkHeaderID(String fkHeaderID) {
        this.fkHeaderID = fkHeaderID;
    }

    public void setFkPlantID(String fkPlantID) {
        this.fkPlantID = fkPlantID;
    }

    public void setFkPlantOriginalID(String fkPlantOriginalID) { this.fkPlantOriginalID = fkPlantOriginalID; }

    public void setDrugTestDevice(String drugTestDevice) {
        DrugTestDevice = drugTestDevice;
    }

    public void setDrugTestResult(String drugTestResult) {
        DrugTestResult = drugTestResult;
    }

    public void setReceiveDateTime(Date receiveDateTime) {
        ReceiveDateTime = receiveDateTime;
    }

    public void setTank(String tank) {
        Tank = tank;
    }

    public void setScaleMeter(Integer scaleMeter) {
        ScaleMeter = scaleMeter;
    }

    public void setTopSeal(String topSeal) {
        TopSeal = topSeal;
    }

    public void setBottomSeal(String bottomSeal) {
        BottomSeal = bottomSeal;
    }

    public void setReceivedLBS(Integer receivedLBS) {
        ReceivedLBS = receivedLBS;
    }

    public void setLoadTemp(Integer loadTemp) {
        LoadTemp = loadTemp;
    }

    public void setIntakeNumber(Integer intakeNumber) {
        IntakeNumber = intakeNumber;
    }

    public void setFinished(Integer finished) {
        Finished = finished;
    }

    public void setWaitingForScaleData(Integer waitingForScaleData) { WaitingForScaleData = waitingForScaleData; }

    public void setTransmitted(Integer transmitted) {
        Transmitted = transmitted;
    }

    public void setTransmittedDate(Date transmittedDate) {
        TransmittedDate = transmittedDate;
    }

    public void setInsertDate(Date insertDate) {
        InsertDate = insertDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        ModifiedDate = modifiedDate;
    }
}
