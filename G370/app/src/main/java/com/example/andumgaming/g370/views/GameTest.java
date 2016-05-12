
package com.example.andumgaming.g370.views;



import com.example.andumgaming.g370.R;

import android.graphics.Point;

//import android.app.FragmentTransaction;

import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.Resources;

import org.w3c.dom.Text;

import Game.Game;



public class GameTest extends AppCompatActivity implements ToastListener{

    private boolean debug = true;

    private Game game;
   // private Board board;


    private Button zoomIn, zoomOut;
    private Button zoomLeft, zoomRight;
    private Button zoomUp, zoomDown;
    private Button zoomReset;

    private Button BuyRoad;
    private Button BuySettlement;
    private Button BuyCity;
    private Button EndTurn;

    private TextView CurrentPlayer;
    private TextView playerid;
    private TextView player1;
    private TextView player2;
    private TextView player3;
    private TextView player4;

    private int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_test);
        findSize();

        //loadFragment();  // TRANSACTION FRAGMENT
        loadButtons();  // ZOOM BUTTONS

        newGame();
        // TODO or load from server
        // loadGame();


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

    //    loadfragment();
        game.getView().setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        game.getBoard().setListener(this);

        // TODO only if starting a new game
//        setupGame();
    }




    private void newGame()
    {
        game = new Game(this, width, height);
    }

    private void loadGame()
    {
        // TODO get data from server and init the game with it
        return;
    }

    private void setupGame()
    {
        // TODO add interactions so the user knows what's going on!!

        for (int player = 1; player <= 4; player++) {
            game.setTurn(player);
            // if it's the phone owner's turn...
                game.setBuildState(Game.BUILD.SETTLEMENT);

                // place a settlement anywhere valid
                // TODO user information interaction


                // place a road anywhere next to that settlement

                // post to server

            // else
                // wait for server

        }
        for (int player = 4; player >= 1; --player) {

            // same as above

        }

        // generate resources for players based on second settlement


    }



    private void findSize()
    {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        width = size.x;
        height = size.y;
        if(debug)System.out.println("TEST Window size: " + width + " by " + height);
    }


    public void ToastMessage(String message) {
        int duration=Toast.LENGTH_SHORT;
        Toast toast= Toast.makeText(this,message,duration);
        toast.show();
    }



    private void loadButtons() {
        zoomReset = (Button) findViewById(R.id.zoomReset);
        BuyRoad = (Button) findViewById(R.id.buyroad);
        BuySettlement = (Button) findViewById(R.id.buyhouse);
        EndTurn = (Button) findViewById(R.id.endturn);
        BuyCity = (Button) findViewById(R.id.buycity);
        CurrentPlayer = (TextView) findViewById(R.id.currentplayer);
        playerid = (TextView) findViewById(R.id.playerid);
        player1 = (TextView) findViewById(R.id.player1);
        player2 = (TextView) findViewById(R.id.player2);
        player3 = (TextView) findViewById(R.id.player3);
        player4 = (TextView) findViewById(R.id.player4);

        zoomReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debug)System.out.println("BUTTON reset zoom");
                game.resetZoom();
            }
        });
/*
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
*/
        BuyRoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuySettlement.getBackground().clearColorFilter();
                BuyCity.getBackground().clearColorFilter();

                //if build state isnt road
                if (game.getBuildState() != Game.BUILD.ROAD) {
                    game.setBuildState(Game.BUILD.ROAD);
                    v.getBackground()
                            .setColorFilter(getResources().getColor(R.color.buy_highlight), PorterDuff.Mode.SRC_ATOP);
                }
                //if build state IS road, unclick
                else {
                    game.setBuildState(Game.BUILD.NONE);
                    v.getBackground().clearColorFilter();
                }
                v.invalidate();
            }
        });
        BuySettlement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyRoad.getBackground().clearColorFilter();
                BuyCity.getBackground().clearColorFilter();

                //if build state isnt settlement
                if (game.getBuildState() != Game.BUILD.SETTLEMENT) {
                    game.setBuildState(Game.BUILD.SETTLEMENT);
                    v.getBackground()
                            .setColorFilter(getResources().getColor(R.color.buy_highlight), PorterDuff.Mode.SRC_ATOP);
                }
                //if build state IS settlement, unclick
                else {
                    game.setBuildState(Game.BUILD.NONE);
                    v.getBackground().clearColorFilter();
                }
                v.invalidate();
            }
        });

        BuyCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyRoad.getBackground().clearColorFilter();
                BuySettlement.getBackground().clearColorFilter();

                //if build state isnt city
                if (game.getBuildState() != Game.BUILD.CITY) {
                    game.setBuildState(Game.BUILD.CITY);
                    v.getBackground()
                            .setColorFilter(getResources().getColor(R.color.buy_highlight), PorterDuff.Mode.SRC_ATOP);
                }
                //if build state IS city, unclick
                else {
                    game.setBuildState(Game.BUILD.NONE);
                    v.getBackground().clearColorFilter();
                }
                v.invalidate();
            }
        });

        EndTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyCity.getBackground().clearColorFilter();
                BuySettlement.getBackground().clearColorFilter();
                BuyRoad.getBackground().clearColorFilter();

                game.nextTurn();

                if (game.getTurn() == 0) {
                    EndTurn.getBackground().setColorFilter(Game.PLAYERS.NONE.col, PorterDuff.Mode.SRC_ATOP);
                    playerid.setText("Player 1");
                    player1.setText("Player 1");
                    player1.setTextColor(Game.PLAYERS.ONE.col);
                }
                else if (game.getTurn() == 1) {
                    EndTurn.getBackground().setColorFilter(Game.PLAYERS.ONE.col, PorterDuff.Mode.SRC_ATOP);
                    playerid.setText("Player 1");
                    player1.setText("Player 1");
                    player1.setTextColor(Game.PLAYERS.ONE.col);
                }
                else if (game.getTurn() == 2) {
                    EndTurn.getBackground().setColorFilter(Game.PLAYERS.TWO.col, PorterDuff.Mode.SRC_ATOP);
                    playerid.setText("Player 2");
                    player2.setText("Player 2");
                    player2.setTextColor(Game.PLAYERS.TWO.col);
                }
                else if (game.getTurn() == 3) {
                    EndTurn.getBackground().setColorFilter(Game.PLAYERS.THREE.col, PorterDuff.Mode.SRC_ATOP);
                    playerid.setText("Player 3");
                    player3.setText("Player 3");
                    player3.setTextColor(Game.PLAYERS.THREE.col);
                }
                else if (game.getTurn() == 4) {
                    EndTurn.getBackground().setColorFilter(Game.PLAYERS.FOUR.col, PorterDuff.Mode.SRC_ATOP);
                    playerid.setText("Player 4");
                    player4.setText("Player 4");
                    player4.setTextColor(Game.PLAYERS.FOUR.col);
                }
                v.invalidate();
            }
        });
    }
}
