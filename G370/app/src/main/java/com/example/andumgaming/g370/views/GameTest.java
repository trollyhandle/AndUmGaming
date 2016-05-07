
package com.example.andumgaming.g370.views;

import com.example.andumgaming.g370.R;
import com.example.andumgaming.g370.views.fragments.ActionPanelFragment;
import com.example.andumgaming.g370.views.fragments.BuySubpanelFragment;

import android.content.Intent;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

//import android.app.FragmentTransaction;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import Game.Board;
import Game.BoardView;
import Game.Game;
import Game.Point_XY;

public class GameTest extends AppCompatActivity {

    private boolean debug = true;

    private Game game;

    private Button zoomIn;
    private Button zoomOut;
    private Button zoomLeft;
    private Button zoomUp;
    private Button zoomDown;
    private Button zoomRight;
    private Button zoomReset;

    private int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_test);
        findSize();

        loadFragment();  // TRANSACTION FRAGMENT
        loadButtons();  // ZOOM BUTTONS



        game = new Game(this, width, height);

        // add game - board and view - to layout
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.game_layout);
        if (layout != null)  // calms Android Studios: should not be null, I think...
            layout.addView(game.getView());
        else if(debug)System.out.println("VIEW ERROR dynamic add-to-layout failed");

        // bring buttons to foreground
        LinearLayout button_layout = (LinearLayout)findViewById(R.id.zoom_control_layout);
        if (button_layout != null) // calms Android Studios: should not be null, I think...
            button_layout.bringToFront();
        else if(debug)System.out.println("VIEW ERROR buttons move to foreground failed");

        // bring fragment to foreground
        FrameLayout fragmentLayout = (FrameLayout)findViewById(R.id.fragmentlayout);
        if (fragmentLayout != null) // calms Android Studios: should not be null, I think...
            fragmentLayout.bringToFront();
        else if(debug)System.out.println("VIEW ERROR fragment bring to foreground failed");

        game.getView().setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void findSize()
    {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        width = size.x;
        height = size.y;
        if(debug)System.out.println("TEST Window size: " + width + " by " + height);
    }

    private void loadButtons()
    {
        zoomIn      = (Button)findViewById(R.id.zoomIn);
        zoomOut     = (Button)findViewById(R.id.zoomOut);
        zoomLeft    = (Button)findViewById(R.id.zoomLeft);
        zoomUp      = (Button)findViewById(R.id.zoomUp);
        zoomDown    = (Button)findViewById(R.id.zoomDown);
        zoomRight   = (Button)findViewById(R.id.zoomRight);
        zoomReset   = (Button)findViewById(R.id.zoomReset);

        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debug)System.out.println("BUTTON zoom in");
                game.resize(10);
            }
        });
        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debug)System.out.println("BUTTON zoom out");
                game.resize(-10);
            }
        });
        zoomLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debug)System.out.println("BUTTON move left");
//                game.move(-10, 0);
                game.setBuildState(Game.BUILD.SETLLEMENT);
            }
        });
        zoomRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debug)System.out.println("BUTTON move right");
//                game.move(10, 0);
                game.setBuildState(Game.BUILD.ROAD);
            }
        });
        zoomUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debug)System.out.println("BUTTON move up");
//                game.move(0, -10);
                game.setBuildState(Game.BUILD.CITY);
            }
        });
        zoomDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debug)System.out.println("BUTTON move down");
                game.move(0, 10);
            }
        });
        zoomReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debug)System.out.println("BUTTON reset zoom");
                game.resetZoom();
                game.nextTurn();

            }
        });


        // TODO if the transaction buttons can be made to work here they're fairly easy
    }

    private void loadFragment() {
        ActionPanelFragment newFragment = new ActionPanelFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.game_container, newFragment)
                .addToBackStack(BuySubpanelFragment.class.getSimpleName())
                .commit();


    }



}
