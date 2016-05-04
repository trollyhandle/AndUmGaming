package com.example.andumgaming.g370.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;


import com.example.andumgaming.g370.R;

/**
 * Created by Andre on 4/30/2016.
 */
public class PlayerStatusesFragment extends Fragment {


    public static PlayerStatusesFragment newInstance () {
        PlayerStatusesFragment fragment = new PlayerStatusesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    public PlayerStatusesFragment() {
        //required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.playerstatuses_fragment, container, false);

        //display resources

        //need a way for resources to be updated whenever gained, spent, or traded

        return view;
    }

}