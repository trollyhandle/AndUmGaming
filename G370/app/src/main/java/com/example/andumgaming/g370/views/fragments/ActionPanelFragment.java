package com.example.andumgaming.g370.views.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.support.v4.app.Fragment;
import android.widget.HorizontalScrollView;


import com.example.andumgaming.g370.R;

/**
 * Created by Andre on 4/30/2016.
 */
public class ActionPanelFragment extends Fragment {

    private Button BuySubpanelButton;

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





        BuySubpanelButton = (Button)view.findViewById(R.id.BuySubpanel);

        BuySubpanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuySubpanelFragment newFragment = new BuySubpanelFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.game_container, newFragment)
                        .addToBackStack(BuySubpanelFragment.class
                                .getSimpleName()).commit();
            }
        });








        return view;
    }

}