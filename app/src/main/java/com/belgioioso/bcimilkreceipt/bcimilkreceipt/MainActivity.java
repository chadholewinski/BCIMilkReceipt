package com.belgioioso.bcimilkreceipt.bcimilkreceipt;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbHeader;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.belgioiososvc.bcimilkreceipt.bcimilkreceiptsvc.svcMilkReceipt;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbProfile;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbSettings;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbDatabaseHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    private Button _main_savereceipt_button;
    private TextView main_Bottom_Message;
    private EditText _route, _license, _startmileage;
    private String _spkSettingsID, _spkProfileID, _spkHeaderID, _sUsername;
    private Utilities _oUtils;
    private dbProfile _oProfile;

    //region Class Constructor Methods
    /**
     * onCreate
     *  - Constructor
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the keyboard to not show automatically
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        _sUsername = "N/A";
        
        _oUtils = new Utilities();

        //Instantiate the save receipt and goto pickup buttons
        _main_savereceipt_button = (Button)findViewById(R.id.main_savereceipt_button);

        //Instantiate the main bottom message text view
        main_Bottom_Message = (TextView)findViewById(R.id.main_bottom_message);

        //Instantiate the route, license and startmileage edit text fields
        _route = (EditText)findViewById(R.id.main_route);
        _license = (EditText)findViewById(R.id.main_license);
        _startmileage = (EditText)findViewById(R.id.main_startmileage);

        //Set the on click listener for page to the save receipt and goto pickup buttons
        _main_savereceipt_button.setOnClickListener(this);

        //Setup the bundle object
        Bundle oBundle = getIntent().getExtras();

        //Get the header, settings and profile id's from the bundle
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

        //Setup the screen for initial use
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

        //Set the menu to the milkreceipt main menu
        inflater.inflate(R.menu.milkreceipt_main_menu, menu);

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
        Toast.makeText(MainActivity.this, "Back button pressed: NOT ALLOWED!", Toast.LENGTH_SHORT).show();
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
                case R.id.menu_main_activity:
                    //Instantiate a new intent of LogActivity
                    Intent activity_intent = new Intent(this, LogActivity.class);

                    //Navigate to the activity log screen
                    startActivity(activity_intent);

                    //Log message to activity
                    _oUtils.insertActivity(this, "1", "MainActivity", "onOptionsItemSelected", _sUsername, "Main menu activity log selected", "");

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Logout item selected
                case R.id.menu_main_logout:
                    //Instantiate a new intent of SignInActivity
                    Intent logout_intent = new Intent(this, SignInActivity.class);

                    //Navigate to the activity log screen
                    startActivity(logout_intent);

                    //Log message to activity
                    _oUtils.insertActivity(this, "1", "MainActivity", "onOptionsItemSelected", _sUsername, "Main menu back to SignIn selected", "");

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
            _oUtils.insertActivity(this, "3", "MainActivity", "onOptionsItemSelected", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
        try
        {
            //Lock the user inputs
            lockUserInputs();

            //Save the new header record
            _spkHeaderID = saveNewHeader();

            //Instantiate a new settings object
            dbSettings oSettings = new dbSettings();

            //Instantiate the database handler object
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the settings object from database
            oSettings = oDBHandler.findSettingsByID(_spkSettingsID);

            //Check if the auto db backup flag is set
            if (oSettings.getAutoDBBackup() == 1)
            {
                //Backup the database
                _oUtils.copyDBFile(this, _sUsername);
            }

            //Check if the save receipt button was pressed
            if (v.getId() == R.id.main_savereceipt_button)
            {
                //Log message to activity
                _oUtils.insertActivity(this, "1", "MainActivity", "onClick", _sUsername, "Main save new receipt button pressed", "");

                //Check if the headerID was retrieved
                if (_spkHeaderID != null)
                {
                    //Log message to activity
                    _oUtils.insertActivity(this, "1", "MainActivity", "onClick", _sUsername, "New Ticket Created ID: " + _spkHeaderID, "");

                    //Update the settings record
                    updateSettingsWithLastReceiptID(_spkHeaderID);

                    //Instantiate a new intent of Pickup Activity
                    Intent gotopickup_intent_new = new Intent(this, PickupActivity.class);

                    //Instantiate the bundle object
                    Bundle oBundle_new = new Bundle();

                    //Set the headerID, profileID and settingsID in the bundle
                    oBundle_new.putString("pkHeaderID", _spkHeaderID);
                    oBundle_new.putString("pkProfileID", _spkProfileID);
                    oBundle_new.putString("pkSettingsID", _spkSettingsID);

                    //Setup bundle into intent
                    gotopickup_intent_new.putExtras(oBundle_new);

                    //Navigate to the Pickup Activity page
                    startActivity(gotopickup_intent_new);
                }
                else
                {
                    //Log message to activity
                    _oUtils.insertActivity(this, "1", "MainActivity", "onClick", _sUsername, "New receipt save unsuccessful, no ID created", "");
                }
            }
        }
        catch (Exception ex)
        {
            //Unlock the user inputs
            unlockUserInputs();

            //Log error message to activity
            _oUtils.insertActivity(this, "3", "MainActivity", "onClick", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }
    //endregion

    //region Routines
    /**
     * setupScreen
     * - Setup the bottom message on the screen and other screen intitializations
     */
    private void setupScreen()
    {
        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the profile object from database
            _oProfile = oDBHandler.findProfileByID(_spkProfileID);

            //Check if the profile record was found
            if (_oProfile != null)
            {
                //Format the date for insert and modified
                DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                Date dDate = new Date();

                //Set text for the receipt bottom message text view
                main_Bottom_Message.setText("Current User: " + _oProfile.getFullName() + " - Current Receipt Date: " + dfDate.format(dDate).toString());
                
                //Set the global username variable
                _sUsername = _oProfile.getUsername();
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "MainActivity", "setupScreen", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }

    /**
     * saveNewHeader
     * - Saves a new header record to database and returns the headerID
     * @return
     */
    private String saveNewHeader()
    {
        String sHeaderID = null;
        dbHeader oHeader = new dbHeader();
        int iStartMileage = 0;

        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Create a new headerID GUID
            UUID gID = UUID.randomUUID();

            //Setup the new header object data
            oHeader.setPkHeaderID(gID.toString());
            oHeader.setFkProfileID(_spkProfileID);
            oHeader.setTicketNumber("");
            oHeader.setRouteIdentifier(_route.getText().toString());
            oHeader.setTruckLicenseNumber(_license.getText().toString());

            //Check if the start mileage value was set
            try
            {
                //Parse the start mileage field
                iStartMileage = Integer.parseInt(_startmileage.getText().toString());
            }
            catch(NumberFormatException nfe)
            {
                //Set the start mileage value to 0
                iStartMileage = 0;
            }

            //Continue setup of new header object data
            oHeader.setStartMileage(iStartMileage);
            oHeader.setEndMileage(0);
            oHeader.setTotalMileage(0);
            oHeader.setFinished(0);
            oHeader.setWaitingForScaleData(0);
            oHeader.setTransmitted(0);

            //Set the transmitted, insert and modified date fields
            oHeader.setTransmittedDate(_oUtils.getFormattedDate(this, _sUsername, "1900-01-01 00:00:00.000"));
            oHeader.setInsertDate(_oUtils.getFormattedDate(this, _sUsername));
            oHeader.setModifiedDate(_oUtils.getFormattedDate(this, _sUsername));

            //Add the header to the database
            oDBHandler.addHeader(oHeader);

            //Set the return headerID
            sHeaderID = gID.toString();
        }
        catch (Exception ex)
        {
            //Unlock the user inputs
            unlockUserInputs();

            //Log error message to activity
            _oUtils.insertActivity(this, "3", "MainActivity", "saveNewHeader", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());

            //Set the headerid to null
            sHeaderID = null;
        }

        //Return the headerID
        return sHeaderID;
    }

    /**
     * updateSettingsWithLastReceiptID
     *  - update the settings record with last milk receipt id
     * @param psReceiptID
     */
    private void updateSettingsWithLastReceiptID(String psReceiptID)
    {
        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the current settings record from database
            dbSettings oSettings = oDBHandler.findSettingsByID(_spkSettingsID);

            //Update the settings information
            oSettings.setLastMilkReceiptID(psReceiptID);
            oSettings.setModifiedDate(_oUtils.getFormattedDate(this, _sUsername));

            //Update the record in the database
            oDBHandler.updateSettings(oSettings);
        }
        catch(Exception ex)
        {
            //Unlock the user inputs
            unlockUserInputs();

            //Log error message to activity
            _oUtils.insertActivity(this, "3", "MainActivity", "updateSettingsWithLastReceiptID", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }

    /**
     * lockUserInputs
     *  - lock out the user inputs on the screen
     */
    private void lockUserInputs()
    {
        try
        {
            //Lock out the user inputs on the screen
            _main_savereceipt_button.setEnabled(false);
            _route.setEnabled(false);
            _license.setEnabled(false);
            _startmileage.setEnabled(false);
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "MainActivity", "lockUserInputs", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }

    /**
     * unlockUserInputs
     *  - lock out the user inputs on the screen
     */
    private void unlockUserInputs()
    {
        try
        {
            //Lock out the user inputs on the screen
            _main_savereceipt_button.setEnabled(true);
            _route.setEnabled(true);
            _license.setEnabled(true);
            _startmileage.setEnabled(true);
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "MainActivity", "unlockUserInputs", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }
    //endregion
}
