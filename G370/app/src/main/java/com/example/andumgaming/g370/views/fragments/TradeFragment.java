package com.example.andumgaming.g370.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.andumgaming.g370.R;

/**
 * Created by Andre on 5/3/2016.
 */
public class TradeFragment extends Fragment {
    private Button player1;
    private Button player2;
    private Button player3;
    private Button player4;
    private Button player5;

    public static TradeFragment newInstance() {
        TradeFragment fragment = new TradeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public TradeFragment() {
        //required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.id., container, false);

        player1 = (Button)view.findViewById(R.id.)
    }
*/

}
