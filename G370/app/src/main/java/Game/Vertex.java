package Game;

import android.graphics.Path;
import java.util.ArrayList;

/**
 * Vertex.java
 * Author: Tyler Holland
 * Represents a vertex in the game board
 */
public class Vertex extends Shape {
//    NOTE: inherited member variables:
//    Point_QR coord;
//    Point_XY boardCenter;
//    int hex_size;
//    int poly_size;
//    Path path;

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

    public Edge[] generateEdges()
    {
        if (((coord.q() - coord.r()) + 1) % 3 == 0) {
            return new Edge[] {
                    new Edge(coord, getNeighbor(1)),
                    new Edge(coord, getNeighbor(1)),
                    new Edge(coord, getNeighbor(1))};
        }
        return null;
    }

}
