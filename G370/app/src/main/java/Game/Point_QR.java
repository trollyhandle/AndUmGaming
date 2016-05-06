package Game;

/**
 * Point_QR.java
 * Author: Tyler Holland
 * Represents an axial point in the hexagonal grid.
 */
public class Point_QR {

    private int q, r;

    public Point_QR(int q, int r)
    {
        this.q = q;
        this.r = r;
    }

    public int q() { return q; }
    public int r() { return r; }


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
    public String serialize()
    {
        String json = "\"pointqr\":";
        json += "{\"q\":" + q + ",\"r\":" + r + "}}";
        return json;
    }
    public static Point_QR deserialize(String json)
    {
        return null;
    }
}
