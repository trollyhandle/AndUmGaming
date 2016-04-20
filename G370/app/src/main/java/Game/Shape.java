package Game;

/**
 * Shape.java
 * Author: Tyler Holland
 * Abstract class. Parent to Vertex and Hexagon
 */
public abstract class Shape {

    protected Point_QR coord;
    protected Point_XY center;
    protected int size;

    private static int[][] directions = {
            { 1, 0}, { 1, -1}, {0, -1},
            {-1, 0}, {-1,  1}, {0,  1}  };

    public Shape(int q, int r)
    {
        coord = new Point_QR(q, r);
        center = new Point_XY(0, 0);
        size = 1;
    }


    public Point_QR getNeighbor(int dir)
    {
        int neighbor_q = coord.q() + directions[dir][0];
        int neighbor_r = coord.r() + directions[dir][1];
        return new Point_QR(neighbor_q, neighbor_r);
    }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    
    public Point_XY getCenter() { return center; }
    public void setCenter(Point_XY newCenter) { center = newCenter; }


    public abstract String type();
    public String toString()
    {
//        return String.format("(%1$2d,%2$2d)", coord.q(), coord.r());
        return "" + coord;
    }
//    public String toString()
//    {
//        String str = this.type();
//        str += "(" + coord + "), size " + size;
//        return str;
//    }
}
