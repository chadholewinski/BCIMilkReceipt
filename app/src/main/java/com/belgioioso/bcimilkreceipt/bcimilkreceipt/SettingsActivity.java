package com.belgioioso.bcimilkreceipt.bcimilkreceipt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbDatabaseHandler;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbSettings;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button _settings_ButtonEditCancel, _settings_ButtonSave;
    private TextView _settings_SettingsID, _settings_TabletName, _settings_MachineID, _settings_LastMilkReceiptID, _settings_LastUserLogin, _settings_LastUserLoginDate, _settings_CreatedDate, _settings_ModifiedDate;
    private EditText _settings_WebServiceURL, _settings_DrugTestDevice, _settings_ScanLoop;
    private Switch _settings_TrackPickupGeoLocation, _settings_TrackRouteGeoLocation, _settings_EnableDebug, _settings_EnableAutoDBBackup, _settings_DownloadNotCompletedData;
    private String _spkSettingsID, _spkProfileID, _sUsername;
    private Utilities _oUtils;

    //region Class Constructor Methods
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Set the keyboard to not show automatically
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        _sUsername = "N/A";

        _oUtils = new Utilities();

        //Instantiate the on screen buttons
        _settings_ButtonEditCancel = (Button)findViewById(R.id.settings_buttoneditcancel);
        _settings_ButtonSave = (Button)findViewById(R.id.settings_buttonsave);

        //Instantiate the on screen text views
        _settings_SettingsID = (TextView)findViewById(R.id.settings_settingsid);
        _settings_TabletName = (TextView)findViewById(R.id.settings_tabletname);
        _settings_MachineID = (TextView)findViewById(R.id.settings_machineid);
        _settings_LastMilkReceiptID = (TextView)findViewById(R.id.settings_lastmilkreceiptid);
        _settings_LastUserLogin = (TextView)findViewById(R.id.settings_lastuserlogin);
        _settings_LastUserLoginDate = (TextView)findViewById(R.id.settings_lastuserlogindate);
        _settings_CreatedDate = (TextView)findViewById(R.id.settings_createddate);
        _settings_ModifiedDate = (TextView)findViewById(R.id.settings_modifieddate);

        //Instantiate the on screen edit texts
        _settings_WebServiceURL = (EditText)findViewById(R.id.settings_webserviceurl);
        _settings_DrugTestDevice = (EditText)findViewById(R.id.settings_drugtestdevice);
        _settings_ScanLoop = (EditText)findViewById(R.id.settings_scanloop);

        //Instantiate the on screen switches
        _settings_TrackPickupGeoLocation = (Switch)findViewById(R.id.settings_trackpickupgeolocation);
        _settings_TrackRouteGeoLocation = (Switch)findViewById(R.id.settings_trackroutegeolocation);
        _settings_EnableDebug = (Switch)findViewById(R.id.settings_enabledebug);
        _settings_EnableAutoDBBackup = (Switch)findViewById(R.id.settings_enableautodbbackup);
        _settings_DownloadNotCompletedData = (Switch)findViewById(R.id.settings_downloadnotcompleteddata);

        //Set the on click listener for page to the screen buttons
        _settings_ButtonEditCancel.setOnClickListener(this);
        _settings_ButtonSave.setOnClickListener(this);

        //Setup the bundle object
        Bundle oBundle = getIntent().getExtras();

        //Get the settings and profile id's from the bundle
        _spkSettingsID = oBundle.getString("pkSettingsID");
        _spkProfileID = oBundle.getString("pkProfileID");

        //Check if the settings id was not passed from receipt page
        if (_spkSettingsID == null || _spkSettingsID.length() < 1)
        {
            //Get the settings id from the database
            _spkSettingsID = findSettings(android.os.Build.SERIAL);
        }

        //Setup the screen
        setupScreen();
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

        //Set the menu to the milkreceipt settings menu
        inflater.inflate(R.menu.milkreceipt_settings_menu, menu);

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
        Toast.makeText(SettingsActivity.this, "Back button pressed: NOT ALLOWED!", Toast.LENGTH_SHORT).show();
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
                //Menu Activity item selected
                case R.id.menu_settings_activity:
                    //Instantiate a new intent of LogActivity
                    Intent activity_intent = new Intent(this, LogActivity.class);

                    //Navigate to the activity log screen
                    startActivity(activity_intent);

                    //Log message to activity
                    _oUtils.InsertActivity(this, "1", "SettingsActivity", "onOptionsItemSelected", _sUsername, "menu_settings_activity item selected", "");

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Logout item selected
                case R.id.menu_settings_logout:
                    //Instantiate a new intent of SignInActivity
                    Intent logout_intent = new Intent(this, SignInActivity.class);

                    //Navigate to the activity log screen
                    startActivity(logout_intent);

                    //Log message to activity
                    _oUtils.InsertActivity(this, "1", "SettingsActivity", "onOptionsItemSelected", _sUsername, "menu_settings_logout item selected", "");

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Default action
                default:
                    //Set the return value to true
                    bReturn = super.onOptionsItemSelected(item);

                    break;
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "SettingsActivity", "onOptionsItemSelected", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }

        //Return the value
        return bReturn;
    }

    /**
     * onClick
     *  - Handles the onClick event for the page
     * @param v
     */
    @Override
    public void onClick(View v)
    {
        try
        {
            //Check if the receive save button was pressed
            if (v.getId() == R.id.settings_buttoneditcancel)
            {
                //Log message to activity
                _oUtils.InsertActivity(this, "1", "SettingsActivity", "onClick", _sUsername, "settings_buttoneditcancel pressed", "");

                //Check if the edit/cancel button is set to edit
                if (_settings_ButtonEditCancel.getText() == "Edit")
                {
                    //Set the button to cancel
                    _settings_ButtonEditCancel.setText("Cancel");

                    //Enable the save button
                    _settings_ButtonSave.setEnabled(true);

                    //Enable all controls on screen
                    enableControls();
                }
                else
                {
                    //Reload the settings data from database
                    setupScreen();
                }
            }
            else if (v.getId() == R.id.settings_buttonsave)
            {
                //Log message to activity
                _oUtils.InsertActivity(this, "1", "SettingsActivity", "onClick", _sUsername, "settings_buttonsave pressed", "");

                //Change text on edit cancel button to "Edit"
                _settings_ButtonEditCancel.setText("Edit");

                //Disable the save button
                _settings_ButtonSave.setEnabled(false);

                //Save the settings record
                saveSettings();

                //Disable all on screen controls
                disableControls();
            }
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "SettingsActivity", "onClick", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
                oSettingsNew.setAutoDBBackup(0);
                oSettingsNew.setLastUserLoginID("");
                oSettingsNew.setLastUserLoginDate("1/1/1900");
                oSettingsNew.setLastMilkReceiptID("");
                oSettingsNew.setScanLoop(1);
                oSettingsNew.setLastSettingsUpdate("1/1/1900");
                oSettingsNew.setLastProfileUpdate("1/1/1900");
                oSettingsNew.setUpdateAvailable(0);
                oSettingsNew.setUpdateAvailableDate("1/1/1900");
                oSettingsNew.setDrugTestDevice("CharmSLRosa");
                oSettingsNew.setWebServiceURL("http://localweb.belgioioso.cheese.inc/MilkReceiptREST/MilkReceiptService.svc");

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

                //Log activity
                _oUtils.InsertActivity(this, "1", "ReceiveActivity", "findSettings", _sUsername, "Settings not found, new settings record saved", "");
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
            _oUtils.InsertActivity(this, "3", "SettingsActivity", "findSettings", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }

        //Return the settingsID
        return sReturnID;
    }

    /**
     * setupScreen
     * - Setup the screen
     */
    private void setupScreen()
    {
        dbSettings oSettings = new dbSettings();

        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the settings object from database
            oSettings = oDBHandler.findSettingsByID(_spkSettingsID);

            //Check if the settings record was found
            if (oSettings != null)
            {
                _settings_SettingsID.setText("Settings ID: " + oSettings.getPkSettingsID());
                _settings_TabletName.setText("Tablet Name: " + oSettings.getTabletName());
                _settings_MachineID.setText("Machine ID: " + oSettings.getMachineID());
                _settings_LastMilkReceiptID.setText("Last MilkReceipt ID: " + oSettings.getLastMilkReceiptID());
                _settings_WebServiceURL.setText(oSettings.getWebServiceURL());
                _settings_DrugTestDevice.setText(oSettings.getDrugTestDevice());
                _settings_ScanLoop.setText(oSettings.getScanLoop().toString());
                _settings_TrackPickupGeoLocation.setChecked(Boolean.getBoolean(oSettings.getTrackPickupGeoLocation().toString()));
                _settings_TrackRouteGeoLocation.setChecked(Boolean.getBoolean(oSettings.getTrackRouteGeoLocation().toString()));
                _settings_EnableDebug.setChecked(Boolean.getBoolean(oSettings.getDebug().toString()));
                _settings_EnableAutoDBBackup.setChecked(Boolean.getBoolean(oSettings.getAutoDBBackup().toString()));
                _settings_DownloadNotCompletedData.setChecked(Boolean.getBoolean(oSettings.getDownloadNotCompletedData().toString()));
                _settings_LastUserLogin.setText("Last User Login: " + oSettings.getLastUserLoginID());
                _settings_LastUserLoginDate.setText("Last User Login Date: " + oSettings.getLastUserLoginDate());
                _settings_CreatedDate.setText("Created Date: " + oSettings.getInsertDate());
                _settings_ModifiedDate.setText("Modified Date: " + oSettings.getModifiedDate());
            }

            //Disable the save button
            _settings_ButtonSave.setEnabled(false);
            _settings_ButtonEditCancel.setText("Edit");
            
            //Disable the controls on the screen
            disableControls();
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "SettingsActivity", "setupScreen", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }

    /**
     * disableControls
     *  - disable the controls on the screen
     */
    private void disableControls()
    {
        try
        {
            //Disable controls on screen
            _settings_WebServiceURL.setEnabled(false);
            _settings_DrugTestDevice.setEnabled(false);
            _settings_ScanLoop.setEnabled(false);
            _settings_TrackPickupGeoLocation.setEnabled(false);
            _settings_TrackRouteGeoLocation.setEnabled(false);
            _settings_EnableDebug.setEnabled(false);
            _settings_EnableAutoDBBackup.setEnabled(false);
            _settings_DownloadNotCompletedData.setEnabled(false);
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "SettingsActivity", "disableControls", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }

    /**
     * enableControls
     *  - enable the controls on the screen
     */
    private void enableControls()
    {
        try
        {
            //Enable controls on screen
            _settings_WebServiceURL.setEnabled(true);
            _settings_DrugTestDevice.setEnabled(true);
            _settings_ScanLoop.setEnabled(true);
            _settings_TrackPickupGeoLocation.setEnabled(true);
            _settings_TrackRouteGeoLocation.setEnabled(true);
            _settings_EnableDebug.setEnabled(true);
            _settings_EnableAutoDBBackup.setEnabled(true);
            _settings_DownloadNotCompletedData.setEnabled(true);
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "SettingsActivity", "enableControls", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }

    private void saveSettings()
    {
        dbSettings oSettings = new dbSettings();

        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the current settings from the database
            oSettings = oDBHandler.findSettingsByID(_spkSettingsID);

            //Check if the settings were found from the database
            if (oSettings != null)
            {
                //Set the fields based on what was entered onto the screen
                oSettings.setWebServiceURL(_settings_WebServiceURL.getText().toString());
                oSettings.setDrugTestDevice(_settings_DrugTestDevice.getText().toString());
                oSettings.setScanLoop(Integer.parseInt(_settings_ScanLoop.getText().toString()));
                oSettings.setTrackPickupGeoLocation(_settings_TrackPickupGeoLocation.isChecked() == true ? 1 : 0);
                oSettings.setTrackRouteGeoLocation(_settings_TrackRouteGeoLocation.isChecked() == true ? 1 : 0);
                oSettings.setDebug(_settings_EnableDebug.isChecked() == true ? 1 : 0);
                oSettings.setAutoDBBackup(_settings_EnableAutoDBBackup.isChecked() == true ? 1 : 0);
                oSettings.setDownloadNotCompletedData(_settings_DownloadNotCompletedData.isChecked() == true ? 1 : 0);

                //Format the date for insert and modified
                DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                Date dDate = new Date();

                //Set the modified date
                oSettings.setModifiedDate(dfDate.format(dDate).toString());

                //Update the settings record in the database
                oDBHandler.updateSettings(oSettings);
            }

        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "SettingsActivity", "saveSettings", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }
    //endregion
}
