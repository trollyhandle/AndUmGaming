package com.example.andumgaming.g370.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.andumgaming.g370.R;
import com.example.andumgaming.g370.views.GameTest;

/**
 * Created by Jeff on 4/21/2016.
 */
public class PlayFragment extends Fragment {
    private Button backButton;
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
        backButton = (Button)view.findViewById(R.id.back);
        newButton = (Button)view.findViewById(R.id.newgame);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),GameTest.class);
                getActivity().startActivity(intent);
            }
        });




        return view;
    }
}
