package Game;

import android.content.Context;
import android.graphics.Canvas;

import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * BoardView.java
 * Author: Tyler Holland
 * Container view for the Board interface.
 */

public class BoardView extends View {

    private Board board;
    private ShapeDrawable[] shapes;

    private final boolean debug = false;

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


}
