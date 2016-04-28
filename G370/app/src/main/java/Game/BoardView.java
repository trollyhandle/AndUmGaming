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
        System.out.println("VIEW drawing");
        canvas.drawPath(board.getPath(), paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Point_XY click = new Point_XY(event.getX(), event.getY());
            Point_XY offset = new Point_XY(event.getX() - board.getCenter().x(),
                    event.getY() - board.getCenter().y());
            System.out.println("CLICK: Touch  coordinates: " + click);
            System.out.println("     : Offset coordinates: " + offset);
            System.out.println("     : Hex    coordinates: " +
                    click.toHex(board.getCenter(), board.getClickableSize()));
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            Point_XY click = new Point_XY(event.getX(), event.getY());
            Point_XY offset = new Point_XY(event.getRawX(), event.getRawY());
            System.out.println("DRAG : Source coordinates: " + click);
            System.out.println("     : Final  coordinates: " + offset);
            System.out.printf ("     : Delta  coordinates: " +
                    Point_XY.point_format(offset.x()-click.x(), offset.y()-click.y()));
        }
        // the boolean return value indicates whether the click event has been "consumed" or not
        // this view is getting first dibs on the click, so let others (i.e. zoom buttons) have a
        // chance to shine by returning false
        return false;
    }

}