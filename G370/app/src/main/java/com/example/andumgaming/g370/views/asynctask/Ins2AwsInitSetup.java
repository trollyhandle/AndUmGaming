package com.example.andumgaming.g370.views.asynctask;

/**
 * Created by ross on 5/4/2016.
 */

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import Interface.ICallBackListener;

public class Ins2AwsInitSetup extends AsyncTask<Integer,Void,String> {
    private ICallBackListener listener;

    public Ins2AwsInitSetup(ICallBackListener listener) {
        this.listener = listener;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(Integer ... args) {

        int destQ = args[0];
        int destR = args[1];
        int sourceQ = args[2];
        int sourceR = args[3];
        int direction= args[4];
        int playerID = args[5];
        try{
            String link="http://g370.duckdns.org/gameinit.php";
            String data  = URLEncoder.encode("destQ", "UTF-8") + "=" + destQ;
            data += "&" + URLEncoder.encode("destR", "UTF-8") + "=" + destR;
            data += "&" + URLEncoder.encode("sourceQ", "UTF-8") + "=" + sourceQ;
            data += "&" + URLEncoder.encode("sourceR", "UTF-8") + "=" + sourceR;
            data += "&" + URLEncoder.encode("direction", "UTF-8") + "=" + direction;
            data += "&" + URLEncoder.encode("playerID", "UTF-8") + "=" + playerID;

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