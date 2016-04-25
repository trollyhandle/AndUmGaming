package Game;

import android.graphics.Path;

/**
 * Vertex.java
 * Author: Tyler Holland
 * Represents a vertex in the game board
 */
public class Vertex extends Shape {
//    NOTE: inherited member variables:
//    protected Point_QR coord;
//    protected Point_XY boardCenter;
//    protected int hex_size;
//    protected int poly_size;
//    protected Path path;

    public Vertex(int q, int r)
    {
        super(q, r);
        poly_size = hex_size / 4;
    }

    public String type() { return "Vertex"; }

    public void makeDrawable()
    {
        Point_XY shape_center = boardCenter.jump_hex(coord.q(), coord.r(), hex_size);
        Point_XY pt = shape_center.jump_linear(330, poly_size);

        path = new Path();
        path.addCircle(shape_center.x(), shape_center.y(), 8, Path.Direction.CCW);
        path.moveTo(pt.x(), pt.y());

        for (int i = 30; i <= 360; i += 60) {
            pt = shape_center.jump_linear(i, poly_size);
            path.lineTo(pt.x(), pt.y());
        }
        path.close();
    }
}
