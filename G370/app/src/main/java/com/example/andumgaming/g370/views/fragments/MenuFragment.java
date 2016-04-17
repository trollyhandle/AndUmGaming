package com.example.andumgaming.g370.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.andumgaming.g370.R;

/**
 * Created by Jeff on 4/14/16.
 */
public class MenuFragment extends Fragment{
    private Button playButton;
    private Button settingsButton;
    private Button tutorialButton;
    private Button leaderButton;
    private Button aboutButton;
    private OnFragmentEvent onFragmentEvent;

    public MenuFragment(){

    }


    public static MenuFragment newInstance(){
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){

        View view = inflater.inflate(R.layout.menu_fragment,container, false);

        playButton = (Button)view.findViewById(R.id.PlayButton);
        settingsButton = (Button)view.findViewById(R.id.SettingsButton);
        tutorialButton = (Button)view.findViewById(R.id.TutorialButton);
        aboutButton = (Button)view.findViewById(R.id.AboutButton);
        leaderButton = (Button)view.findViewById(R.id.SoonButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onFragmentEvent !=null){
                   // onFragmentEvent.onEvent(item);
                }

            }
        });
        return view;
        }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }

    public interface OnFragmentEvent{
        //
    }

    }

