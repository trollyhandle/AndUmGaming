package Game;

import android.graphics.Path;

import com.google.gson.annotations.Expose;

/**
 * Shape.java
 * Author: Tyler Holland
 * Abstract class. Parent to Vertex and Hexagon
 */
public abstract class Shape {

    // TO SERIALIZE
    @Expose
    protected Point_QR coord;
    @Expose
    protected String type;

    // NOT SERIALIZE
    protected Path path;

    private final static int[][] directions = {  // rotates CCW
            {0,  1}, {-1,  1}, {-1, 0},
            {0, -1}, { 1, -1}, { 1, 0},
    };

    public Shape(int q, int r)
    {
        coord = new Point_QR(q, r);
        path = new Path();
    }

    // gives the (q, r) coordinates of the neighbor Shape
    // param: dir A direction from 0 to 5, starting to the right and going CCW
    public Point_QR getNeighbor(int dir)
    {
        int neighbor_q = coord.q() + directions[dir][0];
        int neighbor_r = coord.r() + directions[dir][1];
        return new Point_QR(neighbor_q, neighbor_r);
    }

    public Point_QR getCoord() {
        return coord;
    }

    public Path getPath() { return path; }

    public abstract void updatePath(int hex_size, Point_XY boardCenter);

//    public String type() { return type = "shape"; }
    public String type() { return "shape"; }

    public String toString() { return "" + coord; }
//    public String toString()
//    {
//        return type() + ":" + coord + "";
//    }


}
