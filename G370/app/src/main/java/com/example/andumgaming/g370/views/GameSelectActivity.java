package com.example.andumgaming.g370.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.andumgaming.g370.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GameSelectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_select);

        Intent i = new Intent(getApplicationContext(), GameActivity.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("loadJSON")) {  // then we have a json file to parse right away
            InputStream is = getResources().openRawResource(R.raw.sample_game);
            i.putExtra("JSON", readJSONfile(is));
        }

        startActivity(i);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        //delayedHide(100);
    }

    public String readJSONfile(InputStream ins)
    {
        // File-reading code thanks to Teamnull370 (https://github.com/Teamnull370)
        String json = "";
        try {
            String line;
            StringBuffer stringBuffer = new StringBuffer();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins));

            if (ins != null) {
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                    stringBuffer.append("\n");
                }
            }
            json = stringBuffer.toString();
            ins.close();

        }
        catch (Exception e) {
            //Log.e("_raws","error");
            System.out.println("Error: " + e);
        }
        return json;
    }
}
