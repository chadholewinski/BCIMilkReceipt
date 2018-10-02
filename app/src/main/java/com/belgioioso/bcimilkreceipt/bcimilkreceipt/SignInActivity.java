package com.belgioioso.bcimilkreceipt.bcimilkreceipt;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbDatabaseHandler;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbHeader;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbLine;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbPlant;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbProfile;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbReceive;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbSettings;
import com.belgioiososvc.bcimilkreceipt.bcimilkreceiptsvc.svcMilkReceipt;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SignInActivity extends AppCompatActivity implements OnClickListener
{

    private Button _signin_login_button;
    private String _spkSettingsID;
    private Utilities _oUtils;
    private String _sWSURL = "http://10.1.2.44/MilkReceiptREST/MilkReceiptService.svc";

    //region Class Constructor Methods
    /**
     * onCreate
     *  - Constructor
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        _oUtils = new Utilities();

        //Instantiate the login button
        _signin_login_button = (Button)findViewById(R.id.signin_login_button);

        //Set the on click listener for page to the login button
        _signin_login_button.setOnClickListener(this);

        //Get settings for the current tablet and store the id in global variable
        _spkSettingsID = findSettings(android.os.Build.SERIAL);
    }

    /**
     * onCreateOptionsMenu
     * - Adds the options menu to the page
     * @param menu
     * @return - returns true or false if menu was added
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //Instantiate the menu inflater
        MenuInflater inflater = getMenuInflater();

        //Set the menu to the milkreceipt signin menu
        inflater.inflate(R.menu.milkreceipt_signin_menu, menu);

        //Return true for menu setup
        return true;
    }

    /**
     * onBackPressed
     * - Overrides the default back button press to disallow going back to a page
     */
    @Override
    public void onBackPressed()
    {
        //Display message to user
        Toast.makeText(SignInActivity.this, "Back button pressed: NOT ALLOWED!", Toast.LENGTH_SHORT).show();
    }
    //endregion

    //region User Methods
    /**
     * onOptionsItemSelected
     *  - Handles the option selected from menu
     * @param item
     * @return returns true or false if menu item was handled correctly
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        boolean bReturn = false;

        try
        {
            //Check which items was clicked
            switch (item.getItemId())
            {
                //Menu About item selected
                case R.id.menu_signin_about:
                    //Instantiate a new intent of AboutActivity
                    Intent about_intent = new Intent(this, AboutActivity.class);

                    //Navigate to the about screen
                    startActivity(about_intent);

                    //Log message to activity
                    _oUtils.InsertActivity(this, "1", "SignInActivity", "onOptionsItemSelected", "N/A", "menu_signin_about item selected", "");

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Menu Activity item selected
                case R.id.menu_signin_activity:
                    //Instantiate a new intent of LogActivity
                    Intent log_intent = new Intent(this, LogActivity.class);

                    //Navigate to the activity screen
                    startActivity(log_intent);

                    //Log message to activity
                    _oUtils.InsertActivity(this, "1", "SignInActivity", "onOptionsItemSelected", "N/A", "menu_signin_activity item selected", "");

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Menu Settings item selected
                case R.id.menu_signin_settings:
                    //Instantiate a new intent of SettingsActivity
                    Intent settings_intent = new Intent(this, SettingsActivity.class);

                    //Navigate to the settings screen
                    startActivity(settings_intent);

                    //Log message to activity
                    _oUtils.InsertActivity(this, "1", "SignInActivity", "onOptionsItemSelected", "N/A", "menu_signin_settings item selected", "");

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Menu Sync item selected
                case R.id.menu_signin_sync:
                    //Sync data to the web service
                    syncToWebService();

                    //Log message to activity
                    _oUtils.InsertActivity(this, "1", "SignInActivity", "onOptionsItemSelected", "N/A", "menu_signin_sync item selected", "");

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Menu CopyDB item selected
                case R.id.menu_signin_copydb:
                    //Check if required permissions are set for external storage
                    if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED )
                    {
                        //Request the external storage permissions
                        ActivityCompat.requestPermissions( this, new String[] { android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1 );
                    }
                    else
                    {
                        //Copy the sqlite db file to accessible folder
                        copyDBFile();
                    }

                    //Log message to activity
                    _oUtils.InsertActivity(this, "1", "SignInActivity", "onOptionsItemSelected", "N/A", "menu_signin_copydb item selected", "");

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Default action
                default:
                    //Set the return value
                    bReturn = super.onOptionsItemSelected(item);

                    break;
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "SignInActivity", "onOptionsItemSelected", "N/A", ex.getMessage().toString(), ex.getStackTrace().toString());
        }

        //Return the value
        return bReturn;
    }

    /**
     * onClick
     *  - Handles the onClick event for the page
     * @param v
     */
    public void onClick(View v)
    {
        String spkProfileID;
        boolean bCredVerified;
        EditText oUsername, oPin;

        try
        {
            //Check if the login button was pressed
            if (v.getId() == R.id.signin_login_button)
            {
                //Log activity
                _oUtils.InsertActivity(this, "1", "SignInActivity", "onClick", "N/A", "SignIn button pressed", "");

                //Get the username and pin from screen
                oUsername = (EditText)findViewById(R.id.signin_username);
                oPin = (EditText)findViewById(R.id.signin_pin);

                //Get the profileID based on user and pin entered from database
                spkProfileID = findProfileByUsernamePin(oUsername.getText().toString(), oPin.getText().toString());

                //Check if the profileID was retrieved
                if (spkProfileID.length() > 0)
                {
                    //Set credentials verified value to true
                    bCredVerified = true;
                }
                else
                {
                    //Set credentials verified value to false
                    bCredVerified = false;
                }

                //Check if the credentials were verified
                if (bCredVerified)
                {
                    //Log activity
                    _oUtils.InsertActivity(this, "1", "SignInActivity", "onClick", spkProfileID, "SignIn successful", "");

                    //Instantiate a new intent of receipt activity page
                    Intent intent = new Intent(this, ReceiptActivity.class);

                    //Instantiate the bundle object
                    Bundle oBundle = new Bundle();

                    //Set the profileID and settingsID in the bundle
                    oBundle.putString("pkProfileID", spkProfileID);
                    oBundle.putString("pkSettingsID", _spkSettingsID);

                    //Setup bundle into intent
                    intent.putExtras(oBundle);

                    //Navigate to the receipt activity page
                    startActivity(intent);
                }
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "SignInActivity", "onClick", "N/A", ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }
    //endregion

    //region Routines
    /**
     * findSettings
     * - Gets the settings object with data from database
     * @param ptmDevice
     * @return returns the
     */
    private String findSettings(String ptmDevice)
    {
        String sReturnID = "";

        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

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
                oSettingsNew.setLastUserLoginID("00000000-0000-0000-0000-000000000000");
                oSettingsNew.setLastUserLoginDate("1/1/1900");
                oSettingsNew.setLastMilkReceiptID("00000000-0000-0000-0000-000000000000");
                oSettingsNew.setScanLoop(1);
                oSettingsNew.setLastSettingsUpdate("1/1/1900");
                oSettingsNew.setLastProfileUpdate("1/1/1900");
                oSettingsNew.setUpdateAvailable(0);
                oSettingsNew.setUpdateAvailableDate("1/1/1900");
                oSettingsNew.setDrugTestDevice("CharmSLRosa");
                oSettingsNew.setWebServiceURL(_sWSURL);

                //Format the date for insert and modified
                DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                Date dDate = new Date();

                //Set the insert and modified date fields
                oSettingsNew.setInsertDate(dfDate.format(dDate).toString());
                oSettingsNew.setModifiedDate(dfDate.format(dDate).toString());

                //Add the settings record to database
                oDBHandler.addSettings(oSettingsNew);

                //Set the return settingsID
                sReturnID = gID.toString();

                //Send the new settings record to web service
                postSettingsToWS(oSettingsNew);

                //Log activity
                _oUtils.InsertActivity(this, "1", "SignInActivity", "findSettings", "N/A", "Settings not found, new settings record saved", "");
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
            _oUtils.InsertActivity(this, "3", "SignInActivity", "findSettings", "N/A", ex.getMessage().toString(), ex.getStackTrace().toString());
        }

        //Return the settingsID
        return sReturnID;
    }

    /**
     * findProfileByUsernamePin
     * - Get the profile by username and pin
     * @param psUsername
     * @param psPin
     * @return returns the profileID from database
     */
    private String findProfileByUsernamePin(String psUsername, String psPin)
    {
        String sProfileID = "";

        try
        {
            //Instantiate the database handler object
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the profile record from the database by username and pin
            dbProfile oProfile = oDBHandler.findProfileByUserPin(psUsername, psPin);

            //Check if the profile record was found
            if (oProfile == null)
            {
                //Set the profileID to blank
                sProfileID = "";

                //Log activity
                _oUtils.InsertActivity(this, "1", "SignInActivity", "findProfileByUsernamePin", "N/A", "Profile not found for username: " + psUsername, "");
            }
            else
            {
                //Set the profileID to the ID retrieved from the database
                sProfileID = oProfile.getPkProfileID();

                //Log activity
                _oUtils.InsertActivity(this, "1", "SignInActivity", "findProfileByUsernamePin", "N/A", "Profile found for: " + oProfile.getFullName(), "");
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "SignInActivity", "findProfileByUsernamePin", "N/A", ex.getMessage().toString(), ex.getStackTrace().toString());
        }

        //Return the profileID
        return sProfileID;
    }

    /**
     * copyDBFile
     * - Copies the database file to accessible folder
     * @throws IOException
     */
    public void copyDBFile() throws IOException
    {
        File backupDB = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "MilkReceipt.db"); // for example "my_data_backup.db"
        File currentDB = getApplicationContext().getDatabasePath("MilkReceipt.db"); //databaseName=your current application database name, for example "my_data.db"

        //Check if the current DB file exists
        if (currentDB.exists())
        {
            //Check if a copy of database has already been copied to folder
            if (backupDB.exists())
            {
                //Delete the database copy file
                backupDB.delete();
            }

            //Instantiate the source and destination file streams
            FileChannel src = new FileInputStream(currentDB).getChannel();
            FileChannel dst = new FileOutputStream(backupDB).getChannel();

            //Copy the database file to destination folder
            dst.transferFrom(src, 0, src.size());

            //Close the source and destination file streams
            src.close();
            dst.close();
        }
    }

    /**
     * syncToWebService
     * - Runs the background web service sync process
     */
    public void syncToWebService()
    {
        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the settings object from database
            dbSettings oSettings = oDBHandler.findSettingsByName(android.os.Build.SERIAL);

            //Check if settings object is populated
            if (oSettings != null)
            {
                //Connect to web service and get the settings record by serial #
                new SignInActivity.GetSettings().execute(oSettings.getWebServiceURL() + "/GetSettingsDataJSON/" + android.os.Build.SERIAL);
                //new SignInActivity.GetSettings().execute("http://10.1.2.44/MilkReceiptREST/MilkReceiptService.svc/GetSettingsDataJSON/" + android.os.Build.SERIAL);

                //Connect to web service and get the profile records by last date
                //new SignInActivity.GetProfiles().execute(oSettings.getWebServiceURL() + "/GetProfileDataJSON/" + oSettings.getLastProfileUpdate());
                new SignInActivity.GetProfiles().execute(oSettings.getWebServiceURL() + "/GetProfileDataJSON/1900-01-01T000000");
                //new SignInActivity.GetProfiles().execute("http://10.1.2.44/MilkReceiptREST/MilkReceiptService.svc/GetProfileDataJSON/1900-01-01T000000");

                //Connect to web service and get the plant records by last date
                new SignInActivity.GetPlants().execute(oSettings.getWebServiceURL() + "/GetPlantDataJSON/1900-01-01T000000");
                //new SignInActivity.GetPlants().execute("http://10.1.2.44/MilkReceiptREST/MilkReceiptService.svc/GetPlantDataJSON/1900-01-01T000000");

                //Connect to web service and post non-transferred header data
                new SignInActivity.PostHeader().execute(oSettings.getWebServiceURL() + "/PostHeaderDataJSON");
                //new SignInActivity.PostHeader().execute("http://10.1.2.44/MilkReceiptREST/MilkReceiptService.svc/PostHeaderDataJSON");

                //Connect to web service and post non-transferred line data
                new SignInActivity.PostLine().execute(oSettings.getWebServiceURL() + "/PostLineDataJSON");
                //new SignInActivity.PostLine().execute("http://10.1.2.44/MilkReceiptREST/MilkReceiptService.svc/PostLineDataJSON");

                //Connect to web service and post non-transferred receive data
                new SignInActivity.PostReceive().execute(oSettings.getWebServiceURL() + "/PostReceiveDataJSON");
                //new SignInActivity.PostReceive().execute("http://10.1.2.44/MilkReceiptREST/MilkReceiptService.svc/PostReceiveDataJSON");
            }
            else
            {
                //Connect to web service and get the settings record by serial #
                new SignInActivity.GetSettings().execute(_sWSURL + "/GetSettingsDataJSON/" + android.os.Build.SERIAL);

                //Connect to web service and get the profile records by last date
                new SignInActivity.GetProfiles().execute(_sWSURL + "/GetProfileDataJSON/1900-01-01T000000");

                //Connect to web service and get the plant records by last date
                new SignInActivity.GetPlants().execute(_sWSURL + "/GetPlantDataJSON/1900-01-01T000000");

                //Connect to web service and post non-transferred header data
                new SignInActivity.PostHeader().execute(_sWSURL + "/PostHeaderDataJSON");

                //Connect to web service and post non-transferred line data
                new SignInActivity.PostLine().execute(_sWSURL + "/PostLineDataJSON");

                //Connect to web service and post non-transferred receive data
                new SignInActivity.PostReceive().execute(_sWSURL + "/PostReceiveDataJSON");
            }
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "SignInActivity", "syncToWebService", "N/A", ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }

    /**
     * postSettingsToWS
     * - Posts the settings information to the web service
     * @param poSettings
     */
    public void postSettingsToWS(dbSettings poSettings)
    {
        String sURL;
        String sResult;
        JSONObject joParams;
        JSONArray jaParams;
        svcMilkReceipt oService = new svcMilkReceipt();
        
        try
        {
            sURL = "http://10.1.2.44/MilkReceiptREST/MilkReceiptService.svc/PostSettingsDataJSON";

            //Instantiate the JSON Array
            jaParams = new JSONArray();
            
            //Instantiate a new JSON object
            joParams = new JSONObject();

            //Fill the JSON object with data
            joParams.put(poSettings.SETTINGS_COLUMN_PKSETTINGSID, poSettings.getPkSettingsID());
            joParams.put(poSettings.SETTINGS_COLUMN_TABLETNAME, poSettings.getTabletName());
            joParams.put(poSettings.SETTINGS_COLUMN_MACHINEID, poSettings.getMachineID());
            joParams.put(poSettings.SETTINGS_COLUMN_TRACKPICKUPGEOLOCATION, poSettings.getTrackPickupGeoLocation());
            joParams.put(poSettings.SETTINGS_COLUMN_TRACKROUTEGEOLOCATION, poSettings.getTrackRouteGeoLocation());
            joParams.put(poSettings.SETTINGS_COLUMN_DEBUG, poSettings.getDebug());
            joParams.put(poSettings.SETTINGS_COLUMN_DOWNLOADNOTCOMPLETEDDATA, poSettings.getDownloadNotCompletedData());
            joParams.put(poSettings.SETTINGS_COLUMN_AUTODBBACKUP, poSettings.getAutoDBBackup());
            joParams.put(poSettings.SETTINGS_COLUMN_LASTUSERLOGINID, poSettings.getLastUserLoginID());
            joParams.put(poSettings.SETTINGS_COLUMN_LASTUSERLOGINDATE, poSettings.getLastUserLoginDate());
            joParams.put(poSettings.SETTINGS_COLUMN_LASTMILKRECEIPTID, poSettings.getLastMilkReceiptID());
            joParams.put(poSettings.SETTINGS_COLUMN_SCANLOOP, poSettings.getScanLoop());
            joParams.put(poSettings.SETTINGS_COLUMN_LASTSETTINGSUPDATE, poSettings.getLastSettingsUpdate());
            joParams.put(poSettings.SETTINGS_COLUMN_LASTPROFILEUPDATE, poSettings.getLastProfileUpdate());
            joParams.put(poSettings.SETTINGS_COLUMN_UPDATEAVAILABLE, poSettings.getUpdateAvailable());
            joParams.put(poSettings.SETTINGS_COLUMN_UPDATEAVAILABLEDATE, poSettings.getUpdateAvailableDate());
            joParams.put(poSettings.SETTINGS_COLUMN_DRUGTESTDEVICE, poSettings.getDrugTestDevice());
            joParams.put(poSettings.SETTINGS_COLUMN_WEBSERVICEURL, poSettings.getWebServiceURL());
            joParams.put(poSettings.SETTINGS_COLUMN_INSERTDATE, poSettings.getInsertDate());
            joParams.put(poSettings.SETTINGS_COLUMN_MODIFIEDDATE, poSettings.getModifiedDate());

            //Add the JSON object to the array of JSON objects
            jaParams.put(joParams);

            //Post the header data to the web service
            sResult = oService.postJSONData(sURL, jaParams);

            //Check if the result of posted settings record is the same as what was sent
            if (Integer.parseInt(sResult) == 1)
            {
                //Log message to activity
                _oUtils.InsertActivity(this, "1", "SignInActivity", "postSettingsToWS", "N/A", "Successfully uploaded settings to web service", "");
            }
            else
            {
                //Log message to activity
                _oUtils.InsertActivity(this, "1", "SignInActivity", "postSettingsToWS", "N/A", "Failure of uploaded settings to web service", "");
            }
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "SignInActivity", "postSettingsToWS", "N/A", ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }
    //endregion

    //region Class Profile Background Task
    private class GetProfiles extends AsyncTask<String, Void, List<dbProfile>>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected List<dbProfile> doInBackground(String... psURL)
        {
            String sResult;
            svcMilkReceipt oService = new svcMilkReceipt();
            List<dbProfile> olProfile;

            //Get the profile list from the web service
            sResult = oService.readJSONFeed(psURL[0]);

            //Parse the list and create profile object array
            olProfile = oService.ParseProfile(sResult);

            //Return the profile object array
            return olProfile;
        }

        @Override
        protected void onPostExecute(List<dbProfile> polProfile)
        {
            super.onPostExecute(polProfile);

            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(getApplicationContext(), null);

            //Setup the profile and profilefound objects
            dbProfile oProfile, oProfileFound;

            //Loop through all profiles in the list
            for (int i=0; i<polProfile.size(); i++)
            {
                //Get the profile object in list
                oProfile = polProfile.get(i);

                //Get profile record from the database
                oProfileFound = oDBHandler.findProfileByID(oProfile.getPkProfileID());

                //Check if the profile record was found
                if (oProfileFound == null)
                {
                    //Update the profile record
                    oDBHandler.addProfile(oProfile);
                }
                else
                {
                    //Insert the profile record
                    oDBHandler.updateProfile(oProfile);
                }
            }
        }
    }
    //endregion

    //region Class Settings Background Task
    private class GetSettings extends AsyncTask<String, Void, List<dbSettings>>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected List<dbSettings> doInBackground(String... psURL)
        {
            String sResult;
            svcMilkReceipt oService = new svcMilkReceipt();
            List<dbSettings> olSettings;

            //Get the settings record from the web service
            sResult = oService.readJSONFeed(psURL[0]);

            //Parse the settings records and create the settings object array
            olSettings = oService.ParseSettings(sResult);

            //Return the settings object array
            return olSettings;
        }

        @Override
        protected void onPostExecute(List<dbSettings> polSettings)
        {
            super.onPostExecute(polSettings);

            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(getApplicationContext(), null);

            //Setup the settings and settingsfound objects
            dbSettings oSettings, oSettingsFound;

            //Loop through all settings records in the list
            for (int i=0; i<polSettings.size(); i++)
            {
                //Get the settings object in list
                oSettings = polSettings.get(i);

                //Get the settings record from the database
                oSettingsFound = oDBHandler.findSettingsByID(oSettings.getPkSettingsID());

                //Check if the settings record was found
                if (oSettingsFound == null)
                {
                    //Update the settings record
                    oDBHandler.addSettings(oSettings);
                }
                else
                {
                    //Insert the settings record
                    oDBHandler.updateSettings(oSettings);
                }
            }
        }
    }
    //endregion

    //region Class Plant Background Task
    private class GetPlants extends AsyncTask<String, Void, List<dbPlant>>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected List<dbPlant> doInBackground(String... psURL)
        {
            String sResult;
            svcMilkReceipt oService = new svcMilkReceipt();
            List<dbPlant> olPlant;

            //Get the plant list from the web service
            sResult = oService.readJSONFeed(psURL[0]);

            //Parse the list and create plant object array
            olPlant = oService.ParsePlant(sResult);

            //Return the plant object array
            return olPlant;
        }

        @Override
        protected void onPostExecute(List<dbPlant> polPlant)
        {
            super.onPostExecute(polPlant);

            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(getApplicationContext(), null);

            //Setup the plant and plantfound objects
            dbPlant oPlant, oPlantFound;

            //Loop through all plant in the list
            for (int i=0; i<polPlant.size(); i++)
            {
                //Get the plant object in list
                oPlant = polPlant.get(i);

                //Get plant record from the database
                oPlantFound = oDBHandler.findPlantByID(oPlant.getPkPlantID());

                //Check if the plant record was found
                if (oPlantFound == null)
                {
                    //Update the plant record
                    oDBHandler.addPlant(oPlant);
                }
                else
                {
                    //Insert the plant record
                    oDBHandler.updatePlant(oPlant);
                }
            }
        }
    }
    //endregion

    //region Class PostHeader Background Task
    private class PostHeader extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... psURL)
        {
            String sResult;
            JSONObject joParams;
            JSONArray jaParams;
            svcMilkReceipt oService = new svcMilkReceipt();
            dbHeader oHeader;
            List<dbHeader> olHeader;

            try
            {
                //Instantiate the database handler
                dbDatabaseHandler oDBHandler = new dbDatabaseHandler(getApplicationContext(), null);

                olHeader = oDBHandler.findHeadersNonTransmitted();

                if (olHeader != null)
                {
                    if (!olHeader.isEmpty())
                    {
                        //Instantiate the JSON Array
                        jaParams = new JSONArray();

                        //Loop through all header records in the list
                        for (int i = 0; i < olHeader.size(); i++)
                        {
                            //Get the current header record
                            oHeader = olHeader.get(i);

                            //Instantiate a new JSON object
                            joParams = new JSONObject();

                            //Fill the JSON object with data
                            joParams.put(oHeader.HEADER_COLUMN_PKHEADERID, oHeader.getPkHeaderID());
                            joParams.put(oHeader.HEADER_COLUMN_FKPROFILEID, oHeader.getFkProfileID());
                            joParams.put("fkSettingsID", _spkSettingsID);
                            joParams.put(oHeader.HEADER_COLUMN_TICKETNUMBER, oHeader.getTicketNumber());
                            joParams.put(oHeader.HEADER_COLUMN_ROUTEIDENTIFIER, oHeader.getRouteIdentifier());
                            joParams.put(oHeader.HEADER_COLUMN_TRUCKLICENSENUMBER, oHeader.getTruckLicenseNumber());
                            joParams.put(oHeader.HEADER_COLUMN_STARTMILEAGE, oHeader.getStartMileage());
                            joParams.put(oHeader.HEADER_COLUMN_ENDMILEAGE, oHeader.getEndMileage());
                            joParams.put(oHeader.HEADER_COLUMN_TOTALMILEAGE, oHeader.getTotalMileage());
                            joParams.put(oHeader.HEADER_COLUMN_INSERTDATE, oHeader.getInsertDate());
                            joParams.put(oHeader.HEADER_COLUMN_MODIFIEDDATE, oHeader.getModifiedDate());

                            //Add the JSON object to the array of JSON objects
                            jaParams.put(joParams);
                        }

                        //Post the header data to the web service
                        sResult = oService.postJSONData(psURL[0], jaParams);

                        //Check if the result of posted header records is the same as what was sent
                        if (Integer.parseInt(sResult) == olHeader.size())
                        {
                            //Loop through all header records in the list
                            for (int j = 0; j < olHeader.size(); j++)
                            {
                                //Get the current header record
                                oHeader = olHeader.get(j);

                                //Update the transmitted fields
                                oHeader.setTransmitted(1);
                                oHeader.setTransmittedDate(Calendar.getInstance().getTime().toString());

                                //Update the header record
                                oDBHandler.updateHeader(oHeader);
                            }
                        }
                    }
                    else
                    {
                        //Set the return status
                        sResult = "0";
                    }
                }
                else
                {
                    //Set the return status
                    sResult = "0";
                }
            }
            catch(Exception ex)
            {
                sResult = "0";
            }

            //Return the post result
            return sResult;
        }

        @Override
        protected void onPostExecute(String psString)
        {
            super.onPostExecute(psString);
        }
    }
    //endregion

    //region Class PostLine Background Task
    private class PostLine extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... psURL)
        {
            String sResult;
            JSONObject joParams;
            JSONArray jaParams;
            svcMilkReceipt oService = new svcMilkReceipt();
            dbLine oLine;
            List<dbLine> olLine;

            try
            {
                //Instantiate the database handler
                dbDatabaseHandler oDBHandler = new dbDatabaseHandler(getApplicationContext(), null);

                olLine = oDBHandler.findLinesNonTransmitted();

                if(olLine != null)
                {
                    if (!olLine.isEmpty())
                    {
                        //Instantiate the JSON Array
                        jaParams = new JSONArray();

                        //Loop through all line records in the list
                        for (int i=0; i<olLine.size(); i++)
                        {
                            //Get the current line record
                            oLine = olLine.get(i);

                            //Instantiate a new JSON object
                            joParams = new JSONObject();

                            //Fill the JSON object with data
                            joParams.put(oLine.LINE_COLUMN_PKLINEID, oLine.getPkLineID());
                            joParams.put(oLine.LINE_COLUMN_FKHEADERID, oLine.getFkHeaderID());
                            joParams.put("fkSettingsID", _spkSettingsID);
                            joParams.put(oLine.LINE_COLUMN_TANK, oLine.getTank());
                            joParams.put(oLine.LINE_COLUMN_PRODUCER, oLine.getProducer());
                            joParams.put(oLine.LINE_COLUMN_COMPANY, _oUtils.CheckNullString(oLine.getCompany()));
                            joParams.put(oLine.LINE_COLUMN_DIVISION, _oUtils.CheckNullString(oLine.getDivision()));
                            joParams.put(oLine.LINE_COLUMN_TYPE, _oUtils.CheckNullString(oLine.getType()));
                            joParams.put(oLine.LINE_COLUMN_GAUGERODMAJOR, oLine.getGaugeRodMajor());
                            joParams.put(oLine.LINE_COLUMN_GAUGERODMINOR, oLine.getGaugeRodMinor());
                            joParams.put(oLine.LINE_COLUMN_CONVERTEDLBS, oLine.getConvertedLBS());
                            joParams.put(oLine.LINE_COLUMN_TEMPERATURE, oLine.getTemperature());
                            joParams.put(oLine.LINE_COLUMN_PICKUPDATE, oLine.getPickupDate());
                            joParams.put(oLine.LINE_COLUMN_DFATICKET, oLine.getDFATicket());
                            joParams.put(oLine.LINE_COLUMN_LABCODE, oLine.getLabCode());
                            joParams.put(oLine.LINE_COLUMN_LATITUDE, oLine.getLatitude());
                            joParams.put(oLine.LINE_COLUMN_LONGITUDE, oLine.getLongitude());
                            joParams.put(oLine.LINE_COLUMN_ACCURRACY, oLine.getAccurracy());
                            joParams.put(oLine.LINE_COLUMN_INSERTDATE, oLine.getInsertDate());
                            joParams.put(oLine.LINE_COLUMN_MODIFIEDDATE, oLine.getModifiedDate());

                            //Add the JSON object to the array of JSON objects
                            jaParams.put(joParams);
                        }

                        //Post the line data to the web service
                        sResult = oService.postJSONData(psURL[0], jaParams);

                        //Check if the result of posted line records is the same as what was sent
                        if (Integer.parseInt(sResult) == olLine.size())
                        {
                            //Loop through all line records in the list
                            for (int j=0; j<olLine.size(); j++)
                            {
                                //Get the current line record
                                oLine = olLine.get(j);

                                //Update the transmitted fields
                                oLine.setTransmitted(1);
                                oLine.setTransmittedDate(Calendar.getInstance().getTime().toString());

                                //Update the line record
                                oDBHandler.updateLine(oLine);
                            }
                        }
                    }
                    else
                    {
                        //Set the return status
                        sResult = "0";
                    }
                }
                else
                {
                    //Set the return status
                    sResult = "0";
                }
            }
            catch(Exception ex)
            {
                sResult = "0";
            }

            //Return the post result
            return sResult;
        }

        @Override
        protected void onPostExecute(String psString)
        {
            super.onPostExecute(psString);
        }
    }
    //endregion

    //region Class PostReceive Background Task
    private class PostReceive extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... psURL)
        {
            String sResult;
            JSONObject joParams;
            JSONArray jaParams;
            svcMilkReceipt oService = new svcMilkReceipt();
            dbReceive oReceive;
            List<dbReceive> olReceive;

            try
            {
                //Instantiate the database handler
                dbDatabaseHandler oDBHandler = new dbDatabaseHandler(getApplicationContext(), null);

                olReceive = oDBHandler.findReceivesNonTransmitted();

                if (olReceive != null)
                {
                    if (!olReceive.isEmpty())
                    {
                        //Instantiate the JSON Array
                        jaParams = new JSONArray();

                        //Loop through all receive records in the list
                        for (int i=0; i<olReceive.size(); i++)
                        {
                            //Get the current receive record
                            oReceive = olReceive.get(i);

                            //Instantiate a new JSON object
                            joParams = new JSONObject();

                            //Fill the JSON object with data
                            joParams.put(oReceive.RECEIVE_COLUMN_PKRECEIVEID, oReceive.getPkReceiveID());
                            joParams.put(oReceive.RECEIVE_COLUMN_FKHEADERID, oReceive.getFkHeaderID());
                            joParams.put("fkSettingsID", _spkSettingsID);
                            joParams.put(oReceive.RECEIVE_COLUMN_FKPLANTID, oReceive.getFkPlantID());
                            //joParams.put(oReceive.RECEIVE_COLUMN_FKPLANTORIGINALID, oReceive.getFkPlantOriginalID());
                            joParams.put(oReceive.RECEIVE_COLUMN_FKPLANTORIGINALID, "00000000-0000-0000-0000-000000000000");
                            joParams.put(oReceive.RECEIVE_COLUMN_DRUGTESTDEVICE, oReceive.getDrugTestDevice());
                            joParams.put(oReceive.RECEIVE_COLUMN_DRUGTESTRESULT, oReceive.getDrugTestResult());
                            joParams.put(oReceive.RECEIVE_COLUMN_RECEIVEDATETIME, oReceive.getReceiveDateTime());
                            joParams.put(oReceive.RECEIVE_COLUMN_TANK, oReceive.getTank());
                            joParams.put(oReceive.RECEIVE_COLUMN_SCALEMETER, oReceive.getScaleMeter());
                            joParams.put(oReceive.RECEIVE_COLUMN_TOPSEAL, oReceive.getTopSeal());
                            joParams.put(oReceive.RECEIVE_COLUMN_BOTTOMSEAL, oReceive.getBottomSeal());
                            joParams.put(oReceive.RECEIVE_COLUMN_RECEIVEDLBS, oReceive.getReceivedLBS());
                            joParams.put(oReceive.RECEIVE_COLUMN_LOADTEMP, oReceive.getLoadTemp());
                            joParams.put(oReceive.RECEIVE_COLUMN_INTAKENUMBER, oReceive.getIntakeNumber());
                            joParams.put(oReceive.RECEIVE_COLUMN_INSERTDATE, oReceive.getInsertDate());
                            joParams.put(oReceive.RECEIVE_COLUMN_MODIFIEDDATE, oReceive.getModifiedDate());

                            //Add the JSON object to the array of JSON objects
                            jaParams.put(joParams);
                        }

                        //Post the receive data to the web service
                        sResult = oService.postJSONData(psURL[0], jaParams);

                        //Check if the result of posted receive records is the same as what was sent
                        if (Integer.parseInt(sResult) == olReceive.size())
                        {
                            //Loop through all receive records in the list
                            for (int j=0; j<olReceive.size(); j++)
                            {
                                //Get the current receive record
                                oReceive = olReceive.get(j);

                                //Update the transmitted fields
                                oReceive.setTransmitted(1);
                                oReceive.setTransmittedDate(Calendar.getInstance().getTime().toString());

                                //Update the line record
                                oDBHandler.updateReceive(oReceive);
                            }
                        }
                    }
                    else
                    {
                        //Set the return status
                        sResult = "0";
                    }
                }
                else
                {
                    //Set the return status
                    sResult = "0";
                }
            }
            catch(Exception ex)
            {
                Toast.makeText(SignInActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();

                sResult = "0";
            }

            //Return the post result
            return sResult;
        }

        @Override
        protected void onPostExecute(String psString)
        {
            super.onPostExecute(psString);
        }
    }
    //endregion
}
