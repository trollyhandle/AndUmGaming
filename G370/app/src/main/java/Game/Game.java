package Game;

import android.app.Activity;
import android.content.res.Resources;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.andumgaming.g370.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.util.Random;

import Interface.ToastListener;


/**
 * Board and Player manager class.
 * Author: Tyler Holland
 */
public class Game {
    private boolean debug = true;

    // RESOURCE PAINT_COLOR LOOKUP
    public enum RESOURCES {
        BLANK   (0xffffffff), WHEAT   (0xffE9AA13), WOOD    (0xff83440D),
        ORE     (0xff4E656A), BRICK   (0xffCB0501), SHEEP   (0xff1CBC00);
        public final int col; RESOURCES(int color) { col = color; }
        static int getColor(int i) { switch(i) {
            case 1: return WHEAT.col; case 2: return WOOD.col;
            case 3: return ORE.col; case 4: return BRICK.col;
            case 5: return SHEEP.col; } return BLANK.col; }
        static int index(RESOURCES r) { switch(r) {
            case WHEAT: return 1; case WOOD: return 2;
            case ORE: return 3; case BRICK: return 4;
            case SHEEP: return 5; } return 0; }
    }

    // PLAYER PAINT_COLOR LOOKUP
    public enum PLAYERS {
        NONE    (0xffFFFFFF),
        ONE     (0xff3AF2A9), TWO   (0xffD65466),
        THREE   (0xff9835F1), FOUR  (0xffFD6D27);
        public final int col; PLAYERS(int value) { col = value; }
        public static int getColor(int i) { switch(i) {
            case 1: return ONE.col; case 2: return TWO.col;
            case 3: return THREE.col; case 4: return FOUR.col;
        } return NONE.col; }
    }

    public enum TEXT_COLORS {
        WHITE(0xffFFFFFF), RED(0xffFF0F00), BLACK(0xff000000);
        public final int col; TEXT_COLORS(int value) { col = value; }
    }

    public enum BUILD {
        NONE, ROAD, SETTLEMENT, CITY; //, KNIGHT;
    }

    public enum GAMESTATE {
        FIRSTTURN, REGULAR, GAMEEND;
    }

    private INextTurnable iNextTurnable;

    @Expose
    private GAMESTATE gamestate;
    private BUILD build;
    private Point_QR firstRoadPt;
    @Expose private int turn;

    @Expose private Player[] players;
    @Expose private Board board;
    private boolean isFirstPlacementDone;
    private boolean hasRolledThisTurn;

    private BoardView view;
    private TextView wheat, wood, ore, brick, sheep;

    private int width, height;
    private int default_hexsize;
    private Point_XY default_center;
    private Resources r;

    private final int dist_to_begin_move = 10;
    private int touch_x, touch_y, pinch_distance;
    private boolean moving, zooming;
    private Point_QR selected;

    private ToastListener listener;

    public void setListener(ToastListener listener) {
        this.listener = listener;
        if (board != null)
            board.setListener(listener);
    }

    public Board getBoard() {
        return board;
    }

    public interface INextTurnable
    {
        void onNextTurn();
    }


    public Game(Activity parent, int width, int height)
    {
        board = new Board();
        players = new Player[5];
        // player 0 is the nobody player
        players[1] = new Player();
        players[2] = new Player();
        players[3] = new Player();
        players[4] = new Player();

        turn = 1;
        gamestate = GAMESTATE.FIRSTTURN;

        init(parent, width, height);
    }
    public void init(Activity parent, int width, int height)
    {
        r = parent.getResources();
        this.width = width; this.height = height;

        isFirstPlacementDone = false;

        // width/9 is a good initial hex size.
        // I determined this after multiple iterations of a complex modeling algorithm...
        //      just kidding, guess and check!
        default_hexsize = width / 9;
        default_center = new Point_XY(width/2, height/2);

        if(debug)System.out.println("GAME creating Board");
        if(debug)System.out.printf("GAME center at (%1$2d,%2$2d)\n", width / 2, height / 2);

        if(debug)System.out.println("GAME creating BoardView");
        view = new BoardView(parent, board);

        build = BUILD.NONE;

        initResourceTabs(parent);
        setupTouchListener();

        // TODO at some point (maybe here, maybe not) call to server
        if (debug) System.out.println(board);

        board.init(default_hexsize, default_center);
    }

    public void setiNextTurnable(INextTurnable i)
    {
        iNextTurnable = i;
    }

