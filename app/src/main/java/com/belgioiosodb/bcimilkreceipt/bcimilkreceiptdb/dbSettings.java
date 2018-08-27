package com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb;

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
    public String SETTINGS_COLUMN_AUTODBBACKUP = "AutoDBBackup";
    public String SETTINGS_COLUMN_LASTUSERLOGINID = "LastUserLoginID";
    public String SETTINGS_COLUMN_LASTUSERLOGINDATE = "LastUserLoginDate";
    public String SETTINGS_COLUMN_LASTMILKRECEIPTID = "LastMilkReceiptID";
    public String SETTINGS_COLUMN_SCANLOOP = "ScanLoop";
    public String SETTINGS_COLUMN_LASTSETTINGSUPDATE = "LastSettingsUpdate";
    public String SETTINGS_COLUMN_LASTPROFILEUPDATE = "LastProfileUpdate";
    public String SETTINGS_COLUMN_UPDATEAVAILABLE = "UpdateAvailable";
    public String SETTINGS_COLUMN_UPDATEAVAILABLEDATE = "UpdateAvailableDate";
    public String SETTINGS_COLUMN_DRUGTESTDEVICE = "DrugTestResult";
    public String SETTINGS_COLUMN_WEBSERVICEURL = "WebServiceURL";
    public String SETTINGS_COLUMN_INSERTDATE = "InsertDate";
    public String SETTINGS_COLUMN_MODIFIEDDATE = "ModifiedDate";

    String pkSettingsID;
    String TabletName;
    String MachineID;
    Integer TrackPickupGeoLocation;
    Integer TrackRouteGeoLocation;
    Integer Debug;
    Integer AutoDBBackup;
    String LastUserLoginID;
    String LastUserLoginDate;
    String LastMilkReceiptID;
    Integer ScanLoop;
    String LastSettingsUpdate;
    String LastProfileUpdate;
    Integer UpdateAvailable;
    String UpdateAvailableDate;
    String DrugTestDevice;
    String WebServiceURL;
    String InsertDate;
    String ModifiedDate;

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

    public Integer getAutoDBBackup() {
        return AutoDBBackup;
    }

    public String getLastUserLoginID() {
        return LastUserLoginID;
    }

    public String getLastUserLoginDate() { return LastUserLoginDate; }

    public String getLastMilkReceiptID() {
        return LastMilkReceiptID;
    }

    public Integer getScanLoop() {
        return ScanLoop;
    }

    public String getLastSettingsUpdate() {
        return LastSettingsUpdate;
    }

    public String getLastProfileUpdate() {
        return LastProfileUpdate;
    }

    public Integer getUpdateAvailable() {
        return UpdateAvailable;
    }

    public String getUpdateAvailableDate() { return UpdateAvailableDate; }

    public String getDrugTestDevice() {
        return DrugTestDevice;
    }

    public String getWebServiceURL() {
        return WebServiceURL;
    }

    public String getInsertDate() {
        return InsertDate;
    }

    public String getModifiedDate() {
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

    public void setAutoDBBackup(Integer autoDBBackup) {
        AutoDBBackup = autoDBBackup;
    }

    public void setLastUserLoginID(String lastUserLoginID) {
        LastUserLoginID = lastUserLoginID;
    }

    public void setLastUserLoginDate(String lastUserLoginDate) { LastUserLoginDate = lastUserLoginDate; }

    public void setLastMilkReceiptID(String lastMilkReceiptID) { LastMilkReceiptID = lastMilkReceiptID; }

    public void setScanLoop(Integer scanLoop) {
        ScanLoop = scanLoop;
    }

    public void setLastSettingsUpdate(String lastSettingsUpdate) { LastSettingsUpdate = lastSettingsUpdate; }

    public void setLastProfileUpdate(String lastProfileUpdate) { LastProfileUpdate = lastProfileUpdate; }

    public void setUpdateAvailable(Integer updateAvailable) {
        UpdateAvailable = updateAvailable;
    }

    public void setUpdateAvailableDate(String updateAvailableDate) { UpdateAvailableDate = updateAvailableDate; }

    public void setDrugTestDevice(String drugTestDevice) {
        DrugTestDevice = drugTestDevice;
    }

    public void setWebServiceURL(String webServiceURL) {
        WebServiceURL = webServiceURL;
    }

    public void setInsertDate(String insertDate) {
        InsertDate = insertDate;
    }

    public void setModifiedDate(String modifiedDate) {
        ModifiedDate = modifiedDate;
    }
}
