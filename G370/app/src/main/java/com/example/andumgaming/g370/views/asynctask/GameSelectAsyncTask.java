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

public class GameSelectAsyncTask extends AsyncTask<String,Void,String> {
    private ICallBackListener listener;

    public GameSelectAsyncTask(ICallBackListener listener)
    {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... arg0) {

        try{
            String player1 = (String)arg0[0];
            String player2 = (String)arg0[1];
            String player3 = (String)arg0[2];
            String player4 = (String)arg0[3];

            String link="http://g370.duckdns.org/gameselect.php";
            String data  = URLEncoder.encode("player1", "UTF-8") + "=" + URLEncoder.encode(player1, "UTF-8");
            data += "&" + URLEncoder.encode("player2", "UTF-8") + "=" + URLEncoder.encode(player2, "UTF-8");
            data += "&" + URLEncoder.encode("player3", "UTF-8") + "=" + URLEncoder.encode(player3, "UTF-8");
            data += "&" + URLEncoder.encode("player4", "UTF-8") + "=" + URLEncoder.encode(player4, "UTF-8");

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