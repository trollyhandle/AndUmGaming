package Game;

/**
 * Shape.java
 * Author: Tyler Holland
 * Abstract class. Parent to Vertex and Hexagon
 */
public class Shape {

    protected int q, r;
    protected Point center;

    private static int[][] directions = {
            { 1, 0}, { 1, -1}, {0, -1},
            {-1, 0}, {-1,  1}, {0,  1}  };

    public Shape(int q, int r)
    {
        this.q = q;
        this.r = r;
        center = new Point(0, 0);
    }

    public Point getNeighbor(int dir)
    {
        int neighbor_q = q + directions[dir][0];
        int neighbor_r = r + directions[dir][1];
        return new Point(neighbor_q, neighbor_r);
    }

    public String toString()
    {
        return String.format("(%1$2d,%2$2d)", q, r);
    }
}
