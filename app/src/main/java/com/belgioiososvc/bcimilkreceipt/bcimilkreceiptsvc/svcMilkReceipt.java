package com.belgioiososvc.bcimilkreceipt.bcimilkreceiptsvc;

import android.os.Bundle;
import android.util.Log;

import com.belgioioso.bcimilkreceipt.bcimilkreceipt.Utilities;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbHeader;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbSettings;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbPlant;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbProfile;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class svcMilkReceipt
{
    public String sURL;
    public Utilities _oUtils;

    public svcMilkReceipt()
    {
        _oUtils = new Utilities();
    }

    //region WebService Calls
    /**
     * readJSONFeed
     *  - reads the JSON data stream from web service method
     * @param psURL - required URL for web service method
     * @return (String) - data returned from web service method
     */
    public String readJSONFeed(String psURL)
    {
        svcHttpHandler httpHandler = new svcHttpHandler();
        String jsonStr = "";

        try
        {
            //Set the url
            sURL = psURL;

            //Call to httpHandler and run the GET command
            jsonStr = httpHandler.makeGETServiceCall(sURL);

            //Check the json string result for null
            if (jsonStr == null)
            {
                //Set the return result to blank
                jsonStr = "";
            }
        }
        catch (Exception e)
        {
            Log.d("readJSONFeed", e.getLocalizedMessage());
        }

        //Return the json result
        return jsonStr;
    }

    /**
     * postJSONData
     *  - posts the JSON data to the web service method
     * @param psURL - required URL for web service method
     * @param joParams - JSON array of objects for post payload
     * @return (String) - result data from web service method
     */
    public String postJSONData(String psURL, JSONArray joParams)
    {
        svcHttpHandler httpHandler = new svcHttpHandler();
        String sResult = "";

        try
        {
            //Set the url
            sURL = psURL;

            //Call to httpHandler and run the POST command
            sResult = httpHandler.makePOSTServiceCall(sURL, joParams);

            //Check the json string result for null
            if (sResult == null || sResult == "")
            {
                //Set the return result to blank
                sResult = "0";
            }
        }
        catch (Exception e)
        {
            Log.d("postJSONData", e.getLocalizedMessage());
        }

        //Return the result
        return sResult;
    }
    //endregion

    //region Parse Routines
    /**
     * ParseSettings
     *  - parses the JSON feed to create a list of settings objects
     * @param psJSONStr - JSON data string to be parsed
     * @return (List of Settings) - list of settings objects
     */
    public List<dbSettings> ParseSettings(String psJSONStr)
    {
        List<dbSettings> olSettings = new ArrayList<>();
        dbSettings oSettings;
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        try
        {
            //Check if the JSON string is empty
            if (psJSONStr != null && psJSONStr.length() > 35)
            {
                //Instantiate a new JSON object from JSON string
                JSONObject objSettings = new JSONObject(psJSONStr);

                //Instantiate a new JSON array from the JSON object
                JSONArray arrSettings = objSettings.getJSONArray("GetSettingsDataJSONResult");

                //Loop through all of the settings objects in the JSON array
                for (int i = 0; i < arrSettings.length(); i++)
                {
                    //Instantiate a new dbSettings object
                    oSettings = new dbSettings();

                    //Read the current JSON object
                    JSONObject objSettingsSingle = arrSettings.getJSONObject(i);

                    //Load the data into the dbSettings object
                    oSettings.setPkSettingsID(objSettingsSingle.getString("pkSettingsID"));
                    oSettings.setTabletName(objSettingsSingle.getString("TabletName"));
                    oSettings.setMachineID(objSettingsSingle.getString("MachineID"));
                    oSettings.setTrackPickupGeoLocation(objSettingsSingle.getInt("TrackPickupGeoLocation"));
                    oSettings.setTrackRouteGeoLocation(objSettingsSingle.getInt("TrackRouteGeoLocation"));
                    oSettings.setDebug(objSettingsSingle.getInt("Debug"));
                    oSettings.setDownloadNotCompletedData(objSettingsSingle.getInt("DownloadNotCompletedData"));
                    oSettings.setAutoDBBackup(objSettingsSingle.getInt("AutoDBBackup"));
                    oSettings.setLastUserLoginID(objSettingsSingle.getString("LastUserLoginID"));
                    oSettings.setLastUserLoginDate(_oUtils.convertJSONDateToDate(objSettingsSingle.getString("LastUserLoginDate")));
                    oSettings.setLastMilkReceiptID(objSettingsSingle.getString("LastMilkReceiptID"));
                    oSettings.setScanLoop(objSettingsSingle.getInt("ScanLoop"));
                    oSettings.setLastSettingsUpdate(_oUtils.convertJSONDateToDate(objSettingsSingle.getString("LastSettingsUpdate")));
                    oSettings.setLastProfileUpdate(_oUtils.convertJSONDateToDate(objSettingsSingle.getString("LastProfileUpdate")));
                    oSettings.setUpdateAvailable(objSettingsSingle.getInt("UpdateAvailable"));
                    oSettings.setUpdateAvailableDate(_oUtils.convertJSONDateToDate(objSettingsSingle.getString("UpdateAvailableDate")));
                    oSettings.setDrugTestDevice(objSettingsSingle.getString("DrugTestDevice"));
                    oSettings.setWebServiceURL(objSettingsSingle.getString("WebServiceURL"));
                    oSettings.setInsertDate(_oUtils.convertJSONDateToDate(objSettingsSingle.getString("InsertDate")));
                    oSettings.setModifiedDate(_oUtils.convertJSONDateToDate(objSettingsSingle.getString("ModifiedDate")));

                    //Add the settings object to the settings list
                    olSettings.add(oSettings);
                }
            }
        }
        catch(Exception e)
        {
            Log.d("ParseSettings", e.getLocalizedMessage());
        }

        return olSettings;
    }

    /**
     * ParsePlant
     *  - parses the JSON feed to create a list of plant objects
     * @param psJSONStr - JSON data string to be parsed
     * @return (List of Plants) - list of plant objects
     */
    public List<dbPlant> ParsePlant(String psJSONStr)
    {
        List<dbPlant> olPlant = new ArrayList<>();
        dbPlant oPlant;
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        try
        {
            //Check if the JSON string is empty
            if (psJSONStr != null)
            {
                //Instantiate a new JSON object from JSON string
                JSONObject objPlant = new JSONObject(psJSONStr);

                //Instantiate a new JSON array from the JSON object
                JSONArray arrPlant = objPlant.getJSONArray("GetPlantDataJSONResult");

                //Loop through all of the plant objects in the JSON array
                for (int i = 0; i < arrPlant.length(); i++)
                {
                    //Instantiate a new dbPlant object
                    oPlant = new dbPlant();

                    //Read the current JSON object
                    JSONObject objPlantSingle = arrPlant.getJSONObject(i);

                    //Load the data into the dbPlant object
                    oPlant.setPkPlantID(objPlantSingle.getString("pkPlantID"));
                    oPlant.setPlantName(objPlantSingle.getString("PlantName"));
                    oPlant.setPlantNumber(objPlantSingle.getString("PlantNumber"));
                    oPlant.setBTUNumber(objPlantSingle.getString("BTUNumber"));
                    oPlant.setAddress(objPlantSingle.getString("Address"));
                    oPlant.setCityStateZip(objPlantSingle.getString("CityStateZip"));
                    oPlant.setLatitude(objPlantSingle.getDouble("Latitude"));
                    oPlant.setLongitude(objPlantSingle.getDouble("Longitude"));
                    oPlant.setActive(objPlantSingle.getInt("Active"));
                    oPlant.setInsertDate(_oUtils.convertJSONDateToDate(objPlantSingle.getString("InsertDate")));
                    oPlant.setModifiedDate(_oUtils.convertJSONDateToDate(objPlantSingle.getString("ModifiedDate")));

                    //Add the plant object to the plant list
                    olPlant.add(oPlant);
                }
            }
        }
        catch(Exception e)
        {
            Log.d("ParsePlant", e.getLocalizedMessage());
        }

        return olPlant;
    }

    /**
     * ParseProfile
     *  - parses the JSON feed to create a list of profile objects
     * @param psJSONStr - JSON data string to be parsed
     * @return (List of Profiles) - list of profile objects
     */
    public List<dbProfile> ParseProfile(String psJSONStr)
    {
        List<dbProfile> olProfile = new ArrayList<>();
        dbProfile oProfile;
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        try
        {
            //Check if the JSON string is empty
            if (psJSONStr != null)
            {
                //Instantiate a new JSON object from JSON string
                JSONObject objProfile = new JSONObject(psJSONStr);

                //Instantiate a new JSON array from the JSON object
                JSONArray arrProfile = objProfile.getJSONArray("GetProfileDataJSONResult");

                //Loop through all of the Profile objects in the JSON array
                for (int i = 0; i < arrProfile.length(); i++)
                {
                    //Instantiate a new dbProfile object
                    oProfile = new dbProfile();

                    //Read the current JSON object
                    JSONObject objProfileSingle = arrProfile.getJSONObject(i);

                    //Load the data into the dbProfile object
                    oProfile.setPkProfileID(objProfileSingle.getString("pkProfileID"));
                    oProfile.setFkPlantID(objProfileSingle.getString("fkPlantID"));
                    oProfile.setUsername(objProfileSingle.getString("Username"));
                    oProfile.setFullName(objProfileSingle.getString("FullName"));
                    oProfile.setInitials(objProfileSingle.getString("Initials"));
                    oProfile.setPin(objProfileSingle.getInt("Pin"));
                    oProfile.setHaulerSignature(objProfileSingle.getString("HaulerSignature"));
                    oProfile.setHaulerLicenseNumber(objProfileSingle.getString("HaulerLicenseNumber"));
                    oProfile.setHaulerExpirationDate(_oUtils.convertJSONDateToDate(objProfileSingle.getString("HaulerExpirationDate")).toString());
                    oProfile.setSignatureAgreement(objProfileSingle.getInt("SignatureAgreement"));
                    oProfile.setActive(objProfileSingle.getInt("Active"));
                    oProfile.setAdminSecurity(objProfileSingle.getInt("AdminSecurity"));
                    oProfile.setLastSignInDate(_oUtils.convertJSONDateToDate(objProfileSingle.getString("LastSignInDate")));
                    oProfile.setInsertDate(_oUtils.convertJSONDateToDate(objProfileSingle.getString("InsertDate")));
                    oProfile.setModifiedDate(_oUtils.convertJSONDateToDate(objProfileSingle.getString("ModifiedDate")));

                    //Add the Profile object to the Profile list
                    olProfile.add(oProfile);
                }
            }
        }
        catch(Exception e)
        {
            Log.d("ParseProfile", e.getLocalizedMessage());
        }

        return olProfile;
    }

    /**
     * ParseHeader
     *  - parses the JSON feed to create a list of header objects
     * @param psJSONStr - JSON data string to be parsed
     * @return (List of Headers) - list of header objects
     */
    public List<dbHeader> ParseHeader(String psJSONStr)
    {
        List<dbHeader> olHeader = new ArrayList<>();
        dbHeader oHeader;
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        try
        {
            //Check if the JSON string is empty
            if (psJSONStr != null)
            {
                psJSONStr = "{\"GetHeaderTicketNumberDataJSON\":" + psJSONStr + "}";

                //Instantiate a new JSON object from JSON string
                JSONObject objHeader = new JSONObject(psJSONStr);

                //Instantiate a new JSON array from the JSON object
                JSONArray arrHeader = objHeader.getJSONArray("GetHeaderTicketNumberDataJSON");

                //Loop through all of the Header objects in the JSON array
                for (int i = 0; i < arrHeader.length(); i++)
                {
                    //Instantiate a new dbHeader object
                    oHeader = new dbHeader();

                    //Read the current JSON object
                    JSONObject objHeaderSingle = arrHeader.getJSONObject(i);

                    //Load the data into the dbHeader object
                    oHeader.setPkHeaderID(objHeaderSingle.getString("pkHeaderID"));
                    oHeader.setFkProfileID(objHeaderSingle.getString("fkProfileID"));
                    oHeader.setTicketNumber(objHeaderSingle.getString("TicketNumber"));
                    oHeader.setRouteIdentifier(objHeaderSingle.getString("RouteIdentifier"));
                    oHeader.setTruckLicenseNumber(objHeaderSingle.getString("TruckLicenseNumber"));
                    oHeader.setStartMileage(objHeaderSingle.getInt("StartMileage"));
                    oHeader.setEndMileage(objHeaderSingle.getInt("EndMileage"));

                    //Add the Header object to the Header list
                    olHeader.add(oHeader);
                }
            }
        }
        catch(Exception e)
        {
            Log.d("ParseHeader", e.getLocalizedMessage());
        }

        return olHeader;
    }
    //endregion
}
