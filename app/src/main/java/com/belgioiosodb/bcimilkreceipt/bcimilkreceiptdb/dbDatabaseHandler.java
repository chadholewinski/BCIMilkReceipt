package com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb;

/**
 * Created by Chad Holewinski on 12/6/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class dbDatabaseHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "MilkReceipt.db";

    //region Class Constructor Methods
    //public dbDatabaseHandler(Context context, SQLiteDatabase.CursorFactory factory, int version)
    public dbDatabaseHandler(Context context, SQLiteDatabase.CursorFactory factory)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    //endregion

    //region Handler Methods
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Create the tables
        db.execSQL(createHeader());
        db.execSQL(createLine());
        db.execSQL(createReceive());
        db.execSQL(createProfile());
        db.execSQL(createSettings());
        db.execSQL(createPlant());
        db.execSQL(createActivityHeader());
        db.execSQL(createVersion());
        db.execSQL(createGeoLocation());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Instantiate new database objects
        dbHeader oHeader = new dbHeader();
        dbLine oLine = new dbLine();
        dbReceive oReceive = new dbReceive();
        dbProfile oProfile = new dbProfile();
        dbSettings oSettings = new dbSettings();
        dbPlant oPlant = new dbPlant();
        dbActivityHeader oActHeader = new dbActivityHeader();
        dbVersion oVersion = new dbVersion();
        dbGeoLocation oGeoLocation = new dbGeoLocation();

        //If table exists, delete the table
        db.execSQL("DROP TABLE IF EXISTS " + oHeader.TABLE_HEADER);
        db.execSQL("DROP TABLE IF EXISTS " + oLine.TABLE_LINE);
        db.execSQL("DROP TABLE IF EXISTS " + oReceive.TABLE_RECEIVE);
        db.execSQL("DROP TABLE IF EXISTS " + oProfile.TABLE_PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + oSettings.TABLE_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + oPlant.TABLE_PLANT);
        db.execSQL("DROP TABLE IF EXISTS " + oActHeader.TABLE_ACTIVITYHEADER);
        db.execSQL("DROP TABLE IF EXISTS " + oVersion.TABLE_VERSION);
        db.execSQL("DROP TABLE IF EXISTS " + oGeoLocation.TABLE_GEOLOCATION);

        //Create the tables
        onCreate(db);
    }
    //endregion

    //region Database Creation Methods
    //createHeader
    // - Creates the header table string for SQLite
    private String createHeader()
    {
        //Instantiate new database header object
        dbHeader oHeader = new dbHeader();

        //Build the header creation table string
        String CREATE_HEADER_TABLE = "CREATE TABLE " + oHeader.TABLE_HEADER + "(" +
                oHeader.HEADER_COLUMN_PKHEADERID + " TEXT PRIMARY KEY, " +
                oHeader.HEADER_COLUMN_FKPROFILEID + " TEXT, " +
                oHeader.HEADER_COLUMN_TICKETNUMBER + " TEXT, " +
                oHeader.HEADER_COLUMN_ROUTEIDENTIFIER + " TEXT, " +
                oHeader.HEADER_COLUMN_TRUCKLICENSENUMBER + " TEXT, " +
                oHeader.HEADER_COLUMN_STARTMILEAGE + " INTEGER, " +
                oHeader.HEADER_COLUMN_ENDMILEAGE + " INTEGER, " +
                oHeader.HEADER_COLUMN_TOTALMILEAGE + " INTEGER, " +
                oHeader.HEADER_COLUMN_FINISHED + " INTEGER, " +
                oHeader.HEADER_COLUMN_WAITINGFORSCALEDATA + " INTEGER, " +
                oHeader.HEADER_COLUMN_TRANSMITTED + " INTEGER, " +
                oHeader.HEADER_COLUMN_TRANSMITTEDDATE + " DATE, " +
                oHeader.HEADER_COLUMN_INSERTDATE + " DATE, " +
                oHeader.HEADER_COLUMN_MODIFIEDDATE + " DATE)";

        //Return the string
        return CREATE_HEADER_TABLE;
    }

    //createLine
    // - Creates the line table string for SQLite
    private String createLine()
    {
        //Instantiate new database line object
        dbLine oLine = new dbLine();

        //Build the line creation table string
        String CREATE_LINE_TABLE = "CREATE TABLE " + oLine.TABLE_LINE + "(" +
                oLine.LINE_COLUMN_PKLINEID + " TEXT PRIMARY KEY, " +
                oLine.LINE_COLUMN_FKHEADERID + " TEXT, " +
                oLine.LINE_COLUMN_TANK + " TEXT, " +
                oLine.LINE_COLUMN_PRODUCER + " TEXT ," +
                oLine.LINE_COLUMN_COMPANY + " TEXT, " +
                oLine.LINE_COLUMN_DIVISION + " TEXT, " +
                oLine.LINE_COLUMN_TYPE + " TEXT, " +
                oLine.LINE_COLUMN_GAUGERODMAJOR + " INTEGER, " +
                oLine.LINE_COLUMN_GAUGERODMINOR + " INTEGER, " +
                oLine.LINE_COLUMN_CONVERTEDLBS + " INTEGER, " +
                oLine.LINE_COLUMN_TEMPERATURE + " INTEGER, " +
                oLine.LINE_COLUMN_PICKUPDATE + " DATE, " +
                oLine.LINE_COLUMN_DFATICKET + " TEXT, " +
                oLine.LINE_COLUMN_LABCODE + " TEXT, " +
                oLine.LINE_COLUMN_LATITUDE + " FLOAT, " +
                oLine.LINE_COLUMN_LONGITUDE + " FLOAT, " +
                oLine.LINE_COLUMN_ACCURRACY + " FLOAT, " +
                oLine.LINE_COLUMN_FINISHED + " INTEGER, " +
                oLine.LINE_COLUMN_WAITINGFORSCALEDATA + " INTEGER, " +
                oLine.LINE_COLUMN_TRANSMITTED + " INTEGER, " +
                oLine.LINE_COLUMN_TRANSMITTEDDATE+ " DATE, " +
                oLine.LINE_COLUMN_INSERTDATE + " DATE, " +
                oLine.LINE_COLUMN_MODIFIEDDATE + " DATE)";

        //Return the string
        return CREATE_LINE_TABLE;
    }

    //createReceive
    // - Creates the receive table string for SQLite
    private String createReceive()
    {
        //Instantiate new database receive object
        dbReceive oReceive = new dbReceive();

        //Build the receive creation table string
        String CREATE_RECEIVE_TABLE = "CREATE TABLE " + oReceive.TABLE_RECEIVE + "(" +
                oReceive.RECEIVE_COLUMN_PKRECEIVEID + " TEXT PRIMARY KEY, " +
                oReceive.RECEIVE_COLUMN_FKHEADERID + " TEXT, " +
                oReceive.RECEIVE_COLUMN_FKPLANTID + " TEXT, " +
                oReceive.RECEIVE_COLUMN_FKPLANTORIGINALID + " TEXT, " +
                oReceive.RECEIVE_COLUMN_DRUGTESTDEVICE + " TEXT, " +
                oReceive.RECEIVE_COLUMN_DRUGTESTRESULT + " TEXT, " +
                oReceive.RECEIVE_COLUMN_RECEIVEDATETIME + " DATE, " +
                oReceive.RECEIVE_COLUMN_TANK + " TEXT, " +
                oReceive.RECEIVE_COLUMN_SCALEMETER + " INTEGER, " +
                oReceive.RECEIVE_COLUMN_TOPSEAL + " TEXT, " +
                oReceive.RECEIVE_COLUMN_BOTTOMSEAL + " TEXT, " +
                oReceive.RECEIVE_COLUMN_RECEIVEDLBS + " INTEGER, " +
                oReceive.RECEIVE_COLUMN_LOADTEMP + " INTEGER, " +
                oReceive.RECEIVE_COLUMN_INTAKENUMBER + " INTEGER, " +
                oReceive.RECEIVE_COLUMN_FINISHED + " INTEGER, " +
                oReceive.RECEIVE_COLUMN_WAITINGFORSCALEDATA + " INTEGER, " +
                oReceive.RECEIVE_COLUMN_TRANSMITTED + " INTEGER, " +
                oReceive.RECEIVE_COLUMN_TRANSMITTEDDATE+ " DATE, " +
                oReceive.RECEIVE_COLUMN_INSERTDATE + " DATE, " +
                oReceive.RECEIVE_COLUMN_MODIFIEDDATE + " DATE)";

        //Return the string
        return CREATE_RECEIVE_TABLE;
    }

    //createProfile
    // - Creates the profile tablet string for SQLite
    private String createProfile()
    {
        //Instantiate new database profile object
        dbProfile oProfile = new dbProfile();

        //Build the profile creation table string
        String CREATE_PROFILE_TABLE = "CREATE TABLE " + oProfile.TABLE_PROFILE + "(" +
                oProfile.PROFILE_COLUMN_PKPROFILEID + " TEXT PRIMARY KEY, " +
                oProfile.PROFILE_COLUMN_FKPLANTID + " TEXT, " +
                oProfile.PROFILE_COLUMN_USERNAME + " TEXT, " +
                oProfile.PROFILE_COLUMN_FULLNAME + " TEXT, " +
                oProfile.PROFILE_COLUMN_INITIALS + " TEXT, " +
                oProfile.PROFILE_COLUMN_PIN + " INTEGER, " +
                oProfile.PROFILE_COLUMN_HAULERSIGNATURE + " TEXT, " +
                oProfile.PROFILE_COLUMN_HAULERLICENSENUMBER + " TEXT, " +
                oProfile.PROFILE_COLUMN_HAULEREXPIRATIONDATE + " TEXT, " +
                oProfile.PROFILE_COLUMN_HAULERNUMBER + " TEXT, " +
                oProfile.PROFILE_COLUMN_SIGNATUREAGREEMENT + " INTEGER, " +
                oProfile.PROFILE_COLUMN_ACTIVE + " INTEGER, " +
                oProfile.PROFILE_COLUMN_ADMINSECURITY + " INTEGER, " +
                oProfile.PROFILE_COLUMN_LASTSIGNINDATE + " DATE, " +
                oProfile.PROFILE_COLUMN_INSERTDATE + " DATE, " +
                oProfile.PROFILE_COLUMN_MODIFIEDDATE + " DATE)";

        //Return the string
        return CREATE_PROFILE_TABLE;
    }

    //createSettings
    // - Creates the settings tablet string for SQLite
    private String createSettings()
    {
        //Instantiate new database settings object
        dbSettings oSettings = new dbSettings();

        //Build the settings creation table string
        String CREATE_SETTINGS_TABLE = "CREATE TABLE " + oSettings.TABLE_SETTINGS + "(" +
                oSettings.SETTINGS_COLUMN_PKSETTINGSID + " TEXT, " +
                oSettings.SETTINGS_COLUMN_TABLETNAME + " TEXT, " +
                oSettings.SETTINGS_COLUMN_MACHINEID + " TEXT, " +
                oSettings.SETTINGS_COLUMN_TRACKPICKUPGEOLOCATION + " INTEGER, " +
                oSettings.SETTINGS_COLUMN_TRACKROUTEGEOLOCATION + " INTEGER, " +
                oSettings.SETTINGS_COLUMN_DEBUG + " INTEGER, " +
                oSettings.SETTINGS_COLUMN_DOWNLOADNOTCOMPLETEDDATA + " INTEGER, " +
                oSettings.SETTINGS_COLUMN_AUTODBBACKUP + " INTEGER, " +
                oSettings.SETTINGS_COLUMN_LASTUSERLOGINID + " TEXT, " +
                oSettings.SETTINGS_COLUMN_LASTUSERLOGINDATE + " DATE, " +
                oSettings.SETTINGS_COLUMN_LASTMILKRECEIPTID + " TEXT, " +
                oSettings.SETTINGS_COLUMN_SCANLOOP + " INTEGER, " +
                oSettings.SETTINGS_COLUMN_LASTSETTINGSUPDATE + " DATE, " +
                oSettings.SETTINGS_COLUMN_LASTPROFILEUPDATE + " DATE, " +
                oSettings.SETTINGS_COLUMN_UPDATEAVAILABLE + " INTEGER, " +
                oSettings.SETTINGS_COLUMN_UPDATEAVAILABLEDATE + " DATE, " +
                oSettings.SETTINGS_COLUMN_DRUGTESTDEVICE + " TEXT, " +
                oSettings.SETTINGS_COLUMN_WEBSERVICEURL+ " TEXT, " +
                oSettings.SETTINGS_COLUMN_INSERTDATE + " DATE, " +
                oSettings.SETTINGS_COLUMN_MODIFIEDDATE + " DATE)";

        //Return the string
        return CREATE_SETTINGS_TABLE;
    }

    //createPlant
    // - Creates the plant tablet string for SQLite
    private String createPlant()
    {
        //Instantiate new database plant object
        dbPlant oPlant = new dbPlant();

        //Build the plant creation table string
        String CREATE_PLANT_TABLE = "CREATE TABLE " + oPlant.TABLE_PLANT + "(" +
                oPlant.PLANT_COLUMN_PKPLANTID + " TEXT PRIMARY KEY, " +
                oPlant.PLANT_COLUMN_PLANTNAME + " TEXT, " +
                oPlant.PLANT_COLUMN_PLANTNUMBER + " TEXT, " +
                oPlant.PLANT_COLUMN_BTUNUMBER + " TEXT, " +
                oPlant.PLANT_COLUMN_ADDRESS + " TEXT, " +
                oPlant.PLANT_COLUMN_CITYSTATEZIP + " TEXT, " +
                oPlant.PLANT_COLUMN_LATITUDE + " FLOAT, " +
                oPlant.PLANT_COLUMN_LONGITUDE + " FLOAT, " +
                oPlant.PLANT_COLUMN_ACTIVE + " INTEGER, " +
                oPlant.PLANT_COLUMN_INSERTDATE + " DATE, " +
                oPlant.PLANT_COLUMN_MODIFIEDDATE + " DATE)";

        //Return the string
        return CREATE_PLANT_TABLE;
    }

    //createActivityHeader
    // - Creates the activity header tablet string for SQLite
    private String createActivityHeader()
    {
        //Instantiate new database activity header object
        dbActivityHeader oActHeader = new dbActivityHeader();

        //Build the activity header creation table string
        String CREATE_ACTHEADER_TABLE = "CREATE TABLE " + oActHeader.TABLE_ACTIVITYHEADER + "(" +
                oActHeader.ACTIVITYHEADER_COLUMN_PKACTIVITYHEADERID + " TEXT PRIMARY KEY, " +
                oActHeader.ACTIVITYHEADER_COLUMN_FKACTIVITYTYPEID + " TEXT, " +
                oActHeader.ACTIVITYHEADER_COLUMN_APPLICATION + " TEXT, " +
                oActHeader.ACTIVITYHEADER_COLUMN_MODULE + " TEXT, " +
                oActHeader.ACTIVITYHEADER_COLUMN_ROUTINE + " TEXT, " +
                oActHeader.ACTIVITYHEADER_COLUMN_USERNAME + " TEXT, " +
                oActHeader.ACTIVITYHEADER_COLUMN_MESSAGE + " TEXT, " +
                oActHeader.ACTIVITYHEADER_COLUMN_STACKTRACE + " TEXT, " +
                oActHeader.ACTIVITYHEADER_COLUMN_TRANSMITTED + " INTEGER, " +
                oActHeader.ACTIVITYHEADER_COLUMN_TRANSMITTEDDATE + " DATE, " +
                oActHeader.ACTIVITYHEADER_COLUMN_INSERTDATE + " DATE)";

        //Return the string
        return CREATE_ACTHEADER_TABLE;
    }

    //createVersion
    // - Creates the version tablet string for SQLite
    private String createVersion()
    {
        //Instantiate new database version object
        dbVersion oVersion = new dbVersion();

        //Build the version creation table string
        String CREATE_VERSION_TABLE = "CREATE TABLE " + oVersion.TABLE_VERSION + "(" +
                oVersion.VERSION_COLUMN_FKSETTINGSID + " TEXT, " +
                oVersion.VERSION_COLUMN_MACHINEID + " TEXT, " +
                oVersion.VERSION_COLUMN_VERSION + " TEXT, " +
                oVersion.VERSION_COLUMN_INSERTDATE + " DATE, " +
                oVersion.VERSION_COLUMN_MODIFIEDDATE + " DATE)";

        //Return the string
        return CREATE_VERSION_TABLE;
    }

    //createGeoLocation
    // - Creates the geo location tablet string for SQLite
    private String createGeoLocation()
    {
        //Instantiate new database geo location object
        dbGeoLocation oGeoLocation = new dbGeoLocation();

        //Build the geo location creation table string
        String CREATE_GEOLOCATION_TABLE = "CREATE TABLE " + oGeoLocation.TABLE_GEOLOCATION + "(" +
                oGeoLocation.GEOLOCATION_COLUMN_PKGEOLOCATIONID + " TEXT PRIMARY KEY, " +
                oGeoLocation.GEOLOCATION_COLUMN_LATITUDE + " FLOAT, " +
                oGeoLocation.GEOLOCATION_COLUMN_LONGITUDE + " FLOAT, " +
                oGeoLocation.GEOLOCATION_COLUMN_ACCURRACY + " FLOAT, " +
                oGeoLocation.GEOLOCATION_COLUMN_HEADINGDIRECTION + " TEXT, " +
                oGeoLocation.GEOLOCATION_COLUMN_SPEED + " FLOAT, " +
                oGeoLocation.GEOLOCATION_COLUMN_ALTITUDE + " FLOAT, " +
                oGeoLocation.GEOLOCATION_COLUMN_TRANSMITTED + " INTEGER, " +
                oGeoLocation.GEOLOCATION_COLUMN_TRANSMITTEDDATE + " DATE, " +
                oGeoLocation.GEOLOCATION_COLUMN_INSERTDATE + " DATE)";

        //Return the string
        return CREATE_GEOLOCATION_TABLE;
    }
    //endregion

    //region Header Methods
    //addHeader
    // - Add header record to SQLite database
    public void addHeader(dbHeader poHeader)
    {
        //Check if header object is null
        if (poHeader != null)
        {
            //Instantiate a content value object
            ContentValues values = new ContentValues();

            //Instantiate a date formatter
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

            //Load content values with column names and values
            values.put(poHeader.HEADER_COLUMN_PKHEADERID, poHeader.getPkHeaderID());
            values.put(poHeader.HEADER_COLUMN_FKPROFILEID, poHeader.getFkProfileID());
            values.put(poHeader.HEADER_COLUMN_TICKETNUMBER, poHeader.getTicketNumber());
            values.put(poHeader.HEADER_COLUMN_ROUTEIDENTIFIER, poHeader.getRouteIdentifier());
            values.put(poHeader.HEADER_COLUMN_TRUCKLICENSENUMBER, poHeader.getTruckLicenseNumber());
            values.put(poHeader.HEADER_COLUMN_STARTMILEAGE, poHeader.getStartMileage());
            values.put(poHeader.HEADER_COLUMN_ENDMILEAGE, poHeader.getEndMileage());
            values.put(poHeader.HEADER_COLUMN_TOTALMILEAGE, poHeader.getTotalMileage());
            values.put(poHeader.HEADER_COLUMN_FINISHED, poHeader.getFinished());
            values.put(poHeader.HEADER_COLUMN_WAITINGFORSCALEDATA, poHeader.getWaitingForScaleData());
            values.put(poHeader.HEADER_COLUMN_TRANSMITTED, poHeader.getTransmitted());
            values.put(poHeader.HEADER_COLUMN_TRANSMITTEDDATE, dfDate.format(poHeader.getTransmittedDate()));
            values.put(poHeader.HEADER_COLUMN_INSERTDATE, dfDate.format(poHeader.getInsertDate()));
            values.put(poHeader.HEADER_COLUMN_MODIFIEDDATE, dfDate.format(poHeader.getModifiedDate()));

            //Instantiate a new db object
            SQLiteDatabase db = this.getWritableDatabase();

            //Insert the header object into database
            db.insert(poHeader.TABLE_HEADER, null, values);

            //Close the database connection
            db.close();
        }
    }

    //updateHeader
    // - Update header record to SQLite database
    public void updateHeader(dbHeader poHeader)
    {
        //Check if header object is null
        if (poHeader != null)
        {
            //Instantiate a content value object
            ContentValues values = new ContentValues();

            //Instantiate a date formatter
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

            //Load content values with column names and values
            values.put(poHeader.HEADER_COLUMN_PKHEADERID, poHeader.getPkHeaderID());
            values.put(poHeader.HEADER_COLUMN_FKPROFILEID, poHeader.getFkProfileID());
            values.put(poHeader.HEADER_COLUMN_TICKETNUMBER, poHeader.getTicketNumber());
            values.put(poHeader.HEADER_COLUMN_ROUTEIDENTIFIER, poHeader.getRouteIdentifier());
            values.put(poHeader.HEADER_COLUMN_TRUCKLICENSENUMBER, poHeader.getTruckLicenseNumber());
            values.put(poHeader.HEADER_COLUMN_STARTMILEAGE, poHeader.getStartMileage());
            values.put(poHeader.HEADER_COLUMN_ENDMILEAGE, poHeader.getEndMileage());
            values.put(poHeader.HEADER_COLUMN_TOTALMILEAGE, poHeader.getTotalMileage());
            values.put(poHeader.HEADER_COLUMN_FINISHED, poHeader.getFinished());
            values.put(poHeader.HEADER_COLUMN_WAITINGFORSCALEDATA, poHeader.getWaitingForScaleData());
            values.put(poHeader.HEADER_COLUMN_TRANSMITTED, poHeader.getTransmitted());
            values.put(poHeader.HEADER_COLUMN_TRANSMITTEDDATE, dfDate.format(poHeader.getTransmittedDate()));
            values.put(poHeader.HEADER_COLUMN_INSERTDATE, dfDate.format(poHeader.getInsertDate()));
            values.put(poHeader.HEADER_COLUMN_MODIFIEDDATE, dfDate.format(poHeader.getModifiedDate()));

            //Instantiate a new db object
            SQLiteDatabase db = this.getWritableDatabase();

            //Update the header object into database
            db.update(poHeader.TABLE_HEADER, values, poHeader.HEADER_COLUMN_PKHEADERID + "= ?", new String[] {poHeader.getPkHeaderID()});

            //Close the database connection
            db.close();
        }
    }

    //findHeaderByID
    // - Get a header record by ID
    public dbHeader findHeaderByID(String psHeaderID)
    {
        String query;
        dbHeader oHeader = new dbHeader();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oHeader.TABLE_HEADER + " WHERE " + oHeader.HEADER_COLUMN_PKHEADERID + " = \"" + psHeaderID + "\"";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            //Get values from database
            oHeader.setPkHeaderID(cursor.getString(0));
            oHeader.setFkProfileID(cursor.getString(1));
            oHeader.setTicketNumber(cursor.getString(2));
            oHeader.setRouteIdentifier(cursor.getString(3));
            oHeader.setTruckLicenseNumber(cursor.getString(4));
            oHeader.setStartMileage(Integer.parseInt(cursor.getString(5)));
            oHeader.setEndMileage(Integer.parseInt(cursor.getString(6)));
            oHeader.setTotalMileage(Integer.parseInt(cursor.getString(7)));
            oHeader.setFinished(Integer.parseInt(cursor.getString(8)));
            oHeader.setWaitingForScaleData(Integer.parseInt(cursor.getString(9)));
            oHeader.setTransmitted(Integer.parseInt(cursor.getString(10)));

            try
            {
                oHeader.setTransmittedDate(dfDate.parse(cursor.getString(11)));
                oHeader.setInsertDate(dfDate.parse(cursor.getString(12)));
                oHeader.setModifiedDate(dfDate.parse(cursor.getString(13)));
            }
            catch(ParseException pe)
            {
                Date dDate = new Date();
                dDate = Calendar.getInstance().getTime();

                oHeader.setTransmittedDate(dDate);
                oHeader.setInsertDate(dDate);
                oHeader.setModifiedDate(dDate);
            }
        }
        else
        {
            //No records found, set header object to null
            oHeader = null;
        }

        //Close the database connection
        db.close();

        //Return the header object
        return oHeader;
    }

    //findHeaderByTicketNumber
    // - Get a header record by ticket number
    public dbHeader findHeaderByTicketNumber(String psTicketNumber)
    {
        String query;
        dbHeader oHeader = new dbHeader();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oHeader.TABLE_HEADER + " WHERE " + oHeader.HEADER_COLUMN_TICKETNUMBER + " = \"" + psTicketNumber + "\"";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            //Get values from database
            oHeader.setPkHeaderID(cursor.getString(0));
            oHeader.setFkProfileID(cursor.getString(1));
            oHeader.setTicketNumber(cursor.getString(2));
            oHeader.setRouteIdentifier(cursor.getString(3));
            oHeader.setTruckLicenseNumber(cursor.getString(4));
            oHeader.setStartMileage(Integer.parseInt(cursor.getString(5)));
            oHeader.setEndMileage(Integer.parseInt(cursor.getString(6)));
            oHeader.setTotalMileage(Integer.parseInt(cursor.getString(7)));
            oHeader.setFinished(Integer.parseInt(cursor.getString(8)));
            oHeader.setWaitingForScaleData(Integer.parseInt(cursor.getString(9)));
            oHeader.setTransmitted(Integer.parseInt(cursor.getString(10)));

            try
            {
                oHeader.setTransmittedDate(dfDate.parse(cursor.getString(11)));
                oHeader.setInsertDate(dfDate.parse(cursor.getString(12)));
                oHeader.setModifiedDate(dfDate.parse(cursor.getString(13)));
            }
            catch(ParseException pe)
            {
                Date dDate = new Date();
                dDate = Calendar.getInstance().getTime();

                oHeader.setTransmittedDate(dDate);
                oHeader.setInsertDate(dDate);
                oHeader.setModifiedDate(dDate);
            }
        }
        else
        {
            //No records found, set header object to null
            oHeader = null;
        }

        //Close the database connection
        db.close();

        //Return the header object
        return oHeader;
    }

    //findHeadersNonFinished
    // - Get list of header objects not finished
    public List<dbHeader> findHeadersNonFinished()
    {
        String query;
        List<dbHeader> olHeader = new ArrayList<>();
        dbHeader oHeader = new dbHeader();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oHeader.TABLE_HEADER + " WHERE " + oHeader.HEADER_COLUMN_FINISHED + " = 0"; //" AND " + oHeader.HEADER_COLUMN_TICKETNUMBER + " = \"\"";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                //Create a fresh instance of the object
                oHeader = new dbHeader();

                //Get values from database
                oHeader.setPkHeaderID(cursor.getString(0));
                oHeader.setFkProfileID(cursor.getString(1));
                oHeader.setTicketNumber(cursor.getString(2));
                oHeader.setRouteIdentifier(cursor.getString(3));
                oHeader.setTruckLicenseNumber(cursor.getString(4));
                oHeader.setStartMileage(Integer.parseInt(cursor.getString(5)));
                oHeader.setEndMileage(Integer.parseInt(cursor.getString(6)));
                oHeader.setTotalMileage(Integer.parseInt(cursor.getString(7)));
                oHeader.setFinished(Integer.parseInt(cursor.getString(8)));
                oHeader.setWaitingForScaleData(Integer.parseInt(cursor.getString(9)));
                oHeader.setTransmitted(Integer.parseInt(cursor.getString(10)));

                try
                {
                    oHeader.setTransmittedDate(dfDate.parse(cursor.getString(11)));
                    oHeader.setInsertDate(dfDate.parse(cursor.getString(12)));
                    oHeader.setModifiedDate(dfDate.parse(cursor.getString(13)));
                }
                catch(ParseException pe)
                {
                    Date dDate = new Date();
                    dDate = Calendar.getInstance().getTime();

                    oHeader.setTransmittedDate(dDate);
                    oHeader.setInsertDate(dDate);
                    oHeader.setModifiedDate(dDate);
                }

                //Add the header object to the header list object
                olHeader.add(oHeader);

                //Move to the next record from database
                cursor.moveToNext();
            }
        }
        else
        {
            //No records found, set header object to null
            olHeader = null;
        }

        //Close the database connection
        db.close();

        //Return the header list object
        return olHeader;
    }

    //findHeadersNonTransmitted
    // - Get list of header objects not transmitted
    public List<dbHeader> findHeadersNonTransmitted()
    {
        String query;
        List<dbHeader> olHeader = new ArrayList<>();
        dbHeader oHeader = new dbHeader();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oHeader.TABLE_HEADER + " WHERE " + oHeader.HEADER_COLUMN_FINISHED + " = 1" + " AND " + oHeader.HEADER_COLUMN_TRANSMITTED + " = 0";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                //Get values from database
                oHeader.setPkHeaderID(cursor.getString(0));
                oHeader.setFkProfileID(cursor.getString(1));
                oHeader.setTicketNumber(cursor.getString(2));
                oHeader.setRouteIdentifier(cursor.getString(3));
                oHeader.setTruckLicenseNumber(cursor.getString(4));
                oHeader.setStartMileage(Integer.parseInt(cursor.getString(5)));
                oHeader.setEndMileage(Integer.parseInt(cursor.getString(6)));
                oHeader.setTotalMileage(Integer.parseInt(cursor.getString(7)));
                oHeader.setFinished(Integer.parseInt(cursor.getString(8)));
                oHeader.setWaitingForScaleData(Integer.parseInt(cursor.getString(9)));
                oHeader.setTransmitted(Integer.parseInt(cursor.getString(10)));

                try
                {
                    oHeader.setTransmittedDate(dfDate.parse(cursor.getString(11)));
                    oHeader.setInsertDate(dfDate.parse(cursor.getString(12)));
                    oHeader.setModifiedDate(dfDate.parse(cursor.getString(13)));
                }
                catch(ParseException pe)
                {
                    Date dDate = new Date();
                    dDate = Calendar.getInstance().getTime();

                    oHeader.setTransmittedDate(dDate);
                    oHeader.setInsertDate(dDate);
                    oHeader.setModifiedDate(dDate);
                }

                //Add the header object to the header list object
                olHeader.add(oHeader);

                //Move to the next record from database
                cursor.moveToNext();
            }
        }
        else
        {
            //No records found, set header object to null
            olHeader = null;
        }

        //Close the database connection
        db.close();

        //Return the header list object
        return olHeader;
    }

    //findHeadersNonTransmittedWaitingOnScale
    // - Get list of header objects not transmitted and waiting on scale data
    public List<dbHeader> findHeadersNonTransmittedWaitingOnScale()
    {
        String query;
        List<dbHeader> olHeader = new ArrayList<>();
        dbHeader oHeader = new dbHeader();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oHeader.TABLE_HEADER + " WHERE " + oHeader.HEADER_COLUMN_FINISHED + " = 0" + " AND " + oHeader.HEADER_COLUMN_TRANSMITTED + " = 0" + " AND " + oHeader.HEADER_COLUMN_WAITINGFORSCALEDATA + " = 1";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                //Get values from database
                oHeader.setPkHeaderID(cursor.getString(0));
                oHeader.setFkProfileID(cursor.getString(1));
                oHeader.setTicketNumber(cursor.getString(2));
                oHeader.setRouteIdentifier(cursor.getString(3));
                oHeader.setTruckLicenseNumber(cursor.getString(4));
                oHeader.setStartMileage(Integer.parseInt(cursor.getString(5)));
                oHeader.setEndMileage(Integer.parseInt(cursor.getString(6)));
                oHeader.setTotalMileage(Integer.parseInt(cursor.getString(7)));
                oHeader.setFinished(Integer.parseInt(cursor.getString(8)));
                oHeader.setWaitingForScaleData(Integer.parseInt(cursor.getString(9)));
                oHeader.setTransmitted(Integer.parseInt(cursor.getString(10)));

                try
                {
                    oHeader.setTransmittedDate(dfDate.parse(cursor.getString(11)));
                    oHeader.setInsertDate(dfDate.parse(cursor.getString(12)));
                    oHeader.setModifiedDate(dfDate.parse(cursor.getString(13)));
                }
                catch(ParseException pe)
                {
                    Date dDate = new Date();
                    dDate = Calendar.getInstance().getTime();

                    oHeader.setTransmittedDate(dDate);
                    oHeader.setInsertDate(dDate);
                    oHeader.setModifiedDate(dDate);
                }

                //Add the header object to the header list object
                olHeader.add(oHeader);

                //Move to the next record from database
                cursor.moveToNext();
            }
        }
        else
        {
            //No records found, set header object to null
            olHeader = null;
        }

        //Close the database connection
        db.close();

        //Return the header list object
        return olHeader;
    }

    //deleteHeaderByID
    // - Delete a header record by ID
    public boolean deleteHeaderByID(String psHeaderID)
    {
        boolean result = false;
        dbHeader oHeader = new dbHeader();
        String query;

        //Create the query string
        query = "SELECT * FROM " + oHeader.TABLE_HEADER + " WHERE " + oHeader.HEADER_COLUMN_PKHEADERID + " =  \"" + psHeaderID + "\"";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Set the ID to the header object
            oHeader.setPkHeaderID(cursor.getString(0));

            //Delete the header record from database
            db.delete(oHeader.TABLE_HEADER, oHeader.HEADER_COLUMN_PKHEADERID + " = ?", new String[] { String.valueOf(oHeader.getPkHeaderID()) });

            //Close the cursor
            cursor.close();

            //Return true
            result = true;
        }

        //Close the database connection
        db.close();

        //Return the result
        return result;
    }

    //deleteHeaderAll
    // - Delete all header records
    public boolean deleteHeaderAll()
    {
        boolean result = false;
        dbHeader oHeader = new dbHeader();

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Delete all header records from database
        db.delete(oHeader.TABLE_HEADER, null, null);

        //Return true
        result = true;

        //Close the database connection
        db.close();

        //Return the result
        return result;
    }
    //endregion

    //region Line Methods
    //addLine
    // - Add line record(s) to SQLite database
    public void addLine(List<dbLine> polLine)
    {
        //Loop through all line objects in array
        for (dbLine oLine : polLine)
        {
            //Instantiate a content value object
            ContentValues values = new ContentValues();

            //Instantiate a date formatter
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

            //Load content values with column names and values
            values.put(oLine.LINE_COLUMN_PKLINEID, oLine.getPkLineID());
            values.put(oLine.LINE_COLUMN_FKHEADERID, oLine.getFkHeaderID());
            values.put(oLine.LINE_COLUMN_TANK, oLine.getTank());
            values.put(oLine.LINE_COLUMN_PRODUCER, oLine.getProducer());
            values.put(oLine.LINE_COLUMN_COMPANY, oLine.getCompany());
            values.put(oLine.LINE_COLUMN_DIVISION, oLine.getDivision());
            values.put(oLine.LINE_COLUMN_TYPE, oLine.getType());
            values.put(oLine.LINE_COLUMN_GAUGERODMAJOR, oLine.getGaugeRodMajor());
            values.put(oLine.LINE_COLUMN_GAUGERODMINOR, oLine.getGaugeRodMinor());
            values.put(oLine.LINE_COLUMN_CONVERTEDLBS, oLine.getConvertedLBS());
            values.put(oLine.LINE_COLUMN_TEMPERATURE, oLine.getTemperature());
            values.put(oLine.LINE_COLUMN_PICKUPDATE, dfDate.format(oLine.getPickupDate()));
            values.put(oLine.LINE_COLUMN_DFATICKET, oLine.getDFATicket());
            values.put(oLine.LINE_COLUMN_LABCODE, oLine.getLabCode());
            values.put(oLine.LINE_COLUMN_LATITUDE, oLine.getLatitude());
            values.put(oLine.LINE_COLUMN_LONGITUDE, oLine.getLongitude());
            values.put(oLine.LINE_COLUMN_ACCURRACY, oLine.getAccurracy());
            values.put(oLine.LINE_COLUMN_FINISHED, oLine.getFinished());
            values.put(oLine.LINE_COLUMN_WAITINGFORSCALEDATA, oLine.getWaitingForScaleData());
            values.put(oLine.LINE_COLUMN_TRANSMITTED, oLine.getTransmitted());
            values.put(oLine.LINE_COLUMN_TRANSMITTEDDATE, dfDate.format(oLine.getTransmittedDate()));
            values.put(oLine.LINE_COLUMN_INSERTDATE, dfDate.format(oLine.getInsertDate()));
            values.put(oLine.LINE_COLUMN_MODIFIEDDATE, dfDate.format(oLine.getModifiedDate()));

            //Instantiate a new db object
            SQLiteDatabase db = this.getWritableDatabase();

            //Insert the line object into database
            db.insert(oLine.TABLE_LINE, null, values);

            //Close the database connection
            db.close();
        }
    }

    //updateLine
    // - update line record(s) to SQLite database
    public void updateLine(dbLine poLine)
    {
        //Check if header object is null
        if (poLine != null)
        {
            //Instantiate a content value object
            ContentValues values = new ContentValues();

            //Instantiate a date formatter
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

            //Load content values with column names and values
            values.put(poLine.LINE_COLUMN_PKLINEID, poLine.getPkLineID());
            values.put(poLine.LINE_COLUMN_FKHEADERID, poLine.getFkHeaderID());
            values.put(poLine.LINE_COLUMN_TANK, poLine.getTank());
            values.put(poLine.LINE_COLUMN_PRODUCER, poLine.getProducer());
            values.put(poLine.LINE_COLUMN_COMPANY, poLine.getCompany());
            values.put(poLine.LINE_COLUMN_DIVISION, poLine.getDivision());
            values.put(poLine.LINE_COLUMN_TYPE, poLine.getType());
            values.put(poLine.LINE_COLUMN_GAUGERODMAJOR, poLine.getGaugeRodMajor());
            values.put(poLine.LINE_COLUMN_GAUGERODMINOR, poLine.getGaugeRodMinor());
            values.put(poLine.LINE_COLUMN_CONVERTEDLBS, poLine.getConvertedLBS());
            values.put(poLine.LINE_COLUMN_TEMPERATURE, poLine.getTemperature());
            values.put(poLine.LINE_COLUMN_PICKUPDATE, dfDate.format(poLine.getPickupDate()));
            values.put(poLine.LINE_COLUMN_DFATICKET, poLine.getDFATicket());
            values.put(poLine.LINE_COLUMN_LABCODE, poLine.getLabCode());
            values.put(poLine.LINE_COLUMN_LATITUDE, poLine.getLatitude());
            values.put(poLine.LINE_COLUMN_LONGITUDE, poLine.getLongitude());
            values.put(poLine.LINE_COLUMN_ACCURRACY, poLine.getAccurracy());
            values.put(poLine.LINE_COLUMN_FINISHED, poLine.getFinished());
            values.put(poLine.LINE_COLUMN_WAITINGFORSCALEDATA, poLine.getWaitingForScaleData());
            values.put(poLine.LINE_COLUMN_TRANSMITTED, poLine.getTransmitted());
            values.put(poLine.LINE_COLUMN_TRANSMITTEDDATE, dfDate.format(poLine.getTransmittedDate()));
            values.put(poLine.LINE_COLUMN_INSERTDATE, dfDate.format(poLine.getInsertDate()));
            values.put(poLine.LINE_COLUMN_MODIFIEDDATE, dfDate.format(poLine.getModifiedDate()));

            //Instantiate a new db object
            SQLiteDatabase db = this.getWritableDatabase();

            //Update the line object into database
            db.update(poLine.TABLE_LINE, values, poLine.LINE_COLUMN_PKLINEID + "= ?", new String[] {poLine.getPkLineID()});

            //Close the database connection
            db.close();
        }
    }

    //findLineByID
    // - Get a line record by ID
    public dbLine findLineByID(String psLineID)
    {
        String query;
        dbLine oLine = new dbLine();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oLine.TABLE_LINE + " WHERE " + oLine.LINE_COLUMN_PKLINEID + " = \"" + psLineID + "\"";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            //Get values from database
            oLine.setPkLineID(cursor.getString(0));
            oLine.setFkHeaderID(cursor.getString(1));
            oLine.setTank(cursor.getString(2));
            oLine.setProducer(cursor.getString(3));
            oLine.setCompany(cursor.getString(4));
            oLine.setDivision(cursor.getString(5));
            oLine.setType(cursor.getString(6));
            oLine.setGaugeRodMajor(Integer.parseInt(cursor.getString(7)));
            oLine.setGaugeRodMinor(Integer.parseInt(cursor.getString(8)));
            oLine.setConvertedLBS(Integer.parseInt(cursor.getString(9)));
            oLine.setTemperature(Integer.parseInt(cursor.getString(10)));
            oLine.setDFATicket(cursor.getString(12));
            oLine.setLabCode(cursor.getString(13));
            oLine.setLatitude(Double.parseDouble(cursor.getString(14)));
            oLine.setLongitude(Double.parseDouble(cursor.getString(15)));
            oLine.setAccurracy(Double.parseDouble(cursor.getString(16)));
            oLine.setFinished(Integer.parseInt(cursor.getString(17)));
            oLine.setWaitingForScaleData(Integer.parseInt(cursor.getString(18)));
            oLine.setTransmitted(Integer.parseInt(cursor.getString(19)));

            try
            {
                oLine.setPickupDate(dfDate.parse(cursor.getString(11)));
                oLine.setTransmittedDate(dfDate.parse(cursor.getString(20)));
                oLine.setInsertDate(dfDate.parse(cursor.getString(21)));
                oLine.setModifiedDate(dfDate.parse(cursor.getString(22)));
            }
            catch(ParseException pe)
            {
                Date dDate = new Date();
                dDate = Calendar.getInstance().getTime();

                oLine.setPickupDate(dDate);
                oLine.setTransmittedDate(dDate);
                oLine.setInsertDate(dDate);
                oLine.setModifiedDate(dDate);
            }
        }
        else
        {
            //No records found, set line object to null
            oLine = null;
        }

        //Close the database connection
        db.close();

        //Return the line object
        return oLine;
    }

    //findLinesByHeaderID
    // - Get a line records by HeaderID
    public ArrayList<dbLine> findLinesByHeaderID(String psHeaderID)
    {
        String query;
        ArrayList<dbLine> oLines = new ArrayList<dbLine>();
        dbLine oLine = new dbLine();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oLine.TABLE_LINE + " WHERE " + oLine.LINE_COLUMN_FKHEADERID + " = \"" + psHeaderID + "\" ORDER BY " + oLine.LINE_COLUMN_INSERTDATE + " ASC";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                //Instantiate new line object
                oLine = new dbLine();

                //Get values from database
                oLine.setPkLineID(cursor.getString(0));
                oLine.setFkHeaderID(cursor.getString(1));
                oLine.setTank(cursor.getString(2));
                oLine.setProducer(cursor.getString(3));
                oLine.setCompany(cursor.getString(4));
                oLine.setDivision(cursor.getString(5));
                oLine.setType(cursor.getString(6));
                oLine.setGaugeRodMajor(Integer.parseInt(cursor.getString(7)));
                oLine.setGaugeRodMinor(Integer.parseInt(cursor.getString(8)));
                oLine.setConvertedLBS(Integer.parseInt(cursor.getString(9)));
                oLine.setTemperature(Integer.parseInt(cursor.getString(10)));
                oLine.setDFATicket(cursor.getString(12));
                oLine.setLabCode(cursor.getString(13));
                oLine.setLatitude(Double.parseDouble(cursor.getString(14)));
                oLine.setLongitude(Double.parseDouble(cursor.getString(15)));
                oLine.setAccurracy(Double.parseDouble(cursor.getString(16)));
                oLine.setFinished(Integer.parseInt(cursor.getString(17)));
                oLine.setWaitingForScaleData(Integer.parseInt(cursor.getString(18)));
                oLine.setTransmitted(Integer.parseInt(cursor.getString(19)));

                try
                {
                    oLine.setPickupDate(dfDate.parse(cursor.getString(11)));
                    oLine.setTransmittedDate(dfDate.parse(cursor.getString(20)));
                    oLine.setInsertDate(dfDate.parse(cursor.getString(21)));
                    oLine.setModifiedDate(dfDate.parse(cursor.getString(22)));
                }
                catch(ParseException pe)
                {
                    Date dDate = new Date();
                    dDate = Calendar.getInstance().getTime();

                    oLine.setPickupDate(dDate);
                    oLine.setTransmittedDate(dDate);
                    oLine.setInsertDate(dDate);
                    oLine.setModifiedDate(dDate);
                }

                //Add line object to array of line objects
                oLines.add(oLine);

                //Move to the next record in cursor
                cursor.moveToNext();
            }
        }
        else
        {
            //No records found, set line array object to null
            oLines = null;
        }

        //Close the database connection
        db.close();

        //Return the line array object
        return oLines;
    }

    //findLinesNonTransmitted
    // - Get list of line objects not transmitted
    public List<dbLine> findLinesNonTransmitted()
    {
        String query;
        List<dbLine> olLine = new ArrayList<>();
        dbLine oLine = new dbLine();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oLine.TABLE_LINE + " WHERE " + oLine.LINE_COLUMN_FINISHED + " = 1" + " AND " + oLine.LINE_COLUMN_TRANSMITTED + " = 0";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                //Instantiate new line object
                oLine = new dbLine();

                //Get values from database
                oLine.setPkLineID(cursor.getString(0));
                oLine.setFkHeaderID(cursor.getString(1));
                oLine.setTank(cursor.getString(2));
                oLine.setProducer(cursor.getString(3));
                oLine.setCompany(cursor.getString(4));
                oLine.setDivision(cursor.getString(5));
                oLine.setType(cursor.getString(6));
                oLine.setGaugeRodMajor(Integer.parseInt(cursor.getString(7)));
                oLine.setGaugeRodMinor(Integer.parseInt(cursor.getString(8)));
                oLine.setConvertedLBS(Integer.parseInt(cursor.getString(9)));
                oLine.setTemperature(Integer.parseInt(cursor.getString(10)));
                oLine.setDFATicket(cursor.getString(12));
                oLine.setLabCode(cursor.getString(13));
                oLine.setLatitude(Double.parseDouble(cursor.getString(14)));
                oLine.setLongitude(Double.parseDouble(cursor.getString(15)));
                oLine.setAccurracy(Double.parseDouble(cursor.getString(16)));
                oLine.setFinished(Integer.parseInt(cursor.getString(17)));
                oLine.setWaitingForScaleData(Integer.parseInt(cursor.getString(18)));
                oLine.setTransmitted(Integer.parseInt(cursor.getString(19)));

                try
                {
                    oLine.setPickupDate(dfDate.parse(cursor.getString(11)));
                    oLine.setTransmittedDate(dfDate.parse(cursor.getString(20)));
                    oLine.setInsertDate(dfDate.parse(cursor.getString(21)));
                    oLine.setModifiedDate(dfDate.parse(cursor.getString(22)));
                }
                catch(ParseException pe)
                {
                    Date dDate = new Date();
                    dDate = Calendar.getInstance().getTime();

                    oLine.setPickupDate(dDate);
                    oLine.setTransmittedDate(dDate);
                    oLine.setInsertDate(dDate);
                    oLine.setModifiedDate(dDate);
                }

                //Add the line object to the line list object
                olLine.add(oLine);

                //Move to the next record from database
                cursor.moveToNext();
            }
        }
        else
        {
            //No records found, set line object to null
            olLine = null;
        }

        //Close the database connection
        db.close();

        //Return the line list object
        return olLine;
    }

    //findLinesNonTransmittedWaitingOnScale
    // - Get list of line objects not transmitted and waiting on scale data
    public List<dbLine> findLinesNonTransmittedWaitingOnScale()
    {
        String query;
        List<dbLine> olLine = new ArrayList<>();
        dbLine oLine = new dbLine();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oLine.TABLE_LINE + " WHERE " + oLine.LINE_COLUMN_FINISHED + " = 0" + " AND " + oLine.LINE_COLUMN_TRANSMITTED + " = 0" + " AND " + oLine.LINE_COLUMN_WAITINGFORSCALEDATA + " = 1";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                //Instantiate new line object
                oLine = new dbLine();

                //Get values from database
                oLine.setPkLineID(cursor.getString(0));
                oLine.setFkHeaderID(cursor.getString(1));
                oLine.setTank(cursor.getString(2));
                oLine.setProducer(cursor.getString(3));
                oLine.setCompany(cursor.getString(4));
                oLine.setDivision(cursor.getString(5));
                oLine.setType(cursor.getString(6));
                oLine.setGaugeRodMajor(Integer.parseInt(cursor.getString(7)));
                oLine.setGaugeRodMinor(Integer.parseInt(cursor.getString(8)));
                oLine.setConvertedLBS(Integer.parseInt(cursor.getString(9)));
                oLine.setTemperature(Integer.parseInt(cursor.getString(10)));
                oLine.setDFATicket(cursor.getString(12));
                oLine.setLabCode(cursor.getString(13));
                oLine.setLatitude(Double.parseDouble(cursor.getString(14)));
                oLine.setLongitude(Double.parseDouble(cursor.getString(15)));
                oLine.setAccurracy(Double.parseDouble(cursor.getString(16)));
                oLine.setFinished(Integer.parseInt(cursor.getString(17)));
                oLine.setWaitingForScaleData(Integer.parseInt(cursor.getString(18)));
                oLine.setTransmitted(Integer.parseInt(cursor.getString(19)));

                try
                {
                    oLine.setPickupDate(dfDate.parse(cursor.getString(11)));
                    oLine.setTransmittedDate(dfDate.parse(cursor.getString(20)));
                    oLine.setInsertDate(dfDate.parse(cursor.getString(21)));
                    oLine.setModifiedDate(dfDate.parse(cursor.getString(22)));
                }
                catch(ParseException pe)
                {
                    Date dDate = new Date();
                    dDate = Calendar.getInstance().getTime();

                    oLine.setPickupDate(dDate);
                    oLine.setTransmittedDate(dDate);
                    oLine.setInsertDate(dDate);
                    oLine.setModifiedDate(dDate);
                }

                //Add the line object to the line list object
                olLine.add(oLine);

                //Move to the next record from database
                cursor.moveToNext();
            }
        }
        else
        {
            //No records found, set line object to null
            olLine = null;
        }

        //Close the database connection
        db.close();

        //Return the line list object
        return olLine;
    }
    
    //deleteLineByID
    // - Delete a line record by ID
    public boolean deleteLineByID(String psLineID)
    {
        boolean result = false;
        dbLine oLine = new dbLine();
        String query;

        //Create the query string
        query = "SELECT * FROM " + oLine.TABLE_LINE + " WHERE " + oLine.LINE_COLUMN_PKLINEID + " =  \"" + psLineID + "\"";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Set the ID to the line object
            oLine.setPkLineID(cursor.getString(0));

            //Delete the line record from database
            db.delete(oLine.TABLE_LINE, oLine.LINE_COLUMN_PKLINEID + " = ?", new String[] { String.valueOf(oLine.getPkLineID()) });

            //Close the cursor
            cursor.close();

            //Return true
            result = true;
        }

        //Close the database connection
        db.close();

        //Return the result
        return result;
    }

    //deleteLineAll
    // - Delete all line records
    public boolean deleteLineAll()
    {
        boolean result = false;
        dbLine oLine = new dbLine();

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Delete all line records from database
        db.delete(oLine.TABLE_LINE, null, null);

        //Return true
        result = true;

        //Close the database connection
        db.close();

        //Return the result
        return result;
    }
    //endregion

    //region Receive Methods
    //addReceive
    // - Add receive record(s) to SQLite database
    public void addReceive(List<dbReceive> polReceive)
    {
        //Loop through all receive objects in array
        for (dbReceive oReceive : polReceive)
        {
            //Instantiate a content value object
            ContentValues values = new ContentValues();

            //Instantiate a date formatter
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

            //Load content values with column names and values
            values.put(oReceive.RECEIVE_COLUMN_PKRECEIVEID, oReceive.getPkReceiveID());
            values.put(oReceive.RECEIVE_COLUMN_FKHEADERID, oReceive.getFkHeaderID());
            values.put(oReceive.RECEIVE_COLUMN_FKPLANTID, oReceive.getFkPlantID());
            values.put(oReceive.RECEIVE_COLUMN_FKPLANTORIGINALID, oReceive.getFkPlantOriginalID());
            values.put(oReceive.RECEIVE_COLUMN_DRUGTESTDEVICE, oReceive.getDrugTestDevice());
            values.put(oReceive.RECEIVE_COLUMN_DRUGTESTRESULT, oReceive.getDrugTestResult());
            values.put(oReceive.RECEIVE_COLUMN_RECEIVEDATETIME, dfDate.format(oReceive.getReceiveDateTime()));
            values.put(oReceive.RECEIVE_COLUMN_TANK, oReceive.getTank());
            values.put(oReceive.RECEIVE_COLUMN_SCALEMETER, oReceive.getScaleMeter().toString());
            values.put(oReceive.RECEIVE_COLUMN_TOPSEAL, oReceive.getTopSeal());
            values.put(oReceive.RECEIVE_COLUMN_BOTTOMSEAL, oReceive.getBottomSeal());
            values.put(oReceive.RECEIVE_COLUMN_RECEIVEDLBS, oReceive.getReceivedLBS().toString());
            values.put(oReceive.RECEIVE_COLUMN_LOADTEMP, oReceive.getLoadTemp().toString());
            values.put(oReceive.RECEIVE_COLUMN_INTAKENUMBER, oReceive.getIntakeNumber().toString());
            values.put(oReceive.RECEIVE_COLUMN_FINISHED, oReceive.getFinished());
            values.put(oReceive.RECEIVE_COLUMN_WAITINGFORSCALEDATA, oReceive.getWaitingForScaleData());
            values.put(oReceive.RECEIVE_COLUMN_TRANSMITTED, oReceive.getTransmitted());
            values.put(oReceive.RECEIVE_COLUMN_TRANSMITTEDDATE, dfDate.format(oReceive.getTransmittedDate()));
            values.put(oReceive.RECEIVE_COLUMN_INSERTDATE, dfDate.format(oReceive.getInsertDate()));
            values.put(oReceive.RECEIVE_COLUMN_MODIFIEDDATE, dfDate.format(oReceive.getModifiedDate()));

            //Instantiate a new db object
            SQLiteDatabase db = this.getWritableDatabase();

            //Insert the receive object into database
            db.insert(oReceive.TABLE_RECEIVE, null, values);

            //Close the database connection
            db.close();
        }
    }

    //updateReceive
    // - Update receive record in SQLite database
    public void updateReceive(dbReceive poReceive)
    {
        //Check if header object is null
        if (poReceive != null)
        {
            //Instantiate a content value object
            ContentValues values = new ContentValues();

            //Instantiate a date formatter
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

            //Load content values with column names and values
            values.put(poReceive.RECEIVE_COLUMN_PKRECEIVEID, poReceive.getPkReceiveID());
            values.put(poReceive.RECEIVE_COLUMN_FKHEADERID, poReceive.getFkHeaderID());
            values.put(poReceive.RECEIVE_COLUMN_FKPLANTID, poReceive.getFkPlantID());
            values.put(poReceive.RECEIVE_COLUMN_FKPLANTORIGINALID, poReceive.getFkPlantOriginalID());
            values.put(poReceive.RECEIVE_COLUMN_DRUGTESTDEVICE, poReceive.getDrugTestDevice());
            values.put(poReceive.RECEIVE_COLUMN_DRUGTESTRESULT, poReceive.getDrugTestResult());
            values.put(poReceive.RECEIVE_COLUMN_RECEIVEDATETIME, dfDate.format(poReceive.getReceiveDateTime()));
            values.put(poReceive.RECEIVE_COLUMN_TANK, poReceive.getTank());
            values.put(poReceive.RECEIVE_COLUMN_SCALEMETER, poReceive.getScaleMeter().toString());
            values.put(poReceive.RECEIVE_COLUMN_TOPSEAL, poReceive.getTopSeal());
            values.put(poReceive.RECEIVE_COLUMN_BOTTOMSEAL, poReceive.getBottomSeal());
            values.put(poReceive.RECEIVE_COLUMN_RECEIVEDLBS, poReceive.getReceivedLBS().toString());
            values.put(poReceive.RECEIVE_COLUMN_LOADTEMP, poReceive.getLoadTemp().toString());
            values.put(poReceive.RECEIVE_COLUMN_INTAKENUMBER, poReceive.getIntakeNumber().toString());
            values.put(poReceive.RECEIVE_COLUMN_FINISHED, poReceive.getFinished());
            values.put(poReceive.RECEIVE_COLUMN_WAITINGFORSCALEDATA, poReceive.getWaitingForScaleData());
            values.put(poReceive.RECEIVE_COLUMN_TRANSMITTED, poReceive.getTransmitted());
            values.put(poReceive.RECEIVE_COLUMN_TRANSMITTEDDATE, dfDate.format(poReceive.getTransmittedDate()));
            values.put(poReceive.RECEIVE_COLUMN_INSERTDATE, dfDate.format(poReceive.getInsertDate()));
            values.put(poReceive.RECEIVE_COLUMN_MODIFIEDDATE, dfDate.format(poReceive.getModifiedDate()));

            //Instantiate a new db object
            SQLiteDatabase db = this.getWritableDatabase();

            //Update the line object into database
            db.update(poReceive.TABLE_RECEIVE, values, poReceive.RECEIVE_COLUMN_PKRECEIVEID + "= ?", new String[] {poReceive.getPkReceiveID()});

            //Close the database connection
            db.close();
        }
    }

    //findReceiveByID
    // - Get a receive record by ID
    public dbReceive findReceiveByID(String psReceiveID)
    {
        String query;
        dbReceive oReceive = new dbReceive();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oReceive.TABLE_RECEIVE + " WHERE " + oReceive.RECEIVE_COLUMN_PKRECEIVEID + " = \"" + psReceiveID + "\"";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            //Get values from database
            oReceive.setPkReceiveID(cursor.getString(0));
            oReceive.setFkHeaderID(cursor.getString(1));
            oReceive.setFkPlantID(cursor.getString(2));
            oReceive.setFkPlantOriginalID(cursor.getString(3));
            oReceive.setDrugTestDevice(cursor.getString(4));
            oReceive.setDrugTestResult(cursor.getString(5));
            oReceive.setTank(cursor.getString(7));
            oReceive.setScaleMeter(Integer.parseInt(cursor.getString(8)));
            oReceive.setTopSeal(cursor.getString(9));
            oReceive.setBottomSeal(cursor.getString(10));
            oReceive.setReceivedLBS(Integer.parseInt(cursor.getString(11)));
            oReceive.setLoadTemp(Integer.parseInt(cursor.getString(12)));
            oReceive.setIntakeNumber(Integer.parseInt(cursor.getString(13)));
            oReceive.setFinished(Integer.parseInt(cursor.getString(14)));
            oReceive.setWaitingForScaleData(Integer.parseInt(cursor.getString(15)));
            oReceive.setTransmitted(Integer.parseInt(cursor.getString(16)));

            try
            {
                oReceive.setReceiveDateTime(dfDate.parse(cursor.getString(6)));
                oReceive.setTransmittedDate(dfDate.parse(cursor.getString(17)));
                oReceive.setInsertDate(dfDate.parse(cursor.getString(18)));
                oReceive.setModifiedDate(dfDate.parse(cursor.getString(19)));
            }
            catch(ParseException pe)
            {
                Date dDate = new Date();
                dDate = Calendar.getInstance().getTime();

                oReceive.setReceiveDateTime(dDate);
                oReceive.setTransmittedDate(dDate);
                oReceive.setInsertDate(dDate);
                oReceive.setModifiedDate(dDate);
            }
        }
        else
        {
            //No records found, set receive object to null
            oReceive = null;
        }

        //Close the database connection
        db.close();

        //Return the receive object
        return oReceive;
    }

    //findReceivesByHeaderID
    // - Get a receive records by HeaderID
    public ArrayList<dbReceive> findReceivesByHeaderID(String psHeaderID)
    {
        String query;
        ArrayList<dbReceive> oReceives = new ArrayList<dbReceive>();
        dbReceive oReceive = new dbReceive();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oReceive.TABLE_RECEIVE + " WHERE " + oReceive.RECEIVE_COLUMN_FKHEADERID + " = \"" + psHeaderID + "\" ORDER BY " + oReceive.RECEIVE_COLUMN_INSERTDATE + " ASC";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst()) {
            //Move to the first record
            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                //Instantiate new receive object
                oReceive = new dbReceive();

                //Get values from database
                oReceive.setPkReceiveID(cursor.getString(0));
                oReceive.setFkHeaderID(cursor.getString(1));
                oReceive.setFkPlantID(cursor.getString(2));
                oReceive.setFkPlantOriginalID(cursor.getString(3));
                oReceive.setDrugTestDevice(cursor.getString(4));
                oReceive.setDrugTestResult(cursor.getString(5));
                oReceive.setTank(cursor.getString(7));
                oReceive.setScaleMeter(Integer.parseInt(cursor.getString(8)));
                oReceive.setTopSeal(cursor.getString(9));
                oReceive.setBottomSeal(cursor.getString(10));
                oReceive.setReceivedLBS(Integer.parseInt(cursor.getString(11)));
                oReceive.setLoadTemp(Integer.parseInt(cursor.getString(12)));
                oReceive.setIntakeNumber(Integer.parseInt(cursor.getString(13)));
                oReceive.setFinished(Integer.parseInt(cursor.getString(14)));
                oReceive.setWaitingForScaleData(Integer.parseInt(cursor.getString(15)));
                oReceive.setTransmitted(Integer.parseInt(cursor.getString(16)));

                try
                {
                    oReceive.setReceiveDateTime(dfDate.parse(cursor.getString(6)));
                    oReceive.setTransmittedDate(dfDate.parse(cursor.getString(17)));
                    oReceive.setInsertDate(dfDate.parse(cursor.getString(18)));
                    oReceive.setModifiedDate(dfDate.parse(cursor.getString(19)));
                }
                catch(ParseException pe)
                {
                    Date dDate = new Date();
                    dDate = Calendar.getInstance().getTime();

                    oReceive.setReceiveDateTime(dDate);
                    oReceive.setTransmittedDate(dDate);
                    oReceive.setInsertDate(dDate);
                    oReceive.setModifiedDate(dDate);
                }

                //Add receive object to array of receive objects
                oReceives.add(oReceive);

                //Move to the next record in cursor
                cursor.moveToNext();
            }
        }
        else
        {
            //No records found, set receive array object to null
            oReceives = null;
        }

        //Close the database connection
        db.close();

        //Return the receive array object
        return oReceives;
    }

    //findReceivesNonTransmitted
    // - Get list of receive objects not transmitted
    public List<dbReceive> findReceivesNonTransmitted()
    {
        String query;
        List<dbReceive> olReceive = new ArrayList<>();
        dbReceive oReceive = new dbReceive();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oReceive.TABLE_RECEIVE + " WHERE " + oReceive.RECEIVE_COLUMN_FINISHED + " = 1" + " AND " + oReceive.RECEIVE_COLUMN_TRANSMITTED + " = 0";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                //Instantiate new receive object
                oReceive = new dbReceive();

                //Get values from database
                oReceive.setPkReceiveID(cursor.getString(0));
                oReceive.setFkHeaderID(cursor.getString(1));
                oReceive.setFkPlantID(cursor.getString(2));
                oReceive.setFkPlantOriginalID(cursor.getString(3));
                oReceive.setDrugTestDevice(cursor.getString(4));
                oReceive.setDrugTestResult(cursor.getString(5));
                oReceive.setTank(cursor.getString(7));
                oReceive.setScaleMeter(Integer.parseInt(cursor.getString(8)));
                oReceive.setTopSeal(cursor.getString(9));
                oReceive.setBottomSeal(cursor.getString(10));
                oReceive.setReceivedLBS(Integer.parseInt(cursor.getString(11)));
                oReceive.setLoadTemp(Integer.parseInt(cursor.getString(12)));
                oReceive.setIntakeNumber(Integer.parseInt(cursor.getString(13)));
                oReceive.setFinished(Integer.parseInt(cursor.getString(14)));
                oReceive.setWaitingForScaleData(Integer.parseInt(cursor.getString(15)));
                oReceive.setTransmitted(Integer.parseInt(cursor.getString(16)));

                try
                {
                    oReceive.setReceiveDateTime(dfDate.parse(cursor.getString(6)));
                    oReceive.setTransmittedDate(dfDate.parse(cursor.getString(17)));
                    oReceive.setInsertDate(dfDate.parse(cursor.getString(18)));
                    oReceive.setModifiedDate(dfDate.parse(cursor.getString(19)));
                }
                catch(ParseException pe)
                {
                    Date dDate = new Date();
                    dDate = Calendar.getInstance().getTime();

                    oReceive.setReceiveDateTime(dDate);
                    oReceive.setTransmittedDate(dDate);
                    oReceive.setInsertDate(dDate);
                    oReceive.setModifiedDate(dDate);
                }

                //Add the receive object to the receive list object
                olReceive.add(oReceive);

                //Move to the next record from database
                cursor.moveToNext();
            }
        }
        else
        {
            //No records found, set receive object to null
            olReceive = null;
        }

        //Close the database connection
        db.close();

        //Return the receive list object
        return olReceive;
    }

    //findReceivesNonTransmittedWaitingForScale
    // - Get list of receive objects not transmitted waiting for scale data
    public List<dbReceive> findReceivesNonTransmittedWaitingForScale()
    {
        String query;
        List<dbReceive> olReceive = new ArrayList<>();
        dbReceive oReceive = new dbReceive();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oReceive.TABLE_RECEIVE + " WHERE " + oReceive.RECEIVE_COLUMN_FINISHED + " = 0" + " AND " + oReceive.RECEIVE_COLUMN_TRANSMITTED + " = 0" + " AND " + oReceive.RECEIVE_COLUMN_WAITINGFORSCALEDATA + " = 1";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                //Instantiate new receive object
                oReceive = new dbReceive();

                //Get values from database
                oReceive.setPkReceiveID(cursor.getString(0));
                oReceive.setFkHeaderID(cursor.getString(1));
                oReceive.setFkPlantID(cursor.getString(2));
                oReceive.setFkPlantOriginalID(cursor.getString(3));
                oReceive.setDrugTestDevice(cursor.getString(4));
                oReceive.setDrugTestResult(cursor.getString(5));
                oReceive.setTank(cursor.getString(7));
                oReceive.setScaleMeter(Integer.parseInt(cursor.getString(8)));
                oReceive.setTopSeal(cursor.getString(9));
                oReceive.setBottomSeal(cursor.getString(10));
                oReceive.setReceivedLBS(Integer.parseInt(cursor.getString(11)));
                oReceive.setLoadTemp(Integer.parseInt(cursor.getString(12)));
                oReceive.setIntakeNumber(Integer.parseInt(cursor.getString(13)));
                oReceive.setFinished(Integer.parseInt(cursor.getString(14)));
                oReceive.setWaitingForScaleData(Integer.parseInt(cursor.getString(15)));
                oReceive.setTransmitted(Integer.parseInt(cursor.getString(16)));

                try
                {
                    oReceive.setReceiveDateTime(dfDate.parse(cursor.getString(6)));
                    oReceive.setTransmittedDate(dfDate.parse(cursor.getString(17)));
                    oReceive.setInsertDate(dfDate.parse(cursor.getString(18)));
                    oReceive.setModifiedDate(dfDate.parse(cursor.getString(19)));
                }
                catch(ParseException pe)
                {
                    Date dDate = new Date();
                    dDate = Calendar.getInstance().getTime();

                    oReceive.setReceiveDateTime(dDate);
                    oReceive.setTransmittedDate(dDate);
                    oReceive.setInsertDate(dDate);
                    oReceive.setModifiedDate(dDate);
                }

                //Add the receive object to the receive list object
                olReceive.add(oReceive);

                //Move to the next record from database
                cursor.moveToNext();
            }
        }
        else
        {
            //No records found, set receive object to null
            olReceive = null;
        }

        //Close the database connection
        db.close();

        //Return the receive list object
        return olReceive;
    }

    //deleteReceiveByID
    // - Delete a receive record by ID
    public boolean deleteReceiveByID(String psReceiveID)
    {
        boolean result = false;
        dbReceive oReceive = new dbReceive();
        String query;

        //Create the query string
        query = "SELECT * FROM " + oReceive.TABLE_RECEIVE + " WHERE " + oReceive.RECEIVE_COLUMN_PKRECEIVEID + " =  \"" + psReceiveID + "\"";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Set the ID to the receive object
            oReceive.setPkReceiveID(cursor.getString(0));

            //Delete the receive record from database
            db.delete(oReceive.TABLE_RECEIVE, oReceive.RECEIVE_COLUMN_PKRECEIVEID + " = ?", new String[] { String.valueOf(oReceive.getPkReceiveID()) });

            //Close the cursor
            cursor.close();

            //Return true
            result = true;
        }

        //Close the database connection
        db.close();

        //Return the result
        return result;
    }

    //deleteReceiveAll
    // - Delete all receive records
    public boolean deleteReceiveAll()
    {
        boolean result = false;
        dbReceive oReceive = new dbReceive();

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Delete all receive records from database
        db.delete(oReceive.TABLE_RECEIVE, null, null);

        //Return true
        result = true;

        //Close the database connection
        db.close();

        //Return the result
        return result;
    }
    //endregion

    //region Profile Methods
    //addProfile
    // - Add profile record to SQLite database
    public void addProfile(dbProfile poProfile)
    {
        //Instantiate a content value object
        ContentValues values = new ContentValues();

        //Instantiate a date formatter
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Load content values with column names and values
        values.put(poProfile.PROFILE_COLUMN_PKPROFILEID, poProfile.getPkProfileID());
        values.put(poProfile.PROFILE_COLUMN_FKPLANTID, poProfile.getFkPlantID());
        values.put(poProfile.PROFILE_COLUMN_USERNAME, poProfile.getUsername());
        values.put(poProfile.PROFILE_COLUMN_FULLNAME, poProfile.getFullName());
        values.put(poProfile.PROFILE_COLUMN_INITIALS, poProfile.getInitials());
        values.put(poProfile.PROFILE_COLUMN_PIN, poProfile.getPin());
        values.put(poProfile.PROFILE_COLUMN_HAULERSIGNATURE, poProfile.getHaulerSignature());
        values.put(poProfile.PROFILE_COLUMN_HAULERLICENSENUMBER, poProfile.getHaulerLicenseNumber());
        values.put(poProfile.PROFILE_COLUMN_HAULEREXPIRATIONDATE, poProfile.getHaulerExpirationDate());
        values.put(poProfile.PROFILE_COLUMN_HAULERNUMBER, poProfile.getHaulerNumber());
        values.put(poProfile.PROFILE_COLUMN_SIGNATUREAGREEMENT, poProfile.getSignatureAgreement());
        values.put(poProfile.PROFILE_COLUMN_ACTIVE, poProfile.getActive());
        values.put(poProfile.PROFILE_COLUMN_ADMINSECURITY, poProfile.getAdminSecurity());
        values.put(poProfile.PROFILE_COLUMN_LASTSIGNINDATE, dfDate.format(poProfile.getLastSignInDate()));
        values.put(poProfile.PROFILE_COLUMN_INSERTDATE, dfDate.format(poProfile.getInsertDate()));
        values.put(poProfile.PROFILE_COLUMN_MODIFIEDDATE, dfDate.format(poProfile.getModifiedDate()));

        //Instantiate a new db object
        SQLiteDatabase db = this.getWritableDatabase();

        //Insert the profile object into database
        db.insert(poProfile.TABLE_PROFILE, null, values);

        //Close the database connection
        db.close();
    }

    //updateProfile
    // - Update profile record(s) to SQLite database
    public void updateProfile(dbProfile poProfile)
    {
        //Check if profile object is null
        if (poProfile != null)
        {
            //Instantiate a content value object
            ContentValues values = new ContentValues();

            //Instantiate a date formatter
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

            //Load content values with column names and values
            values.put(poProfile.PROFILE_COLUMN_PKPROFILEID, poProfile.getPkProfileID());
            values.put(poProfile.PROFILE_COLUMN_FKPLANTID, poProfile.getFkPlantID());
            values.put(poProfile.PROFILE_COLUMN_USERNAME, poProfile.getUsername());
            values.put(poProfile.PROFILE_COLUMN_FULLNAME, poProfile.getFullName());
            values.put(poProfile.PROFILE_COLUMN_INITIALS, poProfile.getInitials());
            values.put(poProfile.PROFILE_COLUMN_PIN, poProfile.getPin());
            values.put(poProfile.PROFILE_COLUMN_HAULERSIGNATURE, poProfile.getHaulerSignature());
            values.put(poProfile.PROFILE_COLUMN_HAULERLICENSENUMBER, poProfile.getHaulerLicenseNumber());
            values.put(poProfile.PROFILE_COLUMN_HAULEREXPIRATIONDATE, poProfile.getHaulerExpirationDate());
            values.put(poProfile.PROFILE_COLUMN_HAULERNUMBER, poProfile.getHaulerNumber());
            values.put(poProfile.PROFILE_COLUMN_SIGNATUREAGREEMENT, poProfile.getSignatureAgreement());
            values.put(poProfile.PROFILE_COLUMN_ACTIVE, poProfile.getActive());
            values.put(poProfile.PROFILE_COLUMN_ADMINSECURITY, poProfile.getAdminSecurity());
            values.put(poProfile.PROFILE_COLUMN_LASTSIGNINDATE, dfDate.format(poProfile.getLastSignInDate()));
            values.put(poProfile.PROFILE_COLUMN_INSERTDATE, dfDate.format(poProfile.getInsertDate()));
            values.put(poProfile.PROFILE_COLUMN_MODIFIEDDATE, dfDate.format(poProfile.getModifiedDate()));

            //Instantiate a new db object
            SQLiteDatabase db = this.getWritableDatabase();

            //Update the profile object into database
            db.update(poProfile.TABLE_PROFILE, values, poProfile.PROFILE_COLUMN_PKPROFILEID + "= ?", new String[] {poProfile.getPkProfileID()});

            //Close the database connection
            db.close();
        }
    }

    //findProfileByID
    // - Get a profile record by ID
    public dbProfile findProfileByID(String psProfileID)
    {
        String query;
        dbProfile oProfile = new dbProfile();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oProfile.TABLE_PROFILE + " WHERE " + oProfile.PROFILE_COLUMN_PKPROFILEID + " = \"" + psProfileID + "\"";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            //Get values from database
            oProfile.setPkProfileID(cursor.getString(0));
            oProfile.setFkPlantID(cursor.getString(1));
            oProfile.setUsername(cursor.getString(2));
            oProfile.setFullName(cursor.getString(3));
            oProfile.setInitials(cursor.getString(4));
            oProfile.setPin(Integer.parseInt(cursor.getString(5)));
            oProfile.setHaulerSignature(cursor.getString(6));
            oProfile.setHaulerLicenseNumber(cursor.getString(7));
            oProfile.setHaulerExpirationDate(cursor.getString(8));
            oProfile.setHaulerNumber(cursor.getString(9));
            oProfile.setSignatureAgreement(Integer.parseInt(cursor.getString(10)));
            oProfile.setActive(Integer.parseInt(cursor.getString(11)));
            oProfile.setAdminSecurity(Integer.parseInt(cursor.getString(12)));

            try
            {
                oProfile.setLastSignInDate(dfDate.parse(cursor.getString(13)));
                oProfile.setInsertDate(dfDate.parse(cursor.getString(14)));
                oProfile.setModifiedDate(dfDate.parse(cursor.getString(15)));
            }
            catch(ParseException pe)
            {
                Date dDate = new Date();
                dDate = Calendar.getInstance().getTime();

                oProfile.setLastSignInDate(dDate);
                oProfile.setInsertDate(dDate);
                oProfile.setModifiedDate(dDate);
            }
        }
        else
        {
            //No records found, set profile object to null
            oProfile = null;
        }

        //Close the database connection
        db.close();

        //Return the profile object
        return oProfile;
    }

    //findProfileByUserPin
    // - Get a profile record by username and pin
    public dbProfile findProfileByUserPin(String psUsername, String psPin)
    {
        String query;
        dbProfile oProfile = new dbProfile();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oProfile.TABLE_PROFILE + " WHERE " + oProfile.PROFILE_COLUMN_USERNAME + " = \"" + psUsername + "\" AND " + oProfile.PROFILE_COLUMN_PIN + " = " + psPin + " AND " + oProfile.PROFILE_COLUMN_ACTIVE + " = 1";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            //Get values from database
            oProfile.setPkProfileID(cursor.getString(0));
            oProfile.setFkPlantID(cursor.getString(1));
            oProfile.setUsername(cursor.getString(2));
            oProfile.setFullName(cursor.getString(3));
            oProfile.setInitials(cursor.getString(4));
            oProfile.setPin(Integer.parseInt(cursor.getString(5)));
            oProfile.setHaulerSignature(cursor.getString(6));
            oProfile.setHaulerLicenseNumber(cursor.getString(7));
            oProfile.setHaulerExpirationDate(cursor.getString(8));
            oProfile.setHaulerNumber(cursor.getString(9));
            oProfile.setSignatureAgreement(Integer.parseInt(cursor.getString(10)));
            oProfile.setActive(Integer.parseInt(cursor.getString(11)));
            oProfile.setAdminSecurity(Integer.parseInt(cursor.getString(12)));

            try
            {
                oProfile.setLastSignInDate(dfDate.parse(cursor.getString(13)));
                oProfile.setInsertDate(dfDate.parse(cursor.getString(14)));
                oProfile.setModifiedDate(dfDate.parse(cursor.getString(15)));
            }
            catch(ParseException pe)
            {
                Date dDate = new Date();
                dDate = Calendar.getInstance().getTime();

                oProfile.setLastSignInDate(dDate);
                oProfile.setInsertDate(dDate);
                oProfile.setModifiedDate(dDate);
            }
        }
        else
        {
            //No records found, set profile object to null
            oProfile = null;
        }

        //Close the database connection
        db.close();

        //Return the profile object
        return oProfile;
    }

    //findProfileLastDate
    // - Get a profile record last date
    public String findProfileLastDate()
    {
        String query;
        String sLastDate;
        dbProfile oProfile = new dbProfile();

        //Create the query string
        query = "SELECT * FROM " + oProfile.TABLE_PROFILE + " ORDER BY " + oProfile.PROFILE_COLUMN_MODIFIEDDATE + " DESC LIMIT 1";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            //Get values from database
            sLastDate = cursor.getString(15);
        }
        else
        {
            //No records found, set profile object to null
            sLastDate = "1/1/1900";
        }

        //Close the database connection
        db.close();

        //Return the last date
        return sLastDate;
    }

    //deleteProfileByID
    // - Delete a profile record by ID
    public boolean deleteProfileByID(String psProfileID)
    {
        boolean result = false;
        dbProfile oProfile = new dbProfile();
        String query;

        //Create the query string
        query = "SELECT * FROM " + oProfile.TABLE_PROFILE + " WHERE " + oProfile.PROFILE_COLUMN_PKPROFILEID + " =  \"" + psProfileID + "\"";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Set the ID to the profile object
            oProfile.setPkProfileID(cursor.getString(0));

            //Delete the profile record from database
            db.delete(oProfile.TABLE_PROFILE, oProfile.PROFILE_COLUMN_PKPROFILEID + " = ?", new String[] { String.valueOf(oProfile.getPkProfileID()) });

            //Close the cursor
            cursor.close();

            //Return true
            result = true;
        }

        //Close the database connection
        db.close();

        //Return the result
        return result;
    }
    //endregion

    //region Settings Methods
    //addSettings
    // - Add settings record(s) to SQLite database
    public void addSettings(dbSettings poSettings)
    {
        //Check if settings object is null
        if (poSettings != null)
        {
            //Instantiate a content value object
            ContentValues values = new ContentValues();

            //Instantiate a date formatter
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

            //Load content values with column names and values
            values.put(poSettings.SETTINGS_COLUMN_PKSETTINGSID, poSettings.getPkSettingsID());
            values.put(poSettings.SETTINGS_COLUMN_TABLETNAME, poSettings.getTabletName());
            values.put(poSettings.SETTINGS_COLUMN_MACHINEID, poSettings.getMachineID());
            values.put(poSettings.SETTINGS_COLUMN_TRACKPICKUPGEOLOCATION, poSettings.getTrackPickupGeoLocation());
            values.put(poSettings.SETTINGS_COLUMN_TRACKROUTEGEOLOCATION, poSettings.getTrackRouteGeoLocation());
            values.put(poSettings.SETTINGS_COLUMN_DEBUG, poSettings.getDebug());
            values.put(poSettings.SETTINGS_COLUMN_DOWNLOADNOTCOMPLETEDDATA, poSettings.getDownloadNotCompletedData());
            values.put(poSettings.SETTINGS_COLUMN_AUTODBBACKUP, poSettings.getAutoDBBackup());
            values.put(poSettings.SETTINGS_COLUMN_LASTUSERLOGINID, poSettings.getLastUserLoginID());
            values.put(poSettings.SETTINGS_COLUMN_LASTUSERLOGINDATE, dfDate.format(poSettings.getLastUserLoginDate()));
            values.put(poSettings.SETTINGS_COLUMN_LASTMILKRECEIPTID, poSettings.getLastMilkReceiptID());
            values.put(poSettings.SETTINGS_COLUMN_SCANLOOP, poSettings.getScanLoop());
            values.put(poSettings.SETTINGS_COLUMN_LASTSETTINGSUPDATE, dfDate.format(poSettings.getLastSettingsUpdate()));
            values.put(poSettings.SETTINGS_COLUMN_LASTPROFILEUPDATE, dfDate.format(poSettings.getLastProfileUpdate()));
            values.put(poSettings.SETTINGS_COLUMN_UPDATEAVAILABLE, poSettings.getUpdateAvailable());
            values.put(poSettings.SETTINGS_COLUMN_UPDATEAVAILABLEDATE, dfDate.format(poSettings.getUpdateAvailableDate()));
            values.put(poSettings.SETTINGS_COLUMN_DRUGTESTDEVICE, poSettings.getDrugTestDevice());
            values.put(poSettings.SETTINGS_COLUMN_WEBSERVICEURL, poSettings.getWebServiceURL());
            values.put(poSettings.SETTINGS_COLUMN_INSERTDATE, dfDate.format(poSettings.getInsertDate()));
            values.put(poSettings.SETTINGS_COLUMN_MODIFIEDDATE, dfDate.format(poSettings.getModifiedDate()));

            //Instantiate a new db object
            SQLiteDatabase db = this.getWritableDatabase();

            //Insert the settings object into database
            db.insert(poSettings.TABLE_SETTINGS, null, values);

            //Close the database connection
            db.close();
        }
    }

    //updateSettings
    // - Update settings record(s) to SQLite database
    public void updateSettings(dbSettings poSettings)
    {
        //Check if settings object is null
        if (poSettings != null)
        {
            //Instantiate a content value object
            ContentValues values = new ContentValues();

            //Instantiate a date formatter
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

            //Load content values with column names and values
            values.put(poSettings.SETTINGS_COLUMN_PKSETTINGSID, poSettings.getPkSettingsID());
            values.put(poSettings.SETTINGS_COLUMN_TABLETNAME, poSettings.getTabletName());
            values.put(poSettings.SETTINGS_COLUMN_MACHINEID, poSettings.getMachineID());
            values.put(poSettings.SETTINGS_COLUMN_TRACKPICKUPGEOLOCATION, poSettings.getTrackPickupGeoLocation());
            values.put(poSettings.SETTINGS_COLUMN_TRACKROUTEGEOLOCATION, poSettings.getTrackRouteGeoLocation());
            values.put(poSettings.SETTINGS_COLUMN_DEBUG, poSettings.getDebug());
            values.put(poSettings.SETTINGS_COLUMN_DOWNLOADNOTCOMPLETEDDATA, poSettings.getDownloadNotCompletedData());
            values.put(poSettings.SETTINGS_COLUMN_AUTODBBACKUP, poSettings.getAutoDBBackup());
            values.put(poSettings.SETTINGS_COLUMN_LASTUSERLOGINID, poSettings.getLastUserLoginID());
            values.put(poSettings.SETTINGS_COLUMN_LASTUSERLOGINDATE, dfDate.format(poSettings.getLastUserLoginDate()));
            values.put(poSettings.SETTINGS_COLUMN_LASTMILKRECEIPTID, poSettings.getLastMilkReceiptID());
            values.put(poSettings.SETTINGS_COLUMN_SCANLOOP, poSettings.getScanLoop());
            values.put(poSettings.SETTINGS_COLUMN_LASTSETTINGSUPDATE, dfDate.format(poSettings.getLastSettingsUpdate()));
            values.put(poSettings.SETTINGS_COLUMN_LASTPROFILEUPDATE, dfDate.format(poSettings.getLastProfileUpdate()));
            values.put(poSettings.SETTINGS_COLUMN_UPDATEAVAILABLE, poSettings.getUpdateAvailable());
            values.put(poSettings.SETTINGS_COLUMN_UPDATEAVAILABLEDATE, dfDate.format(poSettings.getUpdateAvailableDate()));
            values.put(poSettings.SETTINGS_COLUMN_DRUGTESTDEVICE, poSettings.getDrugTestDevice());
            values.put(poSettings.SETTINGS_COLUMN_WEBSERVICEURL, poSettings.getWebServiceURL());
            values.put(poSettings.SETTINGS_COLUMN_INSERTDATE, dfDate.format(poSettings.getInsertDate()));
            values.put(poSettings.SETTINGS_COLUMN_MODIFIEDDATE, dfDate.format(poSettings.getModifiedDate()));

            //Instantiate a new db object
            SQLiteDatabase db = this.getWritableDatabase();

            //Update the settings object into database
            db.update(poSettings.TABLE_SETTINGS, values, poSettings.SETTINGS_COLUMN_MACHINEID + "= ?", new String[] {poSettings.getMachineID()});

            //Close the database connection
            db.close();
        }
    }

    //findSettingsByID
    // - Get a settings record by ID
    public dbSettings findSettingsByID(String psSettingsID)
    {
        String query;
        dbSettings oSettings = new dbSettings();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oSettings.TABLE_SETTINGS + " WHERE " + oSettings.SETTINGS_COLUMN_PKSETTINGSID + " = \"" + psSettingsID + "\"";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            //Get values from database
            oSettings.setPkSettingsID(cursor.getString(0));
            oSettings.setTabletName(cursor.getString(1));
            oSettings.setMachineID(cursor.getString(2));
            oSettings.setTrackPickupGeoLocation(Integer.parseInt(cursor.getString(3)));
            oSettings.setTrackRouteGeoLocation(Integer.parseInt(cursor.getString(4)));
            oSettings.setDebug(Integer.parseInt(cursor.getString(5)));
            oSettings.setDownloadNotCompletedData(Integer.parseInt(cursor.getString(6)));
            oSettings.setAutoDBBackup(Integer.parseInt(cursor.getString(7)));
            oSettings.setLastUserLoginID(cursor.getString(8));
            oSettings.setLastMilkReceiptID(cursor.getString(10));
            oSettings.setScanLoop(Integer.parseInt(cursor.getString(11)));
            oSettings.setUpdateAvailable(Integer.parseInt(cursor.getString(14)));
            oSettings.setDrugTestDevice(cursor.getString(16));
            oSettings.setWebServiceURL(cursor.getString(17));

            try
            {
                oSettings.setLastUserLoginDate(dfDate.parse(cursor.getString(9)));
                oSettings.setLastSettingsUpdate(dfDate.parse(cursor.getString(12)));
                oSettings.setLastProfileUpdate(dfDate.parse(cursor.getString(13)));
                oSettings.setUpdateAvailableDate(dfDate.parse(cursor.getString(15)));
                oSettings.setInsertDate(dfDate.parse(cursor.getString(18)));
                oSettings.setModifiedDate(dfDate.parse(cursor.getString(19)));
            }
            catch(ParseException pe)
            {
                Date dDate = new Date();
                dDate = Calendar.getInstance().getTime();

                oSettings.setLastUserLoginDate(dDate);
                oSettings.setLastSettingsUpdate(dDate);
                oSettings.setLastProfileUpdate(dDate);
                oSettings.setUpdateAvailableDate(dDate);
                oSettings.setInsertDate(dDate);
                oSettings.setModifiedDate(dDate);
            }
        }
        else
        {
            //No records found, set settings object to null
            oSettings = null;
        }

        //Close the database connection
        db.close();

        //Return the settings object
        return oSettings;
    }

    //findSettingsByName
    // - Get a settings record by name
    public dbSettings findSettingsByName(String psName)
    {
        String query;
        dbSettings oSettings = new dbSettings();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oSettings.TABLE_SETTINGS + " WHERE " + oSettings.SETTINGS_COLUMN_TABLETNAME + " = \"" + psName + "\"";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            //Get values from database
            oSettings.setPkSettingsID(cursor.getString(0));
            oSettings.setTabletName(cursor.getString(1));
            oSettings.setMachineID(cursor.getString(2));
            oSettings.setTrackPickupGeoLocation(Integer.parseInt(cursor.getString(3)));
            oSettings.setTrackRouteGeoLocation(Integer.parseInt(cursor.getString(4)));
            oSettings.setDebug(Integer.parseInt(cursor.getString(5)));
            oSettings.setDownloadNotCompletedData(Integer.parseInt(cursor.getString(6) == null ? "0" : cursor.getString(6)));
            oSettings.setAutoDBBackup(Integer.parseInt(cursor.getString(7)));
            oSettings.setLastUserLoginID(cursor.getString(8));
            oSettings.setLastMilkReceiptID(cursor.getString(10));
            oSettings.setScanLoop(Integer.parseInt(cursor.getString(11)));
            oSettings.setUpdateAvailable(Integer.parseInt(cursor.getString(14)));
            oSettings.setDrugTestDevice(cursor.getString(16));
            oSettings.setWebServiceURL(cursor.getString(17));

            try
            {
                oSettings.setLastUserLoginDate(dfDate.parse(cursor.getString(9)));
                oSettings.setLastSettingsUpdate(dfDate.parse(cursor.getString(12)));
                oSettings.setLastProfileUpdate(dfDate.parse(cursor.getString(13)));
                oSettings.setUpdateAvailableDate(dfDate.parse(cursor.getString(15)));
                oSettings.setInsertDate(dfDate.parse(cursor.getString(18)));
                oSettings.setModifiedDate(dfDate.parse(cursor.getString(19)));
            }
            catch(ParseException pe)
            {
                Date dDate = new Date();
                dDate = Calendar.getInstance().getTime();

                oSettings.setLastUserLoginDate(dDate);
                oSettings.setLastSettingsUpdate(dDate);
                oSettings.setLastProfileUpdate(dDate);
                oSettings.setUpdateAvailableDate(dDate);
                oSettings.setInsertDate(dDate);
                oSettings.setModifiedDate(dDate);
            }
        }
        else
        {
            //No records found, set settings object to null
            oSettings = null;
        }

        //Close the database connection
        db.close();

        //Return the settings object
        return oSettings;
    }

    //findSettingsByNameAll
    // - Get all settings records by name
    public List<dbSettings> findSettingsByNameAll(String psName)
    {
        String query;
        List<dbSettings> olSettings = new ArrayList<dbSettings>();
        dbSettings oSettings = new dbSettings();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oSettings.TABLE_SETTINGS + " WHERE " + oSettings.SETTINGS_COLUMN_TABLETNAME + " = \"" + psName + "\"";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                //Instantiate new settings object
                oSettings = new dbSettings();

                //Get values from database
                oSettings.setPkSettingsID(cursor.getString(0));
                oSettings.setTabletName(cursor.getString(1));
                oSettings.setMachineID(cursor.getString(2));
                oSettings.setTrackPickupGeoLocation(Integer.parseInt(cursor.getString(3)));
                oSettings.setTrackRouteGeoLocation(Integer.parseInt(cursor.getString(4)));
                oSettings.setDebug(Integer.parseInt(cursor.getString(5)));
                oSettings.setDownloadNotCompletedData(Integer.parseInt(cursor.getString(6)));
                oSettings.setAutoDBBackup(Integer.parseInt(cursor.getString(7)));
                oSettings.setLastUserLoginID(cursor.getString(8));
                oSettings.setLastMilkReceiptID(cursor.getString(10));
                oSettings.setScanLoop(Integer.parseInt(cursor.getString(11)));
                oSettings.setUpdateAvailable(Integer.parseInt(cursor.getString(14)));
                oSettings.setDrugTestDevice(cursor.getString(16));
                oSettings.setWebServiceURL(cursor.getString(17));

                try
                {
                    oSettings.setLastUserLoginDate(dfDate.parse(cursor.getString(9)));
                    oSettings.setLastSettingsUpdate(dfDate.parse(cursor.getString(12)));
                    oSettings.setLastProfileUpdate(dfDate.parse(cursor.getString(13)));
                    oSettings.setUpdateAvailableDate(dfDate.parse(cursor.getString(15)));
                    oSettings.setInsertDate(dfDate.parse(cursor.getString(18)));
                    oSettings.setModifiedDate(dfDate.parse(cursor.getString(19)));
                }
                catch(ParseException pe)
                {
                    Date dDate = new Date();
                    dDate = Calendar.getInstance().getTime();

                    oSettings.setLastUserLoginDate(dDate);
                    oSettings.setLastSettingsUpdate(dDate);
                    oSettings.setLastProfileUpdate(dDate);
                    oSettings.setUpdateAvailableDate(dDate);
                    oSettings.setInsertDate(dDate);
                    oSettings.setModifiedDate(dDate);
                }

                //Add settings object to array list
                olSettings.add(oSettings);

                //Move to the next record in database
                cursor.moveToNext();
            }
        }
        else
        {
            //No records found, set settings object to null
            olSettings = null;
        }

        //Close the database connection
        db.close();

        //Return the settings object array
        return olSettings;
    }

    //deleteSettingsByID
    // - Delete a settings record by ID
    public boolean deleteSettingsByID(String psSettingsID)
    {
        boolean result = false;
        dbSettings oSettings = new dbSettings();
        String query;

        //Create the query string
        query = "SELECT * FROM " + oSettings.TABLE_SETTINGS + " WHERE " + oSettings.SETTINGS_COLUMN_PKSETTINGSID + " =  \"" + psSettingsID + "\"";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Set the ID to the settings object
            oSettings.setPkSettingsID(cursor.getString(0));

            //Delete the settings record from database
            db.delete(oSettings.TABLE_SETTINGS, oSettings.SETTINGS_COLUMN_PKSETTINGSID + " = ?", new String[] { String.valueOf(oSettings.getPkSettingsID()) });

            //Close the cursor
            cursor.close();

            //Return true
            result = true;
        }

        //Close the database connection
        db.close();

        //Return the result
        return result;
    }

    //deleteSettingsAll
    // - Delete all settings records
    public boolean deleteSettingsAll()
    {
        boolean result = false;
        dbSettings oSettings = new dbSettings();

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Delete all activity records from database
        db.delete(oSettings.TABLE_SETTINGS, null, null);

        //Return true
        result = true;

        //Close the database connection
        db.close();

        //Return the result
        return result;
    }
    //endregion

    //region Activity Methods
    //addActivity
    // - Add activity record to SQLite database
    public void addActivity(dbActivityHeader poActivity)
    {
        //Check if activity object is null
        if (poActivity != null)
        {
            //Instantiate a content value object
            ContentValues values = new ContentValues();

            //Instantiate a date formatter
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

            //Load content values with column names and values
            values.put(poActivity.ACTIVITYHEADER_COLUMN_PKACTIVITYHEADERID, poActivity.getPkActivityHeaderID());
            values.put(poActivity.ACTIVITYHEADER_COLUMN_FKACTIVITYTYPEID, poActivity.getFkActivityTypeID());
            values.put(poActivity.ACTIVITYHEADER_COLUMN_APPLICATION, poActivity.getApplication());
            values.put(poActivity.ACTIVITYHEADER_COLUMN_MODULE, poActivity.getModule());
            values.put(poActivity.ACTIVITYHEADER_COLUMN_ROUTINE, poActivity.getRoutine());
            values.put(poActivity.ACTIVITYHEADER_COLUMN_USERNAME, poActivity.getUsername());
            values.put(poActivity.ACTIVITYHEADER_COLUMN_MESSAGE, poActivity.getMessage());
            values.put(poActivity.ACTIVITYHEADER_COLUMN_STACKTRACE, poActivity.getStackTrace());
            values.put(poActivity.ACTIVITYHEADER_COLUMN_TRANSMITTED, poActivity.getTransmitted());
            values.put(poActivity.ACTIVITYHEADER_COLUMN_TRANSMITTEDDATE, dfDate.format(poActivity.getTransmittedDate()));
            values.put(poActivity.ACTIVITYHEADER_COLUMN_INSERTDATE, dfDate.format(poActivity.getInsertDate()));

            //Instantiate a new db object
            SQLiteDatabase db = this.getWritableDatabase();

            //Insert the activity object into database
            db.insert(poActivity.TABLE_ACTIVITYHEADER, null, values);

            //Close the database connection
            db.close();
        }
    }

    //updateActivity
    // - Update Activity record(s) to SQLite database
    public void updateActivity(dbActivityHeader poActivity)
    {
        //Check if activity object is null
        if (poActivity != null)
        {
            //Instantiate a content value object
            ContentValues values = new ContentValues();

            //Instantiate a date formatter
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

            //Load content values with column names and values
            values.put(poActivity.ACTIVITYHEADER_COLUMN_PKACTIVITYHEADERID, poActivity.getPkActivityHeaderID());
            values.put(poActivity.ACTIVITYHEADER_COLUMN_FKACTIVITYTYPEID, poActivity.getFkActivityTypeID());
            values.put(poActivity.ACTIVITYHEADER_COLUMN_APPLICATION, poActivity.getApplication());
            values.put(poActivity.ACTIVITYHEADER_COLUMN_MODULE, poActivity.getModule());
            values.put(poActivity.ACTIVITYHEADER_COLUMN_ROUTINE, poActivity.getRoutine());
            values.put(poActivity.ACTIVITYHEADER_COLUMN_USERNAME, poActivity.getUsername());
            values.put(poActivity.ACTIVITYHEADER_COLUMN_MESSAGE, poActivity.getMessage());
            values.put(poActivity.ACTIVITYHEADER_COLUMN_STACKTRACE, poActivity.getStackTrace());
            values.put(poActivity.ACTIVITYHEADER_COLUMN_TRANSMITTED, poActivity.getTransmitted());
            values.put(poActivity.ACTIVITYHEADER_COLUMN_TRANSMITTEDDATE, dfDate.format(poActivity.getTransmittedDate()));
            values.put(poActivity.ACTIVITYHEADER_COLUMN_INSERTDATE, dfDate.format(poActivity.getInsertDate()));

            //Instantiate a new db object
            SQLiteDatabase db = this.getWritableDatabase();

            //Update the profile object into database
            db.update(poActivity.TABLE_ACTIVITYHEADER, values, poActivity.ACTIVITYHEADER_COLUMN_PKACTIVITYHEADERID + "= ?", new String[] {poActivity.getPkActivityHeaderID()});

            //Close the database connection
            db.close();
        }
    }

    //findActivityByID
    // - Get activity record by id
    public dbActivityHeader findActivityByID(String psActivityHeaderID)
    {
        String query;
        dbActivityHeader oActivity = new dbActivityHeader();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oActivity.TABLE_ACTIVITYHEADER + " WHERE " + oActivity.ACTIVITYHEADER_COLUMN_PKACTIVITYHEADERID + " = \"" + psActivityHeaderID + "\"";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Get values from database
            oActivity.setPkActivityHeaderID(cursor.getString(0));
            oActivity.setFkActivityTypeID(cursor.getString(1));
            oActivity.setApplication(cursor.getString(2));
            oActivity.setModule(cursor.getString(3));
            oActivity.setRoutine(cursor.getString(4));
            oActivity.setUsername(cursor.getString(5));
            oActivity.setMessage(cursor.getString(6));
            oActivity.setStackTrace(cursor.getString(7));
            oActivity.setTransmitted(Integer.parseInt(cursor.getString(8)));

            try
            {
                oActivity.setTransmittedDate(dfDate.parse(cursor.getString(9)));
                oActivity.setInsertDate(dfDate.parse(cursor.getString(10)));
            }
            catch(ParseException pe)
            {
                Date dDate = new Date();
                dDate = Calendar.getInstance().getTime();

                oActivity.setTransmittedDate(dDate);
                oActivity.setInsertDate(dDate);
            }
        }
        else
        {
            //No records found, set activity object to null
            oActivity = null;
        }

        //Close the database connection
        db.close();

        //Return the activity record
        return oActivity;
    }

    //findActivityByDateType
    // - Get activity records by date and type
    public List<dbActivityHeader> findActivityByDateType(String psStartDate, String psEndDate, String psActTypeID)
    {
        String query;
        List<dbActivityHeader> olActivity = new ArrayList<dbActivityHeader>();
        dbActivityHeader oActivity = new dbActivityHeader();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oActivity.TABLE_ACTIVITYHEADER + " WHERE " + oActivity.ACTIVITYHEADER_COLUMN_INSERTDATE + " >= \"" + psStartDate + "\" AND " + oActivity.ACTIVITYHEADER_COLUMN_INSERTDATE + " < \"" + psEndDate + "\" AND " + oActivity.ACTIVITYHEADER_COLUMN_FKACTIVITYTYPEID + " = \"" + psActTypeID + "\"";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            while(!cursor.isAfterLast()) {
                //Instantiate new activity object
                oActivity = new dbActivityHeader();
                
                //Get values from database
                oActivity.setPkActivityHeaderID(cursor.getString(0));
                oActivity.setFkActivityTypeID(cursor.getString(1));
                oActivity.setApplication(cursor.getString(2));
                oActivity.setModule(cursor.getString(3));
                oActivity.setRoutine(cursor.getString(4));
                oActivity.setUsername(cursor.getString(5));
                oActivity.setMessage(cursor.getString(6));
                oActivity.setStackTrace(cursor.getString(7));
                oActivity.setTransmitted(Integer.parseInt(cursor.getString(8)));

                try
                {
                    oActivity.setTransmittedDate(dfDate.parse(cursor.getString(9)));
                    oActivity.setInsertDate(dfDate.parse(cursor.getString(10)));
                }
                catch(ParseException pe)
                {
                    Date dDate = new Date();
                    dDate = Calendar.getInstance().getTime();

                    oActivity.setTransmittedDate(dDate);
                    oActivity.setInsertDate(dDate);
                }

                //Add the object to the list of objects
                olActivity.add(oActivity);

                //Move to the next record from database
                cursor.moveToNext();
            }
        }
        else
        {
            //No records found, set activity object list to null
            olActivity = null;
        }

        //Close the database connection
        db.close();

        //Return the activity list
        return olActivity;
    }

    //findActivityNonTransmitted
    // - Get list of activity objects not transmitted
    public List<dbActivityHeader> findActivityNonTransmitted(Context poContext, String psUsername)
    {
        String query;
        List<dbActivityHeader> olActivity = new ArrayList<>();
        dbActivityHeader oActivity = new dbActivityHeader();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oActivity.TABLE_ACTIVITYHEADER + " WHERE " + oActivity.ACTIVITYHEADER_COLUMN_TRANSMITTED + " = 0";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                //Instantiate new activity object
                oActivity = new dbActivityHeader();

                //Get values from database
                oActivity.setPkActivityHeaderID(cursor.getString(0));
                oActivity.setFkActivityTypeID(cursor.getString(1));
                oActivity.setApplication(cursor.getString(2));
                oActivity.setModule(cursor.getString(3));
                oActivity.setRoutine(cursor.getString(4));
                oActivity.setUsername(cursor.getString(5));
                oActivity.setMessage(cursor.getString(6));
                oActivity.setStackTrace(cursor.getString(7));
                oActivity.setTransmitted(Integer.parseInt(cursor.getString(8)));

                try
                {
                    oActivity.setTransmittedDate(dfDate.parse(cursor.getString(9)));
                    oActivity.setInsertDate(dfDate.parse(cursor.getString(10)));
                }
                catch(ParseException pe)
                {
                    Date dDate = new Date();
                    dDate = Calendar.getInstance().getTime();

                    oActivity.setTransmittedDate(dDate);
                    oActivity.setInsertDate(dDate);
                }

                //Add the activity object to the activity list object
                olActivity.add(oActivity);

                //Move to the next record from database
                cursor.moveToNext();
            }
        }
        else
        {
            //No records found, set activity object to null
            olActivity = null;
        }

        //Close the database connection
        db.close();

        //Return the activity list object
        return olActivity;
    }

    //deleteActivityAll
    // - Delete all activity records
    public boolean deleteActivityAll()
    {
        boolean result = false;
        dbActivityHeader oActivity = new dbActivityHeader();

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Delete all activity records from database
        db.delete(oActivity.TABLE_ACTIVITYHEADER, null, null);

        //Return true
        result = true;

        //Close the database connection
        db.close();

        //Return the result
        return result;
    }
    //endregion
    
    //region GeoLocation Methods
    //addGeoLocation
    // - Add geolocation record to SQLite database
    public void addGeoLocation(dbGeoLocation poGeoLocation)
    {
        //Check if geolocation object is null
        if (poGeoLocation != null)
        {
            //Instantiate a content value object
            ContentValues values = new ContentValues();

            //Instantiate a date formatter
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

            //Load content values with column names and values
            values.put(poGeoLocation.GEOLOCATION_COLUMN_PKGEOLOCATIONID, poGeoLocation.getPkGeoLocationID());
            values.put(poGeoLocation.GEOLOCATION_COLUMN_LATITUDE, poGeoLocation.getLatitude());
            values.put(poGeoLocation.GEOLOCATION_COLUMN_LONGITUDE, poGeoLocation.getLongitude());
            values.put(poGeoLocation.GEOLOCATION_COLUMN_ACCURRACY, poGeoLocation.getAccurracy());
            values.put(poGeoLocation.GEOLOCATION_COLUMN_HEADINGDIRECTION, poGeoLocation.getHeadingDirection());
            values.put(poGeoLocation.GEOLOCATION_COLUMN_SPEED, poGeoLocation.getSpeed());
            values.put(poGeoLocation.GEOLOCATION_COLUMN_ALTITUDE, poGeoLocation.getAltitude());
            values.put(poGeoLocation.GEOLOCATION_COLUMN_TRANSMITTED, poGeoLocation.getTransmitted());
            values.put(poGeoLocation.GEOLOCATION_COLUMN_TRANSMITTEDDATE, dfDate.format(poGeoLocation.getTransmittedDate()));
            values.put(poGeoLocation.GEOLOCATION_COLUMN_INSERTDATE, dfDate.format(poGeoLocation.getInsertDate()));

            //Instantiate a new db object
            SQLiteDatabase db = this.getWritableDatabase();

            //Insert the geolocation object into database
            db.insert(poGeoLocation.TABLE_GEOLOCATION, null, values);

            //Close the database connection
            db.close();
        }
    }
    //endregion

    //region Plant Methods
    //addPlant
    // - Add plant record to SQLite database
    public void addPlant(dbPlant poPlant)
    {
        //Instantiate a content value object
        ContentValues values = new ContentValues();

        //Instantiate a date formatter
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Load content values with column names and values
        values.put(poPlant.PLANT_COLUMN_PKPLANTID, poPlant.getPkPlantID());
        values.put(poPlant.PLANT_COLUMN_PLANTNAME, poPlant.getPlantName());
        values.put(poPlant.PLANT_COLUMN_PLANTNUMBER, poPlant.getPlantNumber());
        values.put(poPlant.PLANT_COLUMN_BTUNUMBER, poPlant.getBTUNumber());
        values.put(poPlant.PLANT_COLUMN_ADDRESS, poPlant.getAddress());
        values.put(poPlant.PLANT_COLUMN_CITYSTATEZIP, poPlant.getCityStateZip());
        values.put(poPlant.PLANT_COLUMN_LATITUDE, poPlant.getLatitude());
        values.put(poPlant.PLANT_COLUMN_LONGITUDE, poPlant.getLongitude());
        values.put(poPlant.PLANT_COLUMN_ACTIVE, poPlant.getActive());
        values.put(poPlant.PLANT_COLUMN_INSERTDATE, dfDate.format(poPlant.getInsertDate()));
        values.put(poPlant.PLANT_COLUMN_MODIFIEDDATE, dfDate.format(poPlant.getModifiedDate()));

        //Instantiate a new db object
        SQLiteDatabase db = this.getWritableDatabase();

        //Insert the plant object into database
        db.insert(poPlant.TABLE_PLANT, null, values);

        //Close the database connection
        db.close();
    }

    //updatePlant
    // - Update plant record(s) to SQLite database
    public void updatePlant(dbPlant poPlant)
    {
        //Check if profile object is null
        if (poPlant != null)
        {
            //Instantiate a content value object
            ContentValues values = new ContentValues();

            //Instantiate a date formatter
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

            //Load content values with column names and values
            values.put(poPlant.PLANT_COLUMN_PKPLANTID, poPlant.getPkPlantID());
            values.put(poPlant.PLANT_COLUMN_PLANTNAME, poPlant.getPlantName());
            values.put(poPlant.PLANT_COLUMN_PLANTNUMBER, poPlant.getPlantNumber());
            values.put(poPlant.PLANT_COLUMN_BTUNUMBER, poPlant.getBTUNumber());
            values.put(poPlant.PLANT_COLUMN_ADDRESS, poPlant.getAddress());
            values.put(poPlant.PLANT_COLUMN_CITYSTATEZIP, poPlant.getCityStateZip());
            values.put(poPlant.PLANT_COLUMN_LATITUDE, poPlant.getLatitude());
            values.put(poPlant.PLANT_COLUMN_LONGITUDE, poPlant.getLongitude());
            values.put(poPlant.PLANT_COLUMN_ACTIVE, poPlant.getActive());
            values.put(poPlant.PLANT_COLUMN_INSERTDATE, dfDate.format(poPlant.getInsertDate()));
            values.put(poPlant.PLANT_COLUMN_MODIFIEDDATE, dfDate.format(poPlant.getModifiedDate()));

            //Instantiate a new db object
            SQLiteDatabase db = this.getWritableDatabase();

            //Update the plant object into database
            db.update(poPlant.TABLE_PLANT, values, poPlant.PLANT_COLUMN_PKPLANTID + "= ?", new String[] {poPlant.getPkPlantID()});

            //Close the database connection
            db.close();
        }
    }

    //findPlantByID
    // - Get a plant record by ID
    public dbPlant findPlantByID(String psPlantID)
    {
        String query;
        dbPlant oPlant = new dbPlant();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oPlant.TABLE_PLANT + " WHERE " + oPlant.PLANT_COLUMN_PKPLANTID + " = \"" + psPlantID + "\"";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            //Get values from database
            oPlant.setPkPlantID(cursor.getString(0));
            oPlant.setPlantName(cursor.getString(1));
            oPlant.setPlantNumber(cursor.getString(2));
            oPlant.setBTUNumber(cursor.getString(3));
            oPlant.setAddress(cursor.getString(4));
            oPlant.setCityStateZip(cursor.getString(5));
            oPlant.setLatitude(Double.parseDouble(cursor.getString(6)));
            oPlant.setLongitude(Double.parseDouble(cursor.getString(7)));
            oPlant.setActive(Integer.parseInt(cursor.getString(8)));

            try
            {
                oPlant.setInsertDate(dfDate.parse(cursor.getString(9)));
                oPlant.setModifiedDate(dfDate.parse(cursor.getString(10)));
            }
            catch(ParseException pe)
            {
                Date dDate = new Date();
                dDate = Calendar.getInstance().getTime();

                oPlant.setInsertDate(dDate);
                oPlant.setModifiedDate(dDate);
            }
        }
        else
        {
            //No records found, set plant object to null
            oPlant = null;
        }

        //Close the database connection
        db.close();

        //Return the plant object
        return oPlant;
    }

    //findPlantsActive
    // - Get all active plant records
    public List<dbPlant> findPlantsActive()
    {
        String query;
        List<dbPlant> olPlant = new ArrayList<dbPlant>();
        dbPlant oPlant = new dbPlant();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oPlant.TABLE_PLANT + " WHERE " + oPlant.PLANT_COLUMN_ACTIVE + " = 1 ORDER BY " + oPlant.PLANT_COLUMN_PLANTNAME + " ASC";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                //Create new instance of the plant object
                oPlant = new dbPlant();

                //Get values from database
                oPlant.setPkPlantID(cursor.getString(0));
                oPlant.setPlantName(cursor.getString(1));
                oPlant.setPlantNumber(cursor.getString(2));
                oPlant.setBTUNumber(cursor.getString(3));
                oPlant.setAddress(cursor.getString(4));
                oPlant.setCityStateZip(cursor.getString(5));
                oPlant.setLatitude(Double.parseDouble(cursor.getString(6)));
                oPlant.setLongitude(Double.parseDouble(cursor.getString(7)));
                oPlant.setActive(Integer.parseInt(cursor.getString(8)));

                try
                {
                    oPlant.setInsertDate(dfDate.parse(cursor.getString(9)));
                    oPlant.setModifiedDate(dfDate.parse(cursor.getString(10)));
                }
                catch(ParseException pe)
                {
                    Date dDate = new Date();
                    dDate = Calendar.getInstance().getTime();

                    oPlant.setInsertDate(dDate);
                    oPlant.setModifiedDate(dDate);
                }

                //Add the plant object to the plant list object
                olPlant.add(oPlant);

                //Move to the next record from database
                cursor.moveToNext();
            }
        }
        else
        {
            //No records found, set plant object to null
            olPlant = null;
        }

        //Close the database connection
        db.close();

        //Return the plant list object
        return olPlant;
    }

    //findPlantLastDate
    // - Get a plant record last date
    public Date findPlantLastDate()
    {
        String query;
        String sLastDate;
        Date dLastDate = new Date("1/1/1900");
        dbPlant oPlant = new dbPlant();

        //Instantiate a date formatter
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oPlant.TABLE_PLANT + " ORDER BY " + oPlant.PLANT_COLUMN_MODIFIEDDATE + " DESC LIMIT 1";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            //Get values from database
            sLastDate = cursor.getString(10);

            try
            {
                dLastDate = dfDate.parse(sLastDate);
            }
            catch(ParseException pex)
            {
                dLastDate = new Date("1/1/1900");
            }
        }

        //Close the database connection
        db.close();

        //Return the last date
        return dLastDate;
    }

    //findPlantByName
    // - Get a plant record by name
    public dbPlant findPlantByName(String psPlantName)
    {
        String query;
        dbPlant oPlant = new dbPlant();
        DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

        //Create the query string
        query = "SELECT * FROM " + oPlant.TABLE_PLANT + " WHERE " + oPlant.PLANT_COLUMN_PLANTNAME + " = \"" + psPlantName + "\"";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Move to the first record
            cursor.moveToFirst();

            //Get values from database
            oPlant.setPkPlantID(cursor.getString(0));
            oPlant.setPlantName(cursor.getString(1));
            oPlant.setPlantNumber(cursor.getString(2));
            oPlant.setBTUNumber(cursor.getString(3));
            oPlant.setAddress(cursor.getString(4));
            oPlant.setCityStateZip(cursor.getString(5));
            oPlant.setLatitude(Double.parseDouble(cursor.getString(6)));
            oPlant.setLongitude(Double.parseDouble(cursor.getString(7)));
            oPlant.setActive(Integer.parseInt(cursor.getString(8)));

            try
            {
                oPlant.setInsertDate(dfDate.parse(cursor.getString(9)));
                oPlant.setModifiedDate(dfDate.parse(cursor.getString(10)));
            }
            catch(ParseException pe)
            {
                Date dDate = new Date();
                dDate = Calendar.getInstance().getTime();

                oPlant.setInsertDate(dDate);
                oPlant.setModifiedDate(dDate);
            }
        }
        else
        {
            //No records found, set plant object to null
            oPlant = null;
        }

        //Close the database connection
        db.close();

        //Return the plant object
        return oPlant;
    }

    //deletePlantByID
    // - Delete a plant record by ID
    public boolean deletePlantByID(String psPlantID)
    {
        boolean result = false;
        dbPlant oPlant = new dbPlant();
        String query;

        //Create the query string
        query = "SELECT * FROM " + oPlant.TABLE_PLANT + " WHERE " + oPlant.PLANT_COLUMN_PKPLANTID + " =  \"" + psPlantID + "\"";

        //Instantiate the database connection
        SQLiteDatabase db = this.getWritableDatabase();

        //Execute query and place in cursor
        Cursor cursor = db.rawQuery(query, null);

        //Check if cursor has records from database
        if (cursor.moveToFirst())
        {
            //Set the ID to the plant object
            oPlant.setPkPlantID(cursor.getString(0));

            //Delete the plant record from database
            db.delete(oPlant.TABLE_PLANT, oPlant.PLANT_COLUMN_PKPLANTID + " = ?", new String[] { String.valueOf(oPlant.getPkPlantID()) });

            //Close the cursor
            cursor.close();

            //Return true
            result = true;
        }

        //Close the database connection
        db.close();

        //Return the result
        return result;
    }
    //endregion
}
