package com.example.andumgaming.g370.views.fragments;


import android.content.Intent;
import android.support.v4.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.andumgaming.g370.R;

import com.example.andumgaming.g370.views.MusicService;

import com.example.andumgaming.g370.views.TutorialActivity;

import Interface.BackStackLisnter;


/**
 * Created by Jeff on 4/14/16.
 */
public class MenuFragment extends Fragment implements BackStackLisnter {
    private Button playButton;
    private Button settingsButton;
    private Button tutorialButton;
    private Button leaderButton;
    private Button aboutButton;
    static final int ZTIME = 1500;
    private long mBackedPressed;

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
        leaderButton = (Button)view.findViewById(R.id.Exit);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("MENU : pre-add " + getFragmentManager().getBackStackEntryCount());

                SettingsFragment newFragment = new SettingsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.container, newFragment)
                        .addToBackStack(SettingsFragment.class
                                .getSimpleName()).commit();


            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayFragment newFragment = new PlayFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.container, newFragment)
                        .addToBackStack(PlayFragment.class
                                .getSimpleName()).commit();
            }
        });

        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TutorialActivity newFragment = new TutorialActivity();
                Intent i = new Intent(getActivity().getApplicationContext(), TutorialActivity.class);
                i.putExtra("isMusicPlaying", MusicService.mPlayer.isPlaying());
                getActivity().startActivity(i);
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutFragment newFragment = new AboutFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.container, newFragment)
                        .addToBackStack(AboutFragment.class
                                .getSimpleName()).commit();
            }
        });



        return view;
        }

    @Override
    public void onBackButtonPressed(){
        // this checks for a double back press to close the app from the main menu
        if (mBackedPressed + ZTIME > System.currentTimeMillis())
        {
            getActivity().finish();
            return;
        }
        mBackedPressed = System.currentTimeMillis();

    }





    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }


    }

