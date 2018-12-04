package com.belgioioso.bcimilkreceipt.bcimilkreceipt;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.widget.Toast;

import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbActivityHeader;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbDatabaseHandler;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbProfile;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbSettings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by ChadHolewinski on 1/2/2018.
 */

public class Utilities 
{
    private String _sWSURL = "http://10.1.2.44/MilkReceiptREST/MilkReceiptService.svc";

    //region Class Constructor Methods
    /**
     * onCreate
     *  - Constructor
     */
    protected void onCreate()
    {
        
    }
    //endregion
    
    //region Helpers
    /**
     * findSettings
     * - Gets the settings object with data from database
     * @param poContext
     * @param psUsername
     * @param ptmDevice
     * @return returns the id of the settings record
     */
    public String findSettings(Context poContext, String psUsername, String ptmDevice)
    {
        String sReturnID = "";

        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(poContext, null);

            //Get the settings object from database
            dbSettings oSettings = oDBHandler.findSettingsByName(ptmDevice);

            //Check if the settings record was found
            if (oSettings == null)
            {
                //Instantiate new settings object
                dbSettings oSettingsNew = new dbSettings();

                //Create a new settingsID GUID
                UUID gID = UUID.randomUUID();

                //Setup the new settings object data
                oSettingsNew.setPkSettingsID(gID.toString());
                oSettingsNew.setTabletName(ptmDevice);
                oSettingsNew.setMachineID(ptmDevice);
                oSettingsNew.setTrackPickupGeoLocation(0);
                oSettingsNew.setTrackRouteGeoLocation(0);
                oSettingsNew.setDebug(0);
                oSettingsNew.setDownloadNotCompletedData(0);
                oSettingsNew.setAutoDBBackup(0);
                oSettingsNew.setLastUserLoginID("");
                oSettingsNew.setLastUserLoginDate(getFormattedDate(poContext, psUsername, "1900-01-01 00:00:00.000"));
                oSettingsNew.setLastMilkReceiptID("");
                oSettingsNew.setScanLoop(1);
                oSettingsNew.setLastSettingsUpdate(getFormattedDate(poContext, psUsername, "1900-01-01 00:00:00.000"));
                oSettingsNew.setLastProfileUpdate(getFormattedDate(poContext, psUsername, "1900-01-01 00:00:00.000"));
                oSettingsNew.setUpdateAvailable(0);
                oSettingsNew.setUpdateAvailableDate(getFormattedDate(poContext, psUsername, "1900-01-01 00:00:00.000"));
                oSettingsNew.setDrugTestDevice("CharmSLRosa");
                oSettingsNew.setWebServiceURL(_sWSURL);
                oSettingsNew.setInsertDate(getFormattedDate(poContext, psUsername));
                oSettingsNew.setModifiedDate(getFormattedDate(poContext, psUsername));

                //Add the settings record to database
                oDBHandler.addSettings(oSettingsNew);

                //Set the return settingsID
                sReturnID = gID.toString();

                //Log activity
                insertActivity(poContext, "1", "Utilites", "findSettings", psUsername, "Settings not found, new settings record saved", "");
            }
            else
            {
                //Set the return settingsID
                sReturnID = oSettings.getPkSettingsID();
            }
        }
        catch(Exception ex)
        {
            //Log error message to activity
            insertActivity(poContext, "3", "Utilities", "findSettings", psUsername, ex.getMessage(), ex.getStackTrace().toString());
        }

