package com.example.andumgaming.g370.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.andumgaming.g370.R;
import com.example.andumgaming.g370.views.LoginActivity;
import com.example.andumgaming.g370.views.asynctask.LoginAsyncTask;


import Interface.BackStackLisnter;

/**
 * Created by Jeff on 4/21/2016.
 */
public class SplashFragment extends Fragment implements BackStackLisnter {


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
                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                getActivity().startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    return view;
    }

    @Override
    public void onBackButtonPressed() {
        // if they press the back button while loading it just quits
        getActivity().finish();
    }
}
