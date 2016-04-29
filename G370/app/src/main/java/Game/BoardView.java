package Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import android.view.View;

/**
 * BoardView.java
 * Author: Tyler Holland
 * Container view for the Board interface.
 */

public class BoardView extends View {

    private Paint paint;
    private Board board;

    private int touch_x, touch_y;
    private boolean debug = true;

    public BoardView(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(0xffffffff);
    }
    public BoardView(Context context, Board board) {
        super(context);
        paint = new Paint();
        paint.setColor(0xffffffff);
        this.board = board;
    }

    public void setBoard(Board board) { this.board = board; }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(debug)System.out.println("VIEW drawing");
        canvas.drawPath(board.getPath(), paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Point_XY click = new Point_XY(event.getX(), event.getY());
            if(debug)System.out.println("CLICK: Hex    coordinates: " +
                    click.toHex(board.getCenter(), board.getClickableSize()));
            touch_x = click.x(); touch_y = click.y();
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int drag_x = (int)event.getRawX(), drag_y = (int)event.getRawY();
            board.move(drag_x - touch_x, drag_y - touch_y);
            invalidate();
            if(debug)System.out.printf("DRAG : start - end - delta: " +
                            "(%1$4d, %2$4d), (%3$4d, %4$4d), (%5$3d, %6$3d)\n",
                    touch_x, touch_y, drag_x, drag_y, drag_x - touch_x, drag_y - touch_y);
            touch_x = drag_x; touch_y = drag_y;
        }
        // the boolean return value indicates whether the click event has been "consumed" or not
        // a false will let the event continue to trigger any other listeners it can
        return true;
    }

}