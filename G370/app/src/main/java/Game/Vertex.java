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
    private int owner;

    public Vertex(int q, int r)
    {
        this(q, r, 10, new Point_XY(0,0));
    }
    public Vertex(int q, int r, int hex_size, Point_XY board_center)
    {
        super(q, r, hex_size, board_center);
        poly_size = hex_size / 4;
        owner = 0;
    }

    public String type() { return "Vertex"; }

    public void setHexSize(int hex_size)
    {
        super.setHexSize(hex_size);
        poly_size = hex_size / 4;
    }
    public void makeDrawable()
    {
        Point_XY shape_center = boardCenter.jump_hex(coord.q(), coord.r(), hex_size);
        Point_XY pt = shape_center.jump_linear(330, poly_size);

        path = new Path();
        path.addCircle(shape_center.x(), shape_center.y(), 4, Path.Direction.CCW);
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

    public int getOwner() { return owner; }
    public boolean isOwned() { return owner != 0; }
    public void setOwner(int player) { owner = player; }

}
