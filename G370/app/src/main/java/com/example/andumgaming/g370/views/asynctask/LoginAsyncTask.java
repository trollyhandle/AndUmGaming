package com.example.andumgaming.g370.views.asynctask;

/**
 * Created by ross on 5/4/2016.
 */

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

public class LoginAsyncTask extends AsyncTask<String,Void,String> {
    private TextView loginStatus;
    private Context context;

    public LoginAsyncTask(Context context, TextView loginStatus) {
        this.context = context;
        this.loginStatus = loginStatus;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0)  {
       /* try {
            URL url = new URL("http://google.com");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 *//* milliseconds *//*);
            conn.setConnectTimeout(15000 *//* milliseconds *//*);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;*/

            return "0"; //"Failed to fetch data!";
    }

    @Override
    protected void onPostExecute(String result) {
        Gson gson = new Gson();
        String json = gson.toJson(result);
        Log.v("System.out",gson.toJson(result));
        /*JsonResponse jsonResponse = gson.fromJson(json, JsonResponse.class);
        if (jsonResponse.success == 1) {
            Intent i = new Intent(context, FullscreenActivity.class);
            context.startActivity(i);
            ((Activity)context).finish();


        } else*/
            this.loginStatus.setText(result);
    }
}