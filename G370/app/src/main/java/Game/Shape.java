package Game;

import android.graphics.Path;

/**
 * Shape.java
 * Author: Tyler Holland
 * Abstract class. Parent to Vertex and Hexagon
 */
public abstract class Shape {

    protected Point_QR coord;
    protected Point_XY boardCenter;
    protected int hex_size;
    protected int poly_size;
    protected Path path;

    private boolean debug = false;

    private static int[][] directions = {
            { 1, 0}, { 1, -1}, {0, -1},
            {-1, 0}, {-1,  1}, {0,  1}  };

    
    public Shape(int q, int r)
    {
        coord = new Point_QR(q, r);
        boardCenter = new Point_XY(0, 0);
        hex_size = 30;
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
        if(debug)System.out.println("SHAPE polygon size: " + poly_size + " (" + type() + ")");
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
    
}
