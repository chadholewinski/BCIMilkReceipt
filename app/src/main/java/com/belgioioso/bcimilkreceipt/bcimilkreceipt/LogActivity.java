package com.belgioioso.bcimilkreceipt.bcimilkreceipt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbActivityHeader;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbDatabaseHandler;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbProfile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LogActivity extends AppCompatActivity implements View.OnClickListener
{
    private EditText _actlog_EndDate, _actlog_StartDate;
    private Button _actlog_SearchButton;
    private Spinner _actlog_ActType;
    private ListView _actlog_ActivityRecords;
    private TextView _actlog_ActType_Record, _actlog_ActModule_Record, _actlog_ActRoutine_Record, _actlog_ActMessage_Record, _actlog_ActStackTrace_Record, _actlog_ActUsername_Record, _actlog_ActInsertDate_Record;
    private String _spkSettingsID, _spkProfileID, _sUsername;
    private Utilities _oUtils;
    private List<String> _olActivityHeaderIDs = new ArrayList<>();

    //region Class Constructor Methods
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        //Set the keyboard to not show automatically
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        _sUsername = "N/A";

        _oUtils = new Utilities();

        //Instantiate the on screen edit texts
        _actlog_StartDate = findViewById(R.id.actlog_startdate);
        _actlog_EndDate = findViewById(R.id.actlog_enddate);

        //Instantiate the on screen buttons
        _actlog_SearchButton = findViewById(R.id.actlog_searchbutton);

        //Instantiate the on screen spinner
        _actlog_ActType = findViewById(R.id.actlog_acttype);

        //Instantiate the on screen list view
        _actlog_ActivityRecords = findViewById(R.id.actlog_activityrecords);

        //Instantiate the on screen text views
        _actlog_ActUsername_Record = findViewById(R.id.actlog_actusername_record);
        _actlog_ActType_Record = findViewById(R.id.actlog_acttype_record);
        _actlog_ActModule_Record = findViewById(R.id.actlog_actmodule_record);
        _actlog_ActRoutine_Record = findViewById(R.id.actlog_actroutine_record);
        _actlog_ActMessage_Record = findViewById(R.id.actlog_actmessage_record);
        _actlog_ActStackTrace_Record = findViewById(R.id.actlog_actstacktrace_record);
        _actlog_ActInsertDate_Record = findViewById(R.id.actlog_actinsertdate_record);

        //Set the onclick listener
        _actlog_SearchButton.setOnClickListener(this);

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

        //Setup the activity records list view on click listener
        _actlog_ActivityRecords.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //Instantiate string and activity header object
                String sPkActivityHeaderID;
                dbActivityHeader oActivity = new dbActivityHeader();

                //Instantiate the database handler
                dbDatabaseHandler oDBHandler = new dbDatabaseHandler(view.getContext(), null);

                //Get the activity header ID
                sPkActivityHeaderID = _olActivityHeaderIDs.get(position);

                //Get the activity record from the database
                oActivity = oDBHandler.findActivityByID(sPkActivityHeaderID);

                //Load record to the screen
                _actlog_ActType_Record.setText("Type: " + oActivity.getFkActivityTypeID());
                _actlog_ActModule_Record.setText("Module: " + oActivity.getModule());
                _actlog_ActRoutine_Record.setText("Routine: " + oActivity.getRoutine());
                _actlog_ActMessage_Record.setText("Message: " + oActivity.getMessage());
                _actlog_ActStackTrace_Record.setText("Stack Trace: " + oActivity.getStackTrace());
                _actlog_ActInsertDate_Record.setText("Insert Date: " + oActivity.getInsertDate().toString());

                //Check if the username ID is available
                if (oActivity.getUsername().length() > 10)
                {
                    //Instantiate a new profile object
                    dbProfile oProfile = new dbProfile();

                    //Get the profile from the database by ID
                    oProfile = oDBHandler.findProfileByID(oActivity.getUsername());

                    //Set the username on screen
                    _actlog_ActUsername_Record.setText("Username: " + oProfile.getFullName());
                }
                else
                {
                    //Set the username on screen
                    _actlog_ActUsername_Record.setText("Username: " + oActivity.getUsername());
                }
            }
        });

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

        //Set the menu to the milkreceipt log menu
        inflater.inflate(R.menu.milkreceipt_log_menu, menu);

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
        Toast.makeText(LogActivity.this, "Back button pressed: NOT ALLOWED!", Toast.LENGTH_SHORT).show();
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
                case R.id.menu_log_back:
                    //Instantiate a new intent of Receipt Activity
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
                    _oUtils.insertActivity(this, "1", "LogActivity", "onOptionsItemSelected", "N/A", "Activity Log menu back to Receipt selected", "");

                    //Set the return value to true
                    bReturn = true;

                    break;
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "LogActivity", "onOptionsItemSelected", "N/A", ex.getMessage().toString(), ex.getStackTrace().toString());
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
            //Check if the act log search button was pressed
            if (v.getId() == R.id.actlog_searchbutton)
            {
                String sActivityType;

                //Get the activity type ID based on what was selected in the dropdown list
                if (_actlog_ActType.getSelectedItem() == "Info")
                {
                    sActivityType = "1";
                }
                else if (_actlog_ActType.getSelectedItem() == "Warning")
                {
                    sActivityType = "2";
                }
                else if (_actlog_ActType.getSelectedItem() == "Error")
                {
                    sActivityType = "3";
                }
                else
                {
                    sActivityType = "4";
                }

                //Clear out the activity header id list
                _olActivityHeaderIDs = new ArrayList<>();

                //Clear the list records
                _actlog_ActivityRecords.setAdapter(null);

                //Clear the detail text views
                _actlog_ActUsername_Record.setText("Username: ");
                _actlog_ActType_Record.setText("Type: ");
                _actlog_ActModule_Record.setText("Module: ");
                _actlog_ActRoutine_Record.setText("Routine: ");
                _actlog_ActMessage_Record.setText("Message: ");
                _actlog_ActStackTrace_Record.setText("Stack Trace: ");
                _actlog_ActInsertDate_Record.setText("Insert Date: ");

                //Load the activity to the screen
                loadActivityList(_actlog_StartDate.getText().toString(), _actlog_EndDate.getText().toString(), sActivityType);
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
     *  - setup the screen for user
     */
    private void setupScreen()
    {
        try
        {
            //Format the date for insert and modified
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy");
            Date dDate = new Date();

            //Set the start and end dates
            _actlog_StartDate.setText(dfDate.format(dDate).toString());
            _actlog_EndDate.setText(dfDate.format(dDate).toString());

            //Set the activity type dropdown list
            String[] sTypes = new String[] {"Info", "Warning", "Debug", "Error"};
            ArrayAdapter<String> aArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sTypes);
            aArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            _actlog_ActType.setAdapter(aArray);
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "LogActivity", "setupScreen", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }

    /**
     * loadActivityList
     *  - Loads the activity from the database onto the screen
     * @param psStartDate
     * @param psEndDate
     * @param psType
     */
    private void loadActivityList(String psStartDate, String psEndDate, String psType)
    {
        List<dbActivityHeader> olActivityHeaders = new ArrayList<>();
        List<String> olActivity = new ArrayList<>();
        dbActivityHeader oActivity = new dbActivityHeader();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(psEndDate));
            c.add(Calendar.DATE, 1);
            String sEndDate = sdf.format(c.getTime());

            //Get the list of activity header records from database
            olActivityHeaders = oDBHandler.findActivityByDateType(psStartDate, sEndDate, psType);

            //Check if the activity header list is populated
            if (olActivityHeaders != null)
            {
                //Loop through the activity header records
                for (int i=0; i<olActivityHeaders.size(); i++)
                {
                    oActivity = olActivityHeaders.get(i);

                    olActivity.add(dfDate.format(oActivity.getInsertDate()) + ": " + oActivity.getMessage());
                    _olActivityHeaderIDs.add(oActivity.getPkActivityHeaderID());
                }

                //Setup the array adapter then populate it into the list view on screen
                ArrayAdapter<String> itemsActivity = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, olActivity);
                _actlog_ActivityRecords.setAdapter(itemsActivity);
            }
            else
            {
                //Clear the list view of records
                _actlog_ActivityRecords.setAdapter(null);
            }
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "LogActivity", "loadActivityList", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }
    //endregion
}
