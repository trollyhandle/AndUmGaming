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
            Point_XY offset = new Point_XY(event.getX() - board.getCenter().x(),
                                           event.getY() - board.getCenter().y());
            if(debug)System.out.println("CLICK: Hex    coordinates: " +
                    click.toHex(board.getCenter(), board.getClickableSize()));
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (event.getHistorySize() > 0) {
                Point_XY start = new Point_XY(event.getHistoricalAxisValue(MotionEvent.AXIS_X, 0),
                        event.getHistoricalAxisValue(MotionEvent.AXIS_Y, 0));
                Point_XY end = new Point_XY(event.getX(), event.getY());

                board.move(end.x()-start.x(), end.y()-start.y());
                invalidate();
                if(debug)System.out.printf("DRAG : start - end - delta: %1s, %2s, (%3$3d, %4$3d)\n",
                        start, end, end.x() - start.x(), end.y() - start.y());
            }
            // else the "click" hasn't moved since last event
        }
        // the boolean return value indicates whether the click event has been "consumed" or not
        // a false will let the event continue to trigger any other listeners it can
        return true;
    }

}