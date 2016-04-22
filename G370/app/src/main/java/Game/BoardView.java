package Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.*;
import android.view.View;

/**
 * BoardView.java
 * Author: Tyler Holland
 * Container view for the Board interface.
 */

public class BoardView extends View {
    private Path mDrawable;
    private Paint paint;
    private Rect bounds;

    public BoardView(Context context) {
        super(context);

        bounds = new Rect(10, 10, 300, 50);

        System.out.println("VIEW Creating OVAL drawable");
//        mDrawable = new ShapeDrawable(new OvalShape());
        mDrawable = new Path();
        paint = new Paint();
        paint.setColor(0xffffffff);
//        mDrawable.setBounds(bounds);
    }

    public void setBounds(int x, int y, int width, int height)
    {
        bounds = new Rect(x, y, x+width, y+height);
//        mDrawable.setBounds(bounds);
    }

    public void setDrawable(Path display)
    {
        System.out.println("VIEW setting drawable");
        mDrawable = display;
//        mDrawable.setBounds(bounds);
    }


    protected void onDraw(Canvas canvas) {
        System.out.println("VIEW drawing");
//        mDrawable.draw(canvas, paint);
        canvas.drawPath(mDrawable, paint);
    }



}