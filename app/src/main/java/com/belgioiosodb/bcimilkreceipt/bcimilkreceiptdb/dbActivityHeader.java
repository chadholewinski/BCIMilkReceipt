package com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb;

import java.util.Date;

/**
 * Created by ChadHolewinski on 12/6/2017.
 */

public class dbActivityHeader
{
    public String TABLE_ACTIVITYHEADER = "MilkReceipt_ActivityHeader";
    public String ACTIVITYHEADER_COLUMN_PKACTIVITYHEADERID = "pkActivityHeaderID";
    public String ACTIVITYHEADER_COLUMN_FKACTIVITYTYPEID = "fkActivityTypeID";
    public String ACTIVITYHEADER_COLUMN_APPLICATION = "Application";
    public String ACTIVITYHEADER_COLUMN_MODULE = "Module";
    public String ACTIVITYHEADER_COLUMN_ROUTINE = "Routine";
    public String ACTIVITYHEADER_COLUMN_USERNAME = "Username";
    public String ACTIVITYHEADER_COLUMN_MESSAGE = "Message";
    public String ACTIVITYHEADER_COLUMN_STACKTRACE = "StackTrace";
    public String ACTIVITYHEADER_COLUMN_TRANSMITTED = "Transmitted";
    public String ACTIVITYHEADER_COLUMN_TRANSMITTEDDATE = "TransmittedDate";
    public String ACTIVITYHEADER_COLUMN_INSERTDATE = "InsertDate";

    String pkActivityHeaderID;
    String fkActivityTypeID;
    String Application;
    String Module;
    String Routine;
    String Username;
    String Message;
    String StackTrace;
    Boolean Transmitted;
    Date TransmittedDate;
    Date InsertDate;

    public dbActivityHeader()
    {

    }

    public String getPkActivityHeaderID() {
        return pkActivityHeaderID;
    }

    public String getFkActivityTypeID() {
        return fkActivityTypeID;
    }

    public String getApplication() {
        return Application;
    }

    public String getModule() {
        return Module;
    }

    public String getRoutine() {
        return Routine;
    }

    public String getUsername() {
        return Username;
    }

    public String getMessage() {
        return Message;
    }

    public String getStackTrace() {
        return StackTrace;
    }

    public Boolean getTransmitted() {
        return Transmitted;
    }

    public Date getTransmittedDate() {
        return TransmittedDate;
    }

    public Date getInsertDate() {
        return InsertDate;
    }

    public void setPkActivityHeaderID(String pkActivityHeaderID) { this.pkActivityHeaderID = pkActivityHeaderID; }

    public void setFkActivityTypeID(String fkActivityTypeID) { this.fkActivityTypeID = fkActivityTypeID; }

    public void setApplication(String application) {
        Application = application;
    }

    public void setModule(String module) {
        Module = module;
    }

    public void setRoutine(String routine) {
        Routine = routine;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setStackTrace(String stackTrace) {
        StackTrace = stackTrace;
    }

    public void setTransmitted(Boolean transmitted) {
        Transmitted = transmitted;
    }

    public void setTransmittedDate(Date transmittedDate) {
        TransmittedDate = transmittedDate;
    }

    public void setInsertDate(Date insertDate) {
        InsertDate = insertDate;
    }
}
