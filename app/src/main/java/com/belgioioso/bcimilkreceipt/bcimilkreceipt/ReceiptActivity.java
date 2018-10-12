package com.belgioioso.bcimilkreceipt.bcimilkreceipt;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbDatabaseHandler;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbHeader;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbProfile;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbSettings;
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

        _sUsername = "N/A";
        
        _oUtils = new Utilities();

        //Instantiate the new receipt and existing receipt buttons
        _receipt_new_button = (Button)findViewById(R.id.receipt_new_button);
        _receipt_existing_button = (Button)findViewById(R.id.receipt_existing_button);

        //Instantiate the existing tickets spinner
        spn_Existing_Tickets = (Spinner)findViewById(R.id.receipt_existing_spinner);

        //Instantiate the main bottom message text view
        receipt_Bottom_Message = (TextView)findViewById(R.id.receipt_bottom_message);

        //Set the on click listener for page to the new receipt and existing receipt buttons
        _receipt_new_button.setOnClickListener(this);
        _receipt_existing_button.setOnClickListener(this);

        //Setup the bundle object
        Bundle oBundle = getIntent().getExtras();

        //Get the settings and profile id's from the bundle
        _spkSettingsID = oBundle.getString("pkSettingsID");
        _spkProfileID = oBundle.getString("pkProfileID");

        //Check if the settings id was not passed from signin page
        if (_spkSettingsID == null || _spkSettingsID.length() < 1)
        {
            //Get the settings id from the database
            _spkSettingsID = findSettings(android.os.Build.SERIAL);
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
            //Check which items was clicked
            switch (item.getItemId())
            {
                //Menu Activity item selected
                case R.id.menu_receipt_activity:
                    //Instantiate a new intent of LogActivity
                    Intent activity_intent = new Intent(this, LogActivity.class);

                    //Navigate to the activity log screen
                    startActivity(activity_intent);

                    //Log message to activity
                    _oUtils.InsertActivity(this, "1", "ReceiptActivity", "onOptionsItemSelected", _sUsername, "menu_receipt_activity item selected", "");

                    //Set the return value to true
                    bReturn = true;

                    break;

                //Menu Settings item selected
                case R.id.menu_signin_settings:
                    //Instantiate a new intent of SettingsActivity
                    Intent settings_intent = new Intent(this, SettingsActivity.class);

                    //Instantiate the bundle object
                    Bundle oBundle = new Bundle();

                    //Set the profileID and settingsID in the bundle
                    oBundle.putString("pkProfileID", _spkProfileID);
                    oBundle.putString("pkSettingsID", _spkSettingsID);

                    //Setup bundle into intent
                    settings_intent.putExtras(oBundle);

                    //Navigate to the settings screen
                    startActivity(settings_intent);

                    //Log message to activity
                    _oUtils.InsertActivity(this, "1", "ReceiptActivity", "onOptionsItemSelected", "N/A", "menu_receipt_settings item selected", "");

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
                    _oUtils.InsertActivity(this, "1", "ReceiptActivity", "onOptionsItemSelected", "N/A", "menu_receipt_copydb item selected", "");

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
                    _oUtils.InsertActivity(this, "1", "ReceiptActivity", "onOptionsItemSelected", _sUsername, "menu_receipt_logout item selected", "");

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
            _oUtils.InsertActivity(this, "3", "ReceiptActivity", "onOptionsItemSelected", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
            //Check if the new receipt button was pressed
            if (v.getId() == R.id.receipt_new_button)
            {
                //Log message to activity
                _oUtils.InsertActivity(this, "1", "ReceiptActivity", "onClick", _sUsername, "receipt_new_button pressed", "");

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
                _oUtils.InsertActivity(this, "1", "ReceiptActivity", "onClick", _sUsername, "receipt_existing_button pressed", "");

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

                    //Setup bundle into intent
                    gotopickup_intent.putExtras(oBundle);

                    //Navigate to the Pickup Activity page
                    startActivity(gotopickup_intent);
                }
                else
                {
                    //Log message to activity
                    _oUtils.InsertActivity(this, "1", "ReceiptActivity", "onClick", _sUsername, "Existing receipt not found for ticket: " + sTicketNumber, "");
                }
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "ReceiptActivity", "onClick", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
                _oUtils.InsertActivity(this, "1", "ReceiptActivity", "findSettings", _sUsername, "Settings not found, new settings record saved", "");
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
            _oUtils.InsertActivity(this, "3", "ReceiptActivity", "findSettings", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }

        //Return the settingsID
        return sReturnID;
    }

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
            _oUtils.InsertActivity(this, "3", "ReceiptActivity", "findSettings", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
            _oUtils.InsertActivity(this, "3", "ReceiptActivity", "findSettings", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
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
    //endregion
}
