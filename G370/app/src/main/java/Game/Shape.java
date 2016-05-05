package Game;

import android.graphics.Path;

/**
 * Shape.java
 * Author: Tyler Holland
 * Abstract class. Parent to Vertex and Hexagon
 */
public abstract class Shape {
    protected Point_QR coord;

    protected int hex_size;
    protected Point_XY boardCenter;
    protected Path path;
    private boolean debug = false;

    private static int[][] directions = {  // rotates CCW
            {0,  1}, {-1,  1}, {-1, 0},
            {0, -1}, { 1, -1}, { 1, 0},
    };
    
    public Shape(int q, int r)
    {
        this(q, r, 30, new Point_XY(0, 0));
    }
    public Shape(int q, int r, int hex_size, Point_XY board_center)
    {
        coord = new Point_QR(q, r);
        boardCenter = board_center;
        this.hex_size = hex_size;
    }


    public Point_QR getNeighbor(int dir)
    {
        int neighbor_q = coord.q() + directions[dir][0];
        int neighbor_r = coord.r() + directions[dir][1];
        return new Point_QR(neighbor_q, neighbor_r);
    }

    public int getHexSize() { return hex_size; }
    public void setHexSize(int hexSize) { this.hex_size = hexSize; }
    
    public void setBoardCenter(Point_XY newCenter) { boardCenter = newCenter; }

    public Path getPath() { return path; }

    public void makeDrawable()
    {
        if(debug)System.out.println("SHAPE Making path...");
        Point_XY shape_center = boardCenter.jump_hex(coord.q(), coord.r(), hex_size);
        if(debug)System.out.println("SHAPE Center at " + shape_center);

        path = new Path();
        path.addCircle(shape_center.x(), shape_center.y(), 6, Path.Direction.CCW);
        if(debug)System.out.println("SHAPE path complete");
    }


    public abstract String type();
    public String toString()
    {
//        String str = this.type();
//        str += "(" + coord + "), hex_size " + hex_size;
//        return str;
        return "" + coord;
    }

    public abstract String serialize();
    public static Shape deserialize(String json)
    {
        return null;
    }


}
