package com.belgioioso.bcimilkreceipt.bcimilkreceipt;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbActivityHeader;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SignInActivity extends AppCompatActivity implements OnClickListener
{

    private Button _signin_login_button;
    private ProgressBar _signin_progressbar;
    private TextView _signin_progresslabel;
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

        //Set the keyboard to not show automatically
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        _oUtils = new Utilities();

        //Instantiate the login button
        _signin_login_button = (Button)findViewById(R.id.signin_login_button);

        //Instantiate the progress bar
        _signin_progressbar = (ProgressBar)findViewById(R.id.signin_progressbar);

        //Instantiate the progress bar label
        _signin_progresslabel = (TextView)findViewById(R.id.signin_progresslabel);

        //Set the on click listener for page to the login button
        _signin_login_button.setOnClickListener(this);

        //Get settings for the current tablet and store the id in global variable
        _spkSettingsID = _oUtils.findSettings(this, "N/A", android.os.Build.SERIAL);

        //Sync data to the web service
        syncToWebService();
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
                    _oUtils.insertActivity(this, "1", "SignInActivity", "onOptionsItemSelected", "N/A", "SignIn menu about selected", "");

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Menu Sync item selected
                case R.id.menu_signin_sync:
                    //Sync data to the web service
                    syncToWebService();

                    //Log message to activity
                    _oUtils.insertActivity(this, "1", "SignInActivity", "onOptionsItemSelected", "N/A", "SignIn menu sync data selected", "");

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
            _oUtils.insertActivity(this, "3", "SignInActivity", "onOptionsItemSelected", "N/A", ex.getMessage().toString(), ex.getStackTrace().toString());
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
                _oUtils.insertActivity(this, "1", "SignInActivity", "onClick", "N/A", "SignIn button pressed", "");

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
                    _oUtils.insertActivity(this, "1", "SignInActivity", "onClick", spkProfileID, "SignIn successful", "");

                    //Update the settings information
                    updateSettingsForLastUserLoggedIn(spkProfileID);

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
            _oUtils.insertActivity(this, "3", "SignInActivity", "onClick", "N/A", ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }
    //endregion

    //region Routines
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
                _oUtils.insertActivity(this, "1", "SignInActivity", "findProfileByUsernamePin", "N/A", "Profile not found for username: " + psUsername, "");
            }
            else
            {
                //Set the profileID to the ID retrieved from the database
                sProfileID = oProfile.getPkProfileID();

                //Log activity
                _oUtils.insertActivity(this, "1", "SignInActivity", "findProfileByUsernamePin", "N/A", "Profile found for: " + oProfile.getFullName(), "");
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "SignInActivity", "findProfileByUsernamePin", "N/A", ex.getMessage().toString(), ex.getStackTrace().toString());
        }

        //Return the profileID
        return sProfileID;
    }

    /**
     * updateSettingsForLastUserLoggedIn
     *  - updates the settings record with last user logged in information
     * @param spkProfileID
     */
    private void updateSettingsForLastUserLoggedIn(String spkProfileID)
    {
        try
        {
            //Instantiate the database handler object
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the current settings record
            dbSettings oSettings = oDBHandler.findSettingsByID(_spkSettingsID);

            //Update the settings user logged in fields
            oSettings.setLastUserLoginID(spkProfileID);
            oSettings.setLastUserLoginDate(_oUtils.getFormattedDate(this, "N/A"));
            oSettings.setModifiedDate(_oUtils.getFormattedDate(this, "N/A"));

            //Update the settings record
            oDBHandler.updateSettings(oSettings);
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "SignInActivity", "updateSettingsForLastUserLoggedIn", "N/A", ex.getMessage().toString(), ex.getStackTrace().toString());
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
            //Set the progress bar to 0
            _signin_progressbar.setProgress(0);
            _signin_progresslabel.setText("Start of data syncronization (0%)");

            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the settings object from database
            dbSettings oSettings = oDBHandler.findSettingsByName(android.os.Build.SERIAL);

            //Check if settings object is populated
            if (oSettings != null)
            {
                //Connect to web service and post settings data
                new SignInActivity.PostSettings().execute(oSettings.getWebServiceURL() + "/PostSettingsDataJSON");

                //Connect to web service and get the settings record by serial #
                new SignInActivity.GetSettings().execute(oSettings.getWebServiceURL() + "/GetSettingsDataJSON/" + android.os.Build.SERIAL);

                //Connect to web service and get the profile records by last date
                //new SignInActivity.GetProfiles().execute(oSettings.getWebServiceURL() + "/GetProfileDataJSON/" + oSettings.getLastProfileUpdate());
                new SignInActivity.GetProfiles().execute(oSettings.getWebServiceURL() + "/GetProfileDataJSON/1900-01-01T000000");

                //Connect to web service and get the plant records by last date
                new SignInActivity.GetPlants().execute(oSettings.getWebServiceURL() + "/GetPlantDataJSON/1900-01-01T000000");

                //Connect to web service and post non-transferred header data
                new SignInActivity.PostHeader().execute(oSettings.getWebServiceURL() + "/PostHeaderDataJSON");

                //Connect to web service and post non-transferred line data
                new SignInActivity.PostLine().execute(oSettings.getWebServiceURL() + "/PostLineDataJSON");

                //Connect to web service and post non-transferred receive data
                new SignInActivity.PostReceive().execute(oSettings.getWebServiceURL() + "/PostReceiveDataJSON");

                //Connect to web service and post non-transferred receive data
                new SignInActivity.PostActivity().execute(oSettings.getWebServiceURL() + "/PostActivityDataJSON");

                //Connect to web service and get ticket numbers for partially completed tickets
                new SignInActivity.GetHeaderTicketNumbers().execute(oSettings.getWebServiceURL() + "/GetHeaderTicketNumberDataJSON");
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

                //Connect to web service and post non-transferred receive data
                new SignInActivity.PostActivity().execute(_sWSURL + "/PostActivityDataJSON");

                //Connect to web service and get ticket numbers for partially completed tickets
                new SignInActivity.GetHeaderTicketNumbers().execute(_sWSURL + "/GetHeaderTicketNumberDataJSON");
            }
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "SignInActivity", "syncToWebService", "N/A", ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }
    //endregion

    //region Class PostSettings Background Task
    private class PostSettings extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            //Disable the login button until data sync is finished
            _signin_login_button.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... psURL)
        {
            String sResult;
            JSONObject joParams;
            JSONArray jaParams;
            svcMilkReceipt oService = new svcMilkReceipt();
            dbSettings oSettings;

            try
            {
                //Instantiate the database handler
                dbDatabaseHandler oDBHandler = new dbDatabaseHandler(getApplicationContext(), null);

                //Get the settings from the database
                oSettings = oDBHandler.findSettingsByID(_spkSettingsID);

                //Check if the settings object is null
                if (oSettings != null)
                {
                    //Instantiate the JSON Array and Object
                    jaParams = new JSONArray();
                    joParams = new JSONObject();

                    //Fill the JSON object with data
                    joParams.put(oSettings.SETTINGS_COLUMN_PKSETTINGSID, oSettings.getPkSettingsID());
                    joParams.put(oSettings.SETTINGS_COLUMN_TABLETNAME, oSettings.getTabletName());
                    joParams.put(oSettings.SETTINGS_COLUMN_MACHINEID, oSettings.getMachineID());
                    joParams.put(oSettings.SETTINGS_COLUMN_TRACKPICKUPGEOLOCATION, oSettings.getTrackPickupGeoLocation());
                    joParams.put(oSettings.SETTINGS_COLUMN_TRACKROUTEGEOLOCATION, oSettings.getTrackRouteGeoLocation());
                    joParams.put(oSettings.SETTINGS_COLUMN_DEBUG, oSettings.getDebug());
                    joParams.put(oSettings.SETTINGS_COLUMN_DOWNLOADNOTCOMPLETEDDATA, oSettings.getDownloadNotCompletedData());
                    joParams.put(oSettings.SETTINGS_COLUMN_AUTODBBACKUP, oSettings.getAutoDBBackup());
                    joParams.put(oSettings.SETTINGS_COLUMN_LASTUSERLOGINID, oSettings.getLastUserLoginID());
                    joParams.put(oSettings.SETTINGS_COLUMN_LASTUSERLOGINDATE, _oUtils.getFormattedDateString(getApplicationContext(), "N/A", oSettings.getLastUserLoginDate()));
                    joParams.put(oSettings.SETTINGS_COLUMN_LASTMILKRECEIPTID, oSettings.getLastMilkReceiptID());
                    joParams.put(oSettings.SETTINGS_COLUMN_SCANLOOP, oSettings.getScanLoop());
                    joParams.put(oSettings.SETTINGS_COLUMN_LASTSETTINGSUPDATE, _oUtils.getFormattedDateString(getApplicationContext(), "N/A", oSettings.getLastSettingsUpdate()));
                    joParams.put(oSettings.SETTINGS_COLUMN_LASTPROFILEUPDATE, _oUtils.getFormattedDateString(getApplicationContext(), "N/A", oSettings.getLastProfileUpdate()));
                    joParams.put(oSettings.SETTINGS_COLUMN_UPDATEAVAILABLE, oSettings.getUpdateAvailable());
                    joParams.put(oSettings.SETTINGS_COLUMN_UPDATEAVAILABLEDATE, _oUtils.getFormattedDateString(getApplicationContext(), "N/A", oSettings.getUpdateAvailableDate()));
                    joParams.put(oSettings.SETTINGS_COLUMN_DRUGTESTDEVICE, oSettings.getDrugTestDevice());
                    joParams.put(oSettings.SETTINGS_COLUMN_WEBSERVICEURL, oSettings.getWebServiceURL());
                    joParams.put(oSettings.SETTINGS_COLUMN_INSERTDATE, _oUtils.getFormattedDateString(getApplicationContext(), "N/A", oSettings.getInsertDate()));
                    joParams.put(oSettings.SETTINGS_COLUMN_MODIFIEDDATE, _oUtils.getFormattedDateString(getApplicationContext(), "N/A", oSettings.getModifiedDate()));

                    //Add the JSON object to the array of JSON objects
                    jaParams.put(joParams);

                    //Post the header data to the web service
                    sResult = oService.postJSONData(psURL[0], jaParams);
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

            _signin_progressbar.setProgress(100);
            _signin_progresslabel.setText("Upload: Settings (100%)");
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

            _signin_progressbar.setProgress(33);
            _signin_progresslabel.setText("Download: Settings (33%)");
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

            _signin_progressbar.setProgress(66);
            _signin_progresslabel.setText("Download: Profiles (66%)");
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

            _signin_progressbar.setProgress(100);
            _signin_progresslabel.setText("Download: Plants (100%)");
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
            List<dbHeader> olHeaderWaitingScaleData;

            try
            {
                //Instantiate the database handler
                dbDatabaseHandler oDBHandler = new dbDatabaseHandler(getApplicationContext(), null);

                olHeader = oDBHandler.findHeadersNonTransmitted();
                olHeaderWaitingScaleData = oDBHandler.findHeadersNonTransmittedWaitingOnScale();

                //Check if the header array is null and the headerwaitingscaledata is not null
                if (olHeader == null && olHeaderWaitingScaleData != null)
                {
                    //Instantiate the header array
                    olHeader = new ArrayList<>();

                    //Add the waitingscaledata list to the header list
                    olHeader.addAll(olHeaderWaitingScaleData);   
                }
                else if (olHeader != null && olHeaderWaitingScaleData != null)
                {
                    //Add the waitingscaledata list to the header list
                    olHeader.addAll(olHeaderWaitingScaleData);
                }

                //Check if the header array is null or empty
                if (olHeader == null || olHeader.isEmpty())
                {
                    //Return 0
                    sResult = "0";
                }
                else
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
                        joParams.put(oHeader.HEADER_COLUMN_INSERTDATE, _oUtils.getFormattedDateString(getApplicationContext(), "N/A", oHeader.getInsertDate()));
                        joParams.put(oHeader.HEADER_COLUMN_MODIFIEDDATE, _oUtils.getFormattedDateString(getApplicationContext(), "N/A", oHeader.getModifiedDate()));

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
                            oHeader.setTransmittedDate(Calendar.getInstance().getTime());

                            //Update the header record
                            oDBHandler.updateHeader(oHeader);
                        }
                    }
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

            _signin_progressbar.setProgress(25);
            _signin_progresslabel.setText("Upload: Headers (25%)");
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
            List<dbLine> olLineWaitingScaleData;

            try
            {
                //Instantiate the database handler
                dbDatabaseHandler oDBHandler = new dbDatabaseHandler(getApplicationContext(), null);

                //Get the lines that are non-transmitted and waiting on scale data
                olLine = oDBHandler.findLinesNonTransmitted();
                olLineWaitingScaleData = oDBHandler.findLinesNonTransmittedWaitingOnScale();

                //Check if the line array is null and the linewaitingscaledata is not null
                if (olLine == null && olLineWaitingScaleData != null)
                {
                    //Instantiate the line array
                    olLine = new ArrayList<>();

                    //Add the waitingscaledata list to the line list
                    olLine.addAll(olLineWaitingScaleData);
                }
                else if (olLine != null && olLineWaitingScaleData != null)
                {
                    //Add the waitingscaledata list to the line list
                    olLine.addAll(olLineWaitingScaleData);
                }

                //Check if line array is null or empty
                if (olLine == null || olLine.isEmpty())
                {
                    //Return 0
                    sResult = "0";
                }
                else
                {
                    //Instantiate the JSON Array
                    jaParams = new JSONArray();

                    //Loop through all line records in the list
                    for (int i = 0; i < olLine.size(); i++)
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
                        joParams.put(oLine.LINE_COLUMN_COMPANY, _oUtils.checkNullString(oLine.getCompany()));
                        joParams.put(oLine.LINE_COLUMN_DIVISION, _oUtils.checkNullString(oLine.getDivision()));
                        joParams.put(oLine.LINE_COLUMN_TYPE, _oUtils.checkNullString(oLine.getType()));
                        joParams.put(oLine.LINE_COLUMN_GAUGERODMAJOR, oLine.getGaugeRodMajor());
                        joParams.put(oLine.LINE_COLUMN_GAUGERODMINOR, oLine.getGaugeRodMinor());
                        joParams.put(oLine.LINE_COLUMN_CONVERTEDLBS, oLine.getConvertedLBS());
                        joParams.put(oLine.LINE_COLUMN_TEMPERATURE, oLine.getTemperature());
                        joParams.put(oLine.LINE_COLUMN_PICKUPDATE, _oUtils.getFormattedDateString(getApplicationContext(), "N/A", oLine.getPickupDate()));
                        joParams.put(oLine.LINE_COLUMN_DFATICKET, oLine.getDFATicket());
                        joParams.put(oLine.LINE_COLUMN_LABCODE, oLine.getLabCode());
                        joParams.put(oLine.LINE_COLUMN_LATITUDE, oLine.getLatitude());
                        joParams.put(oLine.LINE_COLUMN_LONGITUDE, oLine.getLongitude());
                        joParams.put(oLine.LINE_COLUMN_ACCURRACY, oLine.getAccurracy());
                        joParams.put(oLine.LINE_COLUMN_INSERTDATE, _oUtils.getFormattedDateString(getApplicationContext(), "N/A", oLine.getInsertDate()));
                        joParams.put(oLine.LINE_COLUMN_MODIFIEDDATE, _oUtils.getFormattedDateString(getApplicationContext(), "N/A", oLine.getModifiedDate()));

                        //Add the JSON object to the array of JSON objects
                        jaParams.put(joParams);
                    }

                    //Post the line data to the web service
                    sResult = oService.postJSONData(psURL[0], jaParams);

                    //Check if the result of posted line records is the same as what was sent
                    if (Integer.parseInt(sResult) == olLine.size())
                    {
                        //Loop through all line records in the list
                        for (int j = 0; j < olLine.size(); j++)
                        {
                            //Get the current line record
                            oLine = olLine.get(j);

                            //Update the transmitted fields
                            oLine.setTransmitted(1);
                            oLine.setTransmittedDate(Calendar.getInstance().getTime());

                            //Update the line record
                            oDBHandler.updateLine(oLine);
                        }
                    }
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

            _signin_progressbar.setProgress(50);
            _signin_progresslabel.setText("Upload: Lines (50%)");
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
            List<dbReceive> olReceiveWaitingScaleData;

            try
            {
                //Instantiate the database handler
                dbDatabaseHandler oDBHandler = new dbDatabaseHandler(getApplicationContext(), null);

                //Get the receives non-transmitted and waiting on scale data
                olReceive = oDBHandler.findReceivesNonTransmitted();
                olReceiveWaitingScaleData = oDBHandler.findReceivesNonTransmittedWaitingForScale();
                
                //Check if the receive array is null and the receivewaitingscaledata is not null
                if (olReceive == null && olReceiveWaitingScaleData != null)
                {
                    //Instantiate the receive array
                    olReceive = new ArrayList<>();

                    //Add the waitingscaledata list to the receive list
                    olReceive.addAll(olReceiveWaitingScaleData);
                }
                else if (olReceive != null && olReceiveWaitingScaleData != null)
                {
                    //Add the waitingscaledata list to the receive list
                    olReceive.addAll(olReceiveWaitingScaleData);
                }

                //Check if the receives array is null or empty
                if (olReceive == null || olReceive.isEmpty())
                {
                    //Return 0
                    sResult = "0";
                }
                else
                {
                    //Instantiate the JSON Array
                    jaParams = new JSONArray();

                    //Loop through all receive records in the list
                    for (int i = 0; i < olReceive.size(); i++)
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
                        joParams.put(oReceive.RECEIVE_COLUMN_FKPLANTORIGINALID, "00000000-0000-0000-0000-000000000000");
                        joParams.put(oReceive.RECEIVE_COLUMN_DRUGTESTDEVICE, oReceive.getDrugTestDevice());
                        joParams.put(oReceive.RECEIVE_COLUMN_DRUGTESTRESULT, oReceive.getDrugTestResult());
                        joParams.put(oReceive.RECEIVE_COLUMN_RECEIVEDATETIME, _oUtils.getFormattedDateString(getApplicationContext(), "N/A", oReceive.getReceiveDateTime()));
                        joParams.put(oReceive.RECEIVE_COLUMN_TANK, oReceive.getTank());
                        joParams.put(oReceive.RECEIVE_COLUMN_SCALEMETER, oReceive.getScaleMeter());
                        joParams.put(oReceive.RECEIVE_COLUMN_TOPSEAL, oReceive.getTopSeal());
                        joParams.put(oReceive.RECEIVE_COLUMN_BOTTOMSEAL, oReceive.getBottomSeal());
                        joParams.put(oReceive.RECEIVE_COLUMN_RECEIVEDLBS, oReceive.getReceivedLBS());
                        joParams.put(oReceive.RECEIVE_COLUMN_LOADTEMP, oReceive.getLoadTemp());
                        joParams.put(oReceive.RECEIVE_COLUMN_INTAKENUMBER, oReceive.getIntakeNumber());
                        joParams.put(oReceive.RECEIVE_COLUMN_INSERTDATE, _oUtils.getFormattedDateString(getApplicationContext(), "N/A", oReceive.getInsertDate()));
                        joParams.put(oReceive.RECEIVE_COLUMN_MODIFIEDDATE, _oUtils.getFormattedDateString(getApplicationContext(), "N/A", oReceive.getModifiedDate()));

                        //Add the JSON object to the array of JSON objects
                        jaParams.put(joParams);
                    }

                    //Post the receive data to the web service
                    sResult = oService.postJSONData(psURL[0], jaParams);

                    //Check if the result of posted receive records is the same as what was sent
                    if (Integer.parseInt(sResult) == olReceive.size())
                    {
                        //Loop through all receive records in the list
                        for (int j = 0; j < olReceive.size(); j++)
                        {
                            //Get the current receive record
                            oReceive = olReceive.get(j);

                            //Update the transmitted fields
                            oReceive.setTransmitted(1);
                            oReceive.setTransmittedDate(Calendar.getInstance().getTime());

                            //Update the line record
                            oDBHandler.updateReceive(oReceive);
                        }
                    }
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

            _signin_progressbar.setProgress(75);
            _signin_progresslabel.setText("Upload: Receives (75%)");
        }
    }
    //endregion

    //region Class PostActivity Background Task
    private class PostActivity extends AsyncTask<String, Void, String>
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
            dbActivityHeader oActivity;
            List<dbActivityHeader> olActivity;

            try
            {
                //Instantiate the database handler
                dbDatabaseHandler oDBHandler = new dbDatabaseHandler(getApplicationContext(), null);

                olActivity = oDBHandler.findActivityNonTransmitted(getApplicationContext(), "N/A");

                if (olActivity != null)
                {
                    if (!olActivity.isEmpty())
                    {
                        //Instantiate the JSON Array
                        jaParams = new JSONArray();

                        //Loop through all receive records in the list
                        for (int i=0; i<olActivity.size(); i++)
                        {
                            //Get the current receive record
                            oActivity = olActivity.get(i);

                            //Instantiate a new JSON object
                            joParams = new JSONObject();

                            //Fill the JSON object with data
                            joParams.put(oActivity.ACTIVITYHEADER_COLUMN_FKACTIVITYTYPEID, oActivity.getFkActivityTypeID());
                            joParams.put("fkSettingsID", _spkSettingsID);
                            joParams.put(oActivity.ACTIVITYHEADER_COLUMN_APPLICATION, oActivity.getApplication());
                            joParams.put(oActivity.ACTIVITYHEADER_COLUMN_MODULE, oActivity.getModule());
                            joParams.put(oActivity.ACTIVITYHEADER_COLUMN_ROUTINE, oActivity.getRoutine());
                            joParams.put(oActivity.ACTIVITYHEADER_COLUMN_USERNAME, oActivity.getUsername());
                            joParams.put(oActivity.ACTIVITYHEADER_COLUMN_MESSAGE, oActivity.getMessage());
                            joParams.put(oActivity.ACTIVITYHEADER_COLUMN_STACKTRACE, oActivity.getStackTrace());
                            joParams.put(oActivity.ACTIVITYHEADER_COLUMN_INSERTDATE, _oUtils.getFormattedDateString(getApplicationContext(), "N/A", oActivity.getInsertDate()));

                            //Add the JSON object to the array of JSON objects
                            jaParams.put(joParams);
                        }

                        //Post the receive data to the web service
                        sResult = oService.postJSONData(psURL[0], jaParams);

                        //Check if the result of posted receive records is the same as what was sent
                        if (Integer.parseInt(sResult) == olActivity.size())
                        {
                            //Loop through all receive records in the list
                            for (int j=0; j<olActivity.size(); j++)
                            {
                                //Get the current receive record
                                oActivity = olActivity.get(j);

                                //Update the transmitted fields
                                oActivity.setTransmitted(1);
                                oActivity.setTransmittedDate(Calendar.getInstance().getTime());

                                //Update the line record
                                oDBHandler.updateActivity(oActivity);
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

            _signin_progressbar.setProgress(100);
            _signin_progresslabel.setText("Upload: Activity (100%)");

            _signin_progresslabel.setText("End of data syncronization (100%)");

            //Sync finished enable the signin button
            _signin_login_button.setEnabled(true);
        }
    }
    //endregion

    //region Class Header Ticket Number Background Task
    private class GetHeaderTicketNumbers extends AsyncTask<String, Void, List<dbHeader>>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected List<dbHeader> doInBackground(String... psURL)
        {
            svcMilkReceipt oService = new svcMilkReceipt();
            JSONObject joParams;
            JSONArray jaParams;
            dbHeader oHeader;
            List<dbHeader> olHeader = new ArrayList<>();
            String sResult;

            try
            {
                //Instantiate the database handler
                dbDatabaseHandler oDBHandler = new dbDatabaseHandler(getApplicationContext(), null);

                //Get array of header records not finished, waiting on scale data
                olHeader = oDBHandler.findHeadersNonFinished();

                //Check if the header array is null
                if (olHeader != null)
                {
                    //Check if the header array is empty
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
                            joParams.put(oHeader.HEADER_COLUMN_INSERTDATE, _oUtils.getFormattedDateString(getApplicationContext(), "N/A", oHeader.getInsertDate()));
                            joParams.put(oHeader.HEADER_COLUMN_MODIFIEDDATE, _oUtils.getFormattedDateString(getApplicationContext(), "N/A", oHeader.getModifiedDate()));

                            //Add the JSON object to the array of JSON objects
                            jaParams.put(joParams);
                        }

                        //Get the settings record from the web service
                        sResult = oService.postJSONData(psURL[0], jaParams);

                        //Parse the header records and create the header object array
                        olHeader = oService.ParseHeader(sResult);
                    }
                }
            }
            catch(Exception ex)
            {
                Toast.makeText(SignInActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            //Return the header object array
            return olHeader;
        }

        @Override
        protected void onPostExecute(List<dbHeader> polHeader)
        {
            super.onPostExecute(polHeader);

            //Check if the header list is null
            if(polHeader != null)
            {
                //Instantiate the database handler
                dbDatabaseHandler oDBHandler = new dbDatabaseHandler(getApplicationContext(), null);

                //Setup the header and headerfound objects
                dbHeader oHeader, oHeaderFound;

                //Loop through all header records in the list
                for (int i = 0; i < polHeader.size(); i++)
                {
                    //Get the header object in list
                    oHeader = polHeader.get(i);

                    //Get the header record from the database
                    oHeaderFound = oDBHandler.findHeaderByID(oHeader.getPkHeaderID());

                    //Check if the header record was found
                    if (oHeaderFound != null)
                    {
                        //Set the ticket number from the web service
                        oHeaderFound.setTicketNumber(oHeader.getTicketNumber());

                        //Update the settings record
                        oDBHandler.updateHeader(oHeaderFound);
                    }
                }
            }

            _signin_progressbar.setProgress(100);
            _signin_progresslabel.setText("Download: Header Ticket Numbers (100%)");
        }
    }
    //endregion
}
