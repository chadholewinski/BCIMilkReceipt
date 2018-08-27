package com.belgioioso.bcimilkreceipt.bcimilkreceipt;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbDatabaseHandler;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbGeoLocation;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbLine;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbProfile;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbSettings;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PickupActivity extends AppCompatActivity implements View.OnClickListener//, LocationListener
{
    private Button _pickup_scanproducer_button, _pickup_scanlabcode_button, _pickup_save_button, _pickup_gotoreceive_button;
    private TextView _pickup_Bottom_Message, _pickup_Bottom_SaveMessage;
    private EditText _pickup_producer, _pickup_tank, _pickup_labcode, _gaugerod_major, _gaugerod_minor, _convertedLBS, _convertedLBS_confirm, _temperature, _dfa_ticket;
    private String _spkSettingsID, _spkProfileID, _spkHeaderID, _sCompany, _sDivision, _sType, _sLatitude, _sLongitude, _sAccurracy, _sProvider, _sUsername;
    //private LocationManager _oLocationManager;
    //private Location _oLocation;
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
        setContentView(R.layout.activity_pickup);

        _sUsername = "N/A";
        
        _oUtils = new Utilities();

        //Instantiate the on screen buttons
        _pickup_scanproducer_button = (Button)findViewById(R.id.pickup_scanproducer_button);
        _pickup_scanlabcode_button = (Button)findViewById(R.id.pickup_scanlabcode_button);
        _pickup_save_button = (Button)findViewById(R.id.pickup_save_button);
        _pickup_gotoreceive_button = (Button)findViewById(R.id.pickup_gotoreceive_button);

        //Instantiate the pickup bottom message and savemessage text view
        _pickup_Bottom_Message = (TextView)findViewById(R.id.pickup_bottom_message);
        _pickup_Bottom_SaveMessage = (TextView)findViewById(R.id.pickup_bottom_savemessage);

        //Instantiate the pickup edit text boxes
        _pickup_producer = (EditText)findViewById(R.id.producer);
        _pickup_tank = (EditText)findViewById(R.id.tank);
        _pickup_labcode = (EditText)findViewById(R.id.labcode);
        _gaugerod_major = (EditText)findViewById(R.id.gaugerodmajor);
        _gaugerod_minor = (EditText)findViewById(R.id.gaugerodminor);
        _convertedLBS = (EditText)findViewById(R.id.convertedlbs);
        _convertedLBS_confirm = (EditText)findViewById(R.id.convertedlbs_confirm);
        _temperature = (EditText)findViewById(R.id.temperature);
        _dfa_ticket = (EditText)findViewById(R.id.dfa_ticket);

        //Set the on click listener for page to the screen buttons
        _pickup_scanproducer_button.setOnClickListener(this);
        _pickup_scanlabcode_button.setOnClickListener(this);
        _pickup_save_button.setOnClickListener(this);
        _pickup_gotoreceive_button.setOnClickListener(this);

        //Setup the bundle object
        Bundle oBundle = getIntent().getExtras();

        //Get the header, settings and profile id's from the bundle
        _spkHeaderID = oBundle.getString("pkHeaderID");
        _spkSettingsID = oBundle.getString("pkSettingsID");
        _spkProfileID = oBundle.getString("pkProfileID");

        //Check if the settings id was not passed from receipt page
        if (_spkSettingsID == null || _spkSettingsID.length() < 1)
        {
            //Get the settings id from the database
            _spkSettingsID = findSettings(android.os.Build.SERIAL);
        }

        //Setup the screen
        setupScreen();

        _sLatitude = "0";
        _sLongitude = "0";
        _sAccurracy = "0";

        //Setup GPS provider
        //setupGPS();
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

//    @Override
//    public void onLocationChanged(Location location)
//    {
//        dbGeoLocation oGeoLocation = new dbGeoLocation();
//
//        try
//        {
//            //Instantiate the database handler
//            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null, 1);
//
//            //Format the date for insert and modified
//            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
//            Date dDate = new Date();
//
//            double lat = location.getLatitude();
//            double lng = location.getLongitude();
//            double acy = location.getAccuracy();
//            String brg = String.valueOf(location.getBearing());
//            double spd = location.getSpeed();
//            double alt = location.getAltitude();
//
//            _sLatitude = String.valueOf(lat);
//            _sLongitude = String.valueOf(lng);
//            _sAccurracy = String.valueOf(acy);
//
//            //Create a new headerID GUID
//            UUID gID = UUID.randomUUID();
//
//            oGeoLocation.setPkGeoLocationID(gID.toString());
//            oGeoLocation.setLatitude(lat);
//            oGeoLocation.setLongitude(lng);
//            oGeoLocation.setAccurracy(acy);
//            oGeoLocation.setHeadingDirection(brg);
//            oGeoLocation.setSpeed(spd);
//            oGeoLocation.setAltitude(alt);
//            oGeoLocation.setTransmitted(0);
//            oGeoLocation.setTransmittedDate("1/1/1900");
//            oGeoLocation.setInsertDate(dfDate.format(dDate).toString());
//
//            oDBHandler.addGeoLocation(oGeoLocation);
//        }
//        catch (Exception ex)
//        {
//            //Log error message to activity
//            _oUtils.InsertActivity(this, "3", "PickupActivity", "onLocationChanged", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
//        }
//    }

//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras)
//    {
//        // TODO Auto-generated method stub
//    }
//
//    @Override
//    public void onProviderEnabled(String provider)
//    {
//        Toast.makeText(PickupActivity.this, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onProviderDisabled(String provider)
//    {
//        Toast.makeText(PickupActivity.this, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
//    {
//        //Check if result code is
//        if (requestCode == 1)
//        {
//            //Check the grantResults as permission granted
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//            {
//                //Display permission granted
//                Toast.makeText(PickupActivity.this, "GPS not granted permissions", Toast.LENGTH_LONG).show();
//            }
//            else
//            {
//                _oLocation = getCurrentLocation();
//            }
//        }
//    }
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

                    //Navigate to the activity log screen
                    startActivity(activity_intent);

                    //Log message to activity
                    _oUtils.InsertActivity(this, "1", "PickupActivity", "onOptionsItemSelected", _sUsername, "menu_pickup_activity item selected", "");

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
                    _oUtils.InsertActivity(this, "1", "PickupActivity", "onOptionsItemSelected", _sUsername, "menu_pickup_logout item selected", "");

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
            _oUtils.InsertActivity(this, "3", "PickupActivity", "onOptionsItemSelected", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
            //Check if the scan producer button was pressed
            if (v.getId() == R.id.pickup_scanproducer_button)
            {
                //Log message to activity
                _oUtils.InsertActivity(this, "1", "PickupActivity", "onClick", _sUsername, "pickup_scanproducer_button pressed", "");

                //Instantiate a new producer scanning intent integrator
                IntentIntegrator scanProducerIntegrator = new IntentIntegrator(this);

                //Set the producer scan intent to initiate scan
                scanProducerIntegrator.initiateScan();
            }
            //Check if the scan labcode button was pressed
            else if (v.getId() == R.id.pickup_scanlabcode_button)
            {
                //Log message to activity
                _oUtils.InsertActivity(this, "1", "PickupActivity", "onClick", _sUsername, "pickup_scanlabcode_button pressed", "");

                //Instantiate a new labcode scanning intent integrator
                IntentIntegrator scanLabCodeIntegrator = new IntentIntegrator(this);

                //Set the labcode scan intent to initiate scan
                scanLabCodeIntegrator.initiateScan();
            }
            //Check if the save pickup button was pressed
            else if (v.getId() == R.id.pickup_save_button)
            {
                //Log message to activity
                _oUtils.InsertActivity(this, "1", "PickupActivity", "onClick", _sUsername, "pickup_save_button pressed", "");

                //Save the pickup
                String sLineIDSaved = saveNewPickup();

                //Format the date for insert and modified
                DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                Date dDate = new Date();

                //Check if the record was saved
                if (sLineIDSaved.length() > 0)
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

                    //Display save successful message on bottom of screen
                    _pickup_Bottom_SaveMessage.setText("Pickup saved successfully at: " + dfDate.format(dDate).toString());
                }
                else
                {
                    //Display save failed message on bottom of screen
                    _pickup_Bottom_SaveMessage.setText("Pickup saved failed at: " + dfDate.format(dDate).toString());
                }
            }
            //Check if the goto receive button was pressed
            else if (v.getId() == R.id.pickup_gotoreceive_button)
            {
                //Log message to activity
                _oUtils.InsertActivity(this, "1", "PickupActivity", "onClick", _sUsername, "pickup_gotoreceive_button pressed", "");

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
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "PickupActivity", "onClick", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
                if (scanContent.length() == 14)
                {
                    //Log message to activity
                    _oUtils.InsertActivity(this, "1", "PickupActivity", "onActivityResult", _sUsername, "Barcode scan found: " + scanContent, "");

                    //Get the parsed values from content
                    _sCompany = scanContent.substring(0,3);
                    _sDivision = scanContent.substring(3,6);
                    String sProducer = scanContent.substring(6,10);
                    String sTank = scanContent.substring(10,12);
                    _sType = scanContent.substring(12,14);

                    //Set the producer and tank edit text fields
                    _pickup_producer.setText(sProducer);
                    _pickup_tank.setText(sTank);
                }
                //Check if the scan content retrieved is 18 characters long
                else if (scanContent.length() == 18)
                {
                    //Set the labcode edit text field
                    _pickup_labcode.setText(scanContent);
                }
            }
            else
            {
                //Display message to user that the barcode was not found or scanned
                Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);

                //Log message to activity
                _oUtils.InsertActivity(this, "1", "PickupActivity", "onActivityResult", _sUsername, "No scan data received!", "");
            }
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "PickupActivity", "onActivityResult", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }

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
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null, 1);

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
                _oUtils.InsertActivity(this, "1", "PickupActivity", "findSettings", _sUsername, "Settings not found, new settings record saved", "");
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
            _oUtils.InsertActivity(this, "3", "PickupActivity", "findSettings", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null, 1);

            //Get the profile object from database
            _oProfile = oDBHandler.findProfileByID(_spkProfileID);

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
            _oUtils.InsertActivity(this, "3", "PickupActivity", "setupScreen", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }

    /**
     * setupGPS
     *  - initialization of GPS services and listeners
     */
    //private void setupGPS()
    //{
    //    boolean bEnabled;

    //    try
    //    {
    //        _oLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

    //        bEnabled = _oLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    //        if (!bEnabled)
    //        {
    //            Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

    //            startActivity(gpsIntent);
    //        }

    //        Criteria oCriteria = new Criteria();
    //        _sProvider = _oLocationManager.getBestProvider(oCriteria, false);

    //        _oLocation = getCurrentLocation();
    //    }
    //    catch(Exception ex)
    //    {
            //Log error message to activity
    //        _oUtils.InsertActivity(this, "3", "PickupActivity", "setupGPS", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
    //    }
    //}

    /**
     * getCurrentLocation
     *  - get the current location from GPS
     * @return (Location) - the current latitude/longitude location
     */
    //private Location getCurrentLocation()
    //{
    //    try
    //    {
    //        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
    //        {

    //            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  }, 1 );
    //        }
    //        else
    //        {
    //            _oLocation = _oLocationManager.getLastKnownLocation(_sProvider);

    //            if (_oLocation != null)
    //            {
    //                onLocationChanged(_oLocation);
    //            }
    //            else
    //            {
    //                _sLatitude = "0";
    //                _sLongitude = "0";
    //                _sAccurracy = "0";
    //            }
    //        }
    //    }
    //    catch(Exception ex)
    //    {
            //Log error message to activity
    //        _oUtils.InsertActivity(this, "3", "PickupActivity", "getCurrentLocation", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
    //    }

    //    return _oLocation;
    //}

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
                //Format the date for insert and modified
                DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                Date dDate = new Date();

                //Instantiate the database handler
                dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null, 1);

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
                oLine.setGaugeRodMajor(Integer.parseInt(_gaugerod_major.getText().toString()));
                oLine.setGaugeRodMinor(Integer.parseInt(_gaugerod_minor.getText().toString()));
                oLine.setConvertedLBS(Integer.parseInt(_convertedLBS.getText().toString()));
                oLine.setTemperature(Double.parseDouble(_temperature.getText().toString()));
                oLine.setPickupDate(dfDate.format(dDate).toString());
                oLine.setDFATicket(_dfa_ticket.getText().toString());
                oLine.setLabCode(_pickup_labcode.getText().toString());
                oLine.setLatitude(Double.parseDouble(_sLatitude));
                oLine.setLongitude(Double.parseDouble(_sLongitude));
                oLine.setAccurracy(Double.parseDouble(_sAccurracy));
                oLine.setFinished(0);
                oLine.setWaitingForScaleData(0);
                oLine.setTransmitted(0);
                oLine.setTransmittedDate("1/1/1900");
                oLine.setInsertDate(dfDate.format(dDate).toString());
                oLine.setModifiedDate(dfDate.format(dDate).toString());

                //Setup the arraylist for line insertion
                olLine.add(oLine);

                //Add the line to the database
                oDBHandler.addLine(olLine);

                //Set the return LineID
                sLineID = gID.toString();
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "PickupActivity", "saveNewPickup", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
        }

        //Return the lineID
        return sLineID;
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
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "PickupActivity", "checkConvertedLBS", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());
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
                bCheck = true;
            }

            //Check for tank being 2 characters
            if (_pickup_tank.getText().toString().length() < 2)
            {
                bCheck = true;
            }

            //Check if this is a pickup scan type
            if (_sType == "PU")
            {
                //Check if the gauge rod major is entered
                if (Integer.parseInt(_gaugerod_major.getText().toString()) < 0)
                {
                    bCheck = true;
                }

                //Check if the gauge rod minor is entered
                if (Integer.parseInt(_gaugerod_minor.getText().toString()) < 0)
                {
                    bCheck = true;
                }
            }

            //Check convertedlbs against convertexlbsconfirmation
            if (!checkConvertedLBS(_convertedLBS.getText().toString(), _convertedLBS_confirm.getText().toString()))
            {
                bCheck = true;
            }

            //Check that the temperature is entered
            if (Double.parseDouble(_temperature.getText().toString()) <= 0)
            {
                bCheck = true;
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.InsertActivity(this, "3", "PickupActivity", "checkPickupForErrors", _sUsername, ex.getMessage().toString(), ex.getStackTrace().toString());

            bCheck = false;
        }

        //Return the check
        return bCheck;
    }
    //endregion
}
