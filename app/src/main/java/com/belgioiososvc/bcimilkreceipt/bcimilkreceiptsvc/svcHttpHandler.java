package com.belgioiososvc.bcimilkreceipt.bcimilkreceiptsvc;

import android.util.Log;
import org.json.JSONArray;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class svcHttpHandler
{
    private static final String TAG = svcHttpHandler.class.getSimpleName();

    //region Constructor
    public svcHttpHandler()
    {

    }
    //endregion

    //region WebService calls
    /**
     * makeGETServiceCall
     *  - makes the GET call to the web service and returns the result
     * @param reqUrl - url of REST web service method
     * @return (String) - data result retrieved from web service
     */
    public String makeGETServiceCall(String reqUrl)
    {
        String response = null;

        try
        {
            //Setup the url and the http connection to web service
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //Setup the request method
            conn.setRequestMethod("GET");

            // Read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        }
        catch (MalformedURLException e)
        {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        }
        catch (ProtocolException e)
        {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        }
        catch (IOException e)
        {
            Log.e(TAG, "IOException: " + e.getMessage());
        }
        catch (Exception e)
        {
            Log.e(TAG, "Exception: " + e.getMessage());
        }

        return response;
    }

    /**
     * makePOSTServiceCall
     *  - makes the POST call to the web service and returns the result
     * @param reqUrl - url of REST web service method
     * @param reqParams - array of JSON objects being transmitted
     * @return (String) - number of records transmitted successfully to WS
     */
    public String makePOSTServiceCall(String reqUrl, JSONArray reqParams)
    {
        String response = "0";
        String sJsonString;
        StringBuffer sb = new StringBuffer("");
        String line;

        try
        {
            //Convert the JSON array to a string for transmittal
            sJsonString = reqParams.toString();

            //Setup the url and the http connection to web service
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //Setup the read and connection timeouts to the web service
            conn.setReadTimeout(15000); //time in milliseconds
            conn.setConnectTimeout(15000); //time in milliseconds

            //Setup the request method and properties
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

            //Final setup of web service connection
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //Setup data output stream
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

            //Write out the JSON string to the web service for POST
            wr.writeBytes(sJsonString);

            //Cleanup the output writer
            wr.flush();
            wr.close();

            //Get the response from the POST request
            int responseCode = conn.getResponseCode();

            //Check if the response was successful
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                //Setup the reader for response
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                //Read all lines from POST request
                while((line = in.readLine()) != null)
                {
                    //Append the strings into one single string
                    sb.append(line);

                    //Exit loop
                    break;
                }

                //Close the reader
                in.close();

                //Set the response to the string retrieved from POST request
                response = sb.toString();
            }
            else
            {
                //Return 0 because of failure
                response = "0";
            }
        }
        catch (MalformedURLException e)
        {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        }
        catch (ProtocolException e)
        {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        }
        catch (IOException e)
        {
            Log.e(TAG, "IOException: " + e.getMessage());
        }
        catch (Exception e)
        {
            Log.e(TAG, "Exception: " + e.getMessage());
        }

        return response;
    }
    //endregion

    //region Functions
    /**
     * convertStreamToString
     *  - converts the input stream to a string
     * @param is - input stream to be converted
     * @return (String) - string converted from input stream
     */
    private String convertStreamToString(InputStream is)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

        try
        {
            //Loop through all of the lines in the input stream
            while ((line = reader.readLine()) != null)
            {
                //Append the string to the previous strings
                sb.append(line).append('\n');
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
    //endregion
}
