package Game;

/**
 * Point.java
 * Author: Tyler Holland
 * Represents a coordinate point. Simple container class.
 */
public class Point {

    private double x ,y;

    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public Point jump_linear(int degrees, double distance)
    {
        double new_x = x + (distance * Math.cos(Math.toRadians(degrees)));
        double new_y = x + (distance * Math.cos(Math.toRadians(degrees)));
        return new Point(new_x, new_y);
    }


}
