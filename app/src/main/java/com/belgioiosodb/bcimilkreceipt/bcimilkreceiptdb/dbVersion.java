package com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb;

import java.util.Date;

/**
 * Created by ChadHolewinski on 12/6/2017.
 */

public class dbVersion
{
    public String TABLE_VERSION = "MilkReceipt_Version";
    public String VERSION_COLUMN_FKSETTINGSID = "fkSettingsID";
    public String VERSION_COLUMN_MACHINEID = "MachineID";
    public String VERSION_COLUMN_VERSION = "Version";
    public String VERSION_COLUMN_INSERTDATE = "InsertDate";
    public String VERSION_COLUMN_MODIFIEDDATE = "ModifiedDate";

    String fkSettingsID;
    String MachineID;
    String Version;
    Date InsertDate;
    Date ModifiedDate;

    public dbVersion()
    {

    }

    public String getfkSettingsID() {
        return fkSettingsID;
    }

    public String getMachineID() {
        return MachineID;
    }

    public String getVersion() {
        return Version;
    }

    public Date getInsertDate() {
        return InsertDate;
    }

    public Date getModifiedDate() {
        return ModifiedDate;
    }

    public void setfkSettingsID(String fkSettingsID) {
        this.fkSettingsID = fkSettingsID;
    }

    public void setMachineID(String machineID) {
        MachineID = machineID;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public void setInsertDate(Date insertDate) {
        InsertDate = insertDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        ModifiedDate = modifiedDate;
    }
}