    public void nextTurn()
    {
        if(gamestate == GAMESTATE.FIRSTTURN && turn == 4 && !isFirstPlacementDone) {
            turn = 4;
            players[turn].setFirstSettlementPlaced(false);
            isFirstPlacementDone = true;
        }
        else if(gamestate == GAMESTATE.FIRSTTURN && isFirstPlacementDone) {
            turn--;
            if(turn<=0)
            {
                listener.ToastMessage("All players are done with placement, good luck!");
                setGameState(GAMESTATE.REGULAR);
                nextTurn();
            }
            else
                players[turn].setFirstSettlementPlaced(false);

        }
        else
            turn = turn == 4? 1: turn + 1;
        build = BUILD.NONE;

        // set resources to the current player's stats
//        if(gamestate != GAMESTATE.FIRSTTURN)
            refreshResourceCounts();  // dont do this till players are implemented

        if(iNextTurnable!=null) // hopefully avoid nullpointerexceptions
            iNextTurnable.onNextTurn();

        // set cards too

        hasRolledThisTurn = false;
    }

    public void roll()
    {
        if (debug)System.out.println("GAME rolling (state=" + gamestate + ")");
        if(gamestate != GAMESTATE.REGULAR)
                return;
        if (hasRolledThisTurn) {
            listener.ToastMessage("You've already rolled!");
            return;
        }
        hasRolledThisTurn = true;
        Random rand = new Random();

        int die = rand.nextInt(6) + rand.nextInt(6) + 2;
        for (int i = 1; i < players.length; i++) {
            int[] generatedRes = board.getGeneratedResByHexForPlayer(i, die);
            if(debug)System.out.println("Player " + i + " gained " + generatedRes[0]+generatedRes[1]+generatedRes[2]+generatedRes[3]+generatedRes[4]+generatedRes[5]);
            players[i].addResources(generatedRes);
        }
        listener.ToastMessage("Rolled a " + die);
        if(debug)System.out.println("Rolled a " + die);
        if(debug)System.out.println("GAME roll refreshing resources");
        refreshResourceCounts();

    }

    public int getTurn() { return turn; }
    public void setTurn(int turn) {
        this.turn = turn;
        if(iNextTurnable!=null) // hopefully avoid nullpointerexceptions
            iNextTurnable.onNextTurn();
    }

    public void setBuildState(BUILD type) {
        if (turn == 0) return;  // if no player currently ready
        build = type;
        firstRoadPt = null; // just to make sure
        if (selected != null)
            board.deselectSettlement(selected);
        view.invalidate();
    }

    public BUILD getBuildState(){
        return build;
    }

    public GAMESTATE getGameState(){
        return gamestate;
    }
    public void setGameState(GAMESTATE type) {
        gamestate = type;
    }

    public void click(Point_QR hex) {
        // this is where decisions will be made
        if (debug) System.out.println("GAME click at " + hex);
        if (debug) System.out.println("GAME build state: " + build);
        boolean success = false;
        if(gamestate == GAMESTATE.GAMEEND)
        {
            listener.ToastMessage("Game is  over");
            return;
        }

        // are we placing a settlement?
        if (build == BUILD.SETTLEMENT) {
            if(gamestate == GAMESTATE.FIRSTTURN) {
                if(!players[turn].getFirstSettlementPlaced()) {
                    success = board.placeSettlement(hex.q(), hex.r(), turn);
                    if(success)
                    {
                        players[turn].setFirstSettlementPlaced(true);
                    }
                }
                else
                    listener.ToastMessage("You can only place one settlement right now!");
            }
            else {
                if (players[turn].canBuySettlement()) {
                    success = board.buildSettlement(hex.q(),hex.r(),turn);
                    if (success) {
                        players[turn].buySettlement();
                        refreshResourceCounts();
                    }
                }
                else
                    listener.ToastMessage("Not enough resources!");
            }

        }
        // are we placing a city?
        else if (build == BUILD.CITY) {
            if(gamestate == GAMESTATE.FIRSTTURN)
            {
                if(listener != null)
                    listener.ToastMessage("You cannot build a city during initial placement!");
            }
            else {
                if (players[turn].canBuyCity()) {
                    success = board.buildCity(hex.q(), hex.r(), turn);
                    if (success) {
                        players[turn].buyCity();
                        refreshResourceCounts();
                    }
                }
                else
                    listener.ToastMessage("Not enough resources!");
            }
        }

        // are we placing a road?
        else if (build == BUILD.ROAD) {
            if (firstRoadPt == null) {  // no source selected yet
                firstRoadPt = hex;
                board.selectSettlement(selected = hex);
            }
            else {
                if (debug) System.out.println("Road from " + firstRoadPt + " to " + hex);
                if(!players[turn].getFirstSettlementPlaced())
                    listener.ToastMessage("Place a settlement first!");
                else {
                    if (players[turn].canBuyRoad()) {
                        success = board.buildRoad(firstRoadPt, hex, turn);
                        if (success && gamestate != GAMESTATE.FIRSTTURN) {
                            players[turn].buyRoad();
                            refreshResourceCounts();
                        }
                    }
                    else
                        listener.ToastMessage("Not enough resources!");
                }

                firstRoadPt = null;  // reset for next road
                board.deselectSettlement(selected);
                if(success && gamestate == GAMESTATE.FIRSTTURN)
                {
                    nextTurn();
                }
            }
        }
        // are we playing a card?
        // are we moving the robber?

        /*
        Check for the end of the game.
        This is done in the Click so that the game ends as soon as a player gains their 10th victory point, rather than waiting for a turn to cycle over.
         */
        if(debug) System.out.println("Player " + turn + "has "+ board.countVictoryPoints(turn)+ " victory points.");
        if(board.countVictoryPoints(turn) >=10) //10 victory points is the number required to win
        {
            if (debug) System.out.println("Player " + turn + " has won.");
            setGameState(GAMESTATE.GAMEEND);
            String endMessage = "Player "+ turn + "has won! pushing any button will return you to the main menu.";
            listener.ToastMessage(endMessage);
        }
    }


