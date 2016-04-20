package Game;

import android.graphics.drawable.ShapeDrawable;

/**
 * Vertex.java
 * Author: Tyler Holland
 * Represents a vertex in the game board
 */
public class Vertex extends Shape {
//    NOTE: inherited member variables:
//    protected Point_QR coord;
//    protected Point_XY center;
//    protected int size;

    public Vertex(int q, int r)
    {
        super(q, r);
        poly_size = hex_size / 4;
    }

    public String type() { return "Vertex"; }





}
