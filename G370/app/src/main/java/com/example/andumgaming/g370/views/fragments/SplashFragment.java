package com.example.andumgaming.g370.views.fragments;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.andumgaming.g370.R;



import java.security.SecurityPermission;

/**
 * Created by Jeff on 4/21/2016.
 */
public class SplashFragment extends Fragment {

    public SplashFragment(){

    }

    public static SplashFragment newInstance(){
        SplashFragment fragment = new SplashFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){

        View view = inflater.inflate(R.layout.splash,container, false);

        final ImageView iv = (ImageView)view.findViewById(R.id.sanic);
        final Animation an = AnimationUtils.loadAnimation(getContext(),R.anim.rotate);

        iv.startAnimation(an);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                MenuFragment newFragment = new MenuFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.container, newFragment).addToBackStack(MenuFragment.class.getSimpleName()).commit();


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    return view;
    }

}
