package Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.*;
import android.view.View;

/**
 * BoardView.java
 * Author: Tyler Holland
 * Container view for the Board interface.
 */

public class BoardView extends View {
    private ShapeDrawable mDrawable;

    public BoardView(Context context) {
        super(context);

        int x = 10;
        int y = 10;
        int width = 300;
        int height = 50;

        mDrawable = new ShapeDrawable(new OvalShape());
        mDrawable.getPaint().setColor(0xffffffff);
        mDrawable.setBounds(x, y, x + width, y + height);
    }

    public void setSize(int x, int y, int width, int height)
    {
        mDrawable.setBounds(x, y, x + width, y + height);
    }

    public void setDrawable(ShapeDrawable display)
    {
        mDrawable = display;
    }


    protected void onDraw(Canvas canvas) {
        mDrawable.draw(canvas);
    }
}