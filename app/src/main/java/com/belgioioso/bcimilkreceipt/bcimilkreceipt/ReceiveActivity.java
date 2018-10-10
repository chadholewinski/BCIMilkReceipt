package com.belgioioso.bcimilkreceipt.bcimilkreceipt;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import java.util.List;
import java.util.UUID;

public class ReceiveActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button _receive_save_button, _receive_finishticket_button;
    private TextView _receive_Bottom_Message, _receive_ReceiveLBSAvailable, _receive_Bottom_SaveMessage;
    private EditText _receive_DrugTestDevice, _receive_DrugTestResult, _receive_Silo, _receive_Temperature, _receive_TopSeal, _receive_BottomSeal, _receive_ReceivedLBS, _receive_ReceivedLBSConfirmation;
    private Spinner _receive_plant, _receive_scalemeter;
    private String _spkSettingsID, _spkProfileID, _spkHeaderID, _sUsername;
    private Utilities _oUtils;
    private dbProfile _oProfile;
    private List<String> _oPlantIDList = new ArrayList<>();

    //region Class Constructor Methods
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        _sUsername = "N/A";
        
        _oUtils = new Utilities();

        //Instantiate the on screen buttons
        _receive_save_button = (Button)findViewById(R.id.receive_save_button);
        _receive_finishticket_button = (Button)findViewById(R.id.receive_finishticket_button);

        //Instantiate the spinners
        _receive_plant = (Spinner)findViewById(R.id.receive_plant);
        _receive_scalemeter = (Spinner)findViewById(R.id.receive_scalemeter);

        //Instantiate the receive bottom message text view
        _receive_Bottom_Message = (TextView)findViewById(R.id.receive_bottom_message);
        _receive_ReceiveLBSAvailable = (TextView)findViewById(R.id.receive_receivelbsavailable);
        _receive_Bottom_SaveMessage = (TextView)findViewById(R.id.receive_bottom_savemessage);

        //Instantiate the receive edit text boxes
        _receive_DrugTestDevice = (EditText)findViewById(R.id.receive_drugtestdevice);
        _receive_DrugTestResult = (EditText)findViewById(R.id.receive_drugtestresult);
        _receive_Silo = (EditText)findViewById(R.id.receive_silo);
        _receive_Temperature = (EditText)findViewById(R.id.receive_temperature);
        _receive_TopSeal = (EditText)findViewById(R.id.receive_topseal);
        _receive_BottomSeal = (EditText)findViewById(R.id.receive_bottomseal);
        _receive_ReceivedLBS = (EditText)findViewById(R.id.receive_receivelbs);
        _receive_ReceivedLBSConfirmation = (EditText)findViewById(R.id.receive_receivelbs_confirm);

        //Set the on click listener for page to the screen buttons
        _receive_save_button.setOnClickListener(this);
        _receive_finishticket_button.setOnClickListener(this);

        //Setup the bundle object
        Bundle oBundle = getIntent().getExtras();

        //Get the header, settings and profile id's from the bundle
        _spkHeaderID = oBundle.getString("pkHeaderID");
        _spkProfileID = oBundle.getString("pkProfileID");
        _spkSettingsID = oBundle.getString("pkSettingsID");

        //Check if the settings id was not passed from receipt page
        if (_spkSettingsID == null || _spkSettingsID.length() < 1)
        {
            //Get the settings id from the database
            _spkSettingsID = findSettings(android.os.Build.SERIAL);
        }

        //Setup the screen
        setupScreen();

        //Load the drop down fields
        populatePlantSpinner();
        populateScaleMeterSpinner();
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

                    //Navigate to the activity log screen
                    startActivity(activity_intent);

                    //Log message to activity
                    _oUtils.InsertActivity(this, "1", "ReceiveActivity", "onOptionsItemSelected", _sUsername, "menu_receive_activity item selected", "");

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Menu BackToPickups selected
                case R.id.menu_receive_backtopickups:
                    //Instantiate a new intent of PickupActivity
                    Intent pickup_intent = new Intent(this, PickupActivity.class);

                    //Navigate to the pickup screen
                    startActivity(pickup_intent);

                    //Log message to activity
                    _oUtils.InsertActivity(this, "1", "ReceiveActivity", "onOptionsItemSelected", _sUsername, "menu_receive_backtopickups item selected", "");

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
                    _oUtils.InsertActivity(this, "1", "ReceiveActivity", "onOptionsItemSelected", _sUsername, "menu_receive_logout item selected", "");

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
            _oUtils.InsertActivity(this, "3", "ReceiveActivity", "onOptionsItemSelected", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
            //Check if the receive save button was pressed
            if (v.getId() == R.id.receive_save_button)
            {
                //Log message to activity
                _oUtils.InsertActivity(this, "1", "ReceiveActivity", "onClick", _sUsername, "receive_save_button pressed", "");

                //Save the pickup
                String sReceiveIDSaved = saveNewReceive();

                //Format the date for insert and modified
                DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                Date dDate = new Date();

                //Check if the record was saved
                if (sReceiveIDSaved.length() > 0)
                {
                    //Clear the screen
                    clearScreenValues();
                    clearScreenColors();

                    //Display save successful message on bottom of screen
                    _receive_Bottom_SaveMessage.setText("Receive saved successfully at: " + dfDate.format(dDate).toString());

                    //Display the pickup info on UI
                    _receive_ReceiveLBSAvailable.setText("Total LBS Available: " + getTotalLBSLeftOnTicket());
                }
                else
                {
                    //Display save failed message on bottom of screen
                    _receive_Bottom_SaveMessage.setText("Receive saved failed at: " + dfDate.format(dDate).toString());
                }
            }
            //Check if the receive finish ticket button was pressed
            else if (v.getId() == R.id.receive_finishticket_button)
            {
                //Log message to activity
                _oUtils.InsertActivity(this, "1", "ReceiveActivity", "onClick", _sUsername, "receive_finishticket_button pressed", "");

                //Check if the total receive LBS is the sames as total pickup LBS
                if (getTotalLBSLeftOnTicket() == 0)
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
                else
                {
                    //Display finished failed message on bottom of screen
                    _receive_Bottom_SaveMessage.setText("Receive finish failed because not all pickup LBS are used");
                }
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "ReceiveActivity", "onClick", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
            _oUtils.InsertActivity(this, "3", "ReceiveActivity", "findSettings", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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

            //Display the pickup info on UI
            _receive_ReceiveLBSAvailable.setText("Total LBS Available: " + getTotalLBSLeftOnTicket());

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
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "ReceiveActivity", "setupScreen", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
            _oUtils.InsertActivity(this, "3", "ReceiveActivity", "populatePlantSpinner", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
            _oUtils.InsertActivity(this, "3", "ReceiveActivity", "populateScaleMeterSpinner", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
                for (int i = 0; i < polLines.size(); i++) {
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
            _oUtils.InsertActivity(this, "3", "ReceiveActivity", "getTotalPickupLBS", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());

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
            _oUtils.InsertActivity(this, "3", "ReceiveActivity", "getTotalReceiveLBS", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());

            iTotalLBS = 0;
        }

        return iTotalLBS;
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

        try
        {
            //Check if any errors are present for receive
            if (!checkReceiveForErrors())
            {
                //Format the date for insert and modified
                DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                Date dDate = new Date();

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
                oReceive.setReceiveDateTime(dfDate.format(dDate).toString());
                oReceive.setTank(_receive_Silo.getText().toString());
                oReceive.setScaleMeter(getScaleMeterSpinnerSelection());
                oReceive.setTopSeal(_receive_TopSeal.getText().toString());
                oReceive.setBottomSeal(_receive_BottomSeal.getText().toString());
                oReceive.setReceivedLBS(Integer.parseInt(_receive_ReceivedLBS.getText().toString()));
                oReceive.setLoadTemp(Integer.parseInt(_receive_Temperature.getText().toString()));
                oReceive.setIntakeNumber(0);
                oReceive.setFinished(0);
                oReceive.setWaitingForScaleData(0);
                oReceive.setTransmitted(0);
                oReceive.setTransmittedDate("1/1/1900");
                oReceive.setInsertDate(dfDate.format(dDate).toString());
                oReceive.setModifiedDate(dfDate.format(dDate).toString());

                //Setup the arraylist for receive insertion
                olReceive.add(oReceive);

                //Add the receive to the database
                oDBHandler.addReceive(olReceive);

                //Set the return ReceiveID
                sReceiveID = gID.toString();
            }
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "ReceiveActivity", "saveNewReceive", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
        
        return sReceiveID;
    }

    /**
     * checkReceiveForErrors
     *  - checks all fields for errors
     * @return (boolean) - true/false if errors are present or not
     */
    private boolean checkReceiveForErrors()
    {
        boolean bCheck = false;

        try
        {
            //Check for headerid being blank or null
            if (_spkHeaderID.length() == 0 || _spkHeaderID == null)
            {
                bCheck = true;
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "ReceiveActivity", "checkReceiveForErrors", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());

            bCheck = false;
        }

        //Return the check
        return bCheck;
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
                //Set the scale meter to 1
                iScaleMeter = 1;
            }
            else
            {
                //Set the scale meter to 0
                iScaleMeter = 0;
            }
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "ReceiveActivity", "getScaleMeterSpinnerSelection", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
            _oUtils.InsertActivity(this, "3", "ReceiveActivity", "getTotalLBSLeftOnTicket", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());

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
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "ReceiveActivity", "setTicketAsFinished", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());

            //Set finished flag to false
            bFinished = false;
        }

        return bFinished;
    }

    /**
     * clearScreenValues
     *  - clear the screen contents values
     */
    private void clearScreenValues()
    {
        try
        {
            //Clear the screen contents values
            _receive_DrugTestDevice.setText("");
            _receive_DrugTestResult.setText("");
            _receive_Silo.setText("");
            _receive_Temperature.setText("");
            _receive_TopSeal.setText("");
            _receive_BottomSeal.setText("");
            _receive_ReceivedLBS.setText("");
            _receive_ReceivedLBSConfirmation.setText("");
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "ReceiveActivity", "clearScreenValues", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }

    /**
     * clearScreenColors
     *  - clear the screen contents colors
     */
    private void clearScreenColors()
    {
        try
        {
            //Set edit text background colors to default
            _receive_ReceivedLBS.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            _receive_ReceivedLBSConfirmation.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "ReceiveActivity", "clearScreenColors", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }
    //endregion
}
