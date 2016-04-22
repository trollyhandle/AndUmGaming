package Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/**
 * BoardView.java
 * Author: Tyler Holland
 * Container view for the Board interface.
 */

public class BoardView extends View {
    private Path mDrawable;
    private Paint paint;

    public BoardView(Context context) {
        super(context);
        mDrawable = new Path();
        paint = new Paint();
        paint.setColor(0xffffffff);
    }

    public void setDrawable(Path display)
    {
        mDrawable = display;
    }


    protected void onDraw(Canvas canvas) {
        System.out.println("VIEW drawing");
        canvas.drawPath(mDrawable, paint);
    }

}