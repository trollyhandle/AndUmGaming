package Game;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import android.view.View;

/**
 * BoardView.java
 * Author: Tyler Holland
 * Container view for the Board interface.
 */

public class BoardView extends View {

    private Board board;
    private ShapeDrawable[] shapes;

    private final boolean debug = true;

    public BoardView(Context context) {
        super(context);
    }
    public BoardView(Context context, Board board) {
        this(context);
        this.board = board;

    }

    public void setBoard(Board board) { this.board = board; }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(debug)System.out.println("VIEW drawing");
        shapes = board.getShapeDrawables();
        for (ShapeDrawable s : shapes) {
            if (s != null)
                s.draw(canvas);
        }
    }

    // NOW IN Game.java
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            touch_x = (int)event.getX(); touch_y = (int)event.getY();
//        }
//        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            if (event.getPointerCount() == 2) {
//                // do a pinch-to-zoom
//                zooming = true;
//                int x1 = (int) event.getX(0);
//                int x2 = (int) event.getX(1);
//                int y1 = (int) event.getY(0);
//                int y2 = (int) event.getY(1);
//                int distance = (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
//                if (pinch_distance > 0) {
//                    board.resize((distance - pinch_distance) / 2);
//                    invalidate();
//                    if (debug) System.out.printf("ZOOM : %1$3d to %2$3d : CHANGE: %3$d\n",
//                            distance, pinch_distance, distance - pinch_distance);
//                    if (debug) System.out.printf("ZOOM : resized board to %1$d\n", board.getHexSize());
//
//                }
//                pinch_distance = distance;
//
//            } else if (!zooming){
//                int drag_x = (int)event.getX(), drag_y = (int)event.getY();
//                int dx = Math.abs(drag_x - touch_x), dy = Math.abs(drag_y - touch_y);
//                if (moving || (dx > dist_to_begin_move && dy > dist_to_begin_move)) {
//                    board.move(drag_x - touch_x, drag_y - touch_y);
//                    moving = true;
//                    invalidate();
//                    if (debug) System.out.printf("DRAG : start - end - delta: " +
//                                    "(%1$4d, %2$4d), (%3$4d, %4$4d), (%5$3d, %6$3d)\n",
//                            touch_x, touch_y, drag_x, drag_y, drag_x - touch_x, drag_y - touch_y);
//                    touch_x = drag_x;
//                    touch_y = drag_y;
//                }
//            }
//        }
//        else if (event.getAction() == MotionEvent.ACTION_UP) {
//            // touch released. if this wasn't a move or zoom...
//            if (!moving && !zooming) {
//                Point_XY click = new Point_XY(event.getX(), event.getY());
//                Point_QR hex = click.toHex(board.getCenter(), board.getClickableSize());
//                // if the click is in the region of the board
//                if (board.isValid(hex)) {
//                    if(debug)
//                        if ((hex.q()-hex.r())%3 == 0)System.out.println("CLICK: Hex    coordinates: " +
//                                click.toHex(board.getCenter(), board.getClickableSize()));
//                        else System.out.println("CLICK: VERTEX coordinates: " +
//                                click.toHex(board.getCenter(), board.getClickableSize()));
//
//                    board.buildSettlement(hex.q(), hex.r(), player_turn);
//                    invalidate();
//                }
//            }
//            moving = false;
//            zooming = false;
//            pinch_distance = 0;
//        }
//        // the boolean return value indicates whether the click event has been "consumed" or not
//        // a false will let the event continue to trigger any other listeners it can
//        return true;
//    }

}
