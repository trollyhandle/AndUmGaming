package Game;

import android.app.Activity;
import android.content.res.Resources;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.andumgaming.g370.R;


/**
 * Board and Player manager class.
 * Author: Tyler Holland
 */
public class Game {
    private boolean debug = true;

    // RESOURCE PAINT_COLOR LOOKUP
    public enum RESOURCES {
        BLANK   (0xffffffff), WHEAT   (0xffFFDF00), WOOD    (0xff014421),
        ORE     (0xff8A7F80), BRICK   (0xffCB4154), SHEEP   (0xff98FF98);
        private int col; RESOURCES(int color) { col = color; }
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
        NONE (0xffFFFFFF)/*WHITE*/,
        ONE (0xffFF0800)/*RED*/,    TWO(0xff00FF00)/*GREEN*/,
        THREE(0xff1C1CF0)/*BLUE*/,  FOUR(0xffBF00FF)/*VIOLET*/;
        private int col; PLAYERS(int value) { col = value; }
        static int getColor(int i) { switch(i) {
            case 1: return ONE.col; case 2: return TWO.col;
            case 3: return THREE.col; case 4: return FOUR.col;
        } return NONE.col; }
    }

    public enum BUILD {
        NONE, ROAD, SETTLEMENT, CITY; //, KNIGHT;
    }

    private BUILD getBuild;


    private BUILD build;
    private Point_QR firstRoadPt;
    private int turn;

    private Player[] players;
    private TextView wheat, wood, ore, brick, sheep;


    private Board board;

    private BoardView view;
    private int width, height;
    private int default_hexsize;
    private Point_XY default_center;
    private Resources r;

    private final int dist_to_begin_move = 10;
    private int touch_x, touch_y, pinch_distance;
    private boolean moving, zooming;


    ////////////////////
    public BUILD getBuild(){
        return build;
    }

    public Game(Activity parent, int width, int height)
    {
        r = parent.getResources();
        this.width = width; this.height = height;
        // width/9 is a good initial hex size.
        // I determined this after multiple iterations of a complex modeling algorithm...
        //      just kidding, guess and check!
        default_hexsize = width / 9;
        default_center = new Point_XY(width/2, height/2);

        if(debug)System.out.println("GAME creating Board");
        if(debug)System.out.printf("GAME center at (%1$2d,%2$2d)\n", width / 2, height / 2);
        board = new Board(default_hexsize, default_center);

        if(debug)System.out.println("GAME creating BoardView");
        view = new BoardView(parent, board);

        turn = 0;
        build = BUILD.NONE;

        initResourceTabs(parent);
        setupTouchListener();
        System.out.println(board);
        // TODO initialize players[], and at some point (maybe not here) call to server
        if (debug) System.out.println(board);
    }




    public void nextTurn()
    {
        turn = turn == 4? 1: turn + 1;
        build = BUILD.NONE;

        // set resources to the current player's stats
//        refreshResourceCounts();  // dont do this till players are implemented

        // set cards too

    }

    public int getTurn() { return turn; }

    public void setBuildState(BUILD type) {
        build = type;
        firstRoadPt = null; // just to make sure
    }
    public void click(Point_QR hex) {
        // this is where decisions will be made
        if (debug) System.out.println("GAME click at " + hex);
        if (debug) System.out.println("GAME build state: " + build);

        // are we placing a settlement?
        if (build == BUILD.SETTLEMENT) {
            board.buildSettlement(hex.q(), hex.r(), turn);
        }
        // are we placing a city?
        else if (build == BUILD.CITY) {
            board.buildCity(hex.q(), hex.r(), turn);
        }

//        // are we placing a road?
        else if (build == BUILD.ROAD) {
            if (firstRoadPt == null) // no source selected yet
                firstRoadPt = hex;
            else {
                if (debug) System.out.println("Road from " + firstRoadPt + " to " + hex);
                board.buildRoad(firstRoadPt, hex, turn);
                firstRoadPt = null;  // reset for next road
            }
        }
        // are we playing a card?
        // are we moving the robber?
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
                            board.resize((distance - pinch_distance) / 2);
                            view.invalidate();
                        }
                        pinch_distance = distance;
                    } else if (!zooming) {
                        int drag_x = (int) event.getX(), drag_y = (int) event.getY();
                        int dx = Math.abs(drag_x - touch_x), dy = Math.abs(drag_y - touch_y);
                        if (moving || (dx > dist_to_begin_move || dy > dist_to_begin_move)) {
                            board.move(drag_x - touch_x, drag_y - touch_y);
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
}
