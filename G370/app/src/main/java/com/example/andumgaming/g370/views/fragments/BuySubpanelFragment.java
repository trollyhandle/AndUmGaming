package com.example.andumgaming.g370.views.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.andumgaming.g370.R;

/**
 * Created by Andre on 5/1/2016.
 */
public class BuySubpanelFragment extends Fragment {
    private Button closeButton;
    private Button buyRoad;
    private Button buySettlement;
    private Button buyCard;

    public static BuySubpanelFragment newInstance () {
        BuySubpanelFragment fragment = new BuySubpanelFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BuySubpanelFragment() {
        //required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.buysubpanel_fragment, container, false);

        closeButton = (Button)view.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        //implement the following buy buttons

        buyRoad = (Button)view.findViewById(R.id.road);
        buyRoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implement buying a road
            }
        });

        buySettlement = (Button)view.findViewById(R.id.settlement);
        buySettlement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implement buying a settlement
            }
        });

        buyCard = (Button)view.findViewById(R.id.card);
        buyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implement buying a card
            }
        });

        return view;
    }
}