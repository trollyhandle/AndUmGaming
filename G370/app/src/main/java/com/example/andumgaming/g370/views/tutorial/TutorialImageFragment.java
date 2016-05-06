package com.example.andumgaming.g370.views.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.andumgaming.g370.R;
import com.example.andumgaming.g370.views.FullscreenActivity;

// Instances of this class are fragments representing a single
// object in our collection.
public class TutorialImageFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    View rootView;
    ImageView image;

    private Button backButton;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        rootView = inflater.inflate(getArguments().getInt("viewID"), container, false);
        image = (ImageView) rootView.findViewById(getArguments().getInt("imageID"));
        backButton = (Button)rootView.findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //Intent i = new Intent(getActivity().getApplicationContext(), FullscreenActivity.class);
               //getActivity().startActivity(i);
               getActivity().onBackPressed();

            }
        });
        return rootView;
    }


    /*public TutorialImageFragment setTutorialImage(int imageid)
    {
        image = (ImageView) rootView.findViewById(imageid);
        return this;
    }*/

    public TutorialImageFragment newInstance(int viewid,int imageid) {
        TutorialImageFragment tif = new TutorialImageFragment();
        Bundle b = new Bundle();
        b.putInt("viewID", viewid);
        b.putInt("imageID",imageid);
        tif.setArguments(b);
        return tif;
    }
}
