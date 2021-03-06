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

import static android.view.View.INVISIBLE;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button _settings_ButtonEditCancel, _settings_ButtonSave, _settings_ButtonCleanDB;
    private TextView _settings_SettingsID, _settings_TabletName, _settings_MachineID, _settings_LastMilkReceiptID, _settings_LastUserLogin, _settings_LastUserLoginDate, _settings_LastProfileUploadDate, _settings_CreatedDate, _settings_ModifiedDate;
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
        _settings_ButtonEditCancel = findViewById(R.id.settings_buttoneditcancel);
        _settings_ButtonSave = findViewById(R.id.settings_buttonsave);
        _settings_ButtonCleanDB = findViewById(R.id.settings_cleandb_button);

        //Instantiate the on screen text views
        _settings_SettingsID = findViewById(R.id.settings_settingsid);
        _settings_TabletName = findViewById(R.id.settings_tabletname);
        _settings_MachineID = findViewById(R.id.settings_machineid);
        _settings_LastMilkReceiptID = findViewById(R.id.settings_lastmilkreceiptid);
        _settings_LastUserLogin = findViewById(R.id.settings_lastuserlogin);
        _settings_LastUserLoginDate = findViewById(R.id.settings_lastuserlogindate);
        _settings_LastProfileUploadDate = findViewById(R.id.settings_lastprofileuploaddate);
        _settings_CreatedDate = findViewById(R.id.settings_createddate);
        _settings_ModifiedDate = findViewById(R.id.settings_modifieddate);

        //Instantiate the on screen edit texts
        _settings_WebServiceURL = findViewById(R.id.settings_webserviceurl);
        _settings_DrugTestDevice = findViewById(R.id.settings_drugtestdevice);
        _settings_ScanLoop = findViewById(R.id.settings_scanloop);

        //Instantiate the on screen switches
        _settings_TrackPickupGeoLocation = findViewById(R.id.settings_trackpickupgeolocation);
        _settings_TrackRouteGeoLocation = findViewById(R.id.settings_trackroutegeolocation);
        _settings_EnableDebug = findViewById(R.id.settings_enabledebug);
        _settings_EnableAutoDBBackup = findViewById(R.id.settings_enableautodbbackup);
        _settings_DownloadNotCompletedData = findViewById(R.id.settings_downloadnotcompleteddata);

        //Hide the TrackRouteGeoLocation
        _settings_TrackRouteGeoLocation.setVisibility(INVISIBLE);

        //Set the on click listener for page to the screen buttons
        _settings_ButtonEditCancel.setOnClickListener(this);
        _settings_ButtonSave.setOnClickListener(this);
        _settings_ButtonCleanDB.setOnClickListener(this);

        //Setup the bundle object
        Bundle oBundle = getIntent().getExtras();

        //Get the settings and profile id's from the bundle
        _spkSettingsID = oBundle.getString("pkSettingsID");
        _spkProfileID = oBundle.getString("pkProfileID");

        //Check if the profile id is null
        if (_spkProfileID != null)
        {
            //Get the username from database
            _sUsername = _oUtils.findUsernameByID(this, _spkProfileID);
        }

        //Check if the settings id was not passed from receipt page
        if (_spkSettingsID == null || _spkSettingsID.length() < 1)
        {
            //Get the settings id from the database
            _spkSettingsID = _oUtils.findSettings(this, _sUsername, android.os.Build.SERIAL);
        }

        //Setup the screen
        setupScreen();

        //_settings_EnableDebug.setChecked(true);
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

                    //Instantiate the bundle object
                    Bundle oActBundle = new Bundle();

                    //Set the profileID and settingsID in the bundle
                    oActBundle.putString("pkProfileID", _spkProfileID);
                    oActBundle.putString("pkSettingsID", _spkSettingsID);

                    //Setup bundle into intent
                    activity_intent.putExtras(oActBundle);

                    //Navigate to the activity log screen
                    startActivity(activity_intent);

                    //Log message to activity
                    _oUtils.insertActivity(this, "1", "ReceiptActivity", "onOptionsItemSelected", _sUsername, "Settings menu activity log selected", "");

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Logout item selected
                case R.id.menu_settings_logout:
                    //Instantiate a new intent of ReceiptActivity
                    Intent receipt_intent = new Intent(this, ReceiptActivity.class);

                    //Instantiate the bundle object
                    Bundle oBundle = new Bundle();

                    //Set the profileID and settingsID in the bundle
                    oBundle.putString("pkProfileID", _spkProfileID);
                    oBundle.putString("pkSettingsID", _spkSettingsID);

                    //Setup bundle into intent
                    receipt_intent.putExtras(oBundle);

                    //Navigate to the receipt screen
                    startActivity(receipt_intent);

                    //Log message to activity
                    _oUtils.insertActivity(this, "1", "SettingsActivity", "onOptionsItemSelected", _sUsername, "Settings menu back to Receipt selected", "");

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
            _oUtils.insertActivity(this, "3", "SettingsActivity", "onOptionsItemSelected", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
                _oUtils.insertActivity(this, "1", "SettingsActivity", "onClick", _sUsername, "Settings edit cancel button pressed", "");

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
                _oUtils.insertActivity(this, "1", "SettingsActivity", "onClick", _sUsername, "Settings save button pressed", "");

                //Change text on edit cancel button to "Edit"
                _settings_ButtonEditCancel.setText("Edit");

                //Disable the save button
                _settings_ButtonSave.setEnabled(false);

                //Save the settings record
                saveSettings();

                //Disable all on screen controls
                disableControls();
            }
            else if (v.getId() == R.id.settings_cleandb_button)
            {
                //Log message to activity
                _oUtils.insertActivity(this, "1", "SettingsActivity", "onClick", _sUsername, "Settings clean db button pressed", "");

                //Clean DB
                CleanDB();
            }
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "SettingsActivity", "onClick", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }
    //endregion

    //region Routines
    /**
     * setupScreen
     * - Setup the screen
     */
    private void setupScreen()
    {
        dbSettings oSettings;

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
                _settings_TrackPickupGeoLocation.setChecked(oSettings.getTrackPickupGeoLocation() == 1 ? true : false);
                _settings_TrackRouteGeoLocation.setChecked(oSettings.getTrackRouteGeoLocation() == 1 ? true : false);
                _settings_EnableDebug.setChecked(oSettings.getDebug() == 1 ? true : false);
                _settings_EnableAutoDBBackup.setChecked(oSettings.getAutoDBBackup() == 1 ? true : false);
                _settings_DownloadNotCompletedData.setChecked(oSettings.getDownloadNotCompletedData() == 1 ? true : false);
                _settings_LastUserLogin.setText("Last User Login: " + oSettings.getLastUserLoginID());
                _settings_LastUserLoginDate.setText("Last User Login Date: " + oSettings.getLastUserLoginDate());
                _settings_LastProfileUploadDate.setText("Last Profile Upload Date: " + oSettings.getLastProfileUpdate());
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
            _oUtils.insertActivity(this, "3", "SettingsActivity", "setupScreen", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
            _oUtils.insertActivity(this, "3", "SettingsActivity", "disableControls", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
            _oUtils.insertActivity(this, "3", "SettingsActivity", "enableControls", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }

    private void saveSettings()
    {
        dbSettings oSettings;

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

                //Set the modified date
                oSettings.setModifiedDate(_oUtils.getFormattedDate(this, _sUsername));

                //Update the settings record in the database
                oDBHandler.updateSettings(oSettings);
            }

        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "SettingsActivity", "saveSettings", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }

    /**
     * CleanDB
     *  - Clean up the user created data in database
     */
    private void CleanDB()
    {
        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Delete all user created data from database
            oDBHandler.deleteHeaderAll();
            oDBHandler.deleteLineAll();
            oDBHandler.deleteReceiveAll();
            oDBHandler.deleteActivityAll();
            //oDBHandler.deleteSettingsAll();
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "SettingsActivity", "CleanDB", "N/A", ex.getMessage(), ex.getStackTrace().toString());
        }
    }
    //endregion
}
