package com.belgioioso.bcimilkreceipt.bcimilkreceipt;

import android.content.Context;
import android.widget.Toast;

import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbActivityHeader;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbDatabaseHandler;
import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbProfile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by ChadHolewinski on 1/2/2018.
 */

public class Utilities 
{
    //region Class Constructor Methods
    /**
     * onCreate
     *  - Constructor
     */
    protected void onCreate()
    {
        
    }
    //endregion
    
    //region Helpers
    public void InsertActivity(Context poContext, String psActivityType, String psModule, String psRoutine, String psUser, String psMessage, String psStackTrace)
    {
        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(poContext, null, 1);

            //Instantiate new activity object
            dbActivityHeader oActivity = new dbActivityHeader();

            //Create a new settingsID GUID
            UUID gID = UUID.randomUUID();

            //Setup the new settings object data
            oActivity.setPkActivityHeaderID(gID.toString());
            oActivity.setFkActivityTypeID(psActivityType);
            oActivity.setApplication("BCIMilkReceipt");
            oActivity.setModule(psModule);
            oActivity.setRoutine(psRoutine);
            oActivity.setUsername(psUser);
            oActivity.setMessage(psMessage);
            oActivity.setStackTrace(psStackTrace);
            oActivity.setTransmitted(false);
            oActivity.setTransmittedDate("1/1/1900");

            //Format the date for insert and modified
            DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            Date dDate = new Date();

            //Set the insert date field
            oActivity.setInsertDate(dfDate.format(dDate).toString());

            //Add the activity record to database
            oDBHandler.addActivity(oActivity);
        }
        catch(Exception ex)
        {
            Toast.makeText(poContext, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    //endregion
}
