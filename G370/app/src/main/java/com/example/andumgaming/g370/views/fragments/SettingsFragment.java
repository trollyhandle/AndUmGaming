package com.example.andumgaming.g370.views.fragments;
import android.os.Bundle;
import android.view.View;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.CheckBox;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.andumgaming.g370.R;


/**
 * Created by student on 4/10/16.
 */
public class SettingsFragment extends Fragment {
    private CheckBox bgMusic;


    public SettingsFragment() {
        // Required empty public constructor
    }

    // Fragments are typically instantiated through a static initializer like the one below. This
    // allows for the bundling of information into args that can be added to the fragment to persist
    // through the fragment's lifecycle.
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        // Assigning layout file instances of these UI elements to their java counterparts
        bgMusic = (CheckBox)view.findViewById(R.id.checkbox_music);

        // A click listener is defined to handle the callback from the RecipeAsyncTask
        bgMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating an inline concrete implementation of the listener to handle callback on the main thread
            }
        });

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
