package com.belgioioso.bcimilkreceipt.bcimilkreceipt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbDatabaseHandler;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbGeoLocation;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbLine;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbProfile;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbSettings;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PickupActivity extends AppCompatActivity implements View.OnClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private Button _pickup_scanproducer_button, _pickup_scanlabcode_button, _pickup_save_button, _pickup_gotoreceive_button, _pickup_previous_button, _pickup_next_button;
    private TextView _pickup_Bottom_Message, _pickup_Bottom_SaveMessage, _pickup_Totals, _pickup_pickupcount_message, _pickup_location;
    private EditText _pickup_producer, _pickup_tank, _pickup_labcode, _gaugerod_major, _gaugerod_minor, _convertedLBS, _convertedLBS_confirm, _temperature, _dfa_ticket;
    private String _spkSettingsID, _spkProfileID, _spkHeaderID, _sCompany, _sDivision, _sType, _sLatitude, _sLongitude, _sAccurracy, _sUsername, _sTypeOfScan;
    private Utilities _oUtils;
    private dbProfile _oProfile;
    private Integer _iTotalPickupsOnTicket, _iCurrentPickup;
    private Map<Integer, String> _oAllPickupIDs = new HashMap<Integer, String>();
    private GoogleApiClient _oGAC;
    private LocationRequest _oLocationRequest;
    private long _iUPDATE_INTERVAL = 20 * 1000;
    private long _iFASTEST_INTERVAL = 2000;
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    //region Class Constructor Methods
    /**
     * onCreate
     *  - Constructor
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);

        //Set the keyboard to not show automatically
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialization
        _iTotalPickupsOnTicket = 0;
        _iCurrentPickup = 0;
        _sUsername = "N/A";
        _oUtils = new Utilities();

        //Instantiate the on screen buttons
        _pickup_scanproducer_button = findViewById(R.id.pickup_scanproducer_button);
        _pickup_scanlabcode_button = findViewById(R.id.pickup_scanlabcode_button);
        _pickup_save_button = findViewById(R.id.pickup_save_button);
        _pickup_gotoreceive_button = findViewById(R.id.pickup_gotoreceive_button);
        _pickup_previous_button = findViewById(R.id.pickup_previous_button);
        _pickup_next_button = findViewById(R.id.pickup_next_button);

        //Instantiate the pickup bottom message and savemessage text view
        _pickup_Bottom_Message = findViewById(R.id.pickup_bottom_message);
        _pickup_Bottom_SaveMessage = findViewById(R.id.pickup_bottom_savemessage);
        _pickup_Totals = findViewById(R.id.pickup_totals);
        _pickup_pickupcount_message = findViewById(R.id.pickup_pickupcount_message);
        _pickup_location = findViewById(R.id.pickup_location);

        //Instantiate the pickup edit text boxes
        _pickup_producer = findViewById(R.id.producer);
        _pickup_tank = findViewById(R.id.tank);
        _pickup_labcode = findViewById(R.id.labcode);
        _gaugerod_major = findViewById(R.id.gaugerodmajor);
        _gaugerod_minor = findViewById(R.id.gaugerodminor);
        _convertedLBS = findViewById(R.id.convertedlbs);
        _convertedLBS_confirm = findViewById(R.id.convertedlbs_confirm);
        _temperature = findViewById(R.id.temperature);
        _dfa_ticket = findViewById(R.id.dfa_ticket);

        //Set the on click listener for page to the screen buttons
        _pickup_scanproducer_button.setOnClickListener(this);
        _pickup_scanlabcode_button.setOnClickListener(this);
        _pickup_save_button.setOnClickListener(this);
        _pickup_gotoreceive_button.setOnClickListener(this);
        _pickup_previous_button.setOnClickListener(this);
        _pickup_next_button.setOnClickListener(this);

        //Setup the bundle object
        Bundle oBundle = getIntent().getExtras();

        //Get the header, settings and profile id's from the bundle
        _spkHeaderID = oBundle.getString("pkHeaderID");
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

        _sLatitude = "0";
        _sLongitude = "0";
        _sAccurracy = "0";

        //Set focus to the producer input
        _pickup_producer.requestFocus();

        //Setup GPS provider
        setupGPS();
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

        //Set the menu to the milkreceipt pickup menu
        inflater.inflate(R.menu.milkreceipt_pickup_menu, menu);

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
        Toast.makeText(PickupActivity.this, "Back button pressed: NOT ALLOWED!", Toast.LENGTH_SHORT).show();
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
                case R.id.menu_pickup_activity:
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
                    _oUtils.insertActivity(this, "1", "ReceiptActivity", "onOptionsItemSelected", _sUsername, "Pickup menu activity log selected", "");

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Menu Pickup Delete Current Pickup
                case R.id.menu_pickup_delete:
                    //Log message to activity
                    _oUtils.insertActivity(this, "1", "PickupActivity", "onOptionsItemSelected", _sUsername, "Pickup menu delete pickup selected", "");

                    //Check that the pickup screen is not in a new add state
                    if (_iCurrentPickup <= _iTotalPickupsOnTicket)
                    {
                        // Use the Builder class for convenient dialog construction
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);

                        //Build the message
                        builder.setMessage("Are you sure you want to delete this pickup?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Get the lineid from hash map
                                        String sLineID = _oAllPickupIDs.get(_iCurrentPickup);

                                        //Run the deletion process
                                        deleteCurrentPickup(sLineID);

                                        //Format the date for insert and modified
                                        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                                        Date dDate = new Date();

                                        //Display update successful message on bottom of screen
                                        _pickup_Bottom_SaveMessage.setText("Pickup deleted successfully at: " + dfDate.format(dDate));
                                        _pickup_Bottom_SaveMessage.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.valueText_Lime));
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Log message
                                        _oUtils.insertActivity(getApplicationContext(), "1", "YesNoDialog", "onCreateDialog", "N/A", "User cancelled the pickup deletion process", "");

                                    }
                                });

                        //Show the dialog box
                        AlertDialog aDialog = builder.create();
                        aDialog.show();
                    }
                    else
                    {
                        Toast.makeText(this, "Please move to an existing pickup before delete option is selected!", Toast.LENGTH_LONG).show();
                    }

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Logout item selected
                case R.id.menu_pickup_logout:
                    //Instantiate a new intent of SignInActivity
                    Intent logout_intent = new Intent(this, SignInActivity.class);

                    //Navigate to the activity log screen
                    startActivity(logout_intent);

                    //Log message to activity
                    _oUtils.insertActivity(this, "1", "PickupActivity", "onOptionsItemSelected", _sUsername, "Pickup menu back to SignIn selected", "");

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
            _oUtils.insertActivity(this, "3", "PickupActivity", "onOptionsItemSelected", _sUsername, ex.getMessage(), ex.getStackTrace().toString());
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
        String sLineID;

        try
        {
            //Lock the user inputs
            lockUserInputs();

            //Check if the scan producer button was pressed
            if (v.getId() == R.id.pickup_scanproducer_button)
            {
                //Log message to activity
                _oUtils.insertActivity(this, "1", "PickupActivity", "onClick", _sUsername, "Pickup scan producer button pressed", "");

                //Set type of scan
                _sTypeOfScan = "Producer";

                //Instantiate a new producer scanning intent integrator
                IntentIntegrator scanProducerIntegrator = new IntentIntegrator(this);

                //Set the producer scan intent to initiate scan
                scanProducerIntegrator.initiateScan();
            }
            //Check if the scan labcode button was pressed
            else if (v.getId() == R.id.pickup_scanlabcode_button)
            {
                //Log message to activity
                _oUtils.insertActivity(this, "1", "PickupActivity", "onClick", _sUsername, "Pickup scan labcode button pressed", "");

                //Set type of scan
                _sTypeOfScan = "Lab";

                //Instantiate a new labcode scanning intent integrator
                IntentIntegrator scanLabCodeIntegrator = new IntentIntegrator(this);

                //Set the labcode scan intent to initiate scan
                scanLabCodeIntegrator.initiateScan();
            }
            //Check if the save pickup button was pressed
            else if (v.getId() == R.id.pickup_save_button)
            {
                //Log message to activity
                _oUtils.insertActivity(this, "1", "PickupActivity", "onClick", _sUsername, "Pickup save button pressed", "");

                String sLineIDSaved;

                //Check if this is an existing pickup save
                if ((_iCurrentPickup <= _iTotalPickupsOnTicket) && (_iCurrentPickup > 0))
                {
                    //Get the lineid from hash map
                    String sExistingLineID = _oAllPickupIDs.get(_iCurrentPickup);

                    //Save the existing pickup
                    sLineIDSaved = saveExistingPickup(sExistingLineID);
                }
                else
                {
                    //Save the new pickup
                    sLineIDSaved = saveNewPickup();
                }

                //Format the date for insert and modified
                DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                Date dDate = new Date();

                //Check if the line id string is null
                if (sLineIDSaved != null)
                {
                    //Check if the record was saved
                    if (sLineIDSaved.length() > 0)
                    {
                        //Clear the screen contents/colors
                        clearScreenValues();

                        //Check if this is an existing pickup save
                        if ((_iCurrentPickup <= _iTotalPickupsOnTicket) && (_iCurrentPickup > 0))
                        {
                            //Display update successful message on bottom of screen
                            _pickup_Bottom_SaveMessage.setText("Pickup updated successfully at: " + dfDate.format(dDate));
                            _pickup_Bottom_SaveMessage.setTextColor(ContextCompat.getColor(this, R.color.valueText_Lime));
                        }
                        else
                        {
                            //Display save successful message on bottom of screen
                            _pickup_Bottom_SaveMessage.setText("Pickup saved successfully at: " + dfDate.format(dDate));
                            _pickup_Bottom_SaveMessage.setTextColor(ContextCompat.getColor(this, R.color.valueText_Lime));
                        }

                        //Instantiate the database handler
                        dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);
                        List<dbLine> olLine;
                        Integer iTotalLBS = 0;

                        //Get the list of lines by header id for current ticket
                        olLine = oDBHandler.findLinesByHeaderID(_spkHeaderID);

                        //Get the total LBS on ticket
                        iTotalLBS = getTotalPickupLBS(olLine);

                        //Clear the pickups hashmap
                        _oAllPickupIDs = new HashMap<Integer, String>();

                        //Loop through the line records
                        for (int i=0; i<olLine.size(); i++)
                        {
                            //Get the line from list of records
                            dbLine oLine = olLine.get(i);

                            //Add the lineid to the line array
                            _oAllPickupIDs.put(i + 1,oLine.getPkLineID());
                        }

                        //Display the pickup info on UI
                        _pickup_Totals.setText("Total Pickups: " + olLine.size() + " --- Total LBS: " + iTotalLBS);

                        //Update the total pickups on ticket register
                        _iTotalPickupsOnTicket = olLine.size();

                        //Update pickup count message
                        _pickup_pickupcount_message.setText("NEW of " + _iTotalPickupsOnTicket);
                        _iCurrentPickup = _iTotalPickupsOnTicket + 1;

                        //Check if the total pickups count is 0
                        if (_iTotalPickupsOnTicket == 0)
                        {
                            //Disable the previous button
                            _pickup_previous_button.setEnabled(false);
                        }
                        else
                        {
                            //Enable the previous button
                            _pickup_previous_button.setEnabled(true);
                        }

                        //Instantiate a new settings object
                        dbSettings oSettings = new dbSettings();

                        //Get the settings object from database
                        oSettings = oDBHandler.findSettingsByID(_spkSettingsID);

                        //Check if the auto db backup flag is set
                        if (oSettings.getAutoDBBackup() == 1)
                        {
                            //Backup the database
                            _oUtils.copyDBFile(this, _sUsername);
                        }
                    }
                    else
                    {
                        //Display save failed message on bottom of screen
                        _pickup_Bottom_SaveMessage.setText("Pickup save failed at: " + dfDate.format(dDate));
                        _pickup_Bottom_SaveMessage.setTextColor(ContextCompat.getColor(this, R.color.valueText_Red));
                    }
                }
                else
                {
                    //Display save failed message on bottom of screen
                    _pickup_Bottom_SaveMessage.setText("Pickup save failed at: " + dfDate.format(dDate));
                    _pickup_Bottom_SaveMessage.setTextColor(ContextCompat.getColor(this, R.color.valueText_Red));
                }
            }
            //Check if the goto receive button was pressed
            else if (v.getId() == R.id.pickup_gotoreceive_button)
            {
                //Log message to activity
                _oUtils.insertActivity(this, "1", "PickupActivity", "onClick", _sUsername, "Pickup go to receive button pressed", "");

                //Instantiate a new intent of ReceiveActivity
                Intent receive_intent = new Intent(this, ReceiveActivity.class);

                //Instantiate the bundle object
                Bundle oBundle = new Bundle();

                //Set the headerID, profileID and settingsID in the bundle
                oBundle.putString("pkHeaderID", _spkHeaderID);
                oBundle.putString("pkProfileID", _spkProfileID);
                oBundle.putString("pkSettingsID", _spkSettingsID);

                //Setup bundle into intent
                receive_intent.putExtras(oBundle);

                //Navigate to the receive screen
                startActivity(receive_intent);
            }
            //Check if the previous pickup button was pressed
            else if (v.getId() == R.id.pickup_previous_button)
            {
                //Log message to activity
                _oUtils.insertActivity(this, "1", "PickupActivity", "onClick", _sUsername, "Pickup previous button pressed", "");

                //Check if current pickup count is greater than 1
                if (_iCurrentPickup > 1)
                {
                    //Decrement current pickup
                    _iCurrentPickup = _iCurrentPickup - 1;

                    //Get the lineid from hash map
                    sLineID = _oAllPickupIDs.get(_iCurrentPickup);

                    //Load previous pickup
                    loadPickup(sLineID);

                    //Check if the current pickup is 1
                    if (_iCurrentPickup == 1)
                    {
                        //Disable the previous button
                        _pickup_previous_button.setEnabled(false);
                    }

                    //Reset the pickup message
                    _pickup_pickupcount_message.setText(_iCurrentPickup + " of " + _iTotalPickupsOnTicket);
                }

                //Check if current count is less than or equal to total pickups
                if (_iCurrentPickup <= _iTotalPickupsOnTicket)
                {
                    //Enable the next button
                    _pickup_next_button.setEnabled(true);
                }
            }
            //Check if the next pickup button was pressed
            else if (v.getId() == R.id.pickup_next_button)
            {
                //Log message to activity
                _oUtils.insertActivity(this, "1", "PickupActivity", "onClick", _sUsername, "Pickup next button pressed", "");

                //Check if current pickup count is less than total pickups count
                if (_iCurrentPickup < _iTotalPickupsOnTicket)
                {
                    //Increment current pickup
                    _iCurrentPickup = _iCurrentPickup + 1;

                    //Get the lineid from hash map
                    sLineID = _oAllPickupIDs.get(_iCurrentPickup);

                    //Load next pickup
                    loadPickup(sLineID);

                    //Reset the pickup message
                    _pickup_pickupcount_message.setText(_iCurrentPickup + " of " + _iTotalPickupsOnTicket);

                    //Check if current count is less than or equal to total pickups
                    if (_iCurrentPickup <= _iTotalPickupsOnTicket)
                    {
                        //Enable the previous button
                        _pickup_previous_button.setEnabled(true);
                    }
                }
                //Check if current pickup count is equal to total pickup count
                else if (_iCurrentPickup == _iTotalPickupsOnTicket)
                {
                    //Incement current counter to set the count greater than the total count by 1 (means new pickup)
                    _iCurrentPickup = _iCurrentPickup + 1;

                    //Reset the pickup message
                    _pickup_pickupcount_message.setText("NEW of " + _iTotalPickupsOnTicket);

                    //Clear the screen values
                    clearScreenValues();

                    //Check if current count is less than or equal to total pickups
                    if (_iCurrentPickup == _iTotalPickupsOnTicket + 1)
                    {
                        //Enable the previous button
                        _pickup_previous_button.setEnabled(true);
                        _pickup_next_button.setEnabled(false);
                    }
                }
            }

            //Unlock the user inputs
            unlockUserInputs();
        }
        catch (Exception ex)
        {
            //Unlock the user inputs
            unlockUserInputs();

            //Log error message to activity
            _oUtils.insertActivity(this, "3", "PickupActivity", "onClick", _sUsername, ex.getMessage(), ex.getStackTrace().toString());
        }
    }
    //endregion

    //region Routines
    /**
     * onActivityResult
     * - Handles the intent of barcode scanner and the returned result
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        try
        {
            //Instantiate the intentresult object from scanning intentintegrator
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

            //Check if the scanned result was retrieved
            if (scanningResult != null)
            {
                //Get the scan content and the scan format from barcode read
                String scanContent = scanningResult.getContents();

                //Check if the scan content retrieved is 14 characters long
                if ((scanContent.length() == 14) && (_sTypeOfScan == "Producer"))
                {
                    //Log message to activity
                    _oUtils.insertActivity(this, "1", "PickupActivity", "onActivityResult", _sUsername, "Producer barcode scan found: " + scanContent, "");

                    //Get the parsed values from content
                    _sCompany = scanContent.substring(0,3);
                    _sDivision = scanContent.substring(3,6);
                    String sProducer = scanContent.substring(6,10);
                    String sTank = scanContent.substring(10,12);
                    _sType = scanContent.substring(12,14);

                    //Set the producer and tank edit text fields
                    _pickup_producer.setText(sProducer);
                    _pickup_tank.setText(sTank);

                    //Focus to the gauge rod major field
                    _gaugerod_major.requestFocus();
                }
                //Check if the scan content retrieved is 18 characters long
                else
                {
                    //Log message to activity
                    _oUtils.insertActivity(this, "1", "PickupActivity", "onActivityResult", _sUsername, "LabCode barcode scan found: " + scanContent, "");

                    //Set the labcode edit text field
                    _pickup_labcode.setText(scanContent);

                    //Set focus to the lab code field
                    _pickup_labcode.requestFocus();
                }
            }
            else
            {
                //Display message to user that the barcode was not found or scanned
                Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);

                //Log message to activity
                _oUtils.insertActivity(this, "1", "PickupActivity", "onActivityResult", _sUsername, "No scan data received!", "");
            }
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "PickupActivity", "onActivityResult", _sUsername, ex.getMessage(), ex.getStackTrace().toString());
        }
    }

    /**
     * setupScreen
     * - Setup the bottom message on the screen and other screen intitializations
     */
    private void setupScreen()
    {
        ArrayList<dbLine> olLine;
        Integer iTotalLBS = 0;
        Integer iPickups = 0;

        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the profile object from database
            _oProfile = oDBHandler.findProfileByID(_spkProfileID);

            //Get the list of lines by header id for current ticket
            olLine = oDBHandler.findLinesByHeaderID(_spkHeaderID);

            //Check if there are any pickups
            if (olLine != null)
            {
                //Get the total pickups
                iPickups = olLine.size();

                //Set the global total pickups register
                _iTotalPickupsOnTicket = iPickups;

                //Get the total LBS on ticket
                iTotalLBS = getTotalPickupLBS(olLine);

                //Loop through the line records
                for (int i=0; i<olLine.size(); i++)
                {
                    //Get the line from list of records
                    dbLine oLine = olLine.get(i);

                    //Add the lineid to the line array
                    _oAllPickupIDs.put(i + 1,oLine.getPkLineID());
                }

                //Set total pickups to line count and current pickup to number of pickups plus 1 so we know we are adding a new pickup
                _iCurrentPickup = olLine.size() + 1;
                _iTotalPickupsOnTicket = olLine.size();
            }
            else
            {
                //No pickups found, so re-initialize pickup counts
                _iCurrentPickup = 0;
                _iTotalPickupsOnTicket = 0;
                _oAllPickupIDs = new HashMap<Integer, String>();
            }

            //Display the pickup info on UI
            _pickup_Totals.setText("Total Pickups: " + iPickups + " --- Total LBS: " + iTotalLBS);

            //Setup pickup count and buttons
            _pickup_pickupcount_message.setText("NEW of " + iPickups);
            _pickup_next_button.setEnabled(false);

            //Check if the total pickups count is 0
            if (_iTotalPickupsOnTicket == 0)
            {
                //Disable the previous button
                _pickup_previous_button.setEnabled(false);
            }
            else
            {
                //Enable the previous button
                _pickup_previous_button.setEnabled(true);
            }

            //Check if the profile record was found
            if (_oProfile != null)
            {
                //Format the date for insert and modified
                DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                Date dDate = new Date();

                //Set text for the receipt bottom message text view
                _pickup_Bottom_Message.setText("Current User: " + _oProfile.getFullName() + " - Current Pickup Date: " + dfDate.format(dDate).toString());
                
                //Set the username global variable
                _sUsername = _oProfile.getUsername();
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "PickupActivity", "setupScreen", _sUsername, ex.getMessage(), ex.getStackTrace().toString());
        }
    }

    //region GPS
    /**
     * setupGPS
     *  - initialization of GPS services and listeners
     */
    private void setupGPS()
    {
        try
        {
            if(!isLocationEnabled())
            {
                showAlert();
            }

            _oLocationRequest = new LocationRequest();
            _oLocationRequest.setInterval(_iUPDATE_INTERVAL);
            _oLocationRequest.setFastestInterval(_iFASTEST_INTERVAL);
            _oLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            _oGAC = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "PickupActivity", "setupGPS", _sUsername, ex.getMessage(), ex.getStackTrace().toString());
        }
    }

    @Override
    protected void onStart()
    {
        _oGAC.connect();
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        _oGAC.disconnect();
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location)
    {
        if (location != null)
        {
            updateUI(location);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return;
        }

        Location ll = LocationServices.FusedLocationApi.getLastLocation(_oGAC);

        LocationServices.FusedLocationApi.requestLocationUpdates(_oGAC, _oLocationRequest, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Permission was granted!", Toast.LENGTH_LONG).show();

                    try
                    {
                        LocationServices.FusedLocationApi.requestLocationUpdates(_oGAC, _oLocationRequest, this);
                    }
                    catch (SecurityException e)
                    {
                        Toast.makeText(this, "SecurityException:\n" + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                }

                return;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Toast.makeText(this, "onConnectionFailed: \n" + connectionResult.toString(), Toast.LENGTH_LONG).show();
    }

    private void updateUI(Location loc)
    {
        _sLatitude = Double.toString(loc.getLatitude());
        _sLongitude = Double.toString(loc.getLongitude());
        _sAccurracy = Double.toString(loc.getAccuracy());

        _pickup_location.setText("Current Location: Lat: " + _sLatitude + " Long: " + _sLongitude + " Time: " + DateFormat.getTimeInstance().format(loc.getTime()));
    }

    private boolean isLocationEnabled()
    {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showAlert()
    {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to use this app")

                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt)
                    {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt)
                    {

                    }
                });

        dialog.show();
    }
    //endregion

    /**
     * saveNewPickup
     *  - save a new pickup record to database
     * @return (String) - returns the guid of the saved pickup record
     */
    private String saveNewPickup()
    {
        String sLineID = null;
        dbLine oLine = new dbLine();
        List<dbLine> olLine = new ArrayList<>();

        try
        {
            if (!checkPickupForErrors())
            {
                //Instantiate the database handler
                dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

                //Create a new headerID GUID
                UUID gID = UUID.randomUUID();

                //Setup the new line object data
                oLine.setPkLineID(gID.toString());
                oLine.setFkHeaderID(_spkHeaderID);
                oLine.setTank(_pickup_tank.getText().toString());
                oLine.setProducer(_pickup_producer.getText().toString());
                oLine.setCompany(_sCompany);
                oLine.setDivision(_sDivision);
                oLine.setType(_sType);
                oLine.setGaugeRodMajor(TextUtils.isEmpty(_gaugerod_major.getText().toString()) ? 0 : Integer.parseInt(_gaugerod_major.getText().toString()));
                oLine.setGaugeRodMinor(TextUtils.isEmpty(_gaugerod_minor.getText().toString()) ? 0 : Integer.parseInt(_gaugerod_minor.getText().toString()));
                oLine.setConvertedLBS(Integer.parseInt(_convertedLBS.getText().toString()));
                oLine.setTemperature(Integer.parseInt(_temperature.getText().toString()));
                oLine.setPickupDate(_oUtils.getFormattedDate(this, _sUsername));
                oLine.setDFATicket(_dfa_ticket.getText().toString());
                oLine.setLabCode(_pickup_labcode.getText().toString());
                oLine.setLatitude(_sLatitude);
                oLine.setLongitude(_sLongitude);
                oLine.setAccurracy(_sAccurracy);
                oLine.setFinished(0);
                oLine.setWaitingForScaleData(0);
                oLine.setTransmitted(0);
                oLine.setTransmittedDate(_oUtils.getFormattedDate(this, _sUsername, "1900-01-01 00:00:00.000"));
                oLine.setInsertDate(_oUtils.getFormattedDate(this, _sUsername));
                oLine.setModifiedDate(_oUtils.getFormattedDate(this, _sUsername));

                //Setup the arraylist for line insertion
                olLine.add(oLine);

                //Add the line to the database
                oDBHandler.addLine(olLine);

                //Set the return LineID
                sLineID = gID.toString();

                //Log message to activity
                _oUtils.insertActivity(this, "1", "PickupActivity", "saveNewPickup", _sUsername, "New Pickup Saved:: ID: " + sLineID, "");
                _oUtils.insertActivity(this, "1", "PickupActivity", "saveNewPickup", _sUsername, "New Pickup Saved:: Producer: " + oLine.getProducer() + " Tank: " + oLine.getTank() + " GaugeRod: " + oLine.getGaugeRodMajor() + "/" + oLine.getGaugeRodMinor() + " Temp: " + oLine.getTemperature() + " ConvertedLBS: " + oLine.getConvertedLBS() + " LabCode: " + oLine.getLabCode() + " DFATicket: " + oLine.getDFATicket(), "");
            }
        }
        catch (Exception ex)
        {
            //Unlock the user inputs
            unlockUserInputs();

            //Log error message to activity
            _oUtils.insertActivity(this, "3", "PickupActivity", "saveNewPickup", _sUsername, ex.getMessage(), ex.getStackTrace().toString());
        }

        //Return the lineID
        return sLineID;
    }

    /**
     * saveExistingPickup
     *  - save the existing pickup to database
     * @param pspkLineID
     * @return (String) - the line id of the existing pickup
     */
    private String saveExistingPickup(String pspkLineID)
    {
        dbLine oLine = new dbLine();

        try
        {
            if (!checkPickupForErrors())
            {
                //Instantiate the database handler
                dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

                //Get the existing line from database
                oLine = oDBHandler.findLineByID(pspkLineID);

                //Setup the new line object data
                oLine.setPkLineID(pspkLineID);
                oLine.setFkHeaderID(_spkHeaderID);
                oLine.setTank(_pickup_tank.getText().toString());
                oLine.setProducer(_pickup_producer.getText().toString());
                oLine.setCompany(_sCompany);
                oLine.setDivision(_sDivision);
                oLine.setType(_sType);
                oLine.setGaugeRodMajor(_gaugerod_major.getText().toString() == "" ? 0 : Integer.parseInt(_gaugerod_major.getText().toString()));
                oLine.setGaugeRodMinor(_gaugerod_minor.getText().toString() == "" ? 0 : Integer.parseInt(_gaugerod_minor.getText().toString()));
                oLine.setConvertedLBS(Integer.parseInt(_convertedLBS.getText().toString()));
                oLine.setTemperature(Integer.parseInt(_temperature.getText().toString()));
                oLine.setDFATicket(_dfa_ticket.getText().toString());
                oLine.setLabCode(_pickup_labcode.getText().toString());
                oLine.setFinished(0);
                oLine.setWaitingForScaleData(0);
                oLine.setTransmitted(0);
                oLine.setTransmittedDate(_oUtils.getFormattedDate(this, _sUsername, "1900-01-01 00:00:00.000"));
                oLine.setModifiedDate(_oUtils.getFormattedDate(this, _sUsername));

                 //Update the line in the database
                oDBHandler.updateLine(oLine);

                //Log message to activity
                _oUtils.insertActivity(this, "1", "PickupActivity", "saveExistingPickup", _sUsername, "New Pickup Saved:: ID: " + pspkLineID, "");
                _oUtils.insertActivity(this, "1", "PickupActivity", "saveExistingPickup", _sUsername, "New Pickup Saved:: Producer: " + oLine.getProducer() + " Tank: " + oLine.getTank() + " GaugeRod: " + oLine.getGaugeRodMajor() + "/" + oLine.getGaugeRodMinor() + " Temp: " + oLine.getTemperature() + " ConvertedLBS: " + oLine.getConvertedLBS() + " LabCode: " + oLine.getLabCode() + " DFATicket: " + oLine.getDFATicket(), "");
            }
        }
        catch (Exception ex)
        {
            //Unlock the user inputs
            unlockUserInputs();

            //Log error message to activity
            _oUtils.insertActivity(this, "3", "PickupActivity", "saveExistingPickup", _sUsername, ex.getMessage(), ex.getStackTrace().toString());
        }

        //Return the lineID
        return pspkLineID;
    }

    /**
     * deleteCurrentPickup
     *  - delete the current pickup and reset totals
     * @param pspkLineID
     */
    private void deleteCurrentPickup(String pspkLineID)
    {
        dbLine oLine;

        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get line from database
            oLine = oDBHandler.findLineByID(pspkLineID);

            //Check if the line object is populated
            if (oLine != null)
            {
                //Delete the line from the database
                oDBHandler.deleteLineByID(oLine.getPkLineID());
            }

            //Clear the screen
            clearScreenValues();

            //Run the setup again to get all counters and messages reset
            setupScreen();
        }
        catch (Exception ex)
        {
            //Unlock the user inputs
            unlockUserInputs();

            //Log error message to activity
            _oUtils.insertActivity(this, "3", "PickupActivity", "deleteCurrentPickup", _sUsername, ex.getMessage(), ex.getStackTrace().toString());
        }
    }

    /**
     * checkConvertedLBS
     *  - checks if the convertedLBS and the convertedLBSConfirm are equal
     * @param psConvertedLBS
     * @param psConvertedLBSConfirm
     * @return (boolean) result of the check if the 2 values are equal
     */
    private boolean checkConvertedLBS(String psConvertedLBS, String psConvertedLBSConfirm)
    {
        boolean bCheck = false;
        double fConvertedLBS, fConvertedLBSConfirm;

        try
        {
            //Set the check to false
            bCheck = false;

            //Check if the convertedLBS or convertedLBSConfirm is empty or null
            if (!TextUtils.isEmpty(psConvertedLBS) && !TextUtils.isEmpty(psConvertedLBSConfirm))
            {
                //Try converting the strings to double values
                fConvertedLBS = Double.parseDouble(psConvertedLBS);
                fConvertedLBSConfirm = Double.parseDouble(psConvertedLBSConfirm);

                //Check if the convertedLBS is equal to the convertedLBSConfirm
                if (fConvertedLBS == fConvertedLBSConfirm)
                {
                    //Both are equal, set check to valid
                    bCheck = true;
                }
                else
                {
                    //Not equal, set check to invalid
                    bCheck = false;
                }
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "PickupActivity", "checkConvertedLBS", _sUsername, ex.getMessage(), ex.getStackTrace().toString());
        }

        //Return the check result
        return bCheck;
    }

    /**
     * checkPickupForErrors
     *  - checks all fields for errors
     * @return (boolean) - true/false if errors are present or not
     */
    private boolean checkPickupForErrors()
    {
        boolean bCheck = false;

        try
        {
            //Check for headerid being blank or null
            if (_spkHeaderID.length() == 0 || _spkHeaderID == null)
            {
                bCheck = true;
            }

            //Check for producer being 4 characters
            if (_pickup_producer.getText().toString().length() < 4)
            {
                //Set flag as error found
                bCheck = true;

                //Set error for producer
                _pickup_producer.setError("Producer Must be 4 Digits");
            }

            //Check for tank being 2 characters
            if (_pickup_tank.getText().toString().length() < 2)
            {
                //Set flag as error found
                bCheck = true;

                //Set error for tank
                _pickup_tank.setError("Tank Must be 2 Digits");
            }

            //Check if this is a pickup scan type
            if (_sType == "PU")
            {
                //Check if the gauge rod major is entered
                if (_gaugerod_major.getText().length() < 1)
                {
                    //Set flag as error found
                    bCheck = true;

                    //Set error for the gauge rod major
                    _gaugerod_major.setError("Gauge Rod Major is Required");
                }

                //Check if the gauge rod minor is entered
                if (_gaugerod_minor.getText().length() < 1)
                {
                    //Set flag as error found
                    bCheck = true;

                    //Set error for the gauge rod minor
                    _gaugerod_minor.setError("Gauge Rod Minor is Required");
                }
            }

            //Check convertedlbs against convertexlbsconfirmation
            if (!checkConvertedLBS(_convertedLBS.getText().toString(), _convertedLBS_confirm.getText().toString()))
            {
                //Set flag as error found
                bCheck = true;

                //Set error for convertedLBS not equal to convertedLBSConfirmation
                _convertedLBS_confirm.setError("Converted LBS Must Match");
            }

            //Check that the temperature is entered
            if (_temperature.getText().length() < 1)
            {
                //Set flag as error found
                bCheck = true;

                //Set error for temperature
                _temperature.setError("Temperature is Required");
            }
        }
        catch (Exception ex)
        {
            //Unlock the user inputs
            unlockUserInputs();

            //Log error message to activity
            _oUtils.insertActivity(this, "3", "PickupActivity", "checkPickupForErrors", _sUsername, ex.getMessage(), ex.getStackTrace().toString());

            bCheck = false;
        }

        //Return the check
        return bCheck;
    }

    /**
     * getTotalPickupLBS
     *  - get the total pickup lbs for current ticket
     * @param polLines
     * @return (Integer) - returns the total pickup lbs on current ticket
     */
    private Integer getTotalPickupLBS(List<dbLine> polLines)
    {
        Integer iTotalLBS = 0;
        dbLine oLine;

        try
        {
            //Check that the line list has records
            if (polLines != null)
            {
                //Loop through all pickup lines on ticket
                for (int i = 0; i < polLines.size(); i++)
                {
                    //Instantiate a new line object
                    oLine = new dbLine();

                    //Get the next line in list
                    oLine = polLines.get(i);

                    //Add the LBS to the total LBS
                    iTotalLBS = iTotalLBS + oLine.getConvertedLBS();
                }
            }
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "PickupActivity", "getTotalPickupLBS", _sUsername, ex.getMessage(), ex.getStackTrace().toString());

            iTotalLBS = 0;
        }

        return iTotalLBS;
    }

    /**
     * clearScreenValues
     *  - clear the screen contents
     */
    private void clearScreenValues()
    {
        try
        {
            //Clear the screen contents
            _pickup_producer.setText("");
            _pickup_tank.setText("");
            _pickup_labcode.setText("");
            _gaugerod_major.setText("");
            _gaugerod_minor.setText("");
            _convertedLBS.setText("");
            _convertedLBS_confirm.setText("");
            _temperature.setText("");
            _dfa_ticket.setText("");

            //Clear the errors
            _pickup_producer.setError(null);
            _pickup_tank.setError(null);
            _gaugerod_major.setError(null);
            _gaugerod_minor.setError(null);
            _convertedLBS.setError(null);
            _convertedLBS_confirm.setError(null);
            _temperature.setError(null);

            //Unlock the user inputs
            unlockUserInputs();

            //Set focus to the producer input
            _pickup_producer.requestFocus();
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "PickupActivity", "clearScreen", _sUsername, ex.getMessage(), ex.getStackTrace().toString());
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
            _pickup_scanproducer_button.setEnabled(false);
            _pickup_scanlabcode_button.setEnabled(false);
            _pickup_save_button.setEnabled(false);
            _pickup_gotoreceive_button.setEnabled(false);
            _pickup_producer.setEnabled(false);
            _pickup_tank.setEnabled(false);
            _pickup_labcode.setEnabled(false);
            _gaugerod_major.setEnabled(false);
            _gaugerod_minor.setEnabled(false);
            _convertedLBS.setEnabled(false);
            _convertedLBS_confirm.setEnabled(false);
            _temperature.setEnabled(false);
            _dfa_ticket.setEnabled(false);
            _pickup_previous_button.setEnabled(false);
            _pickup_next_button.setEnabled(false);
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "PickupActivity", "lockUserInputs", _sUsername, ex.getMessage(), ex.getStackTrace().toString());
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
            _pickup_scanproducer_button.setEnabled(true);
            _pickup_scanlabcode_button.setEnabled(true);
            _pickup_save_button.setEnabled(true);
            _pickup_gotoreceive_button.setEnabled(true);
            _pickup_producer.setEnabled(true);
            _pickup_tank.setEnabled(true);
            _pickup_labcode.setEnabled(true);
            _gaugerod_major.setEnabled(true);
            _gaugerod_minor.setEnabled(true);
            _convertedLBS.setEnabled(true);
            _convertedLBS_confirm.setEnabled(true);
            _temperature.setEnabled(true);
            _dfa_ticket.setEnabled(true);

            //Check if current count is less than or equal to total pickups
            if (_iCurrentPickup <= _iTotalPickupsOnTicket && _iCurrentPickup > 1)
            {
                //Enable the next and previous buttons
                _pickup_next_button.setEnabled(true);
                _pickup_previous_button.setEnabled(true);
            }
            else if (_iCurrentPickup == 1)
            {
                _pickup_next_button.setEnabled(true);
                _pickup_previous_button.setEnabled(false);
            }
            else if (_iCurrentPickup == _iTotalPickupsOnTicket + 1)
            {
                //Enable the previous button
                _pickup_previous_button.setEnabled(true);
                _pickup_next_button.setEnabled(false);
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "PickupActivity", "unlockUserInputs", _sUsername, ex.getMessage(), ex.getStackTrace().toString());
        }
    }

    /**
     * loadPickup
     *  - load the pickup of the ticket
     * @param pspkLineID
     */
    private void loadPickup(String pspkLineID)
    {
        dbLine oLine = new dbLine();

        try
        {
            //Clear the screen values
            clearScreenValues();

            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the line information from the database
            oLine = oDBHandler.findLineByID(pspkLineID);

            //Check if the line object is populated
            if (oLine != null)
            {
                _pickup_producer.setText(oLine.getProducer());
                _pickup_tank.setText(oLine.getTank());
                _gaugerod_major.setText(oLine.getGaugeRodMajor().toString());
                _gaugerod_minor.setText(oLine.getGaugeRodMinor().toString());
                _convertedLBS.setText(oLine.getConvertedLBS().toString());
                _convertedLBS_confirm.setText(oLine.getConvertedLBS().toString());
                _temperature.setText(oLine.getTemperature().toString());
                _dfa_ticket.setText(oLine.getDFATicket());
                _pickup_labcode.setText(oLine.getLabCode());
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "PickupActivity", "loadPickup", _sUsername, ex.getMessage(), ex.getStackTrace().toString());
        }
    }
    //endregion
}
