package Game;

import android.graphics.Path;

/**
 * Hexagon.java
 * Author: Tyler Holland
 * Represents a hexagon in the game board
 */
public class Hexagon extends Shape {
//    NOTE: inherited member variables:
//    protected Point_QR coord;
//    protected Point_XY boardCenter;
//    protected int hex_size;
//    protected int poly_size;
//    protected Path path;

    public Hexagon(int q, int r)
    {
        super(q, r);
        poly_size = (int)(hex_size / 1.3);
    }

    public String type() { return "Hexagon"; }

    public void makeDrawable()
    {
        Point_XY shape_center = boardCenter.jump_hex(coord.q(), coord.r(), hex_size);
        Point_XY pt = shape_center.jump_linear(0, poly_size);

        path = new Path();
        path.addCircle(shape_center.x(), shape_center.y(), 12, Path.Direction.CCW);
        path.moveTo(pt.x(), pt.y());

        for (int i = 0; i <= 360; i += 60) {
            pt = shape_center.jump_linear(i, poly_size);
            path.lineTo(pt.x(), pt.y());
        }
        path.close();
    }

}
