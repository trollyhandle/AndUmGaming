package com.example.andumgaming.g370.views;

import android.annotation.SuppressLint;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.os.IBinder;
import android.content.Intent;
import android.content.Context;

/* Login required */
import android.widget.TextView;
import android.widget.EditText;


import com.example.andumgaming.g370.R;

import com.example.andumgaming.g370.views.asynctask.LoginAsyncTask;

import com.example.andumgaming.g370.views.fragments.MenuFragment;

import java.util.List;

import Interface.BackStackLisnter;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
   // private SlashActivity splashFragment;
    private MenuFragment menuFragment;
    private BackStackLisnter backStackLisnter;


    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();


        Fragment cuurentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if(cuurentFragment instanceof MenuFragment) {

            for (Fragment f:fragments){
                if (f instanceof BackStackLisnter){
                    backStackLisnter = (BackStackLisnter)f;
                }
            }
        }

        if (backStackLisnter != null) {
            backStackLisnter.onBackButtonPressed();
        }else {
            super.onBackPressed();
        }
    }


    /* initialize background music */
    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon,Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    public void PlayMusic()
    {
        mServ.resumeMusic();
    }
    public void PauseMusic()
    {
        mServ.pauseMusic();
    }

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };



    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        //splashFragment = SlashActivity.newInstance();
         menuFragment = MenuFragment.newInstance();
        //menuFragment.setOnFr

        /* Login Activity */

        usernameField = (EditText)findViewById(R.id.editText1);
        passwordField = (EditText)findViewById(R.id.editText2);

        status = (TextView)findViewById(R.id.textView7);


        mContentView = findViewById(R.id.container);

        getSupportFragmentManager().beginTransaction().add(R.id.container,menuFragment).addToBackStack(MenuFragment.class.getSimpleName()).commit();


        /*initialize buttons*/


        /*start the music*/
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);
    }

    /* Login Activity */
    private EditText usernameField,passwordField;
    private TextView status,method;

    public void loginPost(View view){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        method.setText("Post Method");
        new LoginAsyncTask(this,status,1).execute(username,password);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }



    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Schedule a runnable to remove the status and navigation bar after a delay

        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (MusicService.mPlayer.isPlaying()) {
            MusicService.mPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MusicService.mPlayer != null && ! MusicService.mPlayer.isPlaying()) {
            MusicService.mPlayer.start();
        }
    }

    @Override
    public void onDestroy() {
        MusicService.mPlayer.stop();
        MusicService.mPlayer.release();
        super.onDestroy();
    }
}
