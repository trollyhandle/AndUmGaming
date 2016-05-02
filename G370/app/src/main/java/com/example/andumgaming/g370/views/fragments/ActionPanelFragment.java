package com.example.andumgaming.g370.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.Fragment;


import com.example.andumgaming.g370.R;

/**
 * Created by Andre on 4/30/2016.
 */
public class ActionPanelFragment extends Fragment {

    private Button closeButton;

    public static ActionPanelFragment newInstance () {
        ActionPanelFragment fragment = new ActionPanelFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    public ActionPanelFragment() {
        //required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.actionpanel_fragment, container, false);
        closeButton = (Button)view.findViewById(R.id.close);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

}