        //Return the settingsID
        return sReturnID;
    }

    /**
     * insertActivity
     *  - inserts the activity record information into the activity header table
     * @param poContext
     * @param psActivityType
     * @param psModule
     * @param psRoutine
     * @param psUser
     * @param psMessage
     * @param psStackTrace
     */
    public void insertActivity(Context poContext, String psActivityType, String psModule, String psRoutine, String psUser, String psMessage, String psStackTrace)
    {
        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(poContext, null);

            //Instantiate new activity object
            dbActivityHeader oActivity = new dbActivityHeader();

            //Create a new settingsID GUID
            UUID gID = UUID.randomUUID();

            //Setup the new settings object data
            oActivity.setPkActivityHeaderID(gID.toString());
            oActivity.setFkActivityTypeID(psActivityType);
            oActivity.setApplication("BCIMilkReceipt");
            oActivity.setModule(psModule);
            oActivity.setRoutine(psRoutine);
            oActivity.setUsername(psUser);
            oActivity.setMessage(psMessage);
            oActivity.setStackTrace(psStackTrace);
            oActivity.setTransmitted(0);

            //Format the date for insert and modified
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");
            Date dDate = new Date();
            dDate = Calendar.getInstance().getTime();

            //Set the insert date field
            oActivity.setTransmittedDate(dfDate.parse("1/1/1900 00:00:00.000"));
            oActivity.setInsertDate(dDate);

            //Add the activity record to database
            oDBHandler.addActivity(oActivity);
        }
        catch(Exception ex)
        {
            Toast.makeText(poContext, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * checkNullString
     *  - check for a null string in passed string
     * @param psString
     * @return (String) - if null then pass back blank string else pass back current string
     */
    public String checkNullString(String psString)
    {
        if (psString == null)
        {
            return "";
        }
        else
        {
            return psString;
        }
    }

    /**
     * findUsernameByID
     *  - gets the username from the database by passed id
     * @param poContext
     * @param psProfileID
     * @return (String) - string of the username
     */
    public String findUsernameByID(Context poContext, String psProfileID)
    {
        dbProfile oProfile = new dbProfile();
        String sReturn = "";

        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(poContext, null);

            //Get the profile object from database
            oProfile = oDBHandler.findProfileByID(psProfileID);

            //Check if the profile object is populated
            if (oProfile != null)
            {
                //Set the return string to the username
                sReturn = oProfile.getUsername();
            }
        }
        catch(Exception ex)
        {
            //Log error message to activity
            insertActivity(poContext, "3", "Utilities", "findUsernameByID", "N/A", ex.getMessage(), ex.getStackTrace().toString());
        }

        //Return the username
        return sReturn;
    }

    /**
     * getWiFiIPAddress
     *  - gets the wifi ip address from the system
     * @param poContext
     * @return (String) - the wifi ip address of the system
     */
    public String getWiFiIPAddress(Context poContext, String psUsername)
    {
        String ipAddress = "0.0.0.0";

        try
        {
            WifiManager wifiMgr = (WifiManager) poContext.getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            int ip = wifiInfo.getIpAddress();

            ipAddress = Formatter.formatIpAddress(ip);
        }
        catch(Exception ex)
        {
            //Log error message to activity
            insertActivity(poContext, "3", "Utilities", "getWiFiIPAddess", psUsername, ex.getMessage(), ex.getStackTrace().toString());
        }

        return ipAddress;
    }

    /**
     * convertJSONDateToDate
     *  - converts the MS AJAX datetime format passed from JSON to standard Java date
     * @param sJSONDate
     * @return (Date) - converted MS date to Java date
     */
    public Date convertJSONDateToDate(String sJSONDate)
    {
        Date dDate = new Date();

        try
        {
            //  "/Date(1321867151710+0100)/"
            int idx1 = sJSONDate.indexOf("(");
            int idx2 = sJSONDate.indexOf(")") - 5;

            String s = sJSONDate.substring(idx1+1, idx2);

            long l = Long.valueOf(s);

            dDate = new Date(l);
        }
        catch(Exception ex)
        {
            //Log error message to activity
            //insertActivity(poContext, "3", "Utilities", "convertJSONDateToDate", psUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }

        return dDate;
    }

    /**
     * getFormattedDate
     *  - converts date to proper format
     * @param poContext
     * @param psUsername
     * @return (Date) - date in correct format
     */
    public Date getFormattedDate(Context poContext, String psUsername)
    {
        String sDate;
        Date dReturnDate = new Date();

        try
        {
            //Format the date for insert and modified
            DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date dDate = new Date();

            //Format the the correct string format
            sDate = dfDate.format(dDate);

            //Reformat to a date type
            dReturnDate = dfDate.parse(sDate);
        }
        catch(Exception ex)
        {
            //Log error message to activity
            insertActivity(poContext, "3", "Utilities", "getFormattedDate", psUsername, ex.getMessage(), ex.getStackTrace().toString());
        }

        return dReturnDate;
    }

    /**
     * getFormattedDate
     *  - converts date to proper format
     * @param poContext
     * @param psUsername
     * @param psDateString
     * @return (Date) - date in correct format
     */
    public Date getFormattedDate(Context poContext, String psUsername, String psDateString)
    {
        Date dReturnDate = new Date();
        String sDateString = "";

        try
        {
            //Format the date for insert and modified
            DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            //Check if this is a date that is not in ORACLE format
            if (psDateString.contains("/"))
            {
                //Get the year, month, day and time
                String sYear = psDateString.substring(6, 10);
                String sMonth = psDateString.substring(0, 2);
                String sDay = psDateString.substring(3,5);
                String sTime = psDateString.substring(11, psDateString.length());

                //Format to the oracle standard date format
                sDateString = sYear + "-" + sMonth + "-" + sDay + " " + sTime;
            }
            else
            {
                //Date is in an oracle format
                sDateString = psDateString;
            }

            //Reformat to a date type
            dReturnDate = dfDate.parse(sDateString);
        }
        catch(Exception ex)
        {
            //Log error message to activity
            insertActivity(poContext, "3", "Utilities", "getFormattedDate", psUsername, ex.getMessage(), ex.getStackTrace().toString());
        }

        return dReturnDate;
    }

    /**
     * converts date to proper formatted string
     * @param poContext
     * @param psUsername
     * @param pdDate
     * @return (String) - date string in correct format
     */
    public String getFormattedDateString(Context poContext, String psUsername, Date pdDate)
    {
        String sReturnDate = "";

        try
        {
            //Format the date for insert and modified
            DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            //Reformat to a date type
            sReturnDate = dfDate.format(pdDate);
        }
        catch(Exception ex)
        {
            //Log error message to activity
            insertActivity(poContext, "3", "Utilities", "getFormattedDateString", psUsername, ex.getMessage(), ex.getStackTrace().toString());
        }

        return sReturnDate;
    }

    /**
     * converts date to the proper JSON date format for get in webservice
     * @param poContext
     * @param psUsername
     * @param pdDate
     * @return (String) - date string in correct format
     */
    public String getFormattedJSONDateString(Context poContext, String psUsername, Date pdDate)
    {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String sReturnDate = "1900-01-01T000000";

        try
        {
            //Parse the date into the correct format
            sReturnDate = parser.format(pdDate);
            sReturnDate = sReturnDate.substring(0,19);
            sReturnDate = sReturnDate.replace(":","");
        }
        catch(Exception ex)
        {
            //Log error message to activity
            insertActivity(poContext, "3", "Utilities", "getFormattedJSONDateString", psUsername, ex.getMessage(), ex.getStackTrace().toString());
        }

        return sReturnDate;
    }

    /**
     * copyDBFile
     * - Copies the database file to accessible folder
     * @throws IOException
     */
    public void copyDBFile(Context poContext, String psUsername)
    {
        String sPath = "";

        try
        {
            //Get all of the external file paths
            File fSDCardPath[] = poContext.getExternalFilesDirs(null);

            //Check if there is more than 1 path found
            if (fSDCardPath.length > 1)
            {
                //More than 1 path found, use position 1, this is the SD card
                sPath = fSDCardPath[1].getPath();
            }
            else
            {
                //Only 1 path found, use position 0, this is the internal memory
                sPath = fSDCardPath[0].getPath();
            }

            //Setup the backup and current locations
            File backupDB = new File(sPath, "MilkReceipt.db"); //where you want to copy the database to
            File currentDB = poContext.getDatabasePath("MilkReceipt.db"); //databaseName=your current application database name, for example "my_data.db"

            //Check if the current DB file exists
            if (currentDB.exists())
            {
                //Check if a copy of database has already been copied to folder
                if (backupDB.exists())
                {
                    //Delete the database copy file
                    backupDB.delete();
                }

                //Setup the input and output streams
                InputStream in = new FileInputStream(currentDB);
                OutputStream out = new FileOutputStream(backupDB);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;

                //Write the file to the new location
                while ((len = in.read(buf)) > 0)
                {
                    out.write(buf, 0, len);
                }

                //Close out the streams
                in.close();
                out.close();
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            insertActivity(poContext, "3", "Utilities", "copyDBFile", psUsername, ex.getMessage(), ex.getStackTrace().toString());
        }
    }
    //endregion
}
