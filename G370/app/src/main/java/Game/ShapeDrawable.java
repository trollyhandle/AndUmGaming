package Game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Lightweight class to carry only the necessary information from the Board to the View for drawing.
 * Author: Tyler Holland
 */
public class ShapeDrawable {

    private Path s;
    private Paint p;
    private String text;
    private int pt_x, pt_y;
    private int textSize;
    private boolean hasText;

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
        hasText = false;
    }
    public ShapeDrawable(Path shape, int color, String text, Point_XY point, int txt_Size)
    {
        this(shape, color);
        hasText = true;
        textSize = txt_Size/4;
        this.text = text;
        pt_x = point.x();
        pt_y = point.y();
    }

    public void draw(Canvas c)
    {
        c.drawPath(s, p);

        if (hasText && !text.equals("0")) {
            p.setTextSize(textSize);
            int def_color = p.getColor();
            p.setColor((text.equals("6") || text.equals("8"))? Game.TEXT_COLORS.RED.col: Game.TEXT_COLORS.WHITE.col);
            if (text.length() == 2)
                c.drawText(text, pt_x-(textSize/2), pt_y+(textSize/3), p);
            else
                c.drawText(text, pt_x-(textSize/3), pt_y+(textSize/3), p);
            p.setColor(def_color);
        }
    }
}
