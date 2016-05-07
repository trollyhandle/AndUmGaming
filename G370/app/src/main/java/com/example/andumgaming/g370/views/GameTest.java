
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
import Game.Point_XY;


public class GameTest extends AppCompatActivity {

    private boolean debug = true;

    private Board board;
    private BoardView boardView;

    private Button zoomIn;
    private Button zoomOut;
    private Button zoomLeft;
    private Button zoomUp;
    private Button zoomDown;
    private Button zoomRight;
    private Button zoomReset;

    private int width, height;
    private int default_hex_size;
    private Point_XY default_center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_test);
        findSize();

        // width/9 is a good initial hex size.
        // I determined this after multiple iterations of a complex modeling algorithm...
        //      just kidding, guess and check
        default_hex_size = width / 9;
        default_center = new Point_XY(width/2, height/2);

        loadButtons();  // BUTTONS

        if(debug)System.out.println("TEST creating Board");
        if(debug)System.out.printf("TEST center at (%1$2d,%2$2d)\n", width / 2, height / 2);
        board = new Board(default_hex_size, default_center);
        board.update();

        if(debug)System.out.println("TEST creating BoardView");
        boardView = new BoardView(this, board);
        boardView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.game_layout);
        if (layout != null) {  // calms Android Studios: should not be null, I think...
            layout.addView(boardView);
        }
        else if(debug)System.out.println("VIEW ERROR dynamic add-to-layout failed");

        // bring buttons to foreground
        LinearLayout button_layout = (LinearLayout)findViewById(R.id.zoom_control_layout);
        if (button_layout != null) // calms Android Studios: should not be null, I think...
            button_layout.bringToFront();

        else if(debug)System.out.println("VIEW ERROR buttons move to foreground failed");



        loadfragment();
        FrameLayout fragmentlayout = (FrameLayout)findViewById(R.id.fragmentlayout);
        fragmentlayout.bringToFront();
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
        zoomIn = (Button)findViewById(R.id.zoomIn);
        zoomOut = (Button)findViewById(R.id.zoomOut);
        zoomLeft = (Button)findViewById(R.id.zoomLeft);
        zoomUp = (Button)findViewById(R.id.zoomUp);
        zoomDown = (Button)findViewById(R.id.zoomDown);
        zoomRight = (Button)findViewById(R.id.zoomRight);
        zoomReset = (Button)findViewById(R.id.zoomReset);
        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debug)System.out.println("BUTTON zoom in");
                board.resize(10);
                boardView.invalidate();  // force a redraw
            }
        });
        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debug)System.out.println("BUTTON zoom out");
                board.resize(-10);
                boardView.invalidate();  // force a redraw
            }
        });
        zoomLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debug)System.out.println("BUTTON move left");
                board.move(-10, 0);
                boardView.invalidate();  // force a redraw
            }
        });
        zoomRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debug)System.out.println("BUTTON move right");
                board.move(10, 0);
                boardView.invalidate();  // force a redraw
            }
        });
        zoomUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debug)System.out.println("BUTTON move up");
                board.move(0, -10);
                boardView.invalidate();  // force a redraw
            }
        });
        zoomDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debug)System.out.println("BUTTON move down");
                board.move(0, 10);
                boardView.invalidate();  // force a redraw
            }
        });
        zoomReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debug)System.out.println("BUTTON reset zoom");
                board.setHexSize(default_hex_size);
                board.setCenter(default_center);
                boardView.invalidate();  // force a redraw
                boardView.nextTurn();

            }
        });

    }

    private void loadfragment() {
        ActionPanelFragment newFragment = new ActionPanelFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.game_container,newFragment)
                        .addToBackStack(BuySubpanelFragment.class
                                .getSimpleName()).commit();

    }



}
