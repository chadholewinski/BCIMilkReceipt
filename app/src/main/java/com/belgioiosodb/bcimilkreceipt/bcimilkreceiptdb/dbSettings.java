package com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb;

import java.util.Date;

/**
 * Created by ChadHolewinski on 12/6/2017.
 */

public class dbSettings
{
    public String TABLE_SETTINGS = "MilkReceipt_Settings";
    public String SETTINGS_COLUMN_PKSETTINGSID = "pkSettingsID";
    public String SETTINGS_COLUMN_TABLETNAME = "TabletName";
    public String SETTINGS_COLUMN_MACHINEID = "MachineID";
    public String SETTINGS_COLUMN_TRACKPICKUPGEOLOCATION = "TrackPickupGeoLocation";
    public String SETTINGS_COLUMN_TRACKROUTEGEOLOCATION = "TrackRouteGeoLocation";
    public String SETTINGS_COLUMN_DEBUG = "Debug";
    public String SETTINGS_COLUMN_DOWNLOADNOTCOMPLETEDDATA = "DownloadNotCompletedData";
    public String SETTINGS_COLUMN_AUTODBBACKUP = "AutoDBBackup";
    public String SETTINGS_COLUMN_LASTUSERLOGINID = "LastUserLoginID";
    public String SETTINGS_COLUMN_LASTUSERLOGINDATE = "LastUserLoginDate";
    public String SETTINGS_COLUMN_LASTMILKRECEIPTID = "LastMilkReceiptID";
    public String SETTINGS_COLUMN_SCANLOOP = "ScanLoop";
    public String SETTINGS_COLUMN_LASTSETTINGSUPDATE = "LastSettingsUpdate";
    public String SETTINGS_COLUMN_LASTPROFILEUPDATE = "LastProfileUpdate";
    public String SETTINGS_COLUMN_UPDATEAVAILABLE = "UpdateAvailable";
    public String SETTINGS_COLUMN_UPDATEAVAILABLEDATE = "UpdateAvailableDate";
    public String SETTINGS_COLUMN_DRUGTESTDEVICE = "DrugTestDevice";
    public String SETTINGS_COLUMN_WEBSERVICEURL = "WebServiceURL";
    public String SETTINGS_COLUMN_INSERTDATE = "InsertDate";
    public String SETTINGS_COLUMN_MODIFIEDDATE = "ModifiedDate";

    String pkSettingsID;
    String TabletName;
    String MachineID;
    Integer TrackPickupGeoLocation;
    Integer TrackRouteGeoLocation;
    Integer Debug;
    Integer DownloadNotCompletedData;
    Integer AutoDBBackup;
    String LastUserLoginID;
    Date LastUserLoginDate;
    String LastMilkReceiptID;
    Integer ScanLoop;
    Date LastSettingsUpdate;
    Date LastProfileUpdate;
    Integer UpdateAvailable;
    Date UpdateAvailableDate;
    String DrugTestDevice;
    String WebServiceURL;
    Date InsertDate;
    Date ModifiedDate;

    public dbSettings()
    {

    }

    public String getPkSettingsID() {
        return pkSettingsID;
    }

    public String getTabletName() {
        return TabletName;
    }

    public String getMachineID() {
        return MachineID;
    }

    public Integer getTrackPickupGeoLocation() {
        return TrackPickupGeoLocation;
    }

    public Integer getTrackRouteGeoLocation() {
        return TrackRouteGeoLocation;
    }

    public Integer getDebug() {
        return Debug;
    }

    public Integer getDownloadNotCompletedData() {
        return DownloadNotCompletedData;
    }

    public Integer getAutoDBBackup() {
        return AutoDBBackup;
    }

    public String getLastUserLoginID() {
        return LastUserLoginID;
    }

    public Date getLastUserLoginDate() { return LastUserLoginDate; }

    public String getLastMilkReceiptID() {
        return LastMilkReceiptID;
    }

    public Integer getScanLoop() {
        return ScanLoop;
    }

    public Date getLastSettingsUpdate() {
        return LastSettingsUpdate;
    }

    public Date getLastProfileUpdate() {
        return LastProfileUpdate;
    }

    public Integer getUpdateAvailable() {
        return UpdateAvailable;
    }

    public Date getUpdateAvailableDate() { return UpdateAvailableDate; }

    public String getDrugTestDevice() {
        return DrugTestDevice;
    }

    public String getWebServiceURL() {
        return WebServiceURL;
    }

    public Date getInsertDate() {
        return InsertDate;
    }

    public Date getModifiedDate() {
        return ModifiedDate;
    }

    public void setPkSettingsID(String pkSettingsID) {
        this.pkSettingsID = pkSettingsID;
    }

    public void setTabletName(String tabletName) {
        TabletName = tabletName;
    }

    public void setMachineID(String machineID) {
        MachineID = machineID;
    }

    public void setTrackPickupGeoLocation(Integer trackPickupGeoLocation) { TrackPickupGeoLocation = trackPickupGeoLocation; }

    public void setTrackRouteGeoLocation(Integer trackRouteGeoLocation) { TrackRouteGeoLocation = trackRouteGeoLocation; }

    public void setDebug(Integer debug) {
        Debug = debug;
    }

    public void setDownloadNotCompletedData(Integer downloadNotCompletedData) { DownloadNotCompletedData = downloadNotCompletedData; }

    public void setAutoDBBackup(Integer autoDBBackup) {
        AutoDBBackup = autoDBBackup;
    }

    public void setLastUserLoginID(String lastUserLoginID) {
        LastUserLoginID = lastUserLoginID;
    }

    public void setLastUserLoginDate(Date lastUserLoginDate) { LastUserLoginDate = lastUserLoginDate; }

    public void setLastMilkReceiptID(String lastMilkReceiptID) { LastMilkReceiptID = lastMilkReceiptID; }

    public void setScanLoop(Integer scanLoop) {
        ScanLoop = scanLoop;
    }

    public void setLastSettingsUpdate(Date lastSettingsUpdate) { LastSettingsUpdate = lastSettingsUpdate; }

    public void setLastProfileUpdate(Date lastProfileUpdate) { LastProfileUpdate = lastProfileUpdate; }

    public void setUpdateAvailable(Integer updateAvailable) {
        UpdateAvailable = updateAvailable;
    }

    public void setUpdateAvailableDate(Date updateAvailableDate) { UpdateAvailableDate = updateAvailableDate; }

    public void setDrugTestDevice(String drugTestDevice) {
        DrugTestDevice = drugTestDevice;
    }

    public void setWebServiceURL(String webServiceURL) {
        WebServiceURL = webServiceURL;
    }

    public void setInsertDate(Date insertDate) {
        InsertDate = insertDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        ModifiedDate = modifiedDate;
    }
}
