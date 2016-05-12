package Game;

import com.google.gson.annotations.Expose;

/**
 * Point_QR.java
 * Author: Tyler Holland
 * Represents an axial point in the hexagonal grid.
 */
public class Point_QR {

    // TO SERIALIZE
    @Expose
    private int q;
    @Expose
    private int r;

    public Point_QR() { q = r = 0; }

    public Point_QR(int q, int r)
    {
        this.q = q;
        this.r = r;
    }

    public int q() { return q; }
    public int r() { return r; }

    // Calculates the (x, y) coordinates of this (q, r) point, given a centerpoint and the size of
    // the hexagons
    public Point_XY toPixel(Point_XY center, int size)
    {
        int x = (int)(size * (r + q/2.));
        int y = (int)(q * size * Math.sqrt(3) / 2);
        return new Point_XY(x + center.x(), y + center.y());
    }

    public String toString()
    {
        return String.format("(%1$2d,%2$2d)", q, r);
    }

    public boolean equals(Point_QR other) {
        return q == other.q && r == other.r;
    }

}
