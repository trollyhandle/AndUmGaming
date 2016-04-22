package com.example.andumgaming.g370.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.andumgaming.g370.R;


/**
 * Created by Jeff on 4/21/2016.
 */
public class AboutFragment extends Fragment {

    public AboutFragment(){

    }

    public static AboutFragment newInstance(){
        AboutFragment fragment = new AboutFragment();
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

        View view = inflater.inflate(R.layout.about_fragment, container, false);

        return view;
    }


}
