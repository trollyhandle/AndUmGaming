package com.example.andumgaming.g370.views.asynctask;

/**
 * Created by ross on 5/4/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andumgaming.g370.views.FullscreenActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import Interface.ICallBackListener;

public class SignupAsyncTask extends AsyncTask<String,Void,String> {
    private ICallBackListener listener;

    //flag 0 means get and 1 means post.(By default it is get.)
    public SignupAsyncTask(ICallBackListener listener) {
        this.listener = listener;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {

        try{
            String email = (String)arg0[0];
            String username = (String)arg0[1];
            String password = (String)arg0[2];

            String link="http://g370.duckdns.org/register.php";
            String data  = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            return sb.toString();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }



    @Override
    protected void onPostExecute(String result) {
        listener.onCallBack(result);
    }
}