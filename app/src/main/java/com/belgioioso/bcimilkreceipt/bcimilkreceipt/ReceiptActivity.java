package com.belgioioso.bcimilkreceipt.bcimilkreceipt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbDatabaseHandler;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbHeader;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbLine;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbProfile;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbReceive;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReceiptActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button _receipt_new_button, _receipt_existing_button;
    private TextView receipt_Bottom_Message;
    private String _spkSettingsID, _spkProfileID, _sUsername;
    private Spinner spn_Existing_Tickets;
    private Utilities _oUtils;
    private dbProfile _oProfile;
    private List<String> _olTicketIDs = new ArrayList<>();

    //region Class Constructor Methods
    /**
     * onCreate
     *  - Constructor
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        //Set the keyboard to not show automatically
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        _sUsername = "N/A";
        
        _oUtils = new Utilities();

        //Instantiate the new receipt and existing receipt buttons
        _receipt_new_button = findViewById(R.id.receipt_new_button);
        _receipt_existing_button = findViewById(R.id.receipt_existing_button);

        //Instantiate the existing tickets spinner
        spn_Existing_Tickets = findViewById(R.id.receipt_existing_spinner);

        //Instantiate the main bottom message text view
        receipt_Bottom_Message = findViewById(R.id.receipt_bottom_message);

        //Set the on click listener for page to the new receipt and existing receipt buttons
        _receipt_new_button.setOnClickListener(this);
        _receipt_existing_button.setOnClickListener(this);

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

        //Check if the settings id was not passed from signin page
        if (_spkSettingsID == null || _spkSettingsID.length() < 1)
        {
            //Get the settings id from the database
            _spkSettingsID = _oUtils.findSettings(this, _sUsername, android.os.Build.SERIAL);
        }

        //Setup the spinner with existing tickets on tablet
        populateExistingTicketSpinner();

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

        //Set the menu to the milkreceipt receipt menu
        inflater.inflate(R.menu.milkreceipt_receipt_menu, menu);

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
        Toast.makeText(ReceiptActivity.this, "Back button pressed: NOT ALLOWED!", Toast.LENGTH_SHORT).show();
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
            //Instantiate a new database connection object
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the profile from database
            dbProfile oProfile = oDBHandler.findProfileByID(_spkProfileID);

            //Check which items was clicked
            switch (item.getItemId())
            {
                //Menu Activity item selected
                case R.id.menu_receipt_activity:
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
                    _oUtils.insertActivity(this, "1", "ReceiptActivity", "onOptionsItemSelected", _sUsername, "Receipt menu activity log selected", "");

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Menu Settings item selected
                case R.id.menu_receipt_settings:
                    //Check if the profile logged in has admin security
                    if (oProfile.getAdminSecurity() == 1)
                    {
                        //Instantiate a new intent of SettingsActivity
                        Intent settings_intent = new Intent(this, SettingsActivity.class);

                        //Instantiate the bundle object
                        Bundle oSetBundle = new Bundle();

                        //Set the profileID and settingsID in the bundle
                        oSetBundle.putString("pkProfileID", _spkProfileID);
                        oSetBundle.putString("pkSettingsID", _spkSettingsID);

                        //Setup bundle into intent
                        settings_intent.putExtras(oSetBundle);

                        //Navigate to the settings screen
                        startActivity(settings_intent);

                        //Log message to activity
                        _oUtils.insertActivity(this, "1", "ReceiptActivity", "onOptionsItemSelected", "N/A", "Receipt menu settings selected", "");
                    }
                    else
                    {
                        Toast.makeText(this, "Insufficient user privileges for this option!", Toast.LENGTH_LONG).show();
                    }

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Menu delete item selected
                case R.id.menu_receipt_delete:
                    //Check to see if there are existing tickets in the spinner for deletion
                    if (!spn_Existing_Tickets.getAdapter().isEmpty())
                    {
                        //Log message to activity
                        _oUtils.insertActivity(this, "1", "ReceiptActivity", "onOptionsItemSelected", "N/A", "Receipt menu delete existing ticket selected", "");

                        // Use the Builder class for convenient dialog construction
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);

                        //Build the message
                        builder.setMessage("Are you sure you want to delete this ticket?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Run the deletion process
                                        deleteExistingTicket();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Log message
                                        _oUtils.insertActivity(getApplicationContext(), "1", "YesNoDialog", "onCreateDialog", "N/A", "User cancelled the ticket deletion process", "");

                                    }
                                });

                        //Show the dialog box
                        AlertDialog aDialog = builder.create();
                        aDialog.show();
                    }
                    else
                    {
                        Toast.makeText(this, "There are no tickets for deletion!", Toast.LENGTH_LONG).show();
                    }

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Menu CopyDB item selected
                case R.id.menu_receipt_copydb:
                    //Check if the profile logged in has admin security
                    if (oProfile.getAdminSecurity() == 1)
                    {
                        //Check if required permissions are set for external storage
                        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        {
                            //Request the external storage permissions
                            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }
                        else if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        {
                            //Request the external storage permissions
                            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        }
                        else
                        {
                            //Copy the sqlite db file to accessible folder
                            _oUtils.copyDBFile(this, _sUsername);
                        }

                        //Log message to activity
                        _oUtils.insertActivity(this, "1", "ReceiptActivity", "onOptionsItemSelected", "N/A", "Receipt menu copy database selected", "");
                    }
                    else
                    {
                        Toast.makeText(this, "Insufficient user privileges for this option!", Toast.LENGTH_LONG).show();
                    }

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Logout item selected
                case R.id.menu_receipt_logout:
                    //Instantiate a new intent of SignInActivity
                    Intent logout_intent = new Intent(this, SignInActivity.class);

                    //Navigate to the activity log screen
                    startActivity(logout_intent);

                    //Log message to activity
                    _oUtils.insertActivity(this, "1", "ReceiptActivity", "onOptionsItemSelected", _sUsername, "Receipt menu go to SignIn selected", "");

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
            _oUtils.insertActivity(this, "3", "ReceiptActivity", "onOptionsItemSelected", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
            //Lock out the user inputs
            lockUserInputs();

            //Check if the new receipt button was pressed
            if (v.getId() == R.id.receipt_new_button)
            {
               //Log message to activity
                _oUtils.insertActivity(this, "1", "ReceiptActivity", "onClick", _sUsername, "Receipt new receipt button pressed", "");

               //Instantiate a new intent of Main Activity
                Intent new_receipt_intent = new Intent(this, MainActivity.class);

                //Instantiate the bundle object
                Bundle oBundle = new Bundle();

                //Set the profileID and settingsID in the bundle
                oBundle.putString("pkProfileID", _spkProfileID);
                oBundle.putString("pkSettingsID", _spkSettingsID);

                //Setup bundle into intent
                new_receipt_intent.putExtras(oBundle);

                //Navigate to the Main Activity page
                startActivity(new_receipt_intent);
            }
            //Check if the existing receipt button was pressed
            else if (v.getId() == R.id.receipt_existing_button)
            {
                //Log message to activity
                _oUtils.insertActivity(this, "1", "ReceiptActivity", "onClick", _sUsername, "Receipt existing receipt button pressed", "");

                //Get the selected ticket number from spinner
                String sTicketNumber = spn_Existing_Tickets.getSelectedItem().toString();
                Integer iPosition = spn_Existing_Tickets.getSelectedItemPosition();
                String sTicketID = _olTicketIDs.get(iPosition);

                //Instantiate the database handler
                dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

                //Get the settings object from database
                //dbHeader oHeader = oDBHandler.findHeaderByTicketNumber(sTicketNumber);
                dbHeader oHeader = oDBHandler.findHeaderByID(sTicketID);

                //Check if the header record was found
                if (oHeader != null)
                {
                    //Instantiate a new intent of Pickup Activity
                    Intent gotopickup_intent = new Intent(this, PickupActivity.class);

                    //Instantiate the bundle object
                    Bundle oBundle = new Bundle();

                    //Set the headerID, profileID and settingsID in the bundle
                    oBundle.putString("pkHeaderID", oHeader.getPkHeaderID());
                    oBundle.putString("pkProfileID", _spkProfileID);
                    oBundle.putString("pkSettingsID", _spkSettingsID);

                    //Log message to activity
                    _oUtils.insertActivity(this, "1", "ReceiptActivity", "onClick", _sUsername, "Resuming Ticket ID: " + oHeader.getPkHeaderID(), "");

                    //Setup bundle into intent
                    gotopickup_intent.putExtras(oBundle);

                    //Navigate to the Pickup Activity page
                    startActivity(gotopickup_intent);
                }
                else
                {
                    //Log message to activity
                    _oUtils.insertActivity(this, "1", "ReceiptActivity", "onClick", _sUsername, "Existing receipt not found for ticket: " + sTicketNumber, "");
                }
            }
        }
        catch (Exception ex)
        {
            //Unlock the user inputs
            unlockUserInputs();

            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiptActivity", "onClick", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }
    //endregion

    //region Routines
    /**
     * populateExistingTicketSpinner
     * - Fills the spinner with non finished receipt header ticket numbers
     */
    private void populateExistingTicketSpinner()
    {
        List<String> olTickets = new ArrayList<>();
        dbHeader oHeader = new dbHeader();

        try
        {
            //Instantiate a new database connection object
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the list of non finished receipt header records from database
            List<dbHeader> olHeader = oDBHandler.findHeadersNonFinished();

            //Check if receipt header records were found
            if (olHeader != null)
            {
                //Loop through the receipt header records
                for (int i=0; i<olHeader.size(); i++)
                {
                    //Get the receipt header record from list of records
                    oHeader = olHeader.get(i);

                    //Add the ticket number to the ticket array
                    olTickets.add(oHeader.getTicketNumber() + " - " + oHeader.getRouteIdentifier() + " - " + oHeader.getInsertDate());
                    _olTicketIDs.add(oHeader.getPkHeaderID());
                }

                spn_Existing_Tickets.setEnabled(true);
                _receipt_existing_button.setEnabled(true);
            }
            else
            {
                //No receipt header records found
                olTickets.add("None Available");

                spn_Existing_Tickets.setEnabled(false);
                _receipt_existing_button.setEnabled(false);
            }

            //Instantiate and setup the array adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, olTickets);

            //Setup the adapter dropdown type
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //Set the spinner to the adapter to fill dropdown
            spn_Existing_Tickets.setAdapter(adapter);
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiptActivity", "findSettings", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
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
                receipt_Bottom_Message.setText("Current User: " + _oProfile.getFullName() + " - Current Date: " + dfDate.format(dDate).toString());
                
                //Set the username global variable
                _sUsername = _oProfile.getUsername();
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiptActivity", "findSettings", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }

    /**
     * deleteExistingTicket
     *  - Deletes the existing ticket selected from database
     */
    public void deleteExistingTicket()
    {
        dbHeader oHeader = new dbHeader();
        List<dbLine> olLine = new ArrayList<dbLine>();
        dbLine oLine = new dbLine();
        List<dbReceive> olReceive = new ArrayList<dbReceive>();
        dbReceive oReceive = new dbReceive();

        try
        {
            //Instantiate a new database connection object
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Get the selected ticket number from spinner
            Integer iPosition = spn_Existing_Tickets.getSelectedItemPosition();
            String sTicketID = _olTicketIDs.get(iPosition);

            //Log message to activity
            _oUtils.insertActivity(this, "1", "ReceiptActivity", "deleteExistingTicket", _sUsername, "Deleting ticket with ID: " + sTicketID, "");

            oHeader = oDBHandler.findHeaderByID(sTicketID);
            olLine = oDBHandler.findLinesByHeaderID(sTicketID);
            olReceive = oDBHandler.findReceivesByHeaderID(sTicketID);

            //Check if there are any receives to delete
            if (olReceive != null)
            {
                //Loop through all receive records in the list
                for (int i = 0; i < olReceive.size(); i++)
                {
                    //Get the current receive record
                    oReceive = olReceive.get(i);

                    //Log message to activity
                    _oUtils.insertActivity(this, "1", "ReceiptActivity", "deleteExistingTicket", _sUsername, "Deleting ticket Recieve with ID: " + oReceive.getPkReceiveID(), "");

                    //Delete the receive from database
                    oDBHandler.deleteReceiveByID(oReceive.getPkReceiveID());
                }
            }

            //Check if there are any lines to delete
            if (olLine != null)
            {
                //Loop through all line records in the list
                for (int i = 0; i < olLine.size(); i++)
                {
                    //Get the current line record
                    oLine = olLine.get(i);

                    //Log message to activity
                    _oUtils.insertActivity(this, "1", "ReceiptActivity", "deleteExistingTicket", _sUsername, "Deleting ticket Pickup with ID: " + oLine.getPkLineID(), "");

                    //Delete the line from database
                    oDBHandler.deleteLineByID(oLine.getPkLineID());
                }
            }

            //Delete the header from the database
            oDBHandler.deleteHeaderByID(oHeader.getPkHeaderID());

            //Clear ticket list and re-populate the existing ticket spinner
            _olTicketIDs.clear();
            populateExistingTicketSpinner();
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiptActivity", "deleteExistingTicket", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
            _receipt_new_button.setEnabled(false);
            _receipt_existing_button.setEnabled(false);
            spn_Existing_Tickets.setEnabled(false);
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiptActivity", "lockUserInputs", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
            _receipt_new_button.setEnabled(true);
            _receipt_existing_button.setEnabled(true);
            spn_Existing_Tickets.setEnabled(true);
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "ReceiptActivity", "unlockUserInputs", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }
    //endregion
}
