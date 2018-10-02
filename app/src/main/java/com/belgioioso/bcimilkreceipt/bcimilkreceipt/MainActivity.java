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
    private Button _main_savereceipt_button, _main_gotopickup_button;
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

        _sUsername = "N/A";
        
        _oUtils = new Utilities();

        //Instantiate the save receipt and goto pickup buttons
        _main_savereceipt_button = (Button)findViewById(R.id.main_savereceipt_button);
        _main_gotopickup_button = (Button)findViewById(R.id.main_gotopickup_button);

        //Instantiate the main bottom message text view
        main_Bottom_Message = (TextView)findViewById(R.id.main_bottom_message);

        //Instantiate the route, license and startmileage edit text fields
        _route = (EditText)findViewById(R.id.main_route);
        _license = (EditText)findViewById(R.id.main_license);
        _startmileage = (EditText)findViewById(R.id.main_startmileage);

        //Set the on click listener for page to the save receipt and goto pickup buttons
        _main_savereceipt_button.setOnClickListener(this);
        _main_gotopickup_button.setOnClickListener(this);

        //Setup the bundle object
        Bundle oBundle = getIntent().getExtras();

        //Get the header, settings and profile id's from the bundle
        _spkSettingsID = oBundle.getString("pkSettingsID");
        _spkProfileID = oBundle.getString("pkProfileID");

        //Check if the settings id was not passed from receipt page
        if (_spkSettingsID == null || _spkSettingsID.length() < 1)
        {
            //Get the settings id from the database
            _spkSettingsID = findSettings(android.os.Build.SERIAL);
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
                    _oUtils.InsertActivity(this, "1", "MainActivity", "onOptionsItemSelected", _sUsername, "menu_main_activity item selected", "");

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
                    _oUtils.InsertActivity(this, "1", "MainActivity", "onOptionsItemSelected", _sUsername, "menu_main_logout item selected", "");

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
            _oUtils.InsertActivity(this, "3", "MainActivity", "onOptionsItemSelected", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
            //Check if the save receipt button was pressed
            if (v.getId() == R.id.main_savereceipt_button)
            {
                //Log message to activity
                _oUtils.InsertActivity(this, "1", "MainActivity", "onClick", _sUsername, "main_savereceipt_button pressed", "");

                //Save the new header record
                _spkHeaderID = saveNewHeader();

                //Check if the headerID was retrieved
                if (_spkHeaderID != null)
                {
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
                    _oUtils.InsertActivity(this, "1", "MainActivity", "onClick", _sUsername, "New receipt save unsuccessful, no ID created", "");
                }
            }
            else if (v.getId() == R.id.main_gotopickup_button)
            {
                //Log message to activity
                _oUtils.InsertActivity(this, "1", "MainActivity", "onClick", _sUsername, "main_gotopickup_button pressed", "");

                //Instantiate a new intent of Pickup Activity
                Intent gotopickup_intent = new Intent(this, PickupActivity.class);

                //Instantiate the bundle object
                Bundle oBundle = new Bundle();

                //Set the headerID, profileID and settingsID in the bundle
                oBundle.putString("pkHeaderID", _spkHeaderID);
                oBundle.putString("pkProfileID", _spkProfileID);
                oBundle.putString("pkSettingsID", _spkSettingsID);

                //Setup bundle into intent
                gotopickup_intent.putExtras(oBundle);

                //Navigate to the Pickup Activity page
                startActivity(gotopickup_intent);
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "MainActivity", "onClick", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
                _oUtils.InsertActivity(this, "1", "MainActivity", "findSettings", _sUsername, "Settings not found, new settings record saved", "");
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
            _oUtils.InsertActivity(this, "3", "MainActivity", "findSettings", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }

        //Return the settingsID
        return sReturnID;
    }

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

            //Disable the go to pickup screen button until record is saved
            _main_gotopickup_button.setEnabled(false);
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "MainActivity", "setupScreen", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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

            //Try parse of start mileage
            try
            {
                //Parse the start mileage field
                iStartMileage = Integer.parseInt(_startmileage.getText().toString());
            }
            catch(NumberFormatException nfe)
            {
                //Log error message to activity
                _oUtils.InsertActivity(this, "3", "MainActivity", "saveNewHeader", _sUsername, nfe.getMessage().toString(), nfe.getStackTrace().toString());
            }

            //Continue setup of new header object data
            oHeader.setStartMileage(iStartMileage);
            oHeader.setEndMileage(0);
            oHeader.setTotalMileage(0);
            oHeader.setFinished(0);
            oHeader.setWaitingForScaleData(0);
            oHeader.setTransmitted(0);
            oHeader.setTransmittedDate("1/1/1900");

            //Format the date for insert and modified
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            Date dDate = new Date();

            //Set the insert and modified date fields
            oHeader.setInsertDate(dfDate.format(dDate).toString());
            oHeader.setModifiedDate(dfDate.format(dDate).toString());

            //Add the header to the database
            oDBHandler.addHeader(oHeader);

            //Set the return headerID
            sHeaderID = gID.toString();
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "MainActivity", "saveNewHeader", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }

        //Return the headerID
        return sHeaderID;
    }
    //endregion
}
