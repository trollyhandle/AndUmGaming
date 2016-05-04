package com.example.andumgaming.g370.views.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Button;

import com.example.andumgaming.g370.R;

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
        rootView = inflater.inflate(R.layout.tutorial_images, container, false);
        image = (ImageView) rootView.findViewById(R.id.page_zero);

        backButton = (Button)rootView.findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        return rootView;
    }


    public void setTutorialImage(int imageid)
    {
        image = (ImageView) rootView.findViewById(imageid);
    }
}