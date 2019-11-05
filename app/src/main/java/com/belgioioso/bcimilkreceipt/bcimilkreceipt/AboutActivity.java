package com.belgioioso.bcimilkreceipt.bcimilkreceipt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.belgioiosodb.bcimilkreceipt.bcimilkreceiptdb.dbDatabaseHandler;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button _about_back_button, _about_cleandb_button;
    private TextView txt_About_Label_5, txt_About_Label_6;
    private Utilities _oUtils;

    //region Class Constructor Methods
    /**
     * onCreate
     *  - Constructor
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        _oUtils = new Utilities();

        _about_back_button = findViewById(R.id.about_back_button);
        _about_cleandb_button = findViewById(R.id.about_cleandb_button);
        txt_About_Label_5 = findViewById(R.id.aboutlabel5);
        txt_About_Label_6 = findViewById(R.id.aboutlabel6);

        txt_About_Label_5.setText("Version: " + BuildConfig.VERSION_NAME);
        txt_About_Label_6.setText("Tablet Name: " + android.os.Build.SERIAL);

        _about_back_button.setOnClickListener(this);
        _about_cleandb_button.setOnClickListener(this);
    }
    //endregion

    //region User Events
    /**
     * onClick
     *  - Handles the on click event of the screen
     * @param v
     */
    public void onClick(View v)
    {
        try
        {
            //Check if the back button was clicked
            if (v.getId() == R.id.about_back_button)
            {
                //Instantiate a new intent for signinactivity
                Intent intent = new Intent(this, SignInActivity.class);

                //Start intent to go back to signin page
                startActivity(intent);
            }
            //Check if the clean db button was clicked
            else if (v.getId() == R.id.about_cleandb_button)
            {
                //Clean up the database
                CleanDB();
            }
        }
        catch (Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "AboutActivity", "onClick", "N/A", ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }
    //endregion

    //region Routines
    /**
     * CleanDB
     *  - Clean up the user created data in database
     */
    private void CleanDB()
    {
        try
        {
            //Instantiate the database handler
            dbDatabaseHandler oDBHandler = new dbDatabaseHandler(this, null);

            //Delete all user created data from database
            oDBHandler.deleteHeaderAll();
            oDBHandler.deleteLineAll();
            oDBHandler.deleteReceiveAll();
            oDBHandler.deleteActivityAll();
            //oDBHandler.deleteSettingsAll();
        }
        catch(Exception ex)
        {
            //Log error message to activity
            _oUtils.insertActivity(this, "3", "AboutActivity", "CleanDB", "N/A", ex.getMessage().toString(), ex.getStackTrace().toString());
        }
    }
    //endregion
}
