package Game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Tyler on 5/1/16.
 */
public class ShapeDrawable {

    private Path s;
    private Paint p;

    public ShapeDrawable()
    {
        s = new Path();
        s.addCircle(0, 0, 10, Path.Direction.CCW);
        p = new Paint();
        p.setColor(0xffffffff);  // all white
    }
    public ShapeDrawable(Path shape, int color)
    {
        s = shape;
        p = new Paint();
        p.setColor(color);
    }

    public void draw(Canvas c)
    {
        c.drawPath(s, p);
    }
}
