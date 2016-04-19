package Game;

/**
 * Point.java
 * Author: Tyler Holland
 * Represents a coordinate point. Simple container class.
 */
public class Point {

    private int x ,y;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public Point jump_linear(int degrees, int distance)
    {
        int new_x = x + (int)(distance * Math.cos(Math.toRadians(degrees)));
        int new_y = x + (int)(distance * Math.cos(Math.toRadians(degrees)));
        return new Point(new_x, new_y);
    }

    public Point jump_hex(int q, int r, int hex_size)
    {
        int qr_x = (r * hex_size) + (q * hex_size / 2);  // q * sin(30)
        int qr_y = (int)(q * hex_size * Math.sqrt(3)/2);  // q * cos(30)
        return new Point(x + qr_x, y + qr_y);
    }
}
