package com.belgioioso.bcimilkreceipt.bcimilkreceipt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbDatabaseHandler;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbHeader;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbLine;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbPlant;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbProfile;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbReceive;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbSettings;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReceiveActivity extends AppCompatActivity implements View.OnClickListener//, View.OnFocusChangeListener
{
    private Button _receive_save_button, _receive_finishticket_button, _receive_previous_button, _receive_next_button;
    private TextView _receive_Bottom_Message, _receive_ReceiveLBSAvailable, _receive_Bottom_SaveMessage, _receive_receivecount_message;
    private EditText _receive_DrugTestDevice, _receive_DrugTestResult, _receive_Silo, _receive_Temperature, _receive_TopSeal, _receive_BottomSeal, _receive_ReceivedLBS, _receive_ReceivedLBSConfirmation, _receive_EndMileage, _receive_IntakeNumber;
    private Spinner _receive_plant, _receive_scalemeter;
    private String _spkSettingsID, _spkProfileID, _spkHeaderID, _sUsername;
    private Utilities _oUtils;
    private dbProfile _oProfile;
    private List<String> _oPlantIDList = new ArrayList<>();
    private Map<String, String> _oPlantLookup = new HashMap<String, String>();
    private boolean bIPCheck = false;
    private boolean bDumpHotCheck = false;
    private Integer _iTotalReceiveCount, _iCurrentReceive;
    private Map<Integer, String> _oAllReceiveIDs = new HashMap<Integer, String>();

    //region Class Constructor Methods
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        //Set the keyboard to not show automatically
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Initialization
        _iTotalReceiveCount = 0;
        _iCurrentReceive = 0;
        _sUsername = "N/A";
        _oUtils = new Utilities();

        //Instantiate the on screen buttons
        _receive_save_button = findViewById(R.id.receive_save_button);
        _receive_finishticket_button = findViewById(R.id.receive_finishticket_button);
        _receive_previous_button = findViewById(R.id.receive_previous_button);
        _receive_next_button = findViewById(R.id.receive_next_button);

        //Instantiate the spinners
        _receive_plant = findViewById(R.id.receive_plant);
        _receive_scalemeter = findViewById(R.id.receive_scalemeter);

        //Instantiate the receive bottom message text view
        _receive_Bottom_Message = findViewById(R.id.receive_bottom_message);
        _receive_ReceiveLBSAvailable = findViewById(R.id.receive_receivelbsavailable);
        _receive_Bottom_SaveMessage = findViewById(R.id.receive_bottom_savemessage);
        _receive_receivecount_message = findViewById(R.id.receive_receivecount_message);

        //Instantiate the receive edit text boxes
        _receive_DrugTestDevice = findViewById(R.id.receive_drugtestdevice);
        _receive_DrugTestResult = findViewById(R.id.receive_drugtestresult);
        _receive_Silo = findViewById(R.id.receive_silo);
        _receive_Temperature = findViewById(R.id.receive_temperature);
        _receive_TopSeal = findViewById(R.id.receive_topseal);
        _receive_BottomSeal = findViewById(R.id.receive_bottomseal);
        _receive_ReceivedLBS = findViewById(R.id.receive_receivelbs);
        _receive_ReceivedLBSConfirmation = findViewById(R.id.receive_receivelbs_confirm);
        _receive_EndMileage = findViewById(R.id.receive_endmileage);
        _receive_IntakeNumber = findViewById(R.id.receive_intakenumber);

        //Set the on click listener for page to the screen buttons
        _receive_save_button.setOnClickListener(this);
        _receive_finishticket_button.setOnClickListener(this);
        _receive_previous_button.setOnClickListener(this);
        _receive_next_button.setOnClickListener(this);

        //Set the focus change listener for drug test result edit text
        //_receive_DrugTestResult.setOnFocusChangeListener(hasFocusListener);

        //Setup the bundle object
        Bundle oBundle = getIntent().getExtras();

        //Get the header, settings and profile id's from the bundle
        _spkHeaderID = oBundle.getString("pkHeaderID");
        _spkProfileID = oBundle.getString("pkProfileID");
        _spkSettingsID = oBundle.getString("pkSettingsID");

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

        //Load the drop down fields
        populatePlantSpinner();
        populateScaleMeterSpinner();

        //Set focus to drug test result field
        _receive_DrugTestResult.requestFocus();
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

        //Set the menu to the milkreceipt receive menu
        inflater.inflate(R.menu.milkreceipt_receive_menu, menu);

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
        Toast.makeText(ReceiveActivity.this, "Back button pressed: NOT ALLOWED!", Toast.LENGTH_SHORT).show();
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
                case R.id.menu_receive_activity:
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
                    _oUtils.insertActivity(this, "1", "ReceiptActivity", "onOptionsItemSelected", _sUsername, "Receive menu activity log selected", "");

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Menu BackToPickups selected
                case R.id.menu_receive_backtopickups:
                    //Instantiate a new intent of PickupActivity
                    Intent pickup_intent = new Intent(this, PickupActivity.class);

                    //Instantiate the bundle object
                    Bundle oBundle_new = new Bundle();

                    //Set the headerID, profileID and settingsID in the bundle
                    oBundle_new.putString("pkHeaderID", _spkHeaderID);
                    oBundle_new.putString("pkProfileID", _spkProfileID);
                    oBundle_new.putString("pkSettingsID", _spkSettingsID);

                    //Setup bundle into intent
                    pickup_intent.putExtras(oBundle_new);

                    //Navigate to the pickup screen
                    startActivity(pickup_intent);

                    //Log message to activity
                    _oUtils.insertActivity(this, "1", "ReceiveActivity", "onOptionsItemSelected", _sUsername, "Receive menu back to pickups selected", "");

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Menu Delete Receive selected
                case R.id.menu_receive_delete:
                    //Log message to activity
                    _oUtils.insertActivity(this, "1", "ReceiveActivity", "onOptionsItemSelected", _sUsername, "Receive menu delete receive selected", "");

                    //Check that the receive screen is not in a new add state
                    if (_iCurrentReceive <= _iTotalReceiveCount)
                    {
                        // Use the Builder class for convenient dialog construction
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);

                        //Build the message
                        builder.setMessage("Are you sure you want to delete this receive?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Get the receiveid from hash map
                                        String sReceiveID = _oAllReceiveIDs.get(_iCurrentReceive);

                                        //Run the deletion process
                                        deleteCurrentReceive(sReceiveID);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Log message
                                        _oUtils.insertActivity(getApplicationContext(), "1", "YesNoDialog", "onCreateDialog", "N/A", "User cancelled the receive deletion process", "");

                                    }
                                });

                        //Show the dialog box
                        AlertDialog aDialog = builder.create();
                        aDialog.show();
                    }
                    else
                    {
                        Toast.makeText(this, "Please move to an existing receive before delete option is selected!", Toast.LENGTH_LONG).show();
                    }

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Logout item selected
                case R.id.menu_receive_logout:
                    //Instantiate a new intent of SignInActivity
                    Intent logout_intent = new Intent(this, SignInActivity.class);

                    //Navigate to the activity log screen
                    startActivity(logout_intent);

                    //Log message to activity
                    _oUtils.insertActivity(this, "1", "ReceiveActivity", "onOptionsItemSelected", _sUsername, "Receive menu logout selected", "");

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
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "onOptionsItemSelected", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
        String sReceiveID;
        
        try
        {
            //Lock the user inputs
            lockUserInputs();

            //Check if the receive save button was pressed
            if (v.getId() == R.id.receive_save_button)
            {
                //Log message to activity
                _oUtils.insertActivity(this, "1", "ReceiveActivity", "onClick", _sUsername, "Receive save button pressed", "");

                //Disable the save button
                _receive_save_button.setEnabled(false);

                //Check for IP and Dump/Hot loads
                bIPCheck = checkIPtoSelectedReceivePlant(_receive_plant.getSelectedItem().toString());
                bDumpHotCheck = checkForDumpOrHot(_receive_plant.getSelectedItem().toString());

                //Check if the plant selected is correct for the current ip location
                if (!bIPCheck)
                {
                    // Use the Builder class for convenient dialog construction
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    //Build the message
                    builder.setMessage("Plant Warning: You are not currently at " + _receive_plant.getSelectedItem().toString() + ".  Are you sure you want to use this receiving plant?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    //Check if the dump or hot selections were selected
                                    if (!bDumpHotCheck)
                                    {
                                        //Run the save process
                                        runSaveProcess();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    //Log message
                                    _oUtils.insertActivity(getApplicationContext(), "1", "YesNoDialog", "onCreateDialog", "N/A", "User cancelled the save process", "");

                                    //Disable the save button
                                    _receive_save_button.setEnabled(true);

                                    //Unlock user inputs
                                    unlockUserInputs();
                                }
                            });

                    //Show the dialog box
                    AlertDialog aDialog = builder.create();
                    aDialog.show();
                }

                //Check if the plant selected is correct for the current ip location
                if (bDumpHotCheck)
                {
                    // Use the Builder class for convenient dialog construction
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    //Build the message
                    builder.setMessage("Plant Warning: You have selected a Dumped or Hot load destination.  Are you sure you want to use a Dumped or Hot Destination?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    //Run the save process
                                    runSaveProcess();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    //Log message
                                    _oUtils.insertActivity(getApplicationContext(), "1", "YesNoDialog", "onCreateDialog", "N/A", "User cancelled the save process", "");

                                    //Disable the save button
                                    _receive_save_button.setEnabled(true);

                                    //Unlock user inputs
                                    unlockUserInputs();
                                }
                            });

                    //Show the dialog box
                    AlertDialog aDialog = builder.create();
                    aDialog.show();
                }
            }
            //Check if the receive finish ticket button was pressed
            else if (v.getId() == R.id.receive_finishticket_button)
            {
                //Log message to activity
                _oUtils.insertActivity(this, "1", "ReceiveActivity", "onClick", _sUsername, "Receive finish ticket button pressed", "");

                //Check if the total receive LBS is the sames as total pickup LBS
                if (getTotalLBSLeftOnTicket() == 0)
                {
                    //Check if this is a waiting for scale data ticket
                    if (checkTicketWaitingForScaleData())
                    {
                        //Instantiate a new intent of SignInActivity
                        Intent signin_intent = new Intent(this, SignInActivity.class);

                        //Navigate to the signin screen
                        startActivity(signin_intent);
                    }
                    else
                    {
                        //Flag header, line and receive records as finished and check status returned
                        if (setTicketAsFinished())
                        {
                            //Instantiate a new intent of SignInActivity
                            Intent signin_intent = new Intent(this, SignInActivity.class);

                            //Navigate to the signin screen
                            startActivity(signin_intent);
                        }
                        else
                        {
                            //Display finished failed message on bottom of screen
                            _receive_Bottom_SaveMessage.setText("Receive finish failed");
                        }
                    }
                }
                else
                {
                    //Display finished failed message on bottom of screen
                    _receive_Bottom_SaveMessage.setText("Receive finish failed because not all pickup LBS are used");
                }
            }
            //Check if the receive previous button was pressed
            else if (v.getId() == R.id.receive_previous_button)
            {
                //Log message to activity
                _oUtils.insertActivity(this, "1", "ReceiveActivity", "onClick", _sUsername, "Receive previous button pressed", "");

                //Check if current receive count is greater than 1
                if (_iCurrentReceive > 1)
                {
                    //Decrement current receive
                    _iCurrentReceive = _iCurrentReceive - 1;

                    //Get the receiveid from hash map
                    sReceiveID = _oAllReceiveIDs.get(_iCurrentReceive);

                    //Load previous receive
                    loadReceive(sReceiveID);

                    //Check if the current receive is 1
                    if (_iCurrentReceive == 1)
                    {
                        //Disable the previous button
                        _receive_previous_button.setEnabled(false);
                    }

                    //Reset the receive message
                    _receive_receivecount_message.setText(_iCurrentReceive + " of " + _iTotalReceiveCount);

                    //Unlock the user inputs
                    unlockUserInputs();
                }

                //Check if current count is less than or equal to total receives
                if (_iCurrentReceive <= _iTotalReceiveCount)
                {
                    //Enable the next button
                    _receive_next_button.setEnabled(true);
                }
            }
            //Check if the receive next button was pressed
            else if (v.getId() == R.id.receive_next_button)
            {
                //Log message to activity
                _oUtils.insertActivity(this, "1", "ReceiveActivity", "onClick", _sUsername, "Receive next button pressed", "");

                //Check if current receive count is less than total receives count
                if (_iCurrentReceive < _iTotalReceiveCount)
                {
                    //Increment current receive
                    _iCurrentReceive = _iCurrentReceive + 1;

                    //Get the receiveid from hash map
                    sReceiveID = _oAllReceiveIDs.get(_iCurrentReceive);

                    //Load next receive
                    loadReceive(sReceiveID);

                    //Reset the receive message
                    _receive_receivecount_message.setText(_iCurrentReceive + " of " + _iTotalReceiveCount);

                    //Check if current count is less than or equal to total receives
                    if (_iCurrentReceive <= _iTotalReceiveCount)
                    {
                        //Enable the previous button
                        _receive_previous_button.setEnabled(true);
                    }

                    //Unlock the user inputs
                    unlockUserInputs();
                }
                //Check if current receive count is equal to total receive count
                else if (_iCurrentReceive == _iTotalReceiveCount)
                {
                    //Incement current counter to set the count greater than the total count by 1 (means new receive)
                    _iCurrentReceive = _iCurrentReceive + 1;

                    //Reset the receive message
                    _receive_receivecount_message.setText("NEW of " + _iTotalReceiveCount);

                    //Clear the screen values
                    clearScreenValues();

                    //Check if current count is less than or equal to total receives
                    if (_iCurrentReceive == _iTotalReceiveCount + 1)
                    {
                        //Enable the previous button
                        _receive_previous_button.setEnabled(true);
                        _receive_next_button.setEnabled(false);
                    }

                    //Check if there are no LBS left on ticket
                    if (getTotalLBSLeftOnTicket() == 0)
                    {
                        //Lock the user inputs
                        lockUserInputs();

                        //Enable the finish ticket and previous button
                        _receive_finishticket_button.setEnabled(true);
                        _receive_previous_button.setEnabled(true);
                    }
                    else
                    {
                        //Unlock the user inputs
                        unlockUserInputs();
                    }

                }
            }
        }
        catch (Exception ex)
        {
            //Unlock the user inputs
            unlockUserInputs();

            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "onClick", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
        Integer iTotalLBS = 0;
        ArrayList<dbReceive> olReceive;
        ArrayList<dbLine> olLine;

        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the profile object from database
            _oProfile = oDBHandler.findProfileByID(_spkProfileID);

            //Get the list of lines and receives for ticket
            olLine = oDBHandler.findLinesByHeaderID(_spkHeaderID);
            olReceive = oDBHandler.findReceivesByHeaderID(_spkHeaderID);

            //Check if there are any receives in array list
            if (olReceive != null)
            {
                //Set the total receives count
                _iTotalReceiveCount = olReceive.size();

                //Loop through the receive records
                for (int i=0; i<olReceive.size(); i++)
                {
                    //Get the receive from list of records
                    dbReceive oReceive = olReceive.get(i);

                    //Add the receiveid to the receive array
                    _oAllReceiveIDs.put(i + 1,oReceive.getPkReceiveID());
                }

                //Set total receives to receive count and current receive to number of receives plus 1 so we know we are adding a new receive
                _iCurrentReceive = olReceive.size() + 1;
                _iTotalReceiveCount = olReceive.size();
            }

            //Setup receive count and buttons
            _receive_receivecount_message.setText("NEW of " + _iTotalReceiveCount);
            _receive_next_button.setEnabled(false);

            //Check if the total receive count is 0
            if (_iTotalReceiveCount == 0)
            {
                //Disable the previous button
                _receive_previous_button.setEnabled(false);
            }
            else
            {
                //Enable the previous button
                _receive_previous_button.setEnabled(true);
            }

            //Get the total LBS left available on ticket
            iTotalLBS = getTotalLBSLeftOnTicket();

            //Display the pickup info on UI
            _receive_ReceiveLBSAvailable.setText("Total LBS Available: " + iTotalLBS);

            //Check if there are still LBS available for receives
            if (iTotalLBS > 0 || (iTotalLBS == 0 && olLine.size() > 0 && getTotalPickupLBS(olLine) == 0))
            {
                //Still LBS available so do not allow finish of the ticket
                _receive_finishticket_button.setEnabled(false);
            }
            else
            {
                //Lock the user inputs
                lockUserInputs();

                //No LBS are available finishing of ticket is ONLY available and previous button
                _receive_finishticket_button.setEnabled(true);
                _receive_previous_button.setEnabled(true);
            }

            //Check if the profile record was found
            if (_oProfile != null)
            {
                //Format the date for insert and modified
                DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                Date dDate = new Date();

                //Set text for the receipt bottom message text view
                _receive_Bottom_Message.setText("Current User: " + _oProfile.getFullName() + " - Current Pickup Date: " + dfDate.format(dDate).toString());
                
                //Set the username global variable
                _sUsername = _oProfile.getUsername();
            }

            //Get the settings object
            dbSettings oSettings = oDBHandler.findSettingsByID(_spkSettingsID);

            //Set the default for the drug test device
            _receive_DrugTestDevice.setText(oSettings.getDrugTestDevice());

            //Setup the hash map dictionary
            _oPlantLookup.put("Pulaski AA", "2");
            _oPlantLookup.put("Pulaski AB", "2");
            _oPlantLookup.put("Glenmore", "3");
            _oPlantLookup.put("Langes Corners", "4");
            _oPlantLookup.put("Chase", "5");
            _oPlantLookup.put("Chase II", "5");
            _oPlantLookup.put("New Denmark", "6");
            _oPlantLookup.put("Byron", "8");
            _oPlantLookup.put("Freedom", "10");
            _oPlantLookup.put("DUMPED - Pulaski AA", "2");
            _oPlantLookup.put("DUMPED - Pulaski AB", "2");
            _oPlantLookup.put("DUMPED - Glenmore", "3");
            _oPlantLookup.put("DUMPED - Langes Corners", "4");
            _oPlantLookup.put("DUMPED - Chase", "5");
            _oPlantLookup.put("DUMPED - Chase II", "5");
            _oPlantLookup.put("DUMPED - New Denmark", "6");
            _oPlantLookup.put("DUMPED - Byron", "8");
            _oPlantLookup.put("DUMPED - Freedom", "10");
            _oPlantLookup.put("HOT - Pulaski AA", "2");
            _oPlantLookup.put("HOT - Pulaski AB", "2");
            _oPlantLookup.put("HOT - Glenmore", "3");
            _oPlantLookup.put("HOT - Langes Corners", "4");
            _oPlantLookup.put("HOT - Chase", "5");
            _oPlantLookup.put("HOT - Chase II", "5");
            _oPlantLookup.put("HOT - New Denmark", "6");
            _oPlantLookup.put("HOT - Byron", "8");
            _oPlantLookup.put("HOT - Freedom", "10");
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "setupScreen", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }

    /**
     * populatePlantSpinner
     *  - Populates the plant spinner with plant values
     */
    private void populatePlantSpinner()
    {
        List<String> olPlants = new ArrayList<>();

        try
        {
            //Instantiate a new database connection object
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the list of active plant records from database
            List<dbPlant> olPlant = oDBHandler.findPlantsActive();

            //Check if plant records were found
            if (olPlant != null)
            {
                //Loop through the plant records
                for (int i=0; i<olPlant.size(); i++)
                {
                    //Get the plant from list of records
                    dbPlant oPlant = olPlant.get(i);

                    //Add the plant name to the plant array
                    olPlants.add(oPlant.getPlantName());
                    _oPlantIDList.add(oPlant.getPkPlantID());
                }
            }
            else
            {
                //No plant records found
                olPlants.add("None Available");
            }

            //Instantiate and setup the array adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, olPlants);

            //Setup the adapter dropdown type
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //Set the spinner to the adapter to fill dropdown
            _receive_plant.setAdapter(adapter);
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "populatePlantSpinner", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }

    /**
     * populateScaleMeterSpinner
     *  - Populates the scalemeter spinner with values
     */
    private void populateScaleMeterSpinner()
    {
        List<String> olScaleMeter = new ArrayList<>();

        try
        {
            //Add the plant name to the scalemeter array
            olScaleMeter.add("Meter");
            olScaleMeter.add("Scale");

            //Instantiate and setup the array adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, olScaleMeter);

            //Setup the adapter dropdown type
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //Set the spinner to the adapter to fill dropdown
            _receive_scalemeter.setAdapter(adapter);
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "populateScaleMeterSpinner", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
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
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "getTotalPickupLBS", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());

            iTotalLBS = 0;
        }

        return iTotalLBS;
    }

    /**
     * getTotalReceiveLBS
     *  - gets the total received lbs currently on ticket
     * @param polReceives
     * @return (Integer) - returns the total LBS of received lbs
     */
    private Integer getTotalReceiveLBS(List<dbReceive> polReceives)
    {
        Integer iTotalLBS = 0;
        dbReceive oReceive;
        
        try
        {
            //Check that the receive list has records
            if (polReceives != null)
            {
                //Loop through all receives on ticket
                for (int i = 0; i < polReceives.size(); i++) 
                {
                    //Instantiate a new receive object
                    oReceive = new dbReceive();

                    //Get the next receive in list
                    oReceive = polReceives.get(i);

                    //Add the LBS to the total LBS
                    iTotalLBS = iTotalLBS + oReceive.getReceivedLBS();
                }
            }
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "getTotalReceiveLBS", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());

            iTotalLBS = 0;
        }

        return iTotalLBS;
    }

    /**
     * runSaveProcess
     *  - Run the save process for the recieve
     */
    private void runSaveProcess()
    {
        try
        {
            //Log activity
            _oUtils.insertActivity(getApplicationContext(), "1", "YesNoDialog", "onCreateDialog", _sUsername, "User selected plant: " + _receive_plant.getSelectedItem().toString() + " but IP was: " + _oUtils.getWiFiIPAddress(getApplicationContext(), _sUsername).toString(), "");

            //Instantiate the total lbs
            Integer iTotalLBS = 0;

            //Instantiate a receive id saved string
            String sReceiveIDSaved;

            //Check if this is an existing receive save
            if (_iCurrentReceive <= _iTotalReceiveCount && _iCurrentReceive > 0)
            {
                //Get the receiveid from hash map
                String sExistingReceiveID = _oAllReceiveIDs.get(_iCurrentReceive);

                //Save the existing receive
                sReceiveIDSaved = saveExistingReceive(sExistingReceiveID);
            }
            else
            {
                //Save the new receive
                sReceiveIDSaved = saveNewReceive();
            }

            //Format the date for insert and modified
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            Date dDate = new Date();

            //Check if the record was saved
            if (sReceiveIDSaved.length() > 0)
            {
                //Clear the screen
                clearScreenValues();

                //Check if this is an existing receive save
                if (_iCurrentReceive <= _iTotalReceiveCount && _iCurrentReceive > 0)
                {
                    //Display update successful message on bottom of screen
                    _receive_Bottom_SaveMessage.setText("Receive updated successfully at: " + dfDate.format(dDate));
                    _receive_Bottom_SaveMessage.setTextColor(ContextCompat.getColor(this, R.color.valueText_Lime));
                }
                else
                {
                    //Display save successful message on bottom of screen
                    _receive_Bottom_SaveMessage.setText("Receive saved successfully at: " + dfDate.format(dDate));
                    _receive_Bottom_SaveMessage.setTextColor(ContextCompat.getColor(this, R.color.valueText_Lime));
                }

                //Get the total LBS left available on ticket
                iTotalLBS = getTotalLBSLeftOnTicket();

                dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);
                List<dbReceive> olReceive;
                olReceive = oDBHandler.findReceivesByHeaderID(_spkHeaderID);
                _oAllReceiveIDs = new HashMap<>();

                if (olReceive.size() > 0)
                {
                    //Loop through the receive records
                    for (int i=0; i<olReceive.size(); i++)
                    {
                        //Get the receive from list of records
                        dbReceive oReceive = olReceive.get(i);

                        //Add the receiveid to the receive array
                        _oAllReceiveIDs.put(i + 1,oReceive.getPkReceiveID());
                    }
                }

                //Display the pickup info on UI
                _receive_ReceiveLBSAvailable.setText("Total LBS Available: " + iTotalLBS);

                //Set total receives to receive count and current receive to number of receives plus 1 so we know we are adding a new receive
                _iCurrentReceive = olReceive.size() + 1;
                _iTotalReceiveCount = olReceive.size();

                //Setup receive count and buttons
                _receive_receivecount_message.setText("NEW of " + _iTotalReceiveCount);

                //Disable the next button
                _receive_next_button.setEnabled(false);

                //Check if the total receive count is 0
                if (_iTotalReceiveCount == 0)
                {
                    //Disable the previous button
                    _receive_previous_button.setEnabled(false);
                }
                else
                {
                    //Enable the previous button
                    _receive_previous_button.setEnabled(true);
                }

                //Check if there are still LBS available for receives
                if (iTotalLBS > 0)
                {
                    //Still LBS available so do not allow finish of the ticket
                    _receive_finishticket_button.setEnabled(false);

                    //Disable the save button
                    _receive_save_button.setEnabled(true);
                }
                else
                {
                    //Lock user inputs
                    lockUserInputs();

                    //No LBS are available finishing of ticket is ONLY available
                    _receive_finishticket_button.setEnabled(true);

                    //Enable the previous button
                    _receive_previous_button.setEnabled(true);
                }

                //Instantiate a new settings object
                dbSettings oSettings;

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
                _receive_Bottom_SaveMessage.setText("Receive saved failed at: " + dfDate.format(dDate));
                _receive_Bottom_SaveMessage.setTextColor(ContextCompat.getColor(this, R.color.valueText_Red));

                //Unlock the user inputs
                unlockUserInputs();
            }
        }
        catch (Exception ex)
        {
            //Format the date for insert and modified
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            Date dDate = new Date();

            //Display save failed message on bottom of screen
            _receive_Bottom_SaveMessage.setText("Receive saved failed at: " + dfDate.format(dDate));
            _receive_Bottom_SaveMessage.setTextColor(ContextCompat.getColor(this, R.color.valueText_Red));

            //Unlock the user inputs
            unlockUserInputs();

            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "runSaveProcess", _sUsername, ex.getMessage(), ex.getStackTrace().toString());
        }
    }

    /**
     * saveNewReceive
     *  - save the new receive record to the database
     * @return (String) - returns the guid of the saved receive record
     */
    private String saveNewReceive()
    {
        String sReceiveID = null;
        dbReceive oReceive = new dbReceive();
        List<dbReceive> olReceive = new ArrayList<>();
        Integer iReceivedLBS = 0;

        try
        {
            //Check if any errors are present for receive
            if (!checkReceiveForErrors())
            {
                //Check if the user left receive LBS fields blank, then assume full load
                if (_receive_ReceivedLBS.getText().toString().isEmpty() && _receive_ReceivedLBSConfirmation.getText().toString().isEmpty())
                {
                    //Get the total LBS left
                    iReceivedLBS = getTotalLBSLeftOnTicket();
                }
                else
                {
                    //Use what the driver entered into field
                    iReceivedLBS = TextUtils.isEmpty(_receive_ReceivedLBS.getText().toString()) ? 0 : Integer.parseInt(_receive_ReceivedLBS.getText().toString());
                }

                //Instantiate the database handler
                dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

                //Create a new headerID GUID
                UUID gID = UUID.randomUUID();

                //Get the plant id selected
                Integer iPosition = _receive_plant.getSelectedItemPosition();
                String sPlantID = _oPlantIDList.get(iPosition);

                //Setup the new receive object data
                oReceive.setPkReceiveID(gID.toString());
                oReceive.setFkHeaderID(_spkHeaderID);
                oReceive.setFkPlantID(sPlantID);
                oReceive.setFkPlantOriginalID("");
                oReceive.setDrugTestDevice(_receive_DrugTestDevice.getText().toString());
                oReceive.setDrugTestResult(_receive_DrugTestResult.getText().toString());
                oReceive.setReceiveDateTime(_oUtils.getFormattedDate(this, _sUsername));
                oReceive.setTank(_receive_Silo.getText().toString());
                oReceive.setScaleMeter(getScaleMeterSpinnerSelection());
                oReceive.setTopSeal(_receive_TopSeal.getText().toString());
                oReceive.setBottomSeal(_receive_BottomSeal.getText().toString());
                oReceive.setReceivedLBS(iReceivedLBS);
                oReceive.setLoadTemp(TextUtils.isEmpty(_receive_Temperature.getText().toString()) ? 0 : Integer.parseInt(_receive_Temperature.getText().toString()));
                oReceive.setIntakeNumber(TextUtils.isEmpty(_receive_IntakeNumber.getText().toString()) ? 0 : Integer.parseInt(_receive_IntakeNumber.getText().toString()));
                oReceive.setFinished(0);
                oReceive.setWaitingForScaleData(0);
                oReceive.setTransmitted(0);
                oReceive.setTransmittedDate(_oUtils.getFormattedDate(this, _sUsername,"1900-01-01 00:00:00.000"));
                oReceive.setInsertDate(_oUtils.getFormattedDate(this, _sUsername));
                oReceive.setModifiedDate(_oUtils.getFormattedDate(this, _sUsername));

                //Setup the arraylist for receive insertion
                olReceive.add(oReceive);

                //Add the receive to the database
                oDBHandler.addReceive(olReceive);

                //Set the return ReceiveID
                sReceiveID = gID.toString();

                //Check if the start mileage was entered
                if (getStartMileage() > 0)
                {
                    //Instantiate a new header record
                    dbHeader oHeader = new dbHeader();

                    //Get the header record
                    oHeader = oDBHandler.findHeaderByID(_spkHeaderID);

                    //Set the end mileage on the header record
                    oHeader.setEndMileage(Integer.parseInt(_receive_EndMileage.getText().toString()));

                    //Calculate the total mileage
                    Integer iStartMileage = oHeader.getStartMileage();
                    Integer iEndMileage = oHeader.getEndMileage();
                    Integer iTotalMileage = iEndMileage - iStartMileage;

                    //Set the total mileage on the header record
                    oHeader.setTotalMileage(iTotalMileage);

                    //Update the header record
                    oDBHandler.updateHeader(oHeader);
                }

                //Log message to activity
                _oUtils.insertActivity(this, "1", "ReceiveActivity", "saveNewReceive", _sUsername, "New Receive Saved:: ID: " + sReceiveID, "");
                _oUtils.insertActivity(this, "1", "ReceiveActivity", "saveNewReceive", _sUsername, "New Receive Saved:: Plant: " + _receive_plant.getSelectedItem() + " DrugTestResult: " + oReceive.getDrugTestResult() + " Scale/Meter: " + oReceive.getScaleMeter() + " Silo: " + oReceive.getTank() + " Load Temp: " + oReceive.getLoadTemp() + " TopSeal: " + oReceive.getTopSeal() + " BottomSeal: " + oReceive.getBottomSeal() + " ReceivedLBS: " + oReceive.getReceivedLBS() + " End Mileage: " + _receive_EndMileage.getText() + " Intake Number: " + oReceive.getIntakeNumber(), "");
            }
        }
        catch(Exception ex)
        {
            //Format the date for insert and modified
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            Date dDate = new Date();

            //Display save failed message on bottom of screen
            _receive_Bottom_SaveMessage.setText("Receive saved failed at: " + dfDate.format(dDate));
            _receive_Bottom_SaveMessage.setTextColor(ContextCompat.getColor(this, R.color.valueText_Red));

            //Unlock the user inputs
            unlockUserInputs();

            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "saveNewReceive", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
        
        return sReceiveID;
    }

    /**
     * saveExistingReceive
     *  - save the existing receive to the database
     * @param pspkReceiveID
     * @return (String) - returns the recieveid
     */
    private String saveExistingReceive(String pspkReceiveID)
    {
        dbReceive oReceive = new dbReceive();
        Integer iReceivedLBS = 0;
        String sReturnID = "";

        try
        {
            //Check if any errors are present for receive
            if (!checkReceiveForErrors())
            {
                //Check if the user left receive LBS fields blank, then assume full load
                if (_receive_ReceivedLBS.getText().toString().isEmpty() && _receive_ReceivedLBSConfirmation.getText().toString().isEmpty())
                {
                    //Get the total LBS left
                    iReceivedLBS = getTotalLBSLeftOnTicket();
                }
                else
                {
                    //Use what the driver entered into field
                    iReceivedLBS = TextUtils.isEmpty(_receive_ReceivedLBS.getText().toString()) ? 0 : Integer.parseInt(_receive_ReceivedLBS.getText().toString());
                }

                //Instantiate the database handler
                dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

                                //Get the plant id selected
                Integer iPosition = _receive_plant.getSelectedItemPosition();
                String sPlantID = _oPlantIDList.get(iPosition);

                //Get the receive data from database
                oReceive = oDBHandler.findReceiveByID(pspkReceiveID);

                //Setup the new receive object data
                oReceive.setPkReceiveID(pspkReceiveID);
                oReceive.setFkHeaderID(_spkHeaderID);
                oReceive.setFkPlantID(sPlantID);
                oReceive.setFkPlantOriginalID("");
                oReceive.setDrugTestDevice(_receive_DrugTestDevice.getText().toString());
                oReceive.setDrugTestResult(_receive_DrugTestResult.getText().toString());
                oReceive.setTank(_receive_Silo.getText().toString());
                oReceive.setScaleMeter(getScaleMeterSpinnerSelection());
                oReceive.setTopSeal(_receive_TopSeal.getText().toString());
                oReceive.setBottomSeal(_receive_BottomSeal.getText().toString());
                oReceive.setReceivedLBS(iReceivedLBS);
                oReceive.setLoadTemp(TextUtils.isEmpty(_receive_Temperature.getText().toString()) ? 0 : Integer.parseInt(_receive_Temperature.getText().toString()));
                oReceive.setIntakeNumber(TextUtils.isEmpty(_receive_IntakeNumber.getText().toString()) ? 0 : Integer.parseInt(_receive_IntakeNumber.getText().toString()));
                oReceive.setFinished(0);
                oReceive.setWaitingForScaleData(0);
                oReceive.setTransmitted(0);
                oReceive.setTransmittedDate(_oUtils.getFormattedDate(this, _sUsername,"1900-01-01 00:00:00.000"));
                oReceive.setModifiedDate(_oUtils.getFormattedDate(this, _sUsername));

                //Update the receive in the database
                oDBHandler.updateReceive(oReceive);

                //Check if the start mileage was entered
                if (getStartMileage() > 0)
                {
                    //Instantiate a new header record
                    dbHeader oHeader = new dbHeader();

                    //Get the header record
                    oHeader = oDBHandler.findHeaderByID(_spkHeaderID);

                    //Set the end mileage on the header record
                    oHeader.setEndMileage(Integer.parseInt(_receive_EndMileage.getText().toString()));

                    //Calculate the total mileage
                    Integer iStartMileage = oHeader.getStartMileage();
                    Integer iEndMileage = oHeader.getEndMileage();
                    Integer iTotalMileage = iEndMileage - iStartMileage;

                    //Set the total mileage on the header record
                    oHeader.setTotalMileage(iTotalMileage);

                    //Update the header record
                    oDBHandler.updateHeader(oHeader);
                }

                //Set the return ID
                sReturnID = pspkReceiveID;

                //Log message to activity
                _oUtils.insertActivity(this, "1", "ReceiveActivity", "saveExistingReceive", _sUsername, "Existing Receive Saved:: ID: " + pspkReceiveID, "");
                _oUtils.insertActivity(this, "1", "ReceiveActivity", "saveExistingReceive", _sUsername, "Existing Receive Saved:: Plant: " + _receive_plant.getSelectedItem() + " DrugTestResult: " + oReceive.getDrugTestResult() + " Scale/Meter: " + oReceive.getScaleMeter() + " Silo: " + oReceive.getTank() + " Load Temp: " + oReceive.getLoadTemp() + " TopSeal: " + oReceive.getTopSeal() + " BottomSeal: " + oReceive.getBottomSeal() + " ReceivedLBS: " + oReceive.getReceivedLBS() + " End Mileage: " + _receive_EndMileage.getText() + " Intake Number: " + oReceive.getIntakeNumber(), "");
            }
        }
        catch(Exception ex)
        {
            //Format the date for insert and modified
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            Date dDate = new Date();

            //Display save failed message on bottom of screen
            _receive_Bottom_SaveMessage.setText("Receive saved failed at: " + dfDate.format(dDate));
            _receive_Bottom_SaveMessage.setTextColor(ContextCompat.getColor(this, R.color.valueText_Red));

            //Unlock the user inputs
            unlockUserInputs();

            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "saveExistingReceive", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }

        return sReturnID;
    }

    /**
     * deleteCurrentReceive
     *  - delete the current receive from the database
     * @param pspkReceiveID
     */
    private void deleteCurrentReceive(String pspkReceiveID)
    {
        dbReceive oReceive;

        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get receive from database
            oReceive = oDBHandler.findReceiveByID(pspkReceiveID);

            //Check if the receive object is populated
            if (oReceive != null)
            {
                //Delete the receive from the database
                oDBHandler.deleteReceiveByID(oReceive.getPkReceiveID());
            }

            //Clear the screen
            clearScreenValues();

            //Run the setup again to get all counters and messages reset
            setupScreen();
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "deleteCurrentReceive", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }

    /**
     * checkReceiveForErrors
     *  - checks all fields for errors
     * @return (boolean) - true/false if errors are present or not
     */
    private boolean checkReceiveForErrors()
    {
        boolean bCheck = false;
        Integer iStartMileage = 0;

        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Check for headerid being blank or null
            if (_spkHeaderID.length() == 0 || _spkHeaderID == null)
            {
                bCheck = true;
            }

            //Check that the drug test device is entered
            if (_receive_DrugTestDevice.getText().length() < 1)
            {
                bCheck = true;

                //Set error for the drug test device
                _receive_DrugTestDevice.setError("Drug Test Device is Required");
            }

            //Check that the drug test result is entered
            if (_receive_DrugTestResult.getText().length() < 4)
            {
                bCheck = true;

                //Set error for the drug test result
                _receive_DrugTestResult.setError("Drug Test Result is Required");
            }

            //Check that the silo is entered
            if (_receive_Silo.getText().length() < 1)
            {
                bCheck = true;

                //Set error for the silo
                _receive_Silo.setError("Silo is required");
            }

            //Check that the load temperature is entered
            if (_receive_Temperature.getText().length() < 1)
            {
                bCheck = true;

                //Set error for the load temperature
                _receive_Temperature.setError("Load Temperature is Required");
            }

            //Check that the top seal is entered
            if (_receive_TopSeal.getText().length() < 1)
            {
                bCheck = true;
                
                //Set error for the top seal
                _receive_TopSeal.setError("Top Seal is Required");
            }

            //Check that the bottom seal is entered
            if (_receive_BottomSeal.getText().length() < 1)
            {
                bCheck = true;

                //Set error for the bottom seal
                _receive_BottomSeal.setError("Bottom Seal is Required");
            }

            //Check receivedlbs against receivedlbsconfirmation
            if (!checkReceivedLBS(_receive_ReceivedLBS.getText().toString(), _receive_ReceivedLBSConfirmation.getText().toString()))
            {
                //Set flag as error found
                bCheck = true;

                //Set error for received LBS not matching
                _receive_ReceivedLBSConfirmation.setError("Received LBS Must Match");
            }

            //Get list of lines from database
            List<dbLine> olLine = oDBHandler.findLinesByHeaderID(_spkHeaderID);

            //Get the LBS for checking
            Integer iEnteredLBS = Integer.parseInt(TextUtils.isEmpty(_receive_ReceivedLBS.getText().toString()) ? "0" : _receive_ReceivedLBS.getText().toString());
            Integer iTotalLBSLeft = getTotalLBSLeftOnTicket();
            Integer iTotalPickupLBSOnTicket = getTotalPickupLBS(olLine);

            if (_iCurrentReceive <= _iTotalReceiveCount && _iCurrentReceive > 0)
            {
                if ((iEnteredLBS + iTotalLBSLeft) > iTotalPickupLBSOnTicket)
                {
                    //Set flag as error
                    bCheck = true;

                    //Set error for received LBS entered greater than available
                    _receive_ReceivedLBS.setError("Received LBS Must Be Less Than OR Equal to Available LBS");
                    _receive_ReceivedLBSConfirmation.setError("Received LBS Must Be Less Than OR Equal to Available LBS");
                }
            }
            else
            {
                //Check if received lbs is greater than what is available
                if ((iEnteredLBS > iTotalLBSLeft && iTotalPickupLBSOnTicket > 0) || (iEnteredLBS > iTotalLBSLeft && iTotalPickupLBSOnTicket == 0)) {
                    //Set flag as error
                    bCheck = true;

                    //Set error for received LBS entered greater than available
                    _receive_ReceivedLBS.setError("Received LBS Must Be Less Than OR Equal to Available LBS");
                    _receive_ReceivedLBSConfirmation.setError("Received LBS Must Be Less Than OR Equal to Available LBS");
                }
            }

            //Get the start mileage
            iStartMileage = getStartMileage();

            //Check if start mileage is greater than 0
            if (iStartMileage > 0)
            {
                //Check if the end mileage is entered
                if (_receive_EndMileage.getText().length() < 1)
                {
                    bCheck = true;

                    //Set error for end mileage required
                    _receive_EndMileage.setError("End Mileage is Required");
                }
                //Check if the end mileage is higher than the start mileage
                else if (Integer.parseInt(_receive_EndMileage.getText().toString()) < iStartMileage)
                {
                    bCheck = true;

                    //Set error for end mileage less than start mileage
                    _receive_EndMileage.setError("End Mileage Must Be Greater Than Start Mileage");
                }
            }
        }
        catch (Exception ex)
        {
            //Unlock the user inputs
            unlockUserInputs();

            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "checkReceiveForErrors", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());

            bCheck = false;
        }

        //Return the check
        return bCheck;
    }

    /**
     * checkIPtoSelectedReceivePlant
     *  - checks to see if the ip address proves the tablet is at correct plant
     * @param psPlant
     * @return (Boolean) - true/false if the comparison is valid
     */
    public boolean checkIPtoSelectedReceivePlant(String psPlant)
    {
        String sIPAddress;
        Boolean bValid = false;
        Integer iFirstDecimal = 0;
        Integer iNextDecimal = 0;
        Integer iFinalDecimal = 0;
        String sPlantCode;
        String sPlantLookup;

        try
        {
            //Get the IP address of the tablet
            sIPAddress = _oUtils.getWiFiIPAddress(this, _sUsername);
            //sIPAddress = "192.168.10.35";  //Freedom = 10

            //Log message to activity
            //_oUtils.insertActivity(this, "1", "ReceiveActivity", "checkIPtoSelectedReceivePlant", _sUsername, "Current IP Address for tablet: " + sIPAddress, "");

            //Find the next decimal point then get the 2nd octet of the ip address
            iFirstDecimal = sIPAddress.indexOf('.',0);
            iNextDecimal = sIPAddress.indexOf('.', iFirstDecimal + 1);
            iFinalDecimal = sIPAddress.indexOf('.',iNextDecimal + 1);

            //Get the plant code from IP address
            sPlantCode = sIPAddress.substring(iNextDecimal + 1, iFinalDecimal);

            //Get the plant code from lookup hash table
            sPlantLookup = _oPlantLookup.get(psPlant);

            //Check if the plant does not have a lookup value
            if (sPlantLookup.trim().equals(""))
            {
                //No lookup value means not our plant
                bValid = true;
            }
            else
            {
                //Check if the plant code in IP matches plant code in lookup table
                if (sPlantCode.equals(sPlantLookup))
                {
                    //Match
                    bValid = true;
                }
            }
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "checkIPtoSelectedReceivePlant", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }

        return bValid;
    }

    /**
     * checkForDumpOrHot
     *  - check to see if the selected plant is a dumped or hot load selection
     * @param psPlant
     * @return (Boolean) - true/false if this is a dumped or hot load selection
     */
    private boolean checkForDumpOrHot(String psPlant)
    {
        boolean bReturn = false;
        String sDump, sHot;

        try
        {
            //Get the section of string that would be dumped or hot
            sDump = psPlant.substring(0,5);
            sHot = psPlant.substring(0,2);

            //Check if the dumped matches
            if (sDump == "DUMPED")
            {
                //This is a dumped load
                bReturn = true;
            }

            //Check if the hot matches
            if (sHot == "HOT")
            {
                //This is a hot load
                bReturn = true;
            }
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "checkForDumpOrHot", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }

        return bReturn;
    }

    /**
     * getScaleMeterSpinnerSelection
     *  - get the scale meter selection and return value
     * @return (Integer) - returns the scale or meter value
     */
    private Integer getScaleMeterSpinnerSelection()
    {
        Integer iScaleMeter = 0;

        try
        {
            //Check if scale was selected
            if (_receive_scalemeter.getSelectedItem().toString() == "Scale")
            {
                //Set the scale meter to 0
                iScaleMeter = 0;
            }
            else
            {
                //Set the scale meter to 1
                iScaleMeter = 1;
            }
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "getScaleMeterSpinnerSelection", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }

        return iScaleMeter;
    }

    /**
     * getTotalLBSLeftOnTicket
     *  - get the total LBS left on ticket TotalPickupLBS - TotalReceiveLBS
     * @return (Integer) - returns the total LBS left on ticket
     */
    private Integer getTotalLBSLeftOnTicket()
    {
        Integer iTotalLBSLeft = 99999;
        List<dbLine> olLine;
        List<dbReceive> olReceive;
        Integer iTotalLBS = 0;
        Integer iTotalReceivedLBS = 0;

        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the list of lines and receives by header id for current ticket
            olLine = oDBHandler.findLinesByHeaderID(_spkHeaderID);
            olReceive = oDBHandler.findReceivesByHeaderID(_spkHeaderID);

            //Get the total LBS on ticket
            iTotalLBS = getTotalPickupLBS(olLine);
            iTotalReceivedLBS = getTotalReceiveLBS(olReceive);
            iTotalLBSLeft = iTotalLBS - iTotalReceivedLBS;
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "getTotalLBSLeftOnTicket", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());

            //Set total LBS to 99999
            iTotalLBSLeft = 99999;
        }

        return iTotalLBSLeft;
    }

    /**
     * setTicketAsFinished
     *  - sets the ticket finished flags in header, line and receive records
     * @return (Boolean) - true/false if the flags are set properly
     */
    private boolean setTicketAsFinished()
    {
        boolean bFinished = false;
        dbHeader oHeader;
        List<dbLine> olLine;
        List<dbReceive> olReceive;

        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the header record
            oHeader = oDBHandler.findHeaderByID(_spkHeaderID);

            //Get the list of lines and receives by header id for current ticket
            olLine = oDBHandler.findLinesByHeaderID(_spkHeaderID);
            olReceive = oDBHandler.findReceivesByHeaderID(_spkHeaderID);

            //Check if the header id is valid
            if (oHeader.getPkHeaderID() != "")
            {
                //Update the finished flag
                oHeader.setFinished(1);

                //Update the header record in database
                oDBHandler.updateHeader(oHeader);
            }

            //Check if lines have been retrieved
            if (olLine.size() > 0)
            {
                //Loop through all lines in list
                for (dbLine oLine : olLine)
                {
                    //Update the finished flag
                    oLine.setFinished(1);

                    //Update the line record in database
                    oDBHandler.updateLine(oLine);
                }
            }

            //Check if receive have been retrieved
            if (olReceive.size() > 0)
            {
                //Loop through all receives in list
                for (dbReceive oReceive : olReceive)
                {
                    //Update the finished flag
                    oReceive.setFinished(1);

                    //Update the receive record in database
                    oDBHandler.updateReceive(oReceive);
                }
            }

            //Set the finished flag
            bFinished = true;
        }
        catch (Exception ex)
        {
            //Unlock the user inputs
            unlockUserInputs();

            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "setTicketAsFinished", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());

            //Set finished flag to false
            bFinished = false;
        }

        return bFinished;
    }

    /**
     * checkReceivedLBS
     *  - checks if the receivedLBS and the receivedLBSConfirm are equal
     * @param psReceivedLBS
     * @param psReceivedLBSConfirm
     * @return (boolean) result of the check if the 2 values are equal
     */
    private boolean checkReceivedLBS(String psReceivedLBS, String psReceivedLBSConfirm)
    {
        boolean bCheck = false;
        double fReceivedLBS, fReceivedLBSConfirm;

        try
        {
            //Set the check to false
            bCheck = false;

            //Check if the receive is set to blank, this means assume full load
            if (psReceivedLBS.isEmpty() && psReceivedLBSConfirm.isEmpty())
            {
                //Set check to valid
                bCheck = true;
            }
            else
            {
                //Try converting the strings to double values
                fReceivedLBS = Double.parseDouble(psReceivedLBS);
                fReceivedLBSConfirm = Double.parseDouble(psReceivedLBSConfirm);

                //Check if the receivedLBS is equal to the receivedLBSConfirm
                if (fReceivedLBS == fReceivedLBSConfirm)
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
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "checkReceivedLBS", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }

        //Return the check result
        return bCheck;
    }

    /**
     * getStartMileage
     *  - get the start mileage from the header record
     * @return (Integer) the start mileage value from the header record
     */
    private Integer getStartMileage()
    {
        Integer iStartMileage = 0;
        dbHeader oHeader = new dbHeader();

        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the header record from the database
            oHeader = oDBHandler.findHeaderByID(_spkHeaderID);

            //Check that the header was received
            if (oHeader != null)
            {
                //Get the start mileage from the header record
                iStartMileage = oHeader.getStartMileage();
            }
            else
            {
                //Set the start mileage to 0
                iStartMileage = 0;
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "getStartMileage", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());

            iStartMileage = 0;
        }

        return iStartMileage;
    }

    /**
     * checkTicketWaitingForScaleData
     *  - checks if the ticket is a waiting for scale data ticket
     * @return (Boolean) - true/false if the ticket is waiting for scale data
     */
    private boolean checkTicketWaitingForScaleData()
    {
        List<dbLine> olLine;
        dbLine oLine;
        List<dbReceive> olReceive;
        dbReceive oReceive;
        dbHeader oHeader;
        boolean bReturn = false;

        try
        {
            //Instantiate a new database connection object
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            olLine = oDBHandler.findLinesByHeaderID(_spkHeaderID);
            olReceive  = oDBHandler.findReceivesByHeaderID(_spkHeaderID);

            if ((getTotalPickupLBS(olLine) == 0) && (getTotalReceiveLBS(olReceive) == 0))
            {
                //Instantiate the header object and get header from database
                oHeader = oDBHandler.findHeaderByID(_spkHeaderID);

                //Set waiting on scale data flag
                oHeader.setWaitingForScaleData(1);

                //Update the header record
                oDBHandler.updateHeader(oHeader);

                //Loop through all of the line records
                for (int i = 0; i < olLine.size(); i++)
                {
                    //Instantiate a new line object
                    oLine = new dbLine();

                    //Get the next line in list
                    oLine = olLine.get(i);

                    //Update the line waiting for scale data flag
                    oLine.setWaitingForScaleData(1);

                    //Update line object in database
                    oDBHandler.updateLine(oLine);
                }

                //Loop through all of the receive records
                for (int j = 0; j < olReceive.size(); j++)
                {
                    //Instantiate a new receive object
                    oReceive = new dbReceive();

                    //Get the next receive in list
                    oReceive = olReceive.get(j);

                    //Update the receive waiting for scale data flag
                    oReceive.setWaitingForScaleData(1);

                    //Update receive object in database
                    oDBHandler.updateReceive(oReceive);
                }

                bReturn = true;
            }
            else
            {
                bReturn = false;
            }

        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "checkTicketWaitingForScaleData", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }

        return bReturn;
    }

    /**
     * clearScreenValues
     *  - clear the screen contents values
     */
    private void clearScreenValues()
    {
        dbSettings oSettings = new dbSettings();

        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the settings from the database
            oSettings = oDBHandler.findSettingsByID(_spkSettingsID);

            //Clear the screen contents values
            _receive_DrugTestDevice.setText(oSettings.getDrugTestDevice());
            _receive_DrugTestResult.setText("");
            _receive_Silo.setText("");
            _receive_Temperature.setText("");
            _receive_TopSeal.setText("");
            _receive_BottomSeal.setText("");
            _receive_ReceivedLBS.setText("");
            _receive_ReceivedLBSConfirmation.setText("");
            _receive_EndMileage.setText("");
            _receive_IntakeNumber.setText("");

            //Reset the selections for drop downs to first selection
            _receive_plant.setSelection(0);
            _receive_scalemeter.setSelection(0);

            //Clear errors
            _receive_DrugTestResult.setError(null);
            _receive_Silo.setError(null);
            _receive_Temperature.setError(null);
            _receive_TopSeal.setError(null);
            _receive_BottomSeal.setError(null);
            _receive_ReceivedLBS.setError(null);
            _receive_ReceivedLBSConfirmation.setError(null);
            _receive_EndMileage.setError(null);

            //Unlock the user inputs
            unlockUserInputs();

            //Set focus to drug test result field
            _receive_DrugTestResult.requestFocus();
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "clearScreenValues", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
            _receive_save_button.setEnabled(false);
            _receive_finishticket_button.setEnabled(false);
            _receive_DrugTestDevice.setEnabled(false);
            _receive_DrugTestResult.setEnabled(false);
            _receive_Silo.setEnabled(false);
            _receive_Temperature.setEnabled(false);
            _receive_TopSeal.setEnabled(false);
            _receive_BottomSeal.setEnabled(false);
            _receive_ReceivedLBS.setEnabled(false);
            _receive_ReceivedLBSConfirmation.setEnabled(false);
            _receive_EndMileage.setEnabled(false);
            _receive_IntakeNumber.setEnabled(false);
            _receive_plant.setEnabled(false);
            _receive_scalemeter.setEnabled(false);
            _receive_previous_button.setEnabled(false);
            _receive_next_button.setEnabled(false);
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "lockUserInputs", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
            //Unlock the user inputs on the screen
            _receive_save_button.setEnabled(true);

            //Check if there are LBS left on ticket yet
            if (getTotalLBSLeftOnTicket() > 0)
            {
                //LBS still available, do not unlock finish button
                _receive_finishticket_button.setEnabled(false);
            }
            else
            {
                //LBS is zero unlock finish button
                _receive_finishticket_button.setEnabled(true);
            }

            //Unlock the user inputs on the screen
            _receive_DrugTestDevice.setEnabled(true);
            _receive_DrugTestResult.setEnabled(true);
            _receive_Silo.setEnabled(true);
            _receive_Temperature.setEnabled(true);
            _receive_TopSeal.setEnabled(true);
            _receive_BottomSeal.setEnabled(true);
            _receive_ReceivedLBS.setEnabled(true);
            _receive_ReceivedLBSConfirmation.setEnabled(true);
            _receive_EndMileage.setEnabled(true);
            _receive_IntakeNumber.setEnabled(true);
            _receive_plant.setEnabled(true);
            _receive_scalemeter.setEnabled(true);

            //Check if current count is less than or equal to total receives
            if (_iCurrentReceive <= _iTotalReceiveCount && _iCurrentReceive > 1)
            {
                //Enable the next and previous buttons
                _receive_next_button.setEnabled(true);
                _receive_previous_button.setEnabled(true);
            }
            else if (_iCurrentReceive == 1)
            {
                _receive_next_button.setEnabled(true);
                _receive_previous_button.setEnabled(false);
            }
            else if (_iCurrentReceive == _iTotalReceiveCount + 1)
            {
                //Enable the previous button
                _receive_previous_button.setEnabled(true);
                _receive_next_button.setEnabled(false);
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "unlockUserInputs", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }

    /**
     * loadReceive
     *  - load the receive of the ticket by ID
     * @param pspkReceiveID
     */
    private void loadReceive(String pspkReceiveID)
    {
        dbReceive oReceive = new dbReceive();
        dbPlant oPlant = new dbPlant();
        dbHeader oHeader = new dbHeader();

        try
        {
            //Clear the screen first
            clearScreenValues();

            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the receive information from the database
            oReceive = oDBHandler.findReceiveByID(pspkReceiveID);

            //Check if the receive object is populated
            if (oReceive != null)
            {
                //Set controls on screen with data
                _receive_DrugTestDevice.setText(oReceive.getDrugTestDevice());
                _receive_DrugTestResult.setText(oReceive.getDrugTestResult());
                _receive_Silo.setText(oReceive.getTank());
                _receive_Temperature.setText(oReceive.getLoadTemp().toString());
                _receive_TopSeal.setText(oReceive.getTopSeal());
                _receive_BottomSeal.setText(oReceive.getBottomSeal());
                _receive_ReceivedLBS.setText(oReceive.getReceivedLBS().toString());
                _receive_ReceivedLBSConfirmation.setText(oReceive.getReceivedLBS().toString());
                _receive_IntakeNumber.setText(oReceive.getIntakeNumber() == 0 ? "" : oReceive.getIntakeNumber().toString());

                //Get the header record to get the end mileage
                oHeader = oDBHandler.findHeaderByID(_spkHeaderID);
                _receive_EndMileage.setText(oHeader.getEndMileage() == 0 ? "" : oHeader.getEndMileage().toString());

                //Get the plant to set the plant spinner
                oPlant = oDBHandler.findPlantByID(oReceive.getFkPlantID());
                _receive_plant.setSelection(((ArrayAdapter)_receive_plant.getAdapter()).getPosition(oPlant.getPlantName()));

                //Check if the scale meter is set to 0 (Meter) or 1 (Scale)
                if (oReceive.getScaleMeter() == 0)
                {
                    _receive_scalemeter.setSelection(1);
                }
                else
                {
                    _receive_scalemeter.setSelection(0);
                }
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiveActivity", "loadReceive", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }
    //endregion
}