    /* *********************** */
    // Board Manipulation Functions
    public void move(int dx, int dy)
    {
        board.move(dx, dy);
        view.invalidate();
    }
    public void setCenter(Point_XY newCenter)
    {
        board.setCenter(newCenter);
        view.invalidate();
    }
    public void resize(int ds)
    {
        board.resize(ds);
        view.invalidate();
    }
    public void setSize(int size)
    {
        board.setHexSize(size);
        view.invalidate();
    }
    public void resetZoom()
    {
        board.setHexSize(default_hexsize);
        board.setCenter(default_center);
        view.invalidate();
    }


     /* *********************** */
    public BoardView getView() { return view; }

    private void initResourceTabs(Activity parent)
    {
        wheat   = (TextView)parent.findViewById(R.id.grainint);
        wood    = (TextView)parent.findViewById(R.id.lumberint);
        ore     = (TextView)parent.findViewById(R.id.oreint);
        brick   = (TextView)parent.findViewById(R.id.brickint);
        sheep   = (TextView)parent.findViewById(R.id.woolint);
    }

    private void refreshResourceCounts()
    {
        if(debug)System.out.println("GAME refreshing player resource counts");
        wheat.setText   (r.getString(R.string.res_format, players[turn].getWheat()));
        wood.setText    (r.getString(R.string.res_format, players[turn].getWood()));
        ore.setText     (r.getString(R.string.res_format, players[turn].getOre()));
        brick.setText   (r.getString(R.string.res_format, players[turn].getBrick()));
        sheep.setText   (r.getString(R.string.res_format, players[turn].getSheep()));
    }

    private void setupTouchListener()
    {
        view.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touch_x = (int) event.getX();
                    touch_y = (int) event.getY();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (event.getPointerCount() == 2) {
                        // do a pinch-to-zoom
                        zooming = true;
                        int x1 = (int) event.getX(0), x2 = (int) event.getX(1);
                        int y1 = (int) event.getY(0), y2 = (int) event.getY(1);
                        int distance = (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
                        if (pinch_distance > 0) {
                            resize((distance - pinch_distance) / 2);
                            view.invalidate();
                        }
                        pinch_distance = distance;
                    } else if (!zooming) {
                        int drag_x = (int) event.getX(), drag_y = (int) event.getY();
                        int dx = Math.abs(drag_x - touch_x), dy = Math.abs(drag_y - touch_y);
                        if (moving || (dx > dist_to_begin_move || dy > dist_to_begin_move)) {
                            move(drag_x - touch_x, drag_y - touch_y);
                            view.invalidate();
                            moving = true;
                            touch_x = drag_x;
                            touch_y = drag_y;
                        }
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // touch released. if this wasn't a move or zoom...
                    if (!moving && !zooming) {
                        Point_XY click = new Point_XY(event.getX(), event.getY());
                        Point_QR hex = click.toHex(board.getCenter(), board.getClickableSize());
                        if (board.isValid(hex)) {
                            // if the click is in the region of the board
                            click(hex);
                            view.invalidate();
                        }
                    }
                    moving = zooming = false;
                    pinch_distance = 0;
                }
                // the boolean return value indicates whether the click event has been "consumed" or not
                // a false will let the event continue to trigger any other listeners it can
                return true;
            }
        });
    }

    public String toJSON()
    {
        if(debug) System.out.println("JSON Testing gson de/serialization");
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeHierarchyAdapter(Shape.class, new ShapeAdapter())
                .setPrettyPrinting()
                .create();

        String b_json = gson.toJson(this, Game.class);
        if(debug) System.out.println("JSON Board JSON   :\n " + b_json);

        return b_json;
    }

    public static Gson getGson()
    {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeHierarchyAdapter(Shape.class, new ShapeAdapter())
                .setPrettyPrinting()
                .create();
    }
    public void printBoard()
    {
        System.out.println(board);
    }
}
