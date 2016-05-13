package com.example.andumgaming.g370.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.andumgaming.g370.R;
import com.example.andumgaming.g370.views.GameActivity;
import com.example.andumgaming.g370.views.GameActivity;
import com.example.andumgaming.g370.views.GameSelectActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Jeff on 4/21/2016.
 */
public class PlayFragment extends Fragment {
    private Button create_button, join_button, watch_button;
    private Button newButton;

    public PlayFragment(){

    }

    private static PlayFragment newInstance() {
        PlayFragment fragment = new PlayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.play_fragment, container, false);
        create_button = (Button)view.findViewById(R.id.create_game);
        join_button = (Button)view.findViewById(R.id.join_game);
        watch_button = (Button)view.findViewById(R.id.watch_game);

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(getActivity(), GameSelectActivity.class);
//                getActivity().startActivity(intent);

                Intent i = new Intent(getActivity(), GameActivity.class);

//                Bundle extras = getActivity().getIntent().getExtras();
//                if (extras != null && extras.getBoolean("loadJSON")) {  // then we have a json file to parse right away
//                    InputStream is = getResources().openRawResource(R.raw.sample_game);
//                    i.putExtra("JSON", readJSONfile(is));
//                }

                startActivity(i);

            }
        });

        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), GameSelectActivity.class);
//                intent.putExtra("loadJSON", true);
//                getActivity().startActivity(intent);

                Intent i = new Intent(getActivity(), GameActivity.class);

//                Bundle extras = getActivity().getIntent().getExtras();
//                if (extras != null && extras.getBoolean("loadJSON")) {  // then we have a json file to parse right away
                InputStream is = getResources().openRawResource(R.raw.sample_game);
                i.putExtra("JSON", readJSONfile(is));
//                }

                startActivity(i);
            }
        });

        watch_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GameActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return view;
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
