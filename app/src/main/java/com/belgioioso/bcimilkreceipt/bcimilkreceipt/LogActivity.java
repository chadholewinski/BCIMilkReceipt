package com.belgioioso.bcimilkreceipt.bcimilkreceipt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbActivityHeader;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbDatabaseHandler;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbSettings;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class LogActivity extends AppCompatActivity implements View.OnClickListener
{
    private EditText _actlog_StartDate, _actlog_EndDate;
    private Button _actlog_SearchButton;
    private Spinner _actlog_ActType;
    private ListView _actlog_ActivityRecords;
    private TextView _actlog_ActType_Record, _actlog_ActModule_Record, _actlog_ActRoutine_Record, _actlog_ActMessage_Record, _actlog_ActStackTrace_Record;
    private String _spkSettingsID, _spkProfileID, _sUsername;
    private Utilities _oUtils;

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
        _actlog_StartDate = (EditText)findViewById(R.id.actlog_startdate);
        _actlog_EndDate = (EditText)findViewById(R.id.actlog_enddate);

        //Instantiate the on screen buttons
        _actlog_SearchButton = (Button)findViewById(R.id.actlog_searchbutton);

        //Instantiate the on screen spinner
        _actlog_ActType = (Spinner)findViewById(R.id.actlog_acttype);

        //Instantiate the on screen list view
        _actlog_ActivityRecords = (ListView)findViewById(R.id.actlog_activityrecords);

        //Instantiate the on screen text views
        _actlog_ActType_Record = (TextView)findViewById(R.id.actlog_acttype_record);
        _actlog_ActModule_Record = (TextView)findViewById(R.id.actlog_actmodule_record);
        _actlog_ActRoutine_Record = (TextView)findViewById(R.id.actlog_actroutine_record);
        _actlog_ActMessage_Record = (TextView)findViewById(R.id.actlog_actmessage_record);
        _actlog_ActStackTrace_Record = (TextView)findViewById(R.id.actlog_actstacktrace_record);

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
    }

    //region User Methods
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
                _oUtils.InsertActivity(this, "1", "LogActivity", "findSettings", _sUsername, "Settings not found, new settings record saved", "");
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
            _oUtils.InsertActivity(this, "3", "LogActivity", "findSettings", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }

        //Return the settingsID
        return sReturnID;
    }

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
            _oUtils.InsertActivity(this, "3", "LogActivity", "setupScreen", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }

    private void loadActivityList(String psStartDate, String psEndDate, String psType)
    {
        List<dbActivityHeader> olActivity = new ArrayList<>();

        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(psEndDate));
            c.add(Calendar.DATE, 1);
            String sEndDate = sdf.format(c.getTime());

            olActivity = oDBHandler.findActivityByDateType(psStartDate, sEndDate, psType);


        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "LogActivity", "loadActivityList", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }
    //endregion
}